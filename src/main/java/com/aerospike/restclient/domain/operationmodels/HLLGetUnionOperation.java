package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.Value;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(
        description = "Server returns an HLL object that is the union of all specified HLL objects in the list with the HLL bin.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/latest/com/aerospike/client/operation/HLLOperation.html")
)
public class HLLGetUnionOperation extends HLLOperation {
    @Schema(
            description = "The type of operation. It is always " + OperationTypes.HLL_UNION,
            required = true,
            allowableValues = OperationTypes.HLL_UNION
    )
    final public String type = OperationTypes.HLL_UNION;

    @Schema(required = true)
    private List<byte[]> values;

    public HLLGetUnionOperation(String binName, List<byte[]> values) {
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
        return com.aerospike.client.operation.HLLOperation.getUnion(binName, asVals);
    }
}
