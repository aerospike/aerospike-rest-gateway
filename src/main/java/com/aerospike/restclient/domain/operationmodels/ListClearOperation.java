package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.restclient.domain.ctxmodels.CTX;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(
        description = " Empty a list stored in the specified bin.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html")
)
public class ListClearOperation extends ListOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.LIST_CLEAR,
            required = true,
            allowableValues = OperationTypes.LIST_CLEAR
    )
    final public String type = OperationTypes.LIST_CLEAR;

    public ListClearOperation(String binName, List<CTX> ctx) {
        super(binName);
        this.ctx = ctx;
    }

    @Override
    public com.aerospike.client.Operation toOperation() {
        com.aerospike.client.cdt.CTX[] asCTX = getASCTX();

        return com.aerospike.client.cdt.ListOperation.clear(binName, asCTX);
    }
}
