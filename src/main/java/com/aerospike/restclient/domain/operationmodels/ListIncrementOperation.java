package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.Value;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Increment the value of a an item of a list at the specified index, by the value of `incr`",
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
    private final int index;

    @Schema(required = true)
    private final Number incr;

    private ListPolicy listPolicy;

    @JsonCreator
    public ListIncrementOperation(@JsonProperty(value = "binName", required = true) String binName,
                                  @JsonProperty(value = "index", required = true) int index,
                                  @JsonProperty(value = "incr", required = true) Number incr) {
        super(binName);
        this.index = index;
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
        Value asVal = null;

        if (incr instanceof Integer) {
            asVal = Value.get(incr.intValue());
        } else if (incr instanceof Double) {
            asVal = Value.get(incr.doubleValue());
        }

        if (listPolicy == null) {
            if (asVal == null) {
                return com.aerospike.client.cdt.ListOperation.increment(binName, index, asCTX);
            }
            return com.aerospike.client.cdt.ListOperation.increment(binName, index, asVal, asCTX);
        }

        com.aerospike.client.cdt.ListPolicy asListPolicy = listPolicy.toListPolicy();

        if (asVal == null) {
            return com.aerospike.client.cdt.ListOperation.increment(asListPolicy, binName, index, asCTX);
        }
        return com.aerospike.client.cdt.ListOperation.increment(asListPolicy, binName, index, asVal, asCTX);
    }
}
