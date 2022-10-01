package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.cdt.ListSortFlags;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

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

    private List<ListSortFlag> sortFlags;

    @JsonCreator
    public ListSortOperation(@JsonProperty("binName") String binName) {
        super(binName);
    }

    public List<ListSortFlag> getSortFlags() {
        return sortFlags;
    }

    public void setSortFlags(List<ListSortFlag> sortFlags) {
        this.sortFlags = sortFlags;
    }

    @Override
    public com.aerospike.client.Operation toOperation() {
        com.aerospike.client.cdt.CTX[] asCTX = getASCTX();
        int asSortFlags = ListSortFlags.DEFAULT;

        if (sortFlags != null) {
            asSortFlags = sortFlags.stream().reduce(0, (acc, flag) -> acc | flag.flag, Integer::sum);
        }

        return com.aerospike.client.cdt.ListOperation.sort(binName, asSortFlags, asCTX);
    }
}
