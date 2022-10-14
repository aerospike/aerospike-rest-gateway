package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.Value;
import com.aerospike.client.operation.HLLPolicy;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    private final List<byte[]> values;

    @JsonCreator
    public HLLSetUnionOperation(@JsonProperty("binName") String binName, @JsonProperty("values") List<byte[]> values) {
        super(binName);
        this.values = values;
    }

    public com.aerospike.client.Operation toOperation() {
        List<Value.HLLValue> asVals = values.stream().map(Value.HLLValue::new).toList();
        return com.aerospike.client.operation.HLLOperation.setUnion(HLLPolicy.Default, binName, asVals);
    }
}
