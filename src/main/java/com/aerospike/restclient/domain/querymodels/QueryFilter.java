package com.aerospike.restclient.domain.querymodels;

import com.aerospike.client.query.Filter;
import com.aerospike.restclient.domain.ctxmodels.*;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
            },
            hidden = true
    )
    final public String type = null;

    @Schema(description = "The bin for which a secondary-index is defined.", required = true)
    @JsonProperty(required = true)
    public String binName;

    @ArraySchema(
            schema = @Schema(
                    description = "An optional context for elements within a CDT which a secondary-index is defined.",
                    anyOf = {
                            ListIndexCTX.class,
                            ListRankCTX.class,
                            ListValueCTX.class,
                            MapIndexCTX.class,
                            MapRankCTX.class,
                            MapKeyCTX.class,
                            MapValueCTX.class,
                    }
            )
    )
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
    @JsonSubTypes(
            {
                    @JsonSubTypes.Type(value = ListIndexCTX.class, name = AerospikeAPIConstants.CTX.LIST_INDEX),
                    @JsonSubTypes.Type(value = ListRankCTX.class, name = AerospikeAPIConstants.CTX.LIST_RANK),
                    @JsonSubTypes.Type(value = ListValueCTX.class, name = AerospikeAPIConstants.CTX.LIST_VALUE),
                    @JsonSubTypes.Type(value = MapIndexCTX.class, name = AerospikeAPIConstants.CTX.MAP_INDEX),
                    @JsonSubTypes.Type(value = MapRankCTX.class, name = AerospikeAPIConstants.CTX.MAP_RANK),
                    @JsonSubTypes.Type(value = MapKeyCTX.class, name = AerospikeAPIConstants.CTX.MAP_KEY),
                    @JsonSubTypes.Type(value = MapValueCTX.class, name = AerospikeAPIConstants.CTX.MAP_VALUE),
//                    @JsonSubTypes.Type(value = MapKeyCreateCTX.class, name = AerospikeAPIConstants.CTX.MAP_KEY_CREATE),
//                    @JsonSubTypes.Type(value = ListIndexCreateCTX.class, name = AerospikeAPIConstants.CTX.LIST_INDEX_CREATE),
            }
    )
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

