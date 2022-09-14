package com.aerospike.restclient.domain.ctxmodels;

import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.aerospike.restclient.util.RestClientErrors;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Lookup list by index offset.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/CTX.html")
)
public class ListIndexCTX extends CTX {
    @Schema(
            description = "The type of context this object represents. It is always " + AerospikeAPIConstants.CTX.LIST_INDEX,
            allowableValues = AerospikeAPIConstants.CTX.LIST_INDEX,
            required = true
    )
    public final String type = AerospikeAPIConstants.CTX.LIST_INDEX;

    @Schema(
            description = "If the index is negative, the resolved index starts backwards from end of list. If an index is out of bounds, a parameter error will be returned. Examples:\n" +
                    "* 0: First item.\n" +
                    "* 4: Fifth item.\n" +
                    "* -1: Last item.\n" +
                    "* -3: Third to last item.", required = true
    )
    public Integer index;

    public ListIndexCTX() {
    }

    public ListIndexCTX(int index) {
        this.index = index;
    }

    @Override
    public com.aerospike.client.cdt.CTX toCTX() {
        if (index == null) {
            // TODO: Use javax.validation for our model validation. Not sure on performance implications.
            throw new RestClientErrors.InvalidCTXError("Invalid context. index is required");
        }
        return com.aerospike.client.cdt.CTX.listIndex(index);
    }
}



