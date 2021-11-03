package com.aerospike.restclient;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.restclient.domain.RestClientRecord;
import com.aerospike.restclient.domain.scanmodels.RestClientScanResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.msgpack.jackson.dataformat.MessagePackFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ScanTest {

    private MockMvc mockMVC;

    @Autowired
    private AerospikeClient client;

    @Autowired
    private WebApplicationContext wac;

    private static final int numberOfRecords = 101;
    private Key[] testKeys;
    private String testEndpoint;

    private String setName;
    private String namespace;
    private String currentMediaType;
    private ResponseDeserializer responseDeserializer;

    @BeforeEach
    public void setup() {
        mockMVC = MockMvcBuilders.webAppContextSetup(wac).build();
        for (int i = 0; i < numberOfRecords; i++) {
            Bin bin = new Bin("binInt", i);
            client.add(null, testKeys[i], bin);
        }
    }

    @AfterEach
    public void clean() {
        for (int i = 0; i < numberOfRecords; i++) {
            client.delete(null, testKeys[i]);
        }
    }

    private static Stream<Arguments> getParams() {
        return Stream.of(
                Arguments.of(new JSONResponseDeserializer(), MediaType.APPLICATION_JSON.toString()),
                Arguments.of(new MsgPackResponseDeserializer(), "application/msgpack")
        );
    }

    @ParameterizedTest
    @MethodSource("getParams")
    void addParams(ResponseDeserializer deserializer, String mt) {
        this.responseDeserializer = deserializer;
        this.namespace = "test";
        this.currentMediaType = mt;
        this.testKeys = new Key[numberOfRecords];
        this.setName = "scanSet";
        testEndpoint = "/v1/scan/" + namespace + "/" + setName;

        for (int i = 0; i < numberOfRecords; i++) {
            testKeys[i] = new Key(namespace, setName, "key_" + i);
        }
    }

    @Test
    public void testScanAll() throws Exception {
        MockHttpServletResponse response = mockMVC.perform(
                get(testEndpoint).accept(currentMediaType)
        ).andExpect(status().isOk()).andReturn().getResponse();

        RestClientScanResponse res = responseDeserializer.getResponse(response, RestClientScanResponse.class);
        assertEquals(res.getPagination().getTotalRecords(), numberOfRecords);
    }

    @Test
    public void testScanPaginated() throws Exception {
        int pageSize = 10;
        int scanRequests = 0;
        int total = 0;
        RestClientScanResponse res = null;
        Set<Integer> binValues = new HashSet<>();
        testEndpoint = testEndpoint + "?maxRecords=" + pageSize;
        String endpoint = testEndpoint;
        while (total < numberOfRecords) {
            MockHttpServletResponse response = mockMVC.perform(
                    get(endpoint).accept(currentMediaType)
            ).andExpect(status().isOk()).andReturn().getResponse();

            res = responseDeserializer.getResponse(response, RestClientScanResponse.class);
            for (RestClientRecord r : res.getRecords()) {
                binValues.add((int) r.bins.get("binInt"));
            }
            total += res.getPagination().getTotalRecords();
            scanRequests++;
            System.out.println(res.getPagination().getNextToken());
            endpoint = testEndpoint + "&from=" + res.getPagination().getNextToken();
        }

        assertEquals(total, numberOfRecords);
        assertEquals(binValues.size(), numberOfRecords);
        assertEquals(scanRequests, numberOfRecords / pageSize + 1);
        assertNull(res.getPagination().getNextToken());
    }
}

interface ResponseDeserializer {
    <T> T getResponse(MockHttpServletResponse res, Class<T> clazz);
}

class MsgPackResponseDeserializer implements ResponseDeserializer {

    ObjectMapper msgPackMapper;

    public MsgPackResponseDeserializer() {
        msgPackMapper = new ObjectMapper(new MessagePackFactory());
    }

    @Override
    public <T> T getResponse(MockHttpServletResponse res, Class<T> clazz) {
        T response = null;
        try {
            byte[] responseAsByteArray = res.getContentAsByteArray();
            response = msgPackMapper.readValue(responseAsByteArray, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}

class JSONResponseDeserializer implements ResponseDeserializer {

    ObjectMapper mapper;

    public JSONResponseDeserializer() {
        this.mapper = new ObjectMapper();
    }

    @Override
    public <T> T getResponse(MockHttpServletResponse res, Class<T> clazz) {
        T response = null;
        try {
            String responseAsString = res.getContentAsString();
            response = mapper.readValue(responseAsString, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
