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
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
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
import java.io.UnsupportedEncodingException;
import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
 * These Tests are simple tests which store a simple value via the Java Client, then retrieve it via REST
 * The expected and returned values are compared.
 *
 */
@RunWith(Parameterized.class)
@SpringBootTest
public class RecordGetCorrectTests {

    /* Needed to run as a Spring Boot test */
    @ClassRule
    public static final SpringClassRule springClassRule = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @Autowired
    private AerospikeClient client;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMVC;
    private final RecordDeserializer recordDeserializer;
    private final String currentMediaType;

    /*
     * Returns a two item array, of mediatype and record deserializer
     */
    @Parameters
    public static Object[] mappers() {
        return new Object[][]{
                {new JSONRestRecordDeserializer(), MediaType.APPLICATION_JSON.toString(), true},
                {new MsgPackRestRecordDeserializer(), "application/msgpack", true},
                {new JSONRestRecordDeserializer(), MediaType.APPLICATION_JSON.toString(), false},
                {new MsgPackRestRecordDeserializer(), "application/msgpack", false},
                };
    }

    private final Key testKey;
    private final Key intKey;
    private final Key bytesKey;

    private List<Key> keysToRemove;

    // Endpoint to receive all requests
    private final String noBinEndpoint;
    private final String intEndpoint;
    private final String bytesEndpoint;
    private final String digestEndpoint;

    @Before
    public void setup() {
        mockMVC = MockMvcBuilders.webAppContextSetup(wac).build();
        keysToRemove = new ArrayList<>();
    }

    @After
    public void clean() {
        client.delete(null, this.testKey);
        for (Key key : keysToRemove) {
            client.delete(null, key);
        }
    }

    public RecordGetCorrectTests(RecordDeserializer deserializer, String mt, boolean useSet) {
        this.recordDeserializer = deserializer;
        this.currentMediaType = mt;
        byte[] keyBytes = {1, 127, 127, 1};

        if (useSet) {
            testKey = new Key("test", "junit", "getput");
            intKey = new Key("test", "junit", 1);

            bytesKey = new Key("test", "junit", new Value.BytesValue(keyBytes));

            noBinEndpoint = ASTestUtils.buildEndpointV1("kvs", "test", "junit", "getput");

            intEndpoint = ASTestUtils.buildEndpointV1("kvs", "test", "junit", "1") + "?keytype=INTEGER";

            String keyDigest = Base64.getUrlEncoder().encodeToString(this.testKey.digest);
            digestEndpoint = ASTestUtils.buildEndpointV1("kvs", this.testKey.namespace, this.testKey.setName,
                    keyDigest) + "?keytype=DIGEST";

            String b64byteStr = Base64.getUrlEncoder().encodeToString(keyBytes);
            bytesEndpoint = ASTestUtils.buildEndpointV1("kvs", "test", "junit", b64byteStr) + "?keytype=BYTES";
        } else {
            testKey = new Key("test", null, "getput");
            intKey = new Key("test", null, 1);

            bytesKey = new Key("test", null, new Value.BytesValue(keyBytes));

            noBinEndpoint = ASTestUtils.buildEndpointV1("kvs", "test", "getput");

            intEndpoint = ASTestUtils.buildEndpointV1("kvs", "test", "1") + "?keytype=INTEGER";

            String keyDigest = Base64.getUrlEncoder().encodeToString(this.testKey.digest);
            digestEndpoint = ASTestUtils.buildEndpointV1("kvs", this.testKey.namespace, keyDigest) + "?keytype=DIGEST";

            String b64byteStr = Base64.getUrlEncoder().encodeToString(keyBytes);
            bytesEndpoint = ASTestUtils.buildEndpointV1("kvs", "test", b64byteStr) + "?keytype=BYTES";
        }
    }

    @Test
    public void GetInteger() throws Exception {
        Map<String, Object> binMap = new HashMap<>();

        Bin intBin = new Bin("integer", 10);
        binMap.put(intBin.name, intBin.value.toInteger());

        client.put(null, this.testKey, intBin);

        MockHttpServletResponse res = mockMVC.perform(
                        get(noBinEndpoint).contentType(MediaType.APPLICATION_JSON).accept(currentMediaType))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        Map<String, Object> resObject = recordDeserializer.getReturnedBins(res);

        Assert.assertTrue(ASTestUtils.compareMapStringObj(resObject, binMap));
    }

    @Test
    public void GetString() throws Exception {
        Map<String, Object> binMap = new HashMap<>();
        Bin intBin = new Bin("string", "aerospike");

        binMap.put("string", "aerospike");

        client.put(null, this.testKey, intBin);

        MockHttpServletResponse res = mockMVC.perform(
                        get(noBinEndpoint).contentType(MediaType.APPLICATION_JSON).accept(currentMediaType))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        Map<String, Object> resObject = recordDeserializer.getReturnedBins(res);

        Assert.assertTrue(ASTestUtils.compareMapStringObj(resObject, binMap));
    }

    @Test
    public void GetDouble() throws Exception {
        Map<String, Object> binMap = new HashMap<>();
        Bin intBin = new Bin("double", 2.718);

        binMap.put("double", 2.718);

        client.put(null, this.testKey, intBin);

        MockHttpServletResponse res = mockMVC.perform(
                        get(noBinEndpoint).contentType(MediaType.APPLICATION_JSON).accept(currentMediaType))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        Map<String, Object> resObject = recordDeserializer.getReturnedBins(res);

        Assert.assertTrue(ASTestUtils.compareMapStringObj(resObject, binMap));
    }

    @Test
    public void GetList() throws Exception {
        Map<String, Object> binMap = new HashMap<>();
        List<String> putList = Arrays.asList("a", "e", "r", "o");
        Bin intBin = new Bin("list", putList);

        binMap.put("list", putList);

        client.put(null, this.testKey, intBin);

        MockHttpServletResponse res = mockMVC.perform(
                        get(noBinEndpoint).contentType(MediaType.APPLICATION_JSON).accept(currentMediaType))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        Map<String, Object> resObject = recordDeserializer.getReturnedBins(res);

        Assert.assertTrue(ASTestUtils.compareMapStringObj(resObject, binMap));
    }

    @Test
    public void GetMap() throws Exception {
        Map<String, Object> binMap = new HashMap<>();
        Map<Object, Object> putMap = new HashMap<>();
        putMap.put("aero", "spike");
        putMap.put("int", 5);

        Bin intBin = new Bin("map", putMap);

        binMap.put("map", putMap);

        client.put(null, this.testKey, intBin);

        MockHttpServletResponse res = mockMVC.perform(
                        get(noBinEndpoint).contentType(MediaType.APPLICATION_JSON).accept(currentMediaType))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        Map<String, Object> resObject = recordDeserializer.getReturnedBins(res);

        Assert.assertTrue(ASTestUtils.compareMapStringObj(resObject, binMap));
    }

    /*
     * Ensure that passing a number of bin names at the end of the URI
     * causes only the specified bins to be contained in the Rest Gateway's response
     */
    @Test
    public void GetFilteredByBins() throws Exception {
        Map<String, Object> binMap = new HashMap<>();
        String[] returnBins = {"A", "B", "E"};

        Bin binA = new Bin("A", 1);
        Bin binB = new Bin("B", 2);
        Bin binC = new Bin("C", 3);
        Bin binD = new Bin("D", 4);
        Bin binE = new Bin("E", 5);

        /* Only expect the names of bins we specify*/
        binMap.put("A", 1);
        binMap.put("B", 2);
        binMap.put("E", 5);

        client.put(null, this.testKey, binA, binB, binC, binD, binE);

        /* Get the record and only fetch bins A, B, and E */
        MockHttpServletResponse res = mockMVC.perform(
                get(ASTestUtils.addFilterBins(noBinEndpoint, returnBins)).contentType(MediaType.APPLICATION_JSON)
                        .accept(currentMediaType)).andExpect(status().isOk()).andReturn().getResponse();

        Map<String, Object> resObject = recordDeserializer.getReturnedBins(res);

        Assert.assertTrue(ASTestUtils.compareMapStringObj(resObject, binMap));
    }

    /*
     * Test to ensure that a user can specify that the userKey is an integer by appending
     * the correct query parameter to a get request.
     */
    @Test
    public void TestIntegerKey() throws Exception {
        Map<String, Object> binMap = new HashMap<>();
        binMap.put("id", "integer");
        keysToRemove.add(intKey);

        Bin idBin = new Bin("id", "integer");
        client.put(null, intKey, idBin);

        MockHttpServletResponse res = mockMVC.perform(
                        get(intEndpoint).contentType(MediaType.APPLICATION_JSON).accept(currentMediaType))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        Map<String, Object> resObject = recordDeserializer.getReturnedBins(res);

        Assert.assertTrue(ASTestUtils.compareMapStringObj(resObject, binMap));
    }

    @Test
    public void GetWithDigest() throws Exception {
        Map<String, Object> binMap = new HashMap<>();
        binMap.put("integer", 10);
        Bin intBin = new Bin("integer", 10);

        client.put(null, this.testKey, intBin);

        MockHttpServletResponse res = mockMVC.perform(
                        get(digestEndpoint).contentType(MediaType.APPLICATION_JSON).accept(currentMediaType))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        Map<String, Object> resObject = recordDeserializer.getReturnedBins(res);

        Assert.assertTrue(ASTestUtils.compareMapStringObj(resObject, binMap));
    }

    @Test
    public void TestBytesKey() throws Exception {
        Map<String, Object> binMap = new HashMap<>();
        keysToRemove.add(bytesKey);
        Bin idBin = new Bin("id", "bytes");
        client.put(null, bytesKey, idBin);

        binMap.put("id", "bytes");
        MockHttpServletResponse res = mockMVC.perform(
                        get(bytesEndpoint).contentType(MediaType.APPLICATION_JSON).accept(currentMediaType))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        Map<String, Object> resObject = recordDeserializer.getReturnedBins(res);

        Assert.assertTrue(ASTestUtils.compareMapStringObj(resObject, binMap));
    }

}

interface RecordDeserializer {
    Map<String, Object> getReturnedBins(MockHttpServletResponse res);
}

class MsgPackRestRecordDeserializer implements RecordDeserializer {

    ObjectMapper msgPackMapper;

    public MsgPackRestRecordDeserializer() {
        msgPackMapper = new ObjectMapper(new MessagePackFactory());
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> getReturnedBins(MockHttpServletResponse res) {
        byte[] response = res.getContentAsByteArray();
        TypeReference<Map<String, Object>> sOMapType = new TypeReference<Map<String, Object>>() {
        };
        Map<String, Object> recMap = null;
        try {
            recMap = msgPackMapper.readValue(response, sOMapType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (Map<String, Object>) Objects.requireNonNull(recMap).get("bins");
    }
}

class JSONRestRecordDeserializer implements RecordDeserializer {

    ObjectMapper mapper;

    public JSONRestRecordDeserializer() {
        this.mapper = new ObjectMapper();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> getReturnedBins(MockHttpServletResponse res) {
        String response = null;
        try {
            response = res.getContentAsString();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        TypeReference<Map<String, Object>> sOMapType = new TypeReference<Map<String, Object>>() {
        };
        Map<String, Object> recMap = null;
        try {
            recMap = mapper.readValue(response, sOMapType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (Map<String, Object>) Objects.requireNonNull(recMap).get("bins");
    }
}
