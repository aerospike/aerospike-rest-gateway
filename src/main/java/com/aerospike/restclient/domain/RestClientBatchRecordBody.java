/*
 * Copyright 2019 Aerospike, Inc.
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

import com.aerospike.client.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;

// TODO: Should this be an interface or a abstract class. RE: RestClientBatchRecordResponse is an abstract class but
// TODO: functions?
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes({
        @JsonSubTypes.Type(RestClientBatchReadBody.class),
        @JsonSubTypes.Type(RestClientBatchWriteBody.class),
        @JsonSubTypes.Type(RestClientBatchDeleteBody.class),
        @JsonSubTypes.Type(RestClientBatchUdfBody.class),
})
public abstract class RestClientBatchRecordBody {

    @Schema(description = "Key to write a record.", required = true)
    @JsonProperty(required = true)
    public RestClientKey key;
    abstract public BatchRecord toBatchRecord();
}
