package com.aerospike.restclient.domain.querymodels;

import com.aerospike.client.cdt.CTX;
import com.aerospike.client.query.Filter;
import com.aerospike.client.query.IndexCollectionType;
import com.aerospike.restclient.domain.ctxmodels.RestClientCTX;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import io.swagger.v3.oas.annotations.media.Schema;

public class RestClientQueryBinGeoContainsPointFilter extends RestClientQueryFilter {
    @Schema(description = "TODO", required = true, allowableValues = AerospikeAPIConstants.QueryFilterTypes.GEOCONTAINS_POINT)
    final public String filterType = AerospikeAPIConstants.QueryFilterTypes.GEOCONTAINS_POINT;
    @Schema(description = "TODO", required = true)
    public String point;

    @Schema(description = "TODO", defaultValue = "DEFAULT")
    public IndexCollectionType collectionType = IndexCollectionType.DEFAULT;

    public RestClientQueryBinGeoContainsPointFilter() {
    }

    @Override
    public Filter toFilter() {
        CTX[] asCTX = ctx.stream().map(RestClientCTX::toCTX).toArray(CTX[]::new);

        return Filter.geoContains(binName, collectionType, point, asCTX);
    }
}
