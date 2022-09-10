package com.aerospike.restclient.domain.querymodels;

import com.aerospike.client.query.Filter;
import com.aerospike.client.query.IndexCollectionType;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.aerospike.restclient.util.annotations.ASRestClientSchemas;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Filter values numeric values within a range. Only allowed on bin which has a secondary index defined.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/query/Filter.html")
)
public class QueryRangeFilter extends QueryFilter {
    @Schema(
            description = "The type of query filter this object represents. It is always " + AerospikeAPIConstants.QueryFilterTypes.RANGE,
            required = true,
            allowableValues = AerospikeAPIConstants.QueryFilterTypes.RANGE
    )
    final public String type = AerospikeAPIConstants.QueryFilterTypes.RANGE;
    @Schema(description = "Filter begin value inclusive.", required = true)
    public long begin;

    @Schema(description = "Filter end value inclusive.", required = true)
    @JsonProperty(required = true)
    public long end;

    @ASRestClientSchemas.IndexCollectionType
    public IndexCollectionType collectionType = IndexCollectionType.DEFAULT;

    public QueryRangeFilter() {
    }

    @Override
    public Filter toFilter() {
        return Filter.range(binName, collectionType, begin, end, getCTXArray());
    }
}

