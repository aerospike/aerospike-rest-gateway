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
package com.aerospike.restclient.domain;

import com.aerospike.client.task.Task;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "RestClientExecuteTaskStatus")
public class RestClientExecuteTaskStatus {

    @ApiModelProperty(value = "The ExecuteTask object.")
    private RestClientExecuteTask task;

    @ApiModelProperty(value = "The ExecuteTask status.")
    private String status;

    public RestClientExecuteTaskStatus(RestClientExecuteTask task, int statusCode) {
        this.task = task;
        this.status = translateStatusCode(statusCode);
    }

    public RestClientExecuteTaskStatus() {
        this(new RestClientExecuteTask(), -1);
    }

    private String translateStatusCode(int statusCode) {
        switch (statusCode) {
            case Task.IN_PROGRESS:
                return "IN_PROGRESS";
            case Task.COMPLETE:
                return "COMPLETE";
            case Task.NOT_FOUND:
                return "NOT_FOUND";
            default:
                return "UNKNOWN";
        }
    }

    public RestClientExecuteTask getTask() {
        return task;
    }

    public String getStatus() {
        return status;
    }
}
