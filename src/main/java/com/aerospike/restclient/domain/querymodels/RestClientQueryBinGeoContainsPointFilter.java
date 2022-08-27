package com.aerospike.restclient.domain.querymodels;

import com.aerospike.client.cdt.CTX;
import com.aerospike.client.query.Filter;
import com.aerospike.client.query.IndexCollectionType;
import com.aerospike.restclient.domain.ctxmodels.IRestClientCTX;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import io.swagger.v3.oas.annotations.media.Schema;

public class RestClientQueryBinGeoContainsPointFilter extends RestClientQueryBinFilter {
    @Schema(description = "TODO", required = true, allowableValues = AerospikeAPIConstants.QueryFilterTypes.QUERY_FILTER_TYPE_GEOCONTAINS_POINT)
    final public String filterType = AerospikeAPIConstants.QueryFilterTypes.QUERY_FILTER_TYPE_GEOCONTAINS_POINT;
    @Schema(description = "TODO", required = true)
    public String point;

    @Schema(description = "TODO")
    public IndexCollectionType collectionType = IndexCollectionType.DEFAULT;

    public RestClientQueryBinGeoContainsPointFilter() {
    }

    @Override
    public Filter toFilter() {
        CTX[] asCTX = ctx.stream().map(IRestClientCTX::toCTX).toArray(CTX[]::new);

        return Filter.geoContains(binName, collectionType, point, asCTX);
    }
}
