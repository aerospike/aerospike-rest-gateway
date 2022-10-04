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
    private int byteOffset;

    @Schema(required = true)
    private int byteSize;

    public BitNotOperation(String binName, int byteOffset, int byteSize) {
        super(binName);
        this.byteOffset = byteOffset;
        this.byteSize = byteSize;
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

    public com.aerospike.client.Operation toOperation() {
        return com.aerospike.client.operation.BitOperation.not(BitPolicy.Default, binName, byteOffset, byteSize);
    }
}
