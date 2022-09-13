package com.aerospike.restclient.domain.querymodels;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Body of Query request.")
public class QueryRequestBody {
    @Schema(description = "Query Filter.")
    public QueryFilter filter;

    @Schema(
            description = "Pagination token returned from last query request used to retrieve the next page. Use \"getToken\" to retrieve this token."
    )
    public String from;

    public QueryRequestBody() {
    }
}
