package com.aerospike.restclient.domain.operationmodels;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = " Get the map item at the specified `index`.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/MapOperation.html")
)
public class MapGetByIndexOperation extends MapOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.MAP_GET_BY_INDEX,
            required = true,
            allowableValues = OperationTypes.MAP_GET_BY_INDEX
    )
    final public String type = OperationTypes.MAP_GET_BY_INDEX;

    @Schema(required = true)
    private Integer index;

    @Schema(required = true)
    private MapReturnType mapReturnType;

    private boolean inverted;

    public MapGetByIndexOperation(String binName, Integer index, MapReturnType mapReturnType) {
        super(binName);
        this.index = index;
        this.mapReturnType = mapReturnType;
        inverted = false;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
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

        return com.aerospike.client.cdt.MapOperation.getByIndex(binName, index, mapReturnType.toMapReturnType(inverted),
                asCTX);
    }
}
