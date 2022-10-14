package com.aerospike.restclient.domain.operationmodels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Remove a list item at the specified index.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html")
)
public class ListRemoveOperation extends ListOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.LIST_REMOVE,
            required = true,
            allowableValues = OperationTypes.LIST_REMOVE
    )
    final public String type = OperationTypes.LIST_REMOVE;

    @Schema(required = true)
    private final int index;

    @JsonCreator
    public ListRemoveOperation(@JsonProperty(value = "binName", required = true) String binName,
                               @JsonProperty(value = "index", required = true) int index) {
        super(binName);
        this.index = index;
    }

    @Override
    public com.aerospike.client.Operation toOperation() {
        com.aerospike.client.cdt.CTX[] asCTX = getASCTX();

        return com.aerospike.client.cdt.ListOperation.remove(binName, index, asCTX);
    }
}
