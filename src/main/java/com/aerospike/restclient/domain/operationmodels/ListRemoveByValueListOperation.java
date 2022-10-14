package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.Value;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(
        description = "Remove all items from the list with values contained in the specified list of values. Requires Aerospike Server `3.16.0.1` or later",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html")
)
public class ListRemoveByValueListOperation extends ListOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.LIST_REMOVE_BY_VALUE_LIST,
            required = true,
            allowableValues = OperationTypes.LIST_REMOVE_BY_VALUE_LIST
    )
    final public String type = OperationTypes.LIST_REMOVE_BY_VALUE_LIST;

    @Schema(required = true)
    private final List<Object> values;

    @Schema(required = true)
    private final ListReturnType listReturnType;

    private boolean inverted;

    @JsonCreator
    public ListRemoveByValueListOperation(@JsonProperty("binName") String binName,
                                          @JsonProperty("values") List<Object> values,
                                          @JsonProperty("listReturnType") ListReturnType listReturnType) {
        super(binName);
        this.values = values;
        this.listReturnType = listReturnType;
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

        return com.aerospike.client.cdt.ListOperation.removeByValueList(binName, asVals,
                listReturnType.toListReturnType(inverted), asCTX);
    }
}
