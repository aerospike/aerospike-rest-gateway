package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.Value;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Set the value at the specified index to the specified value.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html")
)
public class ListSetOperation extends ListOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.LIST_SET,
            required = true,
            allowableValues = OperationTypes.LIST_SET
    )
    final public String type = OperationTypes.LIST_SET;

    @Schema(required = true)
    private int index;

    @Schema(required = true)
    private Object value;

    private ListPolicy listPolicy;

    public ListSetOperation(String binName, int index, Object value) {
        super(binName);
        this.index = index;
        this.value = value;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
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

        if (listPolicy == null) {
            return com.aerospike.client.cdt.ListOperation.set(binName, index, Value.get(value), asCTX);
        }

        return com.aerospike.client.cdt.ListOperation.set(listPolicy.toListPolicy(), binName, index, Value.get(value),
                asCTX);
    }
}
