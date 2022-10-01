package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.Value;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = " Return all items in a list with a value matching a specified value.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html")
)
public class ListGetByValueOperation extends ListOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.LIST_GET_BY_VALUE,
            required = true,
            allowableValues = OperationTypes.LIST_GET_BY_VALUE
    )
    final public String type = OperationTypes.LIST_GET_BY_VALUE;

    @Schema(required = true)
    private Value value;

    @Schema(required = true)
    private ListReturnType listReturnType;

    private boolean inverted;

    @JsonCreator
    public ListGetByValueOperation(@JsonProperty("binName") String binName) {
        super(binName);
    }

    public ListGetByValueOperation(String binName, Value value, ListReturnType listReturnType) {
        super(binName);
        this.value = value;
        this.listReturnType = listReturnType;
        inverted = false;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = Value.get(value);
    }

    public ListReturnType getListReturnType() {
        return listReturnType;
    }

    public void setListReturnType(ListReturnType listReturnType) {
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
        com.aerospike.client.cdt.CTX[] asCTX = getASCTX();

        return com.aerospike.client.cdt.ListOperation.getByValue(binName, value,
                listReturnType.toListReturnType(inverted), asCTX);
    }
}
