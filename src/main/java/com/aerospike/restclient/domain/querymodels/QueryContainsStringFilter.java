package com.aerospike.restclient.domain.querymodels;

import com.aerospike.client.cdt.CTX;
import com.aerospike.client.query.Filter;
import com.aerospike.client.query.IndexCollectionType;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.aerospike.restclient.util.annotations.ASRestClientSchemas;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Filter for CDTs that contain a string value. Only allowed on bins which has a secondary index defined.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/latest/com/aerospike/client/query/Filter.html")
)
public class QueryContainsStringFilter extends QueryFilter {
    @Schema(
            description = "The type of query filter this object represents. It is always " + AerospikeAPIConstants.QueryFilterTypes.CONTAINS_STRING,
            required = true,
            allowableValues = AerospikeAPIConstants.QueryFilterTypes.CONTAINS_STRING
    )
    final public String filterType = AerospikeAPIConstants.QueryFilterTypes.CONTAINS_STRING;

    @Schema(required = true)
    public String value;

    @ASRestClientSchemas.IndexCollectionType
    public IndexCollectionType collectionType = IndexCollectionType.DEFAULT;

    public QueryContainsStringFilter() {
    }

    @Override
    public Filter toFilter() {
        CTX[] asCTX = getCTXArray();
        return Filter.contains(binName, collectionType, value, asCTX);
    }
}



