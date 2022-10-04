package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.Value;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(
        description = "Server returns estimated number of elements that would be contained by the intersection of these HLL objects.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/latest/com/aerospike/client/operation/HLLOperation.html")
)
public class HLLGetIntersectionCountOperation extends HLLOperation {
    @Schema(
            description = "The type of operation. It is always " + OperationTypes.HLL_INTERSECT_COUNT,
            required = true,
            allowableValues = OperationTypes.HLL_INTERSECT_COUNT
    )
    final public String type = OperationTypes.HLL_INTERSECT_COUNT;

    @Schema(required = true)
    private List<byte[]> values;

    public HLLGetIntersectionCountOperation(String binName, List<byte[]> values) {
        super(binName);
        this.values = values;
    }

    public List<byte[]> getValues() {
        return values;
    }

    public void setValues(List<byte[]> values) {
        this.values = values;
    }

    public com.aerospike.client.Operation toOperation() {
        List<Value.HLLValue> asVals = values.stream().map(Value.HLLValue::new).toList();
        return com.aerospike.client.operation.HLLOperation.getIntersectCount(binName, asVals);
    }
}
