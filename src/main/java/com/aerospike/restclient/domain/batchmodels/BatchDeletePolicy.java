package com.aerospike.restclient.domain.batchmodels;

import com.aerospike.client.policy.CommitLevel;
import com.aerospike.client.policy.GenerationPolicy;
import com.aerospike.restclient.util.APIDescriptors;
import com.aerospike.restclient.util.converters.PolicyValueConverter;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "BatchDeletePolicy",
        description = "An object that describes a policy for a delete operation used in a batch request.",
        externalDocs = @ExternalDocumentation(
                url =
                        "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/policy/BatchDeletePolicy.html"
        )
)
public class BatchDeletePolicy {
    @Schema(description = APIDescriptors.BATCH_POLICY_FILTER_EXP_NOTES)
    public String filterExp;
    @Schema(description = APIDescriptors.BATCH_POLICY_COMMIT_LEVEL_NOTES)
    public CommitLevel commitLevel;
    @Schema(description = APIDescriptors.BATCH_POLICY_GENERATION_POLICY_NOTES)
    public GenerationPolicy generationPolicy;
    @Schema(description = APIDescriptors.BATCH_POLICY_GENERATION_NOTES)
    public int generation;
    @Schema(description = APIDescriptors.BATCH_POLICY_DURABLE_DELETE_NOTES)
    public boolean durableDelete;
    @Schema(description = APIDescriptors.BATCH_POLICY_SEND_KEY_NOTES)
    public boolean sendKey;

    public BatchDeletePolicy() {
    }

    public com.aerospike.client.policy.BatchDeletePolicy toBatchDeletePolicy() {
        com.aerospike.client.policy.BatchDeletePolicy policy = new com.aerospike.client.policy.BatchDeletePolicy();
        policy.filterExp = PolicyValueConverter.getFilterExp(filterExp);
        policy.commitLevel = commitLevel;
        policy.generationPolicy = generationPolicy;
        policy.generation = generation;
        policy.durableDelete = durableDelete;
        policy.sendKey = sendKey;

        return policy;
    }
}
