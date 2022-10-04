package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.operation.HLLPolicy;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Server creates a new HLL or resets an existing HLL. Server does not return a value.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/latest/com/aerospike/client/operation/HLLOperation.html")
)
public class HLLInitOperation extends HLLOperation {
    @Schema(
            description = "The type of operation. It is always " + OperationTypes.HLL_INIT,
            required = true,
            allowableValues = OperationTypes.HLL_INIT
    )
    final public String type = OperationTypes.HLL_INIT;

    @Schema(required = true)
    private int indexBitCount;

    private Integer minHashBitCount;

    public HLLInitOperation(String binName, int indexBitCount) {
        super(binName);
        this.indexBitCount = indexBitCount;
    }

    public int getIndexBitCount() {
        return indexBitCount;
    }

    public void setIndexBitCount(int indexBitCount) {
        this.indexBitCount = indexBitCount;
    }

    public int getMinHashBitCount() {
        return minHashBitCount;
    }

    public void setMinHashBitCount(int minHashBitCount) {
        this.minHashBitCount = minHashBitCount;
    }

    public com.aerospike.client.Operation toOperation() {
        if (minHashBitCount == null) {
            return com.aerospike.client.operation.HLLOperation.init(HLLPolicy.Default, binName, indexBitCount);
        }
        return com.aerospike.client.operation.HLLOperation.init(HLLPolicy.Default, binName, indexBitCount,
                minHashBitCount);
    }
}
