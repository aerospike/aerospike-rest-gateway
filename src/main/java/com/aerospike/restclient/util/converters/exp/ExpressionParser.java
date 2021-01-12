package com.aerospike.restclient.util.converters.exp;

@FunctionalInterface
public interface ExpressionParser<T> {
    T parse(String expression);
}
