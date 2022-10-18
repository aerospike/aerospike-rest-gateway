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
import com.aerospike.restclient.util.serializers.MsgPackGeoJSONSerializer;
import com.aerospike.restclient.util.serializers.MsgPackObjKeySerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;
import org.msgpack.jackson.dataformat.MessagePackFactory;
import org.msgpack.value.ExtensionValue;
import org.msgpack.value.ValueType;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class MsgpackCustomSerializerTest {
    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper(new MessagePackFactory());
        SimpleModule recModule = new SimpleModule();
        recModule.addSerializer(GeoJSONValue.class, new MsgPackGeoJSONSerializer());
        recModule.addKeySerializer(Object.class, new MsgPackObjKeySerializer());
        mapper.registerModule(recModule);
    }

    @Test
    public void testGeoJSONSerialization() throws Exception {
        String geoStr = "{\"coordinates\": [-122.0, 37.5], \"type\": \"Point\"}";
        GeoJSONValue geoVal = new GeoJSONValue(geoStr);
        byte[] output = mapper.writeValueAsBytes(geoVal);

        MessageUnpacker unpacker = MessagePack.newDefaultUnpacker(output);
        org.msgpack.value.Value v = unpacker.unpackValue();
        ValueType type = v.getValueType();
        Assert.assertEquals(type, ValueType.EXTENSION);

        // Code from msgpack example repo
        ExtensionValue ev = v.asExtensionValue();
        byte extType = ev.getType();
        Assert.assertEquals(extType, (byte) 23);
        byte[] extValue = ev.getData();
        String outStr = new String(extValue, StandardCharsets.UTF_8);
        Assert.assertEquals(geoStr, outStr);
    }

    @Test
    public void testByteserialization() throws Exception {
        byte[] testBytes = {1, 2, 2, 1, 2, 1};
        byte[] output = mapper.writeValueAsBytes(testBytes);

        MessageUnpacker unpacker = MessagePack.newDefaultUnpacker(output);
        org.msgpack.value.Value v = unpacker.unpackValue();
        ValueType type = v.getValueType();
        Assert.assertEquals(type, ValueType.BINARY);

        // Code from msgpack example repo
        byte[] outBytes = v.asBinaryValue().asByteArray();
        Assert.assertArrayEquals(testBytes, outBytes);
    }

    /*
     * JSON can't handle an integer key, make sure our message pack mapper can handle it.
     */
    @Test
    public void testMapWithNonStrKey() throws Exception {
        Map<Object, Object> outObj = new HashMap<>();
        outObj.put(5, "int");
        byte[] output = mapper.writeValueAsBytes(outObj);

        MessageUnpacker unpacker = MessagePack.newDefaultUnpacker(output);
        int size = unpacker.unpackMapHeader();
        Assert.assertEquals(1, size);

        org.msgpack.value.Value v = unpacker.unpackValue();
        ValueType type = v.getValueType();
        Assert.assertEquals(type, ValueType.INTEGER);

        int key = v.asIntegerValue().asInt();
        Assert.assertEquals(key, 5);

        v = unpacker.unpackValue();
        type = v.getValueType();
        Assert.assertEquals(type, ValueType.STRING);

        String value = v.asStringValue().asString();
        Assert.assertEquals("int", value);
    }
}
