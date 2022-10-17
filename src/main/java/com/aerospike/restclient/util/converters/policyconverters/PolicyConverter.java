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
package com.aerospike.restclient.util.converters.policyconverters;

import com.aerospike.client.policy.Policy;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.aerospike.restclient.util.converters.PolicyValueConverter;

import java.util.Map;

public class PolicyConverter {

    public static Policy policyFromMap(Map<String, String> policyMap) {
        Policy policy = new Policy();

        if (policyMap.containsKey(AerospikeAPIConstants.TOTAL_TIMEOUT)) {
            policy.totalTimeout = PolicyValueConverter.getIntValue(policyMap.get(AerospikeAPIConstants.TOTAL_TIMEOUT));
        }
        if (policyMap.containsKey(AerospikeAPIConstants.SOCKET_TIMEOUT)) {
            policy.socketTimeout = PolicyValueConverter.getIntValue(
                    policyMap.get(AerospikeAPIConstants.SOCKET_TIMEOUT));
        }
        if (policyMap.containsKey(AerospikeAPIConstants.SLEEP_BETWEEN_RETRIES)) {
            policy.sleepBetweenRetries = PolicyValueConverter.getIntValue(
                    policyMap.get(AerospikeAPIConstants.SLEEP_BETWEEN_RETRIES));
        }
        if (policyMap.containsKey(AerospikeAPIConstants.MAX_RETRIES)) {
            policy.maxRetries = PolicyValueConverter.getIntValue(policyMap.get(AerospikeAPIConstants.MAX_RETRIES));
        }
        if (policyMap.containsKey(AerospikeAPIConstants.SEND_KEY)) {
            policy.sendKey = PolicyValueConverter.getBoolValue(policyMap.get(AerospikeAPIConstants.SEND_KEY));
        }
        if (policyMap.containsKey(AerospikeAPIConstants.REPLICA)) {
            policy.replica = PolicyValueConverter.getReplica(policyMap.get(AerospikeAPIConstants.REPLICA));
        }
        if (policyMap.containsKey(AerospikeAPIConstants.READ_MODE_AP)) {
            policy.readModeAP = PolicyValueConverter.getReadModeAP(policyMap.get(AerospikeAPIConstants.READ_MODE_AP));
        }
        if (policyMap.containsKey(AerospikeAPIConstants.READ_MODE_SC)) {
            policy.readModeSC = PolicyValueConverter.getReadModeSC(policyMap.get(AerospikeAPIConstants.READ_MODE_SC));
        }
        if (policyMap.containsKey(AerospikeAPIConstants.FILTER_EXP)) {
            policy.filterExp = PolicyValueConverter.getFilterExp(policyMap.get(AerospikeAPIConstants.FILTER_EXP));
        }
        if (policyMap.containsKey(AerospikeAPIConstants.COMPRESS)) {
            policy.compress = PolicyValueConverter.getCompress(policyMap.get(AerospikeAPIConstants.COMPRESS));
        }
        return policy;
    }
}
