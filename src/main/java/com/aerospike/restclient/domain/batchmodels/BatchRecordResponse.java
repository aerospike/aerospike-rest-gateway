package com.aerospike.restclient.domain.batchmodels;

import com.aerospike.client.BatchRecord;
import com.aerospike.client.ResultCode;
import com.aerospike.restclient.domain.RestClientKey;
import com.aerospike.restclient.domain.RestClientRecord;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;


@Schema(
        name = "BatchRecordResponse",
        description = "Object returned in from a single batch operation",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/BatchRecord.html")
)
public class BatchRecordResponse {

    public BatchRecordResponse() {
    }

    public BatchRecordResponse(BatchRecord batchRecord) {
        resultCode = batchRecord.resultCode;
        resultCodeString = ResultCode.getResultString(resultCode);
        record = batchRecord.record != null ? new RestClientRecord(batchRecord.record) : null;
        key = new RestClientKey(batchRecord.key);
        inDoubt = batchRecord.inDoubt;
    }

    @Schema(description = "Result code for this returned record.")
    public int resultCode;

    @Schema(description = "Message associated with resultCode.")
    public String resultCodeString;

    @Schema(description = "Record associated with the key. Null if the record was not found.")
    public RestClientRecord record;

    @Schema(description = "Key to retrieve a record.")
    public RestClientKey key;

    @Schema(description = "Is it possible that the write transaction may have completed even though an error occurred for this record.")
    public boolean inDoubt;
}