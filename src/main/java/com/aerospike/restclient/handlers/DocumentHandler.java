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
import com.aerospike.client.Key;
import com.aerospike.client.policy.Policy;
import com.aerospike.client.policy.WritePolicy;
import com.aerospike.documentapi.AerospikeDocumentClient;
import com.aerospike.documentapi.policy.DocumentPolicy;
import com.aerospike.restclient.util.RestClientErrors;

import java.util.List;
import java.util.Map;

public class DocumentHandler {

    private final AerospikeClient client;

    public DocumentHandler(AerospikeClient client) {
        this.client = client;
    }

    public Map<String, Object> getObject(Key key, List<String> bins, String jsonPath, Policy policy) {
        try {

            AerospikeDocumentClient docClient = new AerospikeDocumentClient(
                    this.client, DocumentPolicy.builder().readPolicy(policy).build());
            return docClient.get(key, bins, jsonPath);
        } catch (Exception e) {
            throw new RestClientErrors.InvalidOperationError(e.getMessage());
        }
    }

    public void putObject(Key key, List<String> bins, String jsonPath, Object jsonObject, WritePolicy policy) {
        try {
            AerospikeDocumentClient docClient = new AerospikeDocumentClient(
                    this.client, DocumentPolicy.builder().writePolicy(policy).build());
            docClient.put(key, bins, jsonPath, jsonObject);
        } catch (Exception e) {
            throw new RestClientErrors.InvalidOperationError(e.getMessage());
        }
    }

    public void appendObject(Key key, List<String> bins, String jsonPath, Object jsonObject, WritePolicy policy) {
        try {
            AerospikeDocumentClient docClient = new AerospikeDocumentClient(
                    this.client, DocumentPolicy.builder().writePolicy(policy).build());
            docClient.append(key, bins, jsonPath, jsonObject);
        } catch (Exception e) {
            throw new RestClientErrors.InvalidOperationError(e.getMessage());
        }
    }

    public void deleteObject(Key key, List<String> bins, String jsonPath, WritePolicy policy) {
        try {
            AerospikeDocumentClient docClient = new AerospikeDocumentClient(
                    this.client, DocumentPolicy.builder().writePolicy(policy).build());
            docClient.delete(key, bins, jsonPath);
        } catch (Exception e) {
            throw new RestClientErrors.InvalidOperationError(e.getMessage());
        }
    }

    public static DocumentHandler create(AerospikeClient client) {
        return new DocumentHandler(client);
    }
}
