package com.aerospike.restclient.domain.operationmodels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Server updates the cached count (if stale) and returns the count.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/latest/com/aerospike/client/operation/HLLOperation.html")
)
public class HLLRefreshCountOperation extends HLLOperation {
    @Schema(
            description = "The type of operation. It is always " + OperationTypes.HLL_SET_COUNT,
            required = true,
            allowableValues = OperationTypes.HLL_SET_UNION
    )
    final public String type = OperationTypes.HLL_SET_UNION;

    @JsonCreator
    public HLLRefreshCountOperation(@JsonProperty(value = "binName", required = true) String binName) {
        super(binName);
    }

    public com.aerospike.client.Operation toOperation() {
        return com.aerospike.client.operation.HLLOperation.refreshCount(binName);
    }
}
