package com.aerospike.restclient.domain.batchmodels;

import com.aerospike.client.policy.ReadModeAP;
import com.aerospike.client.policy.ReadModeSC;
import com.aerospike.restclient.util.APIDescriptors;
import com.aerospike.restclient.util.converters.PolicyValueConverter;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "BatchReadPolicy",
        description = "An object that describes a policy for a read operation used in a batch request.",
        externalDocs = @ExternalDocumentation(
                url =
                        "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.1/com/aerospike/client/policy/BatchReadPolicy.html"
        )
)
public class BatchReadPolicy {
    @Schema(description = APIDescriptors.BATCH_POLICY_FILTER_EXP_NOTES)
    public String filterExp;
    @Schema(description = APIDescriptors.BATCH_POLICY_READMODEAP_NOTES)
    public ReadModeAP readModeAP;
    @Schema(description = APIDescriptors.BATCH_POLICY_READMODESC_NOTES)
    public ReadModeSC readModeSC;

    public BatchReadPolicy() {
    }

    public com.aerospike.client.policy.BatchReadPolicy toBatchReadPolicy() {
        com.aerospike.client.policy.BatchReadPolicy policy = new com.aerospike.client.policy.BatchReadPolicy();
        policy.filterExp = PolicyValueConverter.getFilterExp(filterExp);
        policy.readModeAP = readModeAP;
        policy.readModeSC = readModeSC;

        return policy;
    }
}
