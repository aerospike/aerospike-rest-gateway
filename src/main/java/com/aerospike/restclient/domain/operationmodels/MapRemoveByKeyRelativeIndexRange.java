package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.Value;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "TODO",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/MapOperation.html")
)
public class MapRemoveByKeyRelativeIndexRange extends MapOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.MAP_REMOVE_BY_KEY_RELATIVE_INDEX_RANGE,
            required = true,
            allowableValues = OperationTypes.MAP_REMOVE_BY_KEY_RELATIVE_INDEX_RANGE
    )
    final public String type = OperationTypes.MAP_REMOVE_BY_KEY_RELATIVE_INDEX_RANGE;

    @Schema(required = true)
    private final int index;

    @Schema(required = true)
    private final Object value;

    @Schema(required = true)
    private final MapReturnType mapReturnType;

    private boolean inverted;

    private Integer count;

    @JsonCreator
    public MapRemoveByKeyRelativeIndexRange(@JsonProperty(value = "binName", required = true) String binName,
                                            @JsonProperty(value = "index", required = true) int index,
                                            @JsonProperty(value = "value", required = true) Object value, @JsonProperty(
            value = "mapReturnType", required = true
    ) MapReturnType mapReturnType) {
        super(binName);
        this.index = index;
        this.value = value;
        this.mapReturnType = mapReturnType;
        inverted = false;
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
            return com.aerospike.client.cdt.MapOperation.removeByKeyRelativeIndexRange(binName, Value.get(value), index,
                    intMapReturnType, asCTX);
        }

        return com.aerospike.client.cdt.MapOperation.removeByKeyRelativeIndexRange(binName, Value.get(value), index,
                count, intMapReturnType, asCTX);
    }
}
