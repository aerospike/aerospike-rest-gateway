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
package com.aerospike.restclient.handlers;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.BatchRead;
import com.aerospike.client.BatchRecord;
import com.aerospike.client.policy.BatchPolicy;

import java.util.List;

public class BatchHandler {

    private final AerospikeClient client;

    public BatchHandler(AerospikeClient client) {
        this.client = client;
    }

    public void batchRecord(BatchPolicy policy, List<BatchRecord> batchRecords) {
        boolean allBatchRead = batchRecords.stream().allMatch(r -> r instanceof BatchRead);

        if (allBatchRead) {
            // Supported on old servers
            client.get(policy, batchRecords.stream().map(r -> (BatchRead) r).toList());
        } else {
            client.operate(policy, batchRecords);
        }
    }

    public static BatchHandler create(AerospikeClient client) {
        return new BatchHandler(client);
    }
}
