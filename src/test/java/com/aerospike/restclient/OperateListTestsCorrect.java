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

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.client.cdt.ListOrder;
import com.aerospike.client.cdt.ListWriteFlags;
import com.aerospike.restclient.util.AerospikeOperation;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

import static com.aerospike.restclient.util.AerospikeAPIConstants.OPERATION_FIELD;
import static com.aerospike.restclient.util.AerospikeAPIConstants.OPERATION_VALUES_FIELD;

@RunWith(Parameterized.class)
@SpringBootTest
public class OperateListTestsCorrect {

	@ClassRule
	public static final SpringClassRule springClassRule = new SpringClassRule();

	@Rule
	public final SpringMethodRule springMethodRule = new SpringMethodRule();


	private MockMvc mockMVC;

	@Autowired
	private AerospikeClient client;

	@Autowired
	private WebApplicationContext wac;

	List<Object> objectList;
	private Key testKey = new Key("test", "junit", "listop");
	private String testEndpoint = ASTestUtils.buildEndpoint("operate", "test", "junit", "listop");

	@Before
	public void setup() {
		mockMVC = MockMvcBuilders.webAppContextSetup(wac).build();
		Bin strBin = new Bin("str", "bin");
		objectList = new ArrayList<Object>();
		objectList.add(1L);
		objectList.add(2L);
		objectList.add(0L);
		objectList.add(3L);
		objectList.add(4L);

		Bin listBin = new Bin("list", objectList);
		client.put(null, testKey, strBin, listBin);
	}

	@After
	public void clean() {
		client.delete(null, testKey);
	}

	private OperationPerformer opPerformer = null;

	@Parameters
	public static Object[][] getParams() {
		return new Object[][] {
			{new JSONOperationPerformer(), true}, {new MsgPackOperationPerformer(), true},
			{new JSONOperationPerformer(), false}, {new MsgPackOperationPerformer(), false}
		};
	}

	/* Set up the correct msgpack/json performer for this set of runs. Also decided whether to use the endpoint with a set or without */
	public OperateListTestsCorrect(OperationPerformer performer, boolean useSet) {
		this.opPerformer = performer;
		if (useSet) {
			testKey = new Key("test", "junit", "listop");
			testEndpoint = ASTestUtils.buildEndpoint("operate", "test", "junit", "listop");
		} else {
			testKey = new Key("test", null, "listop");
			testEndpoint = ASTestUtils.buildEndpoint("operate", "test", "listop");
		}
	}

	@Test
	public void testListAppend() throws Exception {
		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();
		opValues.put("bin", "list");
		opValues.put("value", "aerospike");
		opMap.put(OPERATION_FIELD, AerospikeOperation.LIST_APPEND);
		opMap.put(OPERATION_VALUES_FIELD, opValues);
		opList.add(opMap);

		opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opList);

		@SuppressWarnings("unchecked")
		List<Object> realList = (List<Object>) client.get(null, testKey).bins.get("list");
		objectList.add("aerospike");

		Assert.assertTrue(ASTestUtils.compareCollection(objectList, realList));
	}

	@Test
	public void testListAppendItems() throws Exception {
		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();
		List<Object>appendValues = Arrays.asList("aero", "spike", "aero");

		opValues.put("bin", "list");
		opValues.put("values", appendValues);
		opMap.put(OPERATION_FIELD, AerospikeOperation.LIST_APPEND_ITEMS);
		opMap.put(OPERATION_VALUES_FIELD, opValues);
		opList.add(opMap);

		opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opList);

		@SuppressWarnings("unchecked")
		List<Object> realList = (List<Object>) client.get(null, testKey).bins.get("list");
		objectList.add("aero");
		objectList.add("spike");
		objectList.add("aero");

		Assert.assertTrue(ASTestUtils.compareCollection(objectList, realList));
	}

	@Test
	public void testListAppendItemsWithPolicy() throws Exception {
		Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();
		Map<String,Object>listPolicyMap = buildListPolicyMap(ListOrder.UNORDERED, ListWriteFlags.ADD_UNIQUE);
		List<Object>appendValues = Arrays.asList("aero", "spike");

		opValues.put("values", appendValues);
		opValues.put("listPolicy", listPolicyMap);
		opValues.put("bin", "list");

		opMap.put(OPERATION_FIELD, AerospikeOperation.LIST_APPEND_ITEMS);
		opMap.put(OPERATION_VALUES_FIELD, opValues);
		opList.add(opMap);

		opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opList);

		@SuppressWarnings("unchecked")
		List<Object> realList = (List<Object>) client.get(null, testKey).bins.get("list");
		objectList.add("aero");
		objectList.add("spike");

		Assert.assertTrue(ASTestUtils.compareCollection(objectList, realList));
	}

	@Test
	public void testListClear() throws Exception {
		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();

		opValues.put("bin", "list");
		opMap.put(OPERATION_FIELD, AerospikeOperation.LIST_CLEAR);
		opMap.put(OPERATION_VALUES_FIELD, opValues);

		opList.add(opMap);

		opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opList);

		@SuppressWarnings("unchecked")
		List<Object> realList = (List<Object>) client.get(null, testKey).bins.get("list");

		Assert.assertEquals(realList.size(), 0);
	}

	@Test
	public void testListGet() throws Exception {
		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();

		opValues.put("bin", "list");
		opValues.put("index", 2);
		opMap.put(OPERATION_FIELD, AerospikeOperation.LIST_GET);
		opMap.put(OPERATION_VALUES_FIELD, opValues);

		opList.add(opMap);

		Map<String, Object>binsObject = getReturnedBins(opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opList));

		Assert.assertTrue(ASTestUtils.compareSimpleValues(binsObject.get("list"), objectList.get(2)));
	}

	@Test
	public void testListGetByIndexIndex() throws Exception {
		Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));
		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();

		opValues.put("bin", "list");
		opValues.put("index", 2);
		opValues.put("listReturnType", "INDEX");
		opMap.put(OPERATION_FIELD, AerospikeOperation.LIST_GET_BY_INDEX);
		opMap.put(OPERATION_VALUES_FIELD, opValues);
		opList.add(opMap);

		Map<String, Object>binsObject = getReturnedBins(opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opList));

		Assert.assertTrue(ASTestUtils.compareSimpleValues(binsObject.get("list"), 2));
	}

	@Test
	public void testListGetByIndexReverseRank() throws Exception {
		Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();

		opValues.put("bin", "list");
		opValues.put("index", 2);
		opValues.put("listReturnType", "REVERSE_RANK");
		opMap.put(OPERATION_FIELD, AerospikeOperation.LIST_GET_BY_INDEX);
		opMap.put(OPERATION_VALUES_FIELD, opValues);
		opList.add(opMap);

		Map<String, Object>binsObject = getReturnedBins(opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opList));

		Assert.assertTrue(ASTestUtils.compareSimpleValues(binsObject.get("list"), 4));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testListGetByIndexRange() throws Exception {
		Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));
		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();

		opValues.put("bin", "list");
		opValues.put("index", 1);
		opValues.put("count", 3);
		opValues.put("listReturnType", "VALUE");
		opMap.put(OPERATION_FIELD, AerospikeOperation.LIST_GET_BY_INDEX_RANGE);
		opMap.put(OPERATION_VALUES_FIELD, opValues);

		opList.add(opMap);

		Map<String, Object>binsObject = getReturnedBins(opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opList));

		List<Object> retItems = (List<Object>) binsObject.get("list");
		Assert.assertTrue(ASTestUtils.compareCollection(retItems, Arrays.asList(2, 0, 3)));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testListGetByIndexRangeNoCount() throws Exception {
		Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));
		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();

		opValues.put("bin", "list");
		opValues.put("index", 1);
		opValues.put("listReturnType", "VALUE");
		opMap.put(OPERATION_FIELD, AerospikeOperation.LIST_GET_BY_INDEX_RANGE);
		opMap.put(OPERATION_VALUES_FIELD, opValues);

		opList.add(opMap);

		Map<String, Object>binsObject = getReturnedBins(opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opList));

		List<Object> retItems = (List<Object>) binsObject.get("list");
		Assert.assertTrue(ASTestUtils.compareCollection(retItems, Arrays.asList(2, 0, 3, 4)));
	}

	@Test
	public void testListGetByRankValue() throws Exception {
		Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();

		opValues.put("bin", "list");
		opValues.put("rank", 2);
		opValues.put("listReturnType", "VALUE");
		opMap.put(OPERATION_FIELD, AerospikeOperation.LIST_GET_BY_RANK);
		opMap.put(OPERATION_VALUES_FIELD, opValues);

		opList.add(opMap);

		Map<String, Object>binsObject = getReturnedBins(opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opList));

		Assert.assertTrue(ASTestUtils.compareSimpleValues(binsObject.get("list"), 2));
	}

	@Test
	public void testListGetByRankIndex() throws Exception {
		Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();

		opValues.put("bin", "list");
		opValues.put("rank", 2);
		opValues.put("listReturnType", "INDEX");
		opMap.put(OPERATION_FIELD, AerospikeOperation.LIST_GET_BY_RANK);
		opMap.put(OPERATION_VALUES_FIELD, opValues);

		opList.add(opMap);

		Map<String, Object>binsObject = getReturnedBins(opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opList));

		Assert.assertTrue(ASTestUtils.compareSimpleValues(binsObject.get("list"), 1));
	}

	@Test
	public void testListGetByRankRange() throws Exception {
		Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();

		opValues.put("bin", "list");
		opValues.put("rank", 1);
		opValues.put("count", 3);
		opValues.put("listReturnType", "VALUE");
		opMap.put(OPERATION_FIELD, AerospikeOperation.LIST_GET_BY_RANK_RANGE);
		opMap.put(OPERATION_VALUES_FIELD, opValues);

		opList.add(opMap);

		Map<String, Object>binsObject = getReturnedBins(opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opList));

		@SuppressWarnings("unchecked")
		List<Object> retItems = (List<Object>) binsObject.get("list");

		Set<Object> retItemSet = new HashSet<Object>(retItems);
		Set<Object>expectedSet = new HashSet<Object>(Arrays.asList(1, 2, 3));
		Assert.assertTrue(retItemSet.equals(expectedSet));
	}

	@Test
	public void testListGetByRankRangeNoCount() throws Exception {
		Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();

		opValues.put("bin", "list");
		opValues.put("rank", 1);
		opValues.put("listReturnType", "VALUE");
		opMap.put(OPERATION_FIELD, AerospikeOperation.LIST_GET_BY_RANK_RANGE);
		opMap.put(OPERATION_VALUES_FIELD, opValues);

		opList.add(opMap);

		Map<String, Object>binsObject = getReturnedBins(opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opList));

		@SuppressWarnings("unchecked")
		List<Object> retItems = (List<Object>) binsObject.get("list");

		Set<Object> retItemSet = new HashSet<Object>(retItems);
		Set<Object>expectedSet = new HashSet<Object>(Arrays.asList(1, 2, 3, 4));
		Assert.assertTrue(retItemSet.equals(expectedSet));
	}

	@Test
	public void testListGetByValueIndex() throws Exception {
		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();

		opValues.put("bin", "list");
		opValues.put("value", 0);
		opValues.put("listReturnType", "INDEX");
		opMap.put(OPERATION_FIELD, AerospikeOperation.LIST_GET_BY_VALUE);
		opMap.put(OPERATION_VALUES_FIELD, opValues);

		opList.add(opMap);

		Map<String, Object>binsObject = getReturnedBins(opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opList));

		Assert.assertTrue(ASTestUtils.compareCollection((List<?>) binsObject.get("list"), Arrays.asList(2)));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testListGetByValueRange() throws Exception {
		Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();

		opValues.put("bin", "list");
		opValues.put("valueBegin", 1);
		opValues.put("valueEnd", 4);
		opValues.put("listReturnType", "VALUE");
		opMap.put(OPERATION_FIELD, AerospikeOperation.LIST_GET_BY_VALUE_RANGE);
		opMap.put(OPERATION_VALUES_FIELD, opValues);

		opList.add(opMap);

		Map<String, Object>binsObject = getReturnedBins(opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opList));

		List<Object> retItems = (List<Object>) binsObject.get("list");

		Set<Object> retItemSet = new HashSet<Object>(retItems);
		Set<Object>expectedSet = new HashSet<Object>(Arrays.asList(1, 2, 3));
		Assert.assertTrue(retItemSet.equals(expectedSet));
	}

	@Test
	public void testListGetByValueRangeNoEnd() throws Exception {
		Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();

		opValues.put("bin", "list");
		opValues.put("valueBegin", 1);
		opValues.put("listReturnType", "VALUE");
		opMap.put(OPERATION_FIELD, AerospikeOperation.LIST_GET_BY_VALUE_RANGE);
		opMap.put(OPERATION_VALUES_FIELD, opValues);

		opList.add(opMap);

		Map<String, Object>binsObject = getReturnedBins(opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opList));

		@SuppressWarnings("unchecked")
		List<Object> retItems = (List<Object>) binsObject.get("list");

		Set<Object> retItemSet = new HashSet<Object>(retItems);
		Set<Object>expectedSet = new HashSet<Object>(Arrays.asList(1, 2, 3, 4));
		Assert.assertTrue(retItemSet.equals(expectedSet));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testListGetByValueRangeNoBegin() throws Exception {
		Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();

		opValues.put("bin", "list");
		opValues.put("valueEnd", 4);
		opValues.put("listReturnType", "VALUE");
		opMap.put(OPERATION_FIELD, AerospikeOperation.LIST_GET_BY_VALUE_RANGE);
		opMap.put(OPERATION_VALUES_FIELD, opValues);

		opList.add(opMap);

		Map<String, Object>binsObject = getReturnedBins(opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opList));

		List<Object> retItems = (List<Object>) binsObject.get("list");

		Set<Object> retItemSet = new HashSet<Object>(retItems);
		Set<Object>expectedSet = new HashSet<Object>(Arrays.asList(0, 1, 2, 3));
		Assert.assertTrue(retItemSet.equals(expectedSet));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testListGetByValueList() throws Exception {
		Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();

		opValues.put("bin", "list");
		opValues.put("values", Arrays.asList(0, 1, 4));
		opValues.put("listReturnType", "INDEX");
		opMap.put(OPERATION_FIELD, AerospikeOperation.LIST_GET_BY_VALUE_LIST);
		opMap.put(OPERATION_VALUES_FIELD, opValues);

		opList.add(opMap);

		Map<String, Object>binsObject = getReturnedBins(opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opList));

		List<Object> retItems = (List<Object>) binsObject.get("list");

		Set<Object> retItemSet = new HashSet<Object>(retItems);
		Set<Object>expectedSet = new HashSet<Object>(Arrays.asList(2, 0, 4));
		Assert.assertTrue(retItemSet.equals(expectedSet));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testListGetRange() throws Exception {
		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();

		opValues.put("bin", "list");
		opValues.put("index", 1);
		opValues.put("count", 3);
		opMap.put(OPERATION_FIELD, AerospikeOperation.LIST_GET_RANGE);
		opMap.put(OPERATION_VALUES_FIELD, opValues);

		opList.add(opMap);

		Map<String, Object>binsObject = getReturnedBins(opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opList));

		List<Object> retItems = (List<Object>) binsObject.get("list");

		Assert.assertTrue(ASTestUtils.compareCollection(retItems, Arrays.asList(2, 0, 3)));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testListGetRangeNoCount() throws Exception {
		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();

		opValues.put("bin", "list");
		opValues.put("index", 1);
		opMap.put(OPERATION_FIELD, AerospikeOperation.LIST_GET_RANGE);
		opMap.put(OPERATION_VALUES_FIELD, opValues);

		opList.add(opMap);

		Map<String, Object>binsObject = getReturnedBins(opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opList));

		List<Object> retItems = (List<Object>) binsObject.get("list");

		Assert.assertTrue(ASTestUtils.compareCollection(retItems, Arrays.asList(2, 0, 3, 4)));
	}

	@Test
	public void testListIncrement() throws Exception {
		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();

		opValues.put("bin", "list");
		opValues.put("index", 1);
		opValues.put("incr", 10);
		opMap.put(OPERATION_FIELD, AerospikeOperation.LIST_INCREMENT);
		opMap.put(OPERATION_VALUES_FIELD, opValues);

		opList.add(opMap);

		opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opList);

		@SuppressWarnings("unchecked")
		List<Object> retItems = (List<Object>) client.get(null, testKey).bins.get("list");

		Assert.assertTrue(ASTestUtils.compareSimpleValues(retItems.get(1), 12));
	}

	@Test
	public void testListIncrementNoValue() throws Exception {
		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();
		opValues.put("bin", "list");
		opValues.put("index", 1);
		opMap.put(OPERATION_FIELD, AerospikeOperation.LIST_INCREMENT);
		opMap.put(OPERATION_VALUES_FIELD, opValues);

		opList.add(opMap);

		opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opList);

		@SuppressWarnings("unchecked")
		List<Object> retItems = (List<Object>) client.get(null, testKey).bins.get("list");

		Assert.assertTrue(ASTestUtils.compareSimpleValues(retItems.get(1), 3));
	}

	@Test
	public void testListIncrementWithPolicy() throws Exception {
		Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();
		Map<String,Object>listPolicyMap = buildListPolicyMap(ListOrder.UNORDERED, ListWriteFlags.DEFAULT);

		opValues.put("bin", "list");
		opValues.put("index", 1);
		opValues.put("incr", 10);
		opValues.put("listPolicy", listPolicyMap);

		opMap.put(OPERATION_FIELD, AerospikeOperation.LIST_INCREMENT);
		opMap.put(OPERATION_VALUES_FIELD, opValues);
		opList.add(opMap);

		opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opList);

		@SuppressWarnings("unchecked")
		List<Object> retItems = (List<Object>) client.get(null, testKey).bins.get("list");

		Assert.assertTrue(ASTestUtils.compareSimpleValues(retItems.get(1), 12));
	}

	@Test
	public void testListInsert() throws Exception {
		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();

		opValues.put("bin", "list");
		opValues.put("index", 1);
		opValues.put("value", "one");

		opMap.put(OPERATION_FIELD, AerospikeOperation.LIST_INSERT);
		opMap.put(OPERATION_VALUES_FIELD, opValues);
		opList.add(opMap);

		opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opList);

		@SuppressWarnings("unchecked")
		List<Object> retItems = (List<Object>) client.get(null, testKey).bins.get("list");
		objectList.add(1, "one");

		Assert.assertTrue(ASTestUtils.compareCollection(retItems, objectList));
	}

	@Test
	public void testListInsertPolicy() throws Exception {
		Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));
		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();
		Map<String,Object>listPolicyMap = buildListPolicyMap(ListOrder.UNORDERED, ListWriteFlags.DEFAULT);

		opValues.put("bin", "list");
		opValues.put("index", 1);
		opValues.put("value", 5);
		opValues.put("listPolicy", listPolicyMap);
		opMap.put(OPERATION_FIELD, AerospikeOperation.LIST_INSERT);
		opMap.put(OPERATION_VALUES_FIELD, opValues);
		opList.add(opMap);

		opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opList);

		@SuppressWarnings("unchecked")
		List<Object> retItems = (List<Object>) client.get(null, testKey).bins.get("list");

		Assert.assertTrue(ASTestUtils.compareCollection(retItems, Arrays.asList(1, 5, 2, 0, 3, 4)));
	}

	@Test
	public void testListInsertItems() throws Exception {
		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();

		opValues.put("bin", "list");
		opValues.put("index", 1);
		opValues.put("values", Arrays.asList("one", "two", "three"));

		opMap.put(OPERATION_FIELD, AerospikeOperation.LIST_INSERT_ITEMS);
		opMap.put(OPERATION_VALUES_FIELD, opValues);
		opList.add(opMap);

		opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opList);

		@SuppressWarnings("unchecked")
		List<Object> retItems = (List<Object>) client.get(null, testKey).bins.get("list");
		objectList.add(1, "three");
		objectList.add(1, "two");
		objectList.add(1, "one");

		Assert.assertTrue(ASTestUtils.compareCollection(retItems, objectList));
	}

	@Test
	public void testListInsertItemsPolicy() throws Exception {
		Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();
		Map<String,Object>listPolicyMap = buildListPolicyMap(ListOrder.UNORDERED, ListWriteFlags.DEFAULT);

		opValues.put("bin", "list");
		opValues.put("index", 1);
		opValues.put("values", Arrays.asList("one", "two", "three"));
		opValues.put("listPolicy", listPolicyMap);
		opMap.put(OPERATION_FIELD, AerospikeOperation.LIST_INSERT_ITEMS);
		opMap.put(OPERATION_VALUES_FIELD, opValues);
		opList.add(opMap);

		opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opList);

		@SuppressWarnings("unchecked")
		List<Object> retItems = (List<Object>) client.get(null, testKey).bins.get("list");
		objectList.add(1, "three");
		objectList.add(1, "two");
		objectList.add(1, "one");

		Assert.assertTrue(ASTestUtils.compareCollection(retItems, objectList));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testListPop() throws Exception {
		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();

		opValues.put("bin", "list");
		opValues.put("index", 1);
		opMap.put(OPERATION_FIELD, AerospikeOperation.LIST_POP);
		opMap.put(OPERATION_VALUES_FIELD, opValues);
		opList.add(opMap);

		Map<String, Object>returnedBins = getReturnedBins(opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opList));

		Assert.assertTrue(ASTestUtils.compareSimpleValues(returnedBins.get("list"), 2));

		List<Object> retItems = (List<Object>) client.get(null, testKey).bins.get("list");

		objectList.remove(1);
		Assert.assertTrue(ASTestUtils.compareCollection(retItems, objectList));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testListPopRange() throws Exception {
		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();

		opValues.put("bin", "list");
		opValues.put("index", 1);
		opValues.put("count", 3);
		opMap.put(OPERATION_FIELD, AerospikeOperation.LIST_POP_RANGE);
		opMap.put(OPERATION_VALUES_FIELD, opValues);
		opList.add(opMap);

		Map<String, Object>returnedBins = getReturnedBins(opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opList));

		Assert.assertTrue(ASTestUtils.compareSimpleValues(returnedBins.get("list"), Arrays.asList(2, 0, 3)));

		List<Object> retItems = (List<Object>) client.get(null, testKey).bins.get("list");
		// Remove 3 items starting from index 1
		objectList.remove(1);
		objectList.remove(1);
		objectList.remove(1);

		Assert.assertTrue(ASTestUtils.compareCollection(retItems, objectList));
	}

	@Test
	public void testListPopRangeNoCount() throws Exception {
		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();

		opValues.put("bin", "list");
		opValues.put("index", 1);
		opMap.put(OPERATION_FIELD, AerospikeOperation.LIST_POP_RANGE);
		opMap.put(OPERATION_VALUES_FIELD, opValues);
		opList.add(opMap);

		Map<String, Object>returnedBins = getReturnedBins(opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opList));

		Assert.assertTrue(ASTestUtils.compareSimpleValues(returnedBins.get("list"), Arrays.asList(2, 0, 3, 4)));

		@SuppressWarnings("unchecked")
		List<Object> retItems = (List<Object>) client.get(null, testKey).bins.get("list");

		// Remove 4 items starting from index 1
		objectList.remove(1);
		objectList.remove(1);
		objectList.remove(1);
		objectList.remove(1);

		Assert.assertTrue(ASTestUtils.compareCollection(retItems, objectList));
	}

	@Test
	public void testListRemove() throws Exception {
		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();

		opValues.put("bin", "list");
		opValues.put("index", 2);
		opMap.put(OPERATION_FIELD, AerospikeOperation.LIST_REMOVE);
		opMap.put(OPERATION_VALUES_FIELD, opValues);
		opList.add(opMap);

		opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opList);

		@SuppressWarnings("unchecked")
		List<Object> retItems = (List<Object>) client.get(null, testKey).bins.get("list");

		objectList.remove(2);
		Assert.assertTrue(ASTestUtils.compareCollection(retItems, objectList));
	}

	@Test
	public void testListRemoveByIndex() throws Exception {
		Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();

		opValues.put("bin", "list");
		opValues.put("index", 2);
		opValues.put("listReturnType", "RANK");
		opMap.put(OPERATION_FIELD, AerospikeOperation.LIST_REMOVE_BY_INDEX);
		opMap.put(OPERATION_VALUES_FIELD, opValues);
		opList.add(opMap);

		Map<String, Object>returnedBins = getReturnedBins(opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opList));


		//The popped value was the smallest element, so it's rank should be 0
		Assert.assertTrue(ASTestUtils.compareSimpleValues(returnedBins.get("list"), 0));

		@SuppressWarnings("unchecked")
		List<Object> retItems = (List<Object>) client.get(null, testKey).bins.get("list");

		objectList.remove(2);
		Assert.assertTrue(ASTestUtils.compareCollection(retItems, objectList));
	}

	@Test
	public void testListRemoveByIndexRange() throws Exception {
		Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();

		opValues.put("bin", "list");
		opValues.put("index", 1);
		opValues.put("count", 3);
		opValues.put("listReturnType", "COUNT");
		opMap.put(OPERATION_FIELD, AerospikeOperation.LIST_REMOVE_BY_INDEX_RANGE);
		opMap.put(OPERATION_VALUES_FIELD, opValues);
		opList.add(opMap);

		Map<String, Object>returnedBins = getReturnedBins(opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opList));

		//Three items were removed, so the server should return 3 items
		Assert.assertTrue(ASTestUtils.compareSimpleValues(returnedBins.get("list"), 3));


		@SuppressWarnings("unchecked")
		List<Object> retItems = (List<Object>) client.get(null, testKey).bins.get("list");

		objectList.remove(1);
		objectList.remove(1);
		objectList.remove(1);
		Assert.assertTrue(ASTestUtils.compareCollection(retItems, objectList));
	}

	@Test
	public void testListRemoveByIndexRangeNoCount() throws Exception {
		Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();

		opValues.put("bin", "list");
		opValues.put("index", 1);
		opValues.put("listReturnType", "COUNT");
		opMap.put(OPERATION_FIELD, AerospikeOperation.LIST_REMOVE_BY_INDEX_RANGE);
		opMap.put(OPERATION_VALUES_FIELD, opValues);
		opList.add(opMap);

		Map<String, Object>returnedBins = getReturnedBins(opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opList));

		//Three items were removed, so the server should return 3 items
		Assert.assertTrue(ASTestUtils.compareSimpleValues(returnedBins.get("list"), 4));


		@SuppressWarnings("unchecked")
		List<Object> retItems = (List<Object>) client.get(null, testKey).bins.get("list");

		objectList.remove(1);
		objectList.remove(1);
		objectList.remove(1);
		objectList.remove(1);
		Assert.assertTrue(ASTestUtils.compareCollection(retItems, objectList));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testListRemoveByRank() throws Exception {
		Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();

		opValues.put("bin", "list");
		opValues.put("rank", 0);
		opValues.put("listReturnType", "INDEX");
		opMap.put(OPERATION_FIELD, AerospikeOperation.LIST_REMOVE_BY_RANK);
		opMap.put(OPERATION_VALUES_FIELD, opValues);
		opList.add(opMap);

		Map<String, Object>returnedBins = getReturnedBins(opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opList));

		//The popped value was the smallest element, it was at index 2
		Assert.assertTrue(ASTestUtils.compareSimpleValues(returnedBins.get("list"), 2));

		List<Object> retItems = (List<Object>) client.get(null, testKey).bins.get("list");
		objectList.remove(2);
		Assert.assertTrue(ASTestUtils.compareCollection(retItems, objectList));
	}

	@Test
	public void testListRemoveByRankRange() throws Exception {
		Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();

		opValues.put("bin", "list");
		opValues.put("rank", 1);
		opValues.put("count", 3);
		opValues.put("listReturnType", "VALUE");
		opMap.put(OPERATION_FIELD, AerospikeOperation.LIST_REMOVE_BY_RANK_RANGE);
		opMap.put(OPERATION_VALUES_FIELD, opValues);
		opList.add(opMap);

		opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opList);

		@SuppressWarnings("unchecked")
		List<Object> retItems = (List<Object>) client.get(null, testKey).bins.get("list");
		Assert.assertTrue(ASTestUtils.compareCollection(retItems, Arrays.asList(0,4)));
	}

	@Test
	public void testListRemoveByRankRangeNoCount() throws Exception {
		Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();

		opValues.put("bin", "list");
		opValues.put("rank", 1);
		opValues.put("listReturnType", "VALUE");
		opMap.put(OPERATION_FIELD, AerospikeOperation.LIST_REMOVE_BY_RANK_RANGE);
		opMap.put(OPERATION_VALUES_FIELD, opValues);
		opList.add(opMap);

		opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opList);

		@SuppressWarnings("unchecked")
		List<Object> retItems = (List<Object>) client.get(null, testKey).bins.get("list");
		Assert.assertTrue(ASTestUtils.compareCollection(retItems, Arrays.asList(0)));
	}

	@Test
	public void testListRemoveByValue() throws Exception {
		Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

		objectList.add(0);
		Bin newListBin = new Bin("list", objectList);
		client.put(null, testKey, newListBin);
		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();

		opValues.put("bin", "list");
		opValues.put("value", 0);
		opValues.put("listReturnType", "INDEX");
		opMap.put(OPERATION_FIELD, AerospikeOperation.LIST_REMOVE_BY_VALUE);
		opMap.put(OPERATION_VALUES_FIELD, opValues);
		opList.add(opMap);

		Map<String, Object>returnedBins = getReturnedBins(opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opList));

		//The popped value was the smallest element, it was at index 2
		Assert.assertTrue(ASTestUtils.compareCollection((List<?>) returnedBins.get("list"), Arrays.asList(2, 5)));

		@SuppressWarnings("unchecked")
		List<Object> retItems = (List<Object>) client.get(null, testKey).bins.get("list");
		objectList.remove(5);
		objectList.remove(2);
		Assert.assertTrue(ASTestUtils.compareCollection(retItems, objectList));
	}

	@Test
	public void testListRemoveByValueRange() throws Exception {
		Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();

		opValues.put("bin", "list");
		opValues.put("valueBegin", 1);
		opValues.put("valueEnd", 3);
		opValues.put("listReturnType", "COUNT");
		opMap.put(OPERATION_FIELD, AerospikeOperation.LIST_REMOVE_BY_VALUE_RANGE);
		opMap.put(OPERATION_VALUES_FIELD, opValues);
		opList.add(opMap);

		Map<String, Object>returnedBins = getReturnedBins(opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opList));

		//The popped value was the smallest element, it was at index 2
		Assert.assertTrue(ASTestUtils.compareSimpleValues(returnedBins.get("list"), 2));

		@SuppressWarnings("unchecked")
		List<Object> retItems = (List<Object>) client.get(null, testKey).bins.get("list");
		Assert.assertTrue(ASTestUtils.compareCollection(retItems, Arrays.asList(0, 3, 4)));
	}

	@Test
	public void testListRemoveByValueRangeNoBegin() throws Exception {
		Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();

		opValues.put("bin", "list");
		opValues.put("valueEnd", 3);
		opValues.put("listReturnType", "COUNT");
		opMap.put(OPERATION_FIELD, AerospikeOperation.LIST_REMOVE_BY_VALUE_RANGE);
		opMap.put(OPERATION_VALUES_FIELD, opValues);
		opList.add(opMap);

		Map<String, Object>returnedBins = getReturnedBins(opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opList));

		//The popped value was the smallest element, it was at index 2
		Assert.assertTrue(ASTestUtils.compareSimpleValues(returnedBins.get("list"), 3));

		@SuppressWarnings("unchecked")
		List<Object> retItems = (List<Object>) client.get(null, testKey).bins.get("list");
		Assert.assertTrue(ASTestUtils.compareCollection(retItems, Arrays.asList(3, 4)));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testListRemoveByValueRangeNoEnd() throws Exception {
		Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();

		opValues.put("bin", "list");
		opValues.put("valueBegin", 1);
		opValues.put("listReturnType", "COUNT");
		opMap.put(OPERATION_FIELD, AerospikeOperation.LIST_REMOVE_BY_VALUE_RANGE);
		opMap.put(OPERATION_VALUES_FIELD, opValues);
		opList.add(opMap);

		Map<String, Object>returnedBins = getReturnedBins(opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opList));

		//The popped value was the smallest element, it was at index 2
		Assert.assertTrue(ASTestUtils.compareSimpleValues(returnedBins.get("list"), 4));

		List<Object> retItems = (List<Object>) client.get(null, testKey).bins.get("list");
		Assert.assertTrue(ASTestUtils.compareCollection(retItems, Arrays.asList(0)));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testListRemoveByValueList() throws Exception {
		Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

		objectList.add(0);
		Bin newListBin = new Bin("list", objectList);
		client.put(null, testKey, newListBin);
		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();

		opValues.put("bin", "list");
		opValues.put("values", Arrays.asList(0, 2, 4));
		opValues.put("listReturnType", "COUNT");
		opMap.put(OPERATION_FIELD, AerospikeOperation.LIST_REMOVE_BY_VALUE_LIST);
		opMap.put(OPERATION_VALUES_FIELD, opValues);
		opList.add(opMap);

		Map<String, Object>returnedBins = getReturnedBins(opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opList));

		//The popped value was the smallest element, it was at index 2
		Assert.assertTrue(ASTestUtils.compareSimpleValues(returnedBins.get("list"), 4));

		List<Object> retItems = (List<Object>) client.get(null, testKey).bins.get("list");

		Assert.assertTrue(ASTestUtils.compareCollection(retItems, Arrays.asList(1, 3)));
	}


	@Test
	public void testListRemoveRange() throws Exception {

		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();

		opValues.put("bin", "list");
		opValues.put("index", 1);
		opValues.put("count", 3);
		opMap.put(OPERATION_FIELD, AerospikeOperation.LIST_REMOVE_RANGE);
		opMap.put(OPERATION_VALUES_FIELD, opValues);
		opList.add(opMap);

		opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opList);

		@SuppressWarnings("unchecked")
		List<Object> retItems = (List<Object>) client.get(null, testKey).bins.get("list");

		objectList.remove(1);
		objectList.remove(1);
		objectList.remove(1);
		Assert.assertTrue(ASTestUtils.compareCollection(retItems, objectList));
	}

	@Test
	public void testListRemoveRangeNoCount() throws Exception {

		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();

		opValues.put("bin", "list");
		opValues.put("index", 1);
		opMap.put(OPERATION_FIELD, AerospikeOperation.LIST_REMOVE_RANGE);
		opMap.put(OPERATION_VALUES_FIELD, opValues);
		opList.add(opMap);

		opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opList);

		@SuppressWarnings("unchecked")
		List<Object> retItems = (List<Object>) client.get(null, testKey).bins.get("list");

		objectList.remove(1);
		objectList.remove(1);
		objectList.remove(1);
		objectList.remove(1);
		Assert.assertTrue(ASTestUtils.compareCollection(retItems, objectList));
	}

	@Test
	public void testListSetValue() throws Exception {
		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();

		opValues.put("bin", "list");
		opValues.put("value", "two");
		opValues.put("index", 1);
		opMap.put(OPERATION_FIELD, AerospikeOperation.LIST_SET);
		opMap.put(OPERATION_VALUES_FIELD, opValues);
		opList.add(opMap);

		opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opList);

		@SuppressWarnings("unchecked")
		List<Object> retItems = (List<Object>) client.get(null, testKey).bins.get("list");

		Assert.assertTrue(ASTestUtils.compareCollection(retItems, Arrays.asList(1, "two", 0, 3, 4)));
	}

	@Test
	public void testListSetValueWithPolicy() throws Exception {
		Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();
		Map<String,Object>listPolicyMap = buildListPolicyMap(ListOrder.UNORDERED, ListWriteFlags.ADD_UNIQUE);

		opValues.put("bin", "list");
		opValues.put("value", "two");
		opValues.put("index", 1);
		opValues.put("listPolicy", listPolicyMap);
		opMap.put(OPERATION_FIELD, AerospikeOperation.LIST_SET);
		opMap.put(OPERATION_VALUES_FIELD, opValues);
		opList.add(opMap);

		opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opList);

		@SuppressWarnings("unchecked")
		List<Object> retItems = (List<Object>) client.get(null, testKey).bins.get("list");

		Assert.assertTrue(ASTestUtils.compareCollection(retItems, Arrays.asList(1, "two", 0, 3, 4)));
	}

	@Test
	public void testListSetOrder() throws Exception {
		Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();

		opValues.put("bin", "list");
		opValues.put("listOrder", "ORDERED");
		opMap.put(OPERATION_FIELD, AerospikeOperation.LIST_SET_ORDER);
		opMap.put(OPERATION_VALUES_FIELD, opValues);
		opList.add(opMap);

		opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opList);

		@SuppressWarnings("unchecked")
		List<Object> retItems = (List<Object>) client.get(null, testKey).bins.get("list");

		Assert.assertTrue(ASTestUtils.compareCollection(retItems, Arrays.asList(0, 1, 2, 3, 4)));
	}

	@Test
	public void testListSize() throws Exception {
		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();

		opValues.put("bin", "list");
		opMap.put(OPERATION_FIELD, AerospikeOperation.LIST_SIZE);
		opMap.put(OPERATION_VALUES_FIELD, opValues);
		opList.add(opMap);

		Map<String, Object>retBins = getReturnedBins(opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opList));

		Assert.assertTrue(ASTestUtils.compareSimpleValues(retBins.get("list"), 5));
	}

	@Test
	public void testListSort() throws Exception {
		Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();

		opValues.put("bin", "list");
		opMap.put(OPERATION_FIELD, AerospikeOperation.LIST_SORT);
		opMap.put(OPERATION_VALUES_FIELD, opValues);
		opList.add(opMap);

		opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opList);

		@SuppressWarnings("unchecked")
		List<Object> retItems = (List<Object>) client.get(null, testKey).bins.get("list");
		Assert.assertTrue(ASTestUtils.compareCollection(retItems, Arrays.asList(0, 1, 2, 3, 4)));
	}

	@Test
	public void testListTrim() throws Exception {
		List<Map<String, Object>> opList = new ArrayList<Map<String, Object>>();
		Map<String, Object> opMap = new HashMap<String, Object>();
		Map<String, Object> opValues = new HashMap<String, Object>();

		opValues.put("bin", "list");
		opValues.put("index", 1);
		opValues.put("count", 3);
		opMap.put(OPERATION_FIELD, AerospikeOperation.LIST_TRIM);
		opMap.put(OPERATION_VALUES_FIELD, opValues);
		opList.add(opMap);

		opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opList);

		@SuppressWarnings("unchecked")
		List<Object> retItems = (List<Object>) client.get(null, testKey).bins.get("list");
		Assert.assertTrue(ASTestUtils.compareCollection(retItems, Arrays.asList(2, 0, 3)));
	}

	private Map<String,Object> buildListPolicyMap(ListOrder order, int flags) {
		Map<String, Object> policyMap = new HashMap<String, Object>();
		List<Object> flagStrings = new ArrayList<Object>();
		if (order == ListOrder.ORDERED) {
			policyMap.put("order", "ORDERED");
		} else if (order == ListOrder.UNORDERED) {
			policyMap.put("order", "UNORDERED");
		}
		if ((flags & ListWriteFlags.ADD_UNIQUE) != 0) {
			flagStrings.add("ADD_UNIQUE");
		}
		if ((flags & ListWriteFlags.INSERT_BOUNDED) != 0) {
			flagStrings.add("INSERT_BOUNDED");
		}
		policyMap.put("writeFlags", flagStrings);
		return policyMap;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object>getReturnedBins(Map<String, Object> rec) throws Exception {
		return (Map<String, Object>) rec.get("bins");
	}

}

