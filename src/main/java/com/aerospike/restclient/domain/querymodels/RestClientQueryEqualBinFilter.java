package com.aerospike.restclient.domain.querymodels;

import com.aerospike.client.cdt.CTX;
import com.aerospike.client.query.Filter;
import com.aerospike.restclient.domain.ctxmodels.IRestClientCTX;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.aerospike.restclient.util.RestClientErrors;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Arrays;

public class RestClientQueryEqualBinFilter extends RestClientQueryBinFilter {
    @Schema(description = "TODO", required = true, oneOf = {String.class, Long.class})
    @JsonProperty(required = true)
    Object value;

    public RestClientQueryEqualBinFilter() {}

    @Override
    public Filter toFilter() {
        CTX[] asCTX = ctx.stream().map(IRestClientCTX::toCTX).toArray(CTX[]::new);

        if (value instanceof Long || value instanceof Integer) {
            return Filter.equal(binName, ((Number) value).longValue(), asCTX);
        }

        if (value instanceof String) {
            return Filter.equal(binName, (String) value, asCTX);
        }

        throw new RestClientErrors.InvalidQueryFilterError("Equality filter is only allowed for String and Long");
    }
}

