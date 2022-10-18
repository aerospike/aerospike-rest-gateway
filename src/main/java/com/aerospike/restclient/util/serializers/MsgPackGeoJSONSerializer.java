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
package com.aerospike.restclient.util.serializers;

import com.aerospike.client.Value.GeoJSONValue;
import com.aerospike.client.command.ParticleType;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.msgpack.jackson.dataformat.MessagePackExtensionType;
import org.msgpack.jackson.dataformat.MessagePackGenerator;

import java.io.IOException;

public class MsgPackGeoJSONSerializer extends StdSerializer<GeoJSONValue> {

    private static final long serialVersionUID = 1L;

    public MsgPackGeoJSONSerializer() {
        this(null);
    }

    public MsgPackGeoJSONSerializer(Class<GeoJSONValue> t) {
        super(t);
    }

    /*
    We serialize aerospike GeoJSONValue values as a MessagePack extension of type 23.
     */
    @Override
    public void serialize(GeoJSONValue geoVal, JsonGenerator gen, SerializerProvider provider) throws IOException {
        MessagePackExtensionType geoExt = new MessagePackExtensionType((byte) ParticleType.GEOJSON,
                geoVal.toString().getBytes());
        ((MessagePackGenerator) gen).writeExtensionType(geoExt);
    }
}