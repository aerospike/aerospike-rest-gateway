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
import com.aerospike.client.ScanCallback;
import com.aerospike.client.cluster.Node;
import com.aerospike.client.cluster.Partition;
import com.aerospike.client.policy.ScanPolicy;
import com.aerospike.client.query.PartitionFilter;
import com.aerospike.restclient.domain.RestClientKeyRecord;
import com.aerospike.restclient.domain.scanmodels.RestClientScanResponse;

import java.util.Base64;
import java.util.Objects;

public class ScanHandler {

    private final AerospikeClient client;

    private RestClientScanResponse result;

    private Key lastKey;

    private int currentPartition;

    public ScanHandler(AerospikeClient client) {
        this.client = client;
        this.result = new RestClientScanResponse();
    }

    public RestClientScanResponse scanPartition(ScanPolicy policy, String namespace, String setName,
                                                final long maxRecords, String fromToken, String[] binNames) {
        if (maxRecords == 0 && Objects.isNull(fromToken)) {
            client.scanAll(policy, namespace, setName, callback, binNames);
        } else {
            PartitionFilter filter = getPartitionFilter(namespace, setName, fromToken);
            while (isScanRequired(maxRecords)) {
                client.scanPartitions(policy, filter, namespace, setName, callback, binNames);
                policy.maxRecords = maxRecords > 0 ? maxRecords - result.getRecords().size() : maxRecords;
                filter = PartitionFilter.id(++currentPartition);
            }
        }
        setPaginationDetails(maxRecords);
        return result;
    }

    private boolean isScanRequired(final long maxRecords) {
        return (maxRecords == 0 || result.size() < maxRecords) && isValidPartition();
    }

    private boolean isValidPartition() {
        return currentPartition >= 0 && currentPartition < Node.PARTITIONS;
    }

    private PartitionFilter getPartitionFilter(String namespace, String setName, String fromToken) {
        if (fromToken == null) {
            return PartitionFilter.id(currentPartition);
        } else {
            Key lastKey = new Key(namespace, Base64.getDecoder().decode(fromToken), setName, null);
            currentPartition = Partition.getPartitionId(lastKey.digest);
            return PartitionFilter.after(lastKey);
        }
    }

    private void setPaginationDetails(final long maxRecords) {
        result.getPagination().setTotalRecords(result.size());
        if (result.size() == maxRecords && isValidPartition() && lastKey != null)
            result.getPagination().setNextToken(Base64.getEncoder().encodeToString(lastKey.digest));
    }

    private final ScanCallback callback = ((key, record) -> {
        lastKey = key;
        RestClientKeyRecord keyRecord = new RestClientKeyRecord(key, record);
        result.addRecord(keyRecord);
    });

    public static ScanHandler create(AerospikeClient client) {
        return new ScanHandler(client);
    }

}
