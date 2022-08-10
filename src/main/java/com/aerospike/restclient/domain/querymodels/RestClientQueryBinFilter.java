package com.aerospike.restclient.domain.querymodels;

import com.aerospike.client.cdt.CTX;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

abstract public class RestClientQueryBinFilter implements IRestClientQueryFilter {
    @Schema(description = "TODO", required = true)
    @JsonProperty(required = true)
    public String binName;

    @Schema(description = "TODO", required = true)
    @JsonProperty(required = true)
    String[] ctx;
}

