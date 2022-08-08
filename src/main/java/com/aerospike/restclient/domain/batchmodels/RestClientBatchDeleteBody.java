package com.aerospike.restclient.domain.batchmodels;

import com.aerospike.client.BatchDelete;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.aerospike.restclient.util.RestClientErrors;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public class RestClientBatchDeleteBody extends RestClientBatchRecordBody {

    @Schema(description = "Policy attributes used for this batch delete operation.")
    public RestClientBatchDeletePolicy policy;

    @Schema(description = "List of bins to limit the record response to.", allowableValues = AerospikeAPIConstants.BATCH_TYPE_DELETE, required = true)
    @JsonProperty(required = true)
    public final String batchType = AerospikeAPIConstants.BATCH_TYPE_DELETE;

    public RestClientBatchDeleteBody() {
    }

    @Override
    public BatchDelete toBatchRecord() {
        if (key == null) {
            throw new RestClientErrors.InvalidKeyError("Key for a batch delete may not be null");
        }

        if (policy == null) {
            return new BatchDelete(key.toKey());
        }

        return new BatchDelete(policy.toBatchDeletePolicy(), key.toKey());
    }
}

