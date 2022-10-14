package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.cdt.ListOrder;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Create a list in the specified bin.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html")
)
public class ListCreateOperation extends ListOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.LIST_CREATE,
            required = true,
            allowableValues = OperationTypes.LIST_CREATE
    )
    final public String type = OperationTypes.LIST_CREATE;

    @Schema(required = true)
    private final ListOrder order;

    private boolean pad;

    @JsonCreator
    public ListCreateOperation(@JsonProperty(value = "binName", required = true) String binName,
                               @JsonProperty(value = "order", required = true) ListOrder order) {
        super(binName);
        this.order = order;
    }

    public boolean isPad() {
        return pad;
    }

    public void setPad(boolean pad) {
        this.pad = pad;
    }

    @Override
    public com.aerospike.client.Operation toOperation() {
        com.aerospike.client.cdt.CTX[] asCTX = getASCTX();

        return com.aerospike.client.cdt.ListOperation.create(binName, order, pad, asCTX);
    }
}
