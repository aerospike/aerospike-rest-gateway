package com.aerospike.restclient.domain.operationmodels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Server returns indexBitCount and minHashBitCount used to create HLL bin in a list of longs. The list size is 2.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/latest/com/aerospike/client/operation/HLLOperation.html"),
        hidden = true
)
public class HLLDescribeOperation extends HLLOperation {
    @Schema(
            description = "The type of operation. It is always " + OperationTypes.HLL_DESCRIBE,
            required = true,
            allowableValues = OperationTypes.HLL_DESCRIBE
    )
    final public String type = OperationTypes.HLL_DESCRIBE;

    @JsonCreator
    public HLLDescribeOperation(@JsonProperty(value = "binName", required = true) String binName) {
        super(binName);
    }

    public com.aerospike.client.Operation toOperation() {
        return com.aerospike.client.operation.HLLOperation.describe(binName);
    }
}
