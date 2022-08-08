package com.aerospike.restclient.domain.batchmodels;

import com.aerospike.client.policy.BatchDeletePolicy;
import com.aerospike.client.policy.CommitLevel;
import com.aerospike.client.policy.GenerationPolicy;
import com.aerospike.restclient.util.APIDescriptors;
import com.aerospike.restclient.util.converters.PolicyValueConverter;
import io.swagger.v3.oas.annotations.media.Schema;

public class RestClientBatchDeletePolicy {
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

    public RestClientBatchDeletePolicy() {
    }

    public BatchDeletePolicy toBatchDeletePolicy() {
        BatchDeletePolicy policy = new BatchDeletePolicy();
        policy.filterExp = PolicyValueConverter.getFilterExp(filterExp);
        policy.commitLevel = commitLevel;
        policy.generationPolicy = generationPolicy;
        policy.generation = generation;
        policy.durableDelete = durableDelete;
        policy.sendKey = sendKey;

        return policy;
    }
}
