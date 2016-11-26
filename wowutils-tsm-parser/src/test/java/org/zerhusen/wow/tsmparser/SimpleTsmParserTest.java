package org.zerhusen.wow.tsmparser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

/**
 * Created by stephan on 19.11.16.
 */
public class SimpleTsmParserTest {

    private static final Logger LOGGER = LogManager.getLogger(SimpleTsmParserTest.class);

    @Test
    public void extractItemIdsAgainstPhatLewtsTransmogImportString() throws Exception {
        InputStream resourceAsStream = SimpleTsmParserTest.class.getResourceAsStream("/phats_transmog_list.txt");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(resourceAsStream))) {
            String tsmString = br.readLine();
            List<Long> itemIds = SimpleTsmParser.extractItemIds(tsmString);
            assertThat(itemIds).hasSize(3056);
            LOGGER.info(itemIds.size() + " itemIds extracted");
        }
    }

    @Test
    public void extractItemIds() throws Exception {
        assertThat(SimpleTsmParser.extractItemIds("i:123456"))
                .containsOnly(123456L);
        assertThat(SimpleTsmParser.extractItemIds("i:123456,i:786"))
                .containsOnly(123456L, 786L);

        assertThat(SimpleTsmParser.extractItemIds("group:foo,i:123456"))
                .containsOnly(123456L);
        assertThat(SimpleTsmParser.extractItemIds("group:foo`bar,i:123456,i:786"))
                .containsOnly(123456L, 786L);

        assertThat(SimpleTsmParser.extractItemIds("group:foo,i:123456,group:foo1,i:1234567"))
                .containsOnly(123456L, 1234567L);
        assertThat(SimpleTsmParser.extractItemIds("group:foo,group:foo1,i:1234567"))
                .containsOnly(1234567L);
        assertThat(SimpleTsmParser.extractItemIds("group:foo`bar,i:123456,i:786,group:foo`bar1,i:1234567,i:7865"))
                .containsOnly(123456L, 786L, 1234567L, 7865L);
    }

}