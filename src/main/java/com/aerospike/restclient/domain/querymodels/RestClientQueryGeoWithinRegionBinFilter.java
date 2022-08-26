package com.aerospike.restclient.domain.querymodels;

import com.aerospike.client.cdt.CTX;
import com.aerospike.client.query.Filter;
import com.aerospike.client.query.IndexCollectionType;
import com.aerospike.restclient.domain.ctxmodels.IRestClientCTX;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public class RestClientQueryGeoWithinRegionBinFilter extends RestClientQueryBinFilter {
    @Schema(description = "TODO", required = true)
    @JsonProperty(required = true)
    String region;

    @Schema(description = "TODO")
    IndexCollectionType collectionType = IndexCollectionType.DEFAULT;

    public RestClientQueryGeoWithinRegionBinFilter() {
    }

    @Override
    public Filter toFilter() {
        CTX[] asCTX = ctx.stream().map(IRestClientCTX::toCTX).toArray(CTX[]::new);

        return Filter.geoWithinRegion(binName, collectionType, region, asCTX);
    }
}


