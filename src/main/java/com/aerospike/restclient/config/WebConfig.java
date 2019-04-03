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

package com.aerospike.restclient.config;

import java.util.List;

import org.msgpack.jackson.dataformat.MessagePackFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.aerospike.client.Value.GeoJSONValue;
import com.aerospike.restclient.util.converters.JSONMessageConverter;
import com.aerospike.restclient.util.serializers.MsgPackGeoJSONSerializer;
import com.aerospike.restclient.util.serializers.MsgPackObjKeySerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		/* Put our converters first in line */
		converters.add(0, new MsgPackConverter());
		converters.add(0, JSONMessageConverter.getConverter());
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("*")
		.allowedMethods("*")
		.allowedHeaders("*")
		.allowCredentials(Boolean.TRUE);
	}

}

class MsgPackConverter extends AbstractJackson2HttpMessageConverter {
	static final MediaType mediaType = new MediaType("application", "msgpack");

	MsgPackConverter() {
		super(getASMsgPackObjectMapper(), mediaType);
		ObjectMapper mapper = getObjectMapper();
		addSerializerModules(mapper);
	}

	private static ObjectMapper getASMsgPackObjectMapper() {
		MessagePackFactory aerospikeMsgPackFactory = new MessagePackFactory();
		ObjectMapper msgPackMapper = new ObjectMapper(aerospikeMsgPackFactory);
		return msgPackMapper;
	}

	private static void addSerializerModules(ObjectMapper mapper) {
		SimpleModule recModule = new SimpleModule();
		recModule.addSerializer(GeoJSONValue.class, new MsgPackGeoJSONSerializer());
		recModule.addKeySerializer(Object.class, new MsgPackObjKeySerializer());
		mapper.registerModule(recModule);
	}
}