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

import com.aerospike.client.Record;
import com.aerospike.client.*;
import com.aerospike.client.policy.BatchPolicy;
import com.aerospike.client.policy.WritePolicy;

import java.util.List;

public class OperateHandler {

    private final AerospikeClient client;

    public OperateHandler(AerospikeClient client) {
        this.client = client;
    }

    public Record operate(WritePolicy policy, Key key, Operation[] operations) {
        return client.operate(policy, key, operations);
    }

    public Record[] operate(BatchPolicy policy, Key[] key, Operation[] operations) {
        return client.get(policy, key, operations);
    }

    public List<BatchRecord> operate(BatchPolicy policy, List<BatchRecord> records) {
        client.operate(policy, records);
        return records;
    }

    public static OperateHandler create(AerospikeClient client) {
        return new OperateHandler(client);
    }
}
