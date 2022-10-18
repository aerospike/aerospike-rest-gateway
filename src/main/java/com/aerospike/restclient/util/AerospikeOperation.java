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
package com.aerospike.restclient.util;

public enum AerospikeOperation {

    // Operation constants. Used to decode for the /operate endpoints
    ADD,
    APPEND,
    GET,
    PREPEND,
    READ,
    GET_HEADER,
    TOUCH,
    PUT,
    DELETE,

    // List Operation Constants
    LIST_APPEND,
    LIST_APPEND_ITEMS,
    LIST_CLEAR,
    LIST_GET,
    LIST_GET_BY_INDEX,
    LIST_GET_BY_INDEX_RANGE,
    LIST_GET_BY_RANK,
    LIST_GET_BY_RANK_RANGE,
    LIST_GET_BY_VALUE_REL_RANK_RANGE,
    LIST_GET_BY_VALUE,
    LIST_GET_BY_VALUE_RANGE,
    LIST_GET_BY_VALUE_LIST,
    LIST_GET_RANGE,
    LIST_INCREMENT,
    LIST_INSERT,
    LIST_INSERT_ITEMS,
    LIST_POP,
    LIST_POP_RANGE,
    LIST_REMOVE,
    LIST_REMOVE_BY_INDEX,
    LIST_REMOVE_BY_INDEX_RANGE,
    LIST_REMOVE_BY_RANK,
    LIST_REMOVE_BY_RANK_RANGE,
    LIST_REMOVE_BY_VALUE_REL_RANK_RANGE,
    LIST_REMOVE_BY_VALUE,
    LIST_REMOVE_BY_VALUE_RANGE,
    LIST_REMOVE_BY_VALUE_LIST,
    LIST_REMOVE_RANGE,
    LIST_SET,
    LIST_SET_ORDER,
    LIST_SIZE,
    LIST_SORT,
    LIST_TRIM,
    LIST_CREATE,

    // Map Operation Constants
    MAP_CLEAR,
    MAP_DECREMENT,
    MAP_GET_BY_INDEX,
    MAP_GET_BY_INDEX_RANGE,
    MAP_GET_BY_KEY,
    MAP_GET_BY_KEY_LIST,
    MAP_GET_BY_KEY_RANGE,
    MAP_GET_BY_RANK,
    MAP_GET_BY_RANK_RANGE,
    MAP_GET_BY_VALUE,
    MAP_GET_BY_VALUE_RANGE,
    MAP_GET_BY_VALUE_LIST,
    MAP_GET_BY_KEY_REL_INDEX_RANGE,
    MAP_GET_BY_VALUE_REL_RANK_RANGE,
    MAP_INCREMENT,
    MAP_PUT,
    MAP_PUT_ITEMS,
    MAP_REMOVE_BY_INDEX,
    MAP_REMOVE_BY_INDEX_RANGE,
    MAP_REMOVE_BY_KEY,
    MAP_REMOVE_BY_KEY_RANGE,
    MAP_REMOVE_BY_RANK,
    MAP_REMOVE_BY_RANK_RANGE,
    MAP_REMOVE_BY_KEY_REL_INDEX_RANGE,
    MAP_REMOVE_BY_VALUE_REL_RANK_RANGE,
    MAP_REMOVE_BY_VALUE,
    MAP_REMOVE_BY_VALUE_RANGE,
    MAP_REMOVE_BY_VALUE_LIST,
    MAP_SET_MAP_POLICY,
    MAP_SIZE,
    MAP_CREATE,

    // Bit Operation Constants
    BIT_RESIZE,
    BIT_INSERT,
    BIT_REMOVE,
    BIT_SET,
    BIT_OR,
    BIT_XOR,
    BIT_AND,
    BIT_NOT,
    BIT_LSHIFT,
    BIT_RSHIFT,
    BIT_ADD,
    BIT_SUBTRACT,
    BIT_SET_INT,
    BIT_GET,
    BIT_COUNT,
    BIT_LSCAN,
    BIT_RSCAN,
    BIT_GET_INT,

    // HyperLogLog Operation Constants
    HLL_INIT,
    HLL_ADD,
    HLL_SET_UNION,
    HLL_SET_COUNT,
    HLL_FOLD,
    HLL_COUNT,
    HLL_UNION,
    HLL_UNION_COUNT,
    HLL_INTERSECT_COUNT,
    HLL_SIMILARITY,
    HLL_DESCRIBE
}
