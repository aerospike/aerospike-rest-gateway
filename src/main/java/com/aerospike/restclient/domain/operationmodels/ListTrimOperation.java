package com.aerospike.restclient.domain.operationmodels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Trim the list to the specified range. Items with indexes in the range `[index, index + count)` will be retained.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html")
)
public class ListTrimOperation extends ListOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.LIST_TRIM,
            required = true,
            allowableValues = OperationTypes.LIST_TRIM
    )
    final public String type = OperationTypes.LIST_TRIM;

    @Schema(required = true)
    private final int index;

    @Schema(required = true)
    private final int count;

    @JsonCreator
    public ListTrimOperation(@JsonProperty(value = "binName", required = true) String binName,
                             @JsonProperty(value = "index", required = true) int index,
                             @JsonProperty(value = "count", required = true) int count) {
        super(binName);
        this.index = index;
        this.count = count;
    }

    @Override
    public com.aerospike.client.Operation toOperation() {
        com.aerospike.client.cdt.CTX[] asCTX = getASCTX();

        return com.aerospike.client.cdt.ListOperation.trim(binName, index, count, asCTX);
    }
}
