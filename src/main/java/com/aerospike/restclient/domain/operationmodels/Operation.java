/*
 * Copyright 2019 Aerospike, Inc.
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

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(
                        value = AddOperation.class, name = OperationTypes.ADD
                ), @JsonSubTypes.Type(
                value = PrependOperation.class, name = OperationTypes.PREPEND
        ), @JsonSubTypes.Type(
                value = AppendOperation.class, name = OperationTypes.APPEND
        ), @JsonSubTypes.Type(
                value = GetOperation.class, name = OperationTypes.GET
        ), @JsonSubTypes.Type(
                value = ReadOperation.class, name = OperationTypes.READ
        ), @JsonSubTypes.Type(
                value = GetHeaderOperation.class, name = OperationTypes.GET_HEADER
        ), @JsonSubTypes.Type(
                value = TouchOperation.class, name = OperationTypes.TOUCH
        ), @JsonSubTypes.Type(
                value = PutOperation.class, name = OperationTypes.PUT
        ), @JsonSubTypes.Type(
                value = DeleteOperation.class, name = OperationTypes.DELETE
        ), @JsonSubTypes.Type(
                value = ListOperation.class
        ), @JsonSubTypes.Type(
                value = MapOperation.class
        ),
        }
)
@Schema(
        description = "TODO", oneOf = {
        AddOperation.class,
        PrependOperation.class,
        AppendOperation.class,
        GetOperation.class,
        ReadOperation.class,
        GetHeaderOperation.class,
        TouchOperation.class,
        PutOperation.class,
        DeleteOperation.class,
        ListAppendOperation.class,
        ListAppendItemsOperation.class,
        ListClearOperation.class,
        ListGetOperation.class,
        ListGetByIndexOperation.class,
        ListGetByIndexRangeOperation.class,
        ListGetByRankOperation.class,
        ListGetByRankRangeOperation.class,
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
        ListRemoveByValueOperation.class,
        ListRemoveByValueRangeOperation.class,
        ListRemoveByValueListOperation.class,
        ListRemoveRangeOperation.class,
        ListSetOperation.class,
        ListSetOrderOperation.class,
        ListSizeOperation.class,
        ListSortOperation.class,
        ListTrimOperation.class,
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
        MapSetPolicyOperation.class,
        MapSizeOperation.class,
}
)
abstract public class Operation {
    abstract public com.aerospike.client.Operation toOperation();
}


