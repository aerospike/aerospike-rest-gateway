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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mockito;
import org.msgpack.jackson.dataformat.MessagePackFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.aerospike.client.policy.Policy;
import com.aerospike.client.policy.WritePolicy;
import com.aerospike.restclient.controllers.KeyValueController;
import com.aerospike.restclient.service.AerospikeRecordService;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.aerospike.restclient.util.AerospikeAPIConstants.RecordKeyType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(Parameterized.class)
@SpringBootTest
public class KVControllerV1KeyTypeTests {

	@ClassRule
	public static final SpringClassRule springClassRule = new SpringClassRule();
	@Rule
	public final SpringMethodRule springMethodRule = new SpringMethodRule();

	@Autowired KeyValueController controller;
	@MockBean AerospikeRecordService recordService;

	private String ns = "test";
	private String set = "set";
	private String key = "key";

	private Map<String, Object> dummyBins;
	private Map<String, String> queryParams;
	private MultiValueMap<String, String> multiQueryParams;
	private RecordKeyType expectedKeyType;

	private byte[] msgpackBins;
	private ObjectMapper mpMapper = new ObjectMapper(new MessagePackFactory());

	@Parameters
	public static Object[] keyType() {
		return new Object[] {
				RecordKeyType.STRING,
				RecordKeyType.BYTES,
				RecordKeyType.DIGEST,
				RecordKeyType.INTEGER,
				null
		};
	}
	public KVControllerV1KeyTypeTests(RecordKeyType keyType) {
		this.expectedKeyType = keyType;
	}

	@Before
	public void setup() throws JsonProcessingException {
		dummyBins = new HashMap<String, Object>();
		dummyBins.put("bin", "a");
		msgpackBins = mpMapper.writeValueAsBytes(dummyBins);
		queryParams = new HashMap<String, String>();
		multiQueryParams = new LinkedMultiValueMap<String, String>();

		if (expectedKeyType != null) {
			queryParams.put(AerospikeAPIConstants.KEY_TYPE, expectedKeyType.toString());
			multiQueryParams.put(AerospikeAPIConstants.KEY_TYPE, Arrays.asList(expectedKeyType.toString()));
		}
	}

	/* UPDATE */
	@Test
	public void testKeyTypeForUpdateNSSetKey() {
		controller.updateRecordNamespaceSetKey(ns, set, key, dummyBins, queryParams);

		verify(recordService, Mockito.only()).storeRecord(
				eq(ns), eq(set), eq(key), eq(dummyBins), eq(expectedKeyType),
				isA(WritePolicy.class));
	}

	@Test
	public void testRecordKeyTypeForUpdateNSKey() {
		controller.updateRecordNamespaceKey(ns, key, dummyBins, queryParams);

		verify(recordService, Mockito.only()).storeRecord(
				eq(ns), isNull(), eq((key)), eq(dummyBins), eq(expectedKeyType),
				isA(WritePolicy.class));
	}

	@Test
	public void testRecordKeyTypeForUpdateNSSetKeyMP() {
		controller.updateRecordNamespaceSetKeyMP(ns, set, key, new ByteArrayInputStream(msgpackBins), queryParams);

		verify(recordService, Mockito.only()).storeRecord(
				eq(ns), eq(set), eq(key), eq(dummyBins), eq(expectedKeyType),
				isA(WritePolicy.class));
	}

	@Test
	public void testRecordKeyTypeForUpdateNSKeyMP() {
		controller.updateRecordNamespaceKeyMP(ns, key, new ByteArrayInputStream(msgpackBins), queryParams);

		verify(recordService, Mockito.only()).storeRecord(
				eq(ns), isNull(), eq((key)), eq(dummyBins), eq(expectedKeyType),
				isA(WritePolicy.class));
	}

	/* DELETE */
	@Test
	public void testKeyTypeDeleteNSSetKey() {
		controller.deleteRecordNamespaceSetKey(ns, set, key, queryParams);
		verify(recordService, Mockito.only()).deleteRecord(
				any(String.class),
				any(String.class),
				any(String.class),
				eq(expectedKeyType),
				any(WritePolicy.class));
	}
	@Test
	public void testKeyTypeDeleteNSKey() {
		controller.deleteRecordNamespaceKey(ns, key, queryParams);
		verify(recordService, Mockito.only()).deleteRecord(
				any(String.class),
				isNull(),
				any(String.class),
				eq(expectedKeyType),
				any(WritePolicy.class));
	}
	/* CREATE */
	@Test
	public void testKeyTypeCreateNSSetKey() {
		controller.createRecordNamespaceSetKey(ns, set, key, dummyBins, queryParams);

		verify(recordService, Mockito.only()).storeRecord(
				eq(ns), eq(set), eq(key), eq(dummyBins), eq(expectedKeyType),
				isA(WritePolicy.class));
	}

	@Test
	public void testRecordKeyTypeCreateNSKey() {
		controller.createRecordNamespaceKey(ns, key, dummyBins, queryParams);

		verify(recordService, Mockito.only()).storeRecord(
				eq(ns), isNull(), eq((key)), eq(dummyBins), eq(expectedKeyType),
				isA(WritePolicy.class));
	}

	@Test
	public void testRecordKeyTypeCreateNSSetKeyMP() {
		controller.createRecordNamespaceSetKeyMP(ns, set, key, new ByteArrayInputStream(msgpackBins), queryParams);

		verify(recordService, Mockito.only()).storeRecord(
				eq(ns), eq(set), eq(key), eq(dummyBins), eq(expectedKeyType),
				isA(WritePolicy.class));
	}

	@Test
	public void testRecordKeyTypeCreateNSKeyMP() {
		controller.createRecordNamespaceKeyMP(ns, key, new ByteArrayInputStream(msgpackBins), queryParams);

		verify(recordService, Mockito.only()).storeRecord(
				eq(ns), isNull(), eq((key)), eq(dummyBins), eq(expectedKeyType),
				isA(WritePolicy.class));
	}
	/* REPLACE */
	@Test
	public void testKeyTypeReplaceNSSetKey() {
		controller.replaceRecordNamespaceSetKey(ns, set, key, dummyBins, queryParams);

		verify(recordService, Mockito.only()).storeRecord(
				eq(ns), eq(set), eq(key), eq(dummyBins), eq(expectedKeyType),
				isA(WritePolicy.class));
	}

	@Test
	public void testRecordKeyTypeReplaceNSKey() {
		controller.replaceRecordNamespaceKey(ns, key, dummyBins, queryParams);

		verify(recordService, Mockito.only()).storeRecord(
				eq(ns), isNull(), eq((key)), eq(dummyBins), eq(expectedKeyType),
				isA(WritePolicy.class));
	}

	@Test
	public void testRecordKeyTypeReplaceNSSetKeyMP() {
		controller.replaceRecordNamespaceSetKeyMP(ns, set, key, new ByteArrayInputStream(msgpackBins), queryParams);

		verify(recordService, Mockito.only()).storeRecord(
				eq(ns), eq(set), eq(key), eq(dummyBins), eq(expectedKeyType),
				isA(WritePolicy.class));
	}

	@Test
	public void testRecordKeyTypeReplaceNSKeyMP() {
		controller.replaceRecordNamespaceKeyMP(ns, key, new ByteArrayInputStream(msgpackBins), queryParams);

		verify(recordService, Mockito.only()).storeRecord(
				eq(ns), isNull(), eq((key)), eq(dummyBins), eq(expectedKeyType),
				isA(WritePolicy.class));
	}
	/*GET */
	@Test
	public void testKeyTypeForNSSetKey() {
		controller.getRecordNamespaceSetKey(ns, set, key, multiQueryParams);
		verify(recordService, Mockito.only()).fetchRecord(
				any(String.class),
				any(String.class),
				any(String.class),
				any(String[].class),
				eq(expectedKeyType), isA(Policy.class));
	}
	@Test
	public void testKeyTypeForNSKey() {
		controller.getRecordNamespaceKey(ns, key, multiQueryParams);
		verify(recordService, Mockito.only()).fetchRecord(
				any(String.class),
				isNull(),
				any(String.class),
				any(String[].class),
				eq(expectedKeyType), isA(Policy.class));
	}
}
