package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.Value;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = " TODO",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/MapOperation.html")
)
public class ListRemoveByValueRelativeRankRangeOperation extends ListOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.LIST_REMOVE_BY_VALUE_RELATIVE_RANK_RANGE,
            required = true,
            allowableValues = OperationTypes.LIST_REMOVE_BY_VALUE_RELATIVE_RANK_RANGE
    )
    final public String type = OperationTypes.LIST_REMOVE_BY_VALUE_RELATIVE_RANK_RANGE;

    @Schema(required = true)
    private Integer rank;

    @Schema(required = true)
    private Object value;

    @Schema(required = true)
    private ListReturnType listReturnType;

    private boolean inverted;

    private Integer count;

    public ListRemoveByValueRelativeRankRangeOperation(String binName, Integer index, Object value,
                                                       ListReturnType listReturnType) {
        super(binName);
        this.rank = index;
        this.value = value;
        this.listReturnType = listReturnType;
        inverted = false;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public ListReturnType getListReturnType() {
        return listReturnType;
    }

    public void setListReturnType(ListReturnType listReturnType) {
        this.listReturnType = listReturnType;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
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
        int intMapReturnType = listReturnType.toListReturnType(inverted);

        if (count == null) {
            return com.aerospike.client.cdt.ListOperation.removeByValueRelativeRankRange(binName, Value.get(value),
                    rank, intMapReturnType, asCTX);
        }

        return com.aerospike.client.cdt.ListOperation.removeByValueRelativeRankRange(binName, Value.get(value), rank,
                count, intMapReturnType, asCTX);
    }
}
