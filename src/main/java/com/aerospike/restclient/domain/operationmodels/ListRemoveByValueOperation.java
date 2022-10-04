package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.Value;
import com.aerospike.restclient.domain.ctxmodels.CTX;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Collections;
import java.util.Optional;

@Schema(
        description = "Remove and return list entries with a value equal to the specified value. Requires Aerospike Server `3.16.0.`1 or later",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html")
)
public class ListRemoveByValueOperation extends ListOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.LIST_REMOVE_BY_VALUE,
            required = true,
            allowableValues = OperationTypes.LIST_REMOVE_BY_VALUE
    )
    final public String type = OperationTypes.LIST_REMOVE_BY_VALUE;

    @Schema(required = true)
    private Object value;

    @Schema(required = true)
    private ListReturnType listReturnType;

    private boolean inverted;

    public ListRemoveByValueOperation(String binName, Object value, ListReturnType listReturnType) {
        super(binName);
        this.value = value;
        this.listReturnType = listReturnType;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
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
        com.aerospike.client.cdt.CTX[] asCTX = Optional.ofNullable(ctx)
                .orElseGet(Collections::emptyList)
                .stream()
                .map(CTX::toCTX)
                .toArray(com.aerospike.client.cdt.CTX[]::new);

        return com.aerospike.client.cdt.ListOperation.removeByValue(binName, Value.get(value),
                listReturnType.toListReturnType(inverted), asCTX);
    }
}
