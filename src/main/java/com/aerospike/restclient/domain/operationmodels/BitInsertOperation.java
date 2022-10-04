package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.operation.BitPolicy;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Base64;

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
    private Integer byteOffset;

    @Schema(required = true, format = "byte")
    private String value;

    public BitInsertOperation(String binName, Integer byteOffset, String value) {
        super(binName);
        this.byteOffset = byteOffset;
        this.value = value;
    }

    public Integer getByteOffset() {
        return byteOffset;
    }

    public void setByteOffset(Integer byteOffset) {
        this.byteOffset = byteOffset;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public com.aerospike.client.Operation toOperation() {
        return com.aerospike.client.operation.BitOperation.insert(BitPolicy.Default, binName, byteOffset,
                Base64.getDecoder().decode(value));
    }
}
