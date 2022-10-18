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
import com.aerospike.restclient.util.AerospikeOperation;
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

import java.util.*;

@RunWith(Parameterized.class)
@SpringBootTest
public class OperateV2BitCorrectTests {

    @ClassRule
    public static final SpringClassRule springClassRule = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    private MockMvc mockMVC;

    @Autowired
    private AerospikeClient client;

    @Autowired
    private WebApplicationContext wac;

    byte[] byteArray = new byte[]{12, 5, 110, 47};
    private final Key testKey;
    private final String testEndpoint;

    private final String OPERATION_TYPE_KEY = "type";

    private Map<String, Object> opRequest;
    private List<Map<String, Object>> opList;

    @Before
    public void setup() {
        mockMVC = MockMvcBuilders.webAppContextSetup(wac).build();
        Bin bitBin = new Bin("bit", byteArray);
        client.put(null, testKey, bitBin);
        opList = new ArrayList<>();
        opRequest = new HashMap<>();
        opRequest.put("opsList", opList);
    }

    @After
    public void clean() {
        client.delete(null, testKey);
    }

    private final OperationV2Performer opPerformer;

    @Parameterized.Parameters
    public static Object[][] getParams() {
        return new Object[][]{
                {new JSONOperationV2Performer(), true},
                {new MsgPackOperationV2Performer(), true},
                {new JSONOperationV2Performer(), false},
                {new MsgPackOperationV2Performer(), false}
        };
    }

    /* Set up the correct msgpack/json performer for this set of runs. Also decided whether to use the endpoint with a set or without */
    public OperateV2BitCorrectTests(OperationV2Performer performer, boolean useSet) {
        this.opPerformer = performer;
        if (useSet) {
            testKey = new Key("test", "junit", "bitop");
            testEndpoint = ASTestUtils.buildEndpointV2("operate", "test", "junit", "bitop");
        } else {
            testKey = new Key("test", null, "bitop");
            testEndpoint = ASTestUtils.buildEndpointV2("operate", "test", "bitop");
        }
    }

    @Test
    public void testBitResize() {
        Map<String, Object> opMap = new HashMap<>();
        opMap.put("binName", "bit");
        opMap.put("byteSize", 8);
        opMap.put("resizeFlags", 0);
        opMap.put(OPERATION_TYPE_KEY, AerospikeOperation.BIT_RESIZE);
        opList.add(opMap);

        opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest);

        byte[] realByteArray = (byte[]) client.get(null, testKey).bins.get("bit");

        Assert.assertArrayEquals(new byte[]{12, 5, 110, 47, 0, 0, 0, 0}, realByteArray);
    }

    @Test
    public void testBitInsert() {
        Map<String, Object> opMap = new HashMap<>();
        opMap.put("binName", "bit");
        opMap.put("byteOffset", 1);
        opMap.put("value", new byte[]{11});
        opMap.put(OPERATION_TYPE_KEY, AerospikeOperation.BIT_INSERT);
        opList.add(opMap);

        opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest);

        byte[] realByteArray = (byte[]) client.get(null, testKey).bins.get("bit");

        Assert.assertArrayEquals(new byte[]{12, 11, 5, 110, 47}, realByteArray);
    }

    @Test
    public void testBitRemove() {
        Map<String, Object> opMap = new HashMap<>();
        opMap.put("binName", "bit");
        opMap.put("byteOffset", 1);
        opMap.put("byteSize", 2);
        opMap.put(OPERATION_TYPE_KEY, AerospikeOperation.BIT_REMOVE);
        opList.add(opMap);

        opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest);

        byte[] realByteArray = (byte[]) client.get(null, testKey).bins.get("bit");

        Assert.assertArrayEquals(new byte[]{12, 47}, realByteArray);
    }

    @Test
    public void testBitSet() {
        Map<String, Object> opMap = new HashMap<>();
        opMap.put("binName", "bit");
        opMap.put("bitOffset", 16);
        opMap.put("value", new byte[]{127});
        opMap.put("bitSize", 4);
        opMap.put(OPERATION_TYPE_KEY, AerospikeOperation.BIT_SET);
        opList.add(opMap);

        opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest);

        byte[] realByteArray = (byte[]) client.get(null, testKey).bins.get("bit");

        Assert.assertArrayEquals(new byte[]{12, 5, 126, 47}, realByteArray);
    }

    @Test
    public void testBitOr() {
        Map<String, Object> opMap = new HashMap<>();
        opMap.put("binName", "bit");
        opMap.put("bitOffset", 16);
        opMap.put("value", new byte[]{57});
        opMap.put("bitSize", 8);
        opMap.put(OPERATION_TYPE_KEY, AerospikeOperation.BIT_OR);
        opList.add(opMap);

        opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest);

        byte[] realByteArray = (byte[]) client.get(null, testKey).bins.get("bit");

        Assert.assertArrayEquals(new byte[]{12, 5, 127, 47}, realByteArray);
    }

    @Test
    public void testBitXor() {
        Map<String, Object> opMap = new HashMap<>();
        opMap.put("binName", "bit");
        opMap.put("bitOffset", 16);
        opMap.put("value", new byte[]{57});
        opMap.put("bitSize", 8);
        opMap.put(OPERATION_TYPE_KEY, AerospikeOperation.BIT_XOR);
        opList.add(opMap);

        opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest);

        byte[] realByteArray = (byte[]) client.get(null, testKey).bins.get("bit");

        Assert.assertArrayEquals(new byte[]{12, 5, 87, 47}, realByteArray);
    }

    @Test
    public void testBitAnd() {
        Map<String, Object> opMap = new HashMap<>();
        opMap.put("binName", "bit");
        opMap.put("bitOffset", 16);
        opMap.put("value", new byte[]{57});
        opMap.put("bitSize", 8);
        opMap.put(OPERATION_TYPE_KEY, AerospikeOperation.BIT_AND);
        opList.add(opMap);

        opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest);

        byte[] realByteArray = (byte[]) client.get(null, testKey).bins.get("bit");

        Assert.assertArrayEquals(new byte[]{12, 5, 40, 47}, realByteArray);
    }

    @Test
    public void testBitNot() {
        Map<String, Object> opMap = new HashMap<>();
        opMap.put("binName", "bit");
        opMap.put("bitOffset", 16);
        opMap.put("bitSize", 16);
        opMap.put(OPERATION_TYPE_KEY, AerospikeOperation.BIT_NOT);
        opList.add(opMap);

        opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest);

        byte[] realByteArray = (byte[]) client.get(null, testKey).bins.get("bit");

        Assert.assertArrayEquals(new byte[]{12, 5, -111, -48}, realByteArray);
    }

    @Test
    public void testBitLshift() {
        Map<String, Object> opMap = new HashMap<>();
        opMap.put("binName", "bit");
        opMap.put("bitOffset", 24);
        opMap.put("bitSize", 8);
        opMap.put("shift", 3);
        opMap.put(OPERATION_TYPE_KEY, AerospikeOperation.BIT_LSHIFT);
        opList.add(opMap);

        opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest);

        byte[] realByteArray = (byte[]) client.get(null, testKey).bins.get("bit");

        Assert.assertArrayEquals(new byte[]{12, 5, 110, 120}, realByteArray);
    }

    @Test
    public void testBitRshift() {
        Map<String, Object> opMap = new HashMap<>();
        opMap.put("binName", "bit");
        opMap.put("bitOffset", 24);
        opMap.put("bitSize", 8);
        opMap.put("shift", 3);
        opMap.put(OPERATION_TYPE_KEY, AerospikeOperation.BIT_RSHIFT);
        opList.add(opMap);

        opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest);

        byte[] realByteArray = (byte[]) client.get(null, testKey).bins.get("bit");

        Assert.assertArrayEquals(new byte[]{12, 5, 110, 5}, realByteArray);
    }

    @Test
    public void testBitAdd() {
        Map<String, Object> opMap = new HashMap<>();
        opMap.put("binName", "bit");
        opMap.put("bitOffset", 24);
        opMap.put("bitSize", 8);
        opMap.put("value", 3);
        opMap.put(OPERATION_TYPE_KEY, AerospikeOperation.BIT_ADD);
        opList.add(opMap);

        opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest);

        byte[] realByteArray = (byte[]) client.get(null, testKey).bins.get("bit");

        Assert.assertArrayEquals(new byte[]{12, 5, 110, 50}, realByteArray);
    }

    @Test
    public void testBitSubtract() {
        Map<String, Object> opMap = new HashMap<>();
        opMap.put("binName", "bit");
        opMap.put("bitOffset", 24);
        opMap.put("bitSize", 8);
        opMap.put("value", 3);
        opMap.put(OPERATION_TYPE_KEY, AerospikeOperation.BIT_SUBTRACT);
        opList.add(opMap);

        opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest);

        byte[] realByteArray = (byte[]) client.get(null, testKey).bins.get("bit");

        Assert.assertArrayEquals(new byte[]{12, 5, 110, 44}, realByteArray);
    }

    @Test
    public void testBitSetInt() {
        Map<String, Object> opMap = new HashMap<>();
        opMap.put("binName", "bit");
        opMap.put("bitOffset", 24);
        opMap.put("bitSize", 8);
        opMap.put("value", 3);
        opMap.put(OPERATION_TYPE_KEY, AerospikeOperation.BIT_SET_INT);
        opList.add(opMap);

        opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest);

        byte[] realByteArray = (byte[]) client.get(null, testKey).bins.get("bit");

        Assert.assertArrayEquals(new byte[]{12, 5, 110, 3}, realByteArray);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testBitGet() {
        Map<String, Object> opMap = new HashMap<>();
        opMap.put("binName", "bit");
        opMap.put("bitOffset", 24);
        opMap.put("bitSize", 8);
        opMap.put(OPERATION_TYPE_KEY, AerospikeOperation.BIT_GET);
        opList.add(opMap);

        Map<String, Object> res = opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest);
        Map<String, Object> record = (Map<String, Object>) res.get("record");
        byte[] expected;
        try {
            expected = Base64.getDecoder().decode(((Map<String, String>) record.get("bins")).get("bit").getBytes());
        } catch (ClassCastException e) {
            expected = (byte[]) ((Map<String, Object>) record.get("bins")).get("bit");
        }

        Assert.assertArrayEquals(new byte[]{47}, expected);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testBitCount() {
        Map<String, Object> opMap = new HashMap<>();
        opMap.put("binName", "bit");
        opMap.put("bitOffset", 20);
        opMap.put("bitSize", 4);
        opMap.put(OPERATION_TYPE_KEY, AerospikeOperation.BIT_COUNT);
        opList.add(opMap);

        Map<String, Object> res = opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest);
        Map<String, Object> record = (Map<String, Object>) res.get("record");
        int expected = ((Map<String, Integer>) record.get("bins")).get("bit");

        Assert.assertEquals(3, expected);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testBitLscan() {
        Map<String, Object> opMap = new HashMap<>();
        opMap.put("binName", "bit");
        opMap.put("bitOffset", 16);
        opMap.put("bitSize", 8);
        opMap.put("value", true);
        opMap.put(OPERATION_TYPE_KEY, AerospikeOperation.BIT_LSCAN);
        opList.add(opMap);

        Map<String, Object> res = opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest);
        Map<String, Object> record = (Map<String, Object>) res.get("record");
        int expected = ((Map<String, Integer>) record.get("bins")).get("bit");

        Assert.assertEquals(1, expected);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testBitRscan() {
        Map<String, Object> opMap = new HashMap<>();
        opMap.put("binName", "bit");
        opMap.put("bitOffset", 16);
        opMap.put("bitSize", 8);
        opMap.put(OPERATION_TYPE_KEY, AerospikeOperation.BIT_RSCAN);
        opList.add(opMap);

        Map<String, Object> res = opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest);
        Map<String, Object> record = (Map<String, Object>) res.get("record");
        int expected = ((Map<String, Integer>) record.get("bins")).get("bit");

        Assert.assertEquals(7, expected);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testBitGetInt() {
        Map<String, Object> opMap = new HashMap<>();
        opMap.put("binName", "bit");
        opMap.put("bitOffset", 12);
        opMap.put("bitSize", 16);
        opMap.put(OPERATION_TYPE_KEY, AerospikeOperation.BIT_GET_INT);
        opList.add(opMap);

        Map<String, Object> res = opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest);
        Map<String, Object> record = (Map<String, Object>) res.get("record");
        int expected = ((Map<String, Integer>) record.get("bins")).get("bit");

        Assert.assertEquals(22242, expected);
    }

}

