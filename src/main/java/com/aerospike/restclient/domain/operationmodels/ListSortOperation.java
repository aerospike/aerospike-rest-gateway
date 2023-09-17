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

import com.aerospike.client.cdt.ListSortFlags;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(
        description = "Perform a sort operation on the list. Requires Aerospike Server `3.16.0.1` or later.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/" + AerospikeAPIConstants.AS_CLIENT_VERSION + "/com/aerospike/client/cdt/ListOperation.html")
)
public class ListSortOperation extends ListOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.LIST_SORT,
            requiredMode = Schema.RequiredMode.REQUIRED,
            allowableValues = {OperationTypes.LIST_SORT}
    )
    public final String type = OperationTypes.LIST_SORT;

    private List<ListSortFlag> sortFlags;

    @JsonCreator
    public ListSortOperation(
            @JsonProperty(value = "binName")
            @Schema(name = "binName", requiredMode = Schema.RequiredMode.REQUIRED) String binName
    ) {
        super(binName);
    }

    public List<ListSortFlag> getSortFlags() {
        return sortFlags;
    }

    public void setSortFlags(List<ListSortFlag> sortFlags) {
        this.sortFlags = sortFlags;
    }

    @Override
    public com.aerospike.client.Operation toOperation() {
        com.aerospike.client.cdt.CTX[] asCTX = getASCTX();
        int asSortFlags = ListSortFlags.DEFAULT;

        if (sortFlags != null) {
            asSortFlags = sortFlags.stream().reduce(0, (acc, flag) -> acc | flag.flag, Integer::sum);
        }

        return com.aerospike.client.cdt.ListOperation.sort(binName, asSortFlags, asCTX);
    }
}
