package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.operation.HLLPolicy;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    private final int indexBitCount;

    private Integer minHashBitCount;

    @JsonCreator
    public HLLInitOperation(@JsonProperty(value = "binName", required = true) String binName,
                            @JsonProperty(value = "indexBitCount", required = true) int indexBitCount) {
        super(binName);
        this.indexBitCount = indexBitCount;
    }

    public Integer getMinHashBitCount() {
        return minHashBitCount;
    }

    public void setMinHashBitCount(Integer minHashBitCount) {
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
