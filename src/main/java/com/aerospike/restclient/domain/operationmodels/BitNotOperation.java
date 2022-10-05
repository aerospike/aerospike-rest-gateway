package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.operation.BitPolicy;
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
    private int bitOffset;

    @Schema(required = true)
    private int bitSize;

    public BitNotOperation(String binName, int bitOffset, int byteSize) {
        super(binName);
        this.bitOffset = bitOffset;
        this.bitSize = byteSize;
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

    public void setBitSize(int byteSize) {
        this.bitSize = byteSize;
    }

    public com.aerospike.client.Operation toOperation() {
        return com.aerospike.client.operation.BitOperation.not(BitPolicy.Default, binName, bitOffset, bitSize);
    }
}
