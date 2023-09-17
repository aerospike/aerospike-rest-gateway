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
package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.operation.HLLPolicy;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Server creates a new HLL or resets an existing HLL. Server does not return a value.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/" + AerospikeAPIConstants.AS_CLIENT_VERSION + "/com/aerospike/client/operation/HLLOperation.html")
)
public class HLLInitOperation extends HLLOperation {
    @Schema(
            description = "The type of operation. It is always " + OperationTypes.HLL_INIT,
            requiredMode = Schema.RequiredMode.REQUIRED,
            allowableValues = {OperationTypes.HLL_INIT}
    )
    public final String type = OperationTypes.HLL_INIT;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private final int indexBitCount;

    private Integer minHashBitCount;

    @JsonCreator
    public HLLInitOperation(
            @JsonProperty(value = "binName")
            @Schema(name = "binName", requiredMode = Schema.RequiredMode.REQUIRED) String binName,
            @Schema(name = "indexBitCount", requiredMode = Schema.RequiredMode.REQUIRED) int indexBitCount
    ) {
        super(binName);
        this.indexBitCount = indexBitCount;
    }

    public Integer getMinHashBitCount() {
        return minHashBitCount;
    }

    public void setMinHashBitCount(Integer minHashBitCount) {
        this.minHashBitCount = minHashBitCount;
    }

    @Override
    public com.aerospike.client.Operation toOperation() {
        if (minHashBitCount == null) {
            return com.aerospike.client.operation.HLLOperation.init(HLLPolicy.Default, binName, indexBitCount);
        }
        return com.aerospike.client.operation.HLLOperation.init(HLLPolicy.Default, binName, indexBitCount,
                minHashBitCount);
    }
}
