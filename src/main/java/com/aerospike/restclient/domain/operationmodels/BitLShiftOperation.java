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
        description = "Server shifts left byte[] bin starting at bitOffset for bitSize. Server does not return a value.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/" + AerospikeAPIConstants.AS_CLIENT_VERSION + "/com/aerospike/client/operation/BitOperation.html")
)
public class BitLShiftOperation extends BitOperation {
    @Schema(
            description = "The type of operation. It is always " + OperationTypes.BIT_LSHIFT,
            required = true,
            allowableValues = OperationTypes.BIT_LSHIFT
    )
    final public static String type = OperationTypes.BIT_LSHIFT;

    @Schema(required = true)
    private final int bitOffset;

    @Schema(required = true)
    private final int bitSize;

    @Schema(required = true)
    private final int shift;

    @JsonCreator
    public BitLShiftOperation(@JsonProperty(value = "binName", required = true) String binName,
                              @JsonProperty(value = "bitOffset", required = true) int bitOffset,
                              @JsonProperty(value = "bitSize", required = true) int bitSize,
                              @JsonProperty(value = "shift", required = true) int shift) {
        super(binName);
        this.bitOffset = bitOffset;
        this.bitSize = bitSize;
        this.shift = shift;
    }

    @Override
    public com.aerospike.client.Operation toOperation() {
        return com.aerospike.client.operation.BitOperation.lshift(BitPolicy.Default, binName, bitOffset, bitSize,
                shift);
    }
}
