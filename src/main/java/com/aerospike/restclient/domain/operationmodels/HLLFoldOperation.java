package com.aerospike.restclient.domain.operationmodels;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Servers folds indexBitCount to the specified value. This can only be applied when minHashBitCount on the HLL bin is 0. Server does not return a value.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/latest/com/aerospike/client/operation/HLLOperation.html")
)
public class HLLFoldOperation extends HLLOperation {
    @Schema(
            description = "The type of operation. It is always " + OperationTypes.HLL_FOLD,
            required = true,
            allowableValues = OperationTypes.HLL_FOLD
    )
    final public String type = OperationTypes.HLL_FOLD;

    private int indexBitCount;

    public HLLFoldOperation(String binName, int indexBitCount) {
        super(binName);
        this.indexBitCount = indexBitCount;
    }

    public void setIndexBitCount(int indexBitCount) {
        this.indexBitCount = indexBitCount;
    }

    public int getIndexBitCount() {
        return indexBitCount;
    }

    public com.aerospike.client.Operation toOperation() {
        return com.aerospike.client.operation.HLLOperation.fold(binName, indexBitCount);
    }
}
