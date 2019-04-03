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
package com.aerospike.restclient.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.restclient.ASTestUtils;
import com.aerospike.restclient.ASTestUtils.BinMatcher;
import com.aerospike.restclient.ASTestUtils.KeyMatcher;
import com.aerospike.restclient.handlers.RecordHandler;
import com.aerospike.restclient.util.AerospikeAPIConstants.RecordKeyType;

@RunWith(MockitoJUnitRunner.class)
public class RecordServiceV1CreateRecordsTest {
	private String ns = "ns";
	private String set = "set";
	private String strKey = "pk";
	private byte[] bytesKey = new byte[] {1,2,3,4};

	// For the tests of keytypes, we don't really care what bin we use.
	private Bin testBin = new Bin("bin1", "unimportant");
	private BinMatcher testBinMatcher = new BinMatcher(testBin);
	private Map<String, Object>testBinMap;

	@InjectMocks AerospikeRecordServiceV1 recordService;
	@Mock RecordHandler handler;

	@Before
	public void setup() {
		testBinMap = new HashMap<String, Object>();
		testBinMap.put(testBin.name, testBin.value.toString());
	}

	@Test
	public void testStringKeyNoType() {
		Key expectedKey = new Key(ns, set, strKey);
		KeyMatcher matcher = new ASTestUtils.KeyMatcher(expectedKey);
		recordService.storeRecord(ns, set, strKey, testBinMap, null, null);

		verify(handler, Mockito.only()).putRecord(any(), argThat(matcher), argThat(testBinMatcher));
	}

	@Test
	public void testStringKeyStringType() {
		Key expectedKey = new Key(ns, set, strKey);
		KeyMatcher matcher = new ASTestUtils.KeyMatcher(expectedKey);
		recordService.storeRecord(ns, set, strKey, testBinMap, RecordKeyType.STRING, null);

		verify(handler, Mockito.only()).putRecord(any(), argThat(matcher), argThat(testBinMatcher));
	}
	//
	@Test
	public void testIntegerKey() {
		Key expectedKey = new Key(ns, set, 5);
		KeyMatcher matcher = new ASTestUtils.KeyMatcher(expectedKey);
		recordService.storeRecord(ns, set, "5", testBinMap, RecordKeyType.INTEGER, null);

		verify(handler, Mockito.only()).putRecord(any(), argThat(matcher), argThat(testBinMatcher));
	}

	@Test
	public void testBytesType() {
		Key expectedKey = new Key(ns, set, bytesKey);
		KeyMatcher matcher = new ASTestUtils.KeyMatcher(expectedKey);

		String bytesKeyStr = Base64.getUrlEncoder().encodeToString(bytesKey);
		recordService.storeRecord(ns, set, bytesKeyStr, testBinMap, RecordKeyType.BYTES, null);

		verify(handler, Mockito.only()).putRecord(any(), argThat(matcher), argThat(testBinMatcher));
	}

	@Test
	public void testDigestType() {
		byte[] digest = new byte[20];
		Key expectedKey = new Key(ns, digest, null, null);
		KeyMatcher matcher = new ASTestUtils.KeyMatcher(expectedKey);

		String digestStr = Base64.getUrlEncoder().encodeToString(digest);
		recordService.storeRecord(ns, null, digestStr, testBinMap, RecordKeyType.DIGEST, null);

		verify(handler, Mockito.only()).putRecord(any(), argThat(matcher), argThat(testBinMatcher));
	}

	@Test
	public void testStringBin() {
		singleValueBinTest("aerospike");
	}

	@Test
	public void testLongBin() {
		singleValueBinTest(5l);
	}

	@Test
	public void testFloatBin() {
		singleValueBinTest(3.14);
	}

	@Test
	public void testListBin() {
		singleValueBinTest(Arrays.asList(1l, "aero", 3.14));
	}

	@Test
	public void testMapBin() {
		Map<Object, Object>objectBin = new HashMap<>();
		objectBin.put(3l, 14l);
		objectBin.put("hello", "world");
		singleValueBinTest(objectBin);
	}

	@Test
	public void multiBinTest() {
		ArgumentCaptor<Bin> varArgs = ArgumentCaptor.forClass(Bin.class);

		Map<String, Object>binMap = new HashMap<>();
		Bin strBin = new Bin("bin1", "aerospike");
		Bin lBin = new Bin("bin2", 5l);

		binMap.put(strBin.name, strBin.value);
		binMap.put(lBin.name, lBin.value);

		recordService.storeRecord(ns, set, strKey, binMap, null, null);

		verify(handler, Mockito.only()).putRecord(any(), any(Key.class), varArgs.capture());

		Assert.assertTrue(sameBins(varArgs.getAllValues(), lBin, strBin));
	}

	private void singleValueBinTest(Object value) {
		Map<String, Object>binMap = new HashMap<>();
		Bin strBin = new Bin("bin1", value);
		BinMatcher matchesBin = new BinMatcher(strBin);
		binMap.put(strBin.name, strBin.value);

		recordService.storeRecord(ns, set, strKey, binMap, null, null);

		verify(handler, Mockito.only()).putRecord(any(), any(Key.class), argThat(matchesBin));
	}

	private boolean sameBins(List<Bin>binList, Bin...testBins) {
		for (Bin bin: testBins) {
			boolean found = false;
			for (Bin actual: binList) {
				if (actual.equals(bin)) {
					found = true;
					break;
				}
			}
			if (!found) {
				return false;
			}
		}
		return testBins.length == binList.size();
	}
}
