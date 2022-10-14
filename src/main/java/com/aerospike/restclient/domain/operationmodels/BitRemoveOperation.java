package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.operation.BitPolicy;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Server removes bytes from byte[] bin at byteOffset for byteSize. Server does not return a value.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/latest/com/aerospike/client/operation/BitOperation.html")
)
public class BitRemoveOperation extends BitOperation {
    @Schema(
            description = "The type of operation. It is always " + OperationTypes.BIT_REMOVE,
            required = true,
            allowableValues = OperationTypes.BIT_REMOVE
    )
    final public String type = OperationTypes.BIT_REMOVE;

    @Schema(required = true)
    private final int byteOffset;

    @Schema(required = true)
    private final int byteSize;

    @JsonCreator
    public BitRemoveOperation(@JsonProperty(value = "binName", required = true) String binName,
                              @JsonProperty(value = "byteOffset", required = true) int byteOffset,
                              @JsonProperty(value = "byteSize", required = true) int byteSize) {
        super(binName);
        this.byteOffset = byteOffset;
        this.byteSize = byteSize;
    }

    public com.aerospike.client.Operation toOperation() {
        return com.aerospike.client.operation.BitOperation.remove(BitPolicy.Default, binName, byteOffset, byteSize);
    }
}
