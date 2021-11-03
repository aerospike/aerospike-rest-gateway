/*
 * Copyright 2019 Aerospike, Inc.
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
package com.aerospike.restclient.converters;

import com.aerospike.client.policy.BatchPolicy;
import com.aerospike.client.policy.Replica;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.aerospike.restclient.util.converters.policyconverters.BatchPolicyConverter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class BatchPolicyConverterTests {

	Map<String, String> policyMap;

	@BeforeEach
	public void setup() {
		policyMap = new HashMap<>();
	}

	@Test
	public void testReplica() {
		policyMap.put(AerospikeAPIConstants.REPLICA, Replica.MASTER.toString());
		BatchPolicy policy = BatchPolicyConverter.batchPolicyFromMap(policyMap);
		assertEquals(policy.replica, Replica.MASTER);
	}

	@Test
	public void testTotalTimeout() {
		policyMap.put(AerospikeAPIConstants.TOTAL_TIMEOUT, "333");
		BatchPolicy policy = BatchPolicyConverter.batchPolicyFromMap(policyMap);
		assertEquals(policy.totalTimeout, 333);
	}

	@Test
	public void testSocketTimeout() {
		policyMap.put(AerospikeAPIConstants.SOCKET_TIMEOUT, "332");
		BatchPolicy policy = BatchPolicyConverter.batchPolicyFromMap(policyMap);
		assertEquals(policy.socketTimeout, 332);
	}

	@Test
	public void testSleepBetweenRetries() {
		policyMap.put(AerospikeAPIConstants.SLEEP_BETWEEN_RETRIES, "111");
		BatchPolicy policy = BatchPolicyConverter.batchPolicyFromMap(policyMap);
		assertEquals(policy.sleepBetweenRetries, 111);
	}

	@Test
	public void testMaxRetries() {
		policyMap.put(AerospikeAPIConstants.MAX_RETRIES, "5");
		BatchPolicy policy = BatchPolicyConverter.batchPolicyFromMap(policyMap);
		assertEquals(policy.maxRetries, 5);
	}

	@Test
	public void testSendKey() {
		policyMap.put(AerospikeAPIConstants.SEND_KEY, "true");
		BatchPolicy policy = BatchPolicyConverter.batchPolicyFromMap(policyMap);
		assertTrue(policy.sendKey);
	}

	@Test
	public void testAllowInline() {
		policyMap.put(AerospikeAPIConstants.ALLOW_INLINE, "false");
		BatchPolicy policy = BatchPolicyConverter.batchPolicyFromMap(policyMap);
		assertFalse(policy.allowInline);
	}

	@Test
	public void testSendSetName() {
		policyMap.put(AerospikeAPIConstants.SEND_SET_NAME, "true");
		BatchPolicy policy = BatchPolicyConverter.batchPolicyFromMap(policyMap);
		assertTrue(policy.sendSetName);
	}

	@Test
	public void testMaxConcurrentThreads() {
		policyMap.put(AerospikeAPIConstants.MAX_CONCURRENT_THREADS, "7");
		BatchPolicy policy = BatchPolicyConverter.batchPolicyFromMap(policyMap);
		assertEquals(policy.maxConcurrentThreads, 7);
	}
}
