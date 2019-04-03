/*
 * Copyright 2018 Aerospike, Inc.
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
//package com.aerospike.restclient.converters;
//
//import java.util.Arrays;
//import java.util.Base64;
//import java.util.Base64.Encoder;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.junit.runners.Parameterized;
//import org.junit.runners.Parameterized.Parameters;
//import org.msgpack.jackson.dataformat.MessagePackFactory;
//
//import com.aerospike.client.Key;
//import com.aerospike.client.Value;
//import com.aerospike.restclient.domain.RestClientBatchReadBody;
//import com.aerospike.restclient.util.AerospikeAPIConstants.RecordKeyType;
//import com.aerospike.restclient.util.deserializers.JSONBatchReadDeserializer;
//import com.fasterxml.jackson.core.JsonFactory;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.module.SimpleModule;
//
//@RunWith(Parameterized.class)
//public class BatchReadDeserializerTest {
//
//	String testNamespace = "test";
//	String testSetName = "junit";
//	String testStrKey = "testkey";
//
//	BatchReadBodyMapperTester tester = null;
//	ObjectMapper testMapper = null;
//
//
//	@Parameters
//	public static Object[] params() {
//		SimpleModule recordModule = new SimpleModule();
//		recordModule.addDeserializer(RestClientBatchReadBody.class, new JSONBatchReadDeserializer());
//
//		ObjectMapper jMapper = new ObjectMapper(new JsonFactory());
//		ObjectMapper mpMapper = new ObjectMapper(new MessagePackFactory());
//
//		jMapper.registerModule(recordModule);
//		mpMapper.registerModule(recordModule);
//
//		BatchReadBodyMapperTester jsonTester = new JSONBatchReadTester(jMapper);
//		BatchReadBodyMapperTester mpTester = new MsgPackBatchReadTester(mpMapper);
//
//		return new Object[] {
//				jsonTester, mpTester
//		};
//	}
//
//	public BatchReadDeserializerTest(BatchReadBodyMapperTester mapper) {
//		this.tester = mapper;
//	}
//
//	@Test
//	public void testWithStringKeyNSSet() throws Exception {
//
//		List<String>bins = Arrays.asList("bin1", "bin2");
//
//
//		RestClientBatchReadBody rcBRB = tester.getBatchRead(testNamespace, testSetName, testStrKey, bins, null);
//
//		Assert.assertEquals(rcBRB.binNames, bins.toArray(new String[] {}) );
//
//		Key key = rcBRB.key;
//		Assert.assertTrue(key.userKey instanceof Value.StringValue);
//		Assert.assertEquals(key.userKey.toString(), testStrKey);
//		Assert.assertEquals(key.namespace.toString(), testNamespace);
//		Assert.assertEquals(key.setName.toString(), testSetName);
//
//	}
//
//	@Test
//	public void testWithStringKeyNSNoSet() throws Exception {
//
//		List<String>bins = Arrays.asList("bin1", "bin2");
//
//		RestClientBatchReadBody rcBRB = tester.getBatchRead(testNamespace, null, testStrKey, bins, null);
//
//		Key key = rcBRB.key;
//		Assert.assertTrue(key.userKey instanceof Value.StringValue);
//		Assert.assertEquals(key.userKey.toString(), testStrKey);
//		Assert.assertEquals(key.namespace.toString(), testNamespace);
//		Assert.assertNull(key.setName);
//
//	}
//
//	@Test
//	public void testWithIntegerKey() throws Exception {
//
//		List<String>bins = Arrays.asList("bin1", "bin2");
//
//		RestClientBatchReadBody rcBRB = tester.getBatchRead(testNamespace, null, "5", bins, RecordKeyType.INTEGER.toString());
//
//		Key key = rcBRB.key;
//		Assert.assertTrue(key.userKey instanceof Value.LongValue);
//		Assert.assertEquals(5l, key.userKey.toInteger());
//	}
//
//	@Test
//	public void testWithBytes() throws Exception {
//
//		List<String>bins = Arrays.asList("bin1", "bin2");
//		Encoder urlEncoder  = Base64.getUrlEncoder();
//		byte[] testBytes = {1, 2, 3, 4};
//		String strBytes = urlEncoder.encodeToString(testBytes);
//
//		RestClientBatchReadBody rcBRB = tester.getBatchRead(testNamespace, null, strBytes, bins, RecordKeyType.BYTES.toString());
//
//		Key key = rcBRB.key;
//		Assert.assertTrue(key.userKey instanceof Value.BytesValue);
//		Assert.assertTrue(Arrays.equals(testBytes, (byte[])key.userKey.getObject()));
//	}
//
//	@Test
//	public void testWithDigesKeytype() throws Exception {
//
//		List<String>bins = Arrays.asList("bin1", "bin2");
//		Key digestMaker = new Key("test", "demo", 1);
//		Encoder urlEncoder  = Base64.getUrlEncoder();
//		String strBytes = urlEncoder.encodeToString(digestMaker.digest);
//
//		RestClientBatchReadBody rcBRB = tester.getBatchRead(testNamespace, null, strBytes, bins, RecordKeyType.DIGEST.toString());
//
//		Key key = rcBRB.key;
//		Assert.assertTrue(Arrays.equals(digestMaker.digest, key.digest));
//
//	}
//
//	@Test
//	public void testWithDigestNoUserKey() throws Exception {
//
//		List<String>bins = Arrays.asList("bin1", "bin2");
//		Key digestMaker = new Key("test", "demo", 1);
//
//		RestClientBatchReadBody rcBRB = tester.getBatchRead(testNamespace, digestMaker.digest);
//
//		Key key = rcBRB.key;
//		Assert.assertTrue(Arrays.equals(digestMaker.digest, key.digest));
//
//	}
//
//}
//
//interface BatchReadBodyMapperTester {
//	public RestClientBatchReadBody getBatchRead(String ns, String set, String key, List<String>bins, String keytype) throws Exception;
//	public RestClientBatchReadBody getBatchRead(String ns, byte[] digest) throws Exception;
//}
//
//class JSONBatchReadTester implements BatchReadBodyMapperTester {
//	ObjectMapper mapper = null;
//	public JSONBatchReadTester(ObjectMapper mapper) {
//		this.mapper = mapper;
//	}
//
//	@Override
//	public RestClientBatchReadBody getBatchRead(String ns, String set, String key, List<String> bins, String keytype) throws Exception {
//		Map<String, Object>batchReadBody = new HashMap<>();
//		Map<String, String>keyMap = new HashMap<>();
//
//		if (bins != null) {
//			batchReadBody.put("binNames", bins);
//		}
//		if (ns != null) {
//			keyMap.put("namespace", ns);
//		}
//		if (set != null) {
//			keyMap.put("setName", set);
//		}
//
//		if (key != null) {
//			keyMap.put("userKey", key);
//		}
//
//		if(keytype != null) {
//			keyMap.put("keytype", keytype);
//		}
//
//		batchReadBody.put("key", keyMap);
//
//
//		String batchReadStr =  mapper.writeValueAsString(batchReadBody);
//		return mapper.readValue(batchReadStr, RestClientBatchReadBody.class);
//	}
//
//	@Override
//	public RestClientBatchReadBody getBatchRead(String ns, byte[] digest) throws Exception{
//		Map<String, Object>batchReadBody = new HashMap<>();
//		Map<String, String>keyMap = new HashMap<>();
//		Encoder urlEncoder  = Base64.getUrlEncoder();
//		String strBytes = urlEncoder.encodeToString(digest);
//
//		if (ns != null) {
//			keyMap.put("namespace", ns);
//		}
//		keyMap.put("digest", strBytes);
//		batchReadBody.put("key", keyMap);
//
//		String batchReadStr = mapper.writeValueAsString(batchReadBody);
//		return mapper.readValue(batchReadStr, RestClientBatchReadBody.class);
//
//	}
//}
//
//class MsgPackBatchReadTester implements BatchReadBodyMapperTester {
//	ObjectMapper mapper = null;
//	public MsgPackBatchReadTester(ObjectMapper mapper) {
//		this.mapper = mapper;
//	}
//
//	@Override
//	public RestClientBatchReadBody getBatchRead(String ns, String set, String key, List<String> bins, String keytype) throws Exception{
//		Map<String, Object>batchReadBody = new HashMap<>();
//		Map<String, String>keyMap = new HashMap<>();
//
//		if (bins != null) {
//			batchReadBody.put("binNames", bins);
//		}
//		if (ns != null) {
//			keyMap.put("namespace", ns);
//		}
//		if (set != null) {
//			keyMap.put("setName", set);
//		}
//
//		if (key != null) {
//			keyMap.put("userKey", key);
//		}
//
//		if(keytype != null) {
//			keyMap.put("keytype", keytype);
//		}
//
//		batchReadBody.put("key", keyMap);
//
//
//		byte[] batchReadBytes =  mapper.writeValueAsBytes(batchReadBody);
//		return mapper.readValue(batchReadBytes, RestClientBatchReadBody.class);
//	}
//	@Override
//	public RestClientBatchReadBody getBatchRead(String ns, byte[] digest) throws Exception{
//		Map<String, Object>batchReadBody = new HashMap<>();
//		Map<String, String>keyMap = new HashMap<>();
//		Encoder urlEncoder  = Base64.getUrlEncoder();
//		String strBytes = urlEncoder.encodeToString(digest);
//
//		if (ns != null) {
//			keyMap.put("namespace", ns);
//		}
//		keyMap.put("digest", strBytes);
//		batchReadBody.put("key", keyMap);
//
//		byte[] batchReadBytes = mapper.writeValueAsBytes(batchReadBody);
//		return mapper.readValue(batchReadBytes, RestClientBatchReadBody.class);
//
//	}
//
//}