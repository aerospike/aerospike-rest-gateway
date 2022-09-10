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
package com.aerospike.restclient.domain.batchmodels;

import com.aerospike.client.BatchRecord;
import com.aerospike.restclient.domain.RestClientKey;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "batchType")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = BatchReadRequest.class, name = AerospikeAPIConstants.BATCH_TYPE_READ),
                @JsonSubTypes.Type(
                        value = BatchWriteRequest.class,
                        name = AerospikeAPIConstants.BATCH_TYPE_WRITE
                ),
                @JsonSubTypes.Type(
                        value = BatchDeleteRequest.class,
                        name = AerospikeAPIConstants.BATCH_TYPE_DELETE
                ),
                @JsonSubTypes.Type(value = BatchUDFRequest.class, name = AerospikeAPIConstants.BATCH_TYPE_UDF),
        }
)
@Schema(description = "The batch operation base type.")
public abstract class BatchRecordRequest {
    @Schema(
            description = "The type of batch request.",
            allowableValues = {
                    AerospikeAPIConstants.BATCH_TYPE_READ,
                    AerospikeAPIConstants.BATCH_TYPE_WRITE,
                    AerospikeAPIConstants.BATCH_TYPE_DELETE,
                    AerospikeAPIConstants.BATCH_TYPE_UDF
            },
            required = true
    )
    public final String batchType = null;

    @Schema(description = "Key to a record.", required = true)
    @JsonProperty(required = true)
    public RestClientKey key;

    abstract public BatchRecord toBatchRecord();
}

