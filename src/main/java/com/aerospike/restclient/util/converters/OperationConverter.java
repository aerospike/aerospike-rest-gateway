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
package com.aerospike.restclient.util.converters;

import com.aerospike.client.Bin;
import com.aerospike.client.Operation;
import com.aerospike.client.Value;
import com.aerospike.client.cdt.*;
import com.aerospike.restclient.util.AerospikeOperation;
import com.aerospike.restclient.util.RestClientErrors.InvalidOperationError;

import java.util.*;

/*
 * Class containing static methods used for converting Java Maps to Aerospike Operations.
 */
public class OperationConverter {

	// Constants for accessing values from maps
	public static final String LIST_RETURN_KEY = "listReturnType";
	public static final String INVERTED_KEY = "inverted";
	public static final String LIST_ORDER_KEY = "listOrder";
	public static final String LIST_SORT_FLAGS_KEY = "listSortFlags";
	public static final String BIN_KEY = "bin";
	public static final String INDEX_KEY = "index";
	public static final String INCR_KEY = "incr";
	public static final String DECR_KEY = "decr";
	public static final String VALUE_KEY = "value";
	public static final String VALUES_KEY = "values";
	public static final String ORDER_KEY = "order";
	public static final String COUNT_KEY = "count";
	public static final String LIST_POLICY_KEY = "listPolicy";
	public static final String RANK_KEY = "rank";
	public static final String VALUE_BEGIN_KEY = "valueBegin";
	public static final String VALUE_END_KEY = "valueEnd";
	public static final String MAP_KEY_KEY = "key";
	public static final String MAP_KEY_BEGIN_KEY = "keyBegin";
	public static final String MAP_KEY_END_KEY = "keyEnd";
	public static final String MAP_KEYS_KEY = "keys";
	public static final String MAP_VALUES_KEY = "map";
	public static final String MAP_POLICY_KEY = "mapPolicy";
	public static final String MAP_ORDER_KEY = "mapOrder";
	public static final String MAP_RETURN_KEY = "mapReturnType";
	public static final String MAP_WRITE_FLAGS_KEY = "mapWriteFlags";
	public static final String MAP_WRITE_MODE_KEY = "mapWriteMode";
	public static final String OP_VALUES_KEY = "opValues";
	public static final String OPERATION_FIELD_KEY = "operation";
	public static final String WRITE_FLAGS_KEY = "writeFlags";
	public static final String WRITE_MODE_KEY = "writeMode";


	@SuppressWarnings("unchecked")
	public static Operation convertMapToOperation(Map<String, Object>operationMap) {
		/* Make sure that the user is not providing additional top level keys */
		hasAllRequiredKeys(operationMap, OPERATION_FIELD_KEY, OP_VALUES_KEY);
		onlyHasAllowedKeys(operationMap, OPERATION_FIELD_KEY, OP_VALUES_KEY);

		AerospikeOperation opName = (AerospikeOperation) operationMap.get(OPERATION_FIELD_KEY);
		if (opName == null) {
			throw new InvalidOperationError("Operation must contain the \"operation\" field");
		}
		Map<String, Object> opValues = (Map<String, Object>) operationMap.get(OP_VALUES_KEY);
		if (opValues == null) {
			throw new InvalidOperationError("Operation must contain the \"opValues\" field");
		}
		switch(opName) {
			/* Basic Operations */
			case ADD:
				return mapToAddOp(opValues);

			case APPEND:
				return mapToAppendOp(opValues);

			case GET:
				return mapToGetOp(opValues);

			case PREPEND:
				return mapToPrependOp(opValues);

			case READ:
				return mapToReadOp(opValues);

			case GET_HEADER:
				return mapToGetHeaderOp(opValues);

			case TOUCH:
				return mapToTouchOp(opValues);

			case PUT:
				return mapToPutOp(opValues);

			case DELETE:
				return mapToDeleteOp(opValues);
			/* List Operations */

			case LIST_APPEND:
				return mapToListAppendOp(opValues);

			case LIST_APPEND_ITEMS:
				return mapToListAppendItemsOp(opValues);

			case LIST_CLEAR:
				return mapToListClearOp(opValues);

			case LIST_GET:
				return mapToListGetOp(opValues);

			case LIST_GET_BY_INDEX:
				return mapToListGetByIndexOp(opValues);

			case LIST_GET_BY_INDEX_RANGE:
				return mapToListGetByIndexRangeOp(opValues);

			case LIST_GET_BY_RANK:
				return mapToListGetByRankOp(opValues);

			case LIST_GET_BY_RANK_RANGE:
				return mapToListGetByRankRangeOp(opValues);

			case LIST_GET_BY_VALUE_REL_RANK_RANGE:
				return mapToListGetByValueRelRankRangeOp(opValues);

			case LIST_GET_BY_VALUE:
				return mapToListGetByValueOp(opValues);

			case LIST_GET_BY_VALUE_RANGE:
				return mapToListGetByValueRangeOp(opValues);

			case LIST_GET_BY_VALUE_LIST:
				return mapToListGetByValueListOp(opValues);

			case LIST_GET_RANGE:
				return mapToListGetRangeOp(opValues);

			case LIST_INCREMENT:
				return mapToListIncrementOp(opValues);

			case LIST_INSERT:
				return mapToListInsertOp(opValues);

			case LIST_INSERT_ITEMS:
				return mapToListInsertItemsOp(opValues);

			case LIST_POP:
				return mapToListPopOp(opValues);

			case LIST_POP_RANGE:
				return mapToListPopRangeOp(opValues);

			case LIST_REMOVE:
				return mapToListRemoveOp(opValues);

			case LIST_REMOVE_BY_INDEX:
				return mapToListRemoveByIndexOp(opValues);

			case LIST_REMOVE_BY_INDEX_RANGE:
				return mapToListRemoveByIndexRangeOp(opValues);

			case LIST_REMOVE_BY_RANK:
				return mapToListRemoveByRankOp(opValues);

			case LIST_REMOVE_BY_RANK_RANGE:
				return mapToListRemoveByRankRangeOp(opValues);

			case LIST_REMOVE_BY_VALUE_REL_RANK_RANGE:
				return mapToListRemoveByValueRelRankRangeOp(opValues);

			case LIST_REMOVE_BY_VALUE:
				return mapToListRemoveByValueOp(opValues);

			case LIST_REMOVE_BY_VALUE_RANGE:
				return mapToListRemoveByValueRangeOp(opValues);

			case LIST_REMOVE_BY_VALUE_LIST:
				return mapToListRemoveByValueListOp(opValues);

			case LIST_REMOVE_RANGE:
				return mapToListRemoveRangeOp(opValues);

			case LIST_SET:
				return mapToListSetOp(opValues);

			case LIST_SET_ORDER:
				return mapToListSetOrderOp(opValues);

			case LIST_SIZE:
				return mapToListSizeOp(opValues);

			case LIST_SORT:
				return mapToListSortOp(opValues);

			case LIST_TRIM:
				return mapToListTrimOp(opValues);
			/* Map Operations*/

			case MAP_CLEAR:
				return mapToMapClearOp(opValues);

			case MAP_DECREMENT:
				return mapToMapDecrementOp(opValues);

			case MAP_GET_BY_INDEX:
				return mapToMapGetByIndexOp(opValues);

			case MAP_GET_BY_INDEX_RANGE:
				return mapToMapGetByIndexRangeOp(opValues);

			case MAP_GET_BY_KEY:
				return mapToMapGetByKeyOp(opValues);

			case MAP_GET_BY_KEY_LIST:
				return mapToMapGetByKeyListOp(opValues);

			case MAP_GET_BY_KEY_RANGE:
				return mapToMapGetByKeyRangeOp(opValues);

			case MAP_GET_BY_RANK:
				return mapToMapGetByRankOp(opValues);

			case MAP_GET_BY_RANK_RANGE:
				return mapToMapGetByRankRangeOp(opValues);

			case MAP_GET_BY_VALUE:
				return mapToMapGetByValueOp(opValues);

			case MAP_GET_BY_VALUE_RANGE:
				return mapToMapGetByValueRangeOp(opValues);

			case MAP_GET_BY_VALUE_LIST:
				return mapToMapGetByValueListOp(opValues);

			case MAP_GET_BY_KEY_REL_INDEX_RANGE:
				return mapToMapGetByKeyRelIndexRangeOp(opValues);

			case MAP_GET_BY_VALUE_REL_RANK_RANGE:
				return mapToMapGetByValueRelRankRangeOp(opValues);

			case MAP_INCREMENT:
				return mapToMapIncrementOp(opValues);

			case MAP_PUT:
				return mapToMapPutOp(opValues);

			case MAP_PUT_ITEMS:
				return mapToMapPutItemsOp(opValues);

			case MAP_REMOVE_BY_INDEX:
				return mapToMapRemoveByIndexOp(opValues);

			case MAP_REMOVE_BY_INDEX_RANGE:
				return mapToMapRemoveByIndexRangeOp(opValues);

			case MAP_REMOVE_BY_KEY:
				return mapToMapRemoveByKeyOp(opValues);

			case MAP_REMOVE_BY_KEY_RANGE:
				return mapToMapRemoveByKeyRangeOp(opValues);

			case MAP_REMOVE_BY_RANK:
				return mapToMapRemoveByRankOp(opValues);

			case MAP_REMOVE_BY_RANK_RANGE:
				return mapToMapRemoveByRankRangeOp(opValues);

			case MAP_REMOVE_BY_KEY_REL_INDEX_RANGE:
				return mapToMapRemoveByKeyRelIndexRangeOp(opValues);

			case MAP_REMOVE_BY_VALUE_REL_RANK_RANGE:
				return mapToMapRemoveByValueRelRankRangeOp(opValues);

			case MAP_REMOVE_BY_VALUE:
				return mapToMapRemoveByValueOp(opValues);

			case MAP_REMOVE_BY_VALUE_RANGE:
				return mapToMapRemoveByValueRangeOp(opValues);

			case MAP_REMOVE_BY_VALUE_LIST:
				return mapToMapRemoveByValueListOp(opValues);

			case MAP_SET_MAP_POLICY:
				return mapToMapSetMapPolicyOp(opValues);

			case MAP_SIZE:
				return mapToMapSizeOp(opValues);

			default:
				throw new InvalidOperationError("Invalid operation: " + opName);
		}
	}

	private static Operation mapToAddOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, INCR_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, INCR_KEY);

		String binName = getBinName(opValues);
		Value incr = getIncr(opValues);
		Bin bin = new Bin(binName, incr);

		return Operation.add(bin);
	}

	private static Operation mapToAppendOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, VALUE_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, VALUE_KEY);

		String binName = getBinName(opValues);
		Value value = getValue(opValues);
		Bin bin = new Bin(binName, value);

		return Operation.append(bin);
	}

	private static Operation mapToGetOp(Map<String, Object> opValues) {
		// Make sure no options were passed to this operation
		validateNoKeys(opValues);

		return Operation.get();
	}

	private static Operation mapToPrependOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, VALUE_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, VALUE_KEY);

		String binName = getBinName(opValues);
		Value value = getValue(opValues);
		Bin bin = new Bin(binName, value);

		return Operation.prepend(bin);
	}

	private static Operation mapToReadOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY);

		String binName = getBinName(opValues);

		return Operation.get(binName);
	}

	private static Operation mapToGetHeaderOp(Map<String, Object> opValues) {
		validateNoKeys(opValues);

		return Operation.getHeader();
	}

	private static Operation mapToTouchOp(Map<String, Object> opValues) {
		validateNoKeys(opValues);

		return Operation.touch();
	}

	private static Operation mapToPutOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, VALUE_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, VALUE_KEY);

		String binName = getBinName(opValues);
		Value value = getValue(opValues);
		Bin bin = new Bin(binName, value);

		return Operation.put(bin);
	}

	private static Operation mapToDeleteOp(Map<String, Object> opValues) {
		validateNoKeys(opValues);

		return Operation.delete();
	}

	/* LIST OPERATIONS */

	private static Operation mapToListAppendOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, VALUE_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, VALUE_KEY);

		String binName = getBinName(opValues);
		Value value = getValue(opValues);

		return ListOperation.append(binName, value);
	}

	private static Operation mapToListAppendItemsOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, VALUES_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, VALUES_KEY, LIST_POLICY_KEY);

		String binName = getBinName(opValues);
		List<Value> valueList = getValueList(opValues);
		ListPolicy policy = getListPolicy(opValues);

		if (policy != null) {
			return ListOperation.appendItems(policy, binName, valueList);
		} else {
			return ListOperation.appendItems(binName, valueList);
		}
	}

	private static Operation mapToListClearOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY);

		String binName = getBinName(opValues);

		return ListOperation.clear(binName);
	}

	private static Operation mapToListGetOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, INDEX_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, INDEX_KEY);

		String binName = getBinName(opValues);
		int index = getIndex(opValues);

		return ListOperation.get(binName, index);
	}

	private static Operation mapToListGetByIndexOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, INDEX_KEY, LIST_RETURN_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, INDEX_KEY, LIST_RETURN_KEY);

		String binName = getBinName(opValues);
		int index = getIndex(opValues);
		int returnType = getListReturnType(opValues);

		return ListOperation.getByIndex(binName, index, returnType);
	}

	private static Operation mapToListGetByIndexRangeOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, INDEX_KEY, LIST_RETURN_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, INDEX_KEY, LIST_RETURN_KEY, COUNT_KEY);

		String binName = getBinName(opValues);
		int index = getIndex(opValues);
		Integer count = getCount(opValues);
		int returnType = getListReturnType(opValues);

		if (count != null) {
			return ListOperation.getByIndexRange(binName, index, count, returnType);
		} else {
			return ListOperation.getByIndexRange(binName, index, returnType);
		}
	}

	private static Operation mapToListGetByRankOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, RANK_KEY, LIST_RETURN_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, RANK_KEY, LIST_RETURN_KEY);

		String binName = getBinName(opValues);
		int rank = getRank(opValues);
		int returnType = getListReturnType(opValues);

		return ListOperation.getByRank(binName, rank, returnType);
	}

	private static Operation mapToListGetByRankRangeOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, RANK_KEY, LIST_RETURN_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, RANK_KEY, LIST_RETURN_KEY, COUNT_KEY);

		String binName = getBinName(opValues);
		int rank = getRank(opValues);
		Integer count = getCount(opValues);
		int returnType = getListReturnType(opValues);

		if (count != null) {
			return ListOperation.getByRankRange(binName, rank, count, returnType);
		} else {
			return ListOperation.getByRankRange(binName, rank, returnType);
		}
	}

	private static Operation mapToListGetByValueRelRankRangeOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, RANK_KEY, VALUE_KEY, LIST_RETURN_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, RANK_KEY, VALUE_KEY, LIST_RETURN_KEY, COUNT_KEY);

		String binName = getBinName(opValues);
		Value value = getValue(opValues);
		int rank = getRank(opValues);
		Integer count = getCount(opValues);
		int returnType = getListReturnType(opValues);

		if (count != null) {
			return ListOperation.getByValueRelativeRankRange(binName, value, rank, count, returnType);
		} else {
			return ListOperation.getByValueRelativeRankRange(binName, value, rank, returnType);
		}
	}

	private static Operation mapToListGetByValueOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, VALUE_KEY, LIST_RETURN_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, VALUE_KEY, LIST_RETURN_KEY);

		String binName = getBinName(opValues);
		Value value = getValue(opValues);
		int returnType = getListReturnType(opValues);

		return ListOperation.getByValue(binName, value, returnType);
	}

	private static Operation mapToListGetByValueRangeOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, LIST_RETURN_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, LIST_RETURN_KEY, VALUE_BEGIN_KEY, VALUE_END_KEY);

		String binName = getBinName(opValues);
		Value valueBegin = getValueBegin(opValues);
		Value valueEnd = getValueEnd(opValues);
		int returnType = getListReturnType(opValues);

		return ListOperation.getByValueRange(binName, valueBegin, valueEnd, returnType);

	}

	private static Operation mapToListGetByValueListOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, VALUES_KEY, LIST_RETURN_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, VALUES_KEY, LIST_RETURN_KEY);

		String binName = getBinName(opValues);
		List<Value>values = getValueList(opValues);
		int returnType = getListReturnType(opValues);

		return ListOperation.getByValueList(binName, values, returnType);
	}

	private static Operation mapToListGetRangeOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, INDEX_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, INDEX_KEY, COUNT_KEY);

		String binName = getBinName(opValues);
		int index = getIndex(opValues);
		Integer count = getCount(opValues);

		if (count != null) {
			return ListOperation.getRange(binName, index, count);
		} else {
			return ListOperation.getRange(binName, index);
		}
	}

	private static Operation mapToListIncrementOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, INDEX_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, INDEX_KEY, INCR_KEY, LIST_POLICY_KEY);

		String binName = getBinName(opValues);
		int index = getIndex(opValues);
		Value incr = getOptionalIncr(opValues);
		ListPolicy policy = getListPolicy(opValues);

		if (policy != null) {
			if (incr != null) {
				return ListOperation.increment(policy, binName, index, incr);
			} else {
				return ListOperation.increment(policy, binName, index);
			}
		} else {
			if (incr != null) {
				return ListOperation.increment(binName, index, incr);
			} else {
				return ListOperation.increment(binName, index);
			}
		}
	}

	private static Operation mapToListInsertOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, INDEX_KEY, VALUE_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, INDEX_KEY, VALUE_KEY, LIST_POLICY_KEY);

		String binName = getBinName(opValues);
		int index = getIndex(opValues);
		Value value = getValue(opValues);
		ListPolicy policy = getListPolicy(opValues);

		if (policy == null) {
			return ListOperation.insert(binName, index, value);
		} else {
			return ListOperation.insert(policy, binName, index, value);
		}
	}

	private static Operation mapToListInsertItemsOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, INDEX_KEY, VALUES_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, INDEX_KEY, VALUES_KEY, LIST_POLICY_KEY);

		String binName = getBinName(opValues);
		int index = getIndex(opValues);
		List<Value> values = getValueList(opValues);
		ListPolicy policy = getListPolicy(opValues);

		if (policy == null) {
			return ListOperation.insertItems(binName, index, values);
		} else {
			return ListOperation.insertItems(policy, binName, index, values);
		}
	}

	private static Operation mapToListPopOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, INDEX_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, INDEX_KEY);

		String binName = getBinName(opValues);
		int index = getIndex(opValues);

		return ListOperation.pop(binName, index);
	}

	private static Operation mapToListPopRangeOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, INDEX_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, INDEX_KEY, COUNT_KEY);

		String binName = getBinName(opValues);
		int index = getIndex(opValues);
		Integer count = getCount(opValues);

		if (count != null) {
			return ListOperation.popRange(binName, index, count);
		} else {
			return ListOperation.popRange(binName, index);
		}
	}

	private static Operation mapToListRemoveOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, INDEX_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, INDEX_KEY);

		String binName = getBinName(opValues);
		int index = getIndex(opValues);

		return ListOperation.remove(binName, index);
	}

	private static Operation mapToListRemoveByIndexOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, INDEX_KEY, LIST_RETURN_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, INDEX_KEY, LIST_RETURN_KEY);

		String binName = getBinName(opValues);
		int index = getIndex(opValues);
		int returnType = getListReturnType(opValues);

		return ListOperation.removeByIndex(binName, index, returnType);
	}

	private static Operation mapToListRemoveByIndexRangeOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, INDEX_KEY, LIST_RETURN_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, INDEX_KEY, LIST_RETURN_KEY, COUNT_KEY);

		String binName = getBinName(opValues);
		int index = getIndex(opValues);
		Integer count = getCount(opValues);
		int returnType = getListReturnType(opValues);

		if (count != null) {
			return ListOperation.removeByIndexRange(binName, index, count, returnType);
		}
		return ListOperation.removeByIndexRange(binName, index, returnType);
	}

	private static Operation mapToListRemoveByRankOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, RANK_KEY, LIST_RETURN_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, RANK_KEY, LIST_RETURN_KEY);

		String binName = getBinName(opValues);
		int rank = getRank(opValues);
		int returnType = getListReturnType(opValues);

		return ListOperation.removeByRank(binName, rank, returnType);
	}

	private static Operation mapToListRemoveByRankRangeOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, RANK_KEY, LIST_RETURN_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, RANK_KEY, LIST_RETURN_KEY, COUNT_KEY);

		String binName = getBinName(opValues);
		int rank = getRank(opValues);
		int returnType = getListReturnType(opValues);
		Integer count = getCount(opValues);

		if (count != null) {
			return ListOperation.removeByRankRange(binName, rank, count, returnType);
		}
		return ListOperation.removeByRankRange(binName, rank, returnType);
	}

	private static Operation mapToListRemoveByValueRelRankRangeOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, RANK_KEY, VALUE_KEY, LIST_RETURN_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, RANK_KEY, VALUE_KEY, LIST_RETURN_KEY, COUNT_KEY);

		String binName = getBinName(opValues);
		Value value = getValue(opValues);
		int rank = getRank(opValues);
		int returnType = getListReturnType(opValues);
		Integer count = getCount(opValues);

		if (count != null) {
			return ListOperation.removeByValueRelativeRankRange(binName, value, rank, count, returnType);
		}
		return ListOperation.removeByValueRelativeRankRange(binName, value, rank, returnType);
	}

		private static Operation mapToListRemoveByValueOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, VALUE_KEY, LIST_RETURN_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, VALUE_KEY, LIST_RETURN_KEY);

		String binName = getBinName(opValues);
		Value value = getValue(opValues);
		int returnType = getListReturnType(opValues);

		return ListOperation.removeByValue(binName, value, returnType);
	}

	private static Operation mapToListRemoveByValueRangeOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, LIST_RETURN_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, LIST_RETURN_KEY, VALUE_BEGIN_KEY, VALUE_END_KEY);

		String binName = getBinName(opValues);
		Value valueBegin = getValueBegin(opValues);
		Value valueEnd = getValueEnd(opValues);
		int returnType = getListReturnType(opValues);

		return ListOperation.removeByValueRange(binName, valueBegin, valueEnd, returnType);
	}

	private static Operation mapToListRemoveByValueListOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, VALUES_KEY, LIST_RETURN_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, VALUES_KEY, LIST_RETURN_KEY);

		String binName = getBinName(opValues);
		List<Value> values = getValueList(opValues);
		int returnType = getListReturnType(opValues);

		return ListOperation.removeByValueList(binName, values, returnType);
	}

	private static Operation mapToListRemoveRangeOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, INDEX_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, INDEX_KEY, COUNT_KEY);

		String binName = getBinName(opValues);
		int index = getIndex(opValues);
		Integer count = getCount(opValues);

		if (count != null) {
			return ListOperation.removeRange(binName, index, count);
		}
		return ListOperation.removeRange(binName, index);
	}

	private static Operation mapToListSetOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, INDEX_KEY, VALUE_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, INDEX_KEY, VALUE_KEY, LIST_POLICY_KEY);

		String binName = getBinName(opValues);
		Value value = getValue(opValues);
		ListPolicy policy = getListPolicy(opValues);
		int index = getIndex(opValues);

		if (policy != null) {
			return ListOperation.set(policy, binName, index, value);
		}
		return ListOperation.set(binName, index, value);
	}

	private static Operation mapToListSetOrderOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, LIST_ORDER_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, LIST_ORDER_KEY);

		String binName = getBinName(opValues);
		ListOrder order = getListOrder(opValues);

		return ListOperation.setOrder(binName, order);
	}

	private static Operation mapToListSizeOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY);

		String binName = getBinName(opValues);

		return ListOperation.size(binName);
	}

	private static Operation mapToListSortOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, LIST_SORT_FLAGS_KEY);

		String binName = getBinName(opValues);
		int sortFlags = getSortFlags(opValues);

		return ListOperation.sort(binName, sortFlags);
	}

	private static Operation mapToListTrimOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, INDEX_KEY, COUNT_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, INDEX_KEY, COUNT_KEY);

		String binName = getBinName(opValues);
		int index = getIndex(opValues);
		int count = getCount(opValues);

		return ListOperation.trim(binName, index, count);
	}

	/* MAP OPERATIONS */

	private static Operation mapToMapClearOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY);

		String binName = getBinName(opValues);

		return MapOperation.clear(binName);
	}

	private static Operation mapToMapDecrementOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, DECR_KEY, MAP_KEY_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, MAP_POLICY_KEY, DECR_KEY, MAP_KEY_KEY);

		String binName = getBinName(opValues);
		MapPolicy policy = getMapPolicy(opValues);
		Value decr = getDecr(opValues);
		Value key = getMapKey(opValues);

		return MapOperation.decrement(policy, binName, key, Value.get(decr));
	}

	private static Operation mapToMapGetByIndexOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, INDEX_KEY, MAP_RETURN_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, INDEX_KEY, MAP_RETURN_KEY);

		String binName = getBinName(opValues);
		int returnType = getMapReturnType(opValues);
		int index = getIndex(opValues);

		return MapOperation.getByIndex(binName, index, returnType);
	}

	private static Operation mapToMapGetByIndexRangeOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, INDEX_KEY, MAP_RETURN_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, INDEX_KEY, MAP_RETURN_KEY, COUNT_KEY);

		String binName = getBinName(opValues);
		int returnType = getMapReturnType(opValues);
		int index = getIndex(opValues);
		Integer count = getCount(opValues);
		if (count == null) {
			return MapOperation.getByIndexRange(binName, index, returnType);
		}
		return MapOperation.getByIndexRange(binName, index, count, returnType);
	}

	private static Operation mapToMapGetByKeyOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, MAP_KEY_KEY, MAP_RETURN_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, MAP_KEY_KEY, MAP_RETURN_KEY);

		String binName = getBinName(opValues);
		int returnType = getMapReturnType(opValues);
		Value key = getMapKey(opValues);

		return MapOperation.getByKey(binName, key, returnType);
	}

	private static Operation mapToMapGetByKeyListOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, MAP_KEYS_KEY, MAP_RETURN_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, MAP_KEYS_KEY, MAP_RETURN_KEY);

		String binName = getBinName(opValues);
		int returnType = getMapReturnType(opValues);
		List<Value> keys = getMapKeys(opValues);

		return MapOperation.getByKeyList(binName, keys, returnType);
	}

	private static Operation mapToMapGetByKeyRangeOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, MAP_RETURN_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, MAP_KEY_BEGIN_KEY, MAP_KEY_END_KEY, MAP_RETURN_KEY);

		String binName = getBinName(opValues);
		int returnType = getMapReturnType(opValues);
		Value keyBegin = getMapKeyBegin(opValues);
		Value keyEnd = getMapKeyEnd(opValues);

		return MapOperation.getByKeyRange(binName, keyBegin, keyEnd, returnType);
	}

	private static Operation mapToMapGetByRankOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, RANK_KEY, MAP_RETURN_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, RANK_KEY, MAP_RETURN_KEY);

		String binName = getBinName(opValues);
		int rank = getRank(opValues);
		int returnType = getMapReturnType(opValues);
		return MapOperation.getByRank(binName, rank, returnType);
	}

	private static Operation mapToMapGetByRankRangeOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, RANK_KEY, MAP_RETURN_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, RANK_KEY, COUNT_KEY, MAP_RETURN_KEY);

		String binName = getBinName(opValues);
		int rank = getRank(opValues);
		Integer count = getCount(opValues);
		int returnType = getMapReturnType(opValues);

		if (count != null) {
			return MapOperation.getByRankRange(binName, rank, count, returnType);
		}
		return MapOperation.getByRankRange(binName, rank, returnType);
	}

	private static Operation mapToMapGetByValueOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, VALUE_KEY, MAP_RETURN_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, VALUE_KEY, MAP_RETURN_KEY);

		String binName = getBinName(opValues);
		Value value = getValue(opValues);
		int returnType = getMapReturnType(opValues);
		return MapOperation.getByValue(binName, value, returnType);
	}

	private static Operation mapToMapGetByValueRangeOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, MAP_RETURN_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, VALUE_BEGIN_KEY, VALUE_END_KEY, MAP_RETURN_KEY);

		String binName = getBinName(opValues);
		Value valueBegin = getValueBegin(opValues);
		Value valueEnd = getValueEnd(opValues);
		int returnType = getMapReturnType(opValues);

		return MapOperation.getByValueRange(binName, valueBegin, valueEnd, returnType);
	}

	private static Operation mapToMapGetByValueListOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, VALUES_KEY, MAP_RETURN_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, VALUES_KEY, MAP_RETURN_KEY);

		String binName = getBinName(opValues);
		List<Value> values = getValueList(opValues);
		int returnType = getMapReturnType(opValues);

		return MapOperation.getByValueList(binName, values, returnType);
	}

	private static Operation mapToMapGetByKeyRelIndexRangeOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, INDEX_KEY, VALUE_KEY, MAP_RETURN_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, INDEX_KEY, VALUE_KEY, MAP_RETURN_KEY, COUNT_KEY);

		String binName = getBinName(opValues);
		Value value = getValue(opValues);
		int returnType = getMapReturnType(opValues);
		int index = getIndex(opValues);
		Integer count = getCount(opValues);

		if (count == null) {
			return MapOperation.getByKeyRelativeIndexRange(binName, value, index, returnType);
		}
		return MapOperation.getByKeyRelativeIndexRange(binName, value, index, count, returnType);
	}

	private static Operation mapToMapGetByValueRelRankRangeOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, RANK_KEY, VALUE_KEY, MAP_RETURN_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, RANK_KEY, VALUE_KEY, COUNT_KEY, MAP_RETURN_KEY);

		String binName = getBinName(opValues);
		Value value = getValue(opValues);
		int rank = getRank(opValues);
		Integer count = getCount(opValues);
		int returnType = getMapReturnType(opValues);

		if (count != null) {
			return MapOperation.getByValueRelativeRankRange(binName, value, rank, count, returnType);
		}
		return MapOperation.getByValueRelativeRankRange(binName, value, rank, returnType);
	}

		private static Operation mapToMapIncrementOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, INCR_KEY, MAP_KEY_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, INCR_KEY, MAP_POLICY_KEY, MAP_KEY_KEY);

		String binName = getBinName(opValues);
		MapPolicy policy = getMapPolicy(opValues);
		Value incr = getIncr(opValues);
		Value key = getMapKey(opValues);

		return MapOperation.increment(policy, binName, key, incr);
	}

	private static Operation mapToMapPutOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, VALUE_KEY, MAP_KEY_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, VALUE_KEY, MAP_POLICY_KEY, MAP_KEY_KEY);

		MapPolicy policy = getMapPolicy(opValues);
		String binName = getBinName(opValues);
		Value value = getValue(opValues);
		Value key = getMapKey(opValues);

		return MapOperation.put(policy, binName, key, value);
	}

	private static Operation mapToMapPutItemsOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, MAP_VALUES_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, MAP_VALUES_KEY, MAP_POLICY_KEY);

		String binName = getBinName(opValues);
		MapPolicy policy = getMapPolicy(opValues);
		Map<Value, Value>putItems = getMapValues(opValues);

		return MapOperation.putItems(policy, binName, putItems);
	}

	private static Operation mapToMapRemoveByIndexOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, INDEX_KEY, MAP_RETURN_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, INDEX_KEY, MAP_RETURN_KEY);

		String binName = getBinName(opValues);
		int returnType = getMapReturnType(opValues);
		int index = getIndex(opValues);

		return MapOperation.removeByIndex(binName, index, returnType);
	}

	private static Operation mapToMapRemoveByIndexRangeOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, INDEX_KEY, MAP_RETURN_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, INDEX_KEY, COUNT_KEY, MAP_RETURN_KEY);

		String binName = getBinName(opValues);
		int returnType = getMapReturnType(opValues);
		int index = getIndex(opValues);
		Integer count = getCount(opValues);

		if (count != null) {
			return MapOperation.removeByIndexRange(binName, index, count, returnType);
		}
		return MapOperation.removeByIndexRange(binName, index, returnType);
	}

	private static Operation mapToMapRemoveByKeyOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, MAP_KEY_KEY, MAP_RETURN_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, MAP_KEY_KEY, MAP_RETURN_KEY);

		String binName = getBinName(opValues);
		int returnType = getMapReturnType(opValues);
		Value key = getMapKey(opValues);

		return MapOperation.removeByKey(binName, key, returnType);
	}

	private static Operation mapToMapRemoveByKeyRangeOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, MAP_RETURN_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, MAP_KEY_BEGIN_KEY, MAP_KEY_END_KEY, MAP_RETURN_KEY);

		String binName = getBinName(opValues);
		int returnType = getMapReturnType(opValues);
		Value keyBegin = getMapKeyBegin(opValues);
		Value keyEnd = getMapKeyEnd(opValues);

		return MapOperation.removeByKeyRange(binName, keyBegin, keyEnd, returnType);
	}

	private static Operation mapToMapRemoveByRankOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, RANK_KEY, MAP_RETURN_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, RANK_KEY, MAP_RETURN_KEY);

		String binName = getBinName(opValues);
		int rank = getRank(opValues);
		int returnType = getMapReturnType(opValues);

		return MapOperation.removeByRank(binName, rank, returnType);
	}

	private static Operation mapToMapRemoveByRankRangeOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, RANK_KEY, MAP_RETURN_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, RANK_KEY, COUNT_KEY, MAP_RETURN_KEY);

		String binName = getBinName(opValues);
		int rank = getRank(opValues);
		Integer count = getCount(opValues);
		int returnType = getMapReturnType(opValues);

		if (count != null ) {
			return MapOperation.removeByRankRange(binName, rank, count, returnType);
		}
		return MapOperation.removeByRankRange(binName, rank, returnType);
	}

	private static Operation mapToMapRemoveByKeyRelIndexRangeOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, INDEX_KEY, VALUE_KEY, MAP_RETURN_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, INDEX_KEY, VALUE_KEY, COUNT_KEY, MAP_RETURN_KEY);

		String binName = getBinName(opValues);
		Value value = getValue(opValues);
		int returnType = getMapReturnType(opValues);
		int index = getIndex(opValues);
		Integer count = getCount(opValues);

		if (count != null) {
			return MapOperation.removeByKeyRelativeIndexRange(binName, value, index, count, returnType);
		}
		return MapOperation.removeByKeyRelativeIndexRange(binName, value, index, returnType);
	}

	private static Operation mapToMapRemoveByValueRelRankRangeOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, RANK_KEY, VALUE_KEY, MAP_RETURN_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, RANK_KEY, VALUE_KEY, COUNT_KEY, MAP_RETURN_KEY);

		String binName = getBinName(opValues);
		Value value = getValue(opValues);
		int rank = getRank(opValues);
		Integer count = getCount(opValues);
		int returnType = getMapReturnType(opValues);

		if (count != null ) {
			return MapOperation.removeByValueRelativeRankRange(binName, value, rank, count, returnType);
		}
		return MapOperation.removeByValueRelativeRankRange(binName, value, rank, returnType);
	}

		private static Operation mapToMapRemoveByValueOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, VALUE_KEY, MAP_RETURN_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, VALUE_KEY, MAP_RETURN_KEY);

		String binName = getBinName(opValues);
		Value value = getValue(opValues);
		int returnType = getMapReturnType(opValues);
		return MapOperation.removeByValue(binName, value, returnType);
	}

	private static Operation mapToMapRemoveByValueRangeOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, MAP_RETURN_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, VALUE_BEGIN_KEY, VALUE_END_KEY, MAP_RETURN_KEY);

		String binName = getBinName(opValues);
		Value valueBegin = getValueBegin(opValues);
		Value valueEnd = getValueEnd(opValues);
		int returnType = getMapReturnType(opValues);

		return MapOperation.removeByValueRange(binName, valueBegin, valueEnd, returnType);
	}

	private static Operation mapToMapRemoveByValueListOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, VALUES_KEY, MAP_RETURN_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, VALUES_KEY, MAP_RETURN_KEY);

		String binName = getBinName(opValues);
		List<Value> values = getValueList(opValues);
		int returnType = getMapReturnType(opValues);

		return MapOperation.removeByValueList(binName, values, returnType);
	}

	private static Operation mapToMapSetMapPolicyOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY, MAP_POLICY_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY, MAP_POLICY_KEY);

		String binName = getBinName(opValues);
		MapPolicy policy = getMapPolicy(opValues);

		return MapOperation.setMapPolicy(policy, binName);
	}

	private static Operation mapToMapSizeOp(Map<String, Object> opValues) {
		hasAllRequiredKeys(opValues, BIN_KEY);
		onlyHasAllowedKeys(opValues, BIN_KEY);

		String binName = getBinName(opValues);

		return MapOperation.size(binName);
	}

	/*
	 * Ensure that opValues contains an entry for each of the specified keys
	 */
	private static void hasAllRequiredKeys(Map<String, Object> opValues, String ...requiredKeys) {
		if (opValues == null) {
			throw new InvalidOperationError("\"opValues\" entry must be provided.");
		}
		for (String requiredKey: requiredKeys) {
			if (!opValues.containsKey(requiredKey)) {
				throw new InvalidOperationError("Missing required key: " + requiredKey);
			}
		}
	}

	/*
	 * Ensure that opValues does not contain any keys not contained in allowedKeys.
	 */
	private static void onlyHasAllowedKeys(Map<String, Object> opValues, String ...allowedKeys) {
		Set<String> allowedKeySet = new HashSet<String>(Arrays.asList(allowedKeys));

		for (String providedKey: opValues.keySet()) {
			if (!allowedKeySet.contains(providedKey)) {
				throw new InvalidOperationError("Illegal key for operation: " + providedKey);
			}
		}
	}

	/*
	 * Convenience function for operation converters which allow no args.
	 */

	private static void validateNoKeys(Map<String, Object> opValues) {
		onlyHasAllowedKeys(opValues);
	}

	@SuppressWarnings("unchecked")
	private static ListPolicy mapToListPolicy(Map<String, Object>mapPolicy) {
		ListOrder order;
		try {
			order = ListOrder.valueOf((String) mapPolicy.get(ORDER_KEY));
		} catch (ClassCastException cce) {
			throw new InvalidOperationError("order must be a string");
		} catch (IllegalArgumentException e) {
			throw new InvalidOperationError("Invalid List Order");
		}
		int flags = 0;
		if (mapPolicy.containsKey(WRITE_FLAGS_KEY)) {
			try {
				for (String flagName: (List<String>) mapPolicy.get(WRITE_FLAGS_KEY)) {
					if (flagName.equals("ADD_UNIQUE")) {
						flags |= ListWriteFlags.ADD_UNIQUE;
					} else if (flagName.equals("INSERT_BOUNDED")) {
						flags |= ListWriteFlags.INSERT_BOUNDED;
					}
				}
			} catch (ClassCastException cce) {
				throw new InvalidOperationError("writeFlags must be a list of strings");
			}
		}
		return new ListPolicy(order, flags);

	}

	static Value getValue(Map<String, Object>map) {
		Object val = map.get(VALUE_KEY);
		return val == null ? null : Value.get(val);
	}

	static Value getValueBegin(Map<String, Object>map) {
		if (map.containsKey(VALUE_BEGIN_KEY)) {
			return Value.get(map.get(VALUE_BEGIN_KEY));
		} else {
			return null;
		}
	}

	static Value getValueEnd(Map<String, Object>map) {
		if (map.containsKey(VALUE_END_KEY)) {
			return Value.get(map.get(VALUE_END_KEY));
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	static List<Value> getValueList(Map<String, Object>map) {
		List<Object>values = null;
		try {
			values = (List<Object>) map.get(VALUES_KEY);
		} catch (ClassCastException cce) {
			throw new InvalidOperationError("values must be a list");
		}
		List<Value>valueList = new ArrayList<Value>();
		for (Object value : values) {
			valueList.add(Value.get(value));
		}
		return valueList;
	}

	@SuppressWarnings("unchecked")
	static ListPolicy getListPolicy(Map<String, Object>map) {
		if (!map.containsKey(LIST_POLICY_KEY)) {
			return null;
		}
		try {
			return mapToListPolicy((Map<String, Object>) map.get(LIST_POLICY_KEY));
		} catch (ClassCastException cce) {
			throw new InvalidOperationError("List Policy must be a map");
		}
	}

	static String getBinName(Map<String, Object>map) {
		try {
			return (String)map.get(BIN_KEY);
		} catch (ClassCastException cce) {
			throw new InvalidOperationError("bin must be string");
		}
	}

	static Value getIncr(Map<String, Object>map) {
		return Value.get(map.get(INCR_KEY));
	}

	static Value getOptionalIncr(Map<String, Object>map) {
		if (map.containsKey(INCR_KEY)) {
			return Value.get(map.get(INCR_KEY));
		}
		return null;
	}

	static int getIndex(Map<String, Object>map) {
		try {
			return getIntValue(map, INDEX_KEY);
		} catch (ClassCastException cce) {
			throw new InvalidOperationError("index must be an integer");
		}
	}

	static int getRank(Map<String, Object>map) {
		try {
			return getIntValue(map, RANK_KEY);
		} catch (ClassCastException cce) {
			throw new InvalidOperationError("rank must be an integer");
		}
	}

	static Integer getCount(Map<String, Object>map) {
		if (map.containsKey((COUNT_KEY))) {
			try {
				return getIntValue(map, COUNT_KEY);
			} catch (ClassCastException cce) {
				throw new InvalidOperationError("count must be an integer");
			}
		}
		return null;
	}

	static int getSortFlags(Map<String, Object>map) {
		String flagString = null;
		try {
			flagString = (String)map.get(LIST_SORT_FLAGS_KEY);
		} catch (ClassCastException cce) {
			throw new InvalidOperationError("listSortFlags must be a string");
		}
		if (flagString != null && flagString.equals("DROP_DUPLICATES")) {
			return ListSortFlags.DROP_DUPLICATES;
		}

		return ListSortFlags.DEFAULT;
	}
	/*
	 * Get an integer suitable for passing as a list return type
	 * this is the value of the LIST_RETURN_KEY field bitwise or'd with the value of the INVERTED_KEY field
	 */
	static int getListReturnType(Map<String, Object>map) {
		int inverted = getInverted(map);
		String returnTypeString = null;
		try {
			returnTypeString = (String)map.get(LIST_RETURN_KEY);
		} catch (ClassCastException cce) {
			throw new InvalidOperationError("listReturnType must be a string");
		}

		switch(returnTypeString) {
			case "COUNT":
				return ListReturnType.COUNT | inverted;
			case "INDEX":
				return ListReturnType.INDEX | inverted;
			case "NONE":
				return ListReturnType.NONE | inverted;
			case "RANK":
				return ListReturnType.RANK | inverted;
			case "REVERSE_INDEX":
				return ListReturnType.REVERSE_INDEX | inverted;
			case "REVERSE_RANK":
				return ListReturnType.REVERSE_RANK | inverted;
			case "VALUE":
				return ListReturnType.VALUE | inverted;
			default:
				throw new InvalidOperationError("Invalid listReturnType: "+ returnTypeString);
		}
	}

	/*
	 * Get an integer suitable for passing as a list return type
	 * this is the value of the LIST_RETURN_KEY field bitwise or'd with the value of the INVERTED_KEY field
	 */
	static int getMapReturnType(Map<String, Object>map) {
		String returnTypeString = null;
		int inverted = getInverted(map);
		try {
			returnTypeString = (String)map.get(MAP_RETURN_KEY);
		} catch (ClassCastException cce) {
			throw new InvalidOperationError("mapReturnType must be a string");
		}

		switch(returnTypeString) {
			case "COUNT":
				return MapReturnType.COUNT | inverted;
			case "INDEX":
				return MapReturnType.INDEX | inverted;
			case "KEY":
				return MapReturnType.KEY | inverted;
			case "KEY_VALUE":
				return MapReturnType.KEY_VALUE | inverted;
			case "NONE":
				return MapReturnType.NONE | inverted;
			case "RANK":
				return MapReturnType.RANK | inverted;
			case "REVERSE_INDEX":
				return MapReturnType.REVERSE_INDEX | inverted;
			case "REVERSE_RANK":
				return MapReturnType.REVERSE_RANK | inverted;
			case "VALUE":
				return MapReturnType.VALUE | inverted;
			default:
				throw new InvalidOperationError("Invalid mapReturnType: "+ returnTypeString);
		}
	}

	static int getInverted(Map<String, Object>map) {
		boolean inverted = false;
		if (map.containsKey(INVERTED_KEY)) {
			try {
				inverted = (boolean)map.get(INVERTED_KEY);
			} catch (ClassCastException cce) {
				throw new InvalidOperationError("inverted must be a boolean value");
			}
			return inverted ? ListReturnType.INVERTED : 0;
		}
		return 0;
	}

	static ListOrder getListOrder(Map<String, Object>map) {
		String orderString = null;
		try {
			orderString = (String)map.get(LIST_ORDER_KEY);
		} catch (ClassCastException cce) {
			throw new InvalidOperationError("listOrder must be a string");
		}
		try {
			return ListOrder.valueOf(orderString);
		} catch (IllegalArgumentException e) {
			throw new InvalidOperationError("Invalid List order: "+ orderString);
		}

	}

	@SuppressWarnings("unchecked")
	private static MapPolicy getMapPolicy(Map<String, Object>opValues) {
		if (!opValues.containsKey(MAP_POLICY_KEY)) {
			return MapPolicy.Default;
		}
		try {
			return mapToMapPolicy((Map<String, Object>) opValues.get(MAP_POLICY_KEY));
		} catch (ClassCastException cce) {
			throw new InvalidOperationError("mapPolicy must be an object");
		}
	}

	static Value getDecr(Map<String, Object>map) {
		return Value.get(map.get(DECR_KEY));
	}

	static Value getMapKey(Map<String, Object>map) {
		return Value.get(map.get(MAP_KEY_KEY));
	}

	@SuppressWarnings("unchecked")
	static Map<Value, Value> getMapValues(Map<String, Object>map) {
		Map<Value, Value> valueMap = new HashMap<Value, Value>();
		Map<Object, Object>objMap = null;
		try {
			objMap = (Map<Object, Object>)map.get(MAP_VALUES_KEY);
		} catch (ClassCastException cce) {
			throw new InvalidOperationError("map must be an object");
		}
		for (Object key : objMap.keySet()) {
			valueMap.put(Value.get(key), Value.get(objMap.get(key)));
		}

		return valueMap;
	}
	static Value getMapKeyBegin(Map<String, Object>map) {
		Object beginVal = map.get(MAP_KEY_BEGIN_KEY);
		return beginVal == null ? null : Value.get(beginVal);
	}

	static Value getMapKeyEnd(Map<String, Object>map) {
		Object endVal = map.get(MAP_KEY_END_KEY);
		return endVal == null ? null : Value.get(endVal);
	}

	@SuppressWarnings("unchecked")
	static List<Value> getMapKeys(Map<String, Object>map) {
		List<Object>objList = null;
		try {
			objList = (List<Object>)map.get(MAP_KEYS_KEY);
		} catch (ClassCastException cce) {
			throw new InvalidOperationError("keys must be a list");
		}
		List<Value> valueList = new ArrayList<Value>();
		for (Object value : objList) {
			valueList.add(Value.get(value));
		}
		return valueList;
	}

	private static MapPolicy mapToMapPolicy(Map<String, Object>mapPolicy) {
		if (mapPolicy == null) {
			return MapPolicy.Default;
		}
		MapOrder order = MapOrder.UNORDERED;

		String orderString = (String) mapPolicy.get(ORDER_KEY);
		if(orderString == null) {
			throw new InvalidOperationError("Map policy must contain an entry for \"order\"");
		}
		try {
			order = MapOrder.valueOf(orderString);
		} catch (IllegalArgumentException e) {
			throw new InvalidOperationError("Invalid map order: " + orderString);
		}
		if (mapPolicy.containsKey(WRITE_FLAGS_KEY)) {
			int flags = getMapWriteFlags(mapPolicy);
			return new MapPolicy(order, flags);
		}

		MapWriteMode writeMode = MapWriteMode.UPDATE;
		String strWriteMode = (String) mapPolicy.get(WRITE_MODE_KEY);
		if (strWriteMode != null) {
			try {
				writeMode = MapWriteMode.valueOf(strWriteMode);
			}
			catch (IllegalArgumentException e) {
				throw new InvalidOperationError("Invalid write mode "+ strWriteMode);
			}
		}

		return new MapPolicy(order, writeMode);
	}

	private static int getMapWriteFlags(Map<String, Object>mapPolicy) {
		try {
			int flags = 0;
			@SuppressWarnings("unchecked")
			List<String> writeFlags = (List<String>)mapPolicy.get(WRITE_FLAGS_KEY);
			for (String flagName: writeFlags) {
				switch(flagName) {
					case "CREATE_ONLY":
						flags |= MapWriteFlags.CREATE_ONLY;
						break;
					case "UPDATE_ONLY":
						flags |= MapWriteFlags.UPDATE_ONLY;
						break;
					case "NO_FAIL":
						flags |= MapWriteFlags.NO_FAIL;
						break;
					case "PARTIAL":
						flags |= MapWriteFlags.PARTIAL;
						break;
					case "DEFAULT":
						flags |= MapWriteFlags.DEFAULT;
						break;
					default:
						throw new InvalidOperationError(String.format("Unknown mapWriteFlags: %s",  flagName));
				}
			}
			return flags;
		} catch (ClassCastException ccn) {
			throw new InvalidOperationError("writeFlags must be a list of strings");
		}
	}
	private static int getIntValue(Map<String, Object>map, String key) {
		Object nVal = map.get(key);
		try {
			return (int)nVal;
		} catch (ClassCastException cce) {
			try {
				return Math.toIntExact((Long)nVal);
			} catch (ArithmeticException e) {
				throw new InvalidOperationError(String.format("%s is too large", key));
			}
		}
	}
}