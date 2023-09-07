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
        description = "Server returns indexBitCount and minHashBitCount used to create HLL bin in a list of longs. The list size is 2.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/" + AerospikeAPIConstants.AS_CLIENT_VERSION + "/com/aerospike/client/operation/HLLOperation.html"),
        hidden = true
)
public class HLLDescribeOperation extends HLLOperation {
    @Schema(
            description = "The type of operation. It is always " + OperationTypes.HLL_DESCRIBE,
            required = true,
            allowableValues = {OperationTypes.HLL_DESCRIBE}
    )
    final public String type = OperationTypes.HLL_DESCRIBE;

    @JsonCreator
    public HLLDescribeOperation(@JsonProperty(value = "binName") @Schema(
            name = "binName", requiredMode = Schema.RequiredMode.REQUIRED
    ) String binName) {
        super(binName);
    }

    @Override
    public com.aerospike.client.Operation toOperation() {
        return com.aerospike.client.operation.HLLOperation.describe(binName);
    }
}
