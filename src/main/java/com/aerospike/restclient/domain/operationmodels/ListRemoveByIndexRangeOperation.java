package com.aerospike.restclient.domain.operationmodels;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = " Remove and return `count` items beginning at the specified `index` from the list. If `count` is omitted, all items beginning from `index` will be removed and returned. Requires Aerospike Server 3.16.0.1 or later",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html")
)
public class ListRemoveByIndexRangeOperation extends ListOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.LIST_REMOVE_BY_INDEX_RANGE,
            required = true,
            allowableValues = OperationTypes.LIST_REMOVE_BY_INDEX_RANGE
    )
    final public String type = OperationTypes.LIST_REMOVE_BY_INDEX_RANGE;

    @Schema(required = true)
    private Integer index;

    @Schema(required = true)
    private ListReturnType listReturnType;

    private boolean inverted;

    private Integer count;

    public ListRemoveByIndexRangeOperation(String binName, Integer index, ListReturnType listReturnType) {
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

        if (count == null) {
            return com.aerospike.client.cdt.ListOperation.removeByIndexRange(binName, index, intListReturnType, asCTX);
        }

        return com.aerospike.client.cdt.ListOperation.removeByIndexRange(binName, index, count, intListReturnType,
                asCTX);
    }
}
