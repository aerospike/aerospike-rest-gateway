package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.restclient.domain.ctxmodels.CTX;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(
        description = " Get `count` items from the list beginning with the specified index. If `count` is omitted, all items from `index` to the end of the list will be returned.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html")
)
public class ListGetRangeOperation extends ListOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.LIST_GET_RANGE,
            required = true,
            allowableValues = OperationTypes.LIST_GET_RANGE
    )
    final public String type = OperationTypes.LIST_GET_RANGE;

    @Schema(required = true)
    private Integer index;

    private Integer count;

    public ListGetRangeOperation(String binName, Integer index, List<CTX> ctx, Integer count) {
        super(binName);
        this.index = index;
        this.ctx = ctx;
        this.count = count;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
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

        return com.aerospike.client.cdt.ListOperation.getRange(binName, index, count, asCTX);
    }
}
