package com.aerospike.restclient.domain.operationmodels;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Perform a sort operation on the list. Requires Aerospike Server `3.16.0.1` or later.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html")
)
public class ListSortOperation extends ListOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.LIST_SORT,
            required = true,
            allowableValues = OperationTypes.LIST_SORT
    )
    final public String type = OperationTypes.LIST_SORT;

    @Schema(required = true)
    private Integer listSortFlags;

    public ListSortOperation(String binName, Integer listSortFlags) {
        super(binName);
        this.listSortFlags = listSortFlags;
    }

    public Integer getListSortFlags() {
        return listSortFlags;
    }

    public void setListSortFlags(Integer listSortFlags) {
        this.listSortFlags = listSortFlags;
    }

    @Override
    public com.aerospike.client.Operation toOperation() {
        com.aerospike.client.cdt.CTX[] asCTX = getASCTX();

        return com.aerospike.client.cdt.ListOperation.sort(binName, listSortFlags, asCTX);
    }
}
