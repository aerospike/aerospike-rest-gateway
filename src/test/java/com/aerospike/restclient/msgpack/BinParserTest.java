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
import com.aerospike.restclient.util.RestClientErrors.MalformedMsgPackError;
import com.aerospike.restclient.util.deserializers.MsgPackBinParser;
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
import java.util.Map;

public class BinParserTest {

    @Test
    public void testStringBin() throws IOException {
        singleBinParseTest("aerospike");
    }

    @Test
    public void testLongBin() throws IOException {
        singleBinParseTest(5L);
    }

    @Test
    public void testFloatBin() throws IOException {
        singleBinParseTest(3.14);
    }

    @Test
    public void testListBin() throws IOException {
        singleBinParseTest(Arrays.asList(1, 2, 3));
    }

    /*
     * Test a map which can be represented as JSON
     */
    @Test
    public void testStringMapBin() throws IOException {
        Map<String, String> simpleMap = new HashMap<>();

        simpleMap.put("aero", "spike");
        simpleMap.put("is", "awesome");

        singleBinParseTest(simpleMap);
    }

    @Test
    public void testBytesBin() throws IOException {
        Map<String, byte[]> byteBins = new HashMap<>();
        byte[] testBytes = new byte[]{1, 127, 127, 1};
        byteBins.put("byte", testBytes);
        singleBinParseTest(byteBins);
    }

    @Test
    public void testGeoJSONBin() throws IOException {

        String geoString = "{\"coordinates\": [-122.0, 37.5], \"type\": \"Point\"}";
        MessageBufferPacker packer = new MessagePack.PackerConfig().newBufferPacker();
        packer.packMapHeader(1);
        packer.packString("geo");
        packer.packExtensionTypeHeader((byte) 23, geoString.length());
        packer.addPayload(geoString.getBytes(StandardCharsets.UTF_8));

        byte[] geoMsgPackBin = packer.toByteArray();

        MsgPackBinParser parser = new MsgPackBinParser(new ByteArrayInputStream(geoMsgPackBin));
        Map<String, Object> parsedBins = parser.parseBins();

        Assert.assertEquals(parsedBins.size(), 1);
        Assert.assertTrue(parsedBins.get("geo") instanceof GeoJSONValue);
        Assert.assertEquals(((GeoJSONValue) parsedBins.get("geo")).toString(), geoString);
    }

    @Test
    public void testNestedList() throws IOException {
        singleBinParseTest(Arrays.asList(1, 2, Arrays.asList(3, 4)));
    }

    @Test
    public void multiBinTest() throws IOException {
        Map<String, Object> testBins = new HashMap<>();
        testBins.put("string", "aerospike");
        testBins.put("long", 1l);
        testBins.put("float", 3.14d);
        testBins.put("bytes", new byte[]{1, 127, 127, 1});
        testBins.put("list", Arrays.asList(1, "b", 3.14));
        Map<String, String> map = new HashMap<>();
        map.put("hi", "there");
        testBins.put("map", map);

        ObjectMapper mapper = new ObjectMapper(new MessagePackFactory());
        byte[] binBytes = mapper.writeValueAsBytes(testBins);

        MsgPackBinParser parser = new MsgPackBinParser(new ByteArrayInputStream(binBytes));
        Map<String, Object> parsedBins = parser.parseBins();

        Assert.assertTrue(ASTestUtils.compareMapStringObj(testBins, parsedBins));

    }

    private void singleBinParseTest(Object value) throws IOException {
        Map<String, Object> testBins = new HashMap<>();
        testBins.put("bin", value);

        byte[] msgpackBin = writeSimpleBin(testBins);
        MsgPackBinParser parser = new MsgPackBinParser(new ByteArrayInputStream(msgpackBin));
        Map<String, Object> parsedBins = parser.parseBins();

        Assert.assertTrue(ASTestUtils.compareMapStringObj(testBins, parsedBins));
    }

    @Test
    public void nonStringMapKeysTest() throws IOException {
        MessageBufferPacker packer = new MessagePack.PackerConfig().newBufferPacker();
        Map<String, Object> realBins = new HashMap<>();
        Map<Object, Object> mapBin = new HashMap<>();
        mapBin.put(1l, "int");
        mapBin.put(3.14, "float");
        realBins.put("map", mapBin);

        // {"map": {1:"int", 3.14:"float"}}
        packer.packMapHeader(1);
        packer.packString("map");
        packer.packMapHeader(2);

        packer.packInt(1);
        packer.packString("int");

        packer.packDouble(3.14);
        packer.packString("float");

        byte[] msgpackMap = packer.toByteArray();

        MsgPackBinParser parser = new MsgPackBinParser(new ByteArrayInputStream(msgpackMap));
        Map<String, Object> parsedBins = parser.parseBins();

        Assert.assertTrue(ASTestUtils.compareMapStringObj(parsedBins, realBins));

    }

    @Test(expected = MalformedMsgPackError.class)
    public void nonMapBinsTest() throws IOException {
        MessageBufferPacker packer = new MessagePack.PackerConfig().newBufferPacker();
        packer.packInt(5);
        byte[] intBytes = packer.toByteArray();
        MsgPackBinParser parser = new MsgPackBinParser(new ByteArrayInputStream(intBytes));
        parser.parseBins();
    }

    @Test(expected = MalformedMsgPackError.class)
    public void nonStringMapKeyTest() throws IOException {
        MessageBufferPacker packer = new MessagePack.PackerConfig().newBufferPacker();
        packer.packMapHeader(1);
        packer.packInt(5);
        packer.packInt(5);
        byte[] binBytes = packer.toByteArray();
        MsgPackBinParser parser = new MsgPackBinParser(new ByteArrayInputStream(binBytes));
        parser.parseBins();
    }

    @Test(expected = MalformedMsgPackError.class)
    public void testIncompleteMapBins() throws IOException {
        MessageBufferPacker packer = new MessagePack.PackerConfig().newBufferPacker();

        packer.packMapHeader(3);

        MsgPackBinParser parser = new MsgPackBinParser(new ByteArrayInputStream(packer.toByteArray()));

        parser.parseBins();
    }

    @Test(expected = MalformedMsgPackError.class)
    public void testEmptyMapdata() throws IOException {

        MsgPackBinParser parser = new MsgPackBinParser(new ByteArrayInputStream(new byte[]{}));
        parser.parseBins();
    }

    /*
     * Errors
     */

    /*
     * Write a basic Map<String, Object> to msgpack bytearray
     */
    private byte[] writeSimpleBin(Map<String, Object> bins) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper(new MessagePackFactory());
        return mapper.writeValueAsBytes(bins);
    }
}
