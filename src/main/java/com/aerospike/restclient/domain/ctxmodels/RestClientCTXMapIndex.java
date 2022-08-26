package com.aerospike.restclient.domain.ctxmodels;

import com.aerospike.client.cdt.CTX;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public class RestClientCTXMapIndex extends RestClientCTX {
    @Schema(description = "TODO", allowableValues = AerospikeAPIConstants.MAP_INDEX, required = true)
    @JsonProperty(required = true)
    public final String ctxType = AerospikeAPIConstants.MAP_INDEX;

    @Schema(description = "TODO", required = true)
    @JsonProperty(required = true)
    public Integer index;

    RestClientCTXMapIndex() {};

    @Override
    public CTX toCTX() {
        return CTX.mapIndex(index);
    }
}


