package com.aerospike.restclient.domain;

import com.aerospike.client.BatchRecord;
import com.aerospike.client.BatchUDF;
import com.aerospike.client.BatchWrite;
import com.aerospike.client.Value;
import com.aerospike.client.policy.BatchWritePolicy;
import com.aerospike.restclient.util.converters.OperationConverter;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class RestClientBatchWriteResponse extends RestClientBatchRecordResponse {
    @Schema(description = "List of operation.")
    public List<RestClientOperation> opsList;

    //TODO: Should the policy be returned?
    @Schema(description = "TODO")
    public BatchWritePolicy policy;

    public RestClientBatchWriteResponse() {}

    public RestClientBatchWriteResponse(BatchWrite batchWrite) {
        super(batchWrite);
        // TODO: We need to implement the conversion of all ops to maps.  The opposite of what OperationConverter implements.
        // TODO:  This will also require us to implement the conversion of Value to any primitive type.
        // opsList = OperationConverter.convertOperatioToMap(batchWrite.ops)
    }
}
