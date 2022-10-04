package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.Value;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Return all items in a list with values between `valueBegin` and `valueEnd`. If `valueBegin` is omitted, all items with a value less than `valueEnd` will be returned. If `valueEnd` is omitted, all items with a value greater than `valueBegin` will be returned. Requires Aerospike Server `3.16.0.1` or later.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html")
)
public class ListGetByValueRangeOperation extends ListOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.LIST_GET_BY_VALUE_RANGE,
            required = true,
            allowableValues = OperationTypes.LIST_GET_BY_VALUE_RANGE
    )
    final public String type = OperationTypes.LIST_GET_BY_VALUE_RANGE;

    @Schema(required = true)
    private ListReturnType listReturnType;

    private boolean inverted;

    private Value valueBegin;

    private Value valueEnd;

    public ListGetByValueRangeOperation(String binName, ListReturnType listReturnType) {
        super(binName);
        this.listReturnType = listReturnType;
        inverted = false;
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
        this.valueBegin = Value.get(valueBegin);
    }

    public Value getValueEnd() {
        return valueEnd;
    }

    public void setValueEnd(Object valueEnd) {
        this.valueEnd = Value.get(valueEnd);
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

        return com.aerospike.client.cdt.ListOperation.getByValueRange(binName, valueBegin, valueEnd,
                listReturnType.toListReturnType(inverted), asCTX);
    }
}
