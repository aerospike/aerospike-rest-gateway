package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.Value;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(
        description = "Insert multiple items into a list at the specified `index`.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html")
)
public class ListInsertItemsOperation extends ListOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.LIST_INSERT_ITEMS,
            required = true,
            allowableValues = OperationTypes.LIST_INSERT_ITEMS
    )
    final public String type = OperationTypes.LIST_INSERT_ITEMS;

    @Schema(required = true)
    private Integer index;

    @Schema(required = true)
    private List<Object> values;

    private ListPolicy listPolicy;

    public ListInsertItemsOperation(String binName, Integer index, List<Object> values) {
        super(binName);
        this.index = index;
        this.values = values;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public List<Object> getValues() {
        return values;
    }

    public void setValues(List<Object> values) {
        this.values = values;
    }

    public ListPolicy getListPolicy() {
        return listPolicy;
    }

    public void setListPolicy(ListPolicy listPolicy) {
        this.listPolicy = listPolicy;
    }

    @Override
    public com.aerospike.client.Operation toOperation() {
        List<Value> asVals = values.stream().map(Value::get).toList();
        com.aerospike.client.cdt.CTX[] asCTX = getASCTX();

        if (listPolicy == null) {
            return com.aerospike.client.cdt.ListOperation.insertItems(binName, index, asVals, asCTX);
        }

        return com.aerospike.client.cdt.ListOperation.insertItems(listPolicy.toListPolicy(), binName, index, asVals,
                asCTX);
    }
}
