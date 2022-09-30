package com.aerospike.restclient.domain.operationmodels;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = " Remove `count` items from a list, beginning with the item with the specified `rank`. If `count` is omitted, all items beginning with the specified `rank` will be removed and returned. Requires Aerospike Server `3.16.0.1` or later.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html")
)
public class ListRemoveByRankRangeOperation extends ListOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.LIST_REMOVE_BY_RANK_RANGE,
            required = true,
            allowableValues = OperationTypes.LIST_REMOVE_BY_RANK_RANGE
    )
    final public String type = OperationTypes.LIST_REMOVE_BY_RANK_RANGE;

    @Schema(required = true)
    private Integer rank;

    @Schema(required = true)
    private ListReturnType listReturnType;

    private boolean inverted;

    public ListRemoveByRankRangeOperation(String binName, Integer rank, ListReturnType listReturnType) {
        super(binName);
        this.rank = rank;
        this.listReturnType = listReturnType;
    }

    private Integer count;

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
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

    @Override
    public com.aerospike.client.Operation toOperation() {
        com.aerospike.client.cdt.CTX[] asCTX = getASCTX();
        int intListReturnType = listReturnType.toListReturnType(inverted);

        if (count == null) {
            return com.aerospike.client.cdt.ListOperation.removeByRankRange(binName, rank, intListReturnType, asCTX);
        }

        return com.aerospike.client.cdt.ListOperation.removeByRankRange(binName, rank, count, intListReturnType, asCTX);
    }
}
