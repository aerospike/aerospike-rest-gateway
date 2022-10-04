package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.restclient.domain.ctxmodels.CTX;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(
        description = "Return a specified amount of items beginning at a specific index, from a list in the specified bin. If `count` is not provided, all items from `index` until the end of the list will be returned. Requires Aerospike Server `3.16.0.1` or later",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html")
)
public class ListGetByIndexRangeOperation extends ListOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.LIST_GET_BY_INDEX_RANGE,
            required = true,
            allowableValues = OperationTypes.LIST_GET_BY_INDEX_RANGE
    )
    final public String type = OperationTypes.LIST_GET_BY_INDEX_RANGE;

    @Schema(required = true)
    private Integer index;

    @Schema(required = true)
    private ListReturnType listReturnType;

    private boolean inverted;

    private Integer count;

    public ListGetByIndexRangeOperation(String binName, Integer index, ListReturnType listReturnType, List<CTX> ctx,
                                        Integer count) {
        super(binName);
        this.index = index;
        this.listReturnType = listReturnType;
        this.ctx = ctx;
        this.count = count;
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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
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
        int intListReturnType = listReturnType.toListReturnType(inverted);

        if (count != null) {
            return com.aerospike.client.cdt.ListOperation.getByIndexRange(binName, index, count, intListReturnType,
                    asCTX);
        }

        return com.aerospike.client.cdt.ListOperation.getByIndexRange(binName, index, intListReturnType, asCTX);
    }
}
