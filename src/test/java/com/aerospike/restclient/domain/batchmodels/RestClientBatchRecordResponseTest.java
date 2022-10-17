/*
 * Copyright 2022 Aerospike, Inc.
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
package com.aerospike.restclient.domain.batchmodels;

import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.client.ResultCode;
import com.aerospike.restclient.ASTestUtils;
import com.aerospike.restclient.domain.RestClientRecord;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class RestClientBatchRecordResponseTest {

    private final static String ns = "test";
    private final static String set = "set";
    private Record testRecord;
    private final Key testKey = new Key(ns, set, "key");

    @Before
    public void setUpRecord() {
        Map<String, Object> binMap = new HashMap<>();
        binMap.put("bin1", "val1");
        testRecord = new Record(binMap, 1, 1);
    }

    @Test
    public void testNoArgConstructor() {
        new BatchRecordResponse();
    }

    // Ensures backwards compatibility with older BatchRead API
    @Test
    public void constructionWithBinNames() {
        String[] testBins = {"b1", "b2", "b3"};
        com.aerospike.client.BatchRecord nullRecordRead = getBatchRead(testKey, testBins, false, null);

        BatchRecordResponse response = new BatchRecordResponse(nullRecordRead);

        Assert.assertNull(response.record);
    }

    @Test
    public void constructionWithoutBinNamesReadAllBins() {
        com.aerospike.client.BatchRecord nullRecordRead = getBatchRead(testKey, null, true, null);

        BatchRecordResponse response = new BatchRecordResponse(nullRecordRead);

        Assert.assertNull(response.record);
    }

    @Test
    public void constructionWithoutBinNamesReadAllBinsFalse() {
        com.aerospike.client.BatchRecord nullRecordRead = getBatchRead(testKey, null, false, null);

        BatchRecordResponse response = new BatchRecordResponse(nullRecordRead);

        Assert.assertNull(response.record);
    }

    @Test
    public void constructionOfUserKey() {
        com.aerospike.client.BatchRecord nullRecordRead = new com.aerospike.client.BatchRecord(testKey, null,
                ResultCode.MAX_ERROR_RATE, true, true);
        BatchRecordResponse response = new BatchRecordResponse(nullRecordRead);
        Key convertedKey = response.key.toKey();
        Assert.assertTrue(ASTestUtils.compareKeys(testKey, convertedKey));
        Assert.assertEquals(response.resultCode, ResultCode.MAX_ERROR_RATE);
        Assert.assertEquals(response.resultCodeString, ResultCode.getResultString(ResultCode.MAX_ERROR_RATE));
        Assert.assertTrue(response.inDoubt);
    }

    @Test
    public void constructionOfRecord() {
        com.aerospike.client.BatchRecord batchRecord = new com.aerospike.client.BatchRecord(testKey, testRecord,
                ResultCode.MAX_ERROR_RATE, false, false);
        BatchRecordResponse response = new BatchRecordResponse(batchRecord);

        RestClientRecord convertedRecord = response.record;
        Assert.assertEquals(convertedRecord.bins, testRecord.bins);
        Assert.assertEquals(convertedRecord.ttl, testRecord.getTimeToLive());
        Assert.assertEquals(convertedRecord.generation, testRecord.generation);
        Assert.assertEquals(response.resultCode, ResultCode.MAX_ERROR_RATE);
        Assert.assertEquals(response.resultCodeString, ResultCode.getResultString(ResultCode.MAX_ERROR_RATE));
        Assert.assertFalse(response.inDoubt);
    }

    private com.aerospike.client.BatchRead getBatchRead(Key key, String[] bins, boolean readAllbins, Record record) {
        com.aerospike.client.BatchRead read;
        if (bins != null) {
            read = new com.aerospike.client.BatchRead(key, bins);
        } else {
            read = new com.aerospike.client.BatchRead(key, readAllbins);
        }
        read.record = record;

        return read;
    }
}