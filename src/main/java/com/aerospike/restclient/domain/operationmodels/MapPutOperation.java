package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.Value;
import com.aerospike.client.cdt.MapPolicy;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = " Store the specified value into the map in the specified bin with the specified key. Equivalent to `Map[key] = value`.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/MapOperation.html")
)
public class MapPutOperation extends MapOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.MAP_PUT,
            required = true,
            allowableValues = OperationTypes.MAP_PUT
    )
    final public String type = OperationTypes.MAP_PUT;

    @Schema(required = true)
    private Object key;

    @Schema(required = true)
    private Object value;

    private MapPolicy mapPolicy;

    public MapPutOperation(String binName, Object key, Object value) {
        super(binName);
        this.key = key;
        this.value = value;
    }

    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
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

        if (mapPolicy == null) {
            mapPolicy = MapPolicy.Default;
        }

        return com.aerospike.client.cdt.MapOperation.put(mapPolicy, binName, Value.get(key), Value.get(value), asCTX);
    }
}
