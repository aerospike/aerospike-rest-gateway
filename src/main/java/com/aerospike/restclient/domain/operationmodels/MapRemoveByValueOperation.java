package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.Value;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = " Remove and return all map items with a value equal to the specified value.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/MapOperation.html")
)
public class MapRemoveByValueOperation extends MapOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.MAP_REMOVE_BY_VALUE,
            required = true,
            allowableValues = OperationTypes.MAP_REMOVE_BY_VALUE
    )
    final public String type = OperationTypes.MAP_REMOVE_BY_VALUE;

    @Schema(required = true)
    private Object value;

    @Schema(required = true)
    private MapReturnType mapReturnType;

    private boolean inverted = false;

    public MapRemoveByValueOperation(String binName, Object value, MapReturnType mapReturnType) {
        super(binName);
        this.value = value;
        this.mapReturnType = mapReturnType;
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

    public boolean isInverted() {
        return inverted;
    }

    public void setInverted(boolean inverted) {
        this.inverted = inverted;
    }

    @Override
    public com.aerospike.client.Operation toOperation() {
        com.aerospike.client.cdt.CTX[] asCTX = getASCTX();

        return com.aerospike.client.cdt.MapOperation.removeByValue(binName, Value.get(value),
                mapReturnType.toMapReturnType(inverted), asCTX);
    }
}
