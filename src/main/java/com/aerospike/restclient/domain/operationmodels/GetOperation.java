package com.aerospike.restclient.domain.operationmodels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Return the contents of a record",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/Operation.html")
)
public class GetOperation extends Operation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.GET,
            required = true,
            allowableValues = OperationTypes.GET
    )
    final public String type = OperationTypes.GET;

    private final String binName;

    @JsonCreator
    public GetOperation(@JsonProperty(value = "binName") String binName) {
        this.binName = binName;
    }

    @Override
    public com.aerospike.client.Operation toOperation() {
        if (binName == null) {
            return com.aerospike.client.Operation.get();
        }

        return com.aerospike.client.Operation.get(binName);
    }
}


