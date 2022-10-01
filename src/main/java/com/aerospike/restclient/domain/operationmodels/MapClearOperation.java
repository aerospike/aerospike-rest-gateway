package com.aerospike.restclient.domain.operationmodels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = " Empty the specified map.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/MapOperation.html")
)
public class MapClearOperation extends MapOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.MAP_CLEAR,
            required = true,
            allowableValues = OperationTypes.MAP_CLEAR
    )
    final public String type = OperationTypes.MAP_CLEAR;

    @JsonCreator
    public MapClearOperation(@JsonProperty("binName") String binName) {
        super(binName);
    }

    @Override
    public com.aerospike.client.Operation toOperation() {
        com.aerospike.client.cdt.CTX[] asCTX = getASCTX();

        return com.aerospike.client.cdt.MapOperation.clear(binName, asCTX);
    }
}

