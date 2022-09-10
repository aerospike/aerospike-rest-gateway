package com.aerospike.restclient.domain.ctxmodels;

import com.aerospike.client.cdt.ListOrder;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Lookup list by base list's index offset. If the list at index offset is not found, create it with the given sort order at that index offset."
)
public class ListIndexCreateCTX extends CTX {
    @Schema(
            description = "The type of context this object represents. It is always " + AerospikeAPIConstants.LIST_INDEX_CREATE,
            allowableValues = AerospikeAPIConstants.LIST_INDEX_CREATE,
            required = true
    )
    public final String ctxType = AerospikeAPIConstants.LIST_INDEX_CREATE;

    @Schema(
            description = "If the index is negative, the resolved index starts backwards from end of list. If an index is out of bounds, a parameter error will be returned. Examples:\n" +
                    "* 0: First item.\n" +
                    "* 4: Fifth item.\n" +
                    "* -1: Last item.\n" +
                    "* -3: Third to last item.", required = true
    )
    public Integer index;

    @Schema(description = "List storage order.", defaultValue = "UNORDERED")
    public ListOrder order = ListOrder.UNORDERED;

    @Schema(description = "If pad is true and the index offset is greater than the bounds of the base list, nil entries will be inserted before the newly created list.")
    public boolean pad;

    ListIndexCreateCTX() {
    }

    ;

    @Override
    public com.aerospike.client.cdt.CTX toCTX() {
        return com.aerospike.client.cdt.CTX.listIndexCreate(index, order, pad);
    }
}
