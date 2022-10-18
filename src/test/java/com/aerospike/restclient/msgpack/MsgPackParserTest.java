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

import com.aerospike.client.Value;
import com.aerospike.client.Value.GeoJSONValue;
import com.aerospike.restclient.ASTestUtils;
import com.aerospike.restclient.util.RestClientErrors.MalformedMsgPackError;
import com.aerospike.restclient.util.deserializers.MsgPackOperationsParser;
import com.aerospike.restclient.util.deserializers.MsgPackParser;
import org.junit.Assert;
import org.junit.Test;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
	Test that simple msgpack is handled by our parser
 */
public class MsgPackParserTest {

    @Test
    public void testStringValue() throws IOException {
        MessageBufferPacker packer = new MessagePack.PackerConfig().newBufferPacker();
        String testString = "aerospike";
        packer.packString(testString);

        MsgPackParser parser = new MsgPackParser(new ByteArrayInputStream(packer.toByteArray()));

        Object val = parser.unpackValue();

        Assert.assertEquals(testString, val);
    }

    @Test
    public void testLongValue() throws IOException {
        MessageBufferPacker packer = new MessagePack.PackerConfig().newBufferPacker();
        Long testVal = 1000l;
        packer.packLong(testVal);

        MsgPackParser parser = new MsgPackParser(new ByteArrayInputStream(packer.toByteArray()));

        Object val = parser.unpackValue();

        Assert.assertEquals(testVal, val);
    }

    @Test
    public void testLargeValue() throws IOException {
        MessageBufferPacker packer = new MessagePack.PackerConfig().newBufferPacker();
        Long bigLong = (long) 1 << 62;
        BigInteger bigNum = new BigInteger(String.valueOf(bigLong));
        packer.packBigInteger(bigNum);

        MsgPackParser parser = new MsgPackParser(new ByteArrayInputStream(packer.toByteArray()));

        Object val = parser.unpackValue();

        Assert.assertEquals(bigLong, val);
    }

    @Test
    public void testFloatValue() throws IOException {
        MessageBufferPacker packer = new MessagePack.PackerConfig().newBufferPacker();
        Double testFloat = 3.14;
        packer.packDouble(testFloat);

        MsgPackParser parser = new MsgPackParser(new ByteArrayInputStream(packer.toByteArray()));

        Object val = parser.unpackValue();

        Assert.assertEquals(testFloat, val);
    }

    @Test
    public void testNullValue() throws IOException {
        MessageBufferPacker packer = new MessagePack.PackerConfig().newBufferPacker();
        packer.packNil();
        MsgPackParser parser = new MsgPackParser(new ByteArrayInputStream(packer.toByteArray()));

        Object val = parser.unpackValue();

        Assert.assertEquals(new Value.NullValue(), val);
    }

    @Test
    public void testBoolValue() throws IOException {
        MessageBufferPacker packer = new MessagePack.PackerConfig().newBufferPacker();
        packer.packBoolean(true);
        MsgPackParser parser = new MsgPackParser(new ByteArrayInputStream(packer.toByteArray()));

        Object val = parser.unpackValue();

        Assert.assertEquals(true, val);
    }

    @Test
    public void testByteValue() throws IOException {
        MessageBufferPacker packer = new MessagePack.PackerConfig().newBufferPacker();
        byte[] testByte = new byte[]{1, 2, 3, 4};
        packer.packBinaryHeader(testByte.length);
        packer.writePayload(testByte);

        MsgPackParser parser = new MsgPackParser(new ByteArrayInputStream(packer.toByteArray()));

        Object val = parser.unpackValue();

        Assert.assertArrayEquals(testByte, (byte[]) val);
    }

    @Test
    public void testMixedValueList() throws IOException {
        MessageBufferPacker packer = new MessagePack.PackerConfig().newBufferPacker();
        Object[] objects = new Object[]{1l, "a", 3.14d};
        packer.packArrayHeader(objects.length);

        packer.packLong((long) objects[0]);
        packer.packString((String) objects[1]);
        packer.packDouble((double) objects[2]);

        MsgPackParser parser = new MsgPackParser(new ByteArrayInputStream(packer.toByteArray()));

        @SuppressWarnings("unchecked") List<Object> val = (List<Object>) parser.unpackValue();

        Assert.assertArrayEquals(objects, val.toArray());

    }

    @Test
    public void testMixedValueMap() throws IOException {
        MessageBufferPacker packer = new MessagePack.PackerConfig().newBufferPacker();
        // {1:2, "a":5, "3.14":0}
        Map<Object, Object> map = new HashMap<>();
        map.put(1l, 2l);
        map.put("a", "bc");
        map.put(3.14d, 0l);

        packer.packMapHeader(3);

        packer.packLong(1l);
        packer.packLong(2l);

        packer.packString("a");
        packer.packString("bc");

        packer.packDouble(3.14d);
        packer.packLong(0l);

        MsgPackParser parser = new MsgPackParser(new ByteArrayInputStream(packer.toByteArray()));

        @SuppressWarnings("unchecked") Map<Object, Object> val = (Map<Object, Object>) parser.unpackValue();

        Assert.assertTrue(ASTestUtils.compareMap(map, val));

    }

    @Test
    public void testNestedList() throws IOException {
        MessageBufferPacker packer = new MessagePack.PackerConfig().newBufferPacker();

        List<Object> longList = new ArrayList<Object>();
        longList.add(1l);
        longList.add(2l);
        longList.add(3l);

        Object[] objects = new Object[]{longList};

        packer.packArrayHeader(objects.length);
        packer.packArrayHeader(3);
        packer.packLong(1l);
        packer.packLong(2l);
        packer.packLong(3l);

        MsgPackParser parser = new MsgPackParser(new ByteArrayInputStream(packer.toByteArray()));

        @SuppressWarnings("unchecked") List<Object> val = (List<Object>) parser.unpackValue();

        Assert.assertArrayEquals(objects, val.toArray());

    }

    @Test
    public void testGeoJSONValue() throws IOException {

        String geoString = "{\"coordinates\": [-122.0, 37.5], \"type\": \"Point\"}";
        MessageBufferPacker packer = new MessagePack.PackerConfig().newBufferPacker();

        packer.packExtensionTypeHeader((byte) 23, geoString.length());
        packer.addPayload(geoString.getBytes(StandardCharsets.UTF_8));

        byte[] geoMsgPack = packer.toByteArray();

        MsgPackOperationsParser parser = new MsgPackOperationsParser(new ByteArrayInputStream(geoMsgPack));
        Object val = parser.unpackValue();

        Assert.assertTrue(val instanceof GeoJSONValue);
        Assert.assertEquals(((GeoJSONValue) val).toString(), geoString);
    }

    @Test(expected = MalformedMsgPackError.class)
    public void testUnknownExtension() throws IOException {

        String geoString = "{\"coordinates\": [-122.0, 37.5], \"type\": \"Point\"}";
        MessageBufferPacker packer = new MessagePack.PackerConfig().newBufferPacker();

        packer.packExtensionTypeHeader((byte) 127, geoString.length());
        packer.addPayload(geoString.getBytes(StandardCharsets.UTF_8));

        byte[] geoMsgPack = packer.toByteArray();

        MsgPackOperationsParser parser = new MsgPackOperationsParser(new ByteArrayInputStream(geoMsgPack));
        parser.unpackValue(); // This should error
    }

    @Test(expected = MalformedMsgPackError.class)
    public void testIncompleteArray() throws IOException {
        MessageBufferPacker packer = new MessagePack.PackerConfig().newBufferPacker();

        packer.packArrayHeader(3);

        MsgPackParser parser = new MsgPackParser(new ByteArrayInputStream(packer.toByteArray()));

        parser.unpackValue();
    }

    /*
     * We can only safely handle numbers up to 2 ^ 63 - 1, this tests that we get an error outside of that range
     */
    @Test(expected = MalformedMsgPackError.class)
    public void testValueOutsideOfLongRange() throws IOException {
        MessageBufferPacker packer = new MessagePack.PackerConfig().newBufferPacker();
        /* 2 ^ 64 -2  This is outside the range of a long*/
        BigInteger bigNum = new BigInteger("18446744073709551614");
        packer.packBigInteger(bigNum);

        MsgPackParser parser = new MsgPackParser(new ByteArrayInputStream(packer.toByteArray()));
        parser.unpackValue();
    }
}
