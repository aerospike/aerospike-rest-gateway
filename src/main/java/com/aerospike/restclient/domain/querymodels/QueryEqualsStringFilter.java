package com.aerospike.restclient.domain.querymodels;

import com.aerospike.client.cdt.CTX;
import com.aerospike.client.query.Filter;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Filter for values that equal the provided value.  Only allowed on bins which has a secondary index defined.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/query/Filter.html")
)
public class QueryEqualsStringFilter extends QueryFilter {
    @Schema(
            description = "The type of query filter this object represents. It is always " + AerospikeAPIConstants.QueryFilterTypes.EQUAL_STRING,
            required = true,
            allowableValues = AerospikeAPIConstants.QueryFilterTypes.EQUAL_STRING
    )
    final public String type = AerospikeAPIConstants.QueryFilterTypes.EQUAL_STRING;

    @Schema(required = true)
    public String value;

    public QueryEqualsStringFilter() {
    }


    @Override
    public Filter toFilter() {
        CTX[] asCTX = getCTXArray();
        return Filter.equal(binName, value, asCTX);
    }
}

