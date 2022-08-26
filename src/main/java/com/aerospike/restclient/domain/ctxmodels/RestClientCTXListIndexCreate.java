package com.aerospike.restclient.domain.ctxmodels;

import com.aerospike.client.cdt.CTX;
import com.aerospike.client.cdt.ListOrder;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public class RestClientCTXListIndexCreate extends RestClientCTX {
    @Schema(description = "TODO", allowableValues = AerospikeAPIConstants.LIST_INDEX_CREATE, required = true)
    @JsonProperty(required = true)
    public final String ctxType = AerospikeAPIConstants.LIST_INDEX_CREATE;
    @Schema(description = "TODO", required = true)
    @JsonProperty(required = true)
    public Integer index;

    @Schema(description = "TODO")
    public ListOrder order = ListOrder.UNORDERED;

    @Schema(description = "TODO")
    public boolean pad;

    RestClientCTXListIndexCreate() {};

    @Override
    public CTX toCTX() {
        return CTX.listIndexCreate(index, order, pad);
    }
}
