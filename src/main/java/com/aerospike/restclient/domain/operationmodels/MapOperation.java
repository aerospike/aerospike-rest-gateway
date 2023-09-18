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

import com.aerospike.restclient.domain.ctxmodels.CTX;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = MapCreateOperation.class, name = OperationTypes.MAP_CREATE),
        @JsonSubTypes.Type(value = MapClearOperation.class, name = OperationTypes.MAP_CLEAR),
        @JsonSubTypes.Type(value = MapGetByIndexOperation.class, name = OperationTypes.MAP_GET_BY_INDEX),
        @JsonSubTypes.Type(value = MapGetByIndexRangeOperation.class, name = OperationTypes.MAP_GET_BY_INDEX_RANGE),
        @JsonSubTypes.Type(value = MapGetByKeyOperation.class, name = OperationTypes.MAP_GET_BY_KEY),
        @JsonSubTypes.Type(value = MapGetByKeyListOperation.class, name = OperationTypes.MAP_GET_BY_KEY_LIST),
        @JsonSubTypes.Type(value = MapGetByKeyRangeOperation.class, name = OperationTypes.MAP_GET_BY_KEY_RANGE),
        @JsonSubTypes.Type(value = MapGetByRankOperation.class, name = OperationTypes.MAP_GET_BY_RANK),
        @JsonSubTypes.Type(value = MapGetByRankRangeOperation.class, name = OperationTypes.MAP_GET_BY_RANK_RANGE),
        @JsonSubTypes.Type(value = MapGetByValueOperation.class, name = OperationTypes.MAP_GET_BY_VALUE),
        @JsonSubTypes.Type(value = MapGetByValueRangeOperation.class, name = OperationTypes.MAP_GET_BY_VALUE_RANGE),
        @JsonSubTypes.Type(value = MapGetByValueListOperation.class, name = OperationTypes.MAP_GET_BY_VALUE_LIST),
        @JsonSubTypes.Type(
                value = MapGetByValueRelativeRankRangeOperation.class,
                name = OperationTypes.MAP_GET_BY_VALUE_RELATIVE_RANK_RANGE),
        @JsonSubTypes.Type(
                value = MapGetByKeyRelativeIndexRange.class,
                name = OperationTypes.MAP_GET_BY_KEY_RELATIVE_INDEX_RANGE),
        @JsonSubTypes.Type(value = MapIncrementOperation.class, name = OperationTypes.MAP_INCREMENT),
        @JsonSubTypes.Type(value = MapPutOperation.class, name = OperationTypes.MAP_PUT),
        @JsonSubTypes.Type(value = MapPutItemsOperation.class, name = OperationTypes.MAP_PUT_ITEMS),
        @JsonSubTypes.Type(value = MapRemoveByIndexOperation.class, name = OperationTypes.MAP_REMOVE_BY_INDEX),
        @JsonSubTypes.Type(
                value = MapRemoveByIndexRangeOperation.class,
                name = OperationTypes.MAP_REMOVE_BY_INDEX_RANGE),
        @JsonSubTypes.Type(value = MapRemoveByKeyOperation.class, name = OperationTypes.MAP_REMOVE_BY_KEY),
        @JsonSubTypes.Type(value = MapRemoveByKeyRangeOperation.class, name = OperationTypes.MAP_REMOVE_BY_KEY_RANGE),
        @JsonSubTypes.Type(value = MapRemoveByRankOperation.class, name = OperationTypes.MAP_REMOVE_BY_RANK),
        @JsonSubTypes.Type(
                value = MapRemoveByRankRangeOperation.class,
                name = OperationTypes.MAP_REMOVE_BY_RANK_RANGE),
        @JsonSubTypes.Type(value = MapRemoveByValueOperation.class, name = OperationTypes.MAP_REMOVE_BY_VALUE),
        @JsonSubTypes.Type(
                value = MapRemoveByValueRangeOperation.class,
                name = OperationTypes.MAP_REMOVE_BY_VALUE_RANGE),
        @JsonSubTypes.Type(
                value = MapRemoveByValueListOperation.class,
                name = OperationTypes.MAP_REMOVE_BY_VALUE_LIST),
        @JsonSubTypes.Type(
                value = MapRemoveByKeyRelativeIndexRange.class,
                name = OperationTypes.MAP_REMOVE_BY_KEY_RELATIVE_INDEX_RANGE),
        @JsonSubTypes.Type(
                value = MapRemoveByValueRelativeRankRange.class,
                name = OperationTypes.MAP_REMOVE_BY_VALUE_RELATIVE_RANK_RANGE),
        @JsonSubTypes.Type(value = MapSetPolicyOperation.class, name = OperationTypes.MAP_SET_POLICY),
        @JsonSubTypes.Type(value = MapSizeOperation.class, name = OperationTypes.MAP_SIZE),
})
@Schema(
        description = "The base type for describing all cdt map operations. Should not be used directly.",
        oneOf = {
                MapCreateOperation.class,
                MapClearOperation.class,
                MapGetByIndexOperation.class,
                MapGetByIndexRangeOperation.class,
                MapGetByKeyOperation.class,
                MapGetByKeyListOperation.class,
                MapGetByKeyRangeOperation.class,
                MapGetByRankOperation.class,
                MapGetByRankRangeOperation.class,
                MapGetByValueOperation.class,
                MapGetByValueRangeOperation.class,
                MapGetByValueListOperation.class,
                MapGetByValueRelativeRankRangeOperation.class,
                MapGetByKeyRelativeIndexRange.class,
                MapIncrementOperation.class,
                MapPutOperation.class,
                MapPutItemsOperation.class,
                MapRemoveByIndexOperation.class,
                MapRemoveByIndexRangeOperation.class,
                MapRemoveByKeyOperation.class,
                MapRemoveByKeyRangeOperation.class,
                MapRemoveByRankOperation.class,
                MapRemoveByRankRangeOperation.class,
                MapRemoveByValueOperation.class,
                MapRemoveByValueRangeOperation.class,
                MapRemoveByValueListOperation.class,
                MapRemoveByValueRelativeRankRange.class,
                MapSetPolicyOperation.class,
                MapSizeOperation.class,
        }
)
public abstract class MapOperation extends Operation {

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    protected String binName;

    protected List<CTX> ctx;

    @JsonCreator
    protected MapOperation(
            @JsonProperty(value = "binName")
            @Schema(name = "binName", requiredMode = Schema.RequiredMode.REQUIRED) String binName
    ) {
        this.binName = binName;
    }

    public List<CTX> getCtx() {
        return ctx;
    }

    public void setCtx(List<CTX> ctx) {
        this.ctx = ctx;
    }

    protected com.aerospike.client.cdt.CTX[] getASCTX() {
        return Optional.ofNullable(ctx)
                .orElseGet(Collections::emptyList)
                .stream()
                .map(CTX::toCTX)
                .toArray(com.aerospike.client.cdt.CTX[]::new);
    }
}
