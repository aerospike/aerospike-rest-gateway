package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.Value;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Remove and return all map items with value in the range `[valueBegin, valueEnd)`. If `valueBegin` is omitted, all map items with a value less than `valueEnd` will be removed and returned. If `valueEnd` is omitted, all map items with a value greater than or equal to `valueBegin` will be removed and returned.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/MapOperation.html")
)
public class MapRemoveByValueRangeOperation extends MapOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.MAP_REMOVE_BY_VALUE_RANGE,
            required = true,
            allowableValues = OperationTypes.MAP_REMOVE_BY_VALUE_RANGE
    )
    final public String type = OperationTypes.MAP_REMOVE_BY_VALUE_RANGE;

    @Schema(required = true)
    private MapReturnType mapReturnType;

    private boolean inverted;

    private Object valueBegin;

    private Object valueEnd;

    public MapRemoveByValueRangeOperation(String binName, MapReturnType mapReturnType) {
        super(binName);
        this.mapReturnType = mapReturnType;
        inverted = false;
    }

    public MapReturnType getMapReturnType() {
        return mapReturnType;
    }

    public void setMapReturnType(MapReturnType mapReturnType) {
        this.mapReturnType = mapReturnType;
    }

    public Object getValueBegin() {
        return valueBegin;
    }

    public void setValueBegin(Object valueBegin) {
        this.valueBegin = valueBegin;
    }

    public Object getValueEnd() {
        return valueEnd;
    }

    public void setValueEnd(Object valueEnd) {
        this.valueEnd = valueEnd;
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
        Value begin = null;
        Value end = null;

        if (valueBegin != null) {
            begin = Value.get(valueBegin);
        }

        if (valueEnd != null) {
            end = Value.get(valueEnd);
        }

        return com.aerospike.client.cdt.MapOperation.removeByValueRange(binName, begin, end,
                mapReturnType.toMapReturnType(inverted), asCTX);
    }
}
