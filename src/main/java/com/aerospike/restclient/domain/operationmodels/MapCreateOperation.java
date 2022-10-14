package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.cdt.MapOrder;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Empty the specified map.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/MapOperation.html")
)
public class MapCreateOperation extends MapOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.MAP_CREATE,
            required = true,
            allowableValues = OperationTypes.MAP_CREATE
    )
    final public String type = OperationTypes.MAP_CREATE;

    @Schema(required = true)
    private final MapOrder mapOrder;

    @JsonCreator
    public MapCreateOperation(@JsonProperty(value = "binName", required = true) String binName,
                              @JsonProperty(value = "mapOrder", required = true) MapOrder mapOrder) {
        super(binName);
        this.mapOrder = mapOrder;
    }

    @Override
    public com.aerospike.client.Operation toOperation() {
        com.aerospike.client.cdt.CTX[] asCTX = getASCTX();

        return com.aerospike.client.cdt.MapOperation.create(binName, mapOrder, asCTX);
    }
}
