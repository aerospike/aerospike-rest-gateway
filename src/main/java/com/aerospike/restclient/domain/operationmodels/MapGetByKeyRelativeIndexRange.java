package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.Value;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "TODO",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/MapOperation.html")
)
public class MapGetByKeyRelativeIndexRange extends MapOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.MAP_GET_BY_KEY_RELATIVE_INDEX_RANGE,
            required = true,
            allowableValues = OperationTypes.MAP_GET_BY_KEY_RELATIVE_INDEX_RANGE
    )
    final public String type = OperationTypes.MAP_GET_BY_KEY_RELATIVE_INDEX_RANGE;

    @Schema(required = true)
    private int index;

    @Schema(required = true)
    private Object value;

    @Schema(required = true)
    private MapReturnType mapReturnType;

    private boolean inverted;

    private Integer count;

    public MapGetByKeyRelativeIndexRange(String binName, int index, Object value, MapReturnType mapReturnType) {
        super(binName);
        this.index = index;
        this.value = value;
        this.mapReturnType = mapReturnType;
        inverted = false;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
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
            return com.aerospike.client.cdt.MapOperation.getByKeyRelativeIndexRange(binName, Value.get(value), index,
                    intMapReturnType, asCTX);
        }

        return com.aerospike.client.cdt.MapOperation.getByKeyRelativeIndexRange(binName, Value.get(value), index, count,
                intMapReturnType, asCTX);
    }
}

