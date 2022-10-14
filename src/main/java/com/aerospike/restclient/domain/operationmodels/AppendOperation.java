package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.Bin;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Append a `value` to the item in the specified `binName`",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/Operation.html")
)
public class AppendOperation extends Operation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.APPEND,
            required = true,
            allowableValues = OperationTypes.APPEND
    )
    final public String type = OperationTypes.APPEND;

    @Schema(required = true)
    private final String binName;

    @Schema(required = true)
    private final String value;

    @JsonCreator
    public AppendOperation(@JsonProperty(value = "binName", required = true) String binName,
                           @JsonProperty(value = "value", required = true) String value) {
        this.binName = binName;
        this.value = value;
    }

    @Override
    public com.aerospike.client.Operation toOperation() {
        return com.aerospike.client.Operation.append(new Bin(binName, value));
    }
}
