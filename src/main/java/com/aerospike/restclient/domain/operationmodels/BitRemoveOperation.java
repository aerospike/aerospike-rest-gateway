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

import com.aerospike.client.operation.BitPolicy;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Server removes bytes from byte[] bin at byteOffset for byteSize. Server does not return a value.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/" + AerospikeAPIConstants.AS_CLIENT_VERSION + "/com/aerospike/client/operation/BitOperation.html")
)
public class BitRemoveOperation extends BitOperation {
    @Schema(
            description = "The type of operation. It is always " + OperationTypes.BIT_REMOVE,
            requiredMode = Schema.RequiredMode.REQUIRED,
            allowableValues = {OperationTypes.BIT_REMOVE}
    )
    public final String type = OperationTypes.BIT_REMOVE;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private final int byteOffset;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private final int byteSize;

    @JsonCreator
    public BitRemoveOperation(
            @JsonProperty(value = "binName")
            @Schema(name = "binName", requiredMode = Schema.RequiredMode.REQUIRED) String binName,
            @JsonProperty(value = "byteOffset")
            @Schema(name = "byteOffset", requiredMode = Schema.RequiredMode.REQUIRED) int byteOffset,
            @JsonProperty(value = "byteSize")
            @Schema(name = "byteSize", requiredMode = Schema.RequiredMode.REQUIRED) int byteSize
    ) {
        super(binName);
        this.byteOffset = byteOffset;
        this.byteSize = byteSize;
    }

    @Override
    public com.aerospike.client.Operation toOperation() {
        return com.aerospike.client.operation.BitOperation.remove(BitPolicy.Default, binName, byteOffset, byteSize);
    }
}
