package com.aerospike.restclient.domain.ctxmodels;

import com.aerospike.client.Value;
import com.aerospike.client.cdt.CTX;
import com.aerospike.client.cdt.ListOrder;
import com.aerospike.client.cdt.MapOrder;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.aerospike.restclient.util.deserializers.ObjectDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;

public class RestClientCTXMapKey extends RestClientCTX {
    @Schema(description = "TODOs", allowableValues = AerospikeAPIConstants.MAP_KEY, required = true)
    @JsonProperty(required = true)
    public final String ctxType = AerospikeAPIConstants.MAP_KEY;

    @Schema(description = "TODO", required = true)
    @JsonProperty(required = true)
    @JsonDeserialize(using = ObjectDeserializer.class)
    public Object key;

    RestClientCTXMapKey() {};

    @Override
    public CTX toCTX() {
        Value asVal = Value.get(key);
        return CTX.mapKey(asVal);
    }
}

