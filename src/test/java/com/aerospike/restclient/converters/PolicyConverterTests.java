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

import com.aerospike.client.policy.CommitLevel;
import com.aerospike.client.policy.GenerationPolicy;
import com.aerospike.client.policy.RecordExistsAction;
import com.aerospike.client.policy.Replica;
import com.aerospike.restclient.util.RestClientErrors.InvalidPolicyValueError;
import com.aerospike.restclient.util.converters.PolicyValueConverter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PolicyConverterTests {

	@Test
	public void testReplica() {
		assertEquals(Replica.MASTER, PolicyValueConverter.getReplica("MASTER"));
		assertEquals(Replica.MASTER_PROLES, PolicyValueConverter.getReplica("MASTER_PROLES"));
		assertEquals(Replica.RANDOM, PolicyValueConverter.getReplica("RANDOM"));
		assertEquals(Replica.SEQUENCE, PolicyValueConverter.getReplica("SEQUENCE"));
	}

	@Test()
	public void testInvalidReplica() {
		assertThrows(InvalidPolicyValueError.class, () -> PolicyValueConverter.getReplica("none"));
	}

	@Test
	public void testCommitLevel() {
		assertEquals(CommitLevel.COMMIT_ALL, PolicyValueConverter.getCommitLevel("COMMIT_ALL"));
		assertEquals(CommitLevel.COMMIT_MASTER, PolicyValueConverter.getCommitLevel("COMMIT_MASTER"));
	}

	@Test
	public void testCompress() {
		assertTrue(PolicyValueConverter.getCompress("true"));
		assertTrue(PolicyValueConverter.getCompress("TRUE"));
	}

	@Test()
	public void testInvalidCommitLevel() {
		assertThrows(InvalidPolicyValueError.class, () -> PolicyValueConverter.getCommitLevel("commited_enough"));
	}

	@Test
	public void testGenerationPolicy() {
		assertEquals(GenerationPolicy.EXPECT_GEN_EQUAL, PolicyValueConverter.getGenerationPolicy("EXPECT_GEN_EQUAL"));
		assertEquals(GenerationPolicy.EXPECT_GEN_GT, PolicyValueConverter.getGenerationPolicy("EXPECT_GEN_GT"));
		assertEquals(GenerationPolicy.NONE, PolicyValueConverter.getGenerationPolicy("NONE"));
	}

	@Test()
	public void testInvalidGenerationPolicy() {
		assertThrows(InvalidPolicyValueError.class, () -> PolicyValueConverter.getGenerationPolicy("lt"));
	}

	@Test
	public void testRecordExistsAction() {
		assertEquals(RecordExistsAction.CREATE_ONLY, PolicyValueConverter.getRecordExistsAction("CREATE_ONLY"));
		assertEquals(RecordExistsAction.REPLACE, PolicyValueConverter.getRecordExistsAction("REPLACE"));
		assertEquals(RecordExistsAction.REPLACE_ONLY, PolicyValueConverter.getRecordExistsAction("REPLACE_ONLY"));
		assertEquals(RecordExistsAction.UPDATE, PolicyValueConverter.getRecordExistsAction("UPDATE"));
		assertEquals(RecordExistsAction.UPDATE_ONLY, PolicyValueConverter.getRecordExistsAction("UPDATE_ONLY"));
	}

	@Test()
	public void testInvalidRecordExistsAction() {
		assertThrows(InvalidPolicyValueError.class, () -> PolicyValueConverter.getRecordExistsAction("nothing"));
	}

	@Test()
	public void testIntValue() {
		assertThrows(InvalidPolicyValueError.class, () -> PolicyValueConverter.getIntValue("five"));
	}
}
