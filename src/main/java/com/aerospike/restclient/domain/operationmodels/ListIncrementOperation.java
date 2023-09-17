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

import com.aerospike.client.Value;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Increment the value of a an item of a list at the specified index, by the value of `incr`",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/" + AerospikeAPIConstants.AS_CLIENT_VERSION + "/com/aerospike/client/cdt/ListOperation.html")
)
public class ListIncrementOperation extends ListOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.LIST_INCREMENT,
            requiredMode = Schema.RequiredMode.REQUIRED,
            allowableValues = {OperationTypes.LIST_INCREMENT}
    )
    public final String type = OperationTypes.LIST_INCREMENT;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private final int index;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private final Number incr;

    private ListPolicy listPolicy;

    @JsonCreator
    public ListIncrementOperation(
            @JsonProperty(value = "binName")
            @Schema(name = "binName", requiredMode = Schema.RequiredMode.REQUIRED) String binName,
            @JsonProperty(value = "index")
            @Schema(name = "index", requiredMode = Schema.RequiredMode.REQUIRED) int index,
            @JsonProperty(value = "incr")
            @Schema(name = "incr", requiredMode = Schema.RequiredMode.REQUIRED) Number incr
    ) {
        super(binName);
        this.index = index;
        this.incr = incr;
    }

    public ListPolicy getListPolicy() {
        return listPolicy;
    }

    public void setListPolicy(ListPolicy listPolicy) {
        this.listPolicy = listPolicy;
    }

    @Override
    public com.aerospike.client.Operation toOperation() {
        com.aerospike.client.cdt.CTX[] asCTX = getASCTX();
        Value asVal = null;

        if (incr instanceof Integer) {
            asVal = Value.get(incr.intValue());
        } else if (incr instanceof Double) {
            asVal = Value.get(incr.doubleValue());
        }

        if (listPolicy == null) {
            if (asVal == null) {
                return com.aerospike.client.cdt.ListOperation.increment(binName, index, asCTX);
            }
            return com.aerospike.client.cdt.ListOperation.increment(binName, index, asVal, asCTX);
        }

        com.aerospike.client.cdt.ListPolicy asListPolicy = listPolicy.toListPolicy();

        if (asVal == null) {
            return com.aerospike.client.cdt.ListOperation.increment(asListPolicy, binName, index, asCTX);
        }
        return com.aerospike.client.cdt.ListOperation.increment(asListPolicy, binName, index, asVal, asCTX);
    }
}
