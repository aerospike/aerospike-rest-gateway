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

import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.client.policy.Policy;
import com.aerospike.client.policy.WritePolicy;
import com.aerospike.restclient.domain.RestClientRecord;
import com.aerospike.restclient.domain.auth.AuthDetails;
import com.aerospike.restclient.handlers.RecordHandler;
import com.aerospike.restclient.util.AerospikeAPIConstants.RecordKeyType;
import com.aerospike.restclient.util.AerospikeClientPool;
import com.aerospike.restclient.util.KeyBuilder;
import com.aerospike.restclient.util.RestClientErrors;
import com.aerospike.restclient.util.converters.BinConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AerospikeRecordServiceV1 implements AerospikeRecordService {

    @Autowired
    private AerospikeClientPool clientPool;

    @Autowired
    private CircuitBreakerFactory<?, ?> circuitBreakerFactory;

    @Override
    public RestClientRecord fetchRecord(
            AuthDetails authDetails, String namespace, String set, String key, String[] bins, RecordKeyType keyType,
            Policy policy
    ) {
        Record fetchedRecord;
        Key asKey = KeyBuilder.buildKey(namespace, set, key, keyType);
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("rest-client");

        if (bins != null && bins.length > 0) {
            fetchedRecord = circuitBreaker.run(
                    () -> RecordHandler.create(clientPool.getClient(authDetails)).getRecord(policy, asKey, bins));
        } else {
            fetchedRecord = circuitBreaker.run(
                    () -> RecordHandler.create(clientPool.getClient(authDetails)).getRecord(policy, asKey));
        }
        /* If the record doesn't exist, getRecord returns Null and does not raise an exception, we
         * want this to be a 404
         */
        if (fetchedRecord == null) {
            throw new RestClientErrors.RecordNotFoundError();
        }
        return new RestClientRecord(fetchedRecord);
    }

    @Override
    public void deleteRecord(
            AuthDetails authDetails, String namespace, String set, String key, RecordKeyType keyType, WritePolicy policy
    ) {
        Key asKey = KeyBuilder.buildKey(namespace, set, key, keyType);
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("rest-client");

        boolean recordExisted = circuitBreaker.run(
                () -> RecordHandler.create(clientPool.getClient(authDetails)).deleteRecord(policy, asKey));
        /* If the record doesn't exist, delete returns false and does not raise an exception, we
         * want this to be a 404
         */
        if (!recordExisted) {
            throw new RestClientErrors.RecordNotFoundError();
        }
    }

    @Override
    public void storeRecord(
            AuthDetails authDetails, String namespace, String set, String key, Map<String, Object> binMap,
            RecordKeyType keyType, WritePolicy policy
    ) {
        Key asKey = KeyBuilder.buildKey(namespace, set, key, keyType);
        Bin[] recordBins = BinConverter.binsFromMap(binMap);
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("rest-client");

        circuitBreaker.run(() -> {
            RecordHandler.create(clientPool.getClient(authDetails)).putRecord(policy, asKey, recordBins);
            return null;
        });
    }

    @Override
    public boolean recordExists(
            AuthDetails authDetails, String namespace, String set, String key, RecordKeyType keyType
    ) {
        Key asKey = KeyBuilder.buildKey(namespace, set, key, keyType);
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("rest-client");
        return circuitBreaker.run(
                () -> RecordHandler.create(clientPool.getClient(authDetails)).existsRecord(null, asKey));
    }
}
