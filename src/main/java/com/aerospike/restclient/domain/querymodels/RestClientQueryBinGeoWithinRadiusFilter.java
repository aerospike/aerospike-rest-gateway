package com.aerospike.restclient.domain.querymodels;

import com.aerospike.client.cdt.CTX;
import com.aerospike.client.query.Filter;
import com.aerospike.client.query.IndexCollectionType;
import com.aerospike.restclient.domain.ctxmodels.IRestClientCTX;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public class RestClientQueryBinGeoWithinRadiusFilter extends RestClientQueryBinFilter {
    @Schema(description = "TODO", required = true, allowableValues = AerospikeAPIConstants.QueryFilterTypes.QUERY_FILTER_TYPE_GEOWITHIN_RADIUS)
    final public String filterType = AerospikeAPIConstants.QueryFilterTypes.QUERY_FILTER_TYPE_GEOWITHIN_RADIUS;
    @Schema(description = "TODO", required = true)
    public double latitude;

    @Schema(description = "TODO", required = true)
    @JsonProperty(required = true)
    public double longitude;

    @Schema(description = "TODO", required = true)
    @JsonProperty(required = true)
    public double radius;


    @Schema(description = "TODO", defaultValue = "DEFAULT")
    IndexCollectionType collectionType = IndexCollectionType.DEFAULT;

    public RestClientQueryBinGeoWithinRadiusFilter() {
    }

    @Override
    public Filter toFilter() {
        CTX[] asCTX = ctx.stream().map(IRestClientCTX::toCTX).toArray(CTX[]::new);

        return Filter.geoWithinRadius(binName, collectionType, latitude, longitude, radius, asCTX);
    }
}

