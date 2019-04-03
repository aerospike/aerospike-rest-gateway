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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.msgpack.jackson.dataformat.MessagePackFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.AerospikeException;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.client.Value.BytesValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(Parameterized.class)
@SpringBootTest
public class RecordPostCorrectTests {

	/* Needed to run as a Spring Boot test */
	@ClassRule
	public static final SpringClassRule springClassRule = new SpringClassRule();

	@Rule
	public final SpringMethodRule springMethodRule = new SpringMethodRule();

	private PostPerformer postPerformer = null;
	private MockMvc mockMVC;

	@Autowired
	private AerospikeClient client;

	@Autowired
	private WebApplicationContext wac;

	private Key testKey = new Key("test", "junit", "getput");
	private Key intKey = new Key("test", "junit", 1);
	private Key bytesKey = new Key("test", "junit", new byte[] {1, 127, 127, 1});
	private String testEndpoint = ASTestUtils.buildEndpoint("kvs", "test", "junit", "getput");
	private String digestEndpoint;
	private String intEndpoint;
	private String bytesEndpoint;

	@Before
	public void setup() {
		mockMVC = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@After
	public void clean() {
		client.delete(null, testKey);
		try {
			client.delete(null, bytesKey);
		} catch (AerospikeException e) {
			;
		}
		try {
			client.delete(null, intKey);
		} catch (AerospikeException e) {
			;
		}
	}

	@Parameters
	public static Object[] getParams() {
		return new Object[][] {
			{
				new JSONPostPerformer(MediaType.APPLICATION_JSON.toString(), new ObjectMapper()),
				true
			},
			{
				new MsgPackPostPerformer("application/msgpack", new ObjectMapper(new MessagePackFactory())),
				true
			},
			{
				new JSONPostPerformer(MediaType.APPLICATION_JSON.toString(), new ObjectMapper()),
				false
			},
			{
				new MsgPackPostPerformer("application/msgpack", new ObjectMapper(new MessagePackFactory())),
				false
			}
		};
	}

	public RecordPostCorrectTests(PostPerformer performer, boolean useSet) {
		if (useSet) {
			this.testEndpoint = ASTestUtils.buildEndpoint("kvs", "test", "junit", "getput");
			this.testKey = new Key("test", "junit", "getput");
			this.intKey = new Key("test", "junit", 1);
			this.bytesKey = new Key("test", "junit", new byte[] {1, 127, 127, 1});

			String urlDigest = Base64.getUrlEncoder().encodeToString(testKey.digest);
			digestEndpoint = ASTestUtils.buildEndpoint("kvs", "test", "junit", urlDigest) + "?keytype=DIGEST";

			String urlBytes = Base64.getUrlEncoder().encodeToString((byte[])((BytesValue)bytesKey.userKey).getObject());
			bytesEndpoint = ASTestUtils.buildEndpoint("kvs", "test", "junit", urlBytes) + "?keytype=BYTES";

			intEndpoint = ASTestUtils.buildEndpoint("kvs", "test", "junit", "1") + "?keytype=INTEGER";

		} else {
			this.testEndpoint = ASTestUtils.buildEndpoint("kvs", "test", "getput");
			this.testKey = new Key("test", null, "getput");
			this.intKey = new Key("test", null, 1);
			this.bytesKey = new Key("test", null, new byte[] {1, 127, 127, 1});

			String urlDigest = Base64.getUrlEncoder().encodeToString(testKey.digest);
			digestEndpoint = ASTestUtils.buildEndpoint("kvs", "test", urlDigest) + "?keytype=DIGEST";

			String urlBytes = Base64.getUrlEncoder().encodeToString((byte[])((BytesValue)bytesKey.userKey).getObject());
			bytesEndpoint = ASTestUtils.buildEndpoint("kvs", "test",  urlBytes) + "?keytype=BYTES";

			intEndpoint = ASTestUtils.buildEndpoint("kvs", "test", "1") + "?keytype=INTEGER";
		}
		this.postPerformer = performer;
	}

	@Test
	public void PutInteger() throws Exception {
		Map<String, Object> binMap = new HashMap<String, Object>();

		binMap.put("integer", 12345);

		postPerformer.perform(mockMVC, testEndpoint, binMap);

		Record record = client.get(null, this.testKey);
		Assert.assertEquals(record.bins.get("integer"), 12345L);
	}

	@Test
	public void PutString() throws Exception {
		Map<String, Object> binMap = new HashMap<String, Object>();

		binMap.put("string", "Aerospike");

		postPerformer.perform(mockMVC, testEndpoint, binMap);

		Record record = client.get(null, this.testKey);
		Assert.assertEquals(record.bins.get("string"), "Aerospike");
	}

	@Test
	public void PutDouble() throws Exception {

		Map<String, Object> binMap = new HashMap<String, Object>();

		binMap.put("double", 2.718);

		postPerformer.perform(mockMVC, testEndpoint, binMap);

		Record record = client.get(null, this.testKey);
		Assert.assertEquals(record.bins.get("double"), 2.718);
	}

	@Test
	public void PutList() throws Exception {
		Map<String, Object> binMap = new HashMap<String, Object>();

		List<?>trueList = Arrays.asList(1L, "a", 3.5);

		binMap.put("ary", trueList);

		postPerformer.perform(mockMVC, testEndpoint, binMap);

		Record record = client.get(null, this.testKey);

		Assert.assertTrue(ASTestUtils.compareCollection((List<?>) record.bins.get("ary"), trueList));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void PutMapStringKeys() throws Exception {
		Map<String, Object> binMap = new HashMap<String, Object>();

		Map<Object, Object>testMap = new HashMap<Object, Object>();

		testMap.put("string", "a string");
		testMap.put("long", 2L);
		testMap.put("double", 4.5);

		binMap.put("map", testMap);

		postPerformer.perform(mockMVC, testEndpoint, binMap);

		Record record = client.get(null, this.testKey);

		Assert.assertTrue(ASTestUtils.compareMap((Map<Object,Object>) record.bins.get("map"), testMap));
	}

	@Test
	public void PutWithIntegerKey() throws Exception {
		Map<String, Object> binMap = new HashMap<String, Object>();

		binMap.put("integer", 12345);

		postPerformer.perform(mockMVC, intEndpoint, binMap);

		Record record = client.get(null, this.intKey);
		Assert.assertEquals(record.bins.get("integer"), 12345L);
	}

	@Test
	public void PutWithBytesKey() throws Exception {
		Map<String, Object> binMap = new HashMap<String, Object>();
		binMap.put("integer", 12345);


		postPerformer.perform(mockMVC, bytesEndpoint, binMap);

		Record record = client.get(null, this.bytesKey);
		Assert.assertEquals(record.bins.get("integer"), 12345L);
	}

	@Test
	public void PutWithDigestKey() throws Exception {
		Map<String, Object> binMap = new HashMap<String, Object>();

		binMap.put("integer", 12345);

		postPerformer.perform(mockMVC, digestEndpoint, binMap);

		Record record = client.get(null, this.testKey);
		Assert.assertEquals(record.bins.get("integer"), 12345L);
	}
}

interface PostPerformer {
	public void perform(MockMvc mockMVC, String testEndpoint, Map<String, Object>binMap)
			throws JsonProcessingException, Exception;
}

class JSONPostPerformer implements PostPerformer {
	String mediaType = null;
	ObjectMapper mapper = null;

	public JSONPostPerformer(String mediaType, ObjectMapper mapper) {
		this.mediaType = mediaType;
		this.mapper = mapper;
	}
	@Override
	public void perform(MockMvc mockMVC, String testEndpoint, Map<String, Object>binMap)
			throws JsonProcessingException, Exception {
		mockMVC.perform(post(testEndpoint).contentType(mediaType)
				.content(mapper.writeValueAsString(binMap)))
		.andExpect(status().isCreated());
	}

}

class MsgPackPostPerformer implements PostPerformer {
	String mediaType = null;
	ObjectMapper mapper = null;

	public MsgPackPostPerformer(String mediaType, ObjectMapper mapper) {
		this.mediaType = mediaType;
		this.mapper = mapper;
	}

	@Override
	public void perform(MockMvc mockMVC, String testEndpoint, Map<String, Object>binMap)
			throws JsonProcessingException, Exception {
		mockMVC.perform(post(testEndpoint).contentType(mediaType)
				.content(mapper.writeValueAsBytes(binMap)))
		.andExpect(status().isCreated());
	}

}