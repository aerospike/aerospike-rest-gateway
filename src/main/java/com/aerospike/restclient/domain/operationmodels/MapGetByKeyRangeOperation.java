package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.Value;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = " Return map values with keys in the specified range. If `keyBegin` is omitted, all map values with key values less than `keyEnd` will be returned. If `keyEnd` is omitted, all map values with a key greater than or equal to `keyBegin` will be returned.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/MapOperation.html")
)
public class MapGetByKeyRangeOperation extends MapOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.MAP_GET_BY_KEY_RANGE,
            required = true,
            allowableValues = OperationTypes.MAP_GET_BY_KEY_RANGE
    )
    final public String type = OperationTypes.MAP_GET_BY_KEY_RANGE;

    @Schema(required = true)
    private MapReturnType mapReturnType;

    private boolean inverted;

    private Object keyBegin;
    private Object keyEnd;

    public MapGetByKeyRangeOperation(String binName, MapReturnType mapReturnType) {
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

    public Object getKeyBegin() {
        return keyBegin;
    }

    public void setKeyBegin(Object keyBegin) {
        this.keyBegin = keyBegin;
    }

    public Object getKeyEnd() {
        return keyEnd;
    }

    public void setKeyEnd(Object keyEnd) {
        this.keyEnd = keyEnd;
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

        if (keyBegin != null) {
            begin = Value.get(keyBegin);
        }

        if (keyEnd != null) {
            end = Value.get(keyEnd);
        }

        return com.aerospike.client.cdt.MapOperation.getByKeyRange(binName, begin, end,
                mapReturnType.toMapReturnType(inverted), asCTX);
    }
}
