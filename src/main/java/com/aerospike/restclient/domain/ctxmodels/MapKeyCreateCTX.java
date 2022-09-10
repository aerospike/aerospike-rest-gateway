package com.aerospike.restclient.domain.ctxmodels;

import com.aerospike.client.Value;
import com.aerospike.client.cdt.MapOrder;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.aerospike.restclient.util.deserializers.ObjectDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Lookup map by base map's key. If the map at key is not found, create it with the given sort order at that key.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/CTX.html")
)
public class MapKeyCreateCTX extends CTX {
    @Schema(
            description = "The type of context this object represents. It is always " + AerospikeAPIConstants.MAP_KEY_CREATE,
            allowableValues = AerospikeAPIConstants.MAP_KEY_CREATE,
            required = true
    )
    @JsonProperty(required = true)
    public final String ctxType = AerospikeAPIConstants.MAP_KEY_CREATE;

    @Schema(description = "String, Integer, or ByteArraySpecifiedType", required = true, example = "my-user-key")
    @JsonDeserialize(using = ObjectDeserializer.class)
    public Object key;

    @Schema(description = "TODO")
    public MapOrder order = MapOrder.UNORDERED;

    MapKeyCreateCTX() {
    }

    ;

    @Override
    public com.aerospike.client.cdt.CTX toCTX() {
        Value asVal = Value.get(key);
        return com.aerospike.client.cdt.CTX.mapKeyCreate(asVal, order);
    }
}
