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

import com.aerospike.client.BatchRecord;
import com.aerospike.client.policy.BatchPolicy;
import com.aerospike.restclient.domain.RestClientBatchRecordBody;
import com.aerospike.restclient.domain.RestClientBatchRecordResponse;
import com.aerospike.restclient.domain.auth.AuthDetails;
import com.aerospike.restclient.handlers.BatchHandler;
import com.aerospike.restclient.util.AerospikeClientPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AerospikeBatchServiceV1 implements AerospikeBatchService {

    @Autowired
    private AerospikeClientPool clientPool;

    @Override
    public List<RestClientBatchRecordResponse> batch(AuthDetails authDetails, List<RestClientBatchRecordBody> batchKeys,
                                                     BatchPolicy policy) {
        List<BatchRecord> batchRecords = batchKeys.stream().map(RestClientBatchRecordBody::toBatchRecord)
                .collect(Collectors.toList());
        BatchHandler.create(clientPool.getClient(authDetails)).batchRecord(policy, batchRecords);

        return batchRecords.stream().map(RestClientBatchRecordResponse::new).collect(Collectors.toList());
    }
}
