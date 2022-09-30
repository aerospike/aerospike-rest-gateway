package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.Value;
import com.aerospike.client.cdt.MapPolicy;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = " Increment the map value with the specified key by the specified amount.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/MapOperation.html")
)
public class MapIncrementOperation extends MapOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.MAP_INCREMENT,
            required = true,
            allowableValues = OperationTypes.MAP_INCREMENT
    )
    final public String type = OperationTypes.MAP_INCREMENT;

    @Schema(required = true)
    private Number incr;

    @Schema(required = true)
    private Object key;

    private MapPolicy mapPolicy;

    public MapIncrementOperation(String binName, Number incr, Object key) {
        super(binName);
        this.incr = incr;
        this.key = key;
    }

    public Number getIncr() {
        return incr;
    }

    public void setIncr(Number incr) {
        this.incr = incr;
    }

    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
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

        Value asIncr;

        // TODO
        if (incr instanceof Number) {
            asIncr = Value.get(incr.intValue());
        } else {
            asIncr = Value.get(incr.doubleValue());
        }

        return com.aerospike.client.cdt.MapOperation.increment(mapPolicy, binName, Value.get(key), asIncr, asCTX);
    }
}
