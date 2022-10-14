package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.operation.BitPolicy;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Server sets value to byte[] bin starting at bitOffset for bitSize. Size must be <= 64. Server does not return a value.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/latest/com/aerospike/client/operation/BitOperation.html")
)
public class BitSetIntOperation extends BitOperation {
    @Schema(
            description = "The type of operation. It is always " + OperationTypes.BIT_SET_INT,
            required = true,
            allowableValues = OperationTypes.BIT_SET_INT
    )
    final public String type = OperationTypes.BIT_SET_INT;

    @Schema(required = true)
    private final int bitOffset;

    @Schema(required = true)
    private final int bitSize;

    @Schema(required = true)
    private final long value;

    @JsonCreator
    public BitSetIntOperation(@JsonProperty(value = "binName", required = true) String binName,
                              @JsonProperty(value = "bitOffset", required = true) int bitOffset,
                              @JsonProperty(value = "bitSize", required = true) int bitSize,
                              @JsonProperty(value = "value", required = true) long value) {
        super(binName);
        this.bitOffset = bitOffset;
        this.bitSize = bitSize;
        this.value = value;
    }

    public com.aerospike.client.Operation toOperation() {
        return com.aerospike.client.operation.BitOperation.setInt(BitPolicy.Default, binName, bitOffset, bitSize,
                value);
    }
}
