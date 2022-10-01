package com.aerospike.restclient.domain.operationmodels;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = " Return an item, located a specific index, from a list in the specified bin.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html")
)
public class ListGetOperation extends ListOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.LIST_GET,
            required = true,
            allowableValues = OperationTypes.LIST_GET
    )
    final public String type = OperationTypes.LIST_GET;

    @Schema(required = true)
    private Integer index;

    public ListGetOperation(String binName, Integer index) {
        super(binName);
        this.index = index;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    @Override
    public com.aerospike.client.Operation toOperation() {
        com.aerospike.client.cdt.CTX[] asCTX = getASCTX();

        return com.aerospike.client.cdt.ListOperation.get(binName, index, asCTX);
    }
}
