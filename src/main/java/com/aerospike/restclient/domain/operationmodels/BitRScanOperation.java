package com.aerospike.restclient.domain.operationmodels;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Server returns integer bit offset of the last specified value bit in byte[] bin starting at bitOffset for bitSize.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/latest/com/aerospike/client/operation/BitOperation.html")
)
public class BitRScanOperation extends BitOperation {
    @Schema(
            description = "The type of operation. It is always " + OperationTypes.BIT_RSCAN,
            required = true,
            allowableValues = OperationTypes.BIT_RSCAN
    )
    final public String type = OperationTypes.BIT_RSCAN;

    @Schema(required = true)
    private int byteOffset;

    @Schema(required = true)
    private int byteSize;

    @Schema(required = true)
    private boolean value;

    public BitRScanOperation(String binName, int byteOffset, int byteSize, boolean value) {
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

    public boolean isValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public com.aerospike.client.Operation toOperation() {
        return com.aerospike.client.operation.BitOperation.rscan(binName, byteOffset, byteSize, value);
    }
}
