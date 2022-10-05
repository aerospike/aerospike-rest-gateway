package com.aerospike.restclient.domain.operationmodels;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Server returns integer count of set bits from byte[] bin starting at bitOffset for bitSize.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/latest/com/aerospike/client/operation/BitOperation.html")
)
public class BitCountOperation extends BitOperation {
    @Schema(
            description = "The type of operation. It is always " + OperationTypes.BIT_COUNT,
            required = true,
            allowableValues = OperationTypes.BIT_COUNT
    )
    final public String type = OperationTypes.BIT_COUNT;

    @Schema(required = true)
    private int bitOffset;

    @Schema(required = true)
    private int bitSize;

    public BitCountOperation(String binName, int bitOffset, int bitSize) {
        super(binName);
        this.bitOffset = bitOffset;
        this.bitSize = bitSize;
    }

    public int getBitOffset() {
        return bitOffset;
    }

    public void setBitOffset(int bitOffset) {
        this.bitOffset = bitOffset;
    }

    public int getBitSize() {
        return bitSize;
    }

    public void setBitSize(int bitSize) {
        this.bitSize = bitSize;
    }

    public com.aerospike.client.Operation toOperation() {
        return com.aerospike.client.operation.BitOperation.count(binName, bitOffset, bitSize);
    }
}
