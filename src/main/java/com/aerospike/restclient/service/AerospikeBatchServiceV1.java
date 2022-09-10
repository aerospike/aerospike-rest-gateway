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
import com.aerospike.restclient.domain.auth.AuthDetails;
import com.aerospike.restclient.domain.batchmodels.BatchRecordRequest;
import com.aerospike.restclient.domain.batchmodels.BatchRecordResponse;
import com.aerospike.restclient.domain.batchmodels.BatchResponseBody;
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
    public BatchResponseBody batch(AuthDetails authDetails, List<BatchRecordRequest> batchKeys,
                                   BatchPolicy policy) {
        BatchResponseBody resp = new BatchResponseBody();
        List<BatchRecord> batchRecords = batchKeys.stream().map(BatchRecordRequest::toBatchRecord)
                .collect(Collectors.toList());
        BatchHandler.create(clientPool.getClient(authDetails)).batchRecord(policy, batchRecords);
        resp.batchRecords = batchRecords.stream().map(BatchRecordResponse::new).collect(Collectors.toList());
        return resp;
    }
}
