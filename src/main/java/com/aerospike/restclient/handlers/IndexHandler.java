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
import com.aerospike.client.policy.Policy;
import com.aerospike.client.query.IndexCollectionType;
import com.aerospike.client.query.IndexType;

public class IndexHandler {

    private final AerospikeClient client;

    public IndexHandler(AerospikeClient client) {
        this.client = client;
    }

    public void createIndex(Policy policy, String namespace, String setName, String indexName, String binName,
                            IndexType indexType) {
        client.createIndex(policy, namespace, setName, indexName, binName, indexType);
    }

    public void createIndex(Policy policy, String namespace, String setName, String indexName, String binName,
                            IndexType indexType, IndexCollectionType collectionType) {
        client.createIndex(policy, namespace, setName, indexName, binName, indexType, collectionType);
    }

    public void deleteIndex(Policy policy, String namespace, String setName, String indexName) {
        client.dropIndex(policy, namespace, setName, indexName);
    }

    public static IndexHandler create(AerospikeClient client) {
        return new IndexHandler(client);
    }

}
