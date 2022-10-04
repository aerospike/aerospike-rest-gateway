package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.Bin;
import com.aerospike.client.Value;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Store a `value` in the specified `binName`.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/Operation.html")
)
public class PutOperation extends Operation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.PUT,
            required = true,
            allowableValues = OperationTypes.PUT
    )
    final public String type = OperationTypes.PUT;

    @Schema(required = true)
    private String binName;

    @Schema(required = true)
    private Object value;

    public PutOperation() {
    }

    public String getBinName() {
        return binName;
    }

    public void setBinName(String binName) {
        this.binName = binName;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public com.aerospike.client.Operation toOperation() {
        return com.aerospike.client.Operation.put(new Bin(binName, Value.get(value)));
    }
}
