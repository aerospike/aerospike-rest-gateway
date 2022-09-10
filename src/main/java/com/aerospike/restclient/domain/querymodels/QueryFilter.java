package com.aerospike.restclient.domain.querymodels;

import com.aerospike.client.query.Filter;
import com.aerospike.restclient.domain.ctxmodels.CTX;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(
                        value = QueryEqualsStringFilter.class,
                        name = AerospikeAPIConstants.QueryFilterTypes.EQUAL_STRING
                ),
                @JsonSubTypes.Type(
                        value = QueryEqualLongFilter.class,
                        name = AerospikeAPIConstants.QueryFilterTypes.EQUAL_LONG
                ),
                @JsonSubTypes.Type(value = QueryRangeFilter.class, name = AerospikeAPIConstants.QueryFilterTypes.RANGE),
                @JsonSubTypes.Type(
                        value = QueryContainsStringFilter.class,
                        name = AerospikeAPIConstants.QueryFilterTypes.CONTAINS_STRING
                ), @JsonSubTypes.Type(
                value = QueryContainsLongFilter.class,
                name = AerospikeAPIConstants.QueryFilterTypes.CONTAINS_LONG
        ),
                @JsonSubTypes.Type(
                        value = QueryGeoWithinPolygonFilter.class,
                        name = AerospikeAPIConstants.QueryFilterTypes.GEOWITHIN_REGION
                ),
                @JsonSubTypes.Type(
                        value = QueryGeoWithinRadiusFilter.class,
                        name = AerospikeAPIConstants.QueryFilterTypes.GEOWITHIN_RADIUS
                ),
                @JsonSubTypes.Type(
                        value = QueryGeoContainsPointFilter.class,
                        name = AerospikeAPIConstants.QueryFilterTypes.GEOCONTAINS_POINT
                ),
        }
)
@Schema(description = "QueryFilter base type. Only allowed on bin which has a secondary index defined.")
abstract public class QueryFilter {
    //         Must be set in SubType.
    @Schema(
            description = "The type of query filter this object represents.",
            required = true,
            allowableValues = {
                    AerospikeAPIConstants.QueryFilterTypes.EQUAL_STRING,
                    AerospikeAPIConstants.QueryFilterTypes.EQUAL_LONG,
                    AerospikeAPIConstants.QueryFilterTypes.RANGE,
                    AerospikeAPIConstants.QueryFilterTypes.CONTAINS_LONG,
                    AerospikeAPIConstants.QueryFilterTypes.GEOWITHIN_REGION,
                    AerospikeAPIConstants.QueryFilterTypes.GEOWITHIN_RADIUS,
                    AerospikeAPIConstants.QueryFilterTypes.GEOCONTAINS_POINT,
            }
    )
    final public String type = null;

    @Schema(description = "The bin for which a secondary-index is defined.", required = true)
    @JsonProperty(required = true)
    public String binName;

    @Schema(description = "An optional context for elements within a CDT which a secondary-index is defined.")
    public List<CTX> ctx;

    abstract public Filter toFilter();

    protected com.aerospike.client.cdt.CTX[] getCTXArray() {
        com.aerospike.client.cdt.CTX[] asCTX = null;

        if (ctx != null) {
            asCTX = ctx.stream().map(CTX::toCTX).toArray(com.aerospike.client.cdt.CTX[]::new);
        }

        return asCTX;
    }
}

