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

import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.aerospike.restclient.util.RestClientErrors;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Optional;

@Schema(
        description = "An object that describes a batch delete operation to be used in a batch request.",
        externalDocs = @ExternalDocumentation(
                url = "https://javadoc.io/doc/com.aerospike/aerospike-client/" + AerospikeAPIConstants.AS_CLIENT_VERSION + "/com/aerospike/client/BatchDelete.html"
        )
)
public class BatchDelete extends BatchRecord {
    @Schema(
            description = "The type of batch request. It is always " + AerospikeAPIConstants.BATCH_TYPE_DELETE,
            allowableValues = {AerospikeAPIConstants.BATCH_TYPE_DELETE},
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    public final String type = AerospikeAPIConstants.BATCH_TYPE_DELETE;

    @Schema(description = "Policy attributes used for this batch delete operation.")
    public BatchDeletePolicy policy;

    public BatchDelete() {
    }

    @Override
    public com.aerospike.client.BatchDelete toBatchRecord() {
        if (key == null) {
            throw new RestClientErrors.InvalidKeyError("Key for a batch delete may not be null");
        }

        com.aerospike.client.policy.BatchDeletePolicy batchDeletePolicy = Optional.ofNullable(policy)
                .map(BatchDeletePolicy::toBatchDeletePolicy)
                .orElse(null);

        return new com.aerospike.client.BatchDelete(batchDeletePolicy, key.toKey());
    }
}

