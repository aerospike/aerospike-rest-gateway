package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.restclient.domain.ctxmodels.CTX;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(
        description = "Return an item, located a specific index, from a list in the specified bin. The value of `listReturnType` determines what will be returned. Requires Aerospike Server `3.16.0.1` or greater.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html")
)
public class ListGetByIndexOperation extends ListOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.LIST_GET_BY_INDEX,
            required = true,
            allowableValues = OperationTypes.LIST_GET_BY_INDEX
    )
    final public String type = OperationTypes.LIST_GET_BY_INDEX;

    @Schema(required = true)
    private Integer index;

    @Schema(required = true)
    private ListReturnType listReturnType;

    private boolean inverted;

    public ListGetByIndexOperation(String binName, Integer index, ListReturnType listReturnType, List<CTX> ctx) {
        super(binName);
        this.index = index;
        this.listReturnType = listReturnType;
        this.ctx = ctx;
        inverted = false;
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
        return com.aerospike.client.cdt.ListOperation.getByIndex(binName, index,
                listReturnType.toListReturnType(inverted), asCTX);
    }
}
