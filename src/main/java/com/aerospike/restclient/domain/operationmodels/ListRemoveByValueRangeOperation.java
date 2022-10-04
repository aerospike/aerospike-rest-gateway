package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.Value;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Remove all items from the list with values between `valueBegin` and `valueEnd`. If `valueBegin` is omitted all items with a value less than `valueEnd` will be removed. If `valueEnd` is omitted all items with a value greater than `valueBegin` will be removed. Requires Aerospike Server `3.16.0.1` or later.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html")
)
public class ListRemoveByValueRangeOperation extends ListOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.LIST_REMOVE_BY_VALUE_RANGE,
            required = true,
            allowableValues = OperationTypes.LIST_REMOVE_BY_VALUE_RANGE
    )
    final public String type = OperationTypes.LIST_REMOVE_BY_VALUE_RANGE;

    @Schema(required = true)
    private ListReturnType listReturnType;

    private boolean inverted;

    private Object valueBegin;

    private Object valueEnd;

    public ListRemoveByValueRangeOperation(String binName, ListReturnType listReturnType) {
        super(binName);
        this.listReturnType = listReturnType;
    }

    public ListReturnType getListReturnType() {
        return listReturnType;
    }

    public void setListReturnType(ListReturnType listReturnType) {
        this.listReturnType = listReturnType;
    }

    public Object getValueBegin() {
        return valueBegin;
    }

    public void setValueBegin(Object valueBegin) {
        this.valueBegin = valueBegin;
    }

    public Object getValueEnd() {
        return valueEnd;
    }

    public void setValueEnd(Object valueEnd) {
        this.valueEnd = valueEnd;
    }

    public boolean isInverted() {
        return inverted;
    }

    public void setInverted(boolean inverted) {
        this.inverted = inverted;
    }

    @Override
    public com.aerospike.client.Operation toOperation() {
        com.aerospike.client.cdt.CTX[] asCTX = getASCTX();
        Value begin = null;
        Value end = null;

        if (valueBegin != null) {
            begin = Value.get(valueBegin);
        }

        if (valueEnd != null) {
            end = Value.get(valueEnd);
        }

        return com.aerospike.client.cdt.ListOperation.removeByValueRange(binName, begin, end,
                listReturnType.toListReturnType(inverted), asCTX);
    }
}
