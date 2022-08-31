package com.aerospike.restclient.domain.ctxmodels;

import com.aerospike.client.cdt.CTX;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "ctxType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = RestClientCTXListIndex.class, name = AerospikeAPIConstants.LIST_INDEX),
        @JsonSubTypes.Type(value = RestClientCTXListRank.class, name = AerospikeAPIConstants.LIST_RANK),
        @JsonSubTypes.Type(value = RestClientCTXListValue.class, name = AerospikeAPIConstants.LIST_VALUE),
        @JsonSubTypes.Type(value = RestClientCTXMapIndex.class, name = AerospikeAPIConstants.MAP_INDEX),
        @JsonSubTypes.Type(value = RestClientCTXMapRank.class, name = AerospikeAPIConstants.MAP_RANK),
        @JsonSubTypes.Type(value = RestClientCTXMapKey.class, name = AerospikeAPIConstants.MAP_KEY),
        @JsonSubTypes.Type(value = RestClientCTXMapValue.class, name = AerospikeAPIConstants.MAP_VALUE),
        @JsonSubTypes.Type(value = RestClientCTXMapKeyCreate.class, name = AerospikeAPIConstants.MAP_KEY_CREATE),
        @JsonSubTypes.Type(value = RestClientCTXListIndexCreate.class, name = AerospikeAPIConstants.LIST_INDEX_CREATE),
})
abstract public class RestClientCTX {
    public final String ctxType = null;
    abstract public CTX toCTX();
}
