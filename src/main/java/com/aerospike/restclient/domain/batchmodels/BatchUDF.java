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

import com.aerospike.client.Value;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.aerospike.restclient.util.RestClientErrors;
import com.aerospike.restclient.util.deserializers.ObjectDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Optional;

@Schema(
        description = "An object that describes a batch udf operation to be used in a batch request.",
        externalDocs = @ExternalDocumentation(
                url = "https://javadoc.io/doc/com.aerospike/aerospike-client/" + AerospikeAPIConstants.AS_CLIENT_VERSION + "/com/aerospike/client/BatchUDF.html"
        )
)
public class BatchUDF extends BatchRecord {

    @Schema(
            description = "List of bins to limit the record response to.",
            allowableValues = {AerospikeAPIConstants.BATCH_TYPE_UDF},
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty(required = true)
    public final String type = AerospikeAPIConstants.BATCH_TYPE_UDF;

    @Schema(description = "Package or lua module name.")
    @JsonProperty(required = true)
    public String packageName;

    @Schema(description = "Package or lua module name.")
    @JsonProperty(required = true)
    public String functionName;

    @Schema(description = "Optional arguments to lua function.")
    @JsonDeserialize(contentUsing = ObjectDeserializer.class)
    public List<Object> functionArgs;

    @Schema(description = "Policy attributes used for this batch udf operation.")
    public BatchUDFPolicy policy;

    public BatchUDF() {
    }

    @Override
    public com.aerospike.client.BatchUDF toBatchRecord() {
        if (key == null) {
            throw new RestClientErrors.InvalidKeyError("Key for a batch udf may not be null");
        }

        com.aerospike.client.policy.BatchUDFPolicy batchUDFPolicy = Optional.ofNullable(policy)
                .map(BatchUDFPolicy::toBatchUDFPolicy)
                .orElse(null);

        Value[] values = null;

        if (functionArgs != null) {
            values = functionArgs.stream().map(Value::get).toArray(Value[]::new);
        }

        return new com.aerospike.client.BatchUDF(batchUDFPolicy, key.toKey(), packageName, functionName, values);
    }
}
