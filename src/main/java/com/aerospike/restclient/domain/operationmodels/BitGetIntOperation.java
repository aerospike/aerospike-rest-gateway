package com.aerospike.restclient.domain.operationmodels;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Server returns integer from byte[] bin starting at bitOffset for bitSize. Signed indicates if bits should be treated as a signed number.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/latest/com/aerospike/client/operation/BitOperation.html")
)
public class BitGetIntOperation extends BitOperation {
    @Schema(
            description = "The type of operation. It is always " + OperationTypes.BIT_GET_INT,
            required = true,
            allowableValues = OperationTypes.BIT_GET_INT
    )
    final public String type = OperationTypes.BIT_GET_INT;

    @Schema(required = true)
    private int bitOffset;

    @Schema(required = true)
    private int bitSize;

    @Schema(required = true)
    private boolean signed;

    public BitGetIntOperation(String binName, int bitOffset, int bitSize, boolean signed) {
        super(binName);
        this.bitOffset = bitOffset;
        this.bitSize = bitSize;
        this.signed = signed;
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

    public boolean isSigned() {
        return signed;
    }

    public void setSigned(boolean signed) {
        this.signed = signed;
    }

    public com.aerospike.client.Operation toOperation() {
        return com.aerospike.client.operation.BitOperation.getInt(binName, bitOffset, bitSize, signed);
    }
}
