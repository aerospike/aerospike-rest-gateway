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

import com.aerospike.client.AerospikeException;
import com.aerospike.restclient.controllers.KeyValueController;
import com.aerospike.restclient.service.AerospikeRecordService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.msgpack.jackson.dataformat.MessagePackFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;

import static org.junit.jupiter.api.Assertions.assertThrows;

/*
 * Test that exceptions thrown further in the chain are propagated back through these controllers
 *
 */
public class KVControllerV1PutPostUpdateErrorTests {

	private final String ns = "test";
	private final String set = "set";
	private final String key = "key";

	private Map<String, Object> dummyBins;
	private Map<String, String> queryParams;
	private final AerospikeException expectedException = new AerospikeException("test exception");
	private byte[] msgpackBins;
	private final ObjectMapper mpMapper = new ObjectMapper(new MessagePackFactory());

	@Autowired KeyValueController controller;
	@MockBean AerospikeRecordService recordService;

	@SuppressWarnings("unchecked")
	@BeforeEach
	public void setup() throws JsonProcessingException {
		dummyBins = new HashMap<>();
		dummyBins.put("bin", "a");
		msgpackBins = mpMapper.writeValueAsBytes(dummyBins);
		queryParams = new HashMap<>();
		Mockito.doThrow(expectedException)
		.when(recordService)
		.storeRecord(isNull(), anyString(), any(), anyString(), any(dummyBins.getClass()), any(), any());
	}

	/* Create/Post */
	@Test()
	public void testCreateNSSetKey() {
		assertThrows(AerospikeException.class, () -> controller.createRecordNamespaceSetKey(ns, set, key, dummyBins, queryParams, null));
	}

	@Test()
	public void testCreateNSKey() {
		assertThrows(AerospikeException.class, () -> controller.createRecordNamespaceKey(ns, key, dummyBins, queryParams, null));
	}

	@Test()
	public void testCreateNSSetKeyMP() {
		assertThrows(AerospikeException.class, () -> controller.createRecordNamespaceSetKeyMP(ns, set, key, new ByteArrayInputStream(msgpackBins), queryParams, null));
	}

	@Test()
	public void testCreateNSKeyMP() {
		assertThrows(AerospikeException.class, () -> controller.createRecordNamespaceKeyMP(ns, key, new ByteArrayInputStream(msgpackBins), queryParams, null));
	}

	/* Update/Patch */
	@Test()
	public void testUpdateNSSetKey() {
		assertThrows(AerospikeException.class, () -> controller.updateRecordNamespaceSetKey(ns, set, key, dummyBins, queryParams, null));
	}

	@Test()
	public void testUpdateNSKey() {
		assertThrows(AerospikeException.class, () -> controller.updateRecordNamespaceKey(ns, key, dummyBins, queryParams, null));
	}

	@Test()
	public void testUpdateNSSetKeyMP() {
		assertThrows(AerospikeException.class, () -> controller.updateRecordNamespaceSetKeyMP(ns, set, key, new ByteArrayInputStream(msgpackBins), queryParams, null));
	}

	@Test()
	public void testUpdateNSKeyMP() {
		assertThrows(AerospikeException.class, () -> controller.updateRecordNamespaceKeyMP(ns, key, new ByteArrayInputStream(msgpackBins), queryParams, null));
	}

	/* Replace/Put */
	@Test()
	public void testReplaceNSSetKey() {
		assertThrows(AerospikeException.class, () -> controller.replaceRecordNamespaceSetKey(ns, set, key, dummyBins, queryParams, null));
	}

	@Test()
	public void testReplaceNSKey() {
		assertThrows(AerospikeException.class, () -> controller.replaceRecordNamespaceKey(ns, key, dummyBins, queryParams, null));
	}

	@Test()
	public void testReplaceNSSetKeyMP() {
		assertThrows(AerospikeException.class, () -> controller.replaceRecordNamespaceSetKeyMP(ns, set, key, new ByteArrayInputStream(msgpackBins), queryParams, null));
	}

	@Test()
	public void testReplaceNSKeyMP() {
		assertThrows(AerospikeException.class, () -> controller.replaceRecordNamespaceKeyMP(ns, key, new ByteArrayInputStream(msgpackBins), queryParams, null));
	}
}
