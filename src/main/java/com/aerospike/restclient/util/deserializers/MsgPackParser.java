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
package com.aerospike.restclient.util.deserializers;

import com.aerospike.client.Value;
import com.aerospike.client.command.ParticleType;
import com.aerospike.restclient.util.RestClientErrors.MalformedMsgPackError;
import org.msgpack.core.*;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MsgPackParser {
    protected MessageUnpacker unpacker;

    public MsgPackParser(InputStream stream) {
        unpacker = MessagePack.newDefaultUnpacker(stream);
    }

    public Object unpackValue() throws IOException {
        try {

            MessageFormat format = unpacker.getNextFormat();
            switch (format) {
                case NIL:
                    unpacker.unpackNil();
                    return new Value.NullValue();
                case BOOLEAN:
                    return unpacker.unpackBoolean();
                case INT8:
                case INT16:
                case INT32:
                case INT64:
                case UINT8:
                case UINT16:
                case UINT32:
                case NEGFIXINT:
                case POSFIXINT:
                    return unpacker.unpackLong();
                case UINT64:
                    try {
                        BigInteger bigInt = unpacker.unpackBigInteger();
                        return bigInt.longValueExact();
                    } catch (ArithmeticException e) {
                        throw new MalformedMsgPackError("Integer value too large");
                    }
                case FLOAT32:
                case FLOAT64:
                    return unpacker.unpackDouble();
                case STR8:
                case STR16:
                case STR32:
                case FIXSTR:
                    return unpacker.unpackString();
                case BIN8:
                case BIN16:
                case BIN32:
                    int binLen = unpacker.unpackRawStringHeader();
                    return unpacker.readPayload(binLen);
                case EXT8:
                case EXT16:
                case EXT32:
                case FIXEXT1:
                case FIXEXT2:
                case FIXEXT4:
                case FIXEXT8:
                case FIXEXT16:
                    return unpackExtension();
                case ARRAY16:
                case ARRAY32:
                case FIXARRAY:
                    return unpackList();
                case MAP16:
                case MAP32:
                case FIXMAP:
                    return unpackMap();
                case NEVER_USED:
                    throw new MalformedMsgPackError("Unexpected MessageFormat 'NEVER_USED'");
                default:
                    throw new MalformedMsgPackError("Unknown messagpack type");
            }
        } catch (MessagePackException e) {
            throw new MalformedMsgPackError("Failed to unpack data");
        }
    }

    private Object unpackMap() throws IOException {
        int count = unpacker.unpackMapHeader();
        Map<Object, Object> map = new HashMap<Object, Object>(count);
        // Need to unpack key and then value
        for (int i = 0; i < count; i++) {
            Object key = unpackValue();
            Object value = unpackValue();
            map.put(key, value);
        }
        return map;
    }

    private List<Object> unpackList() throws IOException {
        int count = unpacker.unpackArrayHeader();
        List<Object> list = new ArrayList<Object>(count);
        for (int i = 0; i < count; i++) {
            list.add(i, unpackValue());
        }
        return list;
    }

    private Object unpackExtension() throws IOException {
        ExtensionTypeHeader extHeader = unpacker.unpackExtensionTypeHeader();
        byte extType = extHeader.getType();
        int extLen = extHeader.getLength();

        if (extType == ParticleType.GEOJSON) {
            byte[] geoStr = unpacker.readPayload(extLen);
            String geoJsonStr = new String(geoStr, StandardCharsets.UTF_8);
            return new Value.GeoJSONValue(geoJsonStr);
        }

        throw new MalformedMsgPackError(String.format("Unknown extension type %d", extType));

    }
}
