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
package com.aerospike.restclient.config;

import com.aerospike.client.Value;
import com.aerospike.restclient.util.serializers.MsgPackGeoJSONSerializer;
import com.aerospike.restclient.util.serializers.MsgPackObjKeySerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.msgpack.jackson.dataformat.MessagePackFactory;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;

public class MsgPackConverter extends AbstractJackson2HttpMessageConverter {
    static final MediaType mediaType = new MediaType("application", "msgpack");

    MsgPackConverter() {
        super(getASMsgPackObjectMapper(), mediaType);
    }

    public static ObjectMapper getASMsgPackObjectMapper() {
        MessagePackFactory aerospikeMsgPackFactory = new MessagePackFactory();
        ObjectMapper mapper = new ObjectMapper(aerospikeMsgPackFactory);
        mapper.registerModule(new ParameterNamesModule());
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        addSerializerModules(mapper);
        return mapper;
    }

    private static void addSerializerModules(ObjectMapper mapper) {
        SimpleModule recModule = new SimpleModule();
        recModule.addSerializer(Value.GeoJSONValue.class, new MsgPackGeoJSONSerializer());
        recModule.addKeySerializer(Object.class, new MsgPackObjKeySerializer());
        mapper.registerModule(recModule);
    }
}
