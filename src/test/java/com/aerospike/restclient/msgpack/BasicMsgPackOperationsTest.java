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
package com.aerospike.restclient.msgpack;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.AerospikeException;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.restclient.ASTestUtils;
import com.aerospike.restclient.MsgPackOperationV1Performer;
import com.aerospike.restclient.util.AerospikeOperation;
import com.aerospike.restclient.util.deserializers.MsgPackBinParser;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.aerospike.restclient.util.AerospikeAPIConstants.OPERATION_FIELD;
import static com.aerospike.restclient.util.AerospikeAPIConstants.OPERATION_VALUES_FIELD;

@RunWith(Parameterized.class)
@SpringBootTest
public class BasicMsgPackOperationsTest {

    @ClassRule
    public static final SpringClassRule springClassRule = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @Autowired
    private AerospikeClient client;

    @Autowired
    private WebApplicationContext wac;

    private final MsgPackOperationV1Performer opPerformer = new MsgPackOperationV1Performer();

    private MockMvc mockMVC;

    private final Key testKey;
    private final String testEndpoint;

    @Parameters
    public static Object[] getParams() {
        return new Object[]{
                true, false
        };
    }

    public BasicMsgPackOperationsTest(boolean useSet) {
        if (useSet) {
            testKey = new Key("test", "junit", "mpoperate");
            testEndpoint = ASTestUtils.buildEndpointV1("operate", "test", "junit", "mpoperate");
        } else {
            testKey = new Key("test", null, "mpoperate");
            testEndpoint = ASTestUtils.buildEndpointV1("operate", "test", "mpoperate");
        }
    }

    @Before
    public void setup() {
        mockMVC = MockMvcBuilders.webAppContextSetup(wac).build();
        client.put(null, testKey, new Bin("a", "b"));
    }

    @After
    public void clean() {
        try {
            client.delete(null, testKey);
        } catch (AerospikeException ignore) {
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetWithNonJson() throws Exception {
        List<Map<String, Object>> opList = new ArrayList<>();
        Map<String, Object> opMap = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();
        opMap.put(OPERATION_FIELD, AerospikeOperation.GET);
        opMap.put(OPERATION_VALUES_FIELD, opValues);

        opList.add(opMap);

        Map<Object, Object> imap = new HashMap<>();
        imap.put(1L, "long");
        Bin mb = new Bin("map", imap);

        client.put(null, testKey, new Bin("bytes", new byte[]{1, 2, 3}), mb);
        byte[] binBytes = opPerformer.performOperationsAndReturnRaw(mockMVC, testEndpoint, opList);

        MsgPackBinParser bParser = new MsgPackBinParser(new ByteArrayInputStream(binBytes));
        Map<String, Object> retRec = bParser.parseBins();
        Map<String, Object> opBins = (Map<String, Object>) retRec.get("bins");
        Map<String, Object> realBins = client.get(null, testKey).bins;

        Assert.assertTrue(ASTestUtils.compareMapStringObj(opBins, realBins));
    }

    @Test
    public void testPutIntMapKey() throws Exception {
        /*
         * [{OPERATION_FIELD:"PUT", OPERATION_VALUES_FIELD:{"bin":"new", "value":{1:2}}}]
         */
        MessageBufferPacker packer = new MessagePack.PackerConfig().newBufferPacker();
        packer.packArrayHeader(1);

        packer.packMapHeader(2);

        packer.packString(OPERATION_FIELD);
        packer.packString(AerospikeOperation.PUT.name());

        packer.packString(OPERATION_VALUES_FIELD);
        packer.packMapHeader(2);
        packer.packString("bin");
        packer.packString("new");
        packer.packString("value");
        packer.packMapHeader(1);
        packer.packLong(1);
        packer.packLong(2);

        byte[] opBytes = packer.toByteArray();
        opPerformer.performOperationsAndReturnRaw(mockMVC, testEndpoint, opBytes);

        Map<String, Object> realBins = client.get(null, testKey).bins;
        @SuppressWarnings("unchecked") Map<Long, Long> expectedMap = (Map<Long, Long>) realBins.get("new");

        Assert.assertEquals(expectedMap.size(), 1);
        Assert.assertEquals((Long) 2L, expectedMap.get(1L));
    }

    @Test
    public void testPutOpBytes() throws Exception {
        List<Map<String, Object>> opList = new ArrayList<>();
        Map<String, Object> opMap = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();

        opValues.put("bin", "new");
        opValues.put("value", new byte[]{1, 2, 3});
        opMap.put(OPERATION_FIELD, AerospikeOperation.PUT.name());
        opMap.put(OPERATION_VALUES_FIELD, opValues);
        opList.add(opMap);

        opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opList);

        Map<String, Object> expectedBins = new HashMap<>();
        expectedBins.put("a", "b");
        expectedBins.put("new", new byte[]{1, 2, 3});

        Map<String, Object> realBins = client.get(null, testKey).bins;

        Assert.assertTrue(ASTestUtils.compareMapStringObj(expectedBins, realBins));
    }

    @Test
    public void testAppendOp() throws Exception {
        /*
         * Append [4,5] to a bytearray of [1,2,3] and verify that the bin ends up having [1,2,3,4,5]
         */
        client.put(null, testKey, new Bin("bytes", new byte[]{1, 2, 3}));
        List<Map<String, Object>> opList = new ArrayList<>();
        Map<String, Object> opMap = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();

        opMap.put(OPERATION_FIELD, AerospikeOperation.APPEND.name());
        opValues.put("value", new byte[]{4, 5});
        opValues.put("bin", "bytes");
        opMap.put(OPERATION_VALUES_FIELD, opValues);

        opList.add(opMap);
        opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opList);

        Map<String, Object> realBins = client.get(null, testKey, "bytes").bins;

        byte[] realBytes = (byte[]) realBins.get("bytes");
        Assert.assertArrayEquals(realBytes, new byte[]{1, 2, 3, 4, 5});
    }

    @Test
    public void testPrependOp() throws Exception {
        /*
         * Prepend [1,2] to a bytearray of [3,4,5] and verify that the bin ends up having [1,2,3,4,5]
         */
        client.put(null, testKey, new Bin("bytes", new byte[]{3, 4, 5}));
        List<Map<String, Object>> opList = new ArrayList<>();
        Map<String, Object> opMap = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();

        opMap.put(OPERATION_FIELD, AerospikeOperation.PREPEND.name());
        opValues.put("value", new byte[]{1, 2});
        opValues.put("bin", "bytes");
        opMap.put(OPERATION_VALUES_FIELD, opValues);

        opList.add(opMap);
        opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opList);

        Map<String, Object> realBins = client.get(null, testKey, "bytes").bins;

        byte[] realBytes = (byte[]) realBins.get("bytes");
        Assert.assertArrayEquals(realBytes, new byte[]{1, 2, 3, 4, 5});
    }

}
