/*
 * Copyright 2019 Aerospike, Inc.
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
import com.aerospike.client.Record;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class RecordPutCorrectTests {

    @Autowired
    private ObjectMapper objectMapper;

    private PutPerformer putPerformer;
    private MockMvc mockMVC;

    @Autowired
    private AerospikeClient client;

    @Autowired
    private WebApplicationContext wac;

    private Key testKey;
    private Key intKey;
    private Key bytesKey;
    private String testEndpoint;
    private String digestEndpoint;
    private String intEndpoint;
    private String bytesEndpoint;

    @BeforeEach
    public void setup() {
        mockMVC = MockMvcBuilders.webAppContextSetup(wac).build();
        Bin baseBin = new Bin("initial", "bin");
        client.put(null, testKey, baseBin);
        client.put(null, intKey, baseBin);
        client.put(null, bytesKey, baseBin);
    }

    @AfterEach
    public void clean() {
        client.delete(null, testKey);
        client.delete(null, intKey);
        client.delete(null, bytesKey);
    }

    private static Stream<Arguments> getParams() {
        return Stream.of(
                Arguments.of(new JSONPutPerformer(MediaType.APPLICATION_JSON.toString(), new ObjectMapper()), true),
                Arguments.of(new MsgPackPutPerformer("application/msgpack", new ObjectMapper(new MessagePackFactory())), true),
                Arguments.of(new JSONPutPerformer(MediaType.APPLICATION_JSON.toString(), new ObjectMapper()), false),
                Arguments.of(new MsgPackPutPerformer("application/msgpack", new ObjectMapper(new MessagePackFactory())), false)
        );
    }

    @ParameterizedTest
    @MethodSource("getParams")
    void addParams(PutPerformer performer, boolean useSet) {
        if (useSet) {
            this.testEndpoint = ASTestUtils.buildEndpoint("kvs", "test", "junit", "getput");
            this.testKey = new Key("test", "junit", "getput");
            this.intKey = new Key("test", "junit", 1);
            this.bytesKey = new Key("test", "junit", new byte[]{1, 127, 127, 1});

            String urlDigest = Base64.getUrlEncoder().encodeToString(testKey.digest);
            digestEndpoint = ASTestUtils.buildEndpoint("kvs", "test", "junit", urlDigest) + "?keytype=DIGEST";

            String urlBytes = Base64.getUrlEncoder().encodeToString((byte[]) bytesKey.userKey.getObject());
            bytesEndpoint = ASTestUtils.buildEndpoint("kvs", "test", "junit", urlBytes) + "?keytype=BYTES";

            intEndpoint = ASTestUtils.buildEndpoint("kvs", "test", "junit", "1") + "?keytype=INTEGER";

        } else {
            this.testEndpoint = ASTestUtils.buildEndpoint("kvs", "test", "getput");
            this.testKey = new Key("test", null, "getput");
            this.intKey = new Key("test", null, 1);
            this.bytesKey = new Key("test", null, new byte[]{1, 127, 127, 1});

            String urlDigest = Base64.getUrlEncoder().encodeToString(testKey.digest);
            digestEndpoint = ASTestUtils.buildEndpoint("kvs", "test", urlDigest) + "?keytype=DIGEST";

            String urlBytes = Base64.getUrlEncoder().encodeToString((byte[]) bytesKey.userKey.getObject());
            bytesEndpoint = ASTestUtils.buildEndpoint("kvs", "test", urlBytes) + "?keytype=BYTES";

            intEndpoint = ASTestUtils.buildEndpoint("kvs", "test", "1") + "?keytype=INTEGER";
        }
        this.putPerformer = performer;
    }

    @Test
    public void PutInteger() throws Exception {
        Map<String, Object> binMap = new HashMap<>();

        binMap.put("integer", 12345);

        putPerformer.perform(mockMVC, testEndpoint, binMap);

        Record record = client.get(null, this.testKey);
        assertFalse(record.bins.containsKey("initial"));
        assertEquals(record.bins.get("integer"), 12345L);
    }

    @Test
    public void PutString() throws Exception {
        Map<String, Object> binMap = new HashMap<>();

        binMap.put("string", "Aerospike");

        putPerformer.perform(mockMVC, testEndpoint, binMap);

        Record record = client.get(null, this.testKey);
        assertFalse(record.bins.containsKey("initial"));
        assertEquals(record.bins.get("string"), "Aerospike");
    }

    @Test
    public void PutDouble() throws Exception {
        Map<String, Object> binMap = new HashMap<>();

        binMap.put("double", 2.718);

        putPerformer.perform(mockMVC, testEndpoint, binMap);

        Record record = client.get(null, this.testKey);
        assertFalse(record.bins.containsKey("initial"));
        assertEquals(record.bins.get("double"), 2.718);
    }

    @Test
    public void PutList() throws Exception {
        Map<String, Object> binMap = new HashMap<>();

        List<?> trueList = Arrays.asList(1L, "a", 3.5);

        binMap.put("ary", trueList);

        putPerformer.perform(mockMVC, testEndpoint, binMap);

        Record record = client.get(null, this.testKey);

        assertFalse(record.bins.containsKey("initial"));
        assertTrue(ASTestUtils.compareCollection((List<?>) record.bins.get("ary"), trueList));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void PutMapStringKeys() throws Exception {
        Map<Object, Object> testMap = new HashMap<>();
        testMap.put("string", "a string");
        testMap.put("long", 2L);
        testMap.put("double", 4.5);

        Map<String, Object> binMap = new HashMap<>();
        binMap.put("map", testMap);

        putPerformer.perform(mockMVC, testEndpoint, binMap);

        Record record = client.get(null, this.testKey);

        assertFalse(record.bins.containsKey("initial"));
        assertTrue(ASTestUtils.compareMap((Map<Object, Object>) record.bins.get("map"), testMap));
    }

    @Test
    public void PutIntegerKey() throws Exception {
        Map<String, Object> binMap = new HashMap<>();
        binMap.put("integer", 12345);

        putPerformer.perform(mockMVC, intEndpoint, binMap);

        Record record = client.get(null, this.intKey);
        assertFalse(record.bins.containsKey("initial"));
        assertEquals(record.bins.get("integer"), 12345L);
    }

    @Test
    public void PutBytesKey() throws Exception {
        Map<String, Object> binMap = new HashMap<>();
        binMap.put("integer", 12345);

        putPerformer.perform(mockMVC, bytesEndpoint, binMap);

        Record record = client.get(null, this.bytesKey);
        assertFalse(record.bins.containsKey("initial"));
        assertEquals(record.bins.get("integer"), 12345L);
    }

    @Test
    public void PutDigestKey() throws Exception {
        Map<String, Object> binMap = new HashMap<>();
        binMap.put("integer", 12345);

        putPerformer.perform(mockMVC, digestEndpoint, binMap);

        Record record = client.get(null, this.testKey);
        assertFalse(record.bins.containsKey("initial"));
        assertEquals(record.bins.get("integer"), 12345L);
    }

    @Test
    public void PutIntegerWithGenerationMismatch() throws Exception {
        Map<String, Object> binMap = new HashMap<>();
        binMap.put("integer", 12345);

        String queryParams = "?generation=150&generationPolicy=EXPECT_GEN_EQUAL";

        mockMVC.perform(put(testEndpoint + queryParams).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(binMap)))
                .andExpect(status().isConflict());

        Record record = client.get(null, this.testKey);
        assertTrue(record.bins.containsKey("initial"));
        assertFalse(record.bins.containsKey("integer"));
    }
}

interface PutPerformer {
    void perform(MockMvc mockMVC, String testEndpoint, Map<String, Object> binMap) throws Exception;
}

class JSONPutPerformer implements PutPerformer {
    String mediaType;
    ObjectMapper mapper;

    public JSONPutPerformer(String mediaType, ObjectMapper mapper) {
        this.mediaType = mediaType;
        this.mapper = mapper;
    }

    @Override
    public void perform(MockMvc mockMVC, String testEndpoint, Map<String, Object> binMap)
            throws Exception {
        mockMVC.perform(put(testEndpoint).contentType(mediaType)
                .content(mapper.writeValueAsString(binMap)))
                .andExpect(status().isNoContent());
    }
}

class MsgPackPutPerformer implements PutPerformer {
    String mediaType;
    ObjectMapper mapper;

    public MsgPackPutPerformer(String mediaType, ObjectMapper mapper) {
        this.mediaType = mediaType;
        this.mapper = mapper;
    }

    @Override
    public void perform(MockMvc mockMVC, String testEndpoint, Map<String, Object> binMap)
            throws Exception {
        mockMVC.perform(put(testEndpoint).contentType(mediaType)
                .content(mapper.writeValueAsBytes(binMap)))
                .andExpect(status().isNoContent());
    }
}
