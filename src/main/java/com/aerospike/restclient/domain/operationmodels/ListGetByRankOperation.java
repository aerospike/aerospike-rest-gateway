package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.restclient.domain.ctxmodels.CTX;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(
        description = " Return a list item with the specified `rank`. Requires Aerospike Server `3.16.0.1` or later.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html")
)
public class ListGetByRankOperation extends ListOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.LIST_GET_BY_RANK,
            required = true,
            allowableValues = OperationTypes.LIST_GET_BY_RANK
    )
    final public String type = OperationTypes.LIST_GET_BY_RANK;

    @Schema(required = true)
    private Integer rank;

    @Schema(required = true)
    private ListReturnType listReturnType;

    private boolean inverted;

    public ListGetByRankOperation(String binName, Integer rank, ListReturnType listReturnType, List<CTX> ctx) {
        super(binName);
        this.rank = rank;
        this.listReturnType = listReturnType;
        this.ctx = ctx;
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

    public boolean isInverted() {
        return inverted;
    }

    public void setInverted(boolean inverted) {
        this.inverted = inverted;
    }

    @Override
    public com.aerospike.client.Operation toOperation() {
        com.aerospike.client.cdt.CTX[] asCTX = getASCTX();
        return com.aerospike.client.cdt.ListOperation.getByRank(binName, rank,
                listReturnType.toListReturnType(inverted), asCTX);
    }
}
