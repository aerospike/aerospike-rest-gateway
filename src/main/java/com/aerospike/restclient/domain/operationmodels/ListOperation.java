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
        @JsonSubTypes.Type(value = ListAppendOperation.class, name = OperationTypes.LIST_APPEND),
        @JsonSubTypes.Type(value = ListAppendItemsOperation.class, name = OperationTypes.LIST_APPEND_ITEMS),
        @JsonSubTypes.Type(value = ListCreateOperation.class, name = OperationTypes.LIST_CREATE),
        @JsonSubTypes.Type(value = ListClearOperation.class, name = OperationTypes.LIST_CLEAR),
        @JsonSubTypes.Type(value = ListGetOperation.class, name = OperationTypes.LIST_GET),
        @JsonSubTypes.Type(value = ListGetByIndexOperation.class, name = OperationTypes.LIST_GET_BY_INDEX),
        @JsonSubTypes.Type(value = ListGetByIndexRangeOperation.class, name = OperationTypes.LIST_GET_BY_INDEX_RANGE),
        @JsonSubTypes.Type(value = ListGetByRankOperation.class, name = OperationTypes.LIST_GET_BY_RANK),
        @JsonSubTypes.Type(value = ListGetByRankRangeOperation.class, name = OperationTypes.LIST_GET_BY_RANK_RANGE),
        @JsonSubTypes.Type(
                value = ListGetByValueRelativeRankRangeOperation.class,
                name = OperationTypes.LIST_GET_BY_VALUE_RELATIVE_RANK_RANGE),
        @JsonSubTypes.Type(value = ListGetByValueOperation.class, name = OperationTypes.LIST_GET_BY_VALUE),
        @JsonSubTypes.Type(value = ListGetByValueRangeOperation.class, name = OperationTypes.LIST_GET_BY_VALUE_RANGE),
        @JsonSubTypes.Type(value = ListGetByValueListOperation.class, name = OperationTypes.LIST_GET_BY_VALUE_LIST),
        @JsonSubTypes.Type(value = ListGetRangeOperation.class, name = OperationTypes.LIST_GET_RANGE),
        @JsonSubTypes.Type(value = ListIncrementOperation.class, name = OperationTypes.LIST_INCREMENT),
        @JsonSubTypes.Type(value = ListInsertOperation.class, name = OperationTypes.LIST_INSERT),
        @JsonSubTypes.Type(value = ListInsertItemsOperation.class, name = OperationTypes.LIST_INSERT_ITEMS),
        @JsonSubTypes.Type(value = ListPopOperation.class, name = OperationTypes.LIST_POP),
        @JsonSubTypes.Type(value = ListPopRangeOperation.class, name = OperationTypes.LIST_POP_RANGE),
        @JsonSubTypes.Type(value = ListRemoveOperation.class, name = OperationTypes.LIST_REMOVE),
        @JsonSubTypes.Type(value = ListRemoveByIndexOperation.class, name = OperationTypes.LIST_REMOVE_BY_INDEX),
        @JsonSubTypes.Type(
                value = ListRemoveByIndexRangeOperation.class,
                name = OperationTypes.LIST_REMOVE_BY_INDEX_RANGE),
        @JsonSubTypes.Type(value = ListRemoveByRankOperation.class, name = OperationTypes.LIST_REMOVE_BY_RANK),
        @JsonSubTypes.Type(
                value = ListRemoveByRankRangeOperation.class,
                name = OperationTypes.LIST_REMOVE_BY_RANK_RANGE),
        @JsonSubTypes.Type(
                value = ListRemoveByValueRelativeRankRangeOperation.class,
                name = OperationTypes.LIST_REMOVE_BY_VALUE_RELATIVE_RANK_RANGE),
        @JsonSubTypes.Type(value = ListRemoveByValueOperation.class, name = OperationTypes.LIST_REMOVE_BY_VALUE),
        @JsonSubTypes.Type(
                value = ListRemoveByValueRangeOperation.class,
                name = OperationTypes.LIST_REMOVE_BY_VALUE_RANGE),
        @JsonSubTypes.Type(
                value = ListRemoveByValueListOperation.class,
                name = OperationTypes.LIST_REMOVE_BY_VALUE_LIST),
        @JsonSubTypes.Type(value = ListRemoveRangeOperation.class, name = OperationTypes.LIST_REMOVE_RANGE),
        @JsonSubTypes.Type(value = ListSetOperation.class, name = OperationTypes.LIST_SET),
        @JsonSubTypes.Type(value = ListSetOrderOperation.class, name = OperationTypes.LIST_SET_ORDER),
        @JsonSubTypes.Type(value = ListSizeOperation.class, name = OperationTypes.LIST_SIZE),
        @JsonSubTypes.Type(value = ListSortOperation.class, name = OperationTypes.LIST_SORT),
        @JsonSubTypes.Type(value = ListTrimOperation.class, name = OperationTypes.LIST_TRIM),
})
@Schema(
        description = "The base type for describing all cdt list operations. Should not be used directly.",
        oneOf = {
                ListAppendOperation.class,
                ListAppendItemsOperation.class,
                ListCreateOperation.class,
                ListClearOperation.class,
                ListGetOperation.class,
                ListGetByIndexOperation.class,
                ListGetByIndexRangeOperation.class,
                ListGetByRankOperation.class,
                ListGetByRankRangeOperation.class,
                ListGetByValueRelativeRankRangeOperation.class,
                ListGetByValueOperation.class,
                ListGetByValueRangeOperation.class,
                ListGetByValueListOperation.class,
                ListGetRangeOperation.class,
                ListIncrementOperation.class,
                ListInsertOperation.class,
                ListInsertItemsOperation.class,
                ListPopOperation.class,
                ListPopRangeOperation.class,
                ListRemoveOperation.class,
                ListRemoveByIndexOperation.class,
                ListRemoveByIndexRangeOperation.class,
                ListRemoveByRankOperation.class,
                ListRemoveByRankRangeOperation.class,
                ListRemoveByValueRelativeRankRangeOperation.class,
                ListRemoveByValueOperation.class,
                ListRemoveByValueRangeOperation.class,
                ListRemoveByValueListOperation.class,
                ListRemoveRangeOperation.class,
                ListSetOperation.class,
                ListSetOrderOperation.class,
                ListSizeOperation.class,
                ListSortOperation.class,
                ListTrimOperation.class,
        }
)
public abstract class ListOperation extends Operation {
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    protected String binName;

    protected List<CTX> ctx;

    @JsonCreator
    protected ListOperation(
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
