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
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.client.policy.Policy;
import com.aerospike.client.policy.WritePolicy;

public class RecordHandler {

    private final AerospikeClient client;

    public RecordHandler(AerospikeClient client) {
        this.client = client;
    }

    public boolean deleteRecord(WritePolicy policy, Key key) {
        return client.delete(policy, key);
    }

    public Record getRecord(Policy policy, Key key, String[] bins) {
        return client.get(policy, key, bins);
    }

    public Record getRecord(Policy policy, Key key) {
        return client.get(policy, key);
    }

    public void putRecord(WritePolicy policy, Key key, Bin... bins) {
        client.put(policy, key, bins);
    }

    public boolean existsRecord(Policy policy, Key key) {
        return client.exists(policy, key);
    }

    public static RecordHandler create(AerospikeClient client) {
        return new RecordHandler(client);
    }
}
