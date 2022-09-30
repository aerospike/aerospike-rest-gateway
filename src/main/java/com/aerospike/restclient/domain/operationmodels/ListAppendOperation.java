package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.Value;
import com.aerospike.client.cdt.ListPolicy;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Append a `value` to a list stored in the specified `binName`.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html")
)
public class ListAppendOperation extends ListOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.LIST_APPEND,
            required = true,
            allowableValues = OperationTypes.LIST_APPEND
    )
    final public String type = OperationTypes.LIST_APPEND;

    @Schema(required = true)
    private Object value;

    // TODO, this probably needs an intermediate type
    private ListPolicy listPolicy;

    public ListAppendOperation(String binName, Object value) {
        super(binName);
        this.value = value;
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
            return com.aerospike.client.cdt.ListOperation.append(binName, Value.get(value), asCTX);
        }

        return com.aerospike.client.cdt.ListOperation.append(listPolicy, binName, Value.get(value), asCTX);
    }
}
