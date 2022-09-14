package com.aerospike.restclient.domain.batchmodels;

import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.aerospike.restclient.util.RestClientErrors;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Optional;

@Schema(
        description = "An object that describes a batch delete operation to be used in a batch request.",
        externalDocs = @ExternalDocumentation(
                url =
                        "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/BatchDelete.html"
        )
)
public class BatchDelete extends BatchRecord {
    @Schema(
            description = "The type of batch request. It is always " + AerospikeAPIConstants.BATCH_TYPE_DELETE,
            allowableValues = AerospikeAPIConstants.BATCH_TYPE_DELETE,
            required = true
    )
    public final String type = AerospikeAPIConstants.BATCH_TYPE_DELETE;

    @Schema(description = "Policy attributes used for this batch delete operation.")
    public BatchDeletePolicy policy;

    public BatchDelete() {
    }

    @Override
    public com.aerospike.client.BatchDelete toBatchRecord() {
        if (key == null) {
            throw new RestClientErrors.InvalidKeyError("Key for a batch delete may not be null");
        }

        com.aerospike.client.policy.BatchDeletePolicy batchDeletePolicy = Optional.ofNullable(policy).map(
                BatchDeletePolicy::toBatchDeletePolicy).orElse(null);

        return new com.aerospike.client.BatchDelete(batchDeletePolicy, key.toKey());
    }
}

