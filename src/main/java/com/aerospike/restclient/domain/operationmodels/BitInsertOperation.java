package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.operation.BitPolicy;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Server inserts value bytes into byte[] bin at byteOffset. Server does not return a value.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/latest/com/aerospike/client/operation/BitOperation.html")
)
public class BitInsertOperation extends BitOperation {
    @Schema(
            description = "The type of operation. It is always " + OperationTypes.BIT_INSERT,
            required = true,
            allowableValues = OperationTypes.BIT_INSERT
    )
    final public String type = OperationTypes.BIT_INSERT;

    @Schema(required = true)
    private int byteOffset;

    @Schema(required = true)
    private byte[] value;

    public BitInsertOperation(String binName, int byteOffset, byte[] value) {
        super(binName);
        this.byteOffset = byteOffset;
        this.value = value;
    }

    public int getByteOffset() {
        return byteOffset;
    }

    public void setByteOffset(int byteOffset) {
        this.byteOffset = byteOffset;
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

    public com.aerospike.client.Operation toOperation() {
        return com.aerospike.client.operation.BitOperation.insert(BitPolicy.Default, binName, byteOffset, value);
    }
}
