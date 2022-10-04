package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.operation.BitPolicy;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Server resizes byte[] to byteSize according to resizeFlags (See BitResizeFlags). Server does not return a value.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/latest/com/aerospike/client/operation/BitOperation.html")
)
public class BitResizeOperation extends BitOperation {
    @Schema(
            description = "The type of operation. It is always " + OperationTypes.BIT_RESIZE,
            required = true,
            allowableValues = OperationTypes.BIT_RESIZE
    )
    final public String type = OperationTypes.BIT_RESIZE;

    @Schema(required = true)
    private int byteSize;

    @Schema(required = true)
    private Integer resizeFlags;

    public BitResizeOperation(String binName, Integer byteSize, Integer resizeFlags) {
        super(binName);
        this.byteSize = byteSize;
        this.resizeFlags = resizeFlags;
    }

    public Integer getByteSize() {
        return byteSize;
    }

    public void setByteSize(Integer byteSize) {
        this.byteSize = byteSize;
    }

    public Integer getResizeFlags() {
        return resizeFlags;
    }

    public void setResizeFlags(Integer resizeFlags) {
        this.resizeFlags = resizeFlags;
    }

    public com.aerospike.client.Operation toOperation() {
        return com.aerospike.client.operation.BitOperation.resize(BitPolicy.Default, binName, byteSize, resizeFlags);
    }
}
