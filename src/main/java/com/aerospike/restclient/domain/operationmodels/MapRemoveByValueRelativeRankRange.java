package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.Value;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "TODO",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/MapOperation.html")
)
public class MapRemoveByValueRelativeRankRange extends MapOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.MAP_REMOVE_BY_VALUE_RELATIVE_RANK_RANGE,
            required = true,
            allowableValues = OperationTypes.MAP_REMOVE_BY_VALUE_RELATIVE_RANK_RANGE
    )
    final public String type = OperationTypes.MAP_REMOVE_BY_VALUE_RELATIVE_RANK_RANGE;

    @Schema(required = true)
    private Integer rank;

    @Schema(required = true)
    private Object value;

    @Schema(required = true)
    private MapReturnType mapReturnType;

    private boolean inverted = false;

    private Integer count;

    public MapRemoveByValueRelativeRankRange(String binName, Integer index, Object value, MapReturnType mapReturnType) {
        super(binName);
        this.rank = index;
        this.value = value;
        this.mapReturnType = mapReturnType;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
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

        if (count == null) {
            return com.aerospike.client.cdt.MapOperation.removeByValueRelativeRankRange(binName, Value.get(value), rank,
                    mapReturnType.toMapReturnType(inverted), asCTX);
        }

        return com.aerospike.client.cdt.MapOperation.removeByValueRelativeRankRange(binName, Value.get(value), rank,
                count, mapReturnType.toMapReturnType(inverted), asCTX);
    }
}
