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

import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.aerospike.restclient.util.AerospikeOperation;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(
                        value = AddOperation.class,
                        name = OperationTypes.ADD
                ),
                @JsonSubTypes.Type(
                        value = AppendOperation.class,
                        name = OperationTypes.APPEND
                ),
                @JsonSubTypes.Type(
                        value = GetOperation.class,
                        name = OperationTypes.GET
                ),
                @JsonSubTypes.Type(
                        value = ReadOperation.class,
                        name = OperationTypes.READ
                ),
                @JsonSubTypes.Type(
                        value = GetHeaderOperation.class,
                        name = OperationTypes.GET_HEADER
                ),
                @JsonSubTypes.Type(
                        value = TouchOperation.class,
                        name = OperationTypes.TOUCH
                ),
                @JsonSubTypes.Type(
                        value = PutOperation.class,
                        name = OperationTypes.PUT
                ),
                @JsonSubTypes.Type(
                        value = ListAppendOperation.class,
                        name = OperationTypes.LIST_APPEND
                ),
                @JsonSubTypes.Type(
                        value = ListAppendItemsOperation.class,
                        name = OperationTypes.LIST_APPEND_ITEMS
                ),
                @JsonSubTypes.Type(
                        value = ListClearOperation.class,
                        name = OperationTypes.LIST_CLEAR
                ),
                @JsonSubTypes.Type(
                        value = ListGetOperation.class,
                        name = OperationTypes.LIST_GET
                ),
                @JsonSubTypes.Type(
                        value = ListGetByIndexOperation.class,
                        name = OperationTypes.LIST_GET_BY_INDEX
                ),
                @JsonSubTypes.Type(
                        value = ListGetByIndexRangeOperation.class,
                        name = OperationTypes.LIST_GET_BY_INDEX_RANGE
                ),
                @JsonSubTypes.Type(
                        value = ListGetByRankOperation.class,
                        name = OperationTypes.LIST_GET_BY_RANK
                ),
                @JsonSubTypes.Type(
                        value = ListGetByRankRangeOperation.class,
                        name = OperationTypes.LIST_GET_BY_RANK_RANGE
                ),
                @JsonSubTypes.Type(
                        value = ListGetByValueOperation.class,
                        name = OperationTypes.LIST_GET_BY_VALUE
                ),
                @JsonSubTypes.Type(
                        value = ListGetByValueRangeOperation.class,
                        name = OperationTypes.LIST_GET_BY_VALUE_RANGE
                ),
                @JsonSubTypes.Type(
                        value = ListGetByValueListOperation.class,
                        name = OperationTypes.LIST_GET_BY_VALUE_LIST
                ),
                @JsonSubTypes.Type(
                        value = ListGetRangeOperation.class,
                        name = OperationTypes.LIST_GET_RANGE
                ),
                @JsonSubTypes.Type(
                        value = ListIncrementOperation.class,
                        name = OperationTypes.LIST_INCREMENT
                ),
                @JsonSubTypes.Type(
                        value = ListInsertOperation.class,
                        name = OperationTypes.LIST_INSERT
                ),
                @JsonSubTypes.Type(
                        value = ListInsertItemsOperation.class,
                        name = OperationTypes.LIST_INSERT_ITEMS
                ),
                @JsonSubTypes.Type(
                        value = ListPopOperation.class,
                        name = OperationTypes.LIST_POP
                ),
                @JsonSubTypes.Type(
                        value = ListPopRangeOperation.class,
                        name = OperationTypes.LIST_POP_RANGE
                ),
                @JsonSubTypes.Type(
                        value = ListRemoveOperation.class,
                        name = OperationTypes.LIST_REMOVE
                ),
                @JsonSubTypes.Type(
                        value = ListRemoveByIndexOperation.class,
                        name = OperationTypes.LIST_REMOVE_BY_INDEX
                ),
                @JsonSubTypes.Type(
                        value = ListRemoveByIndexRangeOperation.class,
                        name = OperationTypes.LIST_REMOVE_BY_INDEX_RANGE
                ),
                @JsonSubTypes.Type(
                        value = ListRemoveByRankOperation.class,
                        name = OperationTypes.LIST_REMOVE_BY_RANK
                ),
                @JsonSubTypes.Type(
                        value = ListRemoveByRankRangeOperation.class,
                        name = OperationTypes.LIST_REMOVE_BY_RANK_RANGE
                ),
                @JsonSubTypes.Type(
                        value = ListRemoveByValueOperation.class,
                        name = OperationTypes.LIST_REMOVE_BY_VALUE
                ),
                @JsonSubTypes.Type(
                        value = ListRemoveByValueRangeOperation.class,
                        name = OperationTypes.LIST_REMOVE_BY_VALUE_RANGE
                ),
                @JsonSubTypes.Type(
                        value = ListRemoveByValueListOperation.class,
                        name = OperationTypes.LIST_REMOVE_BY_VALUE_LIST
                ),
                @JsonSubTypes.Type(
                        value = ListRemoveRangeOperation.class,
                        name = OperationTypes.LIST_REMOVE_RANGE
                ),
                @JsonSubTypes.Type(
                        value = ListSetOperation.class,
                        name = OperationTypes.LIST_SET
                ),
                @JsonSubTypes.Type(
                        value = ListSetOrderOperation.class,
                        name = OperationTypes.LIST_SET_ORDER
                ),
                @JsonSubTypes.Type(
                        value = ListSizeOperation.class,
                        name = OperationTypes.LIST_SIZE
                ),
                @JsonSubTypes.Type(
                        value = ListSortOperation.class,
                        name = OperationTypes.LIST_SORT
                ),
                @JsonSubTypes.Type(
                        value = ListTrimOperation.class,
                        name = OperationTypes.LIST_TRIM
                ),
                @JsonSubTypes.Type(
                        value = MapClearOperation.class,
                        name = OperationTypes.MAP_CLEAR
                ),
                @JsonSubTypes.Type(
                        value = MapGetByIndexOperation.class,
                        name = OperationTypes.MAP_GET_BY_INDEX
                ),
                @JsonSubTypes.Type(
                        value = MapGetByIndexRangeOperation.class,
                        name = OperationTypes.MAP_GET_BY_INDEX_RANGE
                ),
                @JsonSubTypes.Type(
                        value = MapGetByKeyOperation.class,
                        name = OperationTypes.MAP_GET_BY_KEY
                ),
                @JsonSubTypes.Type(
                        value = MapGetByKeyListOperation.class,
                        name = OperationTypes.MAP_GET_BY_KEY_LIST
                ),
                @JsonSubTypes.Type(
                        value = MapGetByKeyRangeOperation.class,
                        name = OperationTypes.MAP_GET_BY_KEY_RANGE
                ),
                @JsonSubTypes.Type(
                        value = MapGetByRankOperation.class,
                        name = OperationTypes.MAP_GET_BY_RANK
                ),
                @JsonSubTypes.Type(
                        value = MapGetByRankRangeOperation.class,
                        name = OperationTypes.MAP_GET_BY_RANK_RANGE
                ),
                @JsonSubTypes.Type(
                        value = MapGetByValueOperation.class,
                        name = OperationTypes.MAP_GET_BY_VALUE
                ),
                @JsonSubTypes.Type(
                        value = MapGetByValueRangeOperation.class,
                        name = OperationTypes.MAP_GET_BY_VALUE_RANGE
                ),
                @JsonSubTypes.Type(
                        value = MapGetByValueListOperation.class,
                        name = OperationTypes.MAP_GET_BY_VALUE_LIST
                ),
                @JsonSubTypes.Type(
                        value = MapIncrementOperation.class,
                        name = OperationTypes.MAP_INCREMENT
                ),
                @JsonSubTypes.Type(
                        value = MapPutOperation.class,
                        name = OperationTypes.MAP_PUT
                ),
                @JsonSubTypes.Type(
                        value = MapPutItemsOperation.class,
                        name = OperationTypes.MAP_PUT_ITEMS
                ),
                @JsonSubTypes.Type(
                        value = MapRemoveByIndexOperation.class,
                        name = OperationTypes.MAP_REMOVE_BY_INDEX
                ),
                @JsonSubTypes.Type(
                        value = MapRemoveByIndexRangeOperation.class,
                        name = OperationTypes.MAP_REMOVE_BY_INDEX_RANGE
                ),
                @JsonSubTypes.Type(
                        value = MapRemoveByKeyOperation.class,
                        name = OperationTypes.MAP_REMOVE_BY_KEY
                ),
                @JsonSubTypes.Type(
                        value = MapRemoveByKeyRangeOperation.class,
                        name = OperationTypes.MAP_REMOVE_BY_KEY_RANGE
                ),
                @JsonSubTypes.Type(
                        value = MapRemoveByRankOperation.class,
                        name = OperationTypes.MAP_REMOVE_BY_RANK
                ),
                @JsonSubTypes.Type(
                        value = MapRemoveByRankRangeOperation.class,
                        name = OperationTypes.MAP_REMOVE_BY_RANK_RANGE
                ),
                @JsonSubTypes.Type(
                        value = MapRemoveByValueOperation.class,
                        name = OperationTypes.MAP_REMOVE_BY_VALUE
                ),
                @JsonSubTypes.Type(
                        value = MapRemoveByValueRangeOperation.class,
                        name = OperationTypes.MAP_REMOVE_BY_VALUE_RANGE
                ),
                @JsonSubTypes.Type(
                        value = MapRemoveByValueListOperation.class,
                        name = OperationTypes.MAP_REMOVE_BY_VALUE_LIST
                ),
                @JsonSubTypes.Type(
                        value = MapSetPolicyOperation.class,
                        name = OperationTypes.MAP_SET_POLICY
                ),
                @JsonSubTypes.Type(
                        value = MapSizeOperation.class,
                        name = OperationTypes.MAP_SIZE
                ),
        }
)
@Schema(
        description = "TODO", oneOf = {
        AddOperation.class,
        AppendOperation.class,
        GetOperation.class,
        ReadOperation.class,
        GetHeaderOperation.class,
        TouchOperation.class,
        PutOperation.class,
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

    public Operation() {
    }

    public Operation(AerospikeOperation operation, Map<String, Object> values) {
        this.operation = operation;
        this.opValues = values;
    }

    @SuppressWarnings("unchecked")
    public Operation(Map<String, Object> opMap) {
        this.operation = AerospikeOperation.valueOf((String) opMap.get(AerospikeAPIConstants.OPERATION_FIELD));
        this.opValues = (Map<String, Object>) opMap.get(AerospikeAPIConstants.OPERATION_VALUES_FIELD);
    }

    @Schema(
            required = true,
            description = "Aerospike operation to perform on the record",
            example = "LIST_APPEND_ITEMS",
            hidden = true
    )
    private AerospikeOperation operation;

    @Schema(required = true, example = "{\"bin\":\"listbin\", \"values\":[1,2,3]}", hidden = true)
    private Map<String, Object> opValues;

    public AerospikeOperation getOperation() {
        return this.operation;
    }

    public Map<String, Object> getOpValues() {
        return this.opValues;
    }

    public void setOperation(AerospikeOperation op) {
        this.operation = op;
    }

    public void setOpValues(Map<String, Object> opVals) {
        this.opValues = opVals;
    }

    abstract public com.aerospike.client.Operation toOperation();
}


