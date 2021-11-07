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
package com.aerospike.restclient.controller;

import com.aerospike.client.policy.Policy;
import com.aerospike.restclient.controllers.KeyValueController;
import com.aerospike.restclient.service.AerospikeRecordService;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.aerospike.restclient.util.AerospikeAPIConstants.RecordKeyType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Collections;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class KVControllerGetRecordKeyTypeTests {

	@Autowired KeyValueController controller;
	@MockBean AerospikeRecordService recordService;

	private final String ns = "test";
	private final String set = "set";
	private final String key = "key";

	private MultiValueMap<String, String> queryParams;
	private RecordKeyType expectedKeyType;

	private static Stream<Arguments> keyType() {
		return Stream.of(
				Arguments.of(RecordKeyType.STRING),
				Arguments.of(RecordKeyType.BYTES),
				Arguments.of(RecordKeyType.DIGEST),
				Arguments.of(RecordKeyType.INTEGER),
				Arguments.of((Object) null));
	}

	@ParameterizedTest
	@MethodSource("keyType")
	void addKeyType(RecordKeyType keyType) {
		this.expectedKeyType = keyType;
	}

	@BeforeEach
	public void setup() {
		queryParams = new LinkedMultiValueMap<>();
		if (expectedKeyType != null) {
			queryParams.put(AerospikeAPIConstants.KEY_TYPE, Collections.singletonList(expectedKeyType.toString()));
		}
	}

	@Test
	public void testKeyTypeForNSSetKey() {
		controller.getRecordNamespaceSetKey(ns, set, key, queryParams, null);
		verify(recordService, Mockito.only()).fetchRecord(
				isNull(),
				any(String.class),
				any(String.class),
				any(String.class),
				any(String[].class),
				eq(expectedKeyType), isA(Policy.class));
	}

	@Test
	public void testKeyTypeForNSKey() {
		controller.getRecordNamespaceKey(ns, key, queryParams, null);
		verify(recordService, Mockito.only()).fetchRecord(
				isNull(),
				any(String.class),
				isNull(),
				any(String.class),
				any(String[].class),
				eq(expectedKeyType), isA(Policy.class));
	}
}
