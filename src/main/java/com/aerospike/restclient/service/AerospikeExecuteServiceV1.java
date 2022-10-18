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

import com.aerospike.client.policy.WritePolicy;
import com.aerospike.restclient.domain.RestClientOperation;
import com.aerospike.restclient.domain.auth.AuthDetails;
import com.aerospike.restclient.domain.executemodels.RestClientExecuteTask;
import com.aerospike.restclient.domain.executemodels.RestClientExecuteTaskStatus;
import com.aerospike.restclient.domain.operationmodels.Operation;
import com.aerospike.restclient.handlers.ExecuteHandler;
import com.aerospike.restclient.util.AerospikeClientPool;
import com.aerospike.restclient.util.converters.OperationsConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AerospikeExecuteServiceV1 implements AerospikeExecuteService {

    @Autowired
    private AerospikeClientPool clientPool;

    @Deprecated
    @Override
    public RestClientExecuteTask executeV1Scan(AuthDetails authDetails, String namespace, String set,
                                               List<RestClientOperation> opsList, WritePolicy policy,
                                               Map<String, String> requestParams) {
        List<Map<String, Object>> opsMapsList = opsList.stream().map(RestClientOperation::toMap).toList();
        com.aerospike.client.Operation[] operations = OperationsConverter.mapListToOperationsArray(opsMapsList);

        return ExecuteHandler.create(clientPool.getClient(authDetails))
                .executeScan(namespace, set, operations, policy, requestParams);
    }

    @Override
    public RestClientExecuteTask executeV2Scan(AuthDetails authDetails, String namespace, String set,
                                               List<Operation> opsList, WritePolicy policy,
                                               Map<String, String> requestParams) {
        com.aerospike.client.Operation[] operations = opsList.stream()
                .map(Operation::toOperation)
                .toArray(com.aerospike.client.Operation[]::new);
        return ExecuteHandler.create(clientPool.getClient(authDetails))
                .executeScan(namespace, set, operations, policy, requestParams);
    }

    @Override
    public RestClientExecuteTaskStatus queryScanStatus(AuthDetails authDetails, String taskId) {
        return ExecuteHandler.create(clientPool.getClient(authDetails)).queryScanStatus(taskId);
    }
}
