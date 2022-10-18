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
        if (stmtMap.containsKey(AerospikeAPIConstants.RECORDS_PER_SECOND)) {
            stmt.setRecordsPerSecond(getIntValue(stmtMap.get(AerospikeAPIConstants.RECORDS_PER_SECOND)));
        }

        stmt.setMaxRecords(getLongValue(
                stmtMap.getOrDefault(AerospikeAPIConstants.MAX_RECORDS, AerospikeAPIConstants.MAX_RECORDS_DEFAULT)));

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
