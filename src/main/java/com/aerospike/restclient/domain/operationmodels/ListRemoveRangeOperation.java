package com.aerospike.restclient.domain.operationmodels;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Remove `count` items beginning at the specified `index` from the list. If `count` is omitted, all items beginning from `index` will be removed and returned.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html")
)
public class ListRemoveRangeOperation extends ListOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.LIST_REMOVE_RANGE,
            required = true,
            allowableValues = OperationTypes.LIST_REMOVE_RANGE
    )
    final public String type = OperationTypes.LIST_REMOVE_RANGE;

    @Schema(required = true)
    private int index;

    private Integer count;

    public ListRemoveRangeOperation(String binName, Integer index) {
        super(binName);
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public com.aerospike.client.Operation toOperation() {
        com.aerospike.client.cdt.CTX[] asCTX = getASCTX();

        if (count == null) {
            return com.aerospike.client.cdt.ListOperation.removeRange(binName, index, asCTX);
        }

        return com.aerospike.client.cdt.ListOperation.removeRange(binName, index, count, asCTX);
    }
}
