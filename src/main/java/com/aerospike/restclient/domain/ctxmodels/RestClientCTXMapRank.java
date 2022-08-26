package com.aerospike.restclient.domain.ctxmodels;

import com.aerospike.client.cdt.CTX;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public class RestClientCTXMapRank extends RestClientCTX {
    @Schema(description = "TODO", allowableValues = AerospikeAPIConstants.MAP_RANK, required = true)
    @JsonProperty(required = true)
    public final String ctxType = AerospikeAPIConstants.MAP_RANK;

    @Schema(description = "TODO", required = true)
    @JsonProperty(required = true)
    public Integer rank;

    RestClientCTXMapRank() {};

    @Override
    public CTX toCTX() {
        return CTX.mapRank(rank);
    }
}
