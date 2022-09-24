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

import com.aerospike.client.Operation;
import com.aerospike.restclient.domain.RestClientOperation;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.aerospike.restclient.util.RestClientErrors;
import com.aerospike.restclient.util.converters.OperationsConverter;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Schema(
        description = "An object that describes a batch read operation to be used in a batch request.",
        externalDocs = @ExternalDocumentation(
                url =
                        "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/BatchRead.html"
        )
)
public class BatchRead extends BatchRecord {
    // TODO add operations.  It is needed for CTX get operation I believe.
    @Schema(
            description = "The type of batch request. It is always " + AerospikeAPIConstants.BATCH_TYPE_READ,
            allowableValues = AerospikeAPIConstants.BATCH_TYPE_READ,
            required = true
    )
    public final String type = AerospikeAPIConstants.BATCH_TYPE_READ;

    @Schema(description = "Whether all bins should be returned with this record.")
    public boolean readAllBins;

    @Schema(description = "List of operation. Useful for reading from nested CDTs.")
    public List<RestClientOperation> opsList;

    @Schema(description = "List of bins to limit the record response to.", example = "[\"bin1\"]")
    public String[] binNames;

    @Schema(description = "Policy attributes used for this batch read operation.")
    public BatchReadPolicy policy;


    public BatchRead() {
    }

    @Override
    public com.aerospike.client.BatchRead toBatchRecord() {
        if (key == null) {
            throw new RestClientErrors.InvalidKeyError("Key for a batch read may not be null");
        }

        com.aerospike.client.policy.BatchReadPolicy batchReadPolicy = Optional.ofNullable(policy).map(
                BatchReadPolicy::toBatchReadPolicy).orElse(null);

        if (readAllBins) {
            return new com.aerospike.client.BatchRead(batchReadPolicy, key.toKey(), true);
        } else if (opsList != null) {
            List<Map<String, Object>> opsMapsList = opsList.stream().map(RestClientOperation::toMap)
                    .collect(Collectors.toList());
            Operation[] operations = OperationsConverter.mapListToOperationsArray(opsMapsList);

            return new com.aerospike.client.BatchRead(
                    batchReadPolicy, key.toKey(),
                    operations);
        } else {
            return new com.aerospike.client.BatchRead(
                    batchReadPolicy, key.toKey(),
                    binNames);
        }
    }
}