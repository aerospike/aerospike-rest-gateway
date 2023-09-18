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

import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Server returns integer from byte[] bin starting at bitOffset for bitSize. Signed indicates if bits should be treated as a signed number.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/" + AerospikeAPIConstants.AS_CLIENT_VERSION + "/com/aerospike/client/operation/BitOperation.html")
)
public class BitGetIntOperation extends BitOperation {
    @Schema(
            description = "The type of operation. It is always " + OperationTypes.BIT_GET_INT,
            requiredMode = Schema.RequiredMode.REQUIRED,
            allowableValues = {OperationTypes.BIT_GET_INT}
    )
    public final String type = OperationTypes.BIT_GET_INT;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private final int bitOffset;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private final int bitSize;

    private final boolean signed;

    @JsonCreator
    public BitGetIntOperation(
            @JsonProperty(value = "binName")
            @Schema(name = "binName", requiredMode = Schema.RequiredMode.REQUIRED) String binName,
            @JsonProperty(value = "bitOffset")
            @Schema(name = "bitOffset", requiredMode = Schema.RequiredMode.REQUIRED) int bitOffset,
            @JsonProperty(value = "bitSize")
            @Schema(name = "bitSize", requiredMode = Schema.RequiredMode.REQUIRED) int bitSize,
            @JsonProperty(value = "signed", defaultValue = "false") boolean signed
    ) {
        super(binName);
        this.bitOffset = bitOffset;
        this.bitSize = bitSize;
        this.signed = signed;
    }

    @Override
    public com.aerospike.client.Operation toOperation() {
        return com.aerospike.client.operation.BitOperation.getInt(binName, bitOffset, bitSize, signed);
    }
}
