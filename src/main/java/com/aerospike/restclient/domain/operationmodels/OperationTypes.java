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

public final class OperationTypes {
    public static final String ADD = "ADD";
    public static final String APPEND = "APPEND";
    public static final String PREPEND = "PREPEND";
    public static final String GET = "GET";
    public static final String READ = "READ";
    public static final String GET_HEADER = "GET_HEADER";
    public static final String TOUCH = "TOUCH";
    public static final String PUT = "PUT";
    public static final String DELETE = "DELETE";
    public static final String LIST_APPEND = "LIST_APPEND";
    public static final String LIST_APPEND_ITEMS = "LIST_APPEND_ITEMS";
    public static final String LIST_CREATE = "LIST_CREATE";
    public static final String LIST_CLEAR = "LIST_CLEAR";
    public static final String LIST_GET = "LIST_GET";
    public static final String LIST_GET_BY_INDEX = "LIST_GET_BY_INDEX";
    public static final String LIST_GET_BY_INDEX_RANGE = "LIST_GET_BY_INDEX_RANGE";
    public static final String LIST_GET_BY_RANK = "LIST_GET_BY_RANK";
    public static final String LIST_GET_BY_RANK_RANGE = "LIST_GET_BY_RANK_RANGE";
    public static final String LIST_GET_BY_VALUE_RELATIVE_RANK_RANGE = "LIST_GET_BY_VALUE_RELATIVE_RANK_RANGE";
    public static final String LIST_GET_BY_VALUE = "LIST_GET_BY_VALUE";
    public static final String LIST_GET_BY_VALUE_RANGE = "LIST_GET_BY_VALUE_RANGE";
    public static final String LIST_GET_BY_VALUE_LIST = "LIST_GET_BY_VALUE_LIST";
    public static final String LIST_GET_RANGE = "LIST_GET_RANGE";
    public static final String LIST_INCREMENT = "LIST_INCREMENT";
    public static final String LIST_INSERT = "LIST_INSERT";
    public static final String LIST_INSERT_ITEMS = "LIST_INSERT_ITEMS";
    public static final String LIST_POP = "LIST_POP";
    public static final String LIST_POP_RANGE = "LIST_POP_RANGE";
    public static final String LIST_REMOVE = "LIST_REMOVE";
    public static final String LIST_REMOVE_BY_INDEX = "LIST_REMOVE_BY_INDEX";
    public static final String LIST_REMOVE_BY_INDEX_RANGE = "LIST_REMOVE_BY_INDEX_RANGE";
    public static final String LIST_REMOVE_BY_RANK = "LIST_REMOVE_BY_RANK";
    public static final String LIST_REMOVE_BY_RANK_RANGE = "LIST_REMOVE_BY_RANK_RANGE";
    public static final String LIST_REMOVE_BY_VALUE_RELATIVE_RANK_RANGE = "LIST_REMOVE_BY_VALUE_RELATIVE_RANK_RANGE";
    public static final String LIST_REMOVE_BY_VALUE = "LIST_REMOVE_BY_VALUE";
    public static final String LIST_REMOVE_BY_VALUE_RANGE = "LIST_REMOVE_BY_VALUE_RANGE";
    public static final String LIST_REMOVE_BY_VALUE_LIST = "LIST_REMOVE_BY_VALUE_LIST";
    public static final String LIST_REMOVE_RANGE = "LIST_REMOVE_RANGE";
    public static final String LIST_SET = "LIST_SET";
    public static final String LIST_SET_ORDER = "LIST_SET_ORDER";
    public static final String LIST_SIZE = "LIST_SIZE";
    public static final String LIST_SORT = "LIST_SORT";
    public static final String LIST_TRIM = "LIST_TRIM";
    public static final String MAP_CREATE = "MAP_CREATE";
    public static final String MAP_CLEAR = "MAP_CLEAR";
    public static final String MAP_GET_BY_INDEX = "MAP_GET_BY_INDEX";
    public static final String MAP_GET_BY_INDEX_RANGE = "MAP_GET_BY_INDEX_RANGE";
    public static final String MAP_GET_BY_KEY_RELATIVE_INDEX_RANGE = "MAP_GET_BY_KEY_RELATIVE_INDEX_RANGE";
    public static final String MAP_GET_BY_KEY = "MAP_GET_BY_KEY";
    public static final String MAP_GET_BY_KEY_LIST = "MAP_GET_BY_KEY_LIST";
    public static final String MAP_GET_BY_KEY_RANGE = "MAP_GET_BY_KEY_RANGE";
    public static final String MAP_GET_BY_RANK = "MAP_GET_BY_RANK";
    public static final String MAP_GET_BY_RANK_RANGE = "MAP_GET_BY_RANK_RANGE";
    public static final String MAP_GET_BY_VALUE_RELATIVE_RANK_RANGE = "MAP_GET_BY_VALUE_RELATIVE_RANK_RANGE";
    public static final String MAP_GET_BY_VALUE = "MAP_GET_BY_VALUE";
    public static final String MAP_GET_BY_VALUE_RANGE = "MAP_GET_BY_VALUE_RANGE";
    public static final String MAP_GET_BY_VALUE_LIST = "MAP_GET_BY_VALUE_LIST";
    public static final String MAP_INCREMENT = "MAP_INCREMENT";
    public static final String MAP_PUT = "MAP_PUT";
    public static final String MAP_PUT_ITEMS = "MAP_PUT_ITEMS";
    public static final String MAP_REMOVE_BY_INDEX = "MAP_REMOVE_BY_INDEX";
    public static final String MAP_REMOVE_BY_INDEX_RANGE = "MAP_REMOVE_BY_INDEX_RANGE";
    public static final String MAP_REMOVE_BY_KEY_RELATIVE_INDEX_RANGE = "MAP_REMOVE_BY_KEY_RELATIVE_INDEX_RANGE";
    public static final String MAP_REMOVE_BY_KEY = "MAP_REMOVE_BY_KEY";
    public static final String MAP_REMOVE_BY_KEY_RANGE = "MAP_REMOVE_BY_KEY_RANGE";
    public static final String MAP_REMOVE_BY_RANK = "MAP_REMOVE_BY_RANK";
    public static final String MAP_REMOVE_BY_RANK_RANGE = "MAP_REMOVE_BY_RANK_RANGE";
    public static final String MAP_REMOVE_BY_VALUE_RELATIVE_RANK_RANGE = "MAP_REMOVE_BY_VALUE_RELATIVE_RANK_RANGE";
    public static final String MAP_REMOVE_BY_VALUE = "MAP_REMOVE_BY_VALUE";
    public static final String MAP_REMOVE_BY_VALUE_RANGE = "MAP_REMOVE_BY_VALUE_RANGE";
    public static final String MAP_REMOVE_BY_VALUE_LIST = "MAP_REMOVE_BY_VALUE_LIST";
    public static final String MAP_SET_POLICY = "MAP_SET_POLICY";
    public static final String MAP_SIZE = "MAP_SIZE";

    // Bit Operation Constants
    public static final String BIT_RESIZE = "BIT_RESIZE";
    public static final String BIT_INSERT = "BIT_INSERT";
    public static final String BIT_REMOVE = "BIT_REMOVE";
    public static final String BIT_SET = "BIT_SET";
    public static final String BIT_OR = "BIT_OR";
    public static final String BIT_XOR = "BIT_XOR";
    public static final String BIT_AND = "BIT_AND";
    public static final String BIT_NOT = "BIT_NOT";
    public static final String BIT_LSHIFT = "BIT_LSHIFT";
    public static final String BIT_RSHIFT = "BIT_RSHIFT";
    public static final String BIT_ADD = "BIT_ADD";
    public static final String BIT_SUBTRACT = "BIT_SUBTRACT";
    public static final String BIT_SET_INT = "BIT_SET_INT";
    public static final String BIT_GET = "BIT_GET";
    public static final String BIT_COUNT = "BIT_COUNT";
    public static final String BIT_LSCAN = "BIT_LSCAN";
    public static final String BIT_RSCAN = "BIT_RSCAN";
    public static final String BIT_GET_INT = "BIT_GET_INT";

    // HyperLogLog Operation Constants
    public static final String HLL_INIT = "HLL_INIT";
    public static final String HLL_ADD = "HLL_ADD";
    public static final String HLL_SET_UNION = "HLL_SET_UNION";
    public static final String HLL_SET_COUNT = "HLL_SET_COUNT";
    public static final String HLL_FOLD = "HLL_FOLD";
    public static final String HLL_COUNT = "HLL_COUNT";
    public static final String HLL_UNION = "HLL_UNION";
    public static final String HLL_UNION_COUNT = "HLL_UNION_COUNT";
    public static final String HLL_INTERSECT_COUNT = "HLL_INTERSECT_COUNT";
    public static final String HLL_SIMILARITY = "HLL_SIMILARITY";
    public static final String HLL_DESCRIBE = "HLL_DESCRIBE";
}
