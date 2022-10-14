package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.Value;
import com.aerospike.restclient.domain.ctxmodels.CTX;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    private final Object value;

    @Schema(required = true)
    private final ListReturnType listReturnType;

    private boolean inverted;

    @JsonCreator
    public ListRemoveByValueOperation(@JsonProperty(value = "binName", required = true) String binName,
                                      @JsonProperty(value = "value", required = true) Object value, @JsonProperty(
            value = "listReturnType",
            required = true
    ) ListReturnType listReturnType) {
        super(binName);
        this.value = value;
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
