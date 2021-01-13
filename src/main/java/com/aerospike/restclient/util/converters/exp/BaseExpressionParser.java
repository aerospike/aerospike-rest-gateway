package com.aerospike.restclient.util.converters.exp;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class BaseExpressionParser<T> {

    protected static final Pattern pattern = Pattern.compile("(\\))|(\\()|([^\\s)(]+)",
            Pattern.CASE_INSENSITIVE);

    protected class NestedBlock {
        Stack<String> simpleOperators;
        Stack<String> logicOperators;
        Stack<String> unaryLogicOperators;
        Stack<String> operands;
        Stack<T> filters;

        NestedBlock() {
            simpleOperators = new Stack<>();
            logicOperators = new Stack<>();
            unaryLogicOperators = new Stack<>();
            operands = new Stack<>();
            filters = new Stack<>();
        }
    }

    protected <U> boolean forAllEqual(Collection<U> list) {
        return new HashSet<>(list).size() <= 1;
    }

    protected List<String> parseExpressionString(String expression) {
        String decoded = new String(Base64.getUrlDecoder().decode(expression), StandardCharsets.UTF_8);
        Matcher matcher = pattern.matcher(decoded);
        List<String> tokens = new ArrayList<>();
        while (matcher.find()) {
            tokens.add(matcher.group());
        }
        return tokens;
    }
}
