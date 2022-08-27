package com.aerospike.restclient.domain.querymodels;

import com.aerospike.restclient.domain.ctxmodels.*;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "filterType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = RestClientQueryBinEqualFilter.class, name = AerospikeAPIConstants.QueryFilterTypes.QUERY_FILTER_TYPE_EQUAL),
        @JsonSubTypes.Type(value = RestClientQueryBinRangeFilter.class, name = AerospikeAPIConstants.QueryFilterTypes.QUERY_FILTER_TYPE_RANGE),
        @JsonSubTypes.Type(value = RestClientQueryBinContainsFilter.class, name = AerospikeAPIConstants.QueryFilterTypes.QUERY_FILTER_TYPE_CONTAINS),
        @JsonSubTypes.Type(value = RestClientQueryBinGeoWithinRegionFilter.class, name = AerospikeAPIConstants.QueryFilterTypes.QUERY_FILTER_TYPE_GEOWITHIN_REGION),
        @JsonSubTypes.Type(value = RestClientQueryBinGeoWithinRadiusFilter.class, name = AerospikeAPIConstants.QueryFilterTypes.QUERY_FILTER_TYPE_GEOWITHIN_RADIUS),
        @JsonSubTypes.Type(value = RestClientQueryBinGeoContainsPointFilter.class, name = AerospikeAPIConstants.QueryFilterTypes.QUERY_FILTER_TYPE_GEOCONTAINS_POINT),
})
abstract public class RestClientQueryBinFilter implements IRestClientQueryFilter {
    final public String filterType = null;

    @Schema(description = "TODO", required = true)
    @JsonProperty(required = true)
    public String binName;

    @Schema(description = "TODO", required = true)
    @JsonProperty(required = true)
    List<IRestClientCTX> ctx;
}

