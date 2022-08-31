package com.aerospike.restclient.domain.querymodels;

import com.aerospike.client.query.Filter;
import com.aerospike.restclient.domain.ctxmodels.*;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "filterType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = RestClientQueryBinEqualFilter.class, name = AerospikeAPIConstants.QueryFilterTypes.EQUAL),
        @JsonSubTypes.Type(value = RestClientQueryBinRangeFilter.class, name = AerospikeAPIConstants.QueryFilterTypes.RANGE),
        @JsonSubTypes.Type(value = RestClientQueryBinContainsFilter.class, name = AerospikeAPIConstants.QueryFilterTypes.CONTAINS),
        @JsonSubTypes.Type(value = RestClientQueryBinGeoWithinRegionFilter.class, name = AerospikeAPIConstants.QueryFilterTypes.GEOWITHIN_REGION),
        @JsonSubTypes.Type(value = RestClientQueryBinGeoWithinRadiusFilter.class, name = AerospikeAPIConstants.QueryFilterTypes.GEOWITHIN_RADIUS),
        @JsonSubTypes.Type(value = RestClientQueryBinGeoContainsPointFilter.class, name = AerospikeAPIConstants.QueryFilterTypes.GEOCONTAINS_POINT),
})
abstract public class RestClientQueryFilter {
    // Must be set in SubType.
    // final public String filterType;

    @Schema(description = "TODO", required = true)
    @JsonProperty(required = true)
    public String binName;

    @Schema(description = "TODO", required = true)
    @JsonProperty(required = true)
    List<RestClientCTX> ctx;

    abstract public Filter toFilter();
}

