package com.aerospike.restclient.domain.querymodels;

public class RestClientCTX {
    enum Type {
        LIST_BY_INDEX,
        LIST_BY_RANK,
        LIST_BY_VALUE,
        MAP_BY_INDEX,
        MAP_BY_RANK,
        MAP_BY_KEY,
        MAP_BY_VALUE,
    }

    Type type;


}
