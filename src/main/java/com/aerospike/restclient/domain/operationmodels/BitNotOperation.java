package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.operation.BitPolicy;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Server negates byte[] bin starting at bitOffset for bitSize. Server does not return a value.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/latest/com/aerospike/client/operation/BitOperation.html")
)
public class BitNotOperation extends BitOperation {
    @Schema(
            description = "The type of operation. It is always " + OperationTypes.BIT_NOT,
            required = true,
            allowableValues = OperationTypes.BIT_NOT
    )
    final public String type = OperationTypes.BIT_NOT;

    @Schema(required = true)
    private final int bitOffset;

    @Schema(required = true)
    private final int bitSize;

    @JsonCreator
    public BitNotOperation(@JsonProperty(value = "binName", required = true) String binName,
                           @JsonProperty(value = "bitOffset", required = true) int bitOffset,
                           @JsonProperty(value = "bitSize", required = true) int bitSize) {
        super(binName);
        this.bitOffset = bitOffset;
        this.bitSize = bitSize;
    }

    public com.aerospike.client.Operation toOperation() {
        return com.aerospike.client.operation.BitOperation.not(BitPolicy.Default, binName, bitOffset, bitSize);
    }
}
