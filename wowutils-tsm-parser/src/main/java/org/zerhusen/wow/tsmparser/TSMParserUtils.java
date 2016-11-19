package org.zerhusen.wow.tsmparser;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by stephan on 02.10.16.
 */
public final class TSMParserUtils {
    private TSMParserUtils() {
    }

    public static TSMParser.RContext parse(String tsmString) {
        ParserErrorListener parserErrorListener = new ParserErrorListener();

        TSMLexer lexer = new TSMLexer(new ANTLRInputStream(tsmString));
        lexer.removeErrorListeners();
        lexer.addErrorListener(parserErrorListener);

        CommonTokenStream tokenStream = new CommonTokenStream(lexer);

        TSMParser parser = new TSMParser(tokenStream);
        parser.removeErrorListeners();
        parser.addErrorListener(parserErrorListener);
        parser.getBuildParseTree();

        return parser.r();
    }

    public static List<Long> extractItemIds(String tsmString) {
        TSMParser.RContext rContext = parse(tsmString);

        TSMParser.ItemsContext itemsContext = rContext.items();
        if (itemsContext != null) {
            return itemsContext.item().stream()
                    .map(TSMParserUtils::getItemIdAsLong)
                    .collect(Collectors.toList());
        } else {
            return rContext.group_expression().stream()
                    .filter(groupExpressionContext -> groupExpressionContext.items() != null)
                    .flatMap(groupExpressionContext -> groupExpressionContext.items().item().stream())
                    .map(TSMParserUtils::getItemIdAsLong)
                    .collect(Collectors.toList());
        }

    }

    private static Long getItemIdAsLong(TSMParser.ItemContext itemContext) {
        return Long.valueOf(itemContext.item_id().getText());
    }

}
