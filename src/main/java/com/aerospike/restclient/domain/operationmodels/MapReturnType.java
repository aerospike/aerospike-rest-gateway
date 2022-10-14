package com.aerospike.restclient.domain.operationmodels;

/*
 * Wraps constants in Aerospike client class MapReturnType
 */
public enum MapReturnType {
    COUNT, INDEX, KEY, KEY_VALUE, NONE, RANK, REVERSE_INDEX, REVERSE_RANK, VALUE, EXISTS;

    public int toMapReturnType(boolean inverted) {
        int invertedFlag = inverted ? com.aerospike.client.cdt.MapReturnType.INVERTED : 0;

        return switch (this) {
            case COUNT -> com.aerospike.client.cdt.MapReturnType.COUNT | invertedFlag;
            case INDEX -> com.aerospike.client.cdt.MapReturnType.INDEX | invertedFlag;
            case KEY -> com.aerospike.client.cdt.MapReturnType.KEY | invertedFlag;
            case KEY_VALUE -> com.aerospike.client.cdt.MapReturnType.KEY_VALUE | invertedFlag;
            case NONE -> com.aerospike.client.cdt.MapReturnType.NONE | invertedFlag;
            case RANK -> com.aerospike.client.cdt.MapReturnType.RANK | invertedFlag;
            case REVERSE_INDEX -> com.aerospike.client.cdt.MapReturnType.REVERSE_INDEX | invertedFlag;
            case REVERSE_RANK -> com.aerospike.client.cdt.MapReturnType.REVERSE_RANK | invertedFlag;
            case VALUE -> com.aerospike.client.cdt.MapReturnType.VALUE | invertedFlag;
            case EXISTS -> com.aerospike.client.cdt.MapReturnType.EXISTS | invertedFlag;
        };
    }
}
