package com.aerospike.restclient.domain.operationmodels;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Get all map items with indexes in the range `[index, index + count)` . If `count` is omitted, all items beginning with the item at the specified index will be returned.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/MapOperation.html")
)
public class MapGetByIndexRangeOperation extends MapOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.MAP_GET_BY_INDEX_RANGE,
            required = true,
            allowableValues = OperationTypes.MAP_GET_BY_INDEX_RANGE
    )
    final public String type = OperationTypes.MAP_GET_BY_INDEX_RANGE;

    @Schema(required = true)
    private Integer index;

    @Schema(required = true)
    private MapReturnType mapReturnType;

    private boolean inverted;

    private Integer count;

    public MapGetByIndexRangeOperation(String binName, Integer index, MapReturnType mapReturnType) {
        super(binName);
        this.index = index;
        this.mapReturnType = mapReturnType;
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
        int intMapReturnType = mapReturnType.toMapReturnType(inverted);

        if (count == null) {
            return com.aerospike.client.cdt.MapOperation.getByIndexRange(binName, index, intMapReturnType, asCTX);
        }

        return com.aerospike.client.cdt.MapOperation.getByIndexRange(binName, index, count, intMapReturnType, asCTX);
    }
}

