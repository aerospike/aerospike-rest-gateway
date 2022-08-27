package com.aerospike.restclient.domain.ctxmodels;

import com.aerospike.client.cdt.CTX;
import com.aerospike.client.cdt.ListOrder;
import com.aerospike.restclient.util.APIDescriptors;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.aerospike.restclient.util.RestClientErrors;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;

public class RestClientCTXListIndex extends RestClientCTX {
    @Schema(description = "TODO", allowableValues = AerospikeAPIConstants.LIST_INDEX, required = true)
    @JsonProperty(required = true)
    public final String ctxType = AerospikeAPIConstants.LIST_INDEX;
    @Schema(description = "TODO", required = true)
    @JsonProperty(required = true, value = "index")
    public Integer index;

    public RestClientCTXListIndex() {};

    public RestClientCTXListIndex(int index) {
        this.index = index;
    };

    @Override
    public CTX toCTX()
    {
        if (index == null) {
            // TODO: Use javax.validation for our model validation. Not sure on performance implications.
            throw new RestClientErrors.InvalidCTXError("Invalid context. index is required");
        }
        return CTX.listIndex(index);
    }
}



