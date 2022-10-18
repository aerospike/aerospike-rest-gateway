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
package com.aerospike.restclient.domain;

import com.aerospike.client.Record;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class RestClientRecordTest {

    @Test
    public void testNoArgConstructor() {
        new RestClientRecord();
    }

    @Test
    public void testNullBins() {
        Record record = new Record(null, 2, 1000);
        RestClientRecord rcRecord = new RestClientRecord(record);

        Assert.assertNull(rcRecord.bins);
        Assert.assertEquals(rcRecord.generation, 2);
        Assert.assertEquals(rcRecord.ttl, record.getTimeToLive());
    }

    @Test
    public void testWithBins() {
        Map<String, Object> bins = new HashMap<>();
        bins.put("bin1", 5l);
        bins.put("bin2", "hello");
        Record record = new Record(bins, 2, 1000);
        RestClientRecord rcRecord = new RestClientRecord(record);

        Assert.assertEquals(rcRecord.bins, bins);
        Assert.assertEquals(rcRecord.generation, 2);
        Assert.assertEquals(rcRecord.ttl, record.getTimeToLive());
    }
}
