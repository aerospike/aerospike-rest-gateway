package com.aerospike.restclient;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.restclient.util.AerospikeOperation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.aerospike.restclient.util.AerospikeAPIConstants.OPERATION_FIELD;
import static com.aerospike.restclient.util.AerospikeAPIConstants.OPERATION_VALUES_FIELD;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class NestedCdtOperationsTest {

    private MockMvc mockMVC;

    @Autowired
    private AerospikeClient client;

    @Autowired
    private WebApplicationContext wac;

    private OperationPerformer opPerformer;

    private final Key testKey = new Key("test", "junit", "nested");
    private final String testEndpoint = ASTestUtils.buildEndpoint("operate", "test", "junit", "nested");

    private List<Object> l1, l2, l3, objectList;
    private Map<Object, Object> m1, m2, objectMap;

    private static Stream<Arguments> getParams() {
        return Stream.of(
                Arguments.of(new JSONOperationPerformer(), new MsgPackOperationPerformer())
        );
    }

    @ParameterizedTest
    @MethodSource("getParams")
    void addParams(OperationPerformer performer) {
        this.opPerformer = performer;
    }

    @BeforeEach
    public void setup() {
        mockMVC = MockMvcBuilders.webAppContextSetup(wac).build();

        l1 = new ArrayList<>();
        l1.add(7);
        l1.add(9);
        l1.add(5);

        l2 = new ArrayList<>();
        l2.add(1);
        l2.add(2);
        l2.add(3);

        l3 = new ArrayList<>();
        l3.add(6);
        l3.add(5);
        l3.add(4);
        l3.add(1);

        objectList = new ArrayList<>();
        objectList.add(l1);
        objectList.add(l2);
        objectList.add(l3);

        Bin listBin = new Bin("list", objectList);

        m1 = new HashMap<>();
        m1.put("one", 1);
        m1.put("two", 2);
        m1.put("three", 3);

        m2 = new HashMap<>();
        m2.put("one", 1);
        m2.put("two", 2);
        m2.put("three", 3);

        objectMap = new HashMap<>();
        objectMap.put("m1", m1);
        objectMap.put("m2", m2);

        Bin mapBin = new Bin("map", objectMap);
        client.put(null, testKey, listBin, mapBin);
    }

    @AfterEach
    public void clean() {
        client.delete(null, testKey);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testListAppendCdtListIndex() {
        List<Map<String, Object>> opList = new ArrayList<>();
        Map<String, Object> opMap = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();
        opValues.put("bin", "list");
        opValues.put("value", 100);
        opValues.put("listIndex", -1);
        opMap.put(OPERATION_FIELD, AerospikeOperation.LIST_APPEND);
        opMap.put(OPERATION_VALUES_FIELD, opValues);
        opList.add(opMap);

        opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opList);
        List<Object> realList = (List<Object>) client.get(null, testKey).bins.get("list");
        l3.add(100);

        assertTrue(ASTestUtils.compareCollection(objectList, realList));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testListAppendCdtListValue() {
        List<Map<String, Object>> opList = new ArrayList<>();
        Map<String, Object> opMap = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();
        opValues.put("bin", "list");
        opValues.put("value", 100);
        opValues.put("listValue", l2);
        opMap.put(OPERATION_FIELD, AerospikeOperation.LIST_APPEND);
        opMap.put(OPERATION_VALUES_FIELD, opValues);
        opList.add(opMap);

        opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opList);
        List<Object> realList = (List<Object>) client.get(null, testKey).bins.get("list");
        l2.add(100);

        assertTrue(ASTestUtils.compareCollection(objectList, realList));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMapPutCdtMapKey() {
        List<Map<String, Object>> opList = new ArrayList<>();
        Map<String, Object> operation = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();
        opValues.put("bin", "map");
        opValues.put("key", "one");
        opValues.put("value", 11);
        opValues.put("mapKey", "m1");
        operation.put(OPERATION_FIELD, AerospikeOperation.MAP_PUT);
        operation.put(OPERATION_VALUES_FIELD, opValues);
        opList.add(operation);

        opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opList);
        Map<String, Object> bins = client.get(null, testKey).bins;
        Map<Object, Object> realMapBin = (Map<Object, Object>) bins.get("map");
        m1.put("one", 11);

        assertTrue(ASTestUtils.compareMap(realMapBin, objectMap));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMapPutCdtMapRank() {
        List<Map<String, Object>> opList = new ArrayList<>();
        Map<String, Object> operation = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();
        opValues.put("bin", "map");
        opValues.put("key", "one");
        opValues.put("value", 11);
        opValues.put("mapRank", 1);
        operation.put(OPERATION_FIELD, AerospikeOperation.MAP_PUT);
        operation.put(OPERATION_VALUES_FIELD, opValues);
        opList.add(operation);

        opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opList);
        Map<String, Object> bins = client.get(null, testKey).bins;
        Map<Object, Object> realMapBin = (Map<Object, Object>) bins.get("map");
        m2.put("one", 11);

        assertTrue(ASTestUtils.compareMap(realMapBin, objectMap));
    }
}
