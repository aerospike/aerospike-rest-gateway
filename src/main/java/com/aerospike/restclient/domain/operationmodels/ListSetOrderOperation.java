package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.cdt.ListOrder;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Set an ordering for the list.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html")
)
public class ListSetOrderOperation extends ListOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.LIST_SET_ORDER,
            required = true,
            allowableValues = OperationTypes.LIST_SET_ORDER
    )
    final public String type = OperationTypes.LIST_SET_ORDER;

    @Schema(required = true)
    private final ListOrder listOrder;

    @JsonCreator
    public ListSetOrderOperation(@JsonProperty(value = "binName", required = true) String binName,
                                 @JsonProperty(value = "listOrder", required = true) ListOrder listOrder) {
        super(binName);
        this.listOrder = listOrder;
    }

    @Override
    public com.aerospike.client.Operation toOperation() {
        com.aerospike.client.cdt.CTX[] asCTX = getASCTX();

        return com.aerospike.client.cdt.ListOperation.setOrder(binName, listOrder, asCTX);
    }
}
