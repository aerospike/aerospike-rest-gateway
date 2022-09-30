package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.Value;
import com.aerospike.client.cdt.MapPolicy;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.HashMap;
import java.util.Map;

@Schema(
        description = " Store multiple values into the map with the specified keys.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/MapOperation.html")
)
public class MapPutItemsOperation extends MapOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.MAP_PUT_ITEMS,
            required = true,
            allowableValues = OperationTypes.MAP_PUT_ITEMS
    )
    final public String type = OperationTypes.MAP_PUT_ITEMS;

    @Schema(required = true)
    private Map<Object, Object> map;

    private MapPolicy mapPolicy;

    public MapPutItemsOperation(String binName, Map<Object, Object> map) {
        super(binName);
        this.map = map;
    }

    public Map<Object, Object> getMap() {
        return map;
    }

    public void setMap(Map<Object, Object> map) {
        this.map = map;
    }

    public MapPolicy getMapPolicy() {
        return mapPolicy;
    }

    public void setMapPolicy(MapPolicy mapPolicy) {
        this.mapPolicy = mapPolicy;
    }

    @Override
    public com.aerospike.client.Operation toOperation() {
        com.aerospike.client.cdt.CTX[] asCTX = getASCTX();
        Map<Value, Value> valueMap = new HashMap<>();

        if (mapPolicy == null) {
            mapPolicy = MapPolicy.Default;
        }

        for (Object key : map.keySet()) {
            valueMap.put(Value.get(key), Value.get(map.get(key)));
        }

        return com.aerospike.client.cdt.MapOperation.putItems(mapPolicy, binName, valueMap, asCTX);
    }
}
