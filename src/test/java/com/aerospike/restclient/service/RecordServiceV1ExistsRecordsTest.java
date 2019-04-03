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
import static org.mockito.Mockito.when;

import java.util.Base64;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.aerospike.client.Key;
import com.aerospike.restclient.ASTestUtils;
import com.aerospike.restclient.ASTestUtils.KeyMatcher;
import com.aerospike.restclient.handlers.RecordHandler;
import com.aerospike.restclient.util.AerospikeAPIConstants.RecordKeyType;

@RunWith(MockitoJUnitRunner.class)
public class RecordServiceV1ExistsRecordsTest {
	private String ns = "ns";
	private String set = "set";
	private String strKey = "pk";



	@InjectMocks AerospikeRecordServiceV1 recordService;
	@Mock RecordHandler handler;

	@Before
	public void setupMockReturn() {
		when(handler.existsRecord(any(), any(Key.class))).thenReturn(true);
	}

	@Test
	public void testRecordDoesExist() {
		when(handler.existsRecord(any(), any(Key.class))).thenReturn(true);
		Assert.assertTrue(recordService.recordExists(ns, set, strKey, null));
	}

	@Test
	public void testRecordDoesNotExist() {
		when(handler.existsRecord(any(), any(Key.class))).thenReturn(false);
		Assert.assertFalse(recordService.recordExists(ns, set, strKey, null));
	}

	@Test
	public void testStringKeyNoType() {
		Key expectedKey = new Key(ns, set, strKey);
		KeyMatcher matcher = new ASTestUtils.KeyMatcher(expectedKey);

		recordService.recordExists(ns, set, strKey, null);
		verify(handler, Mockito.only()).existsRecord(any(), argThat(matcher));
	}

	@Test
	public void testStringKeyStringType() {
		Key expectedKey = new Key(ns, set, strKey);
		KeyMatcher matcher = new ASTestUtils.KeyMatcher(expectedKey);

		recordService.recordExists(ns, set, strKey, RecordKeyType.STRING);
		verify(handler, Mockito.only()).existsRecord(any(), argThat(matcher));
	}

	@Test
	public void testIntKeyType() {
		Key expectedKey = new Key(ns, set, 5);
		KeyMatcher matcher = new ASTestUtils.KeyMatcher(expectedKey);

		recordService.recordExists(ns, set, "5", RecordKeyType.INTEGER);
		verify(handler, Mockito.only()).existsRecord(any(), argThat(matcher));
	}

	@Test
	public void testBytesType() {
		byte[] bytesKey = new byte[] {1,2,3,4};
		Key expectedKey = new Key(ns, set, bytesKey);
		KeyMatcher matcher = new ASTestUtils.KeyMatcher(expectedKey);

		String byteKeyStr = Base64.getUrlEncoder().encodeToString(bytesKey);

		recordService.recordExists(ns, set, byteKeyStr, RecordKeyType.BYTES);

		verify(handler, Mockito.only()).existsRecord(any(), argThat(matcher));
	}

	@Test
	public void testDigestType() {
		byte[] digest = new byte[20];
		Key expectedKey = new Key(ns, digest, null, null);
		KeyMatcher matcher = new ASTestUtils.KeyMatcher(expectedKey);

		String digestKeyStr = Base64.getUrlEncoder().encodeToString(digest);

		recordService.recordExists(ns, null, digestKeyStr, RecordKeyType.DIGEST);

		verify(handler, Mockito.only()).existsRecord(any(), argThat(matcher));
	}

}
