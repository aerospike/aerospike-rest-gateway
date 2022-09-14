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
