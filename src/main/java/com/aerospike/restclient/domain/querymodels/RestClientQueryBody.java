package com.aerospike.restclient.domain.querymodels;

import com.aerospike.restclient.domain.RestClientOperation;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class RestClientQueryBody {
    @Schema(description = "Query Filter.")
    public RestClientQueryFilter filter;

    @Schema(description = "TODO")
    public String from;

    public RestClientQueryBody() {};
}
