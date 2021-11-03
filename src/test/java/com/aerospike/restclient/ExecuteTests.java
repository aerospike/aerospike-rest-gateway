package com.aerospike.restclient;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.restclient.domain.RestClientExecuteTask;
import com.aerospike.restclient.domain.RestClientExecuteTaskStatus;
import com.aerospike.restclient.util.AerospikeOperation;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.aerospike.restclient.util.AerospikeAPIConstants.OPERATION_FIELD;
import static com.aerospike.restclient.util.AerospikeAPIConstants.OPERATION_VALUES_FIELD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExecuteTests {

    private MockMvc mockMVC;

    @Autowired
    private AerospikeClient client;

    @Autowired
    private WebApplicationContext wac;

    private String setName;
    private String namespace;
    private String currentMediaType;

    private static final int numberOfRecords = 10;

    private Key[] testKeys;
    private String testEndpoint;
    private String queryStatusEndpoint;

    @BeforeEach
    public void setup() {
        mockMVC = MockMvcBuilders.webAppContextSetup(wac).build();
        for (int i = 0; i < testKeys.length; i++) {
            Bin bin = new Bin("binInt", i);
            client.add(null, testKeys[i], bin);
        }
    }

    @AfterEach
    public void clean() {
        for (Key testKey : testKeys) {
            client.delete(null, testKey);
        }
    }

    private ObjectMapper objectMapper;
    private ResponseDeserializer responseDeserializer;

    private static Stream<Arguments> getParams() {
        return Stream.of(
                Arguments.of(new ObjectMapper(), new JSONResponseDeserializer(), MediaType.APPLICATION_JSON.toString(), true),
                Arguments.of(new ObjectMapper(new MessagePackFactory()), new MsgPackResponseDeserializer(), "application/msgpack", true),
                Arguments.of(new ObjectMapper(), new JSONResponseDeserializer(), MediaType.APPLICATION_JSON.toString(), false),
                Arguments.of(new ObjectMapper(new MessagePackFactory()), new MsgPackResponseDeserializer(), "application/msgpack", false)
        );
    }

    @ParameterizedTest
    @MethodSource("getParams")
    void addParams(ObjectMapper mapper, ResponseDeserializer deserializer, String mt, boolean useSet) {
        objectMapper = mapper;
        responseDeserializer = deserializer;
        currentMediaType = mt;
        namespace = "test";
        if (useSet) {
            setName = "executeSet";
            testEndpoint = "/v1/execute/scan/" + namespace + "/" + setName;
        } else {
            testEndpoint = "/v1/execute/scan/" + namespace;
        }
        testKeys = setKeys();
        queryStatusEndpoint = "/v1/execute/scan/status/";
    }

    private Key[] setKeys() {
        Key[] keys = new Key[numberOfRecords];
        for (int i = 0; i < numberOfRecords; i++) {
            keys[i] = new Key(namespace, setName, "exec_" + i);
        }
        return keys;
    }

    @Test
    public void testExecuteScan() throws Exception {
        List<Map<String, Object>> opList = new ArrayList<>();
        Map<String, Object> opMap = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();

        opValues.put("bin", "binInt");
        opValues.put("incr", 1);
        opMap.put(OPERATION_FIELD, AerospikeOperation.ADD);
        opMap.put(OPERATION_VALUES_FIELD, opValues);
        opList.add(opMap);

        byte[] payload = objectMapper.writeValueAsBytes(opList);
        MockHttpServletResponse response = mockMVC.perform(post(testEndpoint)
                .contentType(currentMediaType)
                .content(payload)
                .accept(currentMediaType))
                .andExpect(status().isOk()).andReturn().getResponse();

        RestClientExecuteTask task = responseDeserializer.getResponse(response, RestClientExecuteTask.class);

        Thread.sleep(100);
        String endpoint = queryStatusEndpoint + task.getTaskId();
        MockHttpServletResponse statusResponse = mockMVC.perform(
                get(endpoint).accept(currentMediaType)
        ).andExpect(status().isOk()).andReturn().getResponse();

        RestClientExecuteTaskStatus status = responseDeserializer
                .getResponse(statusResponse, RestClientExecuteTaskStatus.class);

        assertEquals(status.getStatus(), "COMPLETE");

        for (int i = 0; i < numberOfRecords; i++) {
            long binValue = (long) client.get(null, testKeys[i]).bins.get("binInt");
            assertEquals(binValue, i + 1);
        }
    }
}
