package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.Value;
import com.aerospike.client.operation.HLLPolicy;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(
        description = "Server adds values to HLL set. If HLL bin does not exist, use indexBitCount and optionally minHashBitCount to create HLL bin. Server returns number of entries that caused HLL to update a register.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/latest/com/aerospike/client/operation/HLLOperation.html")
)
public class HLLAddOperation extends HLLOperation {
    @Schema(
            description = "The type of operation. It is always " + OperationTypes.HLL_ADD,
            required = true,
            allowableValues = OperationTypes.HLL_ADD
    )
    final public String type = OperationTypes.HLL_ADD;

    @Schema(required = true)
    private final List<Object> values;

    private Integer indexBitCount;

    private Integer minHashBitCount;

    @JsonCreator
    public HLLAddOperation(@JsonProperty("binName") String binName, @JsonProperty("values") List<Object> values) {
        super(binName);
        this.values = values;
    }

    public void setIndexBitCount(Integer indexBitCount) {
        this.indexBitCount = indexBitCount;
    }

    public void setMinHashBitCount(Integer minHashBitCount) {
        this.minHashBitCount = minHashBitCount;
    }

    public Integer getIndexBitCount() {
        return indexBitCount;
    }

    public Integer getMinHashBitCount() {
        return minHashBitCount;
    }

    public com.aerospike.client.Operation toOperation() {
        List<Value> asVals = values.stream().map(Value::get).toList();

        if (indexBitCount != null) {
            if (minHashBitCount != null) {
                return com.aerospike.client.operation.HLLOperation.add(HLLPolicy.Default, binName, asVals,
                        indexBitCount, minHashBitCount);
            }

            return com.aerospike.client.operation.HLLOperation.add(HLLPolicy.Default, binName, asVals, indexBitCount);
        }

        return com.aerospike.client.operation.HLLOperation.add(HLLPolicy.Default, binName, asVals);
    }
}
