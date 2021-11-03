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
package com.aerospike.restclient.domain;

import java.util.HashMap;
import java.util.Map;

import com.aerospike.restclient.util.AerospikeOperation;

import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RestClientOperationTest {

	@Test
	public void testEmptyConstruction(){
		RestClientOperation op = new RestClientOperation();
		AerospikeOperation operation = AerospikeOperation.valueOf("ADD");
		Map<String, Object> values = new HashMap<>();

		op.setOperation(operation);
		op.setOpValues(values);

		assertEquals(operation, op.getOperation());
		assertEquals(values, op.getOpValues());
	}

	@Test
	public void testToMap(){
		RestClientOperation op = new RestClientOperation();
		AerospikeOperation operation = AerospikeOperation.valueOf("ADD");
		Map<String, Object> values = new HashMap<>();

		op.setOperation(operation);
		op.setOpValues(values);

		Map<String, Object>convertedMap = op.toMap();
		assertEquals(convertedMap.get(AerospikeAPIConstants.OPERATION_FIELD), operation);
		assertEquals(convertedMap.get(AerospikeAPIConstants.OPERATION_VALUES_FIELD), values);
	}

	@Test
	public void testObjectMapperInstantiation() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		TypeReference<RestClientOperation> rcoType = new TypeReference<RestClientOperation>(){};
		AerospikeOperation operation = AerospikeOperation.valueOf("ADD");
		Map<String, Object> values = new HashMap<>();
		Map<String, Object> opMap = new HashMap<>();

		opMap.put(AerospikeAPIConstants.OPERATION_FIELD, operation);
		opMap.put(AerospikeAPIConstants.OPERATION_VALUES_FIELD, values);

		String JsonOp = mapper.writeValueAsString(opMap);

		RestClientOperation rcOp = mapper.readValue(JsonOp, rcoType);

		assertEquals(operation, rcOp.getOperation());
		assertEquals(values, rcOp.getOpValues());
	}

	@Test
	public void testObjectMapperSerialization() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		RestClientOperation op = new RestClientOperation();

		TypeReference<Map<String, Object>> somType = new TypeReference<Map<String, Object>>() {};
		AerospikeOperation operation = AerospikeOperation.valueOf("ADD");
		Map<String, Object> values = new HashMap<>();

		op.setOperation(operation);
		op.setOpValues(values);

		String jsonOp = mapper.writeValueAsString(op);

		Map<String, Object>deserializedOp = mapper.readValue(jsonOp, somType);

		assertEquals(deserializedOp.get(AerospikeAPIConstants.OPERATION_FIELD), operation.name());
		assertEquals(deserializedOp.get(AerospikeAPIConstants.OPERATION_VALUES_FIELD), values);
	}
}
