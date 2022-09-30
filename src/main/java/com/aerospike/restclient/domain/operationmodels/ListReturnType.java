package com.aerospike.restclient.domain.operationmodels;

public enum ListReturnType {
    COUNT, INDEX, NONE, RANK, REVERSE_INDEX, REVERSE_RANK, VALUE;

    public int toListReturnType(boolean inverted) {
        int invertedFlag = inverted ? com.aerospike.client.cdt.ListReturnType.INVERTED : 0;
        return switch (this) {
            case COUNT -> com.aerospike.client.cdt.ListReturnType.COUNT | invertedFlag;
            case INDEX -> com.aerospike.client.cdt.ListReturnType.INDEX | invertedFlag;
            case NONE -> com.aerospike.client.cdt.ListReturnType.NONE | invertedFlag;
            case RANK -> com.aerospike.client.cdt.ListReturnType.RANK | invertedFlag;
            case REVERSE_INDEX -> com.aerospike.client.cdt.ListReturnType.REVERSE_INDEX | invertedFlag;
            case REVERSE_RANK -> com.aerospike.client.cdt.ListReturnType.REVERSE_RANK | invertedFlag;
            case VALUE -> com.aerospike.client.cdt.ListReturnType.VALUE | invertedFlag;
        };
    }
}
