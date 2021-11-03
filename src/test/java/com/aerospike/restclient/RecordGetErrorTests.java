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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class RecordGetErrorTests {

	String nonExistentNSendPoint;
	String nonExistentRecordEndpoint;
	String invalidKeyTypeEndpoint;
	String invalidIntegerEndpoint;
	String invalidBytesEndpoint;
	String invalidDigestEndpoint;

	private static Stream<Arguments> getParams() {
		return Stream.of(Arguments.of(true, false));
	}

	@ParameterizedTest
	@MethodSource("getParams")
	void addParams(boolean useSet) {
		if (useSet) {
			nonExistentNSendPoint = ASTestUtils.buildEndpoint("kvs", "fakeNS", "demo", "1");
			nonExistentRecordEndpoint = ASTestUtils.buildEndpoint("kvs", "test", "demo", "thisisnotarealkeyforarecord");
			invalidKeyTypeEndpoint = ASTestUtils.buildEndpoint("kvs", "test", "demo", "1") + "?keytype=skeleton";
			invalidIntegerEndpoint = ASTestUtils.buildEndpoint("kvs", "test", "demo", "key") + "?keytype=INTEGER";
			invalidBytesEndpoint = ASTestUtils.buildEndpoint("kvs", "test", "demo", "/=") + "?keytype=BYTES"; /*Invalid urlsafe bae64*/
			invalidDigestEndpoint = ASTestUtils.buildEndpoint("kvs", "test", "demo", "key") + "?keytype=DIGEST";
		} else {
			nonExistentNSendPoint = ASTestUtils.buildEndpoint("kvs", "fakeNS",  "1");
			nonExistentRecordEndpoint = ASTestUtils.buildEndpoint("kvs", "test", "thisisnotarealkeyforarecord");
			invalidKeyTypeEndpoint = ASTestUtils.buildEndpoint("kvs", "test", "1") + "?keytype=skeleton";
			invalidIntegerEndpoint = ASTestUtils.buildEndpoint("kvs", "test", "key") + "?keytype=INTEGER";
			invalidBytesEndpoint = ASTestUtils.buildEndpoint("kvs", "test", "/=") + "?keytype=BYTES"; /*Invalid urlsafe bae64*/
			invalidDigestEndpoint = ASTestUtils.buildEndpoint("kvs", "test", "key") + "?keytype=DIGEST";
		}
	}

	@Autowired
	private ObjectMapper objectMapper;

	private MockMvc mockMVC;

	@Autowired
	private WebApplicationContext wac;

	@BeforeEach
	public void setup() {
		mockMVC = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@Test
	public void GetFromNonExistentNS() throws Exception {

		MvcResult result = mockMVC.perform(
				get(nonExistentNSendPoint)
				).andExpect(status().isInternalServerError()).andReturn();

		MockHttpServletResponse res = result.getResponse();
		String resJson = res.getContentAsString();
		TypeReference<Map<String, Object>> sOMapType= new TypeReference<Map<String, Object>>() {};
		Map<String, Object>resObject = objectMapper.readValue(resJson, sOMapType);

		assertFalse((boolean) resObject.get("inDoubt"));
	}

	@Test
	public void GetNonExistentRecord() throws Exception {

		MvcResult result = mockMVC.perform(
				get(nonExistentRecordEndpoint)
				).andExpect(status().isNotFound()).andReturn();

		MockHttpServletResponse res = result.getResponse();
		String resJson = res.getContentAsString();
		TypeReference<Map<String, Object>> sOMapType= new TypeReference<Map<String, Object>>() {};
		Map<String, Object>resObject = objectMapper.readValue(resJson, sOMapType);

		assertFalse((boolean) resObject.get("inDoubt"));
	}

	@Test
	public void GetWithInvalidKeyType() throws Exception {
		mockMVC.perform(
				get(invalidKeyTypeEndpoint)
				).andExpect(status().isBadRequest());
	}

	@Test
	public void GetWithInvalidIntegerKey() throws Exception {
		mockMVC.perform(
				get(invalidIntegerEndpoint)
				).andExpect(status().isBadRequest());
	}

	@Test
	public void GetWithInvalidBytesKey() throws Exception {
		mockMVC.perform(
				get(invalidBytesEndpoint) /*This has an illegally encoded urlsafebase64 */
				).andExpect(status().isBadRequest());
	}

	@Test
	public void GetWithInvalidDigestKey() throws Exception {
		mockMVC.perform(
				get(invalidDigestEndpoint) /* This is not 20 bytes long */
				).andExpect(status().isBadRequest());
	}
}
