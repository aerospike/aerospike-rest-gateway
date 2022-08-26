package com.aerospike.restclient.domain.querymodels;

import com.aerospike.client.cdt.CTX;
import com.aerospike.client.query.Filter;
import com.aerospike.client.query.IndexCollectionType;
import com.aerospike.restclient.domain.ctxmodels.IRestClientCTX;
import com.aerospike.restclient.util.RestClientErrors;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

public class RestClientQueryContainsBinFilter extends RestClientQueryBinFilter {
    @Schema(description = "TODO", required = true, oneOf = {String.class, Long.class})
    @JsonProperty(required = true)
    Object value;

    @Schema(description = "TODO")
    IndexCollectionType collectionType = IndexCollectionType.DEFAULT;

    public RestClientQueryContainsBinFilter() {
    }

    @Override
    public Filter toFilter() {
        CTX[] asCTX = ctx.stream().map(IRestClientCTX::toCTX).toArray(CTX[]::new);

        if (value instanceof Long || value instanceof Integer) {
            return Filter.contains(binName, collectionType, ((Number) value).longValue(), asCTX);
        }

        if (value instanceof String) {
            return Filter.contains(binName, collectionType, (String) value, asCTX);
        }

        throw new RestClientErrors.InvalidQueryFilterError("Contains filter is only allowed for String and Long");
    }
}

