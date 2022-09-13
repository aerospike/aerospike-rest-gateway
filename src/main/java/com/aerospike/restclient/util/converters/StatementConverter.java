package com.aerospike.restclient.util.converters;

import com.aerospike.client.query.Statement;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.aerospike.restclient.util.RequestParamHandler;
import com.aerospike.restclient.util.RestClientErrors;
import org.springframework.util.MultiValueMap;

import java.util.Map;

public class StatementConverter {

    public static Statement statementFromMultiMap(MultiValueMap<String, String> stmtMultiMap) {
        Statement stmt = new Statement();

        if (stmtMultiMap.containsKey(AerospikeAPIConstants.RECORD_BINS)) {
            stmt.setBinNames(RequestParamHandler.getBinsFromMap(stmtMultiMap));
        }

        Map<String, String> stmtMap = stmtMultiMap.toSingleValueMap();

        if (stmtMap.containsKey(AerospikeAPIConstants.QUERY_INDEX_NAME)) {
            stmt.setIndexName(stmtMap.get(AerospikeAPIConstants.QUERY_INDEX_NAME));
        }
        if (stmtMap.containsKey(AerospikeAPIConstants.MAX_RECORDS)) {
            stmt.setMaxRecords(getLongValue(stmtMap.get(AerospikeAPIConstants.MAX_RECORDS)));
        } else {
            stmt.setMaxRecords(Long.parseLong(AerospikeAPIConstants.MAX_RECORDS_DEFAULT));
        }
        if (stmtMap.containsKey(AerospikeAPIConstants.RECORDS_PER_SECOND)) {
            stmt.setRecordsPerSecond(getIntValue(stmtMap.get(AerospikeAPIConstants.RECORDS_PER_SECOND)));
        }

        return stmt;
    }

    public static long getLongValue(String intString) {
        try {
            return Long.parseLong(intString, 10);
        } catch (NumberFormatException nfe) {
            throw new RestClientErrors.InvalidQueryError(String.format("Invalid long value: %s", intString));
        }
    }

    public static int getIntValue(String intString) {
        try {
            return Integer.parseInt(intString, 10);
        } catch (NumberFormatException nfe) {
            throw new RestClientErrors.InvalidQueryError(String.format("Invalid integer value: %s", intString));
        }
    }
}
