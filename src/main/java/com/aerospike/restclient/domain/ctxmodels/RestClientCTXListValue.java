package com.aerospike.restclient.domain.ctxmodels;

import com.aerospike.client.Value;
import com.aerospike.client.cdt.CTX;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.aerospike.restclient.util.deserializers.ObjectDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;

public class RestClientCTXListValue extends RestClientCTX {
    @Schema(description = "TODO", allowableValues = AerospikeAPIConstants.LIST_VALUE, required = true)
    @JsonProperty(required = true)
    public final String ctxType = AerospikeAPIConstants.LIST_VALUE;

    @Schema(description = "TODO", required = true)
    @JsonProperty(required = true)
    @JsonDeserialize(using = ObjectDeserializer.class)
    public Object value;

    RestClientCTXListValue() {};

    @Override
    public CTX toCTX() {
        Value asVal = Value.get(value);
        return CTX.listValue(asVal);
    }
}

