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
import com.aerospike.client.Value;
import com.aerospike.client.policy.WritePolicy;
import com.aerospike.client.query.IndexType;
import com.aerospike.restclient.config.JSONMessageConverter;
import com.aerospike.restclient.config.MsgPackConverter;
import com.aerospike.restclient.domain.RestClientKeyRecord;
import com.aerospike.restclient.domain.geojsonmodels.LngLat;
import com.aerospike.restclient.domain.querymodels.*;
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
public class QueryCorrectTest {

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

    private static boolean first = true;

    @Before
    public void setup() throws InterruptedException {
        mockMVC = MockMvcBuilders.webAppContextSetup(wac).build();

        // Need JUnit5 to use Autowired client in @BeforeAll
        if (first) {
            client.truncate(null, "test", null, null);
            first = false;
            WritePolicy writePolicy = new WritePolicy();
            writePolicy.sendKey = true;
            writePolicy.totalTimeout = 0;

            try {
                client.dropIndex(writePolicy, namespace, setName, "binInt-set-index").waitTillComplete();
                client.dropIndex(writePolicy, namespace, setName, "binIntMod-set-index").waitTillComplete();
                client.dropIndex(writePolicy, namespace, setName, "binStr-set-index").waitTillComplete();
                client.dropIndex(writePolicy, namespace, setName, "binGeo-set-index").waitTillComplete();
                client.dropIndex(writePolicy, namespace, null, "binInt-index").waitTillComplete();
                client.dropIndex(writePolicy, namespace, null, "binStr-index").waitTillComplete();
                client.dropIndex(writePolicy, namespace, null, "binIntMod-index").waitTillComplete();
                client.dropIndex(writePolicy, namespace, null, "binGeo-index").waitTillComplete();
            } catch (Exception ignored) {
            }

            client.createIndex(writePolicy, namespace, setName, "binInt-set-index", "binInt", IndexType.NUMERIC)
                    .waitTillComplete();
            client.createIndex(writePolicy, namespace, setName, "binIntMod-set-index", "binIntMod", IndexType.NUMERIC)
                    .waitTillComplete();
            client.createIndex(writePolicy, namespace, setName, "binStr-set-index", "binStr", IndexType.STRING)
                    .waitTillComplete();
            client.createIndex(writePolicy, namespace, setName, "binGeo-set-index", "binGeo", IndexType.GEO2DSPHERE)
                    .waitTillComplete();
            client.createIndex(writePolicy, namespace, null, "binInt-index", "binInt", IndexType.NUMERIC)
                    .waitTillComplete();
            client.createIndex(writePolicy, namespace, null, "binIntMod-index", "binIntMod", IndexType.NUMERIC)
                    .waitTillComplete();
            client.createIndex(writePolicy, namespace, null, "binStr-index", "binStr", IndexType.STRING)
                    .waitTillComplete();
            client.createIndex(writePolicy, namespace, null, "binGeo-index", "binGeo", IndexType.GEO2DSPHERE)
                    .waitTillComplete();

            for (int i = 0; i < numberOfRecords; i++) {
                Bin intBin = new Bin("binInt", i);
                Bin intModBin = new Bin("binIntMod", i % 3);
                Bin strBin = new Bin("binStr", Integer.toString(i));
                client.put(writePolicy, testKeys[i], intBin, intModBin, strBin);
            }

            for (int i = 0; i < 5; i++) {
                Bin geoBin = new Bin("binGeo", new Value.GeoJSONValue(
                        "{\"type\": \"Polygon\", \"coordinates\": [[[0,0], [0, 10], [10, 10], [10, 0], [0,0]]]}"));
                client.put(writePolicy, testKeys[i], geoBin);
            }
        }
    }

    private final QueryHandler queryHandler;

    @Parameterized.Parameters
    public static Object[][] getParams() {
        return new Object[][]{
                {new JSONQueryHandler(), true},
                {new JSONQueryHandler(), false},
                {new MsgPackQueryHandler(), true},
                {new MsgPackQueryHandler(), false},
                };
    }

    public QueryCorrectTest(QueryHandler handler, boolean useSet) {
        this.queryHandler = handler;
        this.namespace = "test";
        this.testKeys = new Key[numberOfRecords];
        this.setName = "queryset";

        if (useSet) {
            testEndpoint = "/v1/query/" + namespace + "/" + setName;
        } else {
            testEndpoint = "/v1/query/" + namespace;
        }

        for (int i = 0; i < numberOfRecords; i++) {
            testKeys[i] = new Key(namespace, setName, "key_" + i);
        }
    }

    @Test
    public void testPIQueryAllPartitions() throws Exception {
        QueryResponseBody res = queryHandler.perform(mockMVC, testEndpoint, new QueryRequestBody());
        Assert.assertEquals(numberOfRecords, res.getPagination().getTotalRecords());
    }

    @Test
    public void testPIQueryPartitionRange() throws Exception {
        int startPartitions = 100;
        int partitionCount = 2048;
        String endpoint = String.join("/", testEndpoint, String.valueOf(startPartitions),
                String.valueOf(partitionCount));
        QueryResponseBody res = queryHandler.perform(mockMVC, endpoint, new QueryRequestBody());
        Assert.assertTrue(res.getPagination().getTotalRecords() < (numberOfRecords / 2) + 50);
    }

    @Test
    public void testPIQueryAllPaginated() throws Exception {
        int pageSize = 100;
        int queryRequests = 0;
        int total = 0;
        QueryResponseBody res = null;
        Set<Integer> binValues = new HashSet<>();
        String endpoint = testEndpoint + "?maxRecords=" + pageSize + "&getToken=True";
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
    public void testPIQueryPartitionRangePaginated() throws Exception {
        int startPartitions = 100;
        int partitionCount = 2048;
        int pageSize = 100;
        int queryRequests = 0;
        int total = 0;
        QueryResponseBody res;
        Set<Integer> binValues = new HashSet<>();
        String endpoint = String.join("/", testEndpoint, String.valueOf(startPartitions),
                partitionCount + "?maxRecords=" + pageSize + "&getToken=True");
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
    public void testSIQueryAllEqualFilteredPaginated() throws Exception {
        int pageSize = 100;
        int queryRequests = 0;
        int total = 0;
        QueryResponseBody res;
        Set<String> keyValues = new HashSet<>();
        String endpoint = testEndpoint + "?maxRecords=" + pageSize + "&getToken=True";
        String fromToken = null;

        do {
            QueryRequestBody requestBody = new QueryRequestBody();
            QueryEqualLongFilter filter = new QueryEqualLongFilter();
            filter.binName = "binIntMod";
            filter.value = 2L;
            requestBody.filter = filter;
            requestBody.from = fromToken;
            res = queryHandler.perform(mockMVC, endpoint, requestBody);
            for (RestClientKeyRecord r : res.getRecords()) {
                keyValues.add((String) r.userKey);
            }
            total += res.getPagination().getTotalRecords();
            queryRequests++;
            fromToken = res.getPagination().getNextToken();
        } while (fromToken != null);

        Assert.assertEquals((int) Math.ceil((double) numberOfRecords / 3 / pageSize), queryRequests);
        Assert.assertNull(res.getPagination().getNextToken());
        Assert.assertEquals((numberOfRecords / 3), total);  // estimate of number of records
        Assert.assertEquals(total, keyValues.size()); // estimate of number of records
    }

    @Test
    public void testSIQueryAllIntEqualFiltered() throws Exception {
        QueryRequestBody queryBody = new QueryRequestBody();
        QueryEqualLongFilter filter = new QueryEqualLongFilter();
        filter.binName = "binInt";
        filter.value = 100L;
        queryBody.filter = filter;
        QueryResponseBody res = queryHandler.perform(mockMVC, testEndpoint, queryBody);
        Assert.assertEquals(1, res.getPagination().getTotalRecords());
    }

    @Test
    public void testSIQueryAllStringEqualFiltered() throws Exception {
        QueryRequestBody queryBody = new QueryRequestBody();
        QueryEqualsStringFilter filter = new QueryEqualsStringFilter();
        filter.binName = "binStr";
        filter.value = "100";
        queryBody.filter = filter;
        QueryResponseBody res = queryHandler.perform(mockMVC, testEndpoint, queryBody);
        Assert.assertEquals(1, res.getPagination().getTotalRecords());
    }

    @Test
    public void testSIQueryAllGeoContainsFiltered() throws Exception {
        QueryRequestBody queryBody = new QueryRequestBody();
        QueryGeoContainsPointFilter filter = new QueryGeoContainsPointFilter();
        filter.binName = "binGeo";
        filter.point = new LngLat(1, 1);
        queryBody.filter = filter;
        QueryResponseBody res = queryHandler.perform(mockMVC, testEndpoint, queryBody);
        Assert.assertEquals(5, res.getPagination().getTotalRecords());
    }
}

/*
 * The handler interface performs a query request via a json string, and returns a List<Map<String, Object>>
 * Implementations are provided for specifying JSON and MsgPack as return formats
 */
interface QueryHandler {
    QueryResponseBody perform(MockMvc mockMVC, String testEndpoint, QueryRequestBody payload) throws Exception;
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
    public QueryResponseBody perform(MockMvc mockMVC, String testEndpoint, QueryRequestBody payload) throws Exception {

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
    public QueryResponseBody perform(MockMvc mockMVC, String testEndpoint, QueryRequestBody payload) throws Exception {
        byte[] response = ASTestUtils.performOperationAndReturn(mockMVC, testEndpoint,
                        new String(msgPackMapper.objectToBytes(payload), StandardCharsets.UTF_8))
                .getBytes(StandardCharsets.UTF_8);
        return getQueryResponse(response);
    }
}