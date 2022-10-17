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

import com.aerospike.client.Value.GeoJSONValue;
import com.aerospike.restclient.ASTestUtils;
import com.aerospike.restclient.domain.RestClientOperation;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.aerospike.restclient.util.AerospikeOperation;
import com.aerospike.restclient.util.RestClientErrors.MalformedMsgPackError;
import com.aerospike.restclient.util.deserializers.MsgPackOperationsParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;
import org.msgpack.jackson.dataformat.MessagePackFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * These tests do not actually test whether valid operations are generated.
 * They simply test that the OperationParser class works as expected.
 * The main method of that class is a conversion from MsgPack to List<Map<String, Object>>
 * So the tests here just exercise a variety of value types to ensure that conversion works.
 * The actual operation behavior is checked in the operate, listoperate, mapoperate tests
 */
public class OperationParserTest {

    private static final String testOpName = AerospikeOperation.TOUCH.name();

    @Test
    public void testStringValue() throws IOException {
        singleOperationParseTest("aerospike");
    }

    @Test
    public void testLongValue() throws IOException {
        singleOperationParseTest(5L);
    }

    @Test
    public void testFloatValue() throws IOException {
        singleOperationParseTest(3.14);
    }

    @Test
    public void testListValue() throws IOException {
        singleOperationParseTest(Arrays.asList(1, 2, 3));
    }

    @Test
    public void testBytesValue() throws IOException {
        byte[] testBytes = new byte[]{1, 127, 127, 1};
        singleOperationParseTest(testBytes);
    }

    @Test
    public void testGeoJSONValue() throws IOException {

        String geoString = "{\"coordinates\": [-122.0, 37.5], \"type\": \"Point\"}";
        MessageBufferPacker packer = new MessagePack.PackerConfig().newBufferPacker();
        // Pack the List
        packer.packArrayHeader(1);

        //Pack the op Map
        packer.packMapHeader(2);

        //Pack the operation field {"opValues => "TOUCH"}
        packer.packString(AerospikeAPIConstants.OPERATION_FIELD);
        packer.packString(testOpName);

        packer.packString(AerospikeAPIConstants.OPERATION_VALUES_FIELD);
        //Pack the opvalues {"opValues=>{}"}
        packer.packMapHeader(1);

        // {"value"=>geojson"}
        packer.packString("value");
        packer.packExtensionTypeHeader((byte) 23, geoString.length());
        packer.addPayload(geoString.getBytes(StandardCharsets.UTF_8));
        byte[] geoMsgPackBin = packer.toByteArray();

        MsgPackOperationsParser parser = new MsgPackOperationsParser(new ByteArrayInputStream(geoMsgPackBin));
        List<RestClientOperation> parsedOps = parser.parseOperations();

        Assert.assertEquals(parsedOps.size(), 1);

        RestClientOperation convertedOp = parsedOps.get(0);
        Map<String, Object> convertedValues = convertedOp.getOpValues();
        Object fetchedGeo = convertedValues.get("value");
        Assert.assertTrue(fetchedGeo instanceof GeoJSONValue);
        Assert.assertEquals(((GeoJSONValue) fetchedGeo).toString(), geoString);
    }

    @Test
    public void testNestedList() throws IOException {
        singleOperationParseTest(Arrays.asList(1, 2, Arrays.asList(3, 4)));
    }

    private void singleOperationParseTest(Object value) throws IOException {
        Map<String, Object> testOp = new HashMap<>();
        testOp.put(AerospikeAPIConstants.OPERATION_FIELD, AerospikeOperation.TOUCH.name());
        Map<String, Object> testOpVals = new HashMap<>();
        testOpVals.put("value", value);
        testOp.put(AerospikeAPIConstants.OPERATION_VALUES_FIELD, testOpVals);

        byte[] msgpackBin = singleOperationList(testOp);
        MsgPackOperationsParser parser = new MsgPackOperationsParser(new ByteArrayInputStream(msgpackBin));
        List<RestClientOperation> parsedOps = parser.parseOperations();
        RestClientOperation parsedOp = parsedOps.get(0);
        RestClientOperation testRCOp = new RestClientOperation(testOp);

        Assert.assertTrue(ASTestUtils.compareRCOperations(testRCOp, parsedOp));
    }

    @Test(expected = MalformedMsgPackError.class)
    public void nonListOps() throws IOException {
        MessageBufferPacker packer = new MessagePack.PackerConfig().newBufferPacker();
        packer.packInt(5);
        byte[] intBytes = packer.toByteArray();
        MsgPackOperationsParser parser = new MsgPackOperationsParser(new ByteArrayInputStream(intBytes));
        parser.parseOperations();
    }

    @Test(expected = MalformedMsgPackError.class)
    public void nonMapOperationTest() throws IOException {
        MessageBufferPacker packer = new MessagePack.PackerConfig().newBufferPacker();
        packer.packArrayHeader(1);
        packer.packInt(5);
        byte[] binBytes = packer.toByteArray();
        MsgPackOperationsParser parser = new MsgPackOperationsParser(new ByteArrayInputStream(binBytes));
        parser.parseOperations();
    }

    @Test(expected = MalformedMsgPackError.class)
    public void extraDatatest() throws IOException {
        MessageBufferPacker packer = new MessagePack.PackerConfig().newBufferPacker();
        packer.packArrayHeader(1);
        packer.packMapHeader(1);
        packer.packString("op");
        packer.packString("something");
        packer.packString("Something Extra");
        byte[] binBytes = packer.toByteArray();
        MsgPackOperationsParser parser = new MsgPackOperationsParser(new ByteArrayInputStream(binBytes));
        parser.parseOperations();
    }

    @Test(expected = MalformedMsgPackError.class)
    public void nonStringMapKeyTest() throws IOException {
        MessageBufferPacker packer = new MessagePack.PackerConfig().newBufferPacker();
        packer.packArrayHeader(1);
        packer.packMapHeader(1);
        packer.packInt(5);
        packer.packInt(5);
        byte[] binBytes = packer.toByteArray();
        MsgPackOperationsParser parser = new MsgPackOperationsParser(new ByteArrayInputStream(binBytes));
        parser.parseOperations();
    }

    /*
     * Pack the header for an array, and no additional data
     */
    @Test(expected = MalformedMsgPackError.class)
    public void testIncompleteOpList() throws IOException {
        MessageBufferPacker packer = new MessagePack.PackerConfig().newBufferPacker();

        packer.packArrayHeader(3);

        MsgPackOperationsParser parser = new MsgPackOperationsParser(new ByteArrayInputStream(packer.toByteArray()));

        parser.parseOperations();
    }

    @Test(expected = MalformedMsgPackError.class)
    public void testIncompleteOp() throws IOException {
        MessageBufferPacker packer = new MessagePack.PackerConfig().newBufferPacker();

        packer.packArrayHeader(1);
        packer.packMapHeader(2);

        MsgPackOperationsParser parser = new MsgPackOperationsParser(new ByteArrayInputStream(packer.toByteArray()));

        parser.parseOperations();
    }

    @Test(expected = MalformedMsgPackError.class)
    public void testEmptyMapdata() throws IOException {

        MsgPackOperationsParser parser = new MsgPackOperationsParser(new ByteArrayInputStream(new byte[]{}));
        parser.parseOperations();
    }
    /*
     * Errors
     */

    /*
     * Write a basic List<Map<String, Object>> to msgpack bytearray
     */
    private byte[] singleOperationList(Map<String, Object> op) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper(new MessagePackFactory());
        return mapper.writeValueAsBytes(Arrays.asList(op));
    }
}
