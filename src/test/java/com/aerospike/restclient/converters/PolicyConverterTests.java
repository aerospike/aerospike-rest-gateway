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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.aerospike.client.policy.CommitLevel;
import com.aerospike.client.policy.GenerationPolicy;
import com.aerospike.client.policy.Priority;
import com.aerospike.client.policy.RecordExistsAction;
import com.aerospike.client.policy.Replica;
import com.aerospike.restclient.util.RestClientErrors.InvalidPolicyValueError;
import com.aerospike.restclient.util.converters.PolicyValueConverter;

public class PolicyConverterTests {


	@Test
	public void testPriority() {
		assertEquals(Priority.HIGH, PolicyValueConverter.getPriority("HIGH"));
		assertEquals(Priority.MEDIUM, PolicyValueConverter.getPriority("MEDIUM"));
		assertEquals(Priority.LOW, PolicyValueConverter.getPriority("LOW"));
		assertEquals(Priority.DEFAULT, PolicyValueConverter.getPriority("DEFAULT"));
	}

	@Test(expected=InvalidPolicyValueError.class)
	public void testInvalidPriority() {
		PolicyValueConverter.getPriority("highest");
	}

	@Test
	public void testReplica() {
		assertEquals(Replica.MASTER, PolicyValueConverter.getReplica("MASTER"));
		assertEquals(Replica.MASTER_PROLES, PolicyValueConverter.getReplica("MASTER_PROLES"));
		assertEquals(Replica.RANDOM, PolicyValueConverter.getReplica("RANDOM"));
		assertEquals(Replica.SEQUENCE, PolicyValueConverter.getReplica("SEQUENCE"));
	}

	@Test(expected=InvalidPolicyValueError.class)
	public void testInvalidReplica() {
		PolicyValueConverter.getReplica("none");
	}

	@Test
	public void testCommitLevel() {
		assertEquals(CommitLevel.COMMIT_ALL, PolicyValueConverter.getCommitLevel("COMMIT_ALL"));
		assertEquals(CommitLevel.COMMIT_MASTER, PolicyValueConverter.getCommitLevel("COMMIT_MASTER"));
	}

	@Test(expected=InvalidPolicyValueError.class)
	public void testInvalidCommitLevel() {
		PolicyValueConverter.getCommitLevel("commited_enough");
	}

	@Test
	public void testGenerationPolicy() {
		assertEquals(GenerationPolicy.EXPECT_GEN_EQUAL, PolicyValueConverter.getGenerationPolicy("EXPECT_GEN_EQUAL"));
		assertEquals(GenerationPolicy.EXPECT_GEN_GT, PolicyValueConverter.getGenerationPolicy("EXPECT_GEN_GT"));
		assertEquals(GenerationPolicy.NONE, PolicyValueConverter.getGenerationPolicy("NONE"));
	}

	@Test(expected=InvalidPolicyValueError.class)
	public void testInvalidGenerationPolicy() {
		PolicyValueConverter.getGenerationPolicy("lt");
	}

	@Test
	public void testRecordExistsAction() {
		assertEquals(RecordExistsAction.CREATE_ONLY, PolicyValueConverter.getRecordExistsAction("CREATE_ONLY"));
		assertEquals(RecordExistsAction.REPLACE, PolicyValueConverter.getRecordExistsAction("REPLACE"));
		assertEquals(RecordExistsAction.REPLACE_ONLY, PolicyValueConverter.getRecordExistsAction("REPLACE_ONLY"));
		assertEquals(RecordExistsAction.UPDATE, PolicyValueConverter.getRecordExistsAction("UPDATE"));
		assertEquals(RecordExistsAction.UPDATE_ONLY, PolicyValueConverter.getRecordExistsAction("UPDATE_ONLY"));
	}

	@Test(expected=InvalidPolicyValueError.class)
	public void testInvalidRecordExistsAction() {
		PolicyValueConverter.getRecordExistsAction("nothing");
	}

	@Test(expected=InvalidPolicyValueError.class)
	public void testIntvalue() {
		PolicyValueConverter.getIntValue("five");
	}
}
