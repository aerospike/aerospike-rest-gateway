package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.operation.BitPolicy;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Server performs bitwise \"xor\" on value and byte[] bin at bitOffset for bitSize.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/latest/com/aerospike/client/operation/BitOperation.html")
)
public class BitXOrOperation extends BitOperation {
    @Schema(
            description = "The type of operation. It is always " + OperationTypes.BIT_XOR,
            required = true,
            allowableValues = OperationTypes.BIT_XOR
    )
    final public String type = OperationTypes.BIT_XOR;

    @Schema(required = true)
    private int bitOffset;

    @Schema(required = true)
    private int bitSize;

    @Schema(required = true)
    private byte[] value;

    public BitXOrOperation(String binName, int bitOffset, int bitSize, byte[] value) {
        super(binName);
        this.bitOffset = bitOffset;
        this.bitSize = bitSize;
        this.value = value;
    }

    public int getByteOffset() {
        return bitOffset;
    }

    public void setByteOffset(int bitOffset) {
        this.bitOffset = bitOffset;
    }

    public int getBitSize() {
        return bitSize;
    }

    public void setBitSize(int bitSize) {
        this.bitSize = bitSize;
    }

    public int getBitOffset() {
        return bitOffset;
    }

    public void setBitOffset(int bitOffset) {
        this.bitOffset = bitOffset;
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

    public com.aerospike.client.Operation toOperation() {
        return com.aerospike.client.operation.BitOperation.xor(BitPolicy.Default, binName, bitOffset, bitSize, value);
    }
}
