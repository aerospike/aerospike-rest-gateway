package com.aerospike.restclient.domain.batchmodels;

import com.aerospike.client.policy.CommitLevel;
import com.aerospike.client.policy.GenerationPolicy;
import com.aerospike.client.policy.RecordExistsAction;
import com.aerospike.restclient.util.APIDescriptors;
import com.aerospike.restclient.util.converters.PolicyValueConverter;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "BatchWritePolicy",
        description = "An object that describes a policy for a write operation used in a batch request.",
        externalDocs = @ExternalDocumentation(
                url =
                        "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/policy/BatchWritePolicy.html"
        )
)
public class BatchWritePolicy {
    @Schema(description = APIDescriptors.BATCH_POLICY_FILTER_EXP_NOTES)
    public String filterExp;
    @Schema(description = APIDescriptors.BATCH_POLICY_RECORD_EXIST_ACTION_NOTES)
    public RecordExistsAction recordExistsAction;
    @Schema(description = APIDescriptors.BATCH_POLICY_COMMIT_LEVEL_NOTES)
    public CommitLevel commitLevel;
    @Schema(description = APIDescriptors.BATCH_POLICY_GENERATION_POLICY_NOTES)
    public GenerationPolicy generationPolicy;
    @Schema(description = APIDescriptors.BATCH_POLICY_GENERATION_NOTES)
    public int generation;
    @Schema(description = APIDescriptors.BATCH_POLICY_EXPIRATION_NOTES)
    public int expiration;
    @Schema(description = APIDescriptors.BATCH_POLICY_DURABLE_DELETE_NOTES)
    public boolean durableDelete;
    @Schema(description = APIDescriptors.BATCH_POLICY_SEND_KEY_NOTES)
    public boolean sendKey;

    public BatchWritePolicy() {
    }

    public com.aerospike.client.policy.BatchWritePolicy toBatchWritePolicy() {
        com.aerospike.client.policy.BatchWritePolicy policy = new com.aerospike.client.policy.BatchWritePolicy();
        policy.recordExistsAction = recordExistsAction;
        policy.commitLevel = commitLevel;
        policy.generationPolicy = generationPolicy;
        policy.generation = generation;
        policy.expiration = expiration;
        policy.durableDelete = durableDelete;
        policy.sendKey = sendKey;
        policy.filterExp = PolicyValueConverter.getFilterExp(filterExp);

        return policy;
    }
}
