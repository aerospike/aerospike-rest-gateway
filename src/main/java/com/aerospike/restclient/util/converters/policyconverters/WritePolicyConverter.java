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
import com.aerospike.client.policy.RecordExistsAction;
import com.aerospike.client.policy.WritePolicy;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.aerospike.restclient.util.converters.PolicyValueConverter;

import java.util.Map;

public class WritePolicyConverter {

    public static WritePolicy writePolicyFromMap(Map<String, String> policyMap) {
        Policy basePolicy = PolicyConverter.policyFromMap(policyMap);
        WritePolicy writePolicy = new WritePolicy(basePolicy);

        /* Write policy defaults to zero max_retries, but Policy defaults to 2,
         * Undo the setting if it isn't explicitly set
         */
        if (!policyMap.containsKey(AerospikeAPIConstants.MAX_RETRIES)) {
            writePolicy.maxRetries = 0;
        }

        if (policyMap.containsKey(AerospikeAPIConstants.EXPIRATION)) {
            writePolicy.expiration = PolicyValueConverter.getIntValue(policyMap.get(AerospikeAPIConstants.EXPIRATION));
        }
        if (policyMap.containsKey(AerospikeAPIConstants.GENERATION)) {
            writePolicy.generation = PolicyValueConverter.getIntValue(policyMap.get(AerospikeAPIConstants.GENERATION));
        }
        if (policyMap.containsKey(AerospikeAPIConstants.DURABLE_DELETE)) {
            writePolicy.durableDelete = PolicyValueConverter.getBoolValue(
                    policyMap.get(AerospikeAPIConstants.DURABLE_DELETE));
        }
        if (policyMap.containsKey(AerospikeAPIConstants.RESPOND_ALL_OPS)) {
            writePolicy.respondAllOps = PolicyValueConverter.getBoolValue(
                    policyMap.get(AerospikeAPIConstants.RESPOND_ALL_OPS));
        }
        if (policyMap.containsKey(AerospikeAPIConstants.COMMIT_LEVEL)) {
            writePolicy.commitLevel = PolicyValueConverter.getCommitLevel(
                    policyMap.get(AerospikeAPIConstants.COMMIT_LEVEL));
        }
        if (policyMap.containsKey(AerospikeAPIConstants.GENERATION_POLICY)) {
            writePolicy.generationPolicy = PolicyValueConverter.getGenerationPolicy(
                    policyMap.get(AerospikeAPIConstants.GENERATION_POLICY));
        }
        if (policyMap.containsKey(AerospikeAPIConstants.RECORD_EXISTS_ACTION)) {
            writePolicy.recordExistsAction = PolicyValueConverter.getRecordExistsAction(
                    policyMap.get(AerospikeAPIConstants.RECORD_EXISTS_ACTION));
        }
        return writePolicy;
    }

    /*
     * Create the write policy from a map, but override the recordExists action.
     * This is used in the post/put/update kvs operations to enforce the HTTP Method semantics
     */
    public static WritePolicy writePolicyWithRecordExistsAction(Map<String, String> policyMap,
                                                                RecordExistsAction existsAction) {

        WritePolicy writePolicy = writePolicyFromMap(policyMap);
        writePolicy.recordExistsAction = existsAction;
        /* We default to the specified exists action, but allow a user to override it
         * through a query param.
         */
        if (policyMap.containsKey(AerospikeAPIConstants.RECORD_EXISTS_ACTION)) {
            writePolicy.recordExistsAction = PolicyValueConverter.getRecordExistsAction(
                    policyMap.get(AerospikeAPIConstants.RECORD_EXISTS_ACTION));
        }
        return writePolicy;
    }
}