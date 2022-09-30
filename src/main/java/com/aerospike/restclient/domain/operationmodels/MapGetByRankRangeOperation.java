package com.aerospike.restclient.domain.operationmodels;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = " Return `count` values from the map beginning with the value with the specified `rank`. If `count` is omitted, all items with a `rank` greater than or equal to the specified `rank` will be returned.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/MapOperation.html")
)
public class MapGetByRankRangeOperation extends MapOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.MAP_GET_BY_RANK_RANGE,
            required = true,
            allowableValues = OperationTypes.MAP_GET_BY_RANK_RANGE
    )
    final public String type = OperationTypes.MAP_GET_BY_RANK_RANGE;

    @Schema(required = true)
    private MapReturnType mapReturnType;

    @Schema(required = true)
    private Integer rank;

    private Integer count;

    private boolean inverted;

    public MapGetByRankRangeOperation(String binName, MapReturnType mapReturnType, Integer rank) {
        super(binName);
        this.mapReturnType = mapReturnType;
        this.rank = rank;
        inverted = false;
    }

    public MapReturnType getMapReturnType() {
        return mapReturnType;
    }

    public void setMapReturnType(MapReturnType mapReturnType) {
        this.mapReturnType = mapReturnType;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public boolean getInverted() {
        return inverted;
    }

    public void setInverted(boolean inverted) {
        this.inverted = inverted;
    }

    public boolean isInverted() {
        return inverted;
    }

    @Override
    public com.aerospike.client.Operation toOperation() {
        com.aerospike.client.cdt.CTX[] asCTX = getASCTX();
        int intMapReturnType = mapReturnType.toMapReturnType(inverted);

        if (count == null) {
            return com.aerospike.client.cdt.MapOperation.getByRankRange(binName, rank, intMapReturnType, asCTX);
        }

        return com.aerospike.client.cdt.MapOperation.getByRankRange(binName, rank, intMapReturnType, count, asCTX);
    }
}
