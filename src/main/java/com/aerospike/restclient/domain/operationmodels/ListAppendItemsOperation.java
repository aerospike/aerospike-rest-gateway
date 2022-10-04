package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.Value;
import com.aerospike.restclient.domain.ctxmodels.CTX;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Schema(
        description = "Append multiple items to a list stored in the specified bin.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html")
)
public class ListAppendItemsOperation extends ListOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.LIST_APPEND_ITEMS,
            required = true,
            allowableValues = OperationTypes.LIST_APPEND_ITEMS
    )
    final public String type = OperationTypes.LIST_APPEND_ITEMS;

    @Schema(required = true)
    private List<Object> values;

    // TODO, this probably needs an intermediate type
    private ListPolicy listPolicy;

    public ListAppendItemsOperation(String binName, List<Object> values) {
        super(binName);
        this.values = values;
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
        com.aerospike.client.cdt.CTX[] asCTX = Optional.ofNullable(ctx)
                .orElseGet(Collections::emptyList)
                .stream()
                .map(CTX::toCTX)
                .toArray(com.aerospike.client.cdt.CTX[]::new);

        if (listPolicy == null) {
            return com.aerospike.client.cdt.ListOperation.appendItems(binName, asVals, asCTX);
        }

        return com.aerospike.client.cdt.ListOperation.appendItems(listPolicy.toListPolicy(), binName, asVals, asCTX);
    }
}
