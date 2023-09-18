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
        description = "TODO",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/" + AerospikeAPIConstants.AS_CLIENT_VERSION + "/com/aerospike/client/cdt/MapOperation.html")
)
public class MapRemoveByValueRelativeRankRange extends MapOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.MAP_REMOVE_BY_VALUE_RELATIVE_RANK_RANGE,
            requiredMode = Schema.RequiredMode.REQUIRED,
            allowableValues = {OperationTypes.MAP_REMOVE_BY_VALUE_RELATIVE_RANK_RANGE}
    )
    public final String type = OperationTypes.MAP_REMOVE_BY_VALUE_RELATIVE_RANK_RANGE;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private final int rank;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private final Object value;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private final MapReturnType mapReturnType;

    private boolean inverted = false;

    private Integer count;

    @JsonCreator
    public MapRemoveByValueRelativeRankRange(
            @JsonProperty(value = "binName")
            @Schema(name = "binName", requiredMode = Schema.RequiredMode.REQUIRED) String binName,
            @JsonProperty(value = "rank")
            @Schema(name = "rank", requiredMode = Schema.RequiredMode.REQUIRED) int rank,
            @JsonProperty(value = "value")
            @Schema(name = "value", requiredMode = Schema.RequiredMode.REQUIRED) Object value,
            @JsonProperty(value = "mapReturnType", required = true) MapReturnType mapReturnType
    ) {
        super(binName);
        this.rank = rank;
        this.value = value;
        this.mapReturnType = mapReturnType;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
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

        if (count == null) {
            return com.aerospike.client.cdt.MapOperation.removeByValueRelativeRankRange(binName,
                    Value.get(value), rank, mapReturnType.toMapReturnType(inverted), asCTX);
        }

        return com.aerospike.client.cdt.MapOperation.removeByValueRelativeRankRange(binName,
                Value.get(value), rank, count, mapReturnType.toMapReturnType(inverted), asCTX);
    }
}
