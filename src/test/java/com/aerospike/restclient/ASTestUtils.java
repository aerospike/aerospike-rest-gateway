/*
 * Copyright 2022 Aerospike, Inc.
 *
 * Portions may be licensed to Aerospike, Inc. under one or more contributor
 * license agreements WHICH ARE COMPATIBLE WITH THE APACHE LICENSE, VERSION 2.0.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.aerospike.restclient;

import com.aerospike.client.Record;
import com.aerospike.client.*;
import com.aerospike.client.cdt.CTX;
import com.aerospike.client.cluster.Node;
import com.aerospike.client.policy.ClientPolicy;
import com.aerospike.client.policy.TlsPolicy;
import com.aerospike.client.policy.WritePolicy;
import com.aerospike.client.query.Filter;
import com.aerospike.restclient.domain.RestClientOperation;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import org.junit.Assert;
import org.mockito.ArgumentMatcher;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ASTestUtils {

    /*
     * Deep comparison of List<?>
     */
    @SuppressWarnings("unchecked")
    public static boolean compareCollection(List<?> list1, List<?> list2) {
        if (list1 == null || list2 == null) return false;
        if (list1.size() != list2.size()) return false;

        for (int i = 0; i < list1.size(); i++) {
            Object elem1 = list1.get(i);
            Object elem2 = list2.get(i);
            if (elem1 instanceof byte[]) {
                if (!Arrays.equals((byte[]) elem1, (byte[]) elem2)) return false;
            } else if (elem1 instanceof List) {
                if (elem2 instanceof List) {
                    if (!compareCollection((List<?>) elem1, (List<?>) elem2)) {
                        return false;
                    }
                } else {
                    return false;
                }
            } else if (elem1 instanceof Map) {
                if (elem2 instanceof Map) {
                    if (!compareMap((Map<Object, Object>) elem1, (Map<Object, Object>) elem2)) {
                        return false;
                    }
                } else {
                    return false;
                }
            } else if (!compareSimpleValues(elem1, elem2)) {
                return false;
            }
        }
        return true;
    }

    public static boolean containSameItems(List<?> list1, List<?> list2) {
        if (list1 == null || list2 == null) return false;
        if (list1.size() != list2.size()) return false;
        Map<Object, Integer> counts1 = new HashMap<>();
        Map<Object, Integer> counts2 = new HashMap<>();

        for (Object obj : list1) {
            if (counts1.containsKey(obj)) {
                counts1.put(obj, counts1.get(obj) + 1);
            } else {
                counts1.put(obj, 1);
            }
        }

        for (Object obj : list2) {
            if (counts2.containsKey(obj)) {
                counts2.put(obj, counts1.get(obj) + 1);
            } else {
                counts2.put(obj, 1);
            }
        }

        // The two maps are the same if the contain the same keys
        // and for every key in s1, s1[key] == s2[key]
        if (!counts1.keySet().equals(counts2.keySet())) {
            return false;
        }
        for (Object key : counts1.keySet()) {
            if (!compareSimpleValues(counts1.get(key), counts2.get(key))) {
                return false;
            }
        }

        return true;
    }

    @SuppressWarnings("unchecked")
    /*
     * Deep comparison of Map<Object, Object>
     */ public static boolean compareMap(Map<Object, Object> map1, Map<Object, Object> map2) {
        if (map1 == null || map2 == null) return false;
        if (map1.size() != map2.size()) return false;

        for (Object key : map1.keySet()) {
            Object value1 = map1.get(key);
            Object value2 = map2.get(key);
            if (value1 instanceof byte[]) {
                if (!Arrays.equals((byte[]) value1, (byte[]) value2)) return false;
            } else if (value1 instanceof List) {
                if (!compareCollection((List<?>) value1, (List<?>) value2)) {
                    return false;
                }
            } else if (value1 instanceof Map) {
                if (!compareMap((Map<Object, Object>) value1, (Map<Object, Object>) value2)) {
                    return false;
                }
            } else if (!compareSimpleValues(value1, value2)) {
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    /*
     * Deep comparison of Map<String, Object>
     */ public static boolean compareMapStringObj(Map<String, Object> map1, Map<String, Object> map2) {
        if (map1 == null || map2 == null) return false;
        if (map1.size() != map2.size()) return false;

        for (String key : map1.keySet()) {
            Object value1 = map1.get(key);
            Object value2 = map2.get(key);
            if (value1 instanceof byte[]) {
                if (!Arrays.equals((byte[]) value1, (byte[]) value2)) {
                    return false;
                }
            } else if (value1.getClass().isArray()) {
                if (!Arrays.equals((Object[]) value1, (Object[]) value2)) {
                    return false;
                }
            } else if (value1 instanceof List) {
                if (!compareCollection((List<?>) value1, (List<?>) value2)) {
                    return false;
                }
            } else if (value1 instanceof Map) {
                if (!compareMap((Map<Object, Object>) value1, (Map<Object, Object>) value2)) {
                    return false;
                }
            } else if (!compareSimpleValues(value1, value2)) {
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    /*
     * Deep comparison of Map<String, Object>
     */ public static boolean compareRCRecordToASRecord(Map<String, Object> rcRecord, Record asRecord) {
        Map<String, Object> map1 = (Map<String, Object>) rcRecord.get("bins");
        Map<String, Object> map2 = asRecord.bins;
        if (map1 == null || map2 == null) return false;
        if (map1.size() != map2.size()) return false;

        for (String key : map1.keySet()) {
            Object value1 = map1.get(key);
            Object value2 = map2.get(key);
            if (value1 instanceof byte[]) {
                if (!Arrays.equals((byte[]) value1, (byte[]) value2)) {
                    return false;
                }
            } else if (value1 instanceof List) {
                if (!compareCollection((List<?>) value1, (List<?>) value2)) {
                    return false;
                }
            } else if (value1 instanceof Map) {
                if (!compareMap((Map<Object, Object>) value1, (Map<Object, Object>) value2)) {
                    return false;
                }
            } else if (!compareSimpleValues(value1, value2)) {
                return false;
            }
        }
        return true;
    }

    public static boolean compareSimpleValues(Object value1, Object value2) {
        /* Compare long and integer safely */
        if ((value1 instanceof Integer && value2 instanceof Long) || (value2 instanceof Integer && value1 instanceof Long)) {
            Number nv1 = (Number) value1;
            Number nv2 = (Number) value2;
            return nv1.longValue() == nv2.longValue();
        } else if (value1 == null && value2 == null) {
            return true;
        } else if (value1 == null || value2 == null) {
            return true;
        } else {
            if (value1 instanceof Value) {
                value1 = ((Value) value1).getObject();
            }
            if (value2 instanceof Value) {
                value2 = ((Value) value2).getObject();
            }
            return value1.equals(value2);
        }

    }

    /*
     * Deep comparison of Map<String, String>
     */
    public static boolean compareStringMap(Map<String, String> map1, Map<String, String> map2) {
        if (map1 == null || map2 == null) return false;
        if (map1.size() != map2.size()) return false;

        for (String key : map1.keySet()) {
            String value1 = map1.get(key);
            String value2 = map2.get(key);
            if (!value1.equals(value2)) {
                return false;
            }
        }

        return true;
    }

    public static boolean compareKeys(Key expected, Key actual) {
        if (!actual.namespace.equals(expected.namespace)) {
            return false;
        }

        if (actual.setName == null) {
            if (expected.setName != null) {
                return false;
            }
        } else if (!actual.setName.equals(expected.setName)) {
            return false;
        }

        if (actual.userKey == null) {
            if (expected.userKey != null) {
                return false;
            }
        } else if (!actual.userKey.equals(expected.userKey)) {
            return false;
        }

        return Arrays.equals(actual.digest, expected.digest);
    }

    public static void compareCTX(CTX expected, CTX actual) {
        Assert.assertEquals(expected.id, actual.id);
        Assert.assertEquals(expected.value, actual.value);
    }

    public static void compareFilter(Filter expected, Filter actual) {
        Assert.assertEquals(expected, actual);
        Assert.assertArrayEquals(expected.getPackedCtx(),
                actual.getPackedCtx()); // At this time, equal() does not check CTX
    }

    /* Build an URL for an operation which takes multiple bins as query params; e.g, /kvs/test/demo/1?bins=a&bins=b&bins=c
     *
     */
    public static String buildEndpointV1(String prefix, String namespace, String set, String key, String[] bins) {
        StringBuilder endpointBuilder = new StringBuilder("/v1/" + prefix + "/" + namespace + "/" + set + "/" + key);
        if (bins.length > 0) {
            endpointBuilder.append("?" + AerospikeAPIConstants.RECORD_BINS + "=" + bins[0]);
            for (int i = 1; i < bins.length; i++) {
                String bin = bins[i];
                endpointBuilder.append("&" + AerospikeAPIConstants.RECORD_BINS + "=" + bin);
            }
        }
        return endpointBuilder.toString();
    }

    public static String addFilterBins(String endpoint, String[] bins) {
        StringBuilder endpointBuilder = new StringBuilder(endpoint);
        if (bins.length > 0) {
            endpointBuilder.append("?" + AerospikeAPIConstants.RECORD_BINS + "=" + bins[0]);
            for (int i = 1; i < bins.length; i++) {
                String bin = bins[i];
                endpointBuilder.append("&" + AerospikeAPIConstants.RECORD_BINS + "=" + bin);
            }
        }
        return endpointBuilder.toString();
    }

    public static String buildEndpointV1(String prefix, String namespace, String set, String[] keys, String[] bins) {
        StringBuilder endpointBuilder = new StringBuilder("/v1/" + prefix + "/" + namespace + "/" + set);
        if (keys.length > 0) {
            endpointBuilder.append("?key=" + keys[0]);
            for (int i = 1; i < keys.length; i++) {
                String key = keys[i];
                endpointBuilder.append("&key=" + key);
            }
        }
        if (bins != null && bins.length > 0) {
            for (String bin : bins) {
                endpointBuilder.append("&" + AerospikeAPIConstants.RECORD_BINS + "=" + bin);
            }
        }
        return endpointBuilder.toString();
    }

    public static String buildEndpointV1(String prefix, String namespace, String set, String key) {
        return buildEndpoint("/v1", prefix, namespace, set, key);
    }

    public static String buildEndpointV1(String prefix, String namespace, String key) {
        return buildEndpoint("/v1", prefix, namespace, key);
    }

    public static String buildEndpointV2(String prefix, String namespace, String set, String key) {
        return buildEndpoint("/v2", prefix, namespace, set, key);
    }

    public static String buildEndpointV2(String prefix, String namespace, String key) {
        return buildEndpoint("/v2", prefix, namespace, key);
    }

    public static String buildEndpoint(String... path) {
        return String.join("/", path);
    }

    public static boolean runningWithAuth() {
        String password = System.getenv("aerospike.restclient.clientpolicy.password");
        if (password == null) {
            password = System.getenv("aerospike_restclient_clientpolicy_password");
        }
        String user = System.getenv("aerospike.restclient.clientpolicy.user");
        if (user == null) {
            user = System.getenv("aerospike_restclient_clientpolicy_user");
        }
        return password != null && user != null;
    }

    /*
     * Get a Host object from the configured environment.
     */
    public static Host getHost() {
        String portStr = System.getenv("aerospike_restclient_port");
        portStr = portStr != null ? portStr : "3000";
        Host host;
        if (System.getenv("aerospike_restclient_hostlist") != null) {
            String hostlist = System.getenv("aerospike_restclient_hostlist");
            host = Host.parseHosts(hostlist, Integer.parseInt(portStr))[0];
        } else {
            String hostname = System.getenv("aerospike_restclient_hostname");
            if (hostname == null) {
                hostname = "localhost";
            }
            host = new Host(hostname, Integer.parseInt(portStr));
        }

        return host;
    }

    /*
     * Verify that the given index exists on every node. This iterates over every node
     * and checks that the node is present on it. If not found initially, the function sleeps for a quarter
     * second and retries. If any node does not contain the index after 2.5 seconds, returns false.
     * This may return false negatives.
     */
    public static boolean indexExists(AerospikeClient client, String namespace, String indexName) {
        for (Node node : client.getNodes()) {
            int attempts = 10;
            while (attempts > 0) {
                String response = Info.request(null, node, "sindex/" + namespace + "/" + indexName);
                if (response.trim().startsWith("FAIL:201")) {
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                    }
                } else {
                    break;
                }
                attempts--;
            }
            if (attempts == 0) {
                return false;
            }
        }
        return true;
    }

    public static ClientPolicy getClientPolicy(TlsPolicy tlsPol, String user, String password) {
        ClientPolicy policy = new ClientPolicy();
        if (user != null && !user.isEmpty()) {
            policy.user = user;
            policy.password = password;
        }
        policy.tlsPolicy = tlsPol;
        return policy;
    }

    /*
     *	Drop the index and wait for it to disappear from each node in the cluster
     */
    public static void ensureDeletion(AerospikeClient client, String namespace, String indexName) {
        try {
            client.dropIndex(null, namespace, null, indexName).waitTillComplete(500, 5000);
        } catch (AerospikeException e) {
            if (e.getResultCode() != ResultCode.INDEX_NOTFOUND) {
                throw e;
            }
        }
        for (Node node : client.getNodes()) {
            int attempts = 10;
            while (attempts > 0) {
                String response = Info.request(null, node, "sindex/" + namespace + "/" + indexName);
                if (response.trim().startsWith("FAIL:201")) {
                    break;
                }
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                }
                attempts--;
            }
        }
    }

    public static void performOperation(MockMvc mockMVC, String endpoint, String payload) throws Exception {

        mockMVC.perform(post(endpoint).contentType(MediaType.APPLICATION_JSON).content(payload))
                .andExpect(status().isOk());
    }

    public static String performOperationAndReturn(MockMvc mockMVC, String endpoint, String payload) throws Exception {

        MockHttpServletResponse res = mockMVC.perform(
                post(endpoint).contentType(MediaType.APPLICATION_JSON).content(payload)).andReturn().getResponse();

        int status = res.getStatus();
        if (status != 200) {
            Assert.fail(
                    String.format("Status expected:200 but was:%d\n Response: %s", status, res.getContentAsString()));
        }

        return res.getContentAsString();
    }

    public static String performOperationAndExpect(MockMvc mockMVC, String endpoint, String payload,
                                                   ResultMatcher matcher) throws Exception {

        ResultActions resultActions = mockMVC.perform(
                post(endpoint).contentType(MediaType.APPLICATION_JSON).content(payload));

        String resp = resultActions.andReturn().getResponse().getContentAsString();

        try {
            resultActions.andExpect(matcher);
        } catch (AssertionError e) {
            throw new Exception(String.format("Response: %s", resp), e);
        }

        return resp;
    }

    /* Perform Operation utilizing MsgPack for input and output */
    public static void performOperation(MockMvc mockMVC, String endpoint, byte[] payload) throws Exception {

        mockMVC.perform(post(endpoint).contentType("application/msgpack").content(payload)).andExpect(status().isOk());
    }

    /* Perform Operation utilizing MsgPack for input and output. Also return Resulting record to caller as byte[] */
    public static byte[] performOperationAndReturn(MockMvc mockMVC, String endpoint, byte[] payload) throws Exception {

        return mockMVC.perform(
                        post(endpoint).contentType("application/msgpack").content(payload).accept("application/msgpack"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsByteArray();
    }

    /* Perform Operation utilizing MsgPack for input and output. Also return Resulting record to caller as byte[] */
    public static byte[] performOperationAndExpect(MockMvc mockMVC, String endpoint, byte[] payload,
                                                   ResultMatcher matcher) throws Exception {

        return mockMVC.perform(
                        post(endpoint).contentType("application/msgpack").content(payload).accept("application/msgpack"))
                .andExpect(matcher)
                .andReturn()
                .getResponse()
                .getContentAsByteArray();
    }

    /* Check if the server is v 3.16.0.1 or newer */
    public static boolean supportsNewCDT(AerospikeClient client) {
        String response = null;
        try {
            response = Info.request(client.getNodes()[0], "build");
        } catch (Exception e) {
        }
        String[] v_numbers = response.split("\\.");
        int major = Integer.parseInt(v_numbers[0]);
        int minor = Integer.parseInt(v_numbers[1]);
        return (major > 3 || (major == 3 && minor > 15));
    }

    public static class BinMatcher implements ArgumentMatcher<Bin> {

        private final Bin actual;

        public BinMatcher(Bin actual) {
            super();
            this.actual = actual;
        }

        @Override
        public boolean matches(Bin argument) {
            if (!(argument instanceof Bin)) {
                return false;
            }
            Bin binArg = argument;

            return binArg.name.equals(actual.name) && binArg.value.equals(actual.value);
        }

    }

    public static class KeyMatcher implements ArgumentMatcher<Key> {

        private final Key expected;

        public KeyMatcher(Key expected) {
            super();
            this.expected = expected;
        }

        @Override
        public boolean matches(Key argument) {
            if (!(argument instanceof Key)) {
                return false;
            }
            Key actual = argument;

            return compareKeys(expected, actual);
        }

    }

    public static class WritePolicyMatcher implements ArgumentMatcher<WritePolicy> {
        public interface WritePolicyComparator {
            boolean comparePolicy(WritePolicy actual, WritePolicy expected);
        }

        private final WritePolicy expected;
        WritePolicyComparator comparator;

        public WritePolicyMatcher(WritePolicy expected, WritePolicyComparator comparator) {
            super();
            this.expected = expected;
            this.comparator = comparator;
        }

        @Override
        public boolean matches(WritePolicy argument) {
            if (!(argument instanceof WritePolicy)) {
                return false;
            }
            WritePolicy actual = argument;

            return comparator.comparePolicy(actual, expected);
        }

    }

    public static boolean compareRCOperations(RestClientOperation expected, RestClientOperation other) {

        String otherOpString = other.getOperation().name();
        Map<String, Object> otherOpVals = other.getOpValues();

        String expectedOpString = expected.getOperation().name();
        Map<String, Object> expectedOpVals = expected.getOpValues();

        if (expectedOpString != null) {
            if (!expectedOpString.equals(otherOpString)) {
                return false;
            }
        } else if (otherOpString != null) {
            return false;
        }

        if (expectedOpVals != null) {
            return compareMapStringObj(expectedOpVals, otherOpVals);
        }

        return otherOpVals == null;
    }
}

