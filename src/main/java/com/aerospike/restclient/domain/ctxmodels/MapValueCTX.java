package com.aerospike.restclient.domain.ctxmodels;

import com.aerospike.client.Value;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.aerospike.restclient.util.deserializers.ObjectDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Lookup map by value.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/CTX.html")
)
public class MapValueCTX extends CTX {
    @Schema(
            description = "The type of context this object represents. It is always " + AerospikeAPIConstants.MAP_VALUE,
            allowableValues = AerospikeAPIConstants.MAP_VALUE,
            required = true
    )
    @JsonProperty(required = true)
    public final String type = AerospikeAPIConstants.MAP_VALUE;

    @Schema(required = true)
    @JsonDeserialize(using = ObjectDeserializer.class)
    public Object value;

    MapValueCTX() {
    }

    public MapValueCTX(Object value) {
        this.value = value;
    }

    @Override
    public com.aerospike.client.cdt.CTX toCTX() {
        Value asVal = Value.get(value);
        return com.aerospike.client.cdt.CTX.mapValue(asVal);
    }
}

