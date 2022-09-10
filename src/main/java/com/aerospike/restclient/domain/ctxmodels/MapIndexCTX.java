package com.aerospike.restclient.domain.ctxmodels;

import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Lookup map by index offset.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/CTX.html")
)
public class MapIndexCTX extends CTX {
    @Schema(
            description = "The type of context this object represents. It is always " + AerospikeAPIConstants.MAP_INDEX,
            allowableValues = AerospikeAPIConstants.MAP_INDEX,
            required = true
    )
    @JsonProperty(required = true)
    public final String ctxType = AerospikeAPIConstants.MAP_INDEX;

    @Schema(
            description = "If the index is negative, the resolved index starts backwards from end of list. If an index is out of bounds, a parameter error will be returned. Examples:\n" +
                    "\n" +
                    "* 0: First item.\n" +
                    "* 4: Fifth item.\n" +
                    "* -1: Last item.\n" +
                    "* -3: Third to last item.", required = true
    )
    @JsonProperty(required = true)
    public Integer index;

    MapIndexCTX() {
    }

    @Override
    public com.aerospike.client.cdt.CTX toCTX() {
        return com.aerospike.client.cdt.CTX.mapIndex(index);
    }
}


