package com.aerospike.restclient.domain.batchmodels;

import com.aerospike.client.BatchWrite;
import com.aerospike.client.Operation;
import com.aerospike.restclient.domain.RestClientOperation;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.aerospike.restclient.util.RestClientErrors;
import com.aerospike.restclient.util.converters.OperationsConverter;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Schema(
        description = "An object that describes a batch write operation to be used in a batch request.",
        externalDocs = @ExternalDocumentation(
                url =
                        "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.1/com/aerospike/client/BatchWrite.html"
        )
)
public class BatchWriteRequest extends BatchRecordRequest {

    @Schema(
            description = "List of bins to limit the record response to.",
            allowableValues = AerospikeAPIConstants.BATCH_TYPE_WRITE,
            required = true
    )
    @JsonProperty(required = true)
    public final String batchType = AerospikeAPIConstants.BATCH_TYPE_WRITE;

    @Schema(description = "List of operation.")
    @JsonProperty(required = true)
    public List<RestClientOperation> opsList;

    @Schema(description = "Policy attributes used for this batch write operation.")
    public BatchWritePolicy policy;

    public BatchWriteRequest() {
    }

    public BatchWrite toBatchRecord() {
        if (key == null) {
            throw new RestClientErrors.InvalidKeyError("Key for a batch write may not be null");
        }

        List<Map<String, Object>> opsMapsList = opsList.stream().map(RestClientOperation::toMap)
                .collect(Collectors.toList());
        Operation[] operations = OperationsConverter.mapListToOperationsArray(opsMapsList);

        if (policy == null) {
            return new BatchWrite(key.toKey(), operations);
        }

        return new BatchWrite(policy.toBatchWritePolicy(), key.toKey(), operations);
    }
}
