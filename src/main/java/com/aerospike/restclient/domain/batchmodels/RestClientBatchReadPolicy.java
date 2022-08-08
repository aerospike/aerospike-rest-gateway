package com.aerospike.restclient.domain.batchmodels;

import com.aerospike.client.policy.BatchReadPolicy;
import com.aerospike.client.policy.ReadModeAP;
import com.aerospike.client.policy.ReadModeSC;
import com.aerospike.restclient.util.APIDescriptors;
import com.aerospike.restclient.util.converters.PolicyValueConverter;
import io.swagger.v3.oas.annotations.media.Schema;

public class RestClientBatchReadPolicy {
    @Schema(description = APIDescriptors.BATCH_POLICY_FILTER_EXP_NOTES)
    public String filterExp;
    @Schema(description = APIDescriptors.BATCH_POLICY_READMODEAP_NOTES)
    public ReadModeAP readModeAP;
    @Schema(description = APIDescriptors.BATCH_POLICY_READMODESC_NOTES)
    public ReadModeSC readModeSC;

    public RestClientBatchReadPolicy() {
    }

    public BatchReadPolicy toBatchReadPolicy() {
        BatchReadPolicy policy = new BatchReadPolicy();
        policy.filterExp = PolicyValueConverter.getFilterExp(filterExp);
        policy.readModeAP = readModeAP;
        policy.readModeSC = readModeSC;

        return policy;
    }
}
