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
import com.aerospike.client.Operation;
import com.aerospike.client.policy.Policy;
import com.aerospike.client.policy.WritePolicy;
import com.aerospike.client.query.Statement;
import com.aerospike.client.task.ExecuteTask;
import com.aerospike.restclient.domain.executemodels.RestClientExecuteTask;
import com.aerospike.restclient.domain.executemodels.RestClientExecuteTaskStatus;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.aerospike.restclient.util.converters.PolicyValueConverter;

import java.util.Map;

public class ExecuteHandler {

    private final AerospikeClient client;

    public ExecuteHandler(AerospikeClient client) {
        this.client = client;
    }

    public RestClientExecuteTask executeScan(String namespace, String set, Operation[] opsList, WritePolicy policy,
                                             Map<String, String> requestParams) {
        Statement stmt = new Statement();
        stmt.setNamespace(namespace);
        stmt.setSetName(set);

        if (requestParams.containsKey(AerospikeAPIConstants.RECORD_BINS)) {
            String[] binNames = requestParams.get(AerospikeAPIConstants.RECORD_BINS).split(",");
            stmt.setBinNames(binNames);
        }
        if (requestParams.containsKey(AerospikeAPIConstants.RECORDS_PER_SECOND)) {
            int recordsPerSecond = PolicyValueConverter.getIntValue(
                    requestParams.get(AerospikeAPIConstants.RECORDS_PER_SECOND));
            stmt.setRecordsPerSecond(recordsPerSecond);
        }

        ExecuteTask task = client.execute(policy, stmt, opsList);

        return new RestClientExecuteTask(task.getTaskId(), true);
    }

    public RestClientExecuteTaskStatus queryScanStatus(String taskId) {
        long id = PolicyValueConverter.getLongValue(taskId);

        Statement statement = new Statement();
        statement.setTaskId(id);

        ExecuteTask task = new ExecuteTask(client.getCluster(), new Policy(), statement, id);
        int status = task.queryStatus();

        return new RestClientExecuteTaskStatus(new RestClientExecuteTask(id, true), status);
    }

    public static ExecuteHandler create(AerospikeClient client) {
        return new ExecuteHandler(client);
    }
}
