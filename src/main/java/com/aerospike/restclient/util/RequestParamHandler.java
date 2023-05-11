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
package com.aerospike.restclient.util;

import com.aerospike.client.policy.*;
import com.aerospike.client.query.Statement;
import com.aerospike.restclient.util.AerospikeAPIConstants.RecordKeyType;
import com.aerospike.restclient.util.converters.PolicyValueConverter;
import com.aerospike.restclient.util.converters.StatementConverter;
import com.aerospike.restclient.util.converters.policyconverters.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;

public final class RequestParamHandler {

    private RequestParamHandler() {
    }

    public static String[] getBinsFromMap(MultiValueMap<String, String> requestParams) {
        List<String> binStr = arrayAware(requestParams).get(AerospikeAPIConstants.RECORD_BINS);
        if (binStr == null) {
            return new String[0];
        }
        return binStr.toArray(new String[0]);
    }

    public static String[] getKeysFromMap(MultiValueMap<String, String> requestParams) {
        List<String> keys = arrayAware(requestParams).get(AerospikeAPIConstants.RECORD_KEY);
        if (keys == null) {
            return new String[0];
        }
        return keys.toArray(new String[0]);
    }

    public static String getJsonPathFromMap(MultiValueMap<String, String> requestParams) {
        return requestParams.getFirst(AerospikeAPIConstants.JSON_PATH);
    }

    public static RecordKeyType getKeyTypeFromMap(MultiValueMap<String, String> requestParams) {
        String keyTypeStr = requestParams.getFirst(AerospikeAPIConstants.KEY_TYPE);
        if (keyTypeStr == null) {
            return null;
        }
        try {
            return RecordKeyType.valueOf(keyTypeStr);
        } catch (IllegalArgumentException e) {
            throw new RestClientErrors.InvalidPolicyValueError(
                    String.format("Invalid keytype: %s , valid choices are STRING, INTEGER, BYTES, DIGEST",
                            keyTypeStr));
        }
    }

    public static RecordKeyType getKeyTypeFromMap(Map<String, String> requestParams) {
        String keyTypeStr = requestParams.get(AerospikeAPIConstants.KEY_TYPE);
        if (keyTypeStr == null) {
            return null;
        }
        try {
            return RecordKeyType.valueOf(keyTypeStr);
        } catch (IllegalArgumentException e) {
            throw new RestClientErrors.InvalidPolicyValueError(
                    String.format("Invalid keytype: %s , valid choices are STRING, INTEGER, BYTES, DIGEST",
                            keyTypeStr));
        }
    }

    public static boolean getGetToken(MultiValueMap<String, String> requestParams) {
        List<String> keys = requestParams.get(AerospikeAPIConstants.GET_TOKEN);
        if (keys == null || keys.isEmpty()) {
            return false;
        }
        return PolicyValueConverter.getBoolValue(keys.get(0));
    }

    public static Policy getPolicy(Map<String, String> requestParams) {
        return PolicyConverter.policyFromMap(requestParams);
    }

    public static Policy getPolicy(MultiValueMap<String, String> requestParams) {
        return PolicyConverter.policyFromMap(requestParams.toSingleValueMap());
    }

    public static WritePolicy getWritePolicy(Map<String, String> requestParams) {
        return WritePolicyConverter.writePolicyFromMap(requestParams);
    }

    public static WritePolicy getWritePolicy(Map<String, String> requestParams, RecordExistsAction existsAction) {
        return WritePolicyConverter.writePolicyWithRecordExistsAction(requestParams, existsAction);
    }

    public static BatchPolicy getBatchPolicy(Map<String, String> requestParams) {
        return BatchPolicyConverter.batchPolicyFromMap(requestParams);
    }

    public static ScanPolicy getScanPolicy(Map<String, String> requestParams) {
        return ScanPolicyConverter.scanPolicyFromMap(requestParams);
    }

    public static QueryPolicy getQueryPolicy(Map<String, String> requestParams) {
        return QueryPolicyConverter.queryPolicyFromMap(requestParams);
    }

    public static Statement getStatement(MultiValueMap<String, String> requestParams) {
        return StatementConverter.statementFromMultiMap(arrayAware(requestParams));
    }

    public static InfoPolicy getInfoPolicy(Map<String, String> requestParams) {
        return InfoPolicyConverter.policyFromMap(requestParams);
    }

    private static MultiValueMap<String, String> arrayAware(MultiValueMap<String, String> requestParams) {
        MultiValueMap<String, String> parsed = new LinkedMultiValueMap<>();
        for (var e : requestParams.entrySet()) {
            parsed.addAll(e.getKey().replaceAll("\\[\\d?\\]$", ""), e.getValue());
        }
        return parsed;
    }
}
