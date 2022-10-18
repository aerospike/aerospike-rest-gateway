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

import com.aerospike.client.policy.ScanPolicy;
import com.aerospike.restclient.domain.auth.AuthDetails;
import com.aerospike.restclient.domain.scanmodels.RestClientScanResponse;
import com.aerospike.restclient.handlers.ScanHandler;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.aerospike.restclient.util.AerospikeClientPool;
import com.aerospike.restclient.util.converters.PolicyValueConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AerospikeScanServiceV1 implements AerospikeScanService {

    @Autowired
    private AerospikeClientPool clientPool;

    @Override
    public RestClientScanResponse scan(AuthDetails authDetails, String[] binNames, Map<String, String> requestParams,
                                       ScanPolicy policy, String namespace, String set) {
        String fromToken = null;
        if (requestParams.containsKey(AerospikeAPIConstants.FROM_TOKEN)) {
            fromToken = requestParams.get(AerospikeAPIConstants.FROM_TOKEN);
        }

        long maxRecords = 0;
        if (requestParams.containsKey(AerospikeAPIConstants.MAX_RECORDS)) {
            maxRecords = PolicyValueConverter.getLongValue(requestParams.get(AerospikeAPIConstants.MAX_RECORDS));
        }

        return ScanHandler.create(clientPool.getClient(authDetails))
                .scanPartition(checkSendKey(policy, requestParams), namespace, set, maxRecords, fromToken, binNames);
    }

    private ScanPolicy checkSendKey(ScanPolicy policy, Map<String, String> requestParams) {
        if (!requestParams.containsKey(AerospikeAPIConstants.SEND_KEY)) {
            policy.sendKey = true;
        }
        return policy;
    }
}

