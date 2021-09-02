/*
 * Copyright 2020 Aerospike, Inc.
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
import com.aerospike.client.Operation;
import com.aerospike.client.Record;
import com.aerospike.client.policy.BatchPolicy;
import com.aerospike.client.policy.WritePolicy;
import com.aerospike.restclient.domain.RestClientOperation;
import com.aerospike.restclient.domain.RestClientRecord;
import com.aerospike.restclient.domain.auth.AuthDetails;
import com.aerospike.restclient.handlers.OperateHandler;
import com.aerospike.restclient.util.AerospikeAPIConstants.RecordKeyType;
import com.aerospike.restclient.util.AerospikeClientPool;
import com.aerospike.restclient.util.KeyBuilder;
import com.aerospike.restclient.util.RestClientErrors;
import com.aerospike.restclient.util.converters.OperationsConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreaker;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AerospikeOperateServiceV1 implements AerospikeOperateService {

    @Autowired
    private AerospikeClientPool clientPool;

    @Autowired
    private Resilience4JCircuitBreaker circuitBreaker;

    @Override
    public RestClientRecord operate(AuthDetails authDetails, String namespace, String set, String key,
                                    List<RestClientOperation> opsList, RecordKeyType keyType, WritePolicy policy) {

        List<Map<String, Object>> opsMapsList = opsList.stream().map(RestClientOperation::toMap)
                .collect(Collectors.toList());

        Operation[] operations = OperationsConverter.mapListToOperationsArray(opsMapsList);
        Key opKey = KeyBuilder.buildKey(namespace, set, key, keyType);
        Record fetchedRecord = circuitBreaker.run(() -> OperateHandler.create(clientPool.getClient(authDetails))
                .operate(policy, opKey, operations));
        if (fetchedRecord == null) {
            throw new RestClientErrors.RecordNotFoundError();
        }

        return new RestClientRecord(fetchedRecord);
    }

    @Override
    public RestClientRecord[] operate(AuthDetails authDetails, String namespace, String set, String[] keys,
                                      List<RestClientOperation> opsList, RecordKeyType keyType, BatchPolicy policy) {

        List<Map<String, Object>> opsMapsList = opsList.stream().map(RestClientOperation::toMap)
                .collect(Collectors.toList());

        Operation[] operations = OperationsConverter.mapListToOperationsArray(opsMapsList);
        Key[] opKeys = Arrays.stream(keys).map(k -> KeyBuilder.buildKey(namespace, set, k, keyType)).toArray(Key[]::new);
        Record[] fetchedRecords = circuitBreaker.run(() -> OperateHandler.create(clientPool.getClient(authDetails))
                .operate(policy, opKeys, operations));

        return Arrays.stream(fetchedRecords).map(RestClientRecord::new).toArray(RestClientRecord[]::new);
    }
}
