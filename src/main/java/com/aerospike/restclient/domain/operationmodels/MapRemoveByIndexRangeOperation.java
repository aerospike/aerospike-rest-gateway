package com.aerospike.restclient.domain.operationmodels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Remove all map items with indexes in the range `[index, index + count)` . If `count` is omitted, all items beginning with the item at the specified index will be removed.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/MapOperation.html")
)
public class MapRemoveByIndexRangeOperation extends MapOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.MAP_REMOVE_BY_INDEX_RANGE,
            required = true,
            allowableValues = OperationTypes.MAP_REMOVE_BY_INDEX_RANGE
    )
    final public String type = OperationTypes.MAP_REMOVE_BY_INDEX_RANGE;

    @Schema(required = true)
    private final int index;

    @Schema(required = true)
    private final MapReturnType mapReturnType;

    private boolean inverted;

    private Integer count;

    @JsonCreator
    public MapRemoveByIndexRangeOperation(@JsonProperty(value = "binName", required = true) String binName,
                                          @JsonProperty(value = "index", required = true) int index, @JsonProperty(
            value = "mapReturnType",
            required = true
    ) MapReturnType mapReturnType) {
        super(binName);
        this.index = index;
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
            return com.aerospike.client.cdt.MapOperation.removeByIndexRange(binName, index, intMapReturnType, asCTX);
        }

        return com.aerospike.client.cdt.MapOperation.removeByIndexRange(binName, index, count, intMapReturnType, asCTX);
    }
}
