package com.aerospike.restclient.domain;

import com.aerospike.client.BatchRecord;
import com.aerospike.client.ResultCode;
import io.swagger.v3.oas.annotations.media.Schema;

public class RestClientBatchRecordResponse {

    public RestClientBatchRecordResponse() {
    }

    public RestClientBatchRecordResponse(BatchRecord batchRecord) {
        resultCode = batchRecord.resultCode;
        resultCodeString = ResultCode.getResultString(resultCode);
        record = batchRecord.record != null ? new RestClientRecord(batchRecord.record) : null;
        key = new RestClientKey(batchRecord.key);
        inDoubt = batchRecord.inDoubt;
    }

    @Schema(description = "TODO")
    public int resultCode;

    @Schema(description = "TODO")
    public String resultCodeString;

    @Schema(description = "Record associated with the key. Null if the record was not found")
    public RestClientRecord record;

    @Schema(description = "Key to retrieve a record")
    public RestClientKey key;

    @Schema(description = "TODO")
    public boolean inDoubt;
}