package com.aerospike.restclient.domain.batchmodels;

import com.aerospike.client.policy.CommitLevel;
import com.aerospike.restclient.util.APIDescriptors;
import com.aerospike.restclient.util.converters.PolicyValueConverter;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "BatchUDFPolicy",
        description = "An object that describes a policy for a udf operation used in a batch request.",
        externalDocs = @ExternalDocumentation(
                url =
                        "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.1/com/aerospike/client/policy/BatchUDFPolicy.html"
        )
)
public class BatchUDFPolicy {
    @Schema(description = APIDescriptors.BATCH_POLICY_FILTER_EXP_NOTES)
    public String filterExp;
    @Schema(description = APIDescriptors.BATCH_POLICY_COMMIT_LEVEL_NOTES)
    public CommitLevel commitLevel;
    @Schema(description = APIDescriptors.BATCH_POLICY_EXPIRATION_NOTES)
    public int expiration;
    @Schema(description = APIDescriptors.BATCH_POLICY_DURABLE_DELETE_NOTES)
    public boolean durableDelete;
    @Schema(description = APIDescriptors.BATCH_POLICY_SEND_KEY_NOTES)
    public boolean sendKey;

    public BatchUDFPolicy() {
    }

    public com.aerospike.client.policy.BatchUDFPolicy toBatchUDFPolicy() {
        com.aerospike.client.policy.BatchUDFPolicy policy = new com.aerospike.client.policy.BatchUDFPolicy();
        policy.filterExp = PolicyValueConverter.getFilterExp(filterExp);
        policy.commitLevel = commitLevel;
        policy.expiration = expiration;
        policy.durableDelete = durableDelete;
        policy.sendKey = sendKey;

        return policy;
    }
}
