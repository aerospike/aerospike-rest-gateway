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

import com.aerospike.client.Record;
import com.aerospike.client.*;
import com.aerospike.restclient.config.JSONMessageConverter;
import com.aerospike.restclient.config.MsgPackConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(Parameterized.class)
@SpringBootTest
public class RecordPostCorrectTests {

    /* Needed to run as a Spring Boot test */
    @ClassRule
    public static final SpringClassRule springClassRule = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    private final PostPerformer postPerformer;
    private MockMvc mockMVC;

    @Autowired
    private AerospikeClient client;

    @Autowired
    private WebApplicationContext wac;

    private final Key testKey;
    private final Key intKey;
    private final Key bytesKey;
    private final String testEndpoint;
    private final String digestEndpoint;
    private final String intEndpoint;
    private final String bytesEndpoint;

    @Before
    public void setup() {
        mockMVC = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @After
    public void clean() {
        client.delete(null, testKey);
        try {
            client.delete(null, bytesKey);
        } catch (AerospikeException ignore) {
        }
        try {
            client.delete(null, intKey);
        } catch (AerospikeException ignore) {
        }
    }

    @Parameters
    public static Object[] getParams() {
        return new Object[][]{
                {
                        new JSONPostPerformer(MediaType.APPLICATION_JSON.toString(),
                                JSONMessageConverter.getJSONObjectMapper()), true
                }, {
                        new MsgPackPostPerformer("application/msgpack", MsgPackConverter.getASMsgPackObjectMapper()),
                        true
                }, {
                        new JSONPostPerformer(MediaType.APPLICATION_JSON.toString(),
                                JSONMessageConverter.getJSONObjectMapper()), false
                }, {
                        new MsgPackPostPerformer("application/msgpack", MsgPackConverter.getASMsgPackObjectMapper()),
                        false
                }
        };
    }

    public RecordPostCorrectTests(PostPerformer performer, boolean useSet) {
        if (useSet) {
            this.testEndpoint = ASTestUtils.buildEndpointV1("kvs", "test", "junit", "getput");
            this.testKey = new Key("test", "junit", "getput");
            this.intKey = new Key("test", "junit", 1);
            this.bytesKey = new Key("test", "junit", new byte[]{1, 127, 127, 1});

            String urlDigest = Base64.getUrlEncoder().encodeToString(testKey.digest);
            digestEndpoint = ASTestUtils.buildEndpointV1("kvs", "test", "junit", urlDigest) + "?keytype=DIGEST";

            String urlBytes = Base64.getUrlEncoder().encodeToString((byte[]) bytesKey.userKey.getObject());
            bytesEndpoint = ASTestUtils.buildEndpointV1("kvs", "test", "junit", urlBytes) + "?keytype=BYTES";

            intEndpoint = ASTestUtils.buildEndpointV1("kvs", "test", "junit", "1") + "?keytype=INTEGER";
        } else {
            this.testEndpoint = ASTestUtils.buildEndpointV1("kvs", "test", "getput");
            this.testKey = new Key("test", null, "getput");
            this.intKey = new Key("test", null, 1);
            this.bytesKey = new Key("test", null, new byte[]{1, 127, 127, 1});

            String urlDigest = Base64.getUrlEncoder().encodeToString(testKey.digest);
            digestEndpoint = ASTestUtils.buildEndpointV1("kvs", "test", urlDigest) + "?keytype=DIGEST";

            String urlBytes = Base64.getUrlEncoder().encodeToString((byte[]) bytesKey.userKey.getObject());
            bytesEndpoint = ASTestUtils.buildEndpointV1("kvs", "test", urlBytes) + "?keytype=BYTES";

            intEndpoint = ASTestUtils.buildEndpointV1("kvs", "test", "1") + "?keytype=INTEGER";
        }
        this.postPerformer = performer;
    }

    @Test
    public void PutInteger() throws Exception {
        Map<String, Object> binMap = new HashMap<>();

        binMap.put("integer", 12345);

        postPerformer.perform(mockMVC, testEndpoint, binMap);

        Record record = client.get(null, this.testKey);
        Assert.assertEquals(record.bins.get("integer"), 12345L);
    }

    @Test
    public void PutString() throws Exception {
        Map<String, Object> binMap = new HashMap<>();

        binMap.put("string", "Aerospike");

        postPerformer.perform(mockMVC, testEndpoint, binMap);

        Record record = client.get(null, this.testKey);
        Assert.assertEquals(record.bins.get("string"), "Aerospike");
    }

    @Test
    public void PutDouble() throws Exception {
        Map<String, Object> binMap = new HashMap<>();

        binMap.put("double", 2.718);

        postPerformer.perform(mockMVC, testEndpoint, binMap);

        Record record = client.get(null, this.testKey);
        Assert.assertEquals(record.bins.get("double"), 2.718);
    }

    @Test
    public void PutList() throws Exception {
        Map<String, Object> binMap = new HashMap<>();

        List<?> trueList = Arrays.asList(1L, "a", 3.5);

        binMap.put("ary", trueList);

        postPerformer.perform(mockMVC, testEndpoint, binMap);

        Record record = client.get(null, this.testKey);

        Assert.assertTrue(ASTestUtils.compareCollection((List<?>) record.bins.get("ary"), trueList));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void PutMapStringKeys() throws Exception {
        Map<String, Object> binMap = new HashMap<>();

        Map<Object, Object> testMap = new HashMap<>();

        testMap.put("string", "a string");
        testMap.put("long", 2L);
        testMap.put("double", 4.5);

        binMap.put("map", testMap);

        postPerformer.perform(mockMVC, testEndpoint, binMap);

        Record record = client.get(null, this.testKey);

        Assert.assertTrue(ASTestUtils.compareMap((Map<Object, Object>) record.bins.get("map"), testMap));
    }

    @Test
    public void PutWithIntegerKey() throws Exception {
        Map<String, Object> binMap = new HashMap<>();

        binMap.put("integer", 12345);

        postPerformer.perform(mockMVC, intEndpoint, binMap);

        Record record = client.get(null, this.intKey);
        Assert.assertEquals(record.bins.get("integer"), 12345L);
    }

    @Test
    public void PutWithBytesKey() throws Exception {
        Map<String, Object> binMap = new HashMap<>();
        binMap.put("integer", 12345);

        postPerformer.perform(mockMVC, bytesEndpoint, binMap);

        Record record = client.get(null, this.bytesKey);
        Assert.assertEquals(record.bins.get("integer"), 12345L);
    }

    @Test
    public void PutWithDigestKey() throws Exception {
        Map<String, Object> binMap = new HashMap<>();

        binMap.put("integer", 12345);

        postPerformer.perform(mockMVC, digestEndpoint, binMap);

        Record record = client.get(null, this.testKey);
        Assert.assertEquals(record.bins.get("integer"), 12345L);
    }

    @Test
    public void PostByteArray() throws Exception {
        Map<String, Object> binMap = new HashMap<>();
        Map<String, String> byteArray = new HashMap<>();
        byte[] arr = new byte[]{1, 101};
        byteArray.put("type", "BYTE_ARRAY");
        byteArray.put("value", Base64.getEncoder().encodeToString(arr));

        binMap.put("byte_array", byteArray);

        postPerformer.perform(mockMVC, testEndpoint, binMap);

        Record record = client.get(null, this.testKey);
        Assert.assertArrayEquals((byte[]) record.bins.get("byte_array"), arr);
    }

    @Test
    public void PostBase64EncodedGeoJson() throws Exception {
        Map<String, Object> binMap = new HashMap<>();
        Map<String, String> geoJson = new HashMap<>();
        String geoStr = "{\"type\": \"Point\", \"coordinates\": [-80.604333, 28.608389]}";
        geoJson.put("type", "GEO_JSON");
        geoJson.put("value", Base64.getEncoder().encodeToString(geoStr.getBytes()));

        binMap.put("geo_json", geoJson);

        postPerformer.perform(mockMVC, testEndpoint, binMap);

        Record record = client.get(null, this.testKey);
        Assert.assertEquals(((Value.GeoJSONValue) record.bins.get("geo_json")).getObject(), geoStr);
    }

    @Test
    public void PostGeoJson() throws Exception {
        Map<String, Object> binMap = new HashMap<>();
        Map<String, Object> geoJson = new HashMap<>();
        List<Double> coordinates = new ArrayList<>();
        coordinates.add(-80.604333);
        coordinates.add(28.608389);
        String geoStr = "{\"coordinates\":[-80.604333,28.608389],\"type\":\"Point\"}";
        geoJson.put("coordinates", coordinates);
        geoJson.put("type", "Point");

        binMap.put("geo_json", geoJson);

        postPerformer.perform(mockMVC, testEndpoint, binMap);

        Record record = client.get(null, this.testKey);
        Assert.assertEquals(((Value.GeoJSONValue) record.bins.get("geo_json")).getObject(), geoStr);
    }

    @Ignore("Fails because GeoJSON can't be nested in a CDT for JSON. Only MSGPack")
    @Test
    public void PostNestedGeoJson() throws Exception {
        Map<String, Object> binMap = new HashMap<>();
        Map<String, Object> geoJson = new HashMap<>();
        List<Double> coordinates = new ArrayList<>();
        coordinates.add(-80.604333);
        coordinates.add(28.608389);
        String geoStr = "{\"coordinates\":[-80.604333,28.608389],\"type\":\"Point\"}";
        geoJson.put("coordinates", coordinates);
        geoJson.put("type", "Point");
        List<Object> geoJsonList = new ArrayList<>();
        geoJsonList.add(geoJson);

        binMap.put("geo_json_list", geoJsonList);

        postPerformer.perform(mockMVC, testEndpoint, binMap);

        Record record = client.get(null, this.testKey);
        List<Object> actualGeoJsonList = (List<Object>) record.bins.get("geo_json_list");
        Assert.assertEquals(((Value.GeoJSONValue) actualGeoJsonList.get(0)).getObject(), geoStr);
    }

    @Test
    public void PostBoolean() throws Exception {
        Map<String, Object> binMap = new HashMap<>();

        binMap.put("bool", true);

        postPerformer.perform(mockMVC, testEndpoint, binMap);

        Record record = client.get(null, this.testKey);
        Assert.assertEquals((long) record.bins.get("bool"), 1L);
    }
}

interface PostPerformer {
    void perform(MockMvc mockMVC, String testEndpoint, Map<String, Object> binMap) throws Exception;
}

class JSONPostPerformer implements PostPerformer {
    String mediaType;
    ObjectMapper mapper;

    public JSONPostPerformer(String mediaType, ObjectMapper mapper) {
        this.mediaType = mediaType;
        this.mapper = mapper;
    }

    @Override
    public void perform(MockMvc mockMVC, String testEndpoint, Map<String, Object> binMap) throws Exception {
        mockMVC.perform(post(testEndpoint).contentType(mediaType).content(mapper.writeValueAsString(binMap)))
                .andExpect(status().isCreated());
    }

}

class MsgPackPostPerformer implements PostPerformer {
    String mediaType;
    ObjectMapper mapper;

    public MsgPackPostPerformer(String mediaType, ObjectMapper mapper) {
        this.mediaType = mediaType;
        this.mapper = mapper;
    }

    @Override
    public void perform(MockMvc mockMVC, String testEndpoint, Map<String, Object> binMap) throws Exception {
        mockMVC.perform(post(testEndpoint).contentType(mediaType).content(mapper.writeValueAsBytes(binMap)))
                .andExpect(status().isCreated());
    }

}