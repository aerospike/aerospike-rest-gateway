package com.aerospike.restclient.domain.querymodels;

import com.aerospike.client.cdt.CTX;
import com.aerospike.restclient.domain.ctxmodels.*;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@JsonSubTypes({
        @JsonSubTypes.Type(value = RestClientQueryEqualBinFilter.class, name = "TODO"),
        @JsonSubTypes.Type(value = RestClientQueryRangeBinFilter.class, name = "TODO"),
        @JsonSubTypes.Type(value = RestClientQueryGeoWithinRegionBinFilter.class, name = "TODO"),
        @JsonSubTypes.Type(value = RestClientQueryGeoWithinRadiusBinFilter.class, name = "TODO"),
        @JsonSubTypes.Type(value = RestClientQueryGeoContainsBinFilter.class, name = "TODO"),
})
abstract public class RestClientQueryBinFilter implements IRestClientQueryFilter {
    @Schema(description = "TODO", required = true)
    @JsonProperty(required = true)
    public String filterType;

    @Schema(description = "TODO", required = true)
    @JsonProperty(required = true)
    public String binName;

    @Schema(description = "TODO", required = true)
    @JsonProperty(required = true)
    List<IRestClientCTX> ctx;
}

