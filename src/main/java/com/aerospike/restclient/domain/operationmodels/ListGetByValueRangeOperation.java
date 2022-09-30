package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.Value;
import com.aerospike.restclient.domain.ctxmodels.CTX;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(
        description = " Return all items in a list with values between `valueBegin` and `valueEnd`. If `valueBegin` is omitted, all items with a value less than `valueEnd` will be returned. If `valueEnd` is omitted, all items with a value greater than `valueBegin` will be returned. Requires Aerospike Server `3.16.0.1` or later.",
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

    private Object valueBegin;

    private Object valueEnd;

    public ListGetByValueRangeOperation(String binName, ListReturnType listReturnType, List<CTX> ctx, Object valueBegin,
                                        Object valueEnd) {
        super(binName);
        this.listReturnType = listReturnType;
        this.ctx = ctx;
        this.valueBegin = valueBegin;
        this.valueEnd = valueEnd;
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

        return com.aerospike.client.cdt.ListOperation.getByValueRange(binName, Value.get(valueBegin),
                Value.get(valueEnd), listReturnType.toListReturnType(inverted), asCTX);
    }
}
