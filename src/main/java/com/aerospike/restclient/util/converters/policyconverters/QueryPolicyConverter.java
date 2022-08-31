package com.aerospike.restclient.util.converters.policyconverters;

import com.aerospike.client.policy.Policy;
import com.aerospike.client.policy.QueryPolicy;
import com.aerospike.client.policy.ScanPolicy;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.aerospike.restclient.util.converters.PolicyValueConverter;

import java.util.Map;

public class QueryPolicyConverter {

    public static QueryPolicy queryPolicyFromMap(Map<String, String> policyMap) {
        Policy basePolicy = PolicyConverter.policyFromMap(policyMap);
        QueryPolicy queryPolicy = new QueryPolicy(basePolicy);

        if (policyMap.containsKey(AerospikeAPIConstants.MAX_CONCURRENT_NODES)) {
            queryPolicy.maxConcurrentNodes = PolicyValueConverter.getIntValue(
                    policyMap.get(AerospikeAPIConstants.MAX_CONCURRENT_NODES));
        }
        if (policyMap.containsKey(AerospikeAPIConstants.RECORD_QUEUE_SIZE)) {
            queryPolicy.recordQueueSize = PolicyValueConverter.getIntValue(
                    policyMap.get(AerospikeAPIConstants.RECORD_QUEUE_SIZE));
        }
        if (policyMap.containsKey(AerospikeAPIConstants.INCLUDE_BIN_DATA)) {
            queryPolicy.includeBinData = PolicyValueConverter.getBoolValue(
                    policyMap.get(AerospikeAPIConstants.INCLUDE_BIN_DATA));
        }
        if (policyMap.containsKey(AerospikeAPIConstants.FAIL_ON_CLUSTER_CHANGE)) {
            queryPolicy.failOnClusterChange = PolicyValueConverter.getBoolValue(
                    policyMap.get(AerospikeAPIConstants.FAIL_ON_CLUSTER_CHANGE));
        }
        if (policyMap.containsKey(AerospikeAPIConstants.SHORT_QUERY)) {
            queryPolicy.shortQuery = PolicyValueConverter.getBoolValue(
                    policyMap.get(AerospikeAPIConstants.SHORT_QUERY));
        }

        return queryPolicy;
    }

}
