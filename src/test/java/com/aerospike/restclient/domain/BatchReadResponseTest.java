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

import com.aerospike.client.BatchRead;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.restclient.ASTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class BatchReadResponseTest {

	private static final String ns = "test";
	private static final String set = "set";
	private static final String[] testBins = {"b1", "b2", "b3"};
	private Record testRecord;
	private final Key testKey = new Key(ns, set, "key");

	@BeforeEach
	public void setUpRecord() {
		Map<String, Object>binMap = new HashMap<>();
		binMap.put("bin1", "val1");
		testRecord = new Record(binMap, 1, 1);
	}

	@Test
	public void testNoArgConstructor() {
		new RestClientBatchReadResponse();
	}

	@Test
	public void constructionWithBinNames() {
		BatchRead nullRecordRead = getBatchRead(testKey, testBins, false,  null);

		RestClientBatchReadResponse response = new RestClientBatchReadResponse(nullRecordRead);

		assertArrayEquals(testBins, response.binNames);
		assertNull(response.record);
		assertFalse(response.readAllBins);
	}

	@Test
	public void constructionWithoutBinNamesReadAllBins() {
		BatchRead nullRecordRead = getBatchRead(testKey, null, true, null);

		RestClientBatchReadResponse response = new RestClientBatchReadResponse(nullRecordRead);

		assertNull(response.binNames);
		assertNull(response.record);
		assertTrue(response.readAllBins);
	}

	@Test
	public void constructionWithoutBinNamesReadAllBinsFalse() {
		BatchRead nullRecordRead = getBatchRead(testKey, null, false, null);

		RestClientBatchReadResponse response = new RestClientBatchReadResponse(nullRecordRead);

		assertNull(response.binNames);
		assertNull(response.record);
		assertFalse(response.readAllBins);
	}

	@Test
	public void constructionOfUserKey() {
		BatchRead nullRecordRead = getBatchRead(testKey, null, false, null);
		RestClientBatchReadResponse response = new RestClientBatchReadResponse(nullRecordRead);
		Key convertedKey = response.key.toKey();
		assertTrue(ASTestUtils.compareKeys(testKey, convertedKey));
	}

	@Test
	public void constructionOfRecord() {
		BatchRead nullRecordRead = getBatchRead(testKey, null, false, testRecord);
		RestClientBatchReadResponse response = new RestClientBatchReadResponse(nullRecordRead);

		RestClientRecord convertedRecord = response.record;
		assertEquals(convertedRecord.bins, testRecord.bins);
		assertEquals(convertedRecord.ttl, testRecord.getTimeToLive());
		assertEquals(convertedRecord.generation, testRecord.generation);
	}

	private BatchRead getBatchRead(Key key, String[] bins, boolean readAllbins, Record record) {
		BatchRead read;
		if (bins != null) {
			read = new BatchRead(key, bins);
		} else {
			read = new BatchRead(key, readAllbins);
		}
		read.record = record;

		return read;
	}
}
