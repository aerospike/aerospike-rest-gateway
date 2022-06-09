package com.aerospike.restclient.domain;

import com.aerospike.client.BatchRead;
import com.aerospike.client.BatchRecord;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes({
        @JsonSubTypes.Type(RestClientBatchReadResponse.class),
        @JsonSubTypes.Type(RestClientBatchWriteResponse.class),
        @JsonSubTypes.Type(RestClientBatchDeleteResponse.class),
        @JsonSubTypes.Type(RestClientBatchUdfResponse.class),
})
public class RestClientBatchRecordResponse {


    public RestClientBatchRecordResponse() {}

    public RestClientBatchRecordResponse(BatchRecord batchRecord) {
        resultCode = batchRecord.resultCode;
        record = batchRecord.record != null ? new RestClientRecord(batchRecord.record) : null;
        key = new RestClientKey(batchRecord.key);
        inDoubt = batchRecord.inDoubt;
    }

    //TODO
    @Schema(description = "TODO")
    public int resultCode;

    @Schema(description = "Record associated with the key. Null if the record was not found")
    public RestClientRecord record;

    @Schema(description = "Key to retrieve a record")
    public RestClientKey key;

    //TODO
    @Schema(description = "TODO")
    public boolean inDoubt;
}