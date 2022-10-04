package com.aerospike.restclient.domain.operationmodels;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Remove a list item at the specified index. Requires Aerospike Server `3.16.0.1` or later.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html")
)
public class ListRemoveByIndexOperation extends ListOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.LIST_REMOVE_BY_INDEX,
            required = true,
            allowableValues = OperationTypes.LIST_REMOVE_BY_INDEX
    )
    final public String type = OperationTypes.LIST_REMOVE_BY_INDEX;

    @Schema(required = true)
    private Integer index;

    @Schema(required = true)
    private ListReturnType listReturnType;

    private boolean inverted;

    public ListRemoveByIndexOperation(String binName, Integer index, ListReturnType listReturnType) {
        super(binName);
        this.index = index;
        this.listReturnType = listReturnType;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public ListReturnType getListReturnType() {
        return listReturnType;
    }

    public void setListReturnType(ListReturnType listReturnType) {
        this.listReturnType = listReturnType;
    }

    public boolean isInverted() {
        return inverted;
    }

    public void setInverted(boolean inverted) {
        this.inverted = inverted;
    }

    @Override
    public com.aerospike.client.Operation toOperation() {
        com.aerospike.client.cdt.CTX[] asCTX = getASCTX();

        return com.aerospike.client.cdt.ListOperation.removeByIndex(binName, index,
                listReturnType.toListReturnType(inverted), asCTX);
    }
}
