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
package com.aerospike.restclient.service;

import com.aerospike.client.Key;
import com.aerospike.client.policy.Policy;
import com.aerospike.client.policy.WritePolicy;
import com.aerospike.restclient.domain.auth.AuthDetails;
import com.aerospike.restclient.handlers.DocumentHandler;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.aerospike.restclient.util.AerospikeClientPool;
import com.aerospike.restclient.util.KeyBuilder;
import com.aerospike.restclient.util.RestClientErrors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
public class AerospikeDocumentServiceV1 implements AerospikeDocumentService {

    @Autowired
    private AerospikeClientPool clientPool;

    @Override
    public Map<String, Object> getObject(AuthDetails authDetails, String namespace, String set, String key,
                                         List<String> bins, String jsonPath,
                                         AerospikeAPIConstants.RecordKeyType keyType, Policy policy) {
        Key asKey = KeyBuilder.buildKey(namespace, set, key, keyType);
        return DocumentHandler.create(clientPool.getClient(authDetails))
                .getObject(asKey, bins, decodeJsonPath(jsonPath), policy);
    }

    @Override
    public void putObject(AuthDetails authDetails, String namespace, String set, String key, List<String> bins,
                          String jsonPath, Object jsonObject, AerospikeAPIConstants.RecordKeyType keyType,
                          WritePolicy policy) {
        Key asKey = KeyBuilder.buildKey(namespace, set, key, keyType);
        DocumentHandler.create(clientPool.getClient(authDetails))
                .putObject(asKey, bins, decodeJsonPath(jsonPath), jsonObject, policy);
    }

    @Override
    public void appendObject(AuthDetails authDetails, String namespace, String set, String key, List<String> bins,
                             String jsonPath, Object jsonObject, AerospikeAPIConstants.RecordKeyType keyType,
                             WritePolicy policy) {
        Key asKey = KeyBuilder.buildKey(namespace, set, key, keyType);
        DocumentHandler.create(clientPool.getClient(authDetails))
                .appendObject(asKey, bins, decodeJsonPath(jsonPath), jsonObject, policy);
    }

    @Override
    public void deleteObject(AuthDetails authDetails, String namespace, String set, String key, List<String> bins,
                             String jsonPath, AerospikeAPIConstants.RecordKeyType keyType, WritePolicy policy) {
        Key asKey = KeyBuilder.buildKey(namespace, set, key, keyType);
        DocumentHandler.create(clientPool.getClient(authDetails))
                .deleteObject(asKey, bins, decodeJsonPath(jsonPath), policy);
    }

    private String decodeJsonPath(String jsonPath) {
        try {
            return URLDecoder.decode(jsonPath, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            throw new RestClientErrors.InvalidOperationError("Invalid jsonPath parameter.");
        }
    }
}
