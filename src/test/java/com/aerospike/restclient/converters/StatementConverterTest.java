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
package com.aerospike.restclient.converters;

import com.aerospike.client.query.Statement;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.aerospike.restclient.util.converters.StatementConverter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

public class StatementConverterTest {
    MultiValueMap<String, String> policyMap;

    @Before
    public void setup() {
        policyMap = new LinkedMultiValueMap<>();
    }

    @Test
    public void testRecordBins() {
        List<String> multiVal = new ArrayList<>();
        multiVal.add("bin1");
        multiVal.add("bin2");
        multiVal.add("bin3");
        policyMap.put(AerospikeAPIConstants.RECORD_BINS, multiVal);
        Statement stmt = StatementConverter.statementFromMultiMap(policyMap);
        Assert.assertArrayEquals(new String[]{"bin1", "bin2", "bin3"}, stmt.getBinNames());
    }

    @Test
    public void testMaxConcurrentNodes() {
        List<String> multiVal = new ArrayList<>();
        multiVal.add("multiVal");
        policyMap.put(AerospikeAPIConstants.QUERY_INDEX_NAME, multiVal);
        Statement stmt = StatementConverter.statementFromMultiMap(policyMap);
        Assert.assertEquals("multiVal", stmt.getIndexName());
    }

    @Test
    public void testMaxRecords() {
        List<String> multiVal = new ArrayList<>();
        multiVal.add("100");
        policyMap.put(AerospikeAPIConstants.MAX_RECORDS, multiVal);
        Statement stmt = StatementConverter.statementFromMultiMap(policyMap);
        Assert.assertEquals(100, stmt.getMaxRecords());
    }

    @Test
    public void testRecordsPerSecond() {
        List<String> multiVal = new ArrayList<>();
        multiVal.add("101");
        policyMap.put(AerospikeAPIConstants.RECORDS_PER_SECOND, multiVal);
        Statement stmt = StatementConverter.statementFromMultiMap(policyMap);
        Assert.assertEquals(101, stmt.getRecordsPerSecond());
    }

}
