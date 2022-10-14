package com.aerospike.restclient.domain.operationmodels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Remove and return `count` items beginning at the specified `index` from the list. If `count` is omitted, all items beginning from `index` will be removed and returned.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html")
)
public class ListPopRangeOperation extends ListOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.LIST_POP_RANGE,
            required = true,
            allowableValues = OperationTypes.LIST_POP_RANGE
    )
    final public String type = OperationTypes.LIST_POP_RANGE;

    @Schema(required = true)
    private final int index;

    private Integer count;

    @JsonCreator
    public ListPopRangeOperation(@JsonProperty(value = "binName", required = true) String binName,
                                 @JsonProperty(value = "index", required = true) int index) {
        super(binName);
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
            return com.aerospike.client.cdt.ListOperation.popRange(binName, index, asCTX);
        }

        return com.aerospike.client.cdt.ListOperation.popRange(binName, index, count, asCTX);
    }
}
