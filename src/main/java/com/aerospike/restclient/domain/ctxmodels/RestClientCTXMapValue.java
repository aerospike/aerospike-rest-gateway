package com.aerospike.restclient.domain.ctxmodels;

import com.aerospike.client.Value;
import com.aerospike.client.cdt.CTX;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.aerospike.restclient.util.deserializers.ObjectDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;

public class RestClientCTXMapValue extends RestClientCTX {
    @Schema(description = "TODOs", allowableValues = AerospikeAPIConstants.MAP_VALUE, required = true)
    @JsonProperty(required = true)
    public final String ctxType = AerospikeAPIConstants.MAP_VALUE;

    @Schema(description = "TODO", required = true)
    @JsonProperty(required = true)
    @JsonDeserialize(using = ObjectDeserializer.class)
    public Object value;

    RestClientCTXMapValue() {};

    public RestClientCTXMapValue(Object value) {
        this.value = value;
    };

    @Override
    public CTX toCTX() {
        Value asVal = Value.get(value);
        return CTX.mapValue(asVal);
    }
}

