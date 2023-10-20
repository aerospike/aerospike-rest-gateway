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
import com.aerospike.client.Record;
import com.aerospike.client.policy.BatchPolicy;
import com.aerospike.client.policy.WritePolicy;
import com.aerospike.restclient.domain.RestClientOperation;
import com.aerospike.restclient.domain.RestClientRecord;
import com.aerospike.restclient.domain.auth.AuthDetails;
import com.aerospike.restclient.domain.operationmodels.OperateResponseRecordBody;
import com.aerospike.restclient.domain.operationmodels.OperateResponseRecordsBody;
import com.aerospike.restclient.domain.operationmodels.Operation;
import com.aerospike.restclient.handlers.OperateHandler;
import com.aerospike.restclient.util.AerospikeAPIConstants.RecordKeyType;
import com.aerospike.restclient.util.AerospikeClientPool;
import com.aerospike.restclient.util.KeyBuilder;
import com.aerospike.restclient.util.RestClientErrors;
import com.aerospike.restclient.util.converters.OperationsConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class AerospikeOperateServiceV1 implements AerospikeOperateService {

    @Autowired
    private AerospikeClientPool clientPool;

    @Autowired
    private CircuitBreakerFactory<?, ?> circuitBreakerFactory;

    @Deprecated
    @Override
    public RestClientRecord operateV1(
            AuthDetails authDetails, String namespace, String set, String key, List<RestClientOperation> opsList,
            RecordKeyType keyType, WritePolicy policy
    ) {
        List<Map<String, Object>> opsMapsList = opsList.stream().map(RestClientOperation::toMap).toList();
        com.aerospike.client.Operation[] operations = OperationsConverter.mapListToOperationsArray(opsMapsList);
        return operate(authDetails, namespace, set, key, operations, keyType, policy);
    }

    @Deprecated
    @Override
    public RestClientRecord[] operateV1(
            AuthDetails authDetails, String namespace, String set, String[] keys, List<RestClientOperation> opsList,
            RecordKeyType keyType, BatchPolicy policy
    ) {
        List<Map<String, Object>> opsMapsList = opsList.stream().map(RestClientOperation::toMap).toList();
        com.aerospike.client.Operation[] operations = OperationsConverter.mapListToOperationsArray(opsMapsList);
        return operate(authDetails, namespace, set, keys, operations, keyType, policy);
    }

    @Override
    public OperateResponseRecordBody operateV2(
            AuthDetails authDetails, String namespace, String set, String key, List<Operation> opsList,
            RecordKeyType keyType, WritePolicy policy
    ) {
        com.aerospike.client.Operation[] operations = opsList.stream()
                .map(Operation::toOperation)
                .toArray(com.aerospike.client.Operation[]::new);
        return new OperateResponseRecordBody(operate(authDetails, namespace, set, key, operations, keyType, policy));
    }

    @Override
    public OperateResponseRecordsBody operateV2(
            AuthDetails authDetails, String namespace, String set, String[] keys, List<Operation> opsList,
            RecordKeyType keyType, BatchPolicy policy
    ) {
        com.aerospike.client.Operation[] operations = opsList.stream()
                .map(Operation::toOperation)
                .toArray(com.aerospike.client.Operation[]::new);
        return new OperateResponseRecordsBody(operate(authDetails, namespace, set, keys, operations, keyType, policy));
    }

    public RestClientRecord operate(
            AuthDetails authDetails, String namespace, String set, String key, com.aerospike.client.Operation[] opsList,
            RecordKeyType keyType, WritePolicy policy
    ) {
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("rest-client");

        Key opKey = KeyBuilder.buildKey(namespace, set, key, keyType);
        Record fetchedRecord = circuitBreaker.run(
                () -> OperateHandler.create(clientPool.getClient(authDetails)).operate(policy, opKey, opsList));
        if (fetchedRecord == null) {
            throw new RestClientErrors.RecordNotFoundError();
        }

        return new RestClientRecord(fetchedRecord);
    }

    public RestClientRecord[] operate(
            AuthDetails authDetails, String namespace, String set, String[] keys,
            com.aerospike.client.Operation[] opsList, RecordKeyType keyType, BatchPolicy policy
    ) {
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("rest-client");

        Key[] opKeys = Arrays.stream(keys)
                .map(k -> KeyBuilder.buildKey(namespace, set, k, keyType))
                .toArray(Key[]::new);
        Record[] fetchedRecords = circuitBreaker.run(
                () -> OperateHandler.create(clientPool.getClient(authDetails)).operate(policy, opKeys, opsList));

        return Arrays.stream(fetchedRecords).map(RestClientRecord::new).toArray(RestClientRecord[]::new);
    }
}
