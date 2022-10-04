package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.Value;
import com.aerospike.client.operation.HLLPolicy;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(
        description = "Server sets union of specified HLL objects with HLL bin. Server does not return a value.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/latest/com/aerospike/client/operation/HLLOperation.html")
)
public class HLLSetUnionOperation extends HLLOperation {
    @Schema(
            description = "The type of operation. It is always " + OperationTypes.HLL_SET_UNION,
            required = true,
            allowableValues = OperationTypes.HLL_SET_UNION
    )
    final public String type = OperationTypes.HLL_SET_UNION;

    @Schema(required = true)
    private List<byte[]> values;

    public HLLSetUnionOperation(String binName, List<byte[]> values) {
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
        return com.aerospike.client.operation.HLLOperation.setUnion(HLLPolicy.Default, binName, asVals);
    }
}
