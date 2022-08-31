package com.aerospike.restclient.converters.policyConverters;

import com.aerospike.client.policy.BatchPolicy;
import com.aerospike.client.policy.QueryPolicy;
import com.aerospike.client.policy.Replica;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.aerospike.restclient.util.converters.policyconverters.BatchPolicyConverter;
import com.aerospike.restclient.util.converters.policyconverters.QueryPolicyConverter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;


public class QueryPolicyConverterTest {

    Map<String, String> policyMap;

    @Before
    public void setup() {
        policyMap = new HashMap<>();
    }

    @Test
    public void testMaxConcurrentNodes() {
        policyMap.put(AerospikeAPIConstants.MAX_CONCURRENT_NODES, "999");
        QueryPolicy policy = QueryPolicyConverter.queryPolicyFromMap(policyMap);
        Assert.assertEquals(policy.maxConcurrentNodes, 999);
    }

    @Test
    public void testRecordQueueSize() {
        policyMap.put(AerospikeAPIConstants.RECORD_QUEUE_SIZE, "1000");
        QueryPolicy policy = QueryPolicyConverter.queryPolicyFromMap(policyMap);
        Assert.assertEquals(policy.recordQueueSize, 1000);
    }

    @Test
    public void testIncludeBinData() {
        policyMap.put(AerospikeAPIConstants.INCLUDE_BIN_DATA, "true");
        QueryPolicy policy = QueryPolicyConverter.queryPolicyFromMap(policyMap);
        Assert.assertTrue(policy.includeBinData);
    }

    @Test
    public void testFailOnClusterChange() {
        policyMap.put(AerospikeAPIConstants.FAIL_ON_CLUSTER_CHANGE, "true");
        QueryPolicy policy = QueryPolicyConverter.queryPolicyFromMap(policyMap);
        Assert.assertTrue(policy.failOnClusterChange);
    }

    @Test
    public void testShortQuery() {
        policyMap.put(AerospikeAPIConstants.SHORT_QUERY, "true");
        QueryPolicy policy = QueryPolicyConverter.queryPolicyFromMap(policyMap);
        Assert.assertTrue(policy.shortQuery);
    }
}
