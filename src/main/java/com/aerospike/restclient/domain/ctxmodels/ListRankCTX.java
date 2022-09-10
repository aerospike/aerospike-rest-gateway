package com.aerospike.restclient.domain.ctxmodels;

import com.aerospike.restclient.util.AerospikeAPIConstants;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Lookup list by rank",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/CTX.html")
)
public class ListRankCTX extends CTX {
    @Schema(
            description = "The type of context this object represents. It is always " + AerospikeAPIConstants.LIST_RANK,
            allowableValues = AerospikeAPIConstants.LIST_RANK,
            required = true
    )
    public final String ctxType = AerospikeAPIConstants.LIST_RANK;

    @Schema(
            description = "* 0 = smallest value\n" +
                    "* N = Nth smallest value\n" +
                    "* -1 = largest value", required = true
    )
    public Integer rank;

    ListRankCTX() {
    }

    public ListRankCTX(int rank) {
        this.rank = rank;
    }


    @Override
    public com.aerospike.client.cdt.CTX toCTX() {
        return com.aerospike.client.cdt.CTX.listRank(rank);
    }
}


