package com.aerospike.restclient.domain.ctxmodels;

import com.aerospike.client.Value;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.aerospike.restclient.util.deserializers.ObjectDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Lookup map by key.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/CTX.html")
)
public class MapKeyCTX extends CTX {
    @Schema(
            description = "The type of context this object represents. It is always " + AerospikeAPIConstants.CTX.MAP_KEY,
            allowableValues = AerospikeAPIConstants.CTX.MAP_KEY,
            required = true
    )
    @JsonProperty(required = true)
    public final String type = AerospikeAPIConstants.CTX.MAP_KEY;

    @JsonDeserialize(using = ObjectDeserializer.class)
    @Schema(description = "String, Integer, or ByteArraySpecifiedType", required = true, example = "my-user-key")
    public Object key;

    public MapKeyCTX() {
    }

    public MapKeyCTX(Object key) {
        this.key = key;
    }

    @Override
    public com.aerospike.client.cdt.CTX toCTX() {
        Value asVal = Value.get(key);
        return com.aerospike.client.cdt.CTX.mapKey(asVal);
    }
}
