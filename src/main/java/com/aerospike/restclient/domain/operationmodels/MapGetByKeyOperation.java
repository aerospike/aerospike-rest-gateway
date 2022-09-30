package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.Value;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = " Return the value with the specified key from the map.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/MapOperation.html")
)
public class MapGetByKeyOperation extends MapOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.MAP_GET_BY_KEY,
            required = true,
            allowableValues = OperationTypes.MAP_GET_BY_KEY
    )
    final public String type = OperationTypes.MAP_GET_BY_KEY;

    @Schema(required = true)
    private Object key;

    @Schema(required = true)
    private MapReturnType mapReturnType;

    private boolean inverted;

    public MapGetByKeyOperation(String binName, Object key, MapReturnType mapReturnType) {
        super(binName);
        this.key = key;
        this.mapReturnType = mapReturnType;
        inverted = false;
    }

    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
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
        return com.aerospike.client.cdt.MapOperation.getByKey(binName, Value.get(key),
                mapReturnType.toMapReturnType(inverted), asCTX);
    }
}
