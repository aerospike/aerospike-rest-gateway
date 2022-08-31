package com.aerospike.restclient.domain.querymodels;

import com.aerospike.client.cdt.CTX;
import com.aerospike.client.query.Filter;
import com.aerospike.client.query.IndexCollectionType;
import com.aerospike.restclient.domain.ctxmodels.RestClientCTX;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public class RestClientQueryBinGeoWithinRegionFilter extends RestClientQueryFilter {
    @Schema(description = "TODO", required = true, allowableValues = AerospikeAPIConstants.QueryFilterTypes.GEOWITHIN_REGION)
    final public String filterType = AerospikeAPIConstants.QueryFilterTypes.GEOWITHIN_REGION;
    @Schema(description = "TODO", required = true)
    @JsonProperty(required = true)
    public String region;

    @Schema(description = "TODO", defaultValue = "DEFAULT")
    public IndexCollectionType collectionType = IndexCollectionType.DEFAULT;

    public RestClientQueryBinGeoWithinRegionFilter() {
    }

    @Override
    public Filter toFilter() {
        CTX[] asCTX = ctx.stream().map(RestClientCTX::toCTX).toArray(CTX[]::new);

        return Filter.geoWithinRegion(binName, collectionType, region, asCTX);
    }
}


