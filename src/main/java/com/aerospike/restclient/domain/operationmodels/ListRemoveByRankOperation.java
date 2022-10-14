package com.aerospike.restclient.domain.operationmodels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Remove a list item with the specified rank. Requires Aerospike Server `3.16.0.1` or later",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html")
)
public class ListRemoveByRankOperation extends ListOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.LIST_REMOVE_BY_RANK,
            required = true,
            allowableValues = OperationTypes.LIST_REMOVE_BY_RANK
    )
    final public String type = OperationTypes.LIST_REMOVE_BY_RANK;

    @Schema(required = true)
    private final int rank;

    @Schema(required = true)
    private final ListReturnType listReturnType;

    private boolean inverted;

    @JsonCreator
    public ListRemoveByRankOperation(@JsonProperty(value = "binName", required = true) String binName,
                                     @JsonProperty(value = "rank", required = true) int rank, @JsonProperty(
            value = "listReturnType",
            required = true
    ) ListReturnType listReturnType) {
        super(binName);
        this.rank = rank;
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

        return com.aerospike.client.cdt.ListOperation.removeByRank(binName, rank,
                listReturnType.toListReturnType(inverted), asCTX);
    }
}
