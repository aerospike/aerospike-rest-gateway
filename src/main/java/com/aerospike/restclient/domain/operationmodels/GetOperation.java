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
        description = "Return the contents of a record",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/" + AerospikeAPIConstants.AS_CLIENT_VERSION + "/com/aerospike/client/Operation.html")
)
public class GetOperation extends Operation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.GET,
            required = true,
            allowableValues = OperationTypes.GET
    )
    final public static String type = OperationTypes.GET;

    private final String binName;

    @JsonCreator
    public GetOperation(@JsonProperty(value = "binName") String binName) {
        this.binName = binName;
    }

    @Override
    public com.aerospike.client.Operation toOperation() {
        if (binName == null) {
            return com.aerospike.client.Operation.get();
        }

        return com.aerospike.client.Operation.get(binName);
    }
}


