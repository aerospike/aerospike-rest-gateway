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

import com.aerospike.client.BatchRead;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.aerospike.restclient.util.RestClientErrors;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public class RestClientBatchReadBody extends RestClientBatchRecordBody {
    @Schema(description = "Whether all bins should be returned with this record.")
    public boolean readAllBins;

    @Schema(description = "List of bins to limit the record response to.", example = "[\"bin1\"]")
    public String[] binNames;

    @Schema(description = "Policy attributes used for this batch read operation.")
    public RestClientBatchReadPolicy policy;

    @Schema(description = "TODO", allowableValues = AerospikeAPIConstants.BATCH_TYPE_READ, required = true)
    @JsonProperty(required = true)
    public final String batchType = AerospikeAPIConstants.BATCH_TYPE_READ;

    public RestClientBatchReadBody() {
    }

    public BatchRead toBatchRecord() {
        if (key == null) {
            throw new RestClientErrors.InvalidKeyError("Key for a batch read may not be null");
        }

        if (readAllBins) {
            if (policy == null) {
                return new BatchRead(key.toKey(), true);
            }

            return new BatchRead(policy.toBatchReadPolicy(), key.toKey(), true);
        } else {
            if (policy == null) {
                return new BatchRead(key.toKey(), binNames);
            }

            return new BatchRead(policy.toBatchReadPolicy(), key.toKey(), binNames);
        }
    }
}
