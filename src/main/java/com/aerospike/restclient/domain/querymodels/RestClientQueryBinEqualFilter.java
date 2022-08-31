package com.aerospike.restclient.domain.querymodels;

import com.aerospike.client.cdt.CTX;
import com.aerospike.client.query.Filter;
import com.aerospike.restclient.domain.ctxmodels.RestClientCTX;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.aerospike.restclient.util.RestClientErrors;
import io.swagger.v3.oas.annotations.media.Schema;

public class RestClientQueryBinEqualFilter extends RestClientQueryFilter {
    @Schema(description = "TODO", required = true, allowableValues = AerospikeAPIConstants.QueryFilterTypes.EQUAL)
    final public String filterType = AerospikeAPIConstants.QueryFilterTypes.EQUAL;

    @Schema(description = "TODO", required = true, oneOf = {String.class, Long.class})
    public Object value;

    public RestClientQueryBinEqualFilter() {}


    @Override
    public Filter toFilter() {
        CTX[] asCTX = null;

        if (ctx != null) {
            asCTX = ctx.stream().map(RestClientCTX::toCTX).toArray(CTX[]::new);
        }

        if (value instanceof Long || value instanceof Integer) {
            return Filter.equal(binName, ((Number) value).longValue(), asCTX);
        }

        if (value instanceof String) {
            return Filter.equal(binName, (String) value, asCTX);
        }

        throw new RestClientErrors.InvalidQueryFilterError("Equality filter is only allowed for String and Long");
    }
}

