package com.aerospike.restclient;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.client.Value;
import com.aerospike.client.policy.WritePolicy;
import com.aerospike.client.query.IndexType;
import com.aerospike.client.task.IndexTask;
import com.aerospike.restclient.config.JSONMessageConverter;
import com.aerospike.restclient.config.MsgPackConverter;
import com.aerospike.restclient.domain.RestClientKeyRecord;
import com.aerospike.restclient.domain.geojsonmodels.LngLat;
import com.aerospike.restclient.domain.querymodels.QueryEqualLongFilter;
import com.aerospike.restclient.domain.querymodels.QueryGeoContainsPointFilter;
import com.aerospike.restclient.domain.querymodels.QueryRequestBody;
import com.aerospike.restclient.domain.querymodels.QueryResponseBody;
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

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

@RunWith(Parameterized.class)
@SpringBootTest
public class QueryTestCorrect {

    @ClassRule
    public static final SpringClassRule springClassRule = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    private MockMvc mockMVC;

    @Autowired
    private AerospikeClient client;

    @Autowired
    private WebApplicationContext wac;

    private static final int numberOfRecords = 501;
    private final Key[] testKeys;
    private final String testEndpoint;

    private final String setName;
    private final String namespace;

    private boolean first = true;

    @Before
    public void setup() throws InterruptedException {
        mockMVC = MockMvcBuilders.webAppContextSetup(wac).build();
        WritePolicy writePolicy = new WritePolicy();
        writePolicy.sendKey = true;
        writePolicy.totalTimeout = 0;
        IndexTask binTask;
        IndexTask geoTask;

        // Need JUnit5 to use Autowired client in @BeforeAll
        if (first) {
            client.truncate(null, "test", null, null);
            first = false;
        }

        try {
            binTask = client.dropIndex(writePolicy, namespace, setName, "binInt-index");
            geoTask = client.dropIndex(writePolicy, namespace, setName, "binGeo-index");
            binTask.waitTillComplete();
            geoTask.waitTillComplete();
        } catch (Exception ignored) {

        }

        binTask = client.createIndex(writePolicy, namespace, setName, "binInt-index", "binInt", IndexType.NUMERIC);
        geoTask = client.createIndex(writePolicy, namespace, setName, "binGeo-index", "binGeo", IndexType.GEO2DSPHERE);
        binTask.waitTillComplete();
        geoTask.waitTillComplete();

        for (int i = 0; i < numberOfRecords; i++) {
            Bin bin = new Bin("binInt", i);
            client.put(writePolicy, testKeys[i], bin);
        }

        for (int i = 0; i < 5; i++) {
            Bin bin = new Bin("binGeo", new Value.GeoJSONValue(
                    "{\"type\": \"Polygon\", \"coordinates\": [[[0,0], [0, 10], [10, 10], [10, 0], [0,0]]]}"));
            client.put(writePolicy, testKeys[i], bin);
        }
    }

    @After
    public void clean() throws InterruptedException {
        for (int i = 0; i < numberOfRecords; i++) {
            client.delete(null, testKeys[i]);
        }
        IndexTask binTask = client.dropIndex(null, namespace, setName, "binInt-index");
        IndexTask geoTask = client.dropIndex(null, namespace, setName, "binGeo-index");
        binTask.waitTillComplete();
        geoTask.waitTillComplete();
    }

    private final QueryHandler queryHandler;

    @Parameterized.Parameters
    public static Object[][] getParams() {
        return new Object[][]{
                {new JSONQueryHandler()},
                {new MsgPackQueryHandler()},
                };
    }

    public QueryTestCorrect(QueryHandler handler) {
        this.queryHandler = handler;
        this.namespace = "test";
        this.testKeys = new Key[numberOfRecords];
        this.setName = "queryset";
        testEndpoint = "/v1/query";

        for (int i = 0; i < numberOfRecords; i++) {
            testKeys[i] = new Key(namespace, setName, "key_" + i);
        }
    }

    @Test
    public void testNamespacePIQueryAllPartitions() throws Exception {
        String endpoint = testEndpoint + "/" + namespace;
        QueryResponseBody res = queryHandler.perform(mockMVC, endpoint, new QueryRequestBody());
        Assert.assertEquals(numberOfRecords, res.getPagination().getTotalRecords());
    }


    @Test
    public void testNamespaceSetPIQueryAllPartitions() throws Exception {
        String endpoint = testEndpoint + "/" + namespace + "/" + setName;
        QueryResponseBody res = queryHandler.perform(mockMVC, endpoint, new QueryRequestBody());
        Assert.assertEquals(res.getPagination().getTotalRecords(), numberOfRecords);
    }

    @Test
    public void testNamespacePIQueryPartitionRange() throws Exception {
        int startPartitions = 100;
        int partitionCount = 2048;
        String endpoint = String.join("/", testEndpoint, namespace, String.valueOf(startPartitions),
                String.valueOf(partitionCount));
        QueryResponseBody res = queryHandler.perform(mockMVC, endpoint, new QueryRequestBody());
        Assert.assertTrue(res.getPagination().getTotalRecords() < (numberOfRecords / 2) + 50);
    }

    @Test
    public void testNamespaceSetPIQueryPartitionRange() throws Exception {
        int startPartitions = 100;
        int partitionCount = 2048;
        String endpoint = String.join("/", testEndpoint, namespace, setName, String.valueOf(startPartitions),
                String.valueOf(partitionCount));
        QueryResponseBody res = queryHandler.perform(mockMVC, endpoint, new QueryRequestBody());
        Assert.assertTrue(res.getPagination().getTotalRecords() < (numberOfRecords / 2) + 50);
    }

    @Test
    public void testNamespacePIQueryAllPaginated() throws Exception {
        int pageSize = 100;
        int queryRequests = 0;
        int total = 0;
        QueryResponseBody res = null;
        Set<Integer> binValues = new HashSet<>();
        String endpoint = testEndpoint + "/" + namespace + "?maxRecords=" + pageSize + "&getToken=True";
        String fromToken = null;

        while (total < numberOfRecords) {
            QueryRequestBody requestBody = new QueryRequestBody();
            requestBody.from = fromToken;
            res = queryHandler.perform(mockMVC, endpoint, requestBody);
            for (RestClientKeyRecord r : res.getRecords()) {
                binValues.add((int) r.bins.get("binInt"));
            }
            total += res.getPagination().getTotalRecords();
            queryRequests++;
            fromToken = res.getPagination().getNextToken();
        }

        Assert.assertEquals(numberOfRecords, binValues.size());
        Assert.assertEquals(numberOfRecords, total);
        Assert.assertEquals(numberOfRecords / pageSize + 1, queryRequests);
        Assert.assertNull(res.getPagination().getNextToken());
    }

    @Test
    public void testNamespaceSetPIQueryAllPaginated() throws Exception {
        int pageSize = 100;
        int queryRequests = 0;
        int total = 0;
        QueryResponseBody res = null;
        Set<Integer> binValues = new HashSet<>();
        String endpoint = testEndpoint + "/" + namespace + "/" + setName + "?maxRecords=" + pageSize + "&getToken=True";
        String fromToken = null;

        while (total < numberOfRecords) {
            QueryRequestBody requestBody = new QueryRequestBody();
            requestBody.from = fromToken;
            res = queryHandler.perform(mockMVC, endpoint, requestBody);
            for (RestClientKeyRecord r : res.getRecords()) {
                binValues.add((int) r.bins.get("binInt"));
            }
            total += res.getPagination().getTotalRecords();
            queryRequests++;
            fromToken = res.getPagination().getNextToken();
        }

        Assert.assertEquals(numberOfRecords, binValues.size());
        Assert.assertEquals(numberOfRecords, total);
        Assert.assertEquals(numberOfRecords / pageSize + 1, queryRequests);
        Assert.assertNull(res.getPagination().getNextToken());
    }

    @Test
    public void testNamespacePIQueryPartitionRangePaginated() throws Exception {
        int startPartitions = 100;
        int partitionCount = 2048;
        int pageSize = 100;
        int queryRequests = 0;
        int total = 0;
        QueryResponseBody res = null;
        Set<Integer> binValues = new HashSet<>();
        String endpoint = String.join("/", testEndpoint, namespace, String.valueOf(startPartitions),
                String.valueOf(partitionCount) + "?maxRecords=" + pageSize + "&getToken=True");
        String fromToken = null;

        do {
            QueryRequestBody requestBody = new QueryRequestBody();
            requestBody.from = fromToken;
            res = queryHandler.perform(mockMVC, endpoint, requestBody);
            for (RestClientKeyRecord r : res.getRecords()) {
                binValues.add((int) r.bins.get("binInt"));
            }
            total += res.getPagination().getTotalRecords();
            queryRequests++;
            fromToken = res.getPagination().getNextToken();
        } while (fromToken != null);

        Assert.assertEquals((int) Math.ceil((double) (numberOfRecords / 2) / pageSize), queryRequests);
        Assert.assertNull(res.getPagination().getNextToken());
        Assert.assertTrue(total < (numberOfRecords / 2) + 100);  // estimate of number of records
        Assert.assertTrue(binValues.size() < (numberOfRecords / 2) + 100); // estimate of number of records
    }

    @Test
    public void testNamespaceSetPIQueryPartitionRangePaginated() throws Exception {
        int startPartitions = 100;
        int partitionCount = 2048;
        int pageSize = 100;
        int queryRequests = 0;
        int total = 0;
        QueryResponseBody res = null;
        Set<Integer> binValues = new HashSet<>();
        String endpoint = String.join("/", testEndpoint, namespace, setName, String.valueOf(startPartitions),
                String.valueOf(partitionCount) + "?maxRecords=" + pageSize + "&getToken=True");
        String fromToken = null;

        do {
            QueryRequestBody requestBody = new QueryRequestBody();
            requestBody.from = fromToken;
            res = queryHandler.perform(mockMVC, endpoint, requestBody);
            for (RestClientKeyRecord r : res.getRecords()) {
                binValues.add((int) r.bins.get("binInt"));
            }
            total += res.getPagination().getTotalRecords();
            queryRequests++;
            fromToken = res.getPagination().getNextToken();
        } while (fromToken != null);

        Assert.assertEquals((int) Math.ceil((double) (numberOfRecords / 2) / pageSize), queryRequests);
        Assert.assertNull(res.getPagination().getNextToken());
        Assert.assertTrue(total < (numberOfRecords / 2) + 100);  // estimate of number of records
        Assert.assertTrue(binValues.size() < (numberOfRecords / 2) + 100); // estimate of number of records
    }

    @Test
    public void testNamespacePIQueryAllEqualFiltered() throws Exception {
        String endpoint = String.join("/", testEndpoint, namespace, setName);
        QueryRequestBody queryBody = new QueryRequestBody();
        QueryEqualLongFilter filter = new QueryEqualLongFilter();
        filter.binName = "binInt";
        filter.value = 100L;
        queryBody.filter = filter;
        QueryResponseBody res = queryHandler.perform(mockMVC, endpoint, queryBody);
        Assert.assertEquals(1, res.getPagination().getTotalRecords());
    }

    @Test
    public void testNamespacePIQueryAllGeoContainsFiltered() throws Exception {
        String endpoint = String.join("/", testEndpoint, namespace, setName);
        QueryRequestBody queryBody = new QueryRequestBody();
        QueryGeoContainsPointFilter filter = new QueryGeoContainsPointFilter();
        filter.binName = "binGeo";
        filter.point = new LngLat(1, 1);
        queryBody.filter = filter;
        QueryResponseBody res = queryHandler.perform(mockMVC, endpoint, queryBody);
        Assert.assertEquals(5, res.getPagination().getTotalRecords());
    }
}

/*
 * The handler interface performs a query request via a json string, and returns a List<Map<String, Object>>
 * Implementations are provided for specifying JSON and MsgPack as return formats
 */
interface QueryHandler {
    QueryResponseBody perform(MockMvc mockMVC, String testEndpoint, QueryRequestBody payload)
            throws Exception;
}

class MsgPackQueryHandler implements QueryHandler {

    ASTestMapper msgPackMapper;

    public MsgPackQueryHandler() {
        msgPackMapper = new ASTestMapper(MsgPackConverter.getASMsgPackObjectMapper(), QueryResponseBody.class);
    }

    private QueryResponseBody getQueryResponse(byte[] response) {
        QueryResponseBody queryResponse = null;
        try {
            queryResponse = (QueryResponseBody) msgPackMapper.bytesToObject(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return queryResponse;
    }

    @Override
    public QueryResponseBody perform(MockMvc mockMVC, String testEndpoint, QueryRequestBody payload)
            throws Exception {

        byte[] response = ASTestUtils.performOperationAndReturn(mockMVC, testEndpoint,
                msgPackMapper.objectToBytes(payload));

        return getQueryResponse(response);
    }

}

class JSONQueryHandler implements QueryHandler {

    ASTestMapper msgPackMapper;

    public JSONQueryHandler() {
        msgPackMapper = new ASTestMapper(JSONMessageConverter.getJSONObjectMapper(), QueryResponseBody.class);
    }

    private QueryResponseBody getQueryResponse(byte[] response) {
        QueryResponseBody queryResponse = null;
        try {
            queryResponse = (QueryResponseBody) msgPackMapper.bytesToObject(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return queryResponse;
    }


    @Override
    public QueryResponseBody perform(MockMvc mockMVC, String testEndpoint, QueryRequestBody payload)
            throws Exception {
        byte[] response = ASTestUtils.performOperationAndReturn(mockMVC, testEndpoint,
                new String(msgPackMapper.objectToBytes(payload), StandardCharsets.UTF_8)).getBytes(
                StandardCharsets.UTF_8);
        return getQueryResponse(response);
    }
}