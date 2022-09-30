package com.aerospike.restclient;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.restclient.domain.operationmodels.OperationTypes;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OperateV2CorrectTest {

    String OPERATION_TYPE_KEY = "type";

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMVC;

    @Autowired
    private AerospikeClient client;

    @Autowired
    private WebApplicationContext wac;

    private final Key testKey = new Key("test", "junit", "operate");
    private final Key testKey2 = new Key("test", "junit", "operate2");
    private final Key intKey = new Key("test", "junit", 1);
    private final Key bytesKey = new Key("test", "junit", new byte[]{1, 127, 127, 1});
    private final String testEndpoint = ASTestUtils.buildEndpointV2("operate", "test", "junit", "operate");
    private final String batchEndpoint = "/v2/operate/read/test/junit";
    private final TypeReference<Map<String, Object>> binType = new TypeReference<Map<String, Object>>() {
    };

    @Before
    public void setup() {
        mockMVC = MockMvcBuilders.webAppContextSetup(wac).build();
        Bin strBin = new Bin("str", "bin");
        Bin intBin = new Bin("int", 5);
        client.put(null, testKey, strBin, intBin);
        client.put(null, testKey2, strBin, intBin);
        client.put(null, intKey, strBin, intBin);
        client.put(null, bytesKey, strBin, intBin);
    }

    @After
    public void clean() {
        client.delete(null, testKey);
        client.delete(null, testKey2);
        client.delete(null, intKey);
        client.delete(null, bytesKey);
    }

    @Test
    public void testGetHeaderOp() throws Exception {
        Map<String, Object> opRequest = new HashMap<>();
        List<Map<String, Object>> opList = new ArrayList<>();
        Map<String, Object> opMap = new HashMap<>();
        opRequest.put("opsList", opList);
        opList.add(opMap);

        opMap.put(OPERATION_TYPE_KEY, OperationTypes.GET_HEADER);

        String jsString = objectMapper.writeValueAsString(opRequest);
        String jsonResult = ASTestUtils.performOperationAndReturn(mockMVC, testEndpoint, jsString);

        Map<String, Object> rcRecord = objectMapper.readValue(jsonResult, binType);
        Assert.assertNull(rcRecord.get(AerospikeAPIConstants.RECORD_BINS));

        Record realRecord = client.getHeader(null, testKey);

        int generation = (int) rcRecord.get(AerospikeAPIConstants.GENERATION);
        Assert.assertEquals(generation, realRecord.generation);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetOp() throws Exception {
        Map<String, Object> opRequest = new HashMap<>();
        List<Map<String, Object>> opList = new ArrayList<>();
        Map<String, Object> opMap = new HashMap<>();
        opRequest.put("opsList", opList);
        opList.add(opMap);

        opMap.put(OPERATION_TYPE_KEY, OperationTypes.GET);

        String jsString = objectMapper.writeValueAsString(opRequest);
        String jsonResult = ASTestUtils.performOperationAndReturn(mockMVC, testEndpoint, jsString);

        Map<String, Object> binsObject = objectMapper.readValue(jsonResult, binType);
        Map<String, Object> realBins = client.get(null, testKey).bins;

        Assert.assertTrue(ASTestUtils.compareMapStringObj((Map<String, Object>) binsObject.get("bins"), realBins));
    }

    @Test
    public void testAddOp() throws Exception {
        Map<String, Object> opRequest = new HashMap<>();
        List<Map<String, Object>> opList = new ArrayList<>();
        Map<String, Object> opMap = new HashMap<>();
        opRequest.put("opsList", opList);
        opList.add(opMap);

        opMap.put("binName", "int");
        opMap.put("incr", 2);
        opMap.put(OPERATION_TYPE_KEY, OperationTypes.ADD);

        String payload = objectMapper.writeValueAsString(opRequest);
        ASTestUtils.performOperation(mockMVC, testEndpoint, payload);

        Map<String, Object> expectedBins = new HashMap<>();
        expectedBins.put("str", "bin");
        expectedBins.put("int", 7L);

        Map<String, Object> realBins = client.get(null, testKey).bins;

        Assert.assertTrue(ASTestUtils.compareMapStringObj(expectedBins, realBins));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testReadOp() throws Exception {
        Map<String, Object> opRequest = new HashMap<>();
        List<Map<String, Object>> opList = new ArrayList<>();
        Map<String, Object> opMap = new HashMap<>();
        opRequest.put("opsList", opList);
        opList.add(opMap);

        opMap.put(OPERATION_TYPE_KEY, OperationTypes.READ);
        opMap.put("binName", "str");

        String jsString = objectMapper.writeValueAsString(opRequest);
        String jsonResult = ASTestUtils.performOperationAndReturn(mockMVC, testEndpoint, jsString);
        Map<String, Object> binsObject = objectMapper.readValue(jsonResult, binType);
        /* Only read the str bin on the get*/
        Map<String, Object> realBins = client.get(null, testKey, "str").bins;

        Assert.assertTrue(ASTestUtils.compareMapStringObj((Map<String, Object>) binsObject.get("bins"), realBins));
    }

    @Test
    public void testPutOp() throws Exception {
        Map<String, Object> opRequest = new HashMap<>();
        List<Map<String, Object>> opList = new ArrayList<>();
        Map<String, Object> opMap = new HashMap<>();
        opRequest.put("opsList", opList);
        opList.add(opMap);

        opMap.put("binName", "new");
        opMap.put("value", "put");
        opMap.put(OPERATION_TYPE_KEY, OperationTypes.PUT);

        opList.add(opMap);

        String payload = objectMapper.writeValueAsString(opRequest);

        ASTestUtils.performOperation(mockMVC, testEndpoint, payload);

        Map<String, Object> expectedBins = new HashMap<>();
        expectedBins.put("str", "bin");
        expectedBins.put("int", 5L);
        expectedBins.put("new", "put");

        Map<String, Object> realBins = client.get(null, testKey).bins;

        Assert.assertTrue(ASTestUtils.compareMapStringObj(expectedBins, realBins));
    }

    @Test
    public void testAppendOp() throws Exception {
        Map<String, Object> opRequest = new HashMap<>();
        List<Map<String, Object>> opList = new ArrayList<>();
        Map<String, Object> opMap = new HashMap<>();
        opRequest.put("opsList", opList);
        opList.add(opMap);

        opMap.put(OPERATION_TYPE_KEY, OperationTypes.APPEND);
        opMap.put("value", "ary");
        opMap.put("binName", "str");

        String jsString = objectMapper.writeValueAsString(opRequest);

        ASTestUtils.performOperation(mockMVC, testEndpoint, jsString);

        /* Only read the str bin on the get*/
        Map<String, Object> expectedBins = new HashMap<>();
        expectedBins.put("str", "binary");
        Map<String, Object> realBins = client.get(null, testKey, "str").bins;

        Assert.assertTrue(ASTestUtils.compareMapStringObj(expectedBins, realBins));
    }

    @Test
    public void testPrependOp() throws Exception {
        Map<String, Object> opRequest = new HashMap<>();
        List<Map<String, Object>> opList = new ArrayList<>();
        Map<String, Object> opMap = new HashMap<>();
        opRequest.put("opsList", opList);
        opList.add(opMap);

        opMap.put(OPERATION_TYPE_KEY, OperationTypes.PREPEND);
        opMap.put("value", "ro");
        opMap.put("binName", "str");

        String jsString = objectMapper.writeValueAsString(opRequest);

        ASTestUtils.performOperation(mockMVC, testEndpoint, jsString);

        /* Only read the str bin on the get*/
        Map<String, Object> expectedBins = new HashMap<>();
        expectedBins.put("str", "robin");
        Map<String, Object> realBins = client.get(null, testKey, "str").bins;

        Assert.assertTrue(ASTestUtils.compareMapStringObj(expectedBins, realBins));
    }

    @Test
    public void testTouchOp() throws Exception {
        Map<String, Object> opRequest = new HashMap<>();
        List<Map<String, Object>> opList = new ArrayList<>();
        Map<String, Object> opMap = new HashMap<>();
        opRequest.put("opsList", opList);
        opList.add(opMap);

        Record record = client.get(null, testKey);
        int oldGeneration = record.generation;

        opMap.put(OPERATION_TYPE_KEY, OperationTypes.TOUCH);

        opList.add(opMap);

        String jsString = objectMapper.writeValueAsString(opRequest);

        ASTestUtils.performOperation(mockMVC, testEndpoint, jsString);

        record = client.get(null, testKey);
        Assert.assertEquals(oldGeneration + 1, record.generation);
    }

    @Test
    public void testTouchOpWithIntegerKey() throws Exception {
        Map<String, Object> opRequest = new HashMap<>();
        List<Map<String, Object>> opList = new ArrayList<>();
        Map<String, Object> opMap = new HashMap<>();
        opRequest.put("opsList", opList);
        opList.add(opMap);

        String intEndpoint = ASTestUtils.buildEndpointV2("operate", "test", "junit", "1") + "?keytype=INTEGER";
        Record record = client.get(null, intKey);
        int oldGeneration = record.generation;

        opMap.put(OPERATION_TYPE_KEY, OperationTypes.TOUCH);

        opList.add(opMap);

        String jsString = objectMapper.writeValueAsString(opRequest);

        ASTestUtils.performOperation(mockMVC, intEndpoint, jsString);

        record = client.get(null, intKey);
        Assert.assertEquals(oldGeneration + 1, record.generation);
    }

    @Test
    public void testTouchOpWithBytesKey() throws Exception {
        Map<String, Object> opRequest = new HashMap<>();
        List<Map<String, Object>> opList = new ArrayList<>();
        Map<String, Object> opMap = new HashMap<>();
        opRequest.put("opsList", opList);
        opList.add(opMap);

        String urlBytes = Base64.getUrlEncoder().encodeToString((byte[]) bytesKey.userKey.getObject());
        String bytesEndpoint = ASTestUtils.buildEndpointV2("operate", "test", "junit", urlBytes) + "?keytype=BYTES";

        Record record = client.get(null, bytesKey);
        int oldGeneration = record.generation;

        opMap.put(OPERATION_TYPE_KEY, OperationTypes.TOUCH);

        String jsString = objectMapper.writeValueAsString(opRequest);

        ASTestUtils.performOperation(mockMVC, bytesEndpoint, jsString);

        record = client.get(null, bytesKey);
        Assert.assertEquals(oldGeneration + 1, record.generation);
    }

    /*
     * Touch a record by providing it's digest
     */
    @Test
    public void testTouchOpWithDigestKey() throws Exception {
        Map<String, Object> opRequest = new HashMap<>();
        List<Map<String, Object>> opList = new ArrayList<>();
        Map<String, Object> opMap = new HashMap<>();
        opRequest.put("opsList", opList);
        opList.add(opMap);

        String urlBytes = Base64.getUrlEncoder().encodeToString(testKey.digest);
        String bytesEndpoint = ASTestUtils.buildEndpointV2("operate", "test", "junit", urlBytes) + "?keytype=DIGEST";

        Record record = client.get(null, testKey);
        int oldGeneration = record.generation;

        opMap.put(OPERATION_TYPE_KEY, OperationTypes.TOUCH);

        opList.add(opMap);

        String jsString = objectMapper.writeValueAsString(opRequest);
        ASTestUtils.performOperation(mockMVC, bytesEndpoint, jsString);

        record = client.get(null, testKey);
        Assert.assertEquals(oldGeneration + 1, record.generation);
    }

    @Test
    public void testGetOpNonExistentRecord() throws Exception {
        // Key that does not exist
//        String fakeEndpoint = "/v2/operate/test/junit12345/operate";
        String fakeEndpoint = ASTestUtils.buildEndpointV2("operate", "test", "junit12345", "operate");
        Map<String, Object> opRequest = new HashMap<>();
        List<Map<String, Object>> opList = new ArrayList<>();
        Map<String, Object> opMap = new HashMap<>();
        opRequest.put("opsList", opList);
        opList.add(opMap);

        opMap.put(OPERATION_TYPE_KEY, OperationTypes.GET);

        String jsString = objectMapper.writeValueAsString(opRequest);
        ASTestUtils.performOperationAndExpect(mockMVC, fakeEndpoint, jsString, status().isNotFound());
    }

    @Test
    public void testDeleteOp() throws Exception {
        Map<String, Object> opRequest = new HashMap<>();
        List<Map<String, Object>> opList = new ArrayList<>();
        Map<String, Object> opMap = new HashMap<>();
        opRequest.put("opsList", opList);
        opList.add(opMap);

        opMap.put(OPERATION_TYPE_KEY, OperationTypes.DELETE);

        String jsString = objectMapper.writeValueAsString(opRequest);

        ASTestUtils.performOperation(mockMVC, testEndpoint, jsString);

        Record record = client.get(null, testKey);
        Assert.assertNull(record);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testBatchGetOp() throws Exception {
        Map<String, Object> opRequest = new HashMap<>();
        List<Map<String, Object>> opList = new ArrayList<>();
        Map<String, Object> opMap = new HashMap<>();
        opRequest.put("opsList", opList);
        opList.add(opMap);

        opMap.put(OPERATION_TYPE_KEY, OperationTypes.GET);

        opList.add(opMap);
        String jsString = objectMapper.writeValueAsString(opRequest);
        String batchUrl = batchEndpoint + "?key=operate&key=operate2";
        String jsonResult = ASTestUtils.performOperationAndReturn(mockMVC, batchUrl, jsString);

        TypeReference<List<Map<String, Object>>> ref = new TypeReference<List<Map<String, Object>>>() {
        };
        List<Object> recordBins = objectMapper.readValue(jsonResult, ref)
                .stream()
                .map(r -> (Map<String, Object>) r.get("bins"))
                .map(Map::keySet)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        List<Object> expected = Arrays.stream(client.get(null, new Key[]{testKey, testKey2}))
                .map(r -> r.bins)
                .map(Map::keySet)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        assertIterableEquals(expected, recordBins);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testBatchGetBinOp() throws Exception {
        Map<String, Object> opRequest = new HashMap<>();
        List<Map<String, Object>> opList = new ArrayList<>();
        Map<String, Object> opMap = new HashMap<>();
        opRequest.put("opsList", opList);
        opList.add(opMap);

        opMap.put("binName", "str");
        opMap.put(OPERATION_TYPE_KEY, OperationTypes.GET);

        opList.add(opMap);
        String jsString = objectMapper.writeValueAsString(opRequest);
        String batchUrl = batchEndpoint + "?key=operate&key=operate2";
        String jsonResult = ASTestUtils.performOperationAndReturn(mockMVC, batchUrl, jsString);

        TypeReference<List<Map<String, Object>>> ref = new TypeReference<List<Map<String, Object>>>() {
        };
        List<Object> recordBins = objectMapper.readValue(jsonResult, ref)
                .stream()
                .map(r -> (Map<String, Object>) r.get("bins"))
                .map(Map::keySet)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        List<Object> expected = Arrays.stream(client.get(null, new Key[]{testKey, testKey2}))
                .map(r -> r.bins)
                .map(Map::keySet)
                .flatMap(Collection::stream)
                .filter(k -> k.equals("str"))
                .collect(Collectors.toList());

        assertIterableEquals(expected, recordBins);
    }
}
