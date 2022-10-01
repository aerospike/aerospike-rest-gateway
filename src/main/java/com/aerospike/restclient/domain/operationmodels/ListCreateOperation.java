package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.cdt.ListOrder;
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
    private ListOrder order;

    private boolean pad;

    public ListCreateOperation(String binName, ListOrder order) {
        super(binName);
        this.order = order;
    }

    public ListOrder getOrder() {
        return order;
    }

    public void setOrder(ListOrder order) {
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
