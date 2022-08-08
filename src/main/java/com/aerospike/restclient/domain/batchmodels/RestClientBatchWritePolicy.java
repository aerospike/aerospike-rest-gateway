package com.aerospike.restclient.domain.batchmodels;

import com.aerospike.client.policy.BatchWritePolicy;
import com.aerospike.client.policy.CommitLevel;
import com.aerospike.client.policy.GenerationPolicy;
import com.aerospike.client.policy.RecordExistsAction;
import com.aerospike.restclient.util.APIDescriptors;
import com.aerospike.restclient.util.converters.PolicyValueConverter;
import io.swagger.v3.oas.annotations.media.Schema;

public class RestClientBatchWritePolicy {
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

    public RestClientBatchWritePolicy() {
    }

    public BatchWritePolicy toBatchWritePolicy() {
        BatchWritePolicy policy = new BatchWritePolicy();
        policy.recordExistsAction = recordExistsAction;
        policy.commitLevel = commitLevel;
        policy.generationPolicy = generationPolicy;
        policy.generation = generation;
        policy.expiration = expiration;
        policy.durableDelete = durableDelete;
        policy.sendKey = sendKey;

        if (filterExp != null) {
            policy.filterExp = PolicyValueConverter.getFilterExp(filterExp);
        }

        return policy;
    }
}
