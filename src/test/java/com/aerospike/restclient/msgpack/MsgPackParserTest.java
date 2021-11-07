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
package com.aerospike.restclient.msgpack;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;

import com.aerospike.client.Value;
import com.aerospike.client.Value.GeoJSONValue;
import com.aerospike.restclient.ASTestUtils;
import com.aerospike.restclient.util.RestClientErrors.MalformedMsgPackError;
import com.aerospike.restclient.util.deserializers.MsgPackOperationsParser;
import com.aerospike.restclient.util.deserializers.MsgPackParser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

		assertEquals(testString, val);
	}

	@Test
	public void testLongValue() throws IOException {
		MessageBufferPacker packer = new MessagePack.PackerConfig().newBufferPacker();
		Long testVal = 1000L;
		packer.packLong(testVal);

		MsgPackParser parser = new MsgPackParser(new ByteArrayInputStream(packer.toByteArray()));

		Object val = parser.unpackValue();

		assertEquals(testVal, val);
	}

	@Test
	public void testLargeValue() throws IOException {
		MessageBufferPacker packer = new MessagePack.PackerConfig().newBufferPacker();
		Long bigLong = (long) 1 << 62;
		BigInteger bigNum = new BigInteger(String.valueOf(bigLong));
		packer.packBigInteger(bigNum);

		MsgPackParser parser = new MsgPackParser(new ByteArrayInputStream(packer.toByteArray()));

		Object val = parser.unpackValue();

		assertEquals(bigLong, val);
	}

	@Test
	public void testFloatValue() throws IOException {
		MessageBufferPacker packer = new MessagePack.PackerConfig().newBufferPacker();
		Double testFloat = 3.14;
		packer.packDouble(testFloat);

		MsgPackParser parser = new MsgPackParser(new ByteArrayInputStream(packer.toByteArray()));

		Object val = parser.unpackValue();

		assertEquals(testFloat, val);
	}

	@Test
	public void testNullValue() throws IOException {
		MessageBufferPacker packer = new MessagePack.PackerConfig().newBufferPacker();
		packer.packNil();
		MsgPackParser parser = new MsgPackParser(new ByteArrayInputStream(packer.toByteArray()));

		Object val = parser.unpackValue();

		assertEquals(new Value.NullValue(), val);
	}

	@Test
	public void testBoolValue() throws IOException {
		MessageBufferPacker packer = new MessagePack.PackerConfig().newBufferPacker();
		packer.packBoolean(true);
		MsgPackParser parser = new MsgPackParser(new ByteArrayInputStream(packer.toByteArray()));

		Object val = parser.unpackValue();

		assertEquals(true, val);
	}

	@Test
	public void testByteValue() throws IOException {
		MessageBufferPacker packer = new MessagePack.PackerConfig().newBufferPacker();
		byte[] testByte = new byte[] {1, 2, 3, 4};
		packer.packBinaryHeader(testByte.length);
		packer.writePayload(testByte);

		MsgPackParser parser = new MsgPackParser(new ByteArrayInputStream(packer.toByteArray()));

		Object val = parser.unpackValue();

		assertArrayEquals(testByte, (byte[]) val);
	}

	@Test
	public void testMixedValueList() throws IOException {
		MessageBufferPacker packer = new MessagePack.PackerConfig().newBufferPacker();
		Object[] objects = new Object[] {1L, "a", 3.14d};
		packer.packArrayHeader(objects.length);

		packer.packLong((long)objects[0]);
		packer.packString((String)objects[1]);
		packer.packDouble((double)objects[2]);

		MsgPackParser parser = new MsgPackParser(new ByteArrayInputStream(packer.toByteArray()));

		@SuppressWarnings("unchecked")
		List<Object>val = (List<Object>)parser.unpackValue();

		assertArrayEquals(objects, val.toArray());
	}

	@Test
	public void testMixedValueMap() throws IOException {
		MessageBufferPacker packer = new MessagePack.PackerConfig().newBufferPacker();
		// {1:2, "a":5, "3.14":0}
		Map<Object, Object>map = new HashMap<>();
		map.put(1L, 2L);
		map.put("a", "bc");
		map.put(3.14d, 0L);

		packer.packMapHeader(3);

		packer.packLong(1L);
		packer.packLong(2L);

		packer.packString("a");
		packer.packString("bc");

		packer.packDouble(3.14d);
		packer.packLong(0L);

		MsgPackParser parser = new MsgPackParser(new ByteArrayInputStream(packer.toByteArray()));

		@SuppressWarnings("unchecked")
		Map<Object, Object>val = (Map<Object, Object>)parser.unpackValue();

		assertTrue(ASTestUtils.compareMap(map,  val));
	}

	@Test
	public void testNestedList() throws IOException {
		MessageBufferPacker packer = new MessagePack.PackerConfig().newBufferPacker();

		List<Object>longList = new ArrayList<>();
		longList.add(1L);
		longList.add(2L);
		longList.add(3L);

		Object[] objects = new Object[] {longList};

		packer.packArrayHeader(objects.length);
		packer.packArrayHeader(3);
		packer.packLong(1L);
		packer.packLong(2L);
		packer.packLong(3L);

		MsgPackParser parser = new MsgPackParser(new ByteArrayInputStream(packer.toByteArray()));

		@SuppressWarnings("unchecked")
		List<Object>val = (List<Object>)parser.unpackValue();

		assertArrayEquals(objects, val.toArray());
	}

	@Test
	public void testGeoJSONValue() throws IOException {

		String geoString = "{\"coordinates\": [-122.0, 37.5], \"type\": \"Point\"}";
		MessageBufferPacker packer = new MessagePack.PackerConfig().newBufferPacker();

		packer.packExtensionTypeHeader((byte) 23, geoString.length());
		packer.addPayload(geoString.getBytes("UTF-8"));

		byte[] geoMsgPack = packer.toByteArray();

		MsgPackOperationsParser parser = new MsgPackOperationsParser(new ByteArrayInputStream(geoMsgPack));
		Object val = parser.unpackValue();

		assertTrue(val instanceof GeoJSONValue);
		assertEquals(val.toString(), geoString);
	}

	@Test
	public void testUnknownExtension() throws IOException {

		String geoString = "{\"coordinates\": [-122.0, 37.5], \"type\": \"Point\"}";
		MessageBufferPacker packer = new MessagePack.PackerConfig().newBufferPacker();

		packer.packExtensionTypeHeader((byte) 127, geoString.length());
		packer.addPayload(geoString.getBytes("UTF-8"));

		byte[] geoMsgPack = packer.toByteArray();

		MsgPackOperationsParser parser = new MsgPackOperationsParser(new ByteArrayInputStream(geoMsgPack));
		assertThrows(MalformedMsgPackError.class, parser::unpackValue);
	}

	@Test
	public void testIncompleteArray() throws IOException {
		MessageBufferPacker packer = new MessagePack.PackerConfig().newBufferPacker();

		packer.packArrayHeader(3);

		MsgPackParser parser = new MsgPackParser(new ByteArrayInputStream(packer.toByteArray()));

		assertThrows(MalformedMsgPackError.class, parser::unpackValue);
	}

	/*
	 * We can only safely handle numbers up to 2 ^ 63 - 1, this tests that we get an error outside of that range
	 */
	@Test
	public void testValueOutsideOfLongRange() throws IOException {
		MessageBufferPacker packer = new MessagePack.PackerConfig().newBufferPacker();
		/* 2 ^ 64 -2  This is outside the range of a long*/
		BigInteger bigNum = new BigInteger("18446744073709551614");
		packer.packBigInteger(bigNum);

		MsgPackParser parser = new MsgPackParser(new ByteArrayInputStream(packer.toByteArray()));
		assertThrows(MalformedMsgPackError.class, parser::unpackValue);
	}
}
