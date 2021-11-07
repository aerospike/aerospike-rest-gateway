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
package com.aerospike.restclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class RecordPutErrorTests {

	@Autowired
	private ObjectMapper objectMapper;

	private MockMvc mockMVC;

	@Autowired
	private WebApplicationContext wac;

	private String nonExistentNSEndpoint;
	private String nonExistentRecordEndpoint;

	private static Stream<Arguments> getParams() {
		return Stream.of(Arguments.of(true, false));
	}

	private void setParams(boolean useSet) {
		if (useSet) {
			nonExistentNSEndpoint = ASTestUtils.buildEndpoint("kvs", "fakeNS", "demo", "1");
			nonExistentRecordEndpoint = ASTestUtils.buildEndpoint("kvs", "test", "demo", "thisisnotarealkeyforarecord");
		} else {
			nonExistentNSEndpoint = ASTestUtils.buildEndpoint("kvs", "fakeNS", "1");
			nonExistentRecordEndpoint = ASTestUtils.buildEndpoint("kvs", "test", "thisisnotarealkeyforarecord");
		}
	}

	@BeforeEach
	public void setup() {
		mockMVC = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@ParameterizedTest
	@MethodSource("getParams")
	public void putToNonExistentNS(boolean useSet) throws Exception {
		setParams(useSet);
		Map<String, Object> binMap = new HashMap<>();
		binMap.put("integer", 12345);

		mockMVC.perform(put(nonExistentNSEndpoint)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(binMap)))
		.andExpect(status().isInternalServerError());
	}

	@ParameterizedTest
	@MethodSource("getParams")
	public void putToNonExistentRecord(boolean useSet) throws Exception {
		setParams(useSet);
		Map<String, Object> binMap = new HashMap<>();

		binMap.put("string", "Aerospike");

		mockMVC.perform(put(nonExistentRecordEndpoint)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(binMap))
				).andExpect(status().isNotFound());
	}
}
