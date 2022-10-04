package com.aerospike.restclient.domain.operationmodels;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Return the map value with the specified rank.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/MapOperation.html")
)
public class MapGetByRankOperation extends MapOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.MAP_GET_BY_RANK,
            required = true,
            allowableValues = OperationTypes.MAP_GET_BY_RANK
    )
    final public String type = OperationTypes.MAP_GET_BY_RANK;

    @Schema(required = true)
    private Integer rank;

    @Schema(required = true)
    private MapReturnType mapReturnType;

    private boolean inverted;

    public MapGetByRankOperation(String binName, Integer rank, MapReturnType mapReturnType) {
        super(binName);
        this.rank = rank;
        this.mapReturnType = mapReturnType;
        inverted = false;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public MapReturnType getMapReturnType() {
        return mapReturnType;
    }

    public void setMapReturnType(MapReturnType mapReturnType) {
        this.mapReturnType = mapReturnType;
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

        return com.aerospike.client.cdt.MapOperation.getByRank(binName, rank, mapReturnType.toMapReturnType(inverted),
                asCTX);
    }
}
