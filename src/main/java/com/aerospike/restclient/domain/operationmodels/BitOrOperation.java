package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.operation.BitPolicy;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Server performs bitwise \"or\" on value and byte[] bin at bitOffset for bitSize. Server does not return a value.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/latest/com/aerospike/client/operation/BitOperation.html")
)
public class BitOrOperation extends BitOperation {
    @Schema(
            description = "The type of operation. It is always " + OperationTypes.BIT_OR,
            required = true,
            allowableValues = OperationTypes.BIT_OR
    )
    final public String type = OperationTypes.BIT_OR;

    @Schema(required = true)
    private final int bitOffset;

    @Schema(required = true)
    private final int bitSize;

    @Schema(required = true)
    private final byte[] value;

    @JsonCreator
    public BitOrOperation(@JsonProperty(value = "binName", required = true) String binName,
                          @JsonProperty(value = "bitOffset", required = true) int bitOffset,
                          @JsonProperty(value = "bitSize", required = true) int bitSize,
                          @JsonProperty(value = "value", required = true) byte[] value) {
        super(binName);
        this.bitOffset = bitOffset;
        this.bitSize = bitSize;
        this.value = value;
    }

    public com.aerospike.client.Operation toOperation() {
        return com.aerospike.client.operation.BitOperation.or(BitPolicy.Default, binName, bitOffset, bitSize, value);
    }
}
