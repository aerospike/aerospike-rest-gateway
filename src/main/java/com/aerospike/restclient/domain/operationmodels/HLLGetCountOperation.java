package com.aerospike.restclient.domain.operationmodels;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Server returns estimated number of elements in the HLL bin.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/latest/com/aerospike/client/operation/HLLOperation.html")
)
public class HLLGetCountOperation extends HLLOperation {
    @Schema(
            description = "The type of operation. It is always " + OperationTypes.HLL_COUNT,
            required = true,
            allowableValues = OperationTypes.HLL_COUNT
    )
    final public String type = OperationTypes.HLL_COUNT;

    public HLLGetCountOperation(String binName) {
        super(binName);
    }

    public com.aerospike.client.Operation toOperation() {
        return com.aerospike.client.operation.HLLOperation.getCount(binName);
    }
}
