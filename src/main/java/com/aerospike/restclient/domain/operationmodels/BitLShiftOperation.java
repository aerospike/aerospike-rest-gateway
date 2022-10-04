package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.operation.BitPolicy;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Server shifts left byte[] bin starting at bitOffset for bitSize. Server does not return a value.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/latest/com/aerospike/client/operation/BitOperation.html")
)
public class BitLShiftOperation extends BitOperation {
    @Schema(
            description = "The type of operation. It is always " + OperationTypes.BIT_LSHIFT,
            required = true,
            allowableValues = OperationTypes.BIT_LSHIFT
    )
    final public String type = OperationTypes.BIT_LSHIFT;

    @Schema(required = true)
    private int byteOffset;

    @Schema(required = true)
    private int byteSize;

    @Schema(required = true)
    private int shift;

    public BitLShiftOperation(String binName, int byteOffset, int byteSize, int shift) {
        super(binName);
        this.byteOffset = byteOffset;
        this.byteSize = byteSize;
        this.shift = shift;
    }

    public int getByteOffset() {
        return byteOffset;
    }

    public void setByteOffset(int byteOffset) {
        this.byteOffset = byteOffset;
    }

    public int getByteSize() {
        return byteSize;
    }

    public void setByteSize(int byteSize) {
        this.byteSize = byteSize;
    }

    public int getShift() {
        return shift;
    }

    public void setShift(Integer shift) {
        this.shift = shift;
    }

    public com.aerospike.client.Operation toOperation() {
        return com.aerospike.client.operation.BitOperation.lshift(BitPolicy.Default, binName, byteOffset, byteSize,
                shift);
    }
}
