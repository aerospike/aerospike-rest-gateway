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
        description = "Return an item, located a specific index, from a list in the specified bin. The value of `listReturnType` determines what will be returned. Requires Aerospike Server `3.16.0.1` or greater.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/" + AerospikeAPIConstants.AS_CLIENT_VERSION + "/com/aerospike/client/cdt/ListOperation.html")
)
public class ListGetByIndexOperation extends ListOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.LIST_GET_BY_INDEX,
            requiredMode = Schema.RequiredMode.REQUIRED,
            allowableValues = {OperationTypes.LIST_GET_BY_INDEX}
    )
    public final String type = OperationTypes.LIST_GET_BY_INDEX;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private final int index;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private final ListReturnType listReturnType;

    private boolean inverted;

    @JsonCreator
    public ListGetByIndexOperation(
            @JsonProperty(value = "binName")
            @Schema(name = "binName", requiredMode = Schema.RequiredMode.REQUIRED) String binName,
            @JsonProperty(value = "index")
            @Schema(name = "index", requiredMode = Schema.RequiredMode.REQUIRED) int index,
            @JsonProperty(value = "listReturnType")
            @Schema(name = "listReturnType", requiredMode = Schema.RequiredMode.REQUIRED) ListReturnType listReturnType
    ) {
        super(binName);
        this.index = index;
        this.listReturnType = listReturnType;
    }

    public boolean isInverted() {
        return inverted;
    }

    public void setInverted(boolean inverted) {
        this.inverted = inverted;
    }

    @Override
    public com.aerospike.client.Operation toOperation() {
        com.aerospike.client.cdt.CTX[] asCTX = getASCTX();
        return com.aerospike.client.cdt.ListOperation.getByIndex(binName, index,
                listReturnType.toListReturnType(inverted), asCTX);
    }
}
