package com.aerospike.restclient.domain.querymodels;

import com.aerospike.restclient.domain.RestClientKeyRecord;
import com.aerospike.restclient.domain.RestClientOperation;
import com.aerospike.restclient.domain.RestClientRecord;
import com.aerospike.restclient.domain.scanmodels.Pagination;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

public class RestClientQueryResponse {
    @Schema(description = "TODO", required = true)
    @JsonProperty(required = true)
    private final List<RestClientKeyRecord> records;

    @Schema(description = "Pagination details.")
    private final Pagination pagination;

    public RestClientQueryResponse(int initialCapacity) {
        records = new ArrayList<>(initialCapacity);
        pagination = new Pagination();
    }

    public RestClientQueryResponse() {
        this(0);
    }

    public List<RestClientKeyRecord> getRecords() {
        return records;
    }

    public void addRecord(RestClientKeyRecord record) {
        records.add(record);
    }

    public int size() {
        return records.size();
    }

    public Pagination getPagination() {
        return pagination;
    }
}
