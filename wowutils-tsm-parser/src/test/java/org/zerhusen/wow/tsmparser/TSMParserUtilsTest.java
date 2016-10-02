package org.zerhusen.wow.tsmparser;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 * Created by stephan on 02.10.16.
 */
public class TSMParserUtilsTest {

    @Test
    public void parse() throws Exception {
        try {
            TSMParserUtils.parse("invalid");
            fail("InvalidTSMStringExpression should be thrown here");
        } catch (InvalidTSMStringExpression e) {
            // do nothing
        }

        try {
            TSMParserUtils.parse("");
            fail("InvalidTSMStringExpression should be thrown here");
        } catch (InvalidTSMStringExpression e) {
            // do nothing
        }

        assertThat(TSMParserUtils.parse("i:123456")).isNotNull();
        assertThat(TSMParserUtils.parse("i:123456,i:786")).isNotNull();

        assertThat(TSMParserUtils.parse("group:foo")).isNotNull();
        assertThat(TSMParserUtils.parse("group:foo` World")).isNotNull();
        assertThat(TSMParserUtils.parse("group:foo` World,group:foo` World")).isNotNull();

        assertThat(TSMParserUtils.parse("group:foo,i:123456")).isNotNull();
        assertThat(TSMParserUtils.parse("group:foo`bar,i:123456")).isNotNull();
        assertThat(TSMParserUtils.parse("group:foo`bar,i:123456,i:786")).isNotNull();
        assertThat(TSMParserUtils.parse("group:foo`bar`Hello World,i:123456,i:786")).isNotNull();
        assertThat(TSMParserUtils.parse("group:foo`200-300,i:123456")).isNotNull();
        assertThat(TSMParserUtils.parse("group:foo`bar 200-300,i:123456")).isNotNull();

        assertThat(TSMParserUtils.parse("group:foo,i:123456,group:foo,i:123456")).isNotNull();
        assertThat(TSMParserUtils.parse("group:foo`bar,i:123456,group:foo`bar,i:123456")).isNotNull();
        assertThat(TSMParserUtils.parse("group:foo`bar,i:123456,i:786,group:foo`bar,i:123456,i:786")).isNotNull();
        assertThat(TSMParserUtils.parse("group:foo`bar`Hello World,i:123456,i:786,group:foo`bar`Hello World,i:123456,i:786")).isNotNull();
        assertThat(TSMParserUtils.parse("group:foo`200-300,i:123456,group:foo`200-300,i:123456")).isNotNull();
        assertThat(TSMParserUtils.parse("group:foo`bar 200-300,i:123456,group:foo`bar 200-300,i:123456")).isNotNull();
    }

    @Test
    public void parseAgainstPhatLewtsTransmogImportString() throws Exception {
        InputStream resourceAsStream = TSMParserUtilsTest.class.getResourceAsStream("/phats_transmog_list.txt");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(resourceAsStream))) {
            String tsmString = br.readLine();
            List<Long> itemIds = TSMParserUtils.extractItemIds(tsmString);
            assertThat(itemIds).isNotEmpty();
            System.out.println(itemIds.size() + " itemIds extracted");
        }
    }

    @Test
    public void extractItemIds() throws Exception {
        assertThat(TSMParserUtils.extractItemIds("i:123456"))
                .containsOnly(123456L);
        assertThat(TSMParserUtils.extractItemIds("i:123456,i:786"))
                .containsOnly(123456L, 786L);

        assertThat(TSMParserUtils.extractItemIds("group:foo,i:123456"))
                .containsOnly(123456L);
        assertThat(TSMParserUtils.extractItemIds("group:foo`bar,i:123456,i:786"))
                .containsOnly(123456L, 786L);

        assertThat(TSMParserUtils.extractItemIds("group:foo,i:123456,group:foo1,i:1234567"))
                .containsOnly(123456L, 1234567L);
        assertThat(TSMParserUtils.extractItemIds("group:foo,group:foo1,i:1234567"))
                .containsOnly(1234567L);
        assertThat(TSMParserUtils.extractItemIds("group:foo`bar,i:123456,i:786,group:foo`bar1,i:1234567,i:7865"))
                .containsOnly(123456L, 786L, 1234567L, 7865L);
    }

}