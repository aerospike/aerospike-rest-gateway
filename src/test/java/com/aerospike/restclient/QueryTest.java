package com.aerospike.restclient;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.client.policy.WritePolicy;
import com.aerospike.client.query.IndexType;
import com.aerospike.restclient.config.JSONMessageConverter;
import com.aerospike.restclient.config.MsgPackConverter;
import com.aerospike.restclient.domain.RestClientKeyRecord;
import com.aerospike.restclient.domain.querymodels.RestClientQueryBinEqualFilter;
import com.aerospike.restclient.domain.querymodels.RestClientQueryBody;
import com.aerospike.restclient.domain.querymodels.RestClientQueryFilter;
import com.aerospike.restclient.domain.querymodels.RestClientQueryResponse;
import com.aerospike.restclient.domain.scanmodels.RestClientScanResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.msgpack.jackson.dataformat.MessagePackFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(Parameterized.class)
@SpringBootTest
public class QueryTest {

    @ClassRule
    public static final SpringClassRule springClassRule = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    private MockMvc mockMVC;

    @Autowired
    private AerospikeClient client;

    @Autowired
    private WebApplicationContext wac;

    private static final int numberOfRecords = 1001;
    private final Key[] testKeys;
    private String testEndpoint;

    private final String setName;
    private final String namespace;

    @Before
    public void setup() throws InterruptedException {
        mockMVC = MockMvcBuilders.webAppContextSetup(wac).build();
        WritePolicy writePolicy = new WritePolicy();
        writePolicy.sendKey = true;


        client.createIndex(null, namespace, setName, "binInt-index", "binInt", IndexType.NUMERIC);
        Thread.sleep(3000);

        for (int i = 0; i < numberOfRecords; i++) {
            Bin bin = new Bin("binInt", i);
            client.add(writePolicy, testKeys[i], bin);
        }
    }

    @After
    public void clean() throws InterruptedException {
        for (int i = 0; i < numberOfRecords; i++) {
            client.delete(null, testKeys[i]);
        }

        client.dropIndex(null, namespace, setName, "binInt-index");
        Thread.sleep(5000);
    }

    private final QueryHandler queryHandler;

    @Parameterized.Parameters
    public static Object[][] getParams() {
        return new Object[][]{
                {new JSONQueryHandler()},
                {new MsgPackQueryHandler()},
        };
    }

    public QueryTest(QueryHandler handler) {
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
        RestClientQueryResponse res = queryHandler.perform(mockMVC, endpoint, new RestClientQueryBody());
        Assert.assertEquals(numberOfRecords, res.getPagination().getTotalRecords());
        Assert.assertEquals(numberOfRecords, res.getPagination().getTotalRecords());
    }


    @Test
    public void testNamespaceSetPIQueryAllPartitions() throws Exception {
        String endpoint = testEndpoint + "/" + namespace + "/" + setName;
        RestClientQueryResponse res = queryHandler.perform(mockMVC, endpoint, new RestClientQueryBody());
        Assert.assertEquals(res.getPagination().getTotalRecords(), numberOfRecords);
    }

    @Test
    public void testNamespacePIQueryPartitionRange() throws Exception {
        int startPartitions = 100;
        int partitionCount = 2048;
        String endpoint = String.join("/", testEndpoint, namespace, String.valueOf(startPartitions), String.valueOf(partitionCount));
        RestClientQueryResponse res = queryHandler.perform(mockMVC, endpoint, new RestClientQueryBody());
        Assert.assertTrue(res.getPagination().getTotalRecords() < (numberOfRecords / 2) + 100);
    }

    @Test
    public void testNamespaceSetPIQueryPartitionRange() throws Exception {
        int startPartitions = 100;
        int partitionCount = 2048;
        String endpoint = String.join("/", testEndpoint, namespace, setName, String.valueOf(startPartitions), String.valueOf(partitionCount));
        RestClientQueryResponse res = queryHandler.perform(mockMVC, endpoint, new RestClientQueryBody());
        Assert.assertTrue(res.getPagination().getTotalRecords() < (numberOfRecords / 2) + 100);
    }

    @Test
    public void testNamespacePIQueryAllPaginated() throws Exception {
        int pageSize = 100;
        int queryRequests = 0;
        int total = 0;
        RestClientQueryResponse res = null;
        Set<Integer> binValues = new HashSet<>();
        String endpoint = testEndpoint + "/" + namespace + "?maxRecords=" + pageSize + "&getToken=True";
        String fromToken = null;

        while (total < numberOfRecords) {
            RestClientQueryBody requestBody = new RestClientQueryBody();
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
        RestClientQueryResponse res = null;
        Set<Integer> binValues = new HashSet<>();
        String endpoint = testEndpoint + "/" + namespace + "/" + setName + "?maxRecords=" + pageSize + "&getToken=True";
        String fromToken = null;

        while (total < numberOfRecords) {
            RestClientQueryBody requestBody = new RestClientQueryBody();
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
        RestClientQueryResponse res = null;
        Set<Integer> binValues = new HashSet<>();
        String endpoint = String.join("/", testEndpoint, namespace, String.valueOf(startPartitions), String.valueOf(partitionCount) + "?maxRecords=" + pageSize + "&getToken=True");
        String fromToken = null;

        do {
            RestClientQueryBody requestBody = new RestClientQueryBody();
            requestBody.from = fromToken;
            res = queryHandler.perform(mockMVC, endpoint, requestBody);
            for (RestClientKeyRecord r : res.getRecords()) {
                binValues.add((int) r.bins.get("binInt"));
            }
            total += res.getPagination().getTotalRecords();
            queryRequests++;
            fromToken = res.getPagination().getNextToken();
        } while (fromToken != null);

        Assert.assertEquals((numberOfRecords / 2) / pageSize, queryRequests);
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
        RestClientQueryResponse res = null;
        Set<Integer> binValues = new HashSet<>();
        String endpoint = String.join("/", testEndpoint, namespace, setName, String.valueOf(startPartitions), String.valueOf(partitionCount) + "?maxRecords=" + pageSize + "&getToken=True");
        String fromToken = null;

        do {
            RestClientQueryBody requestBody = new RestClientQueryBody();
            requestBody.from = fromToken;
            res = queryHandler.perform(mockMVC, endpoint, requestBody);
            for (RestClientKeyRecord r : res.getRecords()) {
                binValues.add((int) r.bins.get("binInt"));
            }
            total += res.getPagination().getTotalRecords();
            queryRequests++;
            fromToken = res.getPagination().getNextToken();
        } while (fromToken != null);

        Assert.assertEquals((numberOfRecords / 2) / pageSize, queryRequests);
        Assert.assertNull(res.getPagination().getNextToken());
        Assert.assertTrue(total < (numberOfRecords / 2) + 100);  // estimate of number of records
        Assert.assertTrue(binValues.size() < (numberOfRecords / 2) + 100); // estimate of number of records
    }

    @Test
    public void testNamespacePIQueryAllFiltered() throws Exception {
        String endpoint = String.join("/", testEndpoint, namespace, setName);
        RestClientQueryBody queryBody = new RestClientQueryBody();
        RestClientQueryBinEqualFilter filter = new RestClientQueryBinEqualFilter();
        filter.binName = "binInt";
        filter.value = 100;
        queryBody.filter = filter;
        RestClientQueryResponse res = queryHandler.perform(mockMVC, endpoint, queryBody);
        Assert.assertEquals(1, res.getPagination().getTotalRecords());
    }
}

class JsonQueryResponseMapper extends ASTestMapper {
    public JsonQueryResponseMapper() {
        super(new ObjectMapper(), RestClientQueryResponse.class);
    }
}

class MsgPackQueryResponseMapper extends ASTestMapper {

    public MsgPackQueryResponseMapper() {
        super(new ObjectMapper(new MessagePackFactory()), RestClientQueryResponse.class);
    }
}

/*
 * The handler interface performs a query request via a json string, and returns a List<Map<String, Object>>
 * Implementations are provided for specifying JSON and MsgPack as return formats
 */
interface QueryHandler {
    RestClientQueryResponse perform(MockMvc mockMVC, String testEndpoint, RestClientQueryBody payload)
            throws Exception;
}

class MsgPackQueryHandler implements QueryHandler {

    ASTestMapper msgPackMapper;

    public MsgPackQueryHandler() {
        msgPackMapper = new ASTestMapper(MsgPackConverter.getASMsgPackObjectMapper(), RestClientQueryResponse.class);
    }

    private RestClientQueryResponse getQueryResponse(byte[] response) {
        RestClientQueryResponse queryResponse = null;
        try {
            queryResponse = (RestClientQueryResponse) msgPackMapper.bytesToObject(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return queryResponse;
    }

    @Override
    public RestClientQueryResponse perform(MockMvc mockMVC, String testEndpoint, RestClientQueryBody payload)
            throws Exception {

        byte[] response = ASTestUtils.performOperationAndReturn(mockMVC, testEndpoint, msgPackMapper.objectToBytes(payload));

        return getQueryResponse(response);
    }

}

class JSONQueryHandler implements QueryHandler {

    ASTestMapper msgPackMapper;

    public JSONQueryHandler() {
        msgPackMapper = new ASTestMapper(JSONMessageConverter.getJSONObjectMapper(), RestClientQueryResponse.class);
    }

    private RestClientQueryResponse getQueryResponse(byte[] response) {
        TypeReference<Map<String, Object>> btype = new TypeReference<Map<String, Object>>() {
        };
        RestClientQueryResponse queryResponse = null;
        try {
            queryResponse = (RestClientQueryResponse) msgPackMapper.bytesToObject(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return queryResponse;
    }


    @Override
    public RestClientQueryResponse perform(MockMvc mockMVC, String testEndpoint, RestClientQueryBody payload)
            throws Exception {
        byte[] response = ASTestUtils.performOperationAndReturn(mockMVC, testEndpoint, new String(msgPackMapper.objectToBytes(payload), StandardCharsets.UTF_8)).getBytes(StandardCharsets.UTF_8);
        return getQueryResponse(response);
    }
}