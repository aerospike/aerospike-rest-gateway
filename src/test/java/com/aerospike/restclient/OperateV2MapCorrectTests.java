package com.aerospike.restclient;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.client.cdt.*;
import com.aerospike.restclient.domain.operationmodels.OperationTypes;
import com.aerospike.restclient.util.converters.OperationConverter;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(Parameterized.class)
@SpringBootTest
@AutoConfigureMockMvc
public class OperateV2MapCorrectTests {

    /* Needed to run as a Spring Boot test */
    @ClassRule
    public static final SpringClassRule springClassRule = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @Autowired
    private MockMvc mockMVC;

    @Autowired
    private AerospikeClient client;

    private final OperationV2Performer opPerformer;

    private Map<Object, Object> objectMap;
    private Map<Object, Object> objectMapInt;
    private Map<String, Object> opRequest;
    private List<Map<String, Object>> opList;
    private final String OPERATION_FIELD_TYPE = "type";
    private final String mapBinName = "map";
    private final String mapBinNameInt = "mapint";
    private final Key testKey;
    private final String testEndpoint;

    @Before
    public void setup() {
        objectMap = new HashMap<>();
        objectMap.put("one", 1);
        objectMap.put("two", 2);
        objectMap.put("three", 3);
        objectMap.put("ten", 10);
        objectMap.put("aero", "spike");
        Bin mapBin = new Bin(mapBinName, objectMap);

        // Create a map with only integer values, for rank operation simplicity
        objectMapInt = new HashMap<>();
        objectMapInt.put("one", 1);
        objectMapInt.put("two", 2);
        objectMapInt.put("three", 3);
        objectMapInt.put("ten", 10);
        objectMapInt.put("zero", 0);
        Bin mapBinInt = new Bin(mapBinNameInt, objectMapInt);
        client.put(null, testKey, mapBin, mapBinInt);
        //Key order the maps
        client.operate(null, testKey,
                MapOperation.setMapPolicy(new MapPolicy(MapOrder.KEY_ORDERED, MapWriteMode.UPDATE), mapBinName),
                MapOperation.setMapPolicy(new MapPolicy(MapOrder.KEY_ORDERED, MapWriteMode.UPDATE), mapBinNameInt));
        opList = new ArrayList<>();
        opRequest = new HashMap<>();
        opRequest.put("opsList", opList);
    }

    @After
    public void clean() {
        client.delete(null, testKey);
    }

    @Parameterized.Parameters
    public static Object[][] getParams() {
        return new Object[][]{
                {new JSONOperationV2Performer(), true},
                {new MsgPackOperationV2Performer(), true},
                {new JSONOperationV2Performer(), false},
                {new MsgPackOperationV2Performer(), false},
                };
    }

    /* Set up the correct msgpack/json performer for this set of runs */
    public OperateV2MapCorrectTests(OperationV2Performer performer, boolean useSet) {
        this.opPerformer = performer;
        if (useSet) {
            testKey = new Key("test", "junit", "mapop");
            testEndpoint = ASTestUtils.buildEndpointV2("operate", "test", "junit", "mapop");
        } else {
            testKey = new Key("test", null, "mapop");
            testEndpoint = ASTestUtils.buildEndpointV2("operate", "test", "mapop");
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMapClear() {
        Map<String, Object> operation = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();

        operation.put("binName", mapBinName);
        operation.put(OPERATION_FIELD_TYPE, OperationTypes.MAP_CLEAR);

        opList.add(operation);

        opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest);

        Map<String, Object> bins = client.get(null, testKey).bins;
        Map<String, Object> realMapBin = (Map<String, Object>) bins.get(mapBinName);
        Assert.assertEquals(realMapBin.size(), 0);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMapIncrement() {
        Map<String, Object> operation = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();

        Map<String, Object> policy = getMapPolicyMap(MapOrder.UNORDERED, MapWriteFlags.DEFAULT);

        operation.put(OperationConverter.MAP_POLICY_KEY, policy);
        operation.put("binName", mapBinName);
        operation.put("key", "ten");
        operation.put("incr", 3);
        operation.put(OPERATION_FIELD_TYPE, OperationTypes.MAP_INCREMENT);

        opList.add(operation);

        opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest);

        Map<String, Object> bins = client.get(null, testKey).bins;
        Map<Object, Object> realMapBin = (Map<Object, Object>) bins.get(mapBinName);
        objectMap.put("ten", 13);
        Assert.assertTrue(ASTestUtils.compareMap(realMapBin, objectMap));
    }

    @Test
    public void testMapGetByIndex() throws Exception {
        Map<String, Object> operation = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();

        operation.put("binName", mapBinName);
        operation.put("index", 0);
        operation.put(OperationConverter.MAP_RETURN_KEY, "KEY");
        operation.put(OPERATION_FIELD_TYPE, OperationTypes.MAP_GET_BY_INDEX);

        opList.add(operation);

        Map<String, Object> bins = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));

        Assert.assertTrue(ASTestUtils.compareSimpleValues(bins.get(mapBinName), "aero"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMapGetByIndexRange() throws Exception {
        Map<String, Object> operation = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();

        operation.put("binName", mapBinName);
        operation.put("index", 1);
        operation.put("count", 3);
        operation.put(OperationConverter.MAP_RETURN_KEY, "KEY");
        operation.put(OPERATION_FIELD_TYPE, OperationTypes.MAP_GET_BY_INDEX_RANGE);

        opList.add(operation);

        Map<String, Object> bins = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));

        List<Object> keys = (List<Object>) bins.get(mapBinName);

        Assert.assertTrue(ASTestUtils.compareCollection(keys, Arrays.asList("one", "ten", "three")));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMapGetByIndexRangeNoCount() throws Exception {
        Map<String, Object> operation = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();

        operation.put("binName", mapBinName);
        operation.put("index", 1);
        operation.put(OperationConverter.MAP_RETURN_KEY, "KEY");
        operation.put(OPERATION_FIELD_TYPE, OperationTypes.MAP_GET_BY_INDEX_RANGE);

        opList.add(operation);

        Map<String, Object> bins = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));

        List<Object> keys = (List<Object>) bins.get(mapBinName);

        Assert.assertTrue(ASTestUtils.compareCollection(keys, Arrays.asList("one", "ten", "three", "two")));
    }

    @Test
    public void testMapGetByKey() throws Exception {
        Map<String, Object> operation = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();

        operation.put("binName", mapBinName);
        operation.put("key", "three");
        operation.put(OperationConverter.MAP_RETURN_KEY, "VALUE");
        operation.put(OPERATION_FIELD_TYPE, OperationTypes.MAP_GET_BY_KEY);

        opList.add(operation);

        Map<String, Object> bins = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));

        Assert.assertTrue(ASTestUtils.compareSimpleValues(bins.get(mapBinName), 3));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMapCreate() {
        Map<String, Object> operation = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();
        Map<String, Object> item = new HashMap<>();
        List<Map<String, Object>> ctx = new ArrayList<>();

        operation.put("binName", mapBinName);
        operation.put("mapOrder", "UNORDERED");
        item.put("type", "mapKey");
        item.put("key", "key1");
        ctx.add(item);
        operation.put("ctx", ctx);
        operation.put(OPERATION_FIELD_TYPE, OperationTypes.MAP_CREATE);

        opList.add(operation);

        opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest);

        Map<String, Object> bins = client.get(null, testKey).bins;
        Map<Object, Object> mapBin = (Map<Object, Object>) bins.get(mapBinName);
        Assert.assertNotNull(mapBin.get("key1"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMapGetByKeyList() throws Exception {
        Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));
        Map<String, Object> operation = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();

        operation.put("binName", mapBinName);
        operation.put("keys", Arrays.asList("aero", "two", "three"));
        operation.put(OperationConverter.MAP_RETURN_KEY, "VALUE");
        operation.put(OPERATION_FIELD_TYPE, OperationTypes.MAP_GET_BY_KEY_LIST);

        opList.add(operation);

        Map<String, Object> bins = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));

        List<Object> retValues = (List<Object>) bins.get(mapBinName);

        Assert.assertTrue(ASTestUtils.compareCollection(retValues, Arrays.asList("spike", 3, 2)));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMapGetByKeyRange() throws Exception {
        Map<String, Object> operation = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();

        operation.put("binName", mapBinName);
        operation.put(OperationConverter.MAP_KEY_BEGIN_KEY, "one");
        // A value after "ten"
        operation.put(OperationConverter.MAP_KEY_END_KEY, "threez");
        operation.put(OperationConverter.MAP_RETURN_KEY, "VALUE");
        operation.put(OPERATION_FIELD_TYPE, OperationTypes.MAP_GET_BY_KEY_RANGE);

        opList.add(operation);

        Map<String, Object> bins = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));

        List<Object> retValues = (List<Object>) bins.get(mapBinName);

        Assert.assertTrue(ASTestUtils.compareCollection(retValues, Arrays.asList(1, 10, 3)));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMapGetByKeyRangeNoBegin() throws Exception {
        Map<String, Object> operation = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();

        operation.put("binName", mapBinName);
        // A value after "ten"
        operation.put(OperationConverter.MAP_KEY_END_KEY, "threez");
        operation.put(OperationConverter.MAP_RETURN_KEY, "VALUE");
        operation.put(OPERATION_FIELD_TYPE, OperationTypes.MAP_GET_BY_KEY_RANGE);

        opList.add(operation);

        Map<String, Object> bins = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));

        List<Object> retValues = (List<Object>) bins.get(mapBinName);

        Assert.assertTrue(ASTestUtils.compareCollection(retValues, Arrays.asList("spike", 1, 10, 3)));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMapGetByKeyRangeNoEnd() throws Exception {
        Map<String, Object> operation = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();

        operation.put("binName", mapBinName);
        operation.put(OperationConverter.MAP_KEY_BEGIN_KEY, "one");
        // A value after "ten"
        operation.put(OperationConverter.MAP_RETURN_KEY, "VALUE");
        operation.put(OPERATION_FIELD_TYPE, OperationTypes.MAP_GET_BY_KEY_RANGE);

        opList.add(operation);

        Map<String, Object> bins = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));

        List<Object> retValues = (List<Object>) bins.get(mapBinName);

        Assert.assertTrue(ASTestUtils.compareCollection(retValues, Arrays.asList(1, 10, 3, 2)));
    }

    @Test
    public void testMapGetByRank() throws Exception {

        Map<String, Object> operation = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();

        operation.put("binName", mapBinNameInt);
        operation.put("rank", 4);
        operation.put(OperationConverter.MAP_RETURN_KEY, "VALUE");
        operation.put(OPERATION_FIELD_TYPE, OperationTypes.MAP_GET_BY_RANK);

        opList.add(operation);

        Map<String, Object> bins = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));

        Assert.assertTrue(ASTestUtils.compareSimpleValues(bins.get(mapBinNameInt), 10));
    }

    @Test
    public void testMapGetByRankRange() throws Exception {

        Map<String, Object> operation = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();

        operation.put("binName", mapBinNameInt);
        operation.put("rank", 1);
        operation.put("count", 3);
        operation.put(OperationConverter.MAP_RETURN_KEY, "VALUE");
        operation.put(OPERATION_FIELD_TYPE, OperationTypes.MAP_GET_BY_RANK_RANGE);

        opList.add(operation);

        Map<String, Object> bins = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));

        @SuppressWarnings("unchecked") List<Object> retVals = (List<Object>) bins.get(mapBinNameInt);
        Assert.assertTrue(ASTestUtils.compareCollection(retVals, Arrays.asList(1, 2, 3)));
    }

    @Test
    public void testMapGetByRankRangeNoCount() throws Exception {

        Map<String, Object> operation = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();

        operation.put("binName", mapBinNameInt);
        operation.put("rank", 1);
        operation.put(OperationConverter.MAP_RETURN_KEY, "VALUE");
        operation.put(OPERATION_FIELD_TYPE, OperationTypes.MAP_GET_BY_RANK_RANGE);

        opList.add(operation);

        Map<String, Object> bins = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));

        @SuppressWarnings("unchecked") List<Object> retVals = (List<Object>) bins.get(mapBinNameInt);
        Assert.assertTrue(ASTestUtils.compareCollection(retVals, Arrays.asList(1, 2, 3, 10)));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMapGetByValue() throws Exception {

        Map<String, Object> operation = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();

        operation.put("binName", mapBinName);
        operation.put("value", 3);
        operation.put(OperationConverter.MAP_RETURN_KEY, "KEY");
        /* Store a second item with the value of 3 to show that we get all keys with the provided value*/
        objectMap.put("threez", 3);
        Bin newBin = new Bin(mapBinName, objectMap);
        client.put(null, testKey, newBin);

        operation.put(OPERATION_FIELD_TYPE, OperationTypes.MAP_GET_BY_VALUE);

        opList.add(operation);

        Map<String, Object> bins = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));

        Assert.assertTrue(
                ASTestUtils.compareCollection((List<Object>) bins.get(mapBinName), Arrays.asList("three", "threez")));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMapGetByValueRange() throws Exception {

        Map<String, Object> operation = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();

        operation.put("binName", mapBinNameInt);
        operation.put(OperationConverter.VALUE_BEGIN_KEY, 1);
        operation.put(OperationConverter.VALUE_END_KEY, 4);
        operation.put(OperationConverter.MAP_RETURN_KEY, "VALUE");

        operation.put(OPERATION_FIELD_TYPE, OperationTypes.MAP_GET_BY_VALUE_RANGE);

        opList.add(operation);

        Map<String, Object> bins = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));

        /* These keys come back in key sorted order, so "one" < "three" < "two" */
        Assert.assertTrue(ASTestUtils.containSameItems((List<Object>) bins.get(mapBinNameInt), Arrays.asList(1, 3, 2)));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMapGetByValueRangeNoBegin() throws Exception {

        Map<String, Object> operation = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();

        operation.put("binName", mapBinNameInt);
        operation.put(OperationConverter.VALUE_END_KEY, 4);
        operation.put(OperationConverter.MAP_RETURN_KEY, "VALUE");

        operation.put(OPERATION_FIELD_TYPE, OperationTypes.MAP_GET_BY_VALUE_RANGE);

        opList.add(operation);

        Map<String, Object> bins = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));

        /* These keys come back in key sorted order, so "one" < "three" < "two" < "zero" */
        Assert.assertTrue(
                ASTestUtils.containSameItems((List<Object>) bins.get(mapBinNameInt), Arrays.asList(1, 3, 2, 0)));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMapGetByValueRangeNoEnd() throws Exception {

        Map<String, Object> operation = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();

        operation.put("binName", mapBinNameInt);
        operation.put(OperationConverter.VALUE_BEGIN_KEY, 1);
        operation.put(OperationConverter.MAP_RETURN_KEY, "VALUE");

        operation.put(OPERATION_FIELD_TYPE, OperationTypes.MAP_GET_BY_VALUE_RANGE);

        opList.add(operation);

        Map<String, Object> bins = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));
        /* These keys come back in key sorted order, so "one" < "ten", "three" < "two" */
        Assert.assertTrue(
                ASTestUtils.containSameItems((List<Object>) bins.get(mapBinNameInt), Arrays.asList(1, 10, 3, 2)));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMapGetByValueList() throws Exception {
        Assume.assumeTrue(ASTestUtils.supportsNewCDT(client));

        Map<String, Object> operation = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();

        operation.put("binName", mapBinNameInt);
        operation.put("values", Arrays.asList(0, 2, 10));
        operation.put(OperationConverter.MAP_RETURN_KEY, "KEY");

        operation.put(OPERATION_FIELD_TYPE, OperationTypes.MAP_GET_BY_VALUE_LIST);

        opList.add(operation);

        Map<String, Object> bins = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));
        /* These keys come back in key sorted order, so "ten" < "two" < "zero" */
        Assert.assertTrue(ASTestUtils.containSameItems((List<Object>) bins.get(mapBinNameInt),
                Arrays.asList("ten", "two", "zero")));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMapGetByKeyRelIndexRange() throws Exception {
        Map<String, Object> operation = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();

        operation.put("binName", mapBinNameInt);
        operation.put("value", "one");
        operation.put("index", 1);

        operation.put(OperationConverter.MAP_RETURN_KEY, "KEY");
        operation.put(OPERATION_FIELD_TYPE, OperationTypes.MAP_GET_BY_KEY_RELATIVE_INDEX_RANGE);

        opList.add(operation);

        Map<String, Object> bins = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));
        List<Object> keys = (List<Object>) bins.get(mapBinNameInt);
        Assert.assertTrue(ASTestUtils.compareCollection(keys, Arrays.asList("ten", "three", "two", "zero")));

        operation.put("count", 3);
        bins = getReturnedBins(opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));
        keys = (List<Object>) bins.get(mapBinNameInt);
        Assert.assertTrue(ASTestUtils.compareCollection(keys, Arrays.asList("ten", "three", "two")));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMapGetByValueRelRankRange() throws Exception {

        Map<String, Object> operation = new HashMap<>();

        operation.put("binName", mapBinNameInt);
        operation.put("value", 1);
        operation.put("rank", -1);
        operation.put(OperationConverter.MAP_RETURN_KEY, "VALUE");
        operation.put(OPERATION_FIELD_TYPE, OperationTypes.MAP_GET_BY_VALUE_RELATIVE_RANK_RANGE);

        opList.add(operation);

        Map<String, Object> bins = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));
        List<Object> retVals = (List<Object>) bins.get(mapBinNameInt);
        Assert.assertTrue(ASTestUtils.compareCollection(retVals, Arrays.asList(0, 1, 2, 3, 10)));

        operation.put("count", 3);
        bins = getReturnedBins(opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));
        retVals = (List<Object>) bins.get(mapBinNameInt);
        Assert.assertTrue(ASTestUtils.compareCollection(retVals, Arrays.asList(0, 1, 2)));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMapPut() {
        Map<String, Object> operation = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();

        Map<String, Object> policy = getMapPolicyMap(MapOrder.UNORDERED, MapWriteFlags.DEFAULT);

        operation.put(OperationConverter.MAP_POLICY_KEY, policy);
        operation.put("binName", mapBinName);
        operation.put("key", "five");
        operation.put("value", 5);
        operation.put(OPERATION_FIELD_TYPE, OperationTypes.MAP_PUT);

        opList.add(operation);

        opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest);

        Map<String, Object> bins = client.get(null, testKey).bins;
        Map<Object, Object> realMapBin = (Map<Object, Object>) bins.get(mapBinName);
        objectMap.put("five", 5);
        Assert.assertTrue(ASTestUtils.compareMap(realMapBin, objectMap));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMapPutItems() {
        Map<String, Object> operation = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();

        Map<String, Object> policy = getMapPolicyMap(MapOrder.UNORDERED, MapWriteFlags.DEFAULT);

        Map<Object, Object> putValues = new HashMap<>();
        putValues.put("five", 5);
        putValues.put("six", 6);
        putValues.put("list", Arrays.asList(1, 2, 3));

        objectMap.put("five", 5);
        objectMap.put("six", 6);
        objectMap.put("list", Arrays.asList(1, 2, 3));

        operation.put("map", putValues);
        operation.put(OperationConverter.MAP_POLICY_KEY, policy);
        operation.put("binName", mapBinName);

        operation.put(OPERATION_FIELD_TYPE, OperationTypes.MAP_PUT_ITEMS);

        opList.add(operation);

        opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest);

        Map<String, Object> bins = client.get(null, testKey).bins;
        Map<Object, Object> realMapBin = (Map<Object, Object>) bins.get(mapBinName);

        Assert.assertTrue(ASTestUtils.compareMap(realMapBin, objectMap));
    }

    @Test
    public void testMapRemoveByIndex() throws Exception {
        Map<String, Object> operation = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();

        operation.put("binName", mapBinName);
        operation.put("index", 0);
        operation.put(OperationConverter.MAP_RETURN_KEY, "KEY");
        operation.put(OPERATION_FIELD_TYPE, OperationTypes.MAP_REMOVE_BY_INDEX);

        opList.add(operation);

        Map<String, Object> bins = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));
        Assert.assertTrue(ASTestUtils.compareSimpleValues(bins.get(mapBinName), "aero"));

        Map<String, Object> realBins = client.get(null, testKey).bins;
        @SuppressWarnings("unchecked") Map<Object, Object> realMapBin = (Map<Object, Object>) realBins.get(mapBinName);
        Assert.assertFalse(realMapBin.containsKey("aero"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMapRemoveByIndexRange() throws Exception {
        Map<String, Object> operation = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();

        operation.put("binName", mapBinName);
        operation.put("index", 0);
        operation.put("count", 3);
        operation.put(OperationConverter.MAP_RETURN_KEY, "KEY");
        operation.put(OPERATION_FIELD_TYPE, OperationTypes.MAP_REMOVE_BY_INDEX_RANGE);

        opList.add(operation);

        Map<String, Object> bins = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));

        List<Object> retVals = (List<Object>) bins.get(mapBinName);

        Assert.assertTrue(ASTestUtils.compareCollection(retVals, Arrays.asList("aero", "one", "ten")));

        Map<String, Object> realBins = client.get(null, testKey).bins;
        Map<Object, Object> realMapBin = (Map<Object, Object>) realBins.get(mapBinName);
        Assert.assertFalse(realMapBin.containsKey("aero"));
        Assert.assertFalse(realMapBin.containsKey("one"));
        Assert.assertFalse(realMapBin.containsKey("ten"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMapRemoveByIndexRangeNoCount() throws Exception {
        Map<String, Object> operation = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();

        operation.put("binName", mapBinName);
        operation.put("index", 0);
        operation.put(OperationConverter.MAP_RETURN_KEY, "KEY");
        operation.put(OPERATION_FIELD_TYPE, OperationTypes.MAP_REMOVE_BY_INDEX_RANGE);

        opList.add(operation);

        Map<String, Object> bins = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));

        List<Object> retVals = (List<Object>) bins.get(mapBinName);
        Assert.assertTrue(ASTestUtils.compareCollection(retVals, Arrays.asList("aero", "one", "ten", "three", "two")));

        Map<String, Object> realBins = client.get(null, testKey).bins;
        Map<Object, Object> realMapBin = (Map<Object, Object>) realBins.get(mapBinName);
        Assert.assertEquals(realMapBin.size(), 0);
    }

    @Test
    public void testMapRemoveByKey() throws Exception {
        Map<String, Object> operation = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();

        operation.put("binName", mapBinName);
        operation.put("key", "two");
        operation.put(OperationConverter.MAP_RETURN_KEY, "VALUE");
        operation.put(OPERATION_FIELD_TYPE, OperationTypes.MAP_REMOVE_BY_KEY);

        opList.add(operation);

        Map<String, Object> bins = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));

        Assert.assertTrue(ASTestUtils.compareSimpleValues(bins.get(mapBinName), 2));

        Map<String, Object> realBins = client.get(null, testKey).bins;
        @SuppressWarnings("unchecked") Map<Object, Object> realMapBin = (Map<Object, Object>) realBins.get(mapBinName);
        Assert.assertFalse(realMapBin.containsKey("two"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMapRemoveByKeyRange() throws Exception {
        Map<String, Object> operation = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();

        operation.put("binName", mapBinName);
        operation.put(OperationConverter.MAP_KEY_BEGIN_KEY, "one");
        // A value after "three"
        operation.put(OperationConverter.MAP_KEY_END_KEY, "threez");
        operation.put(OperationConverter.MAP_RETURN_KEY, "VALUE");
        operation.put(OPERATION_FIELD_TYPE, OperationTypes.MAP_REMOVE_BY_KEY_RANGE);

        opList.add(operation);

        Map<String, Object> bins = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));

        List<Object> retValues = (List<Object>) bins.get(mapBinName);
        Assert.assertTrue(ASTestUtils.compareCollection(retValues, Arrays.asList(1, 10, 3)));

        Map<String, Object> realBins = client.get(null, testKey).bins;
        Map<Object, Object> realMapBin = (Map<Object, Object>) realBins.get(mapBinName);
        Assert.assertFalse(realMapBin.containsKey("one"));
        Assert.assertFalse(realMapBin.containsKey("ten"));
        Assert.assertFalse(realMapBin.containsKey("three"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMapRemoveByKeyRangeNoBegin() throws Exception {
        Map<String, Object> operation = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();

        operation.put("binName", mapBinName);

        // A value after "three"
        operation.put(OperationConverter.MAP_KEY_END_KEY, "threez");
        operation.put(OperationConverter.MAP_RETURN_KEY, "VALUE");
        operation.put(OPERATION_FIELD_TYPE, OperationTypes.MAP_REMOVE_BY_KEY_RANGE);

        opList.add(operation);

        Map<String, Object> bins = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));

        List<Object> retValues = (List<Object>) bins.get(mapBinName);
        Assert.assertTrue(ASTestUtils.compareCollection(retValues, Arrays.asList("spike", 1, 10, 3)));

        Map<String, Object> realBins = client.get(null, testKey).bins;
        Map<Object, Object> realMapBin = (Map<Object, Object>) realBins.get(mapBinName);
        Assert.assertFalse(realMapBin.containsKey("aero"));
        Assert.assertFalse(realMapBin.containsKey("one"));
        Assert.assertFalse(realMapBin.containsKey("ten"));
        Assert.assertFalse(realMapBin.containsKey("three"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMapRemoveByKeyRangeNoEnd() throws Exception {
        Map<String, Object> operation = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();

        operation.put("binName", mapBinName);
        operation.put(OperationConverter.MAP_KEY_BEGIN_KEY, "one");
        // A value after "three"
        operation.put(OperationConverter.MAP_RETURN_KEY, "VALUE");
        operation.put(OPERATION_FIELD_TYPE, OperationTypes.MAP_REMOVE_BY_KEY_RANGE);

        opList.add(operation);

        Map<String, Object> bins = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));

        List<Object> retValues = (List<Object>) bins.get(mapBinName);
        Assert.assertTrue(ASTestUtils.compareCollection(retValues, Arrays.asList(1, 10, 3, 2)));

        Map<String, Object> realBins = client.get(null, testKey).bins;
        Map<Object, Object> realMapBin = (Map<Object, Object>) realBins.get(mapBinName);
        Assert.assertFalse(realMapBin.containsKey("one"));
        Assert.assertFalse(realMapBin.containsKey("ten"));
        Assert.assertFalse(realMapBin.containsKey("three"));
        Assert.assertFalse(realMapBin.containsKey("two"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMapRemoveByRank() throws Exception {

        Map<String, Object> operation = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();

        operation.put("binName", mapBinNameInt);
        operation.put("rank", 2);
        operation.put(OperationConverter.MAP_RETURN_KEY, "VALUE");
        operation.put(OPERATION_FIELD_TYPE, OperationTypes.MAP_REMOVE_BY_RANK);

        opList.add(operation);

        Map<String, Object> bins = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));

        Assert.assertTrue(ASTestUtils.compareSimpleValues(bins.get(mapBinNameInt), 2));

        Map<String, Object> realBins = client.get(null, testKey).bins;
        Map<Object, Object> realMapBin = (Map<Object, Object>) realBins.get(mapBinNameInt);

        Assert.assertFalse(realMapBin.containsKey("two"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMapRemoveByRankRange() throws Exception {

        Map<String, Object> operation = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();

        operation.put("binName", mapBinNameInt);
        operation.put("rank", 1);
        operation.put("count", 3);
        operation.put(OperationConverter.MAP_RETURN_KEY, "VALUE");
        operation.put(OPERATION_FIELD_TYPE, OperationTypes.MAP_REMOVE_BY_RANK_RANGE);

        opList.add(operation);

        Map<String, Object> bins = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));

        List<Object> retVals = (List<Object>) bins.get(mapBinNameInt);

        Assert.assertTrue(ASTestUtils.compareCollection(retVals, Arrays.asList(1, 2, 3)));

        Map<String, Object> realBins = client.get(null, testKey).bins;
        Map<Object, Object> realMapBin = (Map<Object, Object>) realBins.get(mapBinNameInt);

        objectMapInt.remove("one");
        objectMapInt.remove("three");
        objectMapInt.remove("two");

        Assert.assertTrue(ASTestUtils.compareMap(objectMapInt, realMapBin));

    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMapRemoveByRankRangeNoCount() throws Exception {

        Map<String, Object> operation = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();

        operation.put("binName", mapBinNameInt);
        operation.put("rank", 1);
        operation.put(OperationConverter.MAP_RETURN_KEY, "VALUE");
        operation.put(OPERATION_FIELD_TYPE, OperationTypes.MAP_REMOVE_BY_RANK_RANGE);

        opList.add(operation);

        Map<String, Object> bins = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));

        List<Object> retVals = (List<Object>) bins.get(mapBinNameInt);

        Assert.assertTrue(ASTestUtils.compareCollection(retVals, Arrays.asList(1, 2, 3, 10)));

        Map<String, Object> realBins = client.get(null, testKey).bins;
        Map<Object, Object> realMapBin = (Map<Object, Object>) realBins.get(mapBinNameInt);

        objectMapInt.remove("one");
        objectMapInt.remove("three");
        objectMapInt.remove("two");
        objectMapInt.remove("ten");
        Assert.assertTrue(ASTestUtils.compareMap(objectMapInt, realMapBin));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMapRemoveByKeyRelIndexRange() throws Exception {
        Map<String, Object> operation = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();

        operation.put("binName", mapBinNameInt);
        operation.put("value", "one");
        operation.put("index", 1);
        operation.put("count", 2);
        operation.put(OperationConverter.MAP_RETURN_KEY, "KEY");
        operation.put(OPERATION_FIELD_TYPE, OperationTypes.MAP_REMOVE_BY_KEY_RELATIVE_INDEX_RANGE);

        opList.add(operation);

        Map<String, Object> bins = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));
        List<Object> retVals = (List<Object>) bins.get(mapBinNameInt);
        Assert.assertTrue(ASTestUtils.compareCollection(retVals, Arrays.asList("ten", "three")));

        Map<String, Object> realBins = client.get(null, testKey).bins;
        Map<Object, Object> realMapBin = (Map<Object, Object>) realBins.get(mapBinNameInt);
        Assert.assertFalse(realMapBin.containsKey("ten"));
        Assert.assertFalse(realMapBin.containsKey("three"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMapRemoveByValueRelRankRange() throws Exception {

        Map<String, Object> operation = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();

        operation.put("binName", mapBinNameInt);
        operation.put("value", 10);
        operation.put("rank", -1);
        operation.put(OperationConverter.MAP_RETURN_KEY, "VALUE");
        operation.put(OPERATION_FIELD_TYPE, OperationTypes.MAP_REMOVE_BY_VALUE_RELATIVE_RANK_RANGE);

        opList.add(operation);

        Map<String, Object> bins = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));
        List<Object> retVals = (List<Object>) bins.get(mapBinNameInt);
        Assert.assertTrue(ASTestUtils.compareCollection(retVals, Arrays.asList(3, 10)));

        Map<String, Object> realBins = client.get(null, testKey).bins;
        Map<Object, Object> realMapBin = (Map<Object, Object>) realBins.get(mapBinNameInt);
        objectMapInt.remove("three");
        objectMapInt.remove("ten");
        Assert.assertTrue(ASTestUtils.compareMap(objectMapInt, realMapBin));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMapRemoveByValue() throws Exception {

        Map<String, Object> operation = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();

        operation.put("binName", mapBinName);
        operation.put("value", 3);
        operation.put(OperationConverter.MAP_RETURN_KEY, "KEY");
        /* Store a second item with the value of 3 to show that we get all keys with the provided value*/
        objectMap.put("threez", 3);
        Bin newBin = new Bin(mapBinName, objectMap);
        client.put(null, testKey, newBin);

        operation.put(OPERATION_FIELD_TYPE, OperationTypes.MAP_REMOVE_BY_VALUE);

        opList.add(operation);

        Map<String, Object> bins = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));

        Assert.assertTrue(
                ASTestUtils.compareCollection((List<Object>) bins.get(mapBinName), Arrays.asList("three", "threez")));

        Map<String, Object> realBins = client.get(null, testKey).bins;
        Map<Object, Object> realMapBin = (Map<Object, Object>) realBins.get(mapBinName);

        objectMap.remove("three");
        objectMap.remove("threez");
        Assert.assertTrue(ASTestUtils.compareMap(objectMap, realMapBin));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMapRemoveByValueRange() throws Exception {

        Map<String, Object> operation = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();

        operation.put("binName", mapBinNameInt);
        operation.put(OperationConverter.VALUE_BEGIN_KEY, 1);
        operation.put(OperationConverter.VALUE_END_KEY, 4);
        operation.put(OperationConverter.MAP_RETURN_KEY, "VALUE");

        operation.put(OPERATION_FIELD_TYPE, OperationTypes.MAP_REMOVE_BY_VALUE_RANGE);

        opList.add(operation);

        Map<String, Object> bins = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));
        /* These keys come back in key sorted order, so "one" < "three" < "two" */
        Assert.assertTrue(ASTestUtils.containSameItems((List<Object>) bins.get(mapBinNameInt), Arrays.asList(1, 3, 2)));

        Map<String, Object> realBins = client.get(null, testKey).bins;
        Map<Object, Object> realMapBin = (Map<Object, Object>) realBins.get(mapBinNameInt);

        objectMapInt.remove("one");
        objectMapInt.remove("two");
        objectMapInt.remove("three");
        Assert.assertTrue(ASTestUtils.compareMap(objectMapInt, realMapBin));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMapRemoveByValueRangeNoBegin() throws Exception {

        Map<String, Object> operation = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();

        operation.put("binName", mapBinNameInt);
        operation.put(OperationConverter.VALUE_END_KEY, 4);
        operation.put(OperationConverter.MAP_RETURN_KEY, "VALUE");

        operation.put(OPERATION_FIELD_TYPE, OperationTypes.MAP_REMOVE_BY_VALUE_RANGE);

        opList.add(operation);

        Map<String, Object> bins = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));
        /* These keys come back in key sorted order, so "one" < "three" < "two" < "zero"*/
        Assert.assertTrue(
                ASTestUtils.containSameItems((List<Object>) bins.get(mapBinNameInt), Arrays.asList(1, 3, 2, 0)));

        Map<String, Object> realBins = client.get(null, testKey).bins;
        Map<Object, Object> realMapBin = (Map<Object, Object>) realBins.get(mapBinNameInt);

        objectMapInt.remove("zero");
        objectMapInt.remove("one");
        objectMapInt.remove("two");
        objectMapInt.remove("three");
        Assert.assertTrue(ASTestUtils.compareMap(objectMapInt, realMapBin));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMapRemoveByValueRangeNoEnd() throws Exception {

        Map<String, Object> operation = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();

        operation.put("binName", mapBinNameInt);
        operation.put(OperationConverter.VALUE_BEGIN_KEY, 1);
        operation.put(OperationConverter.MAP_RETURN_KEY, "VALUE");

        operation.put(OPERATION_FIELD_TYPE, OperationTypes.MAP_REMOVE_BY_VALUE_RANGE);

        opList.add(operation);

        Map<String, Object> bins = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));
        /* These keys come back in key sorted order, so "one" < "ten", "three" < "two" */
        Assert.assertTrue(
                ASTestUtils.containSameItems((List<Object>) bins.get(mapBinNameInt), Arrays.asList(1, 10, 3, 2)));

        Map<String, Object> realBins = client.get(null, testKey).bins;
        Map<Object, Object> realMapBin = (Map<Object, Object>) realBins.get(mapBinNameInt);

        objectMapInt.remove("one");
        objectMapInt.remove("two");
        objectMapInt.remove("three");
        objectMapInt.remove("ten");
        Assert.assertTrue(ASTestUtils.compareMap(objectMapInt, realMapBin));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMapRemoveByValueList() throws Exception {

        Map<String, Object> operation = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();

        operation.put("binName", mapBinNameInt);
        operation.put("values", Arrays.asList(1, 2, 3));
        operation.put(OperationConverter.MAP_RETURN_KEY, "KEY");

        operation.put(OPERATION_FIELD_TYPE, OperationTypes.MAP_REMOVE_BY_VALUE_LIST);

        opList.add(operation);

        Map<String, Object> bins = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));
        Assert.assertTrue(ASTestUtils.containSameItems((List<Object>) bins.get(mapBinNameInt),
                Arrays.asList("one", "three", "two")));

        Map<String, Object> realBins = client.get(null, testKey).bins;
        Map<Object, Object> realMapBin = (Map<Object, Object>) realBins.get(mapBinNameInt);

        objectMapInt.remove("one");
        objectMapInt.remove("two");
        objectMapInt.remove("three");
        Assert.assertTrue(ASTestUtils.compareMap(objectMapInt, realMapBin));
    }

    /*
     * Test that a create_only map write flag prevents updating an existing value
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testMapWriteFlagsCreateOnly() throws Exception {
        Map<String, Object> policyMap = getMapPolicyMap(MapOrder.UNORDERED, MapWriteFlags.CREATE_ONLY);
        Map<String, Object> operation = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();
        String newKey = "new_key";
        String newVal = "new_value";
        Map<Object, Object> expected = new HashMap<>();
        expected.put(newKey, newVal);

        operation.put("binName", mapBinName);
        operation.put("key", newKey);
        operation.put("value", newVal);
        operation.put(OperationConverter.MAP_POLICY_KEY, policyMap);
        operation.put(OPERATION_FIELD_TYPE, OperationTypes.MAP_PUT);

        opList.add(operation);

        /* This should succeed because we are doing a create only on a new value */
        opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest);
        Map<String, Object> bins = client.get(null, testKey).bins;
        Map<Object, Object> realMapBin = (Map<Object, Object>) bins.get(mapBinName);

        Assert.assertEquals(newVal, realMapBin.get(newKey));

        opPerformer.performOperationsAndExpect(mockMVC, testEndpoint, opRequest, status().isInternalServerError());
    }

    /*
     * Test that an update only map operation cannot add a field to the map.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testMapWriteFlagsUpdateOnlyError() throws Exception {
        Map<String, Object> policyMap = getMapPolicyMap(MapOrder.UNORDERED, MapWriteFlags.UPDATE_ONLY);
        Map<String, Object> operation = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();
        String newKey = "new_key";
        String newVal = "new_value";
        Map<Object, Object> expected = new HashMap<>();
        expected.put(newKey, newVal);

        operation.put("binName", mapBinName);
        operation.put("key", newKey);
        operation.put("value", newVal);
        operation.put(OperationConverter.MAP_POLICY_KEY, policyMap);
        operation.put(OPERATION_FIELD_TYPE, OperationTypes.MAP_PUT);

        opList.add(operation);
        opPerformer.performOperationsAndExpect(mockMVC, testEndpoint, opRequest, status().isInternalServerError());
        Map<String, Object> bins = client.get(null, testKey).bins;
        Map<Object, Object> realMapBin = (Map<Object, Object>) bins.get(mapBinName);
        Assert.assertNull(newVal, realMapBin.get(newKey));
    }

    /*
     * Test that a map put operation with update only can update an existing item.
     */
    @Test
    public void testMapWriteFlagsUpdateOnlyNoError() {
        Map<String, Object> policyMap = getMapPolicyMap(MapOrder.UNORDERED, MapWriteFlags.UPDATE_ONLY);
        Map<String, Object> operation = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();
        String existingKey = "aero";
        String newVal = "new_value";
        Map<Object, Object> expected = new HashMap<>();
        expected.put(existingKey, newVal);

        operation.put("binName", mapBinName);
        operation.put("key", existingKey);
        operation.put("value", newVal);
        operation.put(OperationConverter.MAP_POLICY_KEY, policyMap);
        operation.put(OPERATION_FIELD_TYPE, OperationTypes.MAP_PUT);

        opList.add(operation);
        opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest);
        Map<String, Object> bins = client.get(null, testKey).bins;
        @SuppressWarnings("unchecked") Map<Object, Object> realMapBin = (Map<Object, Object>) bins.get(mapBinName);

        Assert.assertEquals(newVal, realMapBin.get(existingKey));
    }

    /*
     * The map currently has {"aero":"spike"}
     * we attempt to do an update with {"aero"=>"new_val", "new_key"=>"new_val"}
     * along with the update_only partial no_fail flags
     * We expect the operation to not raise an error
     * and the resulting map to not have new_key, and to contain {"aero"=>"new_val"}
     */
    @Test
    public void testMapWriteFlagsUpdateOnlyPartialNoFail() {
        Map<String, Object> policyMap = getMapPolicyMap(MapOrder.UNORDERED,
                MapWriteFlags.UPDATE_ONLY | MapWriteFlags.PARTIAL | MapWriteFlags.NO_FAIL);
        Map<String, Object> operation = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();
        String existingKey = "aero";
        String newKey = "new_key";
        String newVal = "new_value";
        Map<Object, Object> expected = new HashMap<>();
        expected.put(existingKey, newVal);

        operation.put("binName", mapBinName);
        operation.put(OperationConverter.MAP_POLICY_KEY, policyMap);

        Map<Object, Object> putValues = new HashMap<>();
        putValues.put(existingKey, newVal);
        putValues.put(newKey, newVal);
        operation.put("map", putValues);

        operation.put(OPERATION_FIELD_TYPE, OperationTypes.MAP_PUT_ITEMS);

        opList.add(operation);
        opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest);
        Map<String, Object> bins = client.get(null, testKey).bins;
        @SuppressWarnings("unchecked") Map<Object, Object> realMapBin = (Map<Object, Object>) bins.get(mapBinName);

        Assert.assertEquals(newVal, realMapBin.get(existingKey));
        Assert.assertNull(realMapBin.get(newKey));
    }

    @Test
    public void testMapSize() throws Exception {
        Map<String, Object> operation = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();

        operation.put("binName", mapBinName);
        operation.put(OPERATION_FIELD_TYPE, OperationTypes.MAP_SIZE);

        opList.add(operation);

        Map<String, Object> bins = getReturnedBins(
                opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest));
        Assert.assertTrue(ASTestUtils.compareSimpleValues(bins.get(mapBinName), objectMap.size()));
    }

    private Map<String, Object> getMapPolicyMap(MapOrder order, int flags) {
        Map<String, Object> policyMap = new HashMap<>();
        String orderString;
        List<String> writeFlags = new ArrayList<>();
        switch (order) {
            case KEY_VALUE_ORDERED:
                orderString = "KEY_VALUE_ORDERED";
                break;
            case KEY_ORDERED:
                orderString = "KEY_ORDERED";
                break;
            case UNORDERED:
            default:
                orderString = "UNORDERED";
        }

        if ((flags & MapWriteFlags.CREATE_ONLY) != 0) {
            writeFlags.add("CREATE_ONLY");
        }

        if ((flags & MapWriteFlags.UPDATE_ONLY) != 0) {
            writeFlags.add("UPDATE_ONLY");
        }

        if ((flags & MapWriteFlags.PARTIAL) != 0) {
            writeFlags.add("PARTIAL");
        }

        if ((flags & MapWriteFlags.NO_FAIL) != 0) {
            writeFlags.add("NO_FAIL");
        }

        policyMap.put("order", orderString);
        policyMap.put(OperationConverter.WRITE_FLAGS_KEY, writeFlags);
        return policyMap;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getReturnedBins(Map<String, Object> rec) throws Exception {
        return (Map<String, Object>) rec.get("bins");
    }
}

