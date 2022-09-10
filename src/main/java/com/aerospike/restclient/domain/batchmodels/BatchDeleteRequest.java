package com.aerospike.restclient.domain.batchmodels;

import com.aerospike.client.BatchDelete;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.aerospike.restclient.util.RestClientErrors;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "An object that describes a batch delete operation to be used in a batch request.",
        externalDocs = @ExternalDocumentation(
                url =
                        "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.1/com/aerospike/client/BatchDelete.html"
        )
)
public class BatchDeleteRequest extends BatchRecordRequest {
    @Schema(
            description = "The type of batch request. It is always " + AerospikeAPIConstants.BATCH_TYPE_DELETE,
            allowableValues = AerospikeAPIConstants.BATCH_TYPE_DELETE,
            required = true
    )
    public final String batchType = AerospikeAPIConstants.BATCH_TYPE_DELETE;

    @Schema(description = "Policy attributes used for this batch delete operation.")
    public BatchDeletePolicy policy;

    public BatchDeleteRequest() {
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

