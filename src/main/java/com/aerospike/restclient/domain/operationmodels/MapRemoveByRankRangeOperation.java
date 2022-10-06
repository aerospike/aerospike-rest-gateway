package com.aerospike.restclient.domain.operationmodels;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Remove and return `count` values from the map beginning with the value with the specified rank. If `count` is omitted, all items beginning with the specified `rank` will be removed and returned.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/MapOperation.html")
)
public class MapRemoveByRankRangeOperation extends MapOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.MAP_REMOVE_BY_RANK_RANGE,
            required = true,
            allowableValues = OperationTypes.MAP_REMOVE_BY_RANK_RANGE
    )
    final public String type = OperationTypes.MAP_REMOVE_BY_RANK_RANGE;

    @Schema(required = true)
    private int rank;

    @Schema(required = true)
    private MapReturnType mapReturnType;

    private boolean inverted = false;

    private Integer count;

    public MapRemoveByRankRangeOperation(String binName, int rank, MapReturnType mapReturnType) {
        super(binName);
        this.rank = rank;
        this.mapReturnType = mapReturnType;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public MapReturnType getMapReturnType() {
        return mapReturnType;
    }

    public void setMapReturnType(MapReturnType mapReturnType) {
        this.mapReturnType = mapReturnType;
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
        int inMapReturnType = mapReturnType.toMapReturnType(inverted);

        if (count == null) {
            return com.aerospike.client.cdt.MapOperation.removeByRankRange(binName, rank, inMapReturnType, asCTX);
        }

        return com.aerospike.client.cdt.MapOperation.removeByRankRange(binName, rank, count, inMapReturnType, asCTX);
    }
}
