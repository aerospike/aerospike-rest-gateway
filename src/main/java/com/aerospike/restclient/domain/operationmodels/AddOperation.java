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

import com.aerospike.client.Bin;
import com.aerospike.client.Value;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Increment the value of an item in the specified `binName` by the value of `incr`",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/" + AerospikeAPIConstants.AS_CLIENT_VERSION + "/com/aerospike/client/Operation.html")
)
public class AddOperation extends Operation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.ADD,
            requiredMode = Schema.RequiredMode.REQUIRED,
            allowableValues = {OperationTypes.ADD}
    )
    public final String type = OperationTypes.ADD;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private final String binName;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private final Number incr;

    @JsonCreator
    public AddOperation(
            @JsonProperty(value = "binName")
            @Schema(name = "binName", requiredMode = Schema.RequiredMode.REQUIRED) String binName,
            @JsonProperty(value = "incr")
            @Schema(name = "incr", requiredMode = Schema.RequiredMode.REQUIRED) Number incr) {
        this.binName = binName;
        this.incr = incr;
    }

    @Override
    public com.aerospike.client.Operation toOperation() {
        Value val;

        if (incr instanceof Integer || incr instanceof Long) {
            val = Value.get(incr.intValue());
        } else {
            val = Value.get(incr.doubleValue());
        }

        return com.aerospike.client.Operation.add(new Bin(binName, val));
    }
}
