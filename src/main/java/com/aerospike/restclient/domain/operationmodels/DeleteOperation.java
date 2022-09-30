package com.aerospike.restclient.domain.operationmodels;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Delete a record.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/Operation.html")
)
public class DeleteOperation extends Operation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.DELETE,
            required = true,
            allowableValues = OperationTypes.DELETE
    )
    final public String type = OperationTypes.DELETE;

    public DeleteOperation() {
    }

    @Override
    public com.aerospike.client.Operation toOperation() {
        return com.aerospike.client.Operation.delete();
    }
}
