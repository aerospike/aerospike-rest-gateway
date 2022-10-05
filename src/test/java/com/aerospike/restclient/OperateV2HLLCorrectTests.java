package com.aerospike.restclient;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.client.Value;
import com.aerospike.client.operation.HLLOperation;
import com.aerospike.client.operation.HLLPolicy;
import com.aerospike.restclient.util.AerospikeOperation;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

@RunWith(Parameterized.class)
@SpringBootTest
public class OperateV2HLLCorrectTests {

    @ClassRule
    public static final SpringClassRule springClassRule = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    private MockMvc mockMVC;

    @Autowired
    private AerospikeClient client;

    @Autowired
    private WebApplicationContext wac;

    private final Key testKey;
    private final String testEndpoint;
    private final String OPERATION_TYPE_KEY = "type";

    private Map<String, Object> opRequest;
    private List<Map<String, Object>> opList;
    private final List<Object> values = Arrays.asList("a", "b", "c", "d", "e", "f", "g", "g");
    private final List<Object> values2 = Arrays.asList("a", "b", "c", "d", "e", "h", "i", "j");

    @Before
    public void setup() {
        mockMVC = MockMvcBuilders.webAppContextSetup(wac).build();
        client.operate(null, testKey, HLLOperation.init(HLLPolicy.Default, "hll", 8, 8));
        client.operate(null, testKey, HLLOperation.init(HLLPolicy.Default, "hll2", 8, 8));
        opList = new ArrayList<>();
        opRequest = new HashMap<>();
        opRequest.put("opsList", opList);
    }

    @After
    public void clean() {
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
    public OperateV2HLLCorrectTests(OperationV2Performer performer, boolean useSet) {
        this.opPerformer = performer;
        if (useSet) {
            testKey = new Key("test", "junit", "hllop");
            testEndpoint = ASTestUtils.buildEndpointV2("operate", "test", "junit", "hllop");
        } else {
            testKey = new Key("test", null, "hllop");
            testEndpoint = ASTestUtils.buildEndpointV2("operate", "test", "hllop");
        }
    }

    @Test
    public void testHLLAdd() {
        createHLLBin("hll", values);

        Record record = client.operate(null, testKey, HLLOperation.getCount("hll"));
        long count = (long) record.bins.get("hll");

        Assert.assertEquals(7, count);
    }

    @Test
    public void testHLLSetUnion() {
        createHLLBin("hll", values);
        createHLLBin("hll2", values2);

        Value.HLLValue hll2Bin = (Value.HLLValue) client.get(null, testKey).bins.get("hll2");

        Map<String, Object> opMap = new HashMap<>();

        opMap.put("binName", "hll");
        opMap.put("values", Collections.singletonList(Base64.getEncoder().encodeToString(hll2Bin.getBytes())));
        opMap.put(OPERATION_TYPE_KEY, AerospikeOperation.HLL_SET_UNION);
        opList.add(opMap);

        opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest);

        Record record = client.operate(null, testKey, HLLOperation.getCount("hll"));
        long count = (long) record.bins.get("hll");

        Assert.assertEquals(10, count);
    }

    @Test
    public void testHLLRefreshCount() {
        createHLLBin("hll", values);

        Map<String, Object> opMap = new HashMap<>();

        opMap.put("binName", "hll");
        opMap.put(OPERATION_TYPE_KEY, AerospikeOperation.HLL_SET_COUNT);
        opList.add(opMap);

        opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest);

        Record record = client.operate(null, testKey, HLLOperation.getCount("hll"));
        long count = (long) record.bins.get("hll");

        Assert.assertEquals(7, count);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testHLLGetUnion() {
        createHLLBin("hll", values);
        createHLLBin("hll2", values2);

        Value.HLLValue hll2Bin = (Value.HLLValue) client.get(null, testKey).bins.get("hll2");

        Map<String, Object> opMap = new HashMap<>();

        opMap.put("binName", "hll");
        opMap.put("values", Collections.singletonList(Base64.getEncoder().encodeToString(hll2Bin.getBytes())));
        opMap.put(OPERATION_TYPE_KEY, AerospikeOperation.HLL_UNION);
        opList.add(opMap);

        Map<String, Object> res = opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest);
        byte[] expected;
        try {
            expected = Base64.getDecoder()
                    .decode(((Map<String, Map<String, String>>) res.get("bins")).get("hll").get("object").getBytes());
        } catch (ClassCastException e) {
            expected = (byte[]) ((Map<String, Map<String, Object>>) res.get("bins")).get("hll").get("object");
        }
        Value.HLLValue value = new Value.HLLValue(expected);
        Assert.assertNotNull(value);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testHLLGetUnionCount() {
        createHLLBin("hll", values);
        createHLLBin("hll2", values2);

        Value.HLLValue hll2Bin = (Value.HLLValue) client.get(null, testKey).bins.get("hll2");

        Map<String, Object> opMap = new HashMap<>();

        opMap.put("binName", "hll");
        opMap.put("values", Collections.singletonList(Base64.getEncoder().encodeToString(hll2Bin.getBytes())));
        opMap.put(OPERATION_TYPE_KEY, AerospikeOperation.HLL_UNION_COUNT);
        opList.add(opMap);

        Map<String, Object> res = opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest);
        Integer count = ((Map<String, Integer>) res.get("bins")).get("hll");
        Assert.assertEquals(10, count.intValue());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testHLLGetIntersectCount() {
        createHLLBin("hll", values);
        createHLLBin("hll2", values2);

        Value.HLLValue hll2Bin = (Value.HLLValue) client.get(null, testKey).bins.get("hll2");

        Map<String, Object> opMap = new HashMap<>();

        opMap.put("binName", "hll");
        opMap.put("values", Collections.singletonList(Base64.getEncoder().encodeToString(hll2Bin.getBytes())));
        opMap.put(OPERATION_TYPE_KEY, AerospikeOperation.HLL_INTERSECT_COUNT);
        opList.add(opMap);

        Map<String, Object> res = opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest);
        Integer count = ((Map<String, Integer>) res.get("bins")).get("hll");
        Assert.assertEquals(5, count.intValue());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testHLLGetSimilarity() {
        createHLLBin("hll", values);
        createHLLBin("hll2", values2);

        Value.HLLValue hll2Bin = (Value.HLLValue) client.get(null, testKey).bins.get("hll2");

        Map<String, Object> opMap = new HashMap<>();

        opMap.put("binName", "hll");
        opMap.put("values", Collections.singletonList(Base64.getEncoder().encodeToString(hll2Bin.getBytes())));
        opMap.put(OPERATION_TYPE_KEY, AerospikeOperation.HLL_SIMILARITY);
        opList.add(opMap);

        Map<String, Object> res = opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest);
        Double count = ((Map<String, Double>) res.get("bins")).get("hll");
        Assert.assertEquals(0.5, count, 0.005);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testHLLDescribe() {
        createHLLBin("hll", values);

        Map<String, Object> opMap = new HashMap<>();

        opMap.put("binName", "hll");
        opMap.put(OPERATION_TYPE_KEY, AerospikeOperation.HLL_DESCRIBE);
        opList.add(opMap);

        Map<String, Object> res = opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest);
        List<Integer> values = ((Map<String, List<Integer>>) res.get("bins")).get("hll");
        Assert.assertEquals(Arrays.asList(8, 8), values);
    }

    private void createHLLBin(String binName, List<Object> values) {
        Map<String, Object> opRequest = new HashMap<>();
        Map<String, Object> opMap = new HashMap<>();
        List<Map<String, Object>> opList = new ArrayList<>();

        opMap.put("binName", binName);
        opMap.put("values", values);
        opMap.put(OPERATION_TYPE_KEY, AerospikeOperation.HLL_ADD);

        opList.add(opMap);
        opRequest.put("opsList", opList);
        opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest);
    }

}
