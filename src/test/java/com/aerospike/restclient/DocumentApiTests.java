package com.aerospike.restclient;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(Parameterized.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DocumentApiTests {

    @ClassRule
    public static final SpringClassRule springClassRule = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @Autowired
    private MockMvc mockMVC;

    @Autowired
    private AerospikeClient client;

    private final ObjectMapper objectMapper;
    private final String mediaType;

    private Map<String, Object> objectMap;
    private final Key testKey;
    private final String testBinName = "docBin";
    private final String testEndpoint;

    @Before
    public void setup() {
        objectMap = new HashMap<>();
        objectMap.put("example1", ImmutableMap.of("key01", Arrays.asList("A1", "B1", "C1")));
        objectMap.put("example2", Arrays.asList(
                ImmutableMap.of("key02", "E1", "key03", "F1"),
                ImmutableMap.of("key04", "G1", "key05", "H1")
        ));
        Bin docBin = new Bin(testBinName, objectMap);
        client.put(null, testKey, docBin);
    }

    @After
    public void clean() {
        client.delete(null, testKey);
    }

    @Parameterized.Parameters
    public static Object[][] getParams() {
        return new Object[][]{
                {new ObjectMapper(), MediaType.APPLICATION_JSON.toString(), true},
                {new ObjectMapper(new MessagePackFactory()), "application/msgpack", true},
                {new ObjectMapper(), MediaType.APPLICATION_JSON.toString(), false},
                {new ObjectMapper(new MessagePackFactory()), "application/msgpack", false}
        };
    }

    public DocumentApiTests(ObjectMapper objectMapper, String mediaType, boolean useSet) {
        if (useSet) {
            testKey = new Key("test", "junit", "doc");
            testEndpoint = "/v1/document/test/junit/doc/" + testBinName + "/";
        } else {
            testKey = new Key("test", null, "doc");
            testEndpoint = "/v1/document/test/doc/" + testBinName + "/";
        }
        this.objectMapper = objectMapper;
        this.mediaType = mediaType;
    }

    @Test
    public void testDocumentGet() throws Exception {
        String jsonPath = URLEncoder.encode("$.example1", StandardCharsets.UTF_8.toString());

        MvcResult result = mockMVC.perform(
                get(testEndpoint + jsonPath)
        ).andExpect(status().isOk()).andReturn();

        MockHttpServletResponse res = result.getResponse();
        String resJson = res.getContentAsString();
        TypeReference<Map<String, Object>> typeReference = new TypeReference<Map<String, Object>>() {
        };
        Map<String, Object> resObject = objectMapper.readValue(resJson, typeReference);

        Assert.assertEquals(objectMap.get("example1"), resObject);
    }

    @Test
    public void testDocumentPut() throws Exception {
        String jsonPath = URLEncoder.encode("$.example3", StandardCharsets.UTF_8.toString());

        byte[] payload = objectMapper.writeValueAsBytes("str3");
        mockMVC.perform(put(testEndpoint + jsonPath)
                .contentType(mediaType)
                .content(payload)
                .accept(mediaType))
                .andExpect(status().isOk());

        MvcResult result = mockMVC.perform(
                get(testEndpoint + jsonPath)
        ).andExpect(status().isOk()).andReturn();

        MockHttpServletResponse res = result.getResponse();
        String resJson = res.getContentAsString();
        TypeReference<String> typeReference = new TypeReference<String>() {
        };
        String resObject = objectMapper.readValue(resJson, typeReference);

        Assert.assertEquals("str3", resObject);
    }

    @Test
    public void testDocumentAppend() throws Exception {
        String jsonPath = URLEncoder.encode("$.example1.key01", StandardCharsets.UTF_8.toString());

        byte[] payload = objectMapper.writeValueAsBytes("D1");
        mockMVC.perform(post(testEndpoint + jsonPath)
                .contentType(mediaType)
                .content(payload)
                .accept(mediaType))
                .andExpect(status().isOk());

        MvcResult result = mockMVC.perform(
                get(testEndpoint + jsonPath)
        ).andExpect(status().isOk()).andReturn();

        MockHttpServletResponse res = result.getResponse();
        String resJson = res.getContentAsString();
        TypeReference<List<String>> typeReference = new TypeReference<List<String>>() {
        };
        List<String> resObject = objectMapper.readValue(resJson, typeReference);

        Assert.assertEquals(Arrays.asList("A1", "B1", "C1", "D1"), resObject);
    }

    @Test
    public void testDocumentDelete() throws Exception {
        String jsonDeletePath = URLEncoder.encode("$.example2[1]", StandardCharsets.UTF_8.toString());
        String jsonPath = URLEncoder.encode("$.example2", StandardCharsets.UTF_8.toString());

        mockMVC.perform(
                delete(testEndpoint + jsonDeletePath)
        ).andExpect(status().isNoContent());

        MvcResult result = mockMVC.perform(
                get(testEndpoint + jsonPath)
        ).andExpect(status().isOk()).andReturn();

        MockHttpServletResponse res = result.getResponse();
        String resJson = res.getContentAsString();
        TypeReference<List<Map<String, Object>>> typeReference = new TypeReference<List<Map<String, Object>>>() {
        };
        List<Map<String, Object>> resObject = objectMapper.readValue(resJson, typeReference);

        List<Map<String, Object>> expected = Collections.singletonList(
                ImmutableMap.of("key02", "E1", "key03", "F1")
        );

        Assert.assertEquals(expected, resObject);
    }
}
