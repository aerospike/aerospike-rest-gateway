package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.operation.BitPolicy;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Base64;

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
    private Integer byteOffset;

    @Schema(required = true)
    private Integer byteSize;

    @Schema(required = true, format = "byte")
    private String value;

    public BitXOrOperation(String binName, Integer byteOffset, Integer byteSize) {
        super(binName);
        this.byteOffset = byteOffset;
        this.byteSize = byteSize;
    }

    public Integer getByteOffset() {
        return byteOffset;
    }

    public void setByteOffset(Integer byteOffset) {
        this.byteOffset = byteOffset;
    }

    public Integer getByteSize() {
        return byteSize;
    }

    public void setByteSize(Integer byteSize) {
        this.byteSize = byteSize;
    }

    public com.aerospike.client.Operation toOperation() {
        return com.aerospike.client.operation.BitOperation.xor(BitPolicy.Default, binName, byteOffset, byteSize,
                Base64.getDecoder().decode(value));
    }
}
