package com.aerospike.restclient.domain;

import com.aerospike.client.BatchRecord;
import com.aerospike.client.BatchWrite;
import com.aerospike.client.Operation;
import com.aerospike.client.policy.BatchWritePolicy;
import com.aerospike.restclient.util.RestClientErrors;
import com.aerospike.restclient.util.converters.OperationsConverter;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RestClientBatchWriteBody  extends RestClientBatchRecordBody {
    @Schema(description = "List of operation.")
    public List<RestClientOperation> opsList;

    //TODO
    @Schema(description = "TODO")
    public BatchWritePolicy policy;

    public RestClientBatchWriteBody() {}

    public BatchRecord toBatchRecord() {
        if (key == null) {
            throw new RestClientErrors.InvalidKeyError("Key for a batch read may not be null");
        }

        List<Map<String, Object>> opsMapsList = opsList.stream().map(RestClientOperation::toMap)
                .collect(Collectors.toList());
        Operation[] operations = OperationsConverter.mapListToOperationsArray(opsMapsList);

        return new BatchWrite(key.toKey(), operations);
    }
}
