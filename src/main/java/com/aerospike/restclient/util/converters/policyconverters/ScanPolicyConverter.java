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
import com.aerospike.client.policy.ScanPolicy;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.aerospike.restclient.util.converters.PolicyValueConverter;

import java.util.Map;

public class ScanPolicyConverter {

    public static ScanPolicy scanPolicyFromMap(Map<String, String> policyMap) {
        Policy basePolicy = PolicyConverter.policyFromMap(policyMap);
        ScanPolicy scanPolicy = new ScanPolicy(basePolicy);

        if (policyMap.containsKey(AerospikeAPIConstants.MAX_RECORDS)) {
            scanPolicy.maxRecords = PolicyValueConverter.getLongValue(policyMap.get(AerospikeAPIConstants.MAX_RECORDS));
        }
        if (policyMap.containsKey(AerospikeAPIConstants.RECORDS_PER_SECOND)) {
            scanPolicy.recordsPerSecond = PolicyValueConverter.getIntValue(
                    policyMap.get(AerospikeAPIConstants.RECORDS_PER_SECOND));
        }
        if (policyMap.containsKey(AerospikeAPIConstants.MAX_CONCURRENT_NODES)) {
            scanPolicy.maxConcurrentNodes = PolicyValueConverter.getIntValue(
                    policyMap.get(AerospikeAPIConstants.MAX_CONCURRENT_NODES));
        }
        if (policyMap.containsKey(AerospikeAPIConstants.CONCURRENT_NODES)) {
            scanPolicy.concurrentNodes = PolicyValueConverter.getBoolValue(
                    policyMap.get(AerospikeAPIConstants.CONCURRENT_NODES));
        }
        if (policyMap.containsKey(AerospikeAPIConstants.INCLUDE_BIN_DATA)) {
            scanPolicy.includeBinData = PolicyValueConverter.getBoolValue(
                    policyMap.get(AerospikeAPIConstants.INCLUDE_BIN_DATA));
        }
        return scanPolicy;
    }

}
