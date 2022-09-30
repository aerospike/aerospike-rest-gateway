package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.restclient.domain.ctxmodels.CTX;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(
        description = " Remove and return a list value at the specified `index`.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html")
)
public class ListPopOperation extends ListOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.LIST_POP,
            required = true,
            allowableValues = OperationTypes.LIST_POP
    )
    final public String type = OperationTypes.LIST_POP;

    @Schema(required = true)
    private Integer index;

    public ListPopOperation(String binName, Integer index, List<CTX> ctx) {
        super(binName);
        this.index = index;
        this.ctx = ctx;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    @Override
    public com.aerospike.client.Operation toOperation() {
        com.aerospike.client.cdt.CTX[] asCTX = getASCTX();

        return com.aerospike.client.cdt.ListOperation.pop(binName, index, asCTX);
    }
}
