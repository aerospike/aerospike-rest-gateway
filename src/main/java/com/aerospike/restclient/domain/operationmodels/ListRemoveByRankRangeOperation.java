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
        description = "Remove `count` items from a list, beginning with the item with the specified `rank`. If `count` is omitted, all items beginning with the specified `rank` will be removed and returned. Requires Aerospike Server `3.16.0.1` or later.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/" + AerospikeAPIConstants.AS_CLIENT_VERSION + "/com/aerospike/client/cdt/ListOperation.html")
)
public class ListRemoveByRankRangeOperation extends ListOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.LIST_REMOVE_BY_RANK_RANGE,
            requiredMode = Schema.RequiredMode.REQUIRED,
            allowableValues = {OperationTypes.LIST_REMOVE_BY_RANK_RANGE}
    )
    public final String type = OperationTypes.LIST_REMOVE_BY_RANK_RANGE;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private final int rank;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private final ListReturnType listReturnType;

    private boolean inverted;
    private Integer count;

    @JsonCreator
    public ListRemoveByRankRangeOperation(
            @JsonProperty(value = "binName")
            @Schema(name = "binName", requiredMode = Schema.RequiredMode.REQUIRED) String binName,
            @JsonProperty(value = "rank")
            @Schema(name = "rank", requiredMode = Schema.RequiredMode.REQUIRED) int rank,
            @JsonProperty(value = "listReturnType", required = true) ListReturnType listReturnType
    ) {
        super(binName);
        this.rank = rank;
        this.listReturnType = listReturnType;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public com.aerospike.client.Operation toOperation() {
        com.aerospike.client.cdt.CTX[] asCTX = getASCTX();
        int intListReturnType = listReturnType.toListReturnType(inverted);

        if (count == null) {
            return com.aerospike.client.cdt.ListOperation.removeByRankRange(binName, rank, intListReturnType, asCTX);
        }

        return com.aerospike.client.cdt.ListOperation.removeByRankRange(binName, rank, count, intListReturnType, asCTX);
    }
}
