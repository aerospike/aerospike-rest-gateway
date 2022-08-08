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

import com.aerospike.client.*;
import com.aerospike.restclient.domain.batchmodels.RestClientBatchRecordResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.aerospike.restclient.ASTestUtils;

public class RestClientBatchRecordResponseTest {

	private final static String ns = "test";
	private final static String set = "set";
	private Record testRecord;
	private final Key testKey = new Key(ns, set, "key");

	@Before
	public void setUpRecord() {
		Map<String, Object>binMap = new HashMap<>();
		binMap.put("bin1", "val1");
		testRecord = new Record(binMap, 1, 1);
	}

	@Test
	public void testNoArgConstructor() {
		new RestClientBatchRecordResponse();
	}

	// Ensures backwards compatibility with older BatchRead API
	@Test
	public void constructionWithBinNames() {
		String[] testBins = {"b1", "b2", "b3"};
		BatchRead nullRecordRead = getBatchRead(testKey, testBins, false,  null);

		RestClientBatchRecordResponse response = new RestClientBatchRecordResponse(nullRecordRead);

		Assert.assertNull(response.record);
	}

	@Test
	public void constructionWithoutBinNamesReadAllBins() {
		BatchRead nullRecordRead = getBatchRead(testKey, null, true, null);

		RestClientBatchRecordResponse response = new RestClientBatchRecordResponse(nullRecordRead);

		Assert.assertNull(response.record);
	}

	@Test
	public void constructionWithoutBinNamesReadAllBinsFalse() {
		BatchRead nullRecordRead = getBatchRead(testKey, null, false, null);

		RestClientBatchRecordResponse response = new RestClientBatchRecordResponse(nullRecordRead);

		Assert.assertNull(response.record);
	}

	@Test
	public void constructionOfUserKey() {
		BatchRecord nullRecordRead = new BatchRecord(testKey, null, ResultCode.MAX_ERROR_RATE, true, true);
		RestClientBatchRecordResponse response = new RestClientBatchRecordResponse(nullRecordRead);
		Key convertedKey = response.key.toKey();
		Assert.assertTrue(ASTestUtils.compareKeys(testKey, convertedKey));
		Assert.assertEquals(response.resultCode, ResultCode.MAX_ERROR_RATE);
		Assert.assertEquals(response.resultCodeString, ResultCode.getResultString(ResultCode.MAX_ERROR_RATE));
		Assert.assertTrue(response.inDoubt);
	}

	@Test
	public void constructionOfRecord() {
		BatchRecord batchRecord = new BatchRecord(testKey, testRecord, ResultCode.MAX_ERROR_RATE, false, false);
		RestClientBatchRecordResponse response = new RestClientBatchRecordResponse(batchRecord);

		RestClientRecord convertedRecord = response.record;
		Assert.assertEquals(convertedRecord.bins, testRecord.bins);
		Assert.assertEquals(convertedRecord.ttl, testRecord.getTimeToLive());
		Assert.assertEquals(convertedRecord.generation, testRecord.generation);
		Assert.assertEquals(response.resultCode, ResultCode.MAX_ERROR_RATE);
		Assert.assertEquals(response.resultCodeString, ResultCode.getResultString(ResultCode.MAX_ERROR_RATE));
		Assert.assertFalse(response.inDoubt);
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

	private BatchRecord getBatchRecord(Key key, Record record, int resultCode, boolean hasWrite) {
		return new BatchRecord(key, record, resultCode, false, hasWrite);
	}
}