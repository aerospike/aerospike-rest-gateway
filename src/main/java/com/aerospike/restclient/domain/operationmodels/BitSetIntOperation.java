package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.operation.BitPolicy;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Server sets value to byte[] bin starting at bitOffset for bitSize. Size must be <= 64. Server does not return a value.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/latest/com/aerospike/client/operation/BitOperation.html")
)
public class BitSetIntOperation extends BitOperation {
    @Schema(
            description = "The type of operation. It is always " + OperationTypes.BIT_SET,
            required = true,
            allowableValues = OperationTypes.BIT_SET
    )
    final public String type = OperationTypes.BIT_SET;

    @Schema(required = true)
    private int byteOffset;

    @Schema(required = true)
    private int byteSize;

    @Schema(required = true, format = "byte")
    private long value;

    public BitSetIntOperation(String binName, int byteOffset, int byteSize, long value) {
        super(binName);
        this.byteOffset = byteOffset;
        this.byteSize = byteSize;
        this.value = value;
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

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public com.aerospike.client.Operation toOperation() {
        return com.aerospike.client.operation.BitOperation.setInt(BitPolicy.Default, binName, byteOffset, byteSize,
                value);
    }
}
