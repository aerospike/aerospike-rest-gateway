package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.Value;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(
        description = "Return all map items with a value contained in the provided list of values.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/MapOperation.html")
)
public class MapGetByValueListOperation extends MapOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.MAP_GET_BY_VALUE_LIST,
            required = true,
            allowableValues = OperationTypes.MAP_GET_BY_VALUE_LIST
    )
    final public String type = OperationTypes.MAP_GET_BY_VALUE_LIST;

    @Schema(required = true)
    private final List<Object> values;

    @Schema(required = true)
    private final MapReturnType mapReturnType;

    private boolean inverted;

    @JsonCreator
    public MapGetByValueListOperation(@JsonProperty("binName") String binName,
                                      @JsonProperty("values") List<Object> values,
                                      @JsonProperty("mapReturnType") MapReturnType mapReturnType) {
        super(binName);
        this.values = values;
        this.mapReturnType = mapReturnType;
        inverted = false;
    }

    public boolean isInverted() {
        return inverted;
    }

    public void setInverted(boolean inverted) {
        this.inverted = inverted;
    }

    @Override
    public com.aerospike.client.Operation toOperation() {
        List<Value> asVals = values.stream().map(Value::get).toList();
        com.aerospike.client.cdt.CTX[] asCTX = getASCTX();

        return com.aerospike.client.cdt.MapOperation.getByValueList(binName, asVals,
                mapReturnType.toMapReturnType(inverted), asCTX);
    }
}
