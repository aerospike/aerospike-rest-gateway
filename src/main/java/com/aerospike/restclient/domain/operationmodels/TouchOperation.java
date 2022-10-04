package com.aerospike.restclient.domain.operationmodels;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Reset a record’s TTL and increment its generation.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/Operation.html")
)
public class TouchOperation extends Operation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.TOUCH,
            required = true,
            allowableValues = OperationTypes.TOUCH
    )
    final public String type = OperationTypes.TOUCH;

    public TouchOperation() {
    }

    @Override
    public com.aerospike.client.Operation toOperation() {
        return com.aerospike.client.Operation.touch();
    }
}
