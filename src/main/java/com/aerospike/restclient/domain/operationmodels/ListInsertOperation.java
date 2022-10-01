package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.Value;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = " Insert a value into a list at the specified index.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html")
)
public class ListInsertOperation extends ListOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.LIST_INSERT,
            required = true,
            allowableValues = OperationTypes.LIST_INSERT
    )
    final public String type = OperationTypes.LIST_INSERT;

    @Schema(required = true)
    private Integer index;

    @Schema(required = true)
    private Object value;

    private ListPolicy listPolicy;

    public ListInsertOperation(String binName, Integer index, Object value) {
        super(binName);
        this.index = index;
        this.value = value;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
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
            return com.aerospike.client.cdt.ListOperation.insert(binName, index, Value.get(value), asCTX);
        }

        return com.aerospike.client.cdt.ListOperation.insert(listPolicy.toListPolicy(), binName, index,
                Value.get(value), asCTX);
    }
}
