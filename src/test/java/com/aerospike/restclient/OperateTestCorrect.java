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

import static com.aerospike.restclient.util.AerospikeAPIConstants.OPERATION_FIELD;
import static com.aerospike.restclient.util.AerospikeAPIConstants.OPERATION_VALUES_FIELD;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.client.Value.BytesValue;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OperateTestCorrect {

	@Autowired
	private ObjectMapper objectMapper;

	private MockMvc mockMVC;

	@Autowired
	private AerospikeClient client;

	@Autowired
	private WebApplicationContext wac;

	private Key testKey = new Key("test", "junit", "operate");
	private Key intKey = new Key("test", "junit", 1);
	private Key bytesKey = new Key("test", "junit", new byte[] {1, 127, 127, 1});

	private String testEndpoint = ASTestUtils.buildEndpoint("operate", "test", "junit", "operate");
	private TypeReference<Map<String, Object>>binType = new TypeReference<Map<String, Object>>() {};
	@Before
	public void setup() {
		mockMVC = MockMvcBuilders.webAppContextSetup(wac).build();
		Bin strBin = new Bin("str", "bin");
		Bin intBin = new Bin("int", 5);
		client.put(null, testKey, strBin, intBin);
		client.put(null, intKey, strBin, intBin);
		client.put(null, bytesKey, strBin, intBin);
	}

	@After
	public void clean() {
		client.delete(null, testKey);
		client.delete(null, intKey);
		client.delete(null, bytesKey);
	}


	@Test
	public void testGetHeaderOp() throws Exception {
		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();
		opMap.put(OPERATION_FIELD, AerospikeAPIConstants.OPERATION_GET_HEADER);
		opMap.put(OPERATION_VALUES_FIELD, opValues);

		opList.add(opMap);
		String jsString = objectMapper.writeValueAsString(opList);
		String jsonResult = ASTestUtils.performOperationAndReturn(mockMVC, testEndpoint, jsString);

		Map<String, Object>rcRecord = objectMapper.readValue(jsonResult, binType);
		Assert.assertNull(rcRecord.get(AerospikeAPIConstants.RECORD_BINS));

		Record realRecord = client.getHeader(null, testKey);

		int generation = (int) rcRecord.get(AerospikeAPIConstants.GENERATION);
		Assert.assertEquals(generation, realRecord.generation);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetOp() throws Exception {
		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();
		opMap.put(OPERATION_FIELD, AerospikeAPIConstants.OPERATION_GET);
		opMap.put(OPERATION_VALUES_FIELD, opValues);

		opList.add(opMap);
		String jsString = objectMapper.writeValueAsString(opList);
		String jsonResult = ASTestUtils.performOperationAndReturn(mockMVC, testEndpoint, jsString);

		Map<String, Object>binsObject = objectMapper.readValue(jsonResult, binType);
		Map<String, Object>realBins = client.get(null, testKey).bins;

		Assert.assertTrue(ASTestUtils.compareMapStringObj((Map<String, Object>) binsObject.get("bins"), realBins));
	}

	@Test
	public void testAddOp() throws Exception {

		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();

		opValues.put("bin", "int");
		opValues.put("incr", 2);
		opMap.put(OPERATION_FIELD, AerospikeAPIConstants.OPERATION_ADD);
		opMap.put(OPERATION_VALUES_FIELD, opValues);
		opList.add(opMap);

		String payload = objectMapper.writeValueAsString(opList);
		ASTestUtils.performOperation(mockMVC, testEndpoint, payload);

		Map<String, Object> expectedBins = new HashMap<String, Object>();
		expectedBins.put("str", "bin");
		expectedBins.put("int", 7L);

		Map<String, Object>realBins = client.get(null, testKey).bins;

		Assert.assertTrue(ASTestUtils.compareMapStringObj(expectedBins, realBins));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testReadOp() throws Exception {
		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();
		opMap.put(OPERATION_FIELD, AerospikeAPIConstants.OPERATION_READ);
		opValues.put("bin", "str");
		opMap.put(OPERATION_VALUES_FIELD, opValues);

		opList.add(opMap);
		String jsString = objectMapper.writeValueAsString(opList);

		String jsonResult = ASTestUtils.performOperationAndReturn(mockMVC, testEndpoint, jsString);
		Map<String, Object>binsObject = objectMapper.readValue(jsonResult, binType);
		/* Only read the str bin on the get*/
		Map<String, Object>realBins = client.get(null, testKey, "str").bins;

		Assert.assertTrue(ASTestUtils.compareMapStringObj((Map<String, Object>) binsObject.get("bins"), realBins));
	}

	@Test
	public void testPutOp() throws Exception {

		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();

		opValues.put("bin", "new");
		opValues.put("value", "put");
		opMap.put(OPERATION_FIELD, AerospikeAPIConstants.OPERATION_PUT);
		opMap.put(OPERATION_VALUES_FIELD, opValues);
		opList.add(opMap);

		String payload = objectMapper.writeValueAsString(opList);

		ASTestUtils.performOperation(mockMVC, testEndpoint, payload);

		Map<String, Object> expectedBins = new HashMap<String, Object>();
		expectedBins.put("str", "bin");
		expectedBins.put("int", 5L);
		expectedBins.put("new", "put");

		Map<String, Object>realBins = client.get(null, testKey).bins;


		Assert.assertTrue(ASTestUtils.compareMapStringObj(expectedBins, realBins));
	}

	@Test
	public void testAppendOp() throws Exception {
		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();
		opMap.put(OPERATION_FIELD, AerospikeAPIConstants.OPERATION_APPEND);
		opValues.put("value", "ary");
		opValues.put("bin", "str");
		opMap.put(OPERATION_VALUES_FIELD, opValues);

		opList.add(opMap);
		String jsString = objectMapper.writeValueAsString(opList);

		ASTestUtils.performOperation(mockMVC, testEndpoint, jsString);

		/* Only read the str bin on the get*/
		Map<String, Object> expectedBins = new HashMap<String, Object>();
		expectedBins.put("str", "binary");
		Map<String, Object>realBins = client.get(null, testKey, "str").bins;

		Assert.assertTrue(ASTestUtils.compareMapStringObj(expectedBins, realBins));
	}

	@Test
	public void testPrependOp() throws Exception {
		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();

		opMap.put(OPERATION_FIELD, AerospikeAPIConstants.OPERATION_PREPEND);
		opValues.put("value", "ro");
		opValues.put("bin", "str");
		opMap.put(OPERATION_VALUES_FIELD, opValues);
		opList.add(opMap);

		String jsString = objectMapper.writeValueAsString(opList);

		ASTestUtils.performOperation(mockMVC, testEndpoint, jsString);

		/* Only read the str bin on the get*/
		Map<String, Object> expectedBins = new HashMap<String, Object>();
		expectedBins.put("str", "robin");
		Map<String, Object>realBins = client.get(null, testKey, "str").bins;

		Assert.assertTrue(ASTestUtils.compareMapStringObj(expectedBins, realBins));
	}

	@Test
	public void testTouchOp() throws Exception {
		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();

		Record record = client.get(null, testKey);
		int oldGeneration = record.generation;

		opMap.put(OPERATION_FIELD, AerospikeAPIConstants.OPERATION_TOUCH);
		opMap.put(OPERATION_VALUES_FIELD, opValues);
		opList.add(opMap);

		String jsString = objectMapper.writeValueAsString(opList);

		ASTestUtils.performOperation(mockMVC, testEndpoint, jsString);

		record = client.get(null, testKey);
		Assert.assertEquals(oldGeneration + 1, record.generation);
	}

	@Test
	public void testTouchOpWithIntegerKey() throws Exception {
		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();
		String intEndpoint = ASTestUtils.buildEndpoint("operate", "test", "junit", "1") + "?keytype=INTEGER";
		Record record = client.get(null, intKey);
		int oldGeneration = record.generation;

		opMap.put(OPERATION_FIELD, AerospikeAPIConstants.OPERATION_TOUCH);
		opMap.put(OPERATION_VALUES_FIELD, opValues);
		opList.add(opMap);

		String jsString = objectMapper.writeValueAsString(opList);

		ASTestUtils.performOperation(mockMVC, intEndpoint, jsString);

		record = client.get(null, intKey);
		Assert.assertEquals(oldGeneration + 1, record.generation);
	}

	@Test
	public void testTouchOpWithBytesKey() throws Exception {
		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();

		String urlBytes = Base64.getUrlEncoder().encodeToString((byte[])((BytesValue)bytesKey.userKey).getObject());
		String bytesEndpoint = ASTestUtils.buildEndpoint("operate", "test", "junit", urlBytes) + "?keytype=BYTES";

		Record record = client.get(null, bytesKey);
		int oldGeneration = record.generation;

		opMap.put(OPERATION_FIELD, AerospikeAPIConstants.OPERATION_TOUCH);
		opMap.put(OPERATION_VALUES_FIELD, opValues);
		opList.add(opMap);

		String jsString = objectMapper.writeValueAsString(opList);

		ASTestUtils.performOperation(mockMVC, bytesEndpoint, jsString);

		record = client.get(null, bytesKey);
		Assert.assertEquals(oldGeneration + 1, record.generation);
	}

	/*
	 * Touch a record by providing it's digest
	 */
	@Test
	public void testTouchOpWithDigestKey() throws Exception {
		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();

		String urlBytes = Base64.getUrlEncoder().encodeToString(testKey.digest);
		String bytesEndpoint = ASTestUtils.buildEndpoint("operate", "test", "junit", urlBytes) + "?keytype=DIGEST";

		Record record = client.get(null, testKey);
		int oldGeneration = record.generation;

		opMap.put(OPERATION_FIELD, AerospikeAPIConstants.OPERATION_TOUCH);
		opMap.put(OPERATION_VALUES_FIELD, opValues);
		opList.add(opMap);

		String jsString = objectMapper.writeValueAsString(opList);
		ASTestUtils.performOperation(mockMVC, bytesEndpoint, jsString);

		record = client.get(null, testKey);
		Assert.assertEquals(oldGeneration + 1, record.generation);
	}
}
