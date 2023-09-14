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

import com.aerospike.restclient.domain.operationmodels.Operation;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.aerospike.restclient.util.RestClientErrors;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Optional;

@Schema(
        description = "An object that describes a batch write operation to be used in a batch request.",
        externalDocs = @ExternalDocumentation(
                url = "https://javadoc.io/doc/com.aerospike/aerospike-client/" + AerospikeAPIConstants.AS_CLIENT_VERSION + "/com/aerospike/client/BatchWrite.html"
        )
)
public class BatchWrite extends BatchRecord {

    @Schema(
            description = "List of bins to limit the record response to.",
            allowableValues = {AerospikeAPIConstants.BATCH_TYPE_WRITE},
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty(required = true)
    public final String type = AerospikeAPIConstants.BATCH_TYPE_WRITE;

    @Schema(description = "List of operation.")
    public List<Operation> opsList;

    @Schema(description = "Policy attributes used for this batch write operation.")
    public BatchWritePolicy policy;

    public BatchWrite() {
    }

    @Override
    public com.aerospike.client.BatchWrite toBatchRecord() {
        if (key == null) {
            throw new RestClientErrors.InvalidKeyError("Key for a batch write may not be null");
        }

        com.aerospike.client.policy.BatchWritePolicy batchWritePolicy = Optional.ofNullable(policy)
                .map(BatchWritePolicy::toBatchWritePolicy)
                .orElse(null);

        com.aerospike.client.Operation[] operations = opsList.stream()
                .map(com.aerospike.restclient.domain.operationmodels.Operation::toOperation)
                .toArray(com.aerospike.client.Operation[]::new);

        return new com.aerospike.client.BatchWrite(batchWritePolicy, key.toKey(), operations);
    }
}
