package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.operation.BitPolicy;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Server shifts right byte[] bin starting at bitOffset for bitSize. Server does not return a value.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/latest/com/aerospike/client/operation/BitOperation.html")
)
public class BitRShiftOperation extends BitOperation {
    @Schema(
            description = "The type of operation. It is always " + OperationTypes.BIT_LSHIFT,
            required = true,
            allowableValues = OperationTypes.BIT_LSHIFT
    )
    final public String type = OperationTypes.BIT_LSHIFT;

    @Schema(required = true)
    private int bitOffset;

    @Schema(required = true)
    private int bitSize;

    @Schema(required = true)
    private int shift;

    public BitRShiftOperation(String binName, int bitOffset, int bitSize, int shift) {
        super(binName);
        this.bitOffset = bitOffset;
        this.bitSize = bitSize;
        this.shift = shift;
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

    public int getShift() {
        return shift;
    }

    public void setShift(Integer shift) {
        this.shift = shift;
    }

    public com.aerospike.client.Operation toOperation() {
        return com.aerospike.client.operation.BitOperation.rshift(BitPolicy.Default, binName, bitOffset, bitSize,
                shift);
    }
}
