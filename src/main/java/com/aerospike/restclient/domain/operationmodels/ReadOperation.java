package com.aerospike.restclient.domain.operationmodels;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Return the value of a specified `binName`.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/Operation.html")
)
public class ReadOperation extends Operation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.READ,
            required = true,
            allowableValues = OperationTypes.READ
    )
    final public String type = OperationTypes.READ;

    @Schema(required = true)
    private String binName;

    public ReadOperation(@JsonProperty("binName") String binName) {
        this.binName = binName;
    }

    public String getBinName() {
        return binName;
    }

    public void setBinName(String binName) {
        this.binName = binName;
    }

    @Override
    public com.aerospike.client.Operation toOperation() {
        return com.aerospike.client.Operation.get(binName);
    }
}
