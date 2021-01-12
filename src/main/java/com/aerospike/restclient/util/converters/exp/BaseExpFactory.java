package com.aerospike.restclient.util.converters.exp;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class BaseExpFactory {

    protected String stripQuotes(String str) {
        if (str.length() >= 2 && str.charAt(0) == '"' && str.charAt(str.length() - 1) == '"') {
            return str.substring(1, str.length() - 1);
        }
        return str;
    }

    protected List<String> extractParameters(String params) {
        params = params.replaceAll("[()]", "");
        List<String> p = Arrays.asList(params.split(","));
        return p.stream().map(String::trim).collect(Collectors.toList());
    }

    protected Optional<Integer> parseInt(String s) {
        int i;
        try {
            i = Integer.parseInt(s);
        } catch (Exception e) {
            return Optional.empty();
        }
        return Optional.of(i);
    }
}
