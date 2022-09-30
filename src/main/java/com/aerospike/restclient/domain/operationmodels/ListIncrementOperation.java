package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.Value;
import com.aerospike.client.cdt.ListPolicy;
import com.aerospike.restclient.domain.ctxmodels.CTX;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(
        description = " Increment the value of a an item of a list at the specified index, by the value of `incr`",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html")
)
public class ListIncrementOperation extends ListOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.LIST_INCREMENT,
            required = true,
            allowableValues = OperationTypes.LIST_INCREMENT
    )
    final public String type = OperationTypes.LIST_INCREMENT;

    @Schema(required = true)
    private Integer index;

    private Number incr;

    private ListPolicy listPolicy;

    public ListIncrementOperation(String binName, Integer index, List<CTX> ctx, Number incr, ListPolicy listPolicy) {
        super(binName);
        this.index = index;
        this.ctx = ctx;
        this.incr = incr;
        this.listPolicy = listPolicy;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Number getIncr() {
        return incr;
    }

    public void setIncr(Number incr) {
        this.incr = incr;
    }

    public ListPolicy getListPolicy() {
        return listPolicy;
    }

    public void setListPolicy(ListPolicy listPolicy) {
        this.listPolicy = listPolicy;
    }

    @Override
    public com.aerospike.client.Operation toOperation() {
        com.aerospike.client.cdt.CTX[] asCTX = getASCTX();
        Value asVal;

        if (incr instanceof Integer) {
            asVal = Value.get(incr.intValue());
        } else {
            asVal = Value.get(incr.doubleValue());
        }

        if (listPolicy == null) {
            return com.aerospike.client.cdt.ListOperation.increment(binName, index, asVal, asCTX);
        }

        return com.aerospike.client.cdt.ListOperation.increment(listPolicy, binName, index, asVal, asCTX);
    }
}
