package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.Value;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(
        description = " Remove values with the specified keys from the map. Requires Aerospike Server `3.16.0.1` or later",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/MapOperation.html")
)
public class MapGetByKeyListOperation extends MapOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.MAP_GET_BY_KEY_LIST,
            required = true,
            allowableValues = OperationTypes.MAP_GET_BY_KEY_LIST
    )
    final public String type = OperationTypes.MAP_GET_BY_KEY_LIST;

    @Schema(required = true)
    private List<Object> keys;

    @Schema(required = true)
    private MapReturnType mapReturnType;

    private boolean inverted;

    public MapGetByKeyListOperation(String binName, List<Object> keys, MapReturnType mapReturnType) {
        super(binName);
        this.keys = keys;
        this.mapReturnType = mapReturnType;
    }

    public List<Object> getKeys() {
        return keys;
    }

    public void setKeys(List<Object> keys) {
        this.keys = keys;
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
        List<Value> asKeys = keys.stream().map(Value::get).toList();
        com.aerospike.client.cdt.CTX[] asCTX = getASCTX();

        return com.aerospike.client.cdt.MapOperation.getByKeyList(binName, asKeys,
                mapReturnType.toMapReturnType(inverted), asCTX);
    }
}
