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
import com.aerospike.client.policy.QueryPolicy;
import com.aerospike.client.query.PartitionFilter;
import com.aerospike.client.query.PartitionStatus;
import com.aerospike.client.query.QueryListener;
import com.aerospike.client.query.Statement;
import com.aerospike.restclient.domain.RestClientKeyRecord;
import com.aerospike.restclient.domain.querymodels.QueryResponseBody;
import com.aerospike.restclient.domain.scanmodels.Pagination;
import com.aerospike.restclient.util.RestClientErrors;
import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;

import java.io.IOException;
import java.util.Base64;

public class QueryHandler {

    private final AerospikeClient client;

    private QueryResponseBody response;

    private static final int PARTITION_STATE_NONE = 0;
    private static final int PARTITION_STATE_NOT_STARTED = 1;
    private static final int PARTITION_STATE_STARTED = 2;
    private static final int PARTITION_STATE_FINISHED = 3;

    public QueryHandler(AerospikeClient client) {
        this.client = client;
        this.response = new QueryResponseBody();
    }

    public QueryResponseBody queryPartition(QueryPolicy policy, Statement stmt, String fromToken, Boolean getToken,
                                            int beginPart, int partCount) throws IOException {
        PartitionFilter partitionFilter;
        partitionFilter = getPartitionFilter(fromToken, beginPart, partCount);
        return queryPartition(policy, stmt, partitionFilter, getToken);
    }

    public QueryResponseBody queryPartition(QueryPolicy policy, Statement stmt, String fromToken, Boolean getToken) {
        PartitionFilter partitionFilter;
        partitionFilter = getPartitionFilter(fromToken);
        return queryPartition(policy, stmt, partitionFilter, getToken);
    }

    public QueryResponseBody queryPartition(QueryPolicy policy, Statement stmt, PartitionFilter partitionFilter,
                                            Boolean getToken) {
        client.query(policy, stmt, partitionFilter, callback);

        Pagination page = response.getPagination();
        page.setTotalRecords(response.size());

        if (getToken && !partitionFilter.isDone()) {
            try {
                byte[] token = encodePartitionFilter(partitionFilter);
                String tokenStr = Base64.getEncoder().encodeToString(token);
                page.setNextToken(tokenStr);
            } catch (IOException e) {
                throw new RestClientErrors.AerospikeRestClientError(
                        String.format("Unable to encode partition query filter to token: %s", e.getMessage()));
            }
        }

        return response;
    }

    private PartitionFilter getPartitionFilter(String fromToken) {
        if (fromToken == null) {
            return PartitionFilter.all();
        } else {
            byte[] tokenOut = Base64.getDecoder().decode(fromToken);

            try {
                return decodePartitionFilter(tokenOut);
            } catch (IOException e) {
                throw new RestClientErrors.AerospikeRestClientError(
                        String.format("Unable to decode from token: %s", e.getMessage()));
            }
        }
    }

    private PartitionFilter getPartitionFilter(String fromToken, int begin, int count) throws IOException {
        if (fromToken == null) {
            return PartitionFilter.range(begin, count);
        } else {
            byte[] tokenOut = Base64.getDecoder().decode(fromToken);
            return decodePartitionFilter(tokenOut);
        }
    }

    private byte[] encodePartitionFilter(PartitionFilter partitionFilter) throws IOException {
        MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();
        int begin = partitionFilter.getBegin();
        int count = partitionFilter.getCount();

        if (count == 0) {
            return new byte[]{};
        }

        packer.packInt(begin);
        packer.packInt(count);

        for (PartitionStatus part : partitionFilter.getPartitions()) {
            int status;
            byte[] digest = part.digest;
            long bval = part.bval;

            if (part.retry) {
                if (digest == null) {
                    status = PARTITION_STATE_NOT_STARTED;
                } else {
                    status = PARTITION_STATE_STARTED;
                }
            } else {
                status = PARTITION_STATE_FINISHED;
            }

            packer.packInt(status);

            if (status == PARTITION_STATE_STARTED) {
                packer.packBinaryHeader(digest.length).writePayload(digest);
                packer.packLong(bval);
            }
        }

        packer.close();
        return packer.toByteArray();
    }

    private PartitionFilter decodePartitionFilter(byte[] token) throws IOException {
        if (token.length == 0) {
            return PartitionFilter.all();
        }

        MessageUnpacker unpacker = MessagePack.newDefaultUnpacker(token);

        int begin = unpacker.unpackInt();
        int count = unpacker.unpackInt();

        PartitionFilter partitionFilter = PartitionFilter.range(begin, count);
        PartitionStatus[] partitionStatuses = new PartitionStatus[count];

        for (int i = 0; i < count; i++) {
            int status = unpacker.unpackInt();
            PartitionStatus part = new PartitionStatus(i + begin);

            if (status == PARTITION_STATE_STARTED) {
                int digestLen = unpacker.unpackBinaryHeader(); // Investigate removing this since we know it is 20 bytes
                part.digest = unpacker.readPayload(digestLen);
                part.bval = unpacker.unpackLong();
            }

            if (status == PARTITION_STATE_NONE || status == PARTITION_STATE_FINISHED) {
                part.retry = false;
            }

            partitionStatuses[i] = part;
        }

        unpacker.close();
        partitionFilter.setPartitions(partitionStatuses);
        return partitionFilter;
    }

    private final QueryListener callback = ((key, record) -> {
        RestClientKeyRecord keyRecord = new RestClientKeyRecord(key, record);
        response.addRecord(keyRecord);
    });

    public static QueryHandler create(AerospikeClient client) {
        return new QueryHandler(client);
    }

}
