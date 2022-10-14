package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.Value;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(
        description = "Return all items in a list with values that are contained in the specified list of values. Requires Aerospike Server `3.16.0.1` or later.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html")
)
public class ListGetByValueListOperation extends ListOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.LIST_GET_BY_VALUE_LIST,
            required = true,
            allowableValues = OperationTypes.LIST_GET_BY_VALUE_LIST
    )
    final public String type = OperationTypes.LIST_GET_BY_VALUE_LIST;

    @Schema(required = true)
    private final ListReturnType listReturnType;

    @Schema(required = true)
    private final List<Object> values;

    private boolean inverted;

    @JsonCreator
    public ListGetByValueListOperation(@JsonProperty("binName") String binName,
                                       @JsonProperty("listReturnType") ListReturnType listReturnType,
                                       @JsonProperty("values") List<Object> values) {
        super(binName);
        this.listReturnType = listReturnType;
        this.values = values;
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

        return com.aerospike.client.cdt.ListOperation.getByValueList(binName, asVals,
                listReturnType.toListReturnType(inverted), asCTX);
    }
}
