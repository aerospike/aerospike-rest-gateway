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
package com.aerospike.restclient;

import com.aerospike.client.AerospikeException;
import com.aerospike.restclient.util.InfoResponseParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InfoResponseParserTests {

	@Test
	public void getNewStyleReplicationFactor() {
		int replicationFactor = 2;
		String replicationFactorString = String.format("'ns_cluster_size=1;effective_replication_factor=%d;objects=0;", replicationFactor);
		assertEquals(replicationFactor, InfoResponseParser.getReplicationFactor(replicationFactorString, "irrelevant"));
	}

	@Test
	public void getOldStyleReplicationFactor1() {
		int replicationFactor = 2;
		String replicationFactorString = String.format("'ns_cluster_size=1;repl-factor=%d;objects=0;", replicationFactor);
		assertEquals(replicationFactor, InfoResponseParser.getReplicationFactor(replicationFactorString, "irrelevant"));
	}

	@Test
	public void getOldStyleReplicationFactor2() {
		int replicationFactor = 2;
		String replicationFactorString = String.format("'ns_cluster_size=1;replication-factor=%d;objects=0;", replicationFactor);
		assertEquals(replicationFactor, InfoResponseParser.getReplicationFactor(replicationFactorString, "irrelevant"));
	}

	@Test
	public void invalidReplicationFactorResponse() {
		String replicationFactorString = "";
		assertThrows(AerospikeException.class, () -> InfoResponseParser.getReplicationFactor(replicationFactorString, "irrelevant"));
	}
}
