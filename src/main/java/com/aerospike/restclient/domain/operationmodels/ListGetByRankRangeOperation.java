package com.aerospike.restclient.domain.operationmodels;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = " Return `count` items beginning with the specified rank. If `count` is omitted, all items beginning with specified rank will be returned. Requires Aerospike Server `3.16.0.1` or later.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html")
)
public class ListGetByRankRangeOperation extends ListOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.LIST_GET_BY_RANK_RANGE,
            required = true,
            allowableValues = OperationTypes.LIST_GET_BY_RANK_RANGE
    )
    final public String type = OperationTypes.LIST_GET_BY_RANK_RANGE;

    @Schema(required = true)
    private Integer rank;

    @Schema(required = true)
    private ListReturnType listReturnType;
    private boolean inverted;

    private Integer count;

    public ListGetByRankRangeOperation(String binName, Integer rank, ListReturnType listReturnType) {
        super(binName);
        this.rank = rank;
        this.listReturnType = listReturnType;
        inverted = false;
    }

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
            return com.aerospike.client.cdt.ListOperation.getByRankRange(binName, rank, count, intListReturnType,
                    asCTX);
        }

        return com.aerospike.client.cdt.ListOperation.getByRankRange(binName, rank, intListReturnType, asCTX);
    }
}
