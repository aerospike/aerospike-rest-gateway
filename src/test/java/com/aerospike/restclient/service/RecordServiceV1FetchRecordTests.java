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

import static org.mockito.AdditionalMatchers.aryEq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Base64;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.restclient.ASTestUtils;
import com.aerospike.restclient.ASTestUtils.KeyMatcher;
import com.aerospike.restclient.handlers.RecordHandler;
import com.aerospike.restclient.util.AerospikeAPIConstants.RecordKeyType;
import com.aerospike.restclient.util.RestClientErrors;

@RunWith(MockitoJUnitRunner.class)
public class RecordServiceV1FetchRecordTests {
	private String ns = "ns";
	private String set = "set";
	private String strKey = "pk";
	private String[] emptyBins = new String[] {};
	private String[]	testBins = new String[] {"bin1", "bin2"};
	private byte[] bytesKey = new byte[] {1,2,3,4};

	// Returning null triggers an exception, so this is an empty record to return instead
	private Record dummyRecord = new Record(new HashMap<String, Object>(), 1, 1);

	@InjectMocks AerospikeRecordServiceV1 recordService;
	@Mock RecordHandler handler;

	@Before
	public void setupMockReturn() {
		when(handler.getRecord(any(), any(Key.class))).thenReturn(dummyRecord);

	}
	@Test
	public void testStringKeyNoType() {
		Key expectedKey = new Key(ns, set, strKey);
		KeyMatcher matcher = new ASTestUtils.KeyMatcher(expectedKey);
		recordService.fetchRecord(ns, set, strKey, emptyBins, null, null);

		verify(handler, Mockito.only()).getRecord(any(), argThat(matcher));
	}

	@Test
	public void testStringKeyStringType() {
		Key expectedKey = new Key(ns, set, strKey);
		KeyMatcher matcher = new ASTestUtils.KeyMatcher(expectedKey);
		recordService.fetchRecord(ns, set, strKey, emptyBins, RecordKeyType.STRING, null);

		verify(handler, Mockito.only()).getRecord(any(), argThat(matcher));
	}

	@Test
	public void testIntKey() {
		Key expectedKey = new Key(ns, set, 5);
		KeyMatcher matcher = new ASTestUtils.KeyMatcher(expectedKey);
		recordService.fetchRecord(ns, set, "5", emptyBins, RecordKeyType.INTEGER, null);
		verify(handler, Mockito.only()).getRecord(any(), argThat(matcher));
	}

	@Test
	public void testBytesKey() {
		Key expectedKey = new Key(ns, set, bytesKey);
		KeyMatcher matcher = new ASTestUtils.KeyMatcher(expectedKey);
		String bytesKeyStr = Base64.getUrlEncoder().encodeToString(bytesKey);
		recordService.fetchRecord(ns, set, bytesKeyStr, emptyBins, RecordKeyType.BYTES, null);
		verify(handler, Mockito.only()).getRecord(any(), argThat(matcher));
	}

	@Test
	public void testDigestKey() {
		byte[] testDigest = new byte[20];

		when(handler.getRecord(any(), any(Key.class))).thenReturn(dummyRecord);
		Key expectedKey = new Key(ns, testDigest, null, null);

		KeyMatcher matcher = new ASTestUtils.KeyMatcher(expectedKey);
		String digestStr = Base64.getUrlEncoder().encodeToString(testDigest);

		recordService.fetchRecord(ns, null, digestStr, emptyBins, RecordKeyType.DIGEST, null);
		verify(handler, Mockito.only()).getRecord(any(), argThat(matcher));
	}

	@Test
	public void testNoSetKey() {
		Key expectedKey = new Key(ns, null, strKey);
		KeyMatcher matcher = new ASTestUtils.KeyMatcher(expectedKey);
		recordService.fetchRecord(ns, null, strKey, emptyBins, null, null);
		verify(handler, Mockito.only()).getRecord(any(), argThat(matcher));
	}

	@Test
	public void testWithBins() {
		when(handler.getRecord(any(), any(Key.class), any(String[].class))).thenReturn(dummyRecord);
		Key expectedKey = new Key(ns, set, strKey);
		KeyMatcher matcher = new ASTestUtils.KeyMatcher(expectedKey);
		recordService.fetchRecord(ns, set, strKey, testBins, null, null);
		verify(handler, Mockito.only()).getRecord(any(), argThat(matcher), aryEq(testBins));
	}

	@Test(expected=RestClientErrors.RecordNotFoundError.class)
	public void testRecordNotFound() {
		when(handler.getRecord(any(), any(Key.class))).thenReturn(null);
		recordService.fetchRecord(ns, null, strKey, emptyBins, null, null);
	}
}
