package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.Value;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Return all map values with the specified value.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/MapOperation.html")
)
public class MapGetByValueOperation extends MapOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.MAP_GET_BY_VALUE,
            required = true,
            allowableValues = OperationTypes.MAP_GET_BY_VALUE
    )
    final public String type = OperationTypes.MAP_GET_BY_VALUE;

    @Schema(required = true)
    private final Object value;

    @Schema(required = true)
    private final MapReturnType mapReturnType;

    private boolean inverted;

    @JsonCreator
    public MapGetByValueOperation(@JsonProperty(value = "binName", required = true) String binName,
                                  @JsonProperty(value = "value", required = true) Object value,
                                  @JsonProperty(value = "mapReturnType", required = true) MapReturnType mapReturnType) {
        super(binName);
        this.value = value;
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
        com.aerospike.client.cdt.CTX[] asCTX = getASCTX();

        return com.aerospike.client.cdt.MapOperation.getByValue(binName, Value.get(value),
                mapReturnType.toMapReturnType(inverted), asCTX);
    }
}
