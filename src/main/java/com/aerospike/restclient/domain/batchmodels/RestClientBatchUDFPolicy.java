package com.aerospike.restclient.domain.batchmodels;

import com.aerospike.client.policy.BatchUDFPolicy;
import com.aerospike.client.policy.CommitLevel;
import com.aerospike.restclient.util.APIDescriptors;
import com.aerospike.restclient.util.converters.PolicyValueConverter;
import io.swagger.v3.oas.annotations.media.Schema;

public class RestClientBatchUDFPolicy {
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

    public RestClientBatchUDFPolicy() {
    }

    public BatchUDFPolicy toBatchUDFPolicy() {
        BatchUDFPolicy policy = new BatchUDFPolicy();
        policy.filterExp = PolicyValueConverter.getFilterExp(filterExp);
        policy.commitLevel = commitLevel;
        policy.expiration = expiration;
        policy.durableDelete = durableDelete;
        policy.sendKey = sendKey;

        return policy;
    }
}
