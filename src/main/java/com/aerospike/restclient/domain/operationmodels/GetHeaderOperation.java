package com.aerospike.restclient.domain.operationmodels;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = " Return metadata about a record.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/Operation.html")
)
public class GetHeaderOperation extends Operation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.GET_HEADER,
            required = true,
            allowableValues = OperationTypes.GET_HEADER
    )
    final public String type = OperationTypes.GET_HEADER;

    public GetHeaderOperation() {
    }

    @Override
    public com.aerospike.client.Operation toOperation() {
        return com.aerospike.client.Operation.getHeader();
    }
}
