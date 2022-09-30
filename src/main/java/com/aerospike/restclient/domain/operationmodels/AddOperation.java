package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.Bin;
import com.aerospike.client.Value;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = " Increment the value of an item in the specified `binName` by the value of `incr`",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/Operation.html")
)
public class AddOperation extends Operation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.ADD,
            required = true,
            allowableValues = OperationTypes.ADD
    )
    final public String type = OperationTypes.ADD;

    @Schema(required = true)
    private String binName;

    @Schema(required = true)
    private Number incr;

    public AddOperation(String binName, Number incr) {
        this.binName = binName;
        this.incr = incr;
    }

    public String getBinName() {
        return binName;
    }

    public void setBinName(String binName) {
        this.binName = binName;
    }

    public Number getIncr() {
        return incr;
    }

    public void setIncr(Number incr) {
        this.incr = incr;
    }

    @Override
    public com.aerospike.client.Operation toOperation() {
        Value val;

        if (incr instanceof Integer || incr instanceof Long) {
            val = Value.get(incr.intValue());
        } else {
            val = Value.get(incr.doubleValue());
        }

        return com.aerospike.client.Operation.add(new Bin(binName, val));
    }
}
