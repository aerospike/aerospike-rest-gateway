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

import com.aerospike.client.Operation;
import io.swagger.v3.oas.annotations.media.Schema;

abstract public class RestClientOperation {

	public RestClientOperation() {}

	abstract public Operation toOperation();

	public static class Type {

		// Operation constants. Used to decode for the /operate endpoints
		public final static String ADD = "add";
		public final static String APPEND = "append";
		public final static String GET = "get";
		public final static String PREPEND = "prepend";
		public final static String READ = "read";
		public final static String GET_HEADER = "getHeader";
		public final static String TOUCH = "touch";
		public final static String PUT = "put";
		public final static String DELETE = "delete";

		// List Operation Constants
		public final static String LIST_APPEND = "listAppend";
		public final static String LIST_APPEND_ITEMS = "listAppendItems";
		public final static String LIST_CLEAR = "listClear";
		public final static String LIST_GET = "listGet";
		public final static String LIST_GET_BY_INDEX = "listGetByIndex";
		public final static String LIST_GET_BY_INDEX_RANGE = "listGetByIndexRange";
		public final static String LIST_GET_BY_RANK = "listGetByRank";
		public final static String LIST_GET_BY_RANK_RANGE = "listGetByRankRange";
		public final static String LIST_GET_BY_VALUE_REL_RANK_RANGE = "listGetByValueRelRankRange";
		public final static String LIST_GET_BY_VALUE = "listGetByValue";
		public final static String LIST_GET_BY_VALUE_RANGE = "listGetByValueRange";
		public final static String LIST_GET_BY_VALUE_LIST = "listGetByValueList";
		public final static String LIST_GET_RANGE = "listGetRange";
		public final static String LIST_INCREMENT = "listIncrement";
		public final static String LIST_INSERT = "listInsert";
		public final static String LIST_INSERT_ITEMS = "listInsertItems";
		public final static String LIST_POP = "listPop";
		public final static String LIST_POP_RANGE = "listPopRange";
		public final static String LIST_REMOVE = "listRemove";
		public final static String LIST_REMOVE_BY_INDEX = "listRemoveByIndex";
		public final static String LIST_REMOVE_BY_INDEX_RANGE = "listRemoveByIndexRange";
		public final static String LIST_REMOVE_BY_RANK = "listRemoveByRank";
		public final static String LIST_REMOVE_BY_RANK_RANGE = "listRemoveByRankRange";
		public final static String LIST_REMOVE_BY_VALUE_REL_RANK_RANGE = "listRemoveByValueRelRankRange";
		public final static String LIST_REMOVE_BY_VALUE = "listRemoveByValue";
		public final static String LIST_REMOVE_BY_VALUE_RANGE = "listRemoveByValueRange";
		public final static String LIST_REMOVE_BY_VALUE_LIST = "listRemoveByValueList";
		public final static String LIST_REMOVE_RANGE = "listRemoveRange";
		public final static String LIST_SET = "listSet";
		public final static String LIST_SET_ORDER = "listSetOrder";
		public final static String LIST_SIZE = "listSize";
		public final static String LIST_SORT = "listSort";
		public final static String LIST_TRIM = "listTrim";
		public final static String LIST_CREATE = "listCreate";

		// Map Operation Constants
		public final static String MAP_CLEAR = "mapClear";
		public final static String MAP_DECREMENT = "mapDecrement";
		public final static String MAP_GET_BY_INDEX = "mapGetByIndex";
		public final static String MAP_GET_BY_INDEX_RANGE = "mapGetByIndexRange";
		public final static String MAP_GET_BY_KEY = "mapGetByKey";
		public final static String MAP_GET_BY_KEY_LIST = "mapGetByKeyList";
		public final static String MAP_GET_BY_KEY_RANGE = "mapGetByKeyRange";
		public final static String MAP_GET_BY_RANK = "mapGetByRank";
		public final static String MAP_GET_BY_RANK_RANGE = "mapGetByRankRange";
		public final static String MAP_GET_BY_VALUE = "mapGetByValue";
		public final static String MAP_GET_BY_VALUE_RANGE = "mapGetByValueRange";
		public final static String MAP_GET_BY_VALUE_LIST = "mapGetByValueList";
		public final static String MAP_GET_BY_KEY_REL_INDEX_RANGE = "mapGetByKeyRelIndexRange";
		public final static String MAP_GET_BY_VALUE_REL_RANK_RANGE = "mapGetByValueRelRankRange";
		public final static String MAP_INCREMENT = "mapIncrement";
		public final static String MAP_PUT = "mapPut";
		public final static String MAP_PUT_ITEMS = "mapPutItems";
		public final static String MAP_REMOVE_BY_INDEX = "mapRemoveByIndex";
		public final static String MAP_REMOVE_BY_INDEX_RANGE = "mapRemoveByIndexRange";
		public final static String MAP_REMOVE_BY_KEY = "mapRemoveByKey";
		public final static String MAP_REMOVE_BY_KEY_RANGE = "mapRemoveByKeyRange";
		public final static String MAP_REMOVE_BY_RANK = "mapRemoveByRank";
		public final static String MAP_REMOVE_BY_RANK_RANGE = "mapRemoveByRankRange";
		public final static String MAP_REMOVE_BY_KEY_REL_INDEX_RANGE = "mapRemoveByKeyRelIndexRange";
		public final static String MAP_REMOVE_BY_VALUE_REL_RANK_RANGE = "mapRemoveByValueRelRankRange";
		public final static String MAP_REMOVE_BY_VALUE = "mapRemoveByValue";
		public final static String MAP_REMOVE_BY_VALUE_RANGE = "mapRemoveByValueRange";
		public final static String MAP_REMOVE_BY_VALUE_LIST = "mapRemoveByValueList";
		public final static String MAP_SET_MAP_POLICY = "mapSetMapPolicy";
		public final static String MAP_SIZE = "mapSize";
		public final static String MAP_CREATE = "mapCreate";

		// Bit Operation Constants
		public final static String BIT_RESIZE = "bitResize";
		public final static String BIT_INSERT = "bitInsert";
		public final static String BIT_REMOVE = "bitRemove";
		public final static String BIT_SET = "bitSet";
		public final static String BIT_OR = "bitOr";
		public final static String BIT_XOR = "bitXor";
		public final static String BIT_AND = "bitAnd";
		public final static String BIT_NOT = "bitNot";
		public final static String BIT_LSHIFT = "bitLshift";
		public final static String BIT_RSHIFT = "bitRshift";
		public final static String BIT_ADD = "bitAdd";
		public final static String BIT_SUBTRACT = "bitSubtract";
		public final static String BIT_SET_INT = "bitSetInt";
		public final static String BIT_GET = "bitGet";
		public final static String BIT_COUNT = "bitCount";
		public final static String BIT_LSCAN = "bitLscan";
		public final static String BIT_RSCAN = "bitRscan";
		public final static String BIT_GET_INT = "bitGetInt";

		// HLL Operation Constants
		public final static String HLL_INIT = "hllInit";
		public final static String HLL_ADD = "hllAdd";
		public final static String HLL_SET_UNION = "hllSetUnion";
		public final static String HLL_SET_COUNT = "hllSetCount";
		public final static String HLL_FOLD = "hllFold";
		public final static String HLL_COUNT = "hllCount";
		public final static String HLL_UNION = "hllUnion";
		public final static String HLL_UNION_COUNT = "hllUnionCount";
		public final static String HLL_INTERSECT_COUNT = "hllIntersectCount";
		public final static String HLL_SIMILARITY = "hllSimilarity";
		public final static String HLL_DESCRIBE = "hllDescribe";
	}
}


class RestClientAddOperation extends RestClientOperation {
	@Schema(description = "TODO", required = true, allowableValues = Type.ADD)
	String operationType = Type.ADD;

	@Schema(description = "TODO", required = true)
	String binName;

	@Schema(description = "TODO", required = true)
	int increment;

	@Override
	public Operation toOperation() {
		return null;
	}
}