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
package com.aerospike.restclient.domain.batchmodels;

import com.aerospike.restclient.domain.RestClientKey;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = BatchRead.class, name = AerospikeAPIConstants.BATCH_TYPE_READ),
        @JsonSubTypes.Type(value = BatchWrite.class, name = AerospikeAPIConstants.BATCH_TYPE_WRITE),
        @JsonSubTypes.Type(value = BatchDelete.class, name = AerospikeAPIConstants.BATCH_TYPE_DELETE),
        @JsonSubTypes.Type(value = BatchUDF.class, name = AerospikeAPIConstants.BATCH_TYPE_UDF),
})
@Schema(
        description = "The batch operation base type.",
        oneOf = {BatchRead.class, BatchWrite.class, BatchDelete.class, BatchUDF.class}
)
public abstract class BatchRecord {
    @Schema(description = "Key to a record.", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty(required = true)
    public RestClientKey key;

    public abstract com.aerospike.client.BatchRecord toBatchRecord();
}
