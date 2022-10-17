/*
 * Copyright 2022 Aerospike, Inc.
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
import com.aerospike.restclient.domain.operationmodels.OperationTypes;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

@RunWith(Parameterized.class)
@SpringBootTest
@AutoConfigureMockMvc
public class OperateV2ListCorrectTests {

    @ClassRule
    public static final SpringClassRule springClassRule = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @Autowired
    private MockMvc mockMVC;

    @Autowired
    private AerospikeClient client;

    List<Object> objectList;
    List<Object> objectMapList;
    private Map<String, Object> opRequest;
    private List<Map<String, Object>> opList;
    private final Key testKey;
    private final String testEndpoint;

    private final String OPERATION_FIELD = "type";

    @Before
    public void setup() {
        Bin strBin = new Bin("str", "bin");
        objectList = new ArrayList<>();
        objectList.add(1L);
        objectList.add(2L);
        objectList.add(0L);
        objectList.add(3L);
        objectList.add(4L);

        Map<String, String> mapValue = new HashMap<>();
        mapValue.put("3", "b");
        mapValue.put("1", "a");
        mapValue.put("5", "c");

        objectMapList = new ArrayList<>();
        objectMapList.add(mapValue);

        Bin listBin = new Bin("list", objectList);
        Bin mapBin = new Bin("map", objectMapList);
        client.put(null, testKey, strBin, listBin, mapBin);

        opList = new ArrayList<>();
        opRequest = new HashMap<>();
        opRequest.put("opsList", opList);
    }

    @After
    public void clean() {
        client.delete(null, testKey);
    }

    private final OperationV2Performer opPerformer;

    @Parameterized.Parameters
    public static Object[][] getParams() {
        return new Object[][]{
                {new JSONOperationV2Performer(), true},
                {new MsgPackOperationV2Performer(), true},
                {new JSONOperationV2Performer(), false},
                {new MsgPackOperationV2Performer(), false}
        };
    }

    /* Set up the correct msgpack/json performer for this set of runs. Also decided whether to use the endpoint with a set or without */
    public OperateV2ListCorrectTests(OperationV2Performer performer, boolean useSet) {
        this.opPerformer = performer;
        if (useSet) {
            testKey = new Key("test", "junit", "listop");
            testEndpoint = ASTestUtils.buildEndpointV2("operate", "test", "junit", "listop");
        } else {
            testKey = new Key("test", null, "listop");
            testEndpoint = ASTestUtils.buildEndpointV2("operate", "test", "listop");
        }
    }

    @Test
    public void testListAppend() {
        Map<String, Object> opMap = new HashMap<>();

        opRequest.put("opsList", opList);
        opList.add(opMap);
        opMap.put("binName", "list");
        opMap.put("value", "aerospike");
        opMap.put(OPERATION_FIELD, OperationTypes.LIST_APPEND);
//        

        opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest);

        @SuppressWarnings("unchecked") List<Object> realList = (List<Object>) client.get(null, testKey).bins.get(
                "list");
        objectList.add("aerospike");

        Assert.assertTrue(ASTestUtils.compareCollection(objectList, realList));
    }

    @Test
    public void testListAppendItems() {

        Map<String, Object> opMap = new HashMap<>();

        List<Object> appendValues = Arrays.asList("aero", "spike", "aero");

        opMap.put("binName", "list");
        opMap.put("values", appendValues);
        opMap.put(OPERATION_FIELD, OperationTypes.LIST_APPEND_ITEMS);

        opList.add(opMap);
        opRequest.put("opsList", opList);

        opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest);

        @SuppressWarnings("unchecked") List<Object> realList = (List<Object>) client.get(null, testKey).bins.get(
                "list");
        objectList.add("aero");
        objectList.add("spike");
        objectList.add("aero");

        Assert.assertTrue(ASTestUtils.compareCollection(objectList, realList));
    }

    @Test
    public void testListAppendItemsWithPolicy() {
        Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

        Map<String, Object> opMap = new HashMap<>();

        Map<String, Object> listPolicyMap = buildListPolicyMap(ListOrder.UNORDERED, ListWriteFlags.ADD_UNIQUE);
        List<Object> appendValues = Arrays.asList("aero", "spike");

        opMap.put("values", appendValues);
        opMap.put("listPolicy", listPolicyMap);
        opMap.put("binName", "list");

        opMap.put(OPERATION_FIELD, OperationTypes.LIST_APPEND_ITEMS);

        opList.add(opMap);

        opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest);

        @SuppressWarnings("unchecked") List<Object> realList = (List<Object>) client.get(null, testKey).bins.get(
                "list");
        objectList.add("aero");
        objectList.add("spike");

        Assert.assertTrue(ASTestUtils.compareCollection(objectList, realList));
    }

    @Test
    public void testListClear() {

        Map<String, Object> opMap = new HashMap<>();

        opMap.put("binName", "list");
        opMap.put(OPERATION_FIELD, OperationTypes.LIST_CLEAR);

        opList.add(opMap);

        opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest);

        @SuppressWarnings("unchecked") List<Object> realList = (List<Object>) client.get(null, testKey).bins.get(
                "list");

        Assert.assertEquals(realList.size(), 0);
    }

    @Test
    public void testListGet() {

        Map<String, Object> opMap = new HashMap<>();

        opMap.put("binName", "list");
        opMap.put("index", 2);
        opMap.put(OPERATION_FIELD, OperationTypes.LIST_GET);

        opList.add(opMap);

        Map<String, Object> binsObject = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));

        Assert.assertTrue(ASTestUtils.compareSimpleValues(binsObject.get("list"), objectList.get(2)));
    }

    @Test
    public void testListGetByIndexIndex() {
        Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

        Map<String, Object> opMap = new HashMap<>();

        opMap.put("binName", "list");
        opMap.put("index", 2);
        opMap.put("listReturnType", "INDEX");
        opMap.put(OPERATION_FIELD, OperationTypes.LIST_GET_BY_INDEX);

        opList.add(opMap);

        Map<String, Object> binsObject = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));

        Assert.assertTrue(ASTestUtils.compareSimpleValues(binsObject.get("list"), 2));
    }

    @Test
    public void testListGetByIndexReverseRank() {
        Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

        Map<String, Object> opMap = new HashMap<>();

        opMap.put("binName", "list");
        opMap.put("index", 2);
        opMap.put("listReturnType", "REVERSE_RANK");
        opMap.put(OPERATION_FIELD, OperationTypes.LIST_GET_BY_INDEX);

        opList.add(opMap);

        Map<String, Object> binsObject = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));

        Assert.assertTrue(ASTestUtils.compareSimpleValues(binsObject.get("list"), 4));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testListGetByIndexRange() {
        Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

        Map<String, Object> opMap = new HashMap<>();

        opMap.put("binName", "list");
        opMap.put("index", 1);
        opMap.put("count", 3);
        opMap.put("listReturnType", "VALUE");
        opMap.put(OPERATION_FIELD, OperationTypes.LIST_GET_BY_INDEX_RANGE);

        opList.add(opMap);

        Map<String, Object> binsObject = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));

        List<Object> retItems = (List<Object>) binsObject.get("list");
        Assert.assertTrue(ASTestUtils.compareCollection(retItems, Arrays.asList(2, 0, 3)));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testListGetByIndexRangeNoCount() {
        Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

        Map<String, Object> opMap = new HashMap<>();

        opMap.put("binName", "list");
        opMap.put("index", 1);
        opMap.put("listReturnType", "VALUE");
        opMap.put(OPERATION_FIELD, OperationTypes.LIST_GET_BY_INDEX_RANGE);

        opList.add(opMap);

        Map<String, Object> binsObject = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));

        List<Object> retItems = (List<Object>) binsObject.get("list");
        Assert.assertTrue(ASTestUtils.compareCollection(retItems, Arrays.asList(2, 0, 3, 4)));
    }

    @Test
    public void testListGetByRankValue() {
        Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

        Map<String, Object> opMap = new HashMap<>();

        opMap.put("binName", "list");
        opMap.put("rank", 2);
        opMap.put("listReturnType", "VALUE");
        opMap.put(OPERATION_FIELD, OperationTypes.LIST_GET_BY_RANK);

        opList.add(opMap);

        Map<String, Object> binsObject = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));

        Assert.assertTrue(ASTestUtils.compareSimpleValues(binsObject.get("list"), 2));
    }

    @Test
    public void testListGetByRankIndex() {
        Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

        Map<String, Object> opMap = new HashMap<>();

        opMap.put("binName", "list");
        opMap.put("rank", 2);
        opMap.put("listReturnType", "INDEX");
        opMap.put(OPERATION_FIELD, OperationTypes.LIST_GET_BY_RANK);

        opList.add(opMap);

        Map<String, Object> binsObject = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));

        Assert.assertTrue(ASTestUtils.compareSimpleValues(binsObject.get("list"), 1));
    }

    @Test
    public void testListGetByRankRange() {
        Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

        Map<String, Object> opMap = new HashMap<>();

        opMap.put("binName", "list");
        opMap.put("rank", 1);
        opMap.put("count", 3);
        opMap.put("listReturnType", "VALUE");
        opMap.put(OPERATION_FIELD, OperationTypes.LIST_GET_BY_RANK_RANGE);

        opList.add(opMap);

        Map<String, Object> binsObject = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));

        @SuppressWarnings("unchecked") List<Object> retItems = (List<Object>) binsObject.get("list");

        Set<Object> retItemSet = new HashSet<>(retItems);
        Set<Object> expectedSet = new HashSet<>(Arrays.asList(1, 2, 3));
        Assert.assertEquals(retItemSet, expectedSet);
    }

    @Test
    public void testListGetByRankRangeNoCount() {
        Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

        Map<String, Object> opMap = new HashMap<>();

        opMap.put("binName", "list");
        opMap.put("rank", 1);
        opMap.put("listReturnType", "VALUE");
        opMap.put(OPERATION_FIELD, OperationTypes.LIST_GET_BY_RANK_RANGE);

        opList.add(opMap);

        Map<String, Object> binsObject = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));

        @SuppressWarnings("unchecked") List<Object> retItems = (List<Object>) binsObject.get("list");

        Set<Object> retItemSet = new HashSet<>(retItems);
        Set<Object> expectedSet = new HashSet<>(Arrays.asList(1, 2, 3, 4));
        Assert.assertEquals(retItemSet, expectedSet);
    }

    @Test
    public void testListGetByValueRelRankRange() {
        Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

        Map<String, Object> opMap = new HashMap<>();

        opMap.put("binName", "list");
        opMap.put("rank", 1);
        opMap.put("value", 2);
        opMap.put("count", 2);
        opMap.put("listReturnType", "VALUE");
        opMap.put(OPERATION_FIELD, OperationTypes.LIST_GET_BY_VALUE_RELATIVE_RANK_RANGE);

        opList.add(opMap);

        Map<String, Object> binsObject = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));

        @SuppressWarnings("unchecked") List<Object> retItems = (List<Object>) binsObject.get("list");

        Set<Object> retItemSet = new HashSet<>(retItems);
        Set<Object> expectedSet = new HashSet<>(Arrays.asList(3, 4));
        Assert.assertEquals(retItemSet, expectedSet);
    }

    @Test
    public void testListGetByValueRelRankRangeNoCount() {
        Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

        Map<String, Object> opMap = new HashMap<>();

        opMap.put("binName", "list");
        opMap.put("rank", 2);
        opMap.put("value", 0);
        opMap.put("listReturnType", "VALUE");
        opMap.put(OPERATION_FIELD, OperationTypes.LIST_GET_BY_VALUE_RELATIVE_RANK_RANGE);

        opList.add(opMap);

        Map<String, Object> binsObject = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));

        @SuppressWarnings("unchecked") List<Object> retItems = (List<Object>) binsObject.get("list");

        Set<Object> retItemSet = new HashSet<>(retItems);
        Set<Object> expectedSet = new HashSet<>(Arrays.asList(2, 3, 4));
        Assert.assertEquals(retItemSet, expectedSet);
    }

    @Test
    public void testListGetByValueIndex() {

        Map<String, Object> opMap = new HashMap<>();

        opMap.put("binName", "list");
        opMap.put("value", 0);
        opMap.put("listReturnType", "INDEX");
        opMap.put(OPERATION_FIELD, OperationTypes.LIST_GET_BY_VALUE);

        opList.add(opMap);

        Map<String, Object> binsObject = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));

        Assert.assertTrue(
                ASTestUtils.compareCollection((List<?>) binsObject.get("list"), Collections.singletonList(2)));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testListGetByValueRange() {
        Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

        Map<String, Object> opMap = new HashMap<>();

        opMap.put("binName", "list");
        opMap.put("valueBegin", 1);
        opMap.put("valueEnd", 4);
        opMap.put("listReturnType", "VALUE");
        opMap.put(OPERATION_FIELD, OperationTypes.LIST_GET_BY_VALUE_RANGE);

        opList.add(opMap);

        Map<String, Object> binsObject = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));

        List<Object> retItems = (List<Object>) binsObject.get("list");

        Set<Object> retItemSet = new HashSet<>(retItems);
        Set<Object> expectedSet = new HashSet<>(Arrays.asList(1, 2, 3));
        Assert.assertEquals(retItemSet, expectedSet);
    }

    @Test
    public void testListGetByValueRangeNoEnd() {
        Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

        Map<String, Object> opMap = new HashMap<>();

        opMap.put("binName", "list");
        opMap.put("valueBegin", 1);
        opMap.put("listReturnType", "VALUE");
        opMap.put(OPERATION_FIELD, OperationTypes.LIST_GET_BY_VALUE_RANGE);

        opList.add(opMap);

        Map<String, Object> binsObject = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));

        @SuppressWarnings("unchecked") List<Object> retItems = (List<Object>) binsObject.get("list");

        Set<Object> retItemSet = new HashSet<>(retItems);
        Set<Object> expectedSet = new HashSet<>(Arrays.asList(1, 2, 3, 4));
        Assert.assertEquals(retItemSet, expectedSet);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testListGetByValueRangeNoBegin() {
        Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

        Map<String, Object> opMap = new HashMap<>();

        opMap.put("binName", "list");
        opMap.put("valueEnd", 4);
        opMap.put("listReturnType", "VALUE");
        opMap.put(OPERATION_FIELD, OperationTypes.LIST_GET_BY_VALUE_RANGE);

        opList.add(opMap);

        Map<String, Object> binsObject = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));

        List<Object> retItems = (List<Object>) binsObject.get("list");

        Set<Object> retItemSet = new HashSet<>(retItems);
        Set<Object> expectedSet = new HashSet<>(Arrays.asList(0, 1, 2, 3));
        Assert.assertEquals(retItemSet, expectedSet);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testListGetByValueList() {
        Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

        Map<String, Object> opMap = new HashMap<>();

        opMap.put("binName", "list");
        opMap.put("values", Arrays.asList(0, 1, 4));
        opMap.put("listReturnType", "INDEX");
        opMap.put(OPERATION_FIELD, OperationTypes.LIST_GET_BY_VALUE_LIST);

        opList.add(opMap);

        Map<String, Object> binsObject = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));

        List<Object> retItems = (List<Object>) binsObject.get("list");

        Set<Object> retItemSet = new HashSet<>(retItems);
        Set<Object> expectedSet = new HashSet<>(Arrays.asList(2, 0, 4));
        Assert.assertEquals(retItemSet, expectedSet);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testListGetByMapValueList() {
        Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

        Map<String, Object> opMap = new HashMap<>();

        opMap.put("binName", "map");
        opMap.put("values", objectMapList);
        opMap.put("listReturnType", "INDEX");
        opMap.put(OPERATION_FIELD, OperationTypes.LIST_GET_BY_VALUE_LIST);

        opList.add(opMap);

        Map<String, Object> binsObject = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));

        List<Object> retItems = (List<Object>) binsObject.get("map");

        Set<Object> retItemSet = new HashSet<>(retItems);
        Set<Object> expectedSet = new HashSet<>(Collections.singletonList(0));
        Assert.assertEquals(expectedSet, retItemSet);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testListGetRange() {

        Map<String, Object> opMap = new HashMap<>();

        opMap.put("binName", "list");
        opMap.put("index", 1);
        opMap.put("count", 3);
        opMap.put(OPERATION_FIELD, OperationTypes.LIST_GET_RANGE);

        opList.add(opMap);

        Map<String, Object> binsObject = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));

        List<Object> retItems = (List<Object>) binsObject.get("list");

        Assert.assertTrue(ASTestUtils.compareCollection(retItems, Arrays.asList(2, 0, 3)));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testListGetRangeNoCount() {

        Map<String, Object> opMap = new HashMap<>();

        opMap.put("binName", "list");
        opMap.put("index", 1);
        opMap.put(OPERATION_FIELD, OperationTypes.LIST_GET_RANGE);

        opList.add(opMap);

        Map<String, Object> binsObject = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));

        List<Object> retItems = (List<Object>) binsObject.get("list");

        Assert.assertTrue(ASTestUtils.compareCollection(retItems, Arrays.asList(2, 0, 3, 4)));
    }

    @Test
    public void testListIncrement() {

        Map<String, Object> opMap = new HashMap<>();

        opMap.put("binName", "list");
        opMap.put("index", 1);
        opMap.put("incr", 10);
        opMap.put(OPERATION_FIELD, OperationTypes.LIST_INCREMENT);

        opList.add(opMap);

        opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest);

        @SuppressWarnings("unchecked") List<Object> retItems = (List<Object>) client.get(null, testKey).bins.get(
                "list");

        Assert.assertTrue(ASTestUtils.compareSimpleValues(retItems.get(1), 12));
    }

    @Test
    public void testListIncrementWithPolicy() {
        Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

        Map<String, Object> opMap = new HashMap<>();

        Map<String, Object> listPolicyMap = buildListPolicyMap(ListOrder.UNORDERED, ListWriteFlags.DEFAULT);

        opMap.put("binName", "list");
        opMap.put("index", 1);
        opMap.put("incr", 10);
        opMap.put("listPolicy", listPolicyMap);

        opMap.put(OPERATION_FIELD, OperationTypes.LIST_INCREMENT);

        opList.add(opMap);

        opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest);

        @SuppressWarnings("unchecked") List<Object> retItems = (List<Object>) client.get(null, testKey).bins.get(
                "list");

        Assert.assertTrue(ASTestUtils.compareSimpleValues(retItems.get(1), 12));
    }

    @Test
    public void testListInsert() {

        Map<String, Object> opMap = new HashMap<>();

        opMap.put("binName", "list");
        opMap.put("index", 1);
        opMap.put("value", "one");

        opMap.put(OPERATION_FIELD, OperationTypes.LIST_INSERT);

        opList.add(opMap);

        opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest);

        @SuppressWarnings("unchecked") List<Object> retItems = (List<Object>) client.get(null, testKey).bins.get(
                "list");
        objectList.add(1, "one");

        Assert.assertTrue(ASTestUtils.compareCollection(retItems, objectList));
    }

    @Test
    public void testListInsertPolicy() {
        Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

        Map<String, Object> opMap = new HashMap<>();

        Map<String, Object> listPolicyMap = buildListPolicyMap(ListOrder.UNORDERED, ListWriteFlags.DEFAULT);

        opMap.put("binName", "list");
        opMap.put("index", 1);
        opMap.put("value", 5);
        opMap.put("listPolicy", listPolicyMap);
        opMap.put(OPERATION_FIELD, OperationTypes.LIST_INSERT);

        opList.add(opMap);

        opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest);

        @SuppressWarnings("unchecked") List<Object> retItems = (List<Object>) client.get(null, testKey).bins.get(
                "list");

        Assert.assertTrue(ASTestUtils.compareCollection(retItems, Arrays.asList(1, 5, 2, 0, 3, 4)));
    }

    @Test
    public void testListInsertItems() {

        Map<String, Object> opMap = new HashMap<>();

        opMap.put("binName", "list");
        opMap.put("index", 1);
        opMap.put("values", Arrays.asList("one", "two", "three"));

        opMap.put(OPERATION_FIELD, OperationTypes.LIST_INSERT_ITEMS);

        opList.add(opMap);

        opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest);

        @SuppressWarnings("unchecked") List<Object> retItems = (List<Object>) client.get(null, testKey).bins.get(
                "list");
        objectList.add(1, "three");
        objectList.add(1, "two");
        objectList.add(1, "one");

        Assert.assertTrue(ASTestUtils.compareCollection(retItems, objectList));
    }

    @Test
    public void testListInsertItemsPolicy() {
        Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

        Map<String, Object> opMap = new HashMap<>();

        Map<String, Object> listPolicyMap = buildListPolicyMap(ListOrder.UNORDERED, ListWriteFlags.DEFAULT);

        opMap.put("binName", "list");
        opMap.put("index", 1);
        opMap.put("values", Arrays.asList("one", "two", "three"));
        opMap.put("listPolicy", listPolicyMap);
        opMap.put(OPERATION_FIELD, OperationTypes.LIST_INSERT_ITEMS);

        opList.add(opMap);

        opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest);

        @SuppressWarnings("unchecked") List<Object> retItems = (List<Object>) client.get(null, testKey).bins.get(
                "list");
        objectList.add(1, "three");
        objectList.add(1, "two");
        objectList.add(1, "one");

        Assert.assertTrue(ASTestUtils.compareCollection(retItems, objectList));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testListPop() {

        Map<String, Object> opMap = new HashMap<>();

        opMap.put("binName", "list");
        opMap.put("index", 1);
        opMap.put(OPERATION_FIELD, OperationTypes.LIST_POP);

        opList.add(opMap);

        Map<String, Object> returnedBins = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));

        Assert.assertTrue(ASTestUtils.compareSimpleValues(returnedBins.get("list"), 2));

        List<Object> retItems = (List<Object>) client.get(null, testKey).bins.get("list");

        objectList.remove(1);
        Assert.assertTrue(ASTestUtils.compareCollection(retItems, objectList));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testListPopRange() {

        Map<String, Object> opMap = new HashMap<>();

        opMap.put("binName", "list");
        opMap.put("index", 1);
        opMap.put("count", 3);
        opMap.put(OPERATION_FIELD, OperationTypes.LIST_POP_RANGE);

        opList.add(opMap);

        Map<String, Object> returnedBins = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));

        Assert.assertTrue(ASTestUtils.compareSimpleValues(returnedBins.get("list"), Arrays.asList(2, 0, 3)));

        List<Object> retItems = (List<Object>) client.get(null, testKey).bins.get("list");
        // Remove 3 items starting from index 1
        objectList.remove(1);
        objectList.remove(1);
        objectList.remove(1);

        Assert.assertTrue(ASTestUtils.compareCollection(retItems, objectList));
    }

    @Test
    public void testListPopRangeNoCount() {

        Map<String, Object> opMap = new HashMap<>();

        opMap.put("binName", "list");
        opMap.put("index", 1);
        opMap.put(OPERATION_FIELD, OperationTypes.LIST_POP_RANGE);

        opList.add(opMap);

        Map<String, Object> returnedBins = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));

        Assert.assertTrue(ASTestUtils.compareSimpleValues(returnedBins.get("list"), Arrays.asList(2, 0, 3, 4)));

        @SuppressWarnings("unchecked") List<Object> retItems = (List<Object>) client.get(null, testKey).bins.get(
                "list");

        // Remove 4 items starting from index 1
        objectList.remove(1);
        objectList.remove(1);
        objectList.remove(1);
        objectList.remove(1);

        Assert.assertTrue(ASTestUtils.compareCollection(retItems, objectList));
    }

    @Test
    public void testListRemove() {

        Map<String, Object> opMap = new HashMap<>();

        opMap.put("binName", "list");
        opMap.put("index", 2);
        opMap.put(OPERATION_FIELD, OperationTypes.LIST_REMOVE);

        opList.add(opMap);

        opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest);

        @SuppressWarnings("unchecked") List<Object> retItems = (List<Object>) client.get(null, testKey).bins.get(
                "list");

        objectList.remove(2);
        Assert.assertTrue(ASTestUtils.compareCollection(retItems, objectList));
    }

    @Test
    public void testListRemoveByIndex() {
        Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

        Map<String, Object> opMap = new HashMap<>();

        opMap.put("binName", "list");
        opMap.put("index", 2);
        opMap.put("listReturnType", "RANK");
        opMap.put(OPERATION_FIELD, OperationTypes.LIST_REMOVE_BY_INDEX);

        opList.add(opMap);

        Map<String, Object> returnedBins = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));

        //The popped value was the smallest element, so it's rank should be 0
        Assert.assertTrue(ASTestUtils.compareSimpleValues(returnedBins.get("list"), 0));

        @SuppressWarnings("unchecked") List<Object> retItems = (List<Object>) client.get(null, testKey).bins.get(
                "list");

        objectList.remove(2);
        Assert.assertTrue(ASTestUtils.compareCollection(retItems, objectList));
    }

    @Test
    public void testListRemoveByIndexRange() {
        Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

        Map<String, Object> opMap = new HashMap<>();

        opMap.put("binName", "list");
        opMap.put("index", 1);
        opMap.put("count", 3);
        opMap.put("listReturnType", "COUNT");
        opMap.put(OPERATION_FIELD, OperationTypes.LIST_REMOVE_BY_INDEX_RANGE);

        opList.add(opMap);

        Map<String, Object> returnedBins = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));

        //Three items were removed, so the server should return 3 items
        Assert.assertTrue(ASTestUtils.compareSimpleValues(returnedBins.get("list"), 3));

        @SuppressWarnings("unchecked") List<Object> retItems = (List<Object>) client.get(null, testKey).bins.get(
                "list");

        objectList.remove(1);
        objectList.remove(1);
        objectList.remove(1);
        Assert.assertTrue(ASTestUtils.compareCollection(retItems, objectList));
    }

    @Test
    public void testListRemoveByIndexRangeNoCount() {
        Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

        Map<String, Object> opMap = new HashMap<>();

        opMap.put("binName", "list");
        opMap.put("index", 1);
        opMap.put("listReturnType", "COUNT");
        opMap.put(OPERATION_FIELD, OperationTypes.LIST_REMOVE_BY_INDEX_RANGE);

        opList.add(opMap);

        Map<String, Object> returnedBins = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));

        //Three items were removed, so the server should return 3 items
        Assert.assertTrue(ASTestUtils.compareSimpleValues(returnedBins.get("list"), 4));

        @SuppressWarnings("unchecked") List<Object> retItems = (List<Object>) client.get(null, testKey).bins.get(
                "list");

        objectList.remove(1);
        objectList.remove(1);
        objectList.remove(1);
        objectList.remove(1);
        Assert.assertTrue(ASTestUtils.compareCollection(retItems, objectList));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testListRemoveByRank() {
        Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

        Map<String, Object> opMap = new HashMap<>();

        opMap.put("binName", "list");
        opMap.put("rank", 0);
        opMap.put("listReturnType", "INDEX");
        opMap.put(OPERATION_FIELD, OperationTypes.LIST_REMOVE_BY_RANK);

        opList.add(opMap);

        Map<String, Object> returnedBins = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));

        //The popped value was the smallest element, it was at index 2
        Assert.assertTrue(ASTestUtils.compareSimpleValues(returnedBins.get("list"), 2));

        List<Object> retItems = (List<Object>) client.get(null, testKey).bins.get("list");
        objectList.remove(2);
        Assert.assertTrue(ASTestUtils.compareCollection(retItems, objectList));
    }

    @Test
    public void testListRemoveByRankRange() {
        Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

        Map<String, Object> opMap = new HashMap<>();

        opMap.put("binName", "list");
        opMap.put("rank", 1);
        opMap.put("count", 3);
        opMap.put("listReturnType", "VALUE");
        opMap.put(OPERATION_FIELD, OperationTypes.LIST_REMOVE_BY_RANK_RANGE);

        opList.add(opMap);

        opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest);

        @SuppressWarnings("unchecked") List<Object> retItems = (List<Object>) client.get(null, testKey).bins.get(
                "list");
        Assert.assertTrue(ASTestUtils.compareCollection(retItems, Arrays.asList(0, 4)));
    }

    @Test
    public void testListRemoveByRankRangeNoCount() {
        Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

        Map<String, Object> opMap = new HashMap<>();

        opMap.put("binName", "list");
        opMap.put("rank", 1);
        opMap.put("listReturnType", "VALUE");
        opMap.put(OPERATION_FIELD, OperationTypes.LIST_REMOVE_BY_RANK_RANGE);

        opList.add(opMap);

        opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest);

        @SuppressWarnings("unchecked") List<Object> retItems = (List<Object>) client.get(null, testKey).bins.get(
                "list");
        Assert.assertTrue(ASTestUtils.compareCollection(retItems, Collections.singletonList(0)));
    }

    @Test
    public void testListRemoveByValueRelRankRange() {
        Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

        Map<String, Object> opMap = new HashMap<>();

        opMap.put("binName", "list");
        opMap.put("value", 1);
        opMap.put("rank", 1);
        opMap.put("count", 2);
        opMap.put("listReturnType", "VALUE");
        opMap.put(OPERATION_FIELD, OperationTypes.LIST_REMOVE_BY_VALUE_RELATIVE_RANK_RANGE);

        opList.add(opMap);

        opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest);

        @SuppressWarnings("unchecked") List<Object> retItems = (List<Object>) client.get(null, testKey).bins.get(
                "list");
        Assert.assertTrue(ASTestUtils.compareCollection(retItems, Arrays.asList(1, 0, 4)));
    }

    @Test
    public void testListRemoveByValueRelRankRangeNoCount() {
        Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

        Map<String, Object> opMap = new HashMap<>();

        opMap.put("binName", "list");
        opMap.put("value", 1);
        opMap.put("rank", 1);
        opMap.put("listReturnType", "VALUE");
        opMap.put(OPERATION_FIELD, OperationTypes.LIST_REMOVE_BY_VALUE_RELATIVE_RANK_RANGE);

        opList.add(opMap);

        opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest);

        @SuppressWarnings("unchecked") List<Object> retItems = (List<Object>) client.get(null, testKey).bins.get(
                "list");
        Assert.assertTrue(ASTestUtils.compareCollection(retItems, Arrays.asList(1, 0)));
    }

    @Test
    public void testListRemoveByValue() {
        Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

        objectList.add(0);
        Bin newListBin = new Bin("list", objectList);
        client.put(null, testKey, newListBin);

        Map<String, Object> opMap = new HashMap<>();

        opMap.put("binName", "list");
        opMap.put("value", 0);
        opMap.put("listReturnType", "INDEX");
        opMap.put(OPERATION_FIELD, OperationTypes.LIST_REMOVE_BY_VALUE);

        opList.add(opMap);

        Map<String, Object> returnedBins = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));

        //The popped value was the smallest element, it was at index 2
        Assert.assertTrue(ASTestUtils.compareCollection((List<?>) returnedBins.get("list"), Arrays.asList(2, 5)));

        @SuppressWarnings("unchecked") List<Object> retItems = (List<Object>) client.get(null, testKey).bins.get(
                "list");
        objectList.remove(5);
        objectList.remove(2);
        Assert.assertTrue(ASTestUtils.compareCollection(retItems, objectList));
    }

    @Test
    public void testListRemoveByValueRange() {
        Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

        Map<String, Object> opMap = new HashMap<>();

        opMap.put("binName", "list");
        opMap.put("valueBegin", 1);
        opMap.put("valueEnd", 3);
        opMap.put("listReturnType", "COUNT");
        opMap.put(OPERATION_FIELD, OperationTypes.LIST_REMOVE_BY_VALUE_RANGE);

        opList.add(opMap);

        Map<String, Object> returnedBins = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));

        //The popped value was the smallest element, it was at index 2
        Assert.assertTrue(ASTestUtils.compareSimpleValues(returnedBins.get("list"), 2));

        @SuppressWarnings("unchecked") List<Object> retItems = (List<Object>) client.get(null, testKey).bins.get(
                "list");
        Assert.assertTrue(ASTestUtils.compareCollection(retItems, Arrays.asList(0, 3, 4)));
    }

    @Test
    public void testListRemoveByValueRangeNoBegin() {
        Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

        Map<String, Object> opMap = new HashMap<>();

        opMap.put("binName", "list");
        opMap.put("valueEnd", 3);
        opMap.put("listReturnType", "COUNT");
        opMap.put(OPERATION_FIELD, OperationTypes.LIST_REMOVE_BY_VALUE_RANGE);

        opList.add(opMap);

        Map<String, Object> returnedBins = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));

        //The popped value was the smallest element, it was at index 2
        Assert.assertTrue(ASTestUtils.compareSimpleValues(returnedBins.get("list"), 3));

        @SuppressWarnings("unchecked") List<Object> retItems = (List<Object>) client.get(null, testKey).bins.get(
                "list");
        Assert.assertTrue(ASTestUtils.compareCollection(retItems, Arrays.asList(3, 4)));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testListRemoveByValueRangeNoEnd() {
        Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

        Map<String, Object> opMap = new HashMap<>();

        opMap.put("binName", "list");
        opMap.put("valueBegin", 1);
        opMap.put("listReturnType", "COUNT");
        opMap.put(OPERATION_FIELD, OperationTypes.LIST_REMOVE_BY_VALUE_RANGE);

        opList.add(opMap);

        Map<String, Object> returnedBins = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));

        //The popped value was the smallest element, it was at index 2
        Assert.assertTrue(ASTestUtils.compareSimpleValues(returnedBins.get("list"), 4));

        List<Object> retItems = (List<Object>) client.get(null, testKey).bins.get("list");
        Assert.assertTrue(ASTestUtils.compareCollection(retItems, Collections.singletonList(0)));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testListRemoveByValueList() {
        Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

        objectList.add(0);
        Bin newListBin = new Bin("list", objectList);
        client.put(null, testKey, newListBin);

        Map<String, Object> opMap = new HashMap<>();

        opMap.put("binName", "list");
        opMap.put("values", Arrays.asList(0, 2, 4));
        opMap.put("listReturnType", "COUNT");
        opMap.put(OPERATION_FIELD, OperationTypes.LIST_REMOVE_BY_VALUE_LIST);

        opList.add(opMap);

        Map<String, Object> returnedBins = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));

        //The popped value was the smallest element, it was at index 2
        Assert.assertTrue(ASTestUtils.compareSimpleValues(returnedBins.get("list"), 4));

        List<Object> retItems = (List<Object>) client.get(null, testKey).bins.get("list");

        Assert.assertTrue(ASTestUtils.compareCollection(retItems, Arrays.asList(1, 3)));
    }

    @Test
    public void testListRemoveRange() {

        Map<String, Object> opMap = new HashMap<>();

        opMap.put("binName", "list");
        opMap.put("index", 1);
        opMap.put("count", 3);
        opMap.put(OPERATION_FIELD, OperationTypes.LIST_REMOVE_RANGE);

        opList.add(opMap);

        opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest);

        @SuppressWarnings("unchecked") List<Object> retItems = (List<Object>) client.get(null, testKey).bins.get(
                "list");

        objectList.remove(1);
        objectList.remove(1);
        objectList.remove(1);
        Assert.assertTrue(ASTestUtils.compareCollection(retItems, objectList));
    }

    @Test
    public void testListRemoveRangeNoCount() {

        Map<String, Object> opMap = new HashMap<>();

        opMap.put("binName", "list");
        opMap.put("index", 1);
        opMap.put(OPERATION_FIELD, OperationTypes.LIST_REMOVE_RANGE);

        opList.add(opMap);

        opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest);

        @SuppressWarnings("unchecked") List<Object> retItems = (List<Object>) client.get(null, testKey).bins.get(
                "list");

        objectList.remove(1);
        objectList.remove(1);
        objectList.remove(1);
        objectList.remove(1);
        Assert.assertTrue(ASTestUtils.compareCollection(retItems, objectList));
    }

    @Test
    public void testListSetValue() {

        Map<String, Object> opMap = new HashMap<>();

        opMap.put("binName", "list");
        opMap.put("value", "two");
        opMap.put("index", 1);
        opMap.put(OPERATION_FIELD, OperationTypes.LIST_SET);

        opList.add(opMap);

        opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest);

        @SuppressWarnings("unchecked") List<Object> retItems = (List<Object>) client.get(null, testKey).bins.get(
                "list");

        Assert.assertTrue(ASTestUtils.compareCollection(retItems, Arrays.asList(1, "two", 0, 3, 4)));
    }

    @Test
    public void testListSetValueWithPolicy() {
        Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

        Map<String, Object> opMap = new HashMap<>();

        Map<String, Object> listPolicyMap = buildListPolicyMap(ListOrder.UNORDERED, ListWriteFlags.ADD_UNIQUE);

        opMap.put("binName", "list");
        opMap.put("value", "two");
        opMap.put("index", 1);
        opMap.put("listPolicy", listPolicyMap);
        opMap.put(OPERATION_FIELD, OperationTypes.LIST_SET);

        opList.add(opMap);

        opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest);

        @SuppressWarnings("unchecked") List<Object> retItems = (List<Object>) client.get(null, testKey).bins.get(
                "list");

        Assert.assertTrue(ASTestUtils.compareCollection(retItems, Arrays.asList(1, "two", 0, 3, 4)));
    }

    @Test
    public void testListSetOrder() {
        Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

        Map<String, Object> opMap = new HashMap<>();

        opMap.put("binName", "list");
        opMap.put("listOrder", "ORDERED");
        opMap.put(OPERATION_FIELD, OperationTypes.LIST_SET_ORDER);

        opList.add(opMap);

        opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest);

        @SuppressWarnings("unchecked") List<Object> retItems = (List<Object>) client.get(null, testKey).bins.get(
                "list");

        Assert.assertTrue(ASTestUtils.compareCollection(retItems, Arrays.asList(0, 1, 2, 3, 4)));
    }

    @Test
    public void testListSize() {

        Map<String, Object> opMap = new HashMap<>();

        opMap.put("binName", "list");
        opMap.put(OPERATION_FIELD, OperationTypes.LIST_SIZE);

        opList.add(opMap);

        Map<String, Object> retBins = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));

        Assert.assertTrue(ASTestUtils.compareSimpleValues(retBins.get("list"), 5));
    }

    @Test
    public void testListSort() {
        Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

        Map<String, Object> opMap = new HashMap<>();

        opMap.put("binName", "list");
        opMap.put(OPERATION_FIELD, OperationTypes.LIST_SORT);

        opList.add(opMap);

        opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest);

        @SuppressWarnings("unchecked") List<Object> retItems = (List<Object>) client.get(null, testKey).bins.get(
                "list");
        Assert.assertTrue(ASTestUtils.compareCollection(retItems, Arrays.asList(0, 1, 2, 3, 4)));
    }

    @Test
    public void testListTrim() {

        Map<String, Object> opMap = new HashMap<>();

        opMap.put("binName", "list");
        opMap.put("index", 1);
        opMap.put("count", 3);
        opMap.put(OPERATION_FIELD, OperationTypes.LIST_TRIM);

        opList.add(opMap);

        opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest);

        @SuppressWarnings("unchecked") List<Object> retItems = (List<Object>) client.get(null, testKey).bins.get(
                "list");
        Assert.assertTrue(ASTestUtils.compareCollection(retItems, Arrays.asList(2, 0, 3)));
    }

    @Test
    public void testListCreate() {

        Map<String, Object> opMap = new HashMap<>();

        Map<String, Object> item = new HashMap<>();
        List<Map<String, Object>> ctx = new ArrayList<>();

        opMap.put("binName", "list");
        opMap.put("ctx", ctx);
        opMap.put("order", "UNORDERED");
        opMap.put("pad", true);

        item.put("type", "listIndex");
        item.put("index", 7);
        ctx.add(item);

        opMap.put(OPERATION_FIELD, OperationTypes.LIST_CREATE);

        opList.add(opMap);

        opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest);

        @SuppressWarnings("unchecked") List<Object> retItems = (List<Object>) client.get(null, testKey).bins.get(
                "list");
        Assert.assertTrue(((List<?>) retItems.get(7)).isEmpty());
    }

    private Map<String, Object> buildListPolicyMap(ListOrder order, int flags) {
        Map<String, Object> policyMap = new HashMap<>();
        List<Object> flagStrings = new ArrayList<>();
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
    private Map<String, Object> getReturnedBins(Map<String, Object> resp) {
        Map<String, Object> record = (Map<String, Object>) resp.get("record");
        return (Map<String, Object>) record.get("bins");
    }

}
