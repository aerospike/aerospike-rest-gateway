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
package com.aerospike.restclient.domain;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.msgpack.jackson.dataformat.MessagePackFactory;

import com.aerospike.client.BatchRead;
import com.aerospike.client.Key;
import com.aerospike.restclient.ASTestUtils;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.aerospike.restclient.util.AerospikeAPIConstants.RecordKeyType;
import com.aerospike.restclient.util.RestClientErrors;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(Parameterized.class)
public class RestClientBatchReadBodyTest {

	private BatchReadBodyMapper mapper;
	private static String ns = "test";
	private static String set = "set";


	@Parameters
	public static Object[] getParams() {
		return new Object[] {
				new JsonBatchReadBodyMapper(new ObjectMapper()),
				new MsgPackBatchReadBodyMapper(new ObjectMapper(new MessagePackFactory()))
		};
	}

	public RestClientBatchReadBodyTest(BatchReadBodyMapper mapper) {
		this.mapper = mapper;
	}

	@Test
	public void testNoArgConstructor() {
		new RestClientBatchReadBody();
	}

	@Test
	public void testObjectMappedConstructionStringKey() throws Exception {
		Map<String, Object>keyMap = getKeyMap("key", RecordKeyType.STRING);
		Map<String, Object>batchMap = getBatchReadBase();
		batchMap.put("key", keyMap);

		RestClientBatchReadBody mappedBody = mapper.mapToBatchReadBody(batchMap);

		Assert.assertEquals(mappedBody.readAllBins, true);
		Assert.assertArrayEquals(mappedBody.binNames, new String[] {});
		RestClientKey rcKey = mappedBody.key;

		Assert.assertEquals(RecordKeyType.STRING, rcKey.keytype);
	}

	@Test
	public void testObjectMappedConstructionWithBins() throws Exception {
		Map<String, Object>keyMap = getKeyMap("key", RecordKeyType.STRING);
		Map<String, Object>batchMap = getBatchReadBase();
		batchMap.put("key", keyMap);
		String[] bins = {"a", "b", "c"};
		batchMap.put("binNames", bins);

		RestClientBatchReadBody mappedBody = mapper.mapToBatchReadBody(batchMap);

		Assert.assertEquals(mappedBody.readAllBins, true);
		Assert.assertArrayEquals(mappedBody.binNames, bins);
		RestClientKey rcKey = mappedBody.key;

		Assert.assertEquals(RecordKeyType.STRING, rcKey.keytype);
	}

	@Test
	public void testObjectMappedConstructionIntegerKey() throws Exception {
		Map<String, Object>keyMap = getKeyMap(5, RecordKeyType.INTEGER);
		Map<String, Object>batchMap = getBatchReadBase();
		batchMap.put("key", keyMap);

		RestClientBatchReadBody mappedBody = mapper.mapToBatchReadBody(batchMap);

		Assert.assertEquals(mappedBody.readAllBins, true);
		Assert.assertArrayEquals(mappedBody.binNames, new String[] {});
		RestClientKey rcKey = mappedBody.key;

		Assert.assertEquals(RecordKeyType.INTEGER, rcKey.keytype);
	}

	@Test
	public void testObjectMappedConstructionBytesKey() throws Exception {
		Map<String, Object>keyMap = getKeyMap("lookslikebytes", RecordKeyType.BYTES);
		Map<String, Object>batchMap = getBatchReadBase();
		batchMap.put("key", keyMap);

		RestClientBatchReadBody mappedBody = mapper.mapToBatchReadBody(batchMap);

		Assert.assertEquals(mappedBody.readAllBins, true);
		Assert.assertArrayEquals(mappedBody.binNames, new String[] {});
		RestClientKey rcKey = mappedBody.key;

		Assert.assertEquals(RecordKeyType.BYTES, rcKey.keytype);
	}

	@Test
	public void testObjectMappedConstructionDigestKey() throws Exception {
		Map<String, Object>keyMap = getKeyMap("digest", RecordKeyType.DIGEST);
		Map<String, Object>batchMap = getBatchReadBase();
		batchMap.put("key", keyMap);

		RestClientBatchReadBody mappedBody = mapper.mapToBatchReadBody(batchMap);

		Assert.assertEquals(mappedBody.readAllBins, true);
		Assert.assertArrayEquals(mappedBody.binNames, new String[] {});
		RestClientKey rcKey = mappedBody.key;

		Assert.assertEquals(RecordKeyType.DIGEST, rcKey.keytype);
	}

	@Test
	public void testConversionToBatchReadWithBinNames() throws Exception {
		Key realKey = new Key(ns, set, "test");
		RestClientKey rcKey = new RestClientKey(realKey);
		String[] bins = {"b1", "b2", "b3"};
		RestClientBatchReadBody rCBRB = new RestClientBatchReadBody();

		rCBRB.binNames = bins;
		rCBRB.key = rcKey;
		rCBRB.readAllBins = false;

		BatchRead convertedBatchRead = rCBRB.toBatchRead();

		Assert.assertFalse(convertedBatchRead.readAllBins);
		Assert.assertTrue(ASTestUtils.compareKeys(realKey, convertedBatchRead.key));
		Assert.assertArrayEquals(bins, convertedBatchRead.binNames);
	}

	@Test
	public void testConversionToBatchReadWithoutBinNames() throws Exception {
		Key realKey = new Key(ns, set, "test");
		RestClientKey rcKey = new RestClientKey(realKey);
		String[] bins = {"b1", "b2", "b3"};
		RestClientBatchReadBody rCBRB = new RestClientBatchReadBody();

		rCBRB.binNames = bins;
		rCBRB.key = rcKey;
		rCBRB.readAllBins = true;

		BatchRead convertedBatchRead = rCBRB.toBatchRead();

		Assert.assertTrue(convertedBatchRead.readAllBins);
		Assert.assertTrue(ASTestUtils.compareKeys(realKey, convertedBatchRead.key));
		Assert.assertNull(convertedBatchRead.binNames);
	}

	@Test
	public void testConversionToBatchReadWithOnlyKeySet() throws Exception {
		Key realKey = new Key(ns, set, "test");
		RestClientKey rcKey = new RestClientKey(realKey);
		RestClientBatchReadBody rCBRB = new RestClientBatchReadBody();

		rCBRB.key = rcKey;

		BatchRead convertedBatchRead = rCBRB.toBatchRead();

		Assert.assertFalse(convertedBatchRead.readAllBins);
		Assert.assertTrue(ASTestUtils.compareKeys(realKey, convertedBatchRead.key));
		Assert.assertNull(convertedBatchRead.binNames);
	}

	@Test(expected=RestClientErrors.InvalidKeyError.class)
	public void testConversionWithNullKey() throws Exception {
		RestClientBatchReadBody rCBRB = new RestClientBatchReadBody();
		rCBRB.key = null;
		rCBRB.toBatchRead();
	}


	/* HELPERS */
	private Map<String, Object>getBatchReadBase(){
		Map<String, Object>batchMap = new HashMap<>();
		batchMap.put("readAllBins", true);
		batchMap.put("binNames", new String[] {});
		return batchMap;
	}
	private Map<String, Object>getKeyMap(Object userKey, RecordKeyType keytype) {
		Map<String, Object>keyMap = new HashMap<>();
		keyMap.put(AerospikeAPIConstants.NAMESPACE, ns);
		keyMap.put(AerospikeAPIConstants.SETNAME, set);
		keyMap.put(AerospikeAPIConstants.USER_KEY, userKey);

		if (keytype != null) {
			keyMap.put(AerospikeAPIConstants.KEY_TYPE, keytype.toString());
		}

		return keyMap;
	}
}

interface BatchReadBodyMapper {
	RestClientBatchReadBody mapToBatchReadBody(Map<String, Object>brMap) throws Exception;
}

class JsonBatchReadBodyMapper implements BatchReadBodyMapper {
	TypeReference<RestClientBatchReadBody>brbType = new TypeReference<RestClientBatchReadBody>() {};
	private ObjectMapper mapper;
	public JsonBatchReadBodyMapper(ObjectMapper mapper) {
		this.mapper = mapper;
	}
	@Override
	public
	RestClientBatchReadBody mapToBatchReadBody(Map<String, Object>brMap) throws Exception {
		return mapper.readValue(mapper.writeValueAsString(brMap), brbType);
	}
}

class MsgPackBatchReadBodyMapper implements BatchReadBodyMapper {
	TypeReference<RestClientBatchReadBody>brbType = new TypeReference<RestClientBatchReadBody>() {};
	private ObjectMapper mapper;
	public MsgPackBatchReadBodyMapper(ObjectMapper mapper) {
		this.mapper = mapper;
	}
	@Override
	public
	RestClientBatchReadBody mapToBatchReadBody(Map<String, Object>brMap) throws Exception {
		return mapper.readValue(mapper.writeValueAsBytes(brMap), brbType);
	}
}
