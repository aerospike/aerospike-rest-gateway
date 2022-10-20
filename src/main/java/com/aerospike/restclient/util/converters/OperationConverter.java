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
package com.aerospike.restclient.util.converters;

import com.aerospike.client.Bin;
import com.aerospike.client.Operation;
import com.aerospike.client.Value;
import com.aerospike.client.cdt.*;
import com.aerospike.client.operation.*;
import com.aerospike.restclient.config.JSONMessageConverter;
import com.aerospike.restclient.domain.ctxmodels.CTX;
import com.aerospike.restclient.domain.operationmodels.ListReturnType;
import com.aerospike.restclient.domain.operationmodels.MapReturnType;
import com.aerospike.restclient.util.AerospikeOperation;
import com.aerospike.restclient.util.RestClientErrors.InvalidOperationError;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Deprecated. This class, in conjunction with RestClientOperation should only be used in /v1/operate and /v1/execute until removed.
 * The class was designed to convert maps in the format "{"operation": operation, "opValues":{...}}" to
 * the appropriate aerospike java client operation. Now that the rest gateway has been transitioned to use swagger docs
 * all the Operations need to be represented with a model so that its format can be auto documented. An added plus is improved
 * error messages and the ability to independently design the schema for each model i.e. no need for "operation" and "opValues" keys.
 * Class containing static methods used for converting Java Maps to Aerospike Operations.
 */
@SuppressWarnings("unchecked")
@Deprecated
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
    public static final String PAD_KEY = "pad";
    public static final String LIST_POLICY_KEY = "listPolicy";
    public static final String RANK_KEY = "rank";
    public static final String SIGNED_KEY = "signed";
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

    public static final String BYTE_SIZE_KEY = "byteSize";
    public static final String BIT_SIZE_KEY = "bitSize";
    public static final String BIT_RESIZE_FLAGS_KEY = "resizeFlags";
    public static final String BYTE_OFFSET_KEY = "byteOffset";
    public static final String BIT_OFFSET_KEY = "bitOffset";
    public static final String BIT_SHIFT_KEY = "shift";
    public static final String BIT_OVERFLOW_ACTION_KEY = "bitOverflowAction";

    public static final String HLL_INDEX_BIT_COUNT_KEY = "indexBitCount";
    public static final String HLL_MIN_HASH_BIT_COUNT_KEY = "minHashBitCount";

    private static final ObjectMapper mapper = JSONMessageConverter.getJSONObjectMapper();

    @SuppressWarnings("unchecked")
    public static Operation convertMapToOperation(Map<String, Object> operationMap) {
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
        return switch (opName) {
            /* Basic Operations */
            case ADD -> mapToAddOp(opValues);
            case APPEND -> mapToAppendOp(opValues);
            case GET -> mapToGetOp(opValues);
            case PREPEND -> mapToPrependOp(opValues);
            case READ -> mapToReadOp(opValues);
            case GET_HEADER -> mapToGetHeaderOp(opValues);
            case TOUCH -> mapToTouchOp(opValues);
            case PUT -> mapToPutOp(opValues);
            case DELETE -> mapToDeleteOp(opValues);

            /* List Operations */
            case LIST_APPEND -> mapToListAppendOp(opValues);
            case LIST_APPEND_ITEMS -> mapToListAppendItemsOp(opValues);
            case LIST_CLEAR -> mapToListClearOp(opValues);
            case LIST_GET -> mapToListGetOp(opValues);
            case LIST_GET_BY_INDEX -> mapToListGetByIndexOp(opValues);
            case LIST_GET_BY_INDEX_RANGE -> mapToListGetByIndexRangeOp(opValues);
            case LIST_GET_BY_RANK -> mapToListGetByRankOp(opValues);
            case LIST_GET_BY_RANK_RANGE -> mapToListGetByRankRangeOp(opValues);
            case LIST_GET_BY_VALUE_REL_RANK_RANGE -> mapToListGetByValueRelRankRangeOp(opValues);
            case LIST_GET_BY_VALUE -> mapToListGetByValueOp(opValues);
            case LIST_GET_BY_VALUE_RANGE -> mapToListGetByValueRangeOp(opValues);
            case LIST_GET_BY_VALUE_LIST -> mapToListGetByValueListOp(opValues);
            case LIST_GET_RANGE -> mapToListGetRangeOp(opValues);
            case LIST_INCREMENT -> mapToListIncrementOp(opValues);
            case LIST_INSERT -> mapToListInsertOp(opValues);
            case LIST_INSERT_ITEMS -> mapToListInsertItemsOp(opValues);
            case LIST_POP -> mapToListPopOp(opValues);
            case LIST_POP_RANGE -> mapToListPopRangeOp(opValues);
            case LIST_REMOVE -> mapToListRemoveOp(opValues);
            case LIST_REMOVE_BY_INDEX -> mapToListRemoveByIndexOp(opValues);
            case LIST_REMOVE_BY_INDEX_RANGE -> mapToListRemoveByIndexRangeOp(opValues);
            case LIST_REMOVE_BY_RANK -> mapToListRemoveByRankOp(opValues);
            case LIST_REMOVE_BY_RANK_RANGE -> mapToListRemoveByRankRangeOp(opValues);
            case LIST_REMOVE_BY_VALUE_REL_RANK_RANGE -> mapToListRemoveByValueRelRankRangeOp(opValues);
            case LIST_REMOVE_BY_VALUE -> mapToListRemoveByValueOp(opValues);
            case LIST_REMOVE_BY_VALUE_RANGE -> mapToListRemoveByValueRangeOp(opValues);
            case LIST_REMOVE_BY_VALUE_LIST -> mapToListRemoveByValueListOp(opValues);
            case LIST_REMOVE_RANGE -> mapToListRemoveRangeOp(opValues);
            case LIST_SET -> mapToListSetOp(opValues);
            case LIST_SET_ORDER -> mapToListSetOrderOp(opValues);
            case LIST_SIZE -> mapToListSizeOp(opValues);
            case LIST_SORT -> mapToListSortOp(opValues);
            case LIST_TRIM -> mapToListTrimOp(opValues);
            case LIST_CREATE -> mapToListCreateOp(opValues);

            /* Map Operations */
            case MAP_CLEAR -> mapToMapClearOp(opValues);
            case MAP_GET_BY_INDEX -> mapToMapGetByIndexOp(opValues);
            case MAP_GET_BY_INDEX_RANGE -> mapToMapGetByIndexRangeOp(opValues);
            case MAP_GET_BY_KEY -> mapToMapGetByKeyOp(opValues);
            case MAP_GET_BY_KEY_LIST -> mapToMapGetByKeyListOp(opValues);
            case MAP_GET_BY_KEY_RANGE -> mapToMapGetByKeyRangeOp(opValues);
            case MAP_GET_BY_RANK -> mapToMapGetByRankOp(opValues);
            case MAP_GET_BY_RANK_RANGE -> mapToMapGetByRankRangeOp(opValues);
            case MAP_GET_BY_VALUE -> mapToMapGetByValueOp(opValues);
            case MAP_GET_BY_VALUE_RANGE -> mapToMapGetByValueRangeOp(opValues);
            case MAP_GET_BY_VALUE_LIST -> mapToMapGetByValueListOp(opValues);
            case MAP_GET_BY_KEY_REL_INDEX_RANGE -> mapToMapGetByKeyRelIndexRangeOp(opValues);
            case MAP_GET_BY_VALUE_REL_RANK_RANGE -> mapToMapGetByValueRelRankRangeOp(opValues);
            case MAP_INCREMENT -> mapToMapIncrementOp(opValues);
            case MAP_PUT -> mapToMapPutOp(opValues);
            case MAP_PUT_ITEMS -> mapToMapPutItemsOp(opValues);
            case MAP_REMOVE_BY_INDEX -> mapToMapRemoveByIndexOp(opValues);
            case MAP_REMOVE_BY_INDEX_RANGE -> mapToMapRemoveByIndexRangeOp(opValues);
            case MAP_REMOVE_BY_KEY -> mapToMapRemoveByKeyOp(opValues);
            case MAP_REMOVE_BY_KEY_RANGE -> mapToMapRemoveByKeyRangeOp(opValues);
            case MAP_REMOVE_BY_RANK -> mapToMapRemoveByRankOp(opValues);
            case MAP_REMOVE_BY_RANK_RANGE -> mapToMapRemoveByRankRangeOp(opValues);
            case MAP_REMOVE_BY_KEY_REL_INDEX_RANGE -> mapToMapRemoveByKeyRelIndexRangeOp(opValues);
            case MAP_REMOVE_BY_VALUE_REL_RANK_RANGE -> mapToMapRemoveByValueRelRankRangeOp(opValues);
            case MAP_REMOVE_BY_VALUE -> mapToMapRemoveByValueOp(opValues);
            case MAP_REMOVE_BY_VALUE_RANGE -> mapToMapRemoveByValueRangeOp(opValues);
            case MAP_REMOVE_BY_VALUE_LIST -> mapToMapRemoveByValueListOp(opValues);
            case MAP_SET_MAP_POLICY -> mapToMapSetMapPolicyOp(opValues);
            case MAP_SIZE -> mapToMapSizeOp(opValues);
            case MAP_CREATE -> mapToMapCreateOp(opValues);

            /* Bit Operations */
            case BIT_RESIZE -> mapToBitResizeOp(opValues);
            case BIT_INSERT -> mapToBitInsertOp(opValues);
            case BIT_REMOVE -> mapToBitRemoveOp(opValues);
            case BIT_SET -> mapToBitSetOp(opValues);
            case BIT_OR -> mapToBitOrOp(opValues);
            case BIT_XOR -> mapToBitXorOp(opValues);
            case BIT_AND -> mapToBitAndOp(opValues);
            case BIT_NOT -> mapToBitNotOp(opValues);
            case BIT_LSHIFT -> mapToBitLshiftOp(opValues);
            case BIT_RSHIFT -> mapToBitRshiftOp(opValues);
            case BIT_ADD -> mapToBitAddOp(opValues);
            case BIT_SUBTRACT -> mapToBitSubtractOp(opValues);
            case BIT_SET_INT -> mapToBitSetIntOp(opValues);
            case BIT_GET -> mapToBitGetOp(opValues);
            case BIT_COUNT -> mapToBitCountOp(opValues);
            case BIT_LSCAN -> mapToBitLscanOp(opValues);
            case BIT_RSCAN -> mapToBitRscanOp(opValues);
            case BIT_GET_INT -> mapToBitGetIntOp(opValues);

            /* HLL Operations */
            case HLL_INIT -> mapToHLLInitOp(opValues);
            case HLL_ADD -> mapToHLLAddOp(opValues);
            case HLL_SET_UNION -> mapToHLLSetUnionOp(opValues);
            case HLL_SET_COUNT -> mapToHLLRefreshCountOp(opValues);
            case HLL_FOLD -> mapToHLLFoldOp(opValues);
            case HLL_COUNT -> mapToHLLGetCountOp(opValues);
            case HLL_UNION -> mapToHLLGetUnionOp(opValues);
            case HLL_UNION_COUNT -> mapToHLLGetUnionCountOp(opValues);
            case HLL_INTERSECT_COUNT -> mapToHLLGetIntersectCountOp(opValues);
            case HLL_SIMILARITY -> mapToHLLGetSimilarityOp(opValues);
            case HLL_DESCRIBE -> mapToHLLDescribeOp(opValues);
            default -> throw new InvalidOperationError("Invalid operation: " + opName);
        };
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
        onlyHasAllowedKeys(opValues, BIN_KEY);

        String binName = getBinName(opValues);
        if (binName != null) {
            return Operation.get(binName);
        }
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

        return com.aerospike.client.cdt.ListOperation.append(binName, value, extractCTX(opValues));
    }

    private static Operation mapToListAppendItemsOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, VALUES_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, VALUES_KEY, LIST_POLICY_KEY);

        String binName = getBinName(opValues);
        List<Value> valueList = getValueList(opValues);
        ListPolicy policy = getListPolicy(opValues);

        if (policy != null) {
            return com.aerospike.client.cdt.ListOperation.appendItems(policy, binName, valueList, extractCTX(opValues));
        } else {
            return com.aerospike.client.cdt.ListOperation.appendItems(binName, valueList, extractCTX(opValues));
        }
    }

    private static Operation mapToListClearOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY);

        String binName = getBinName(opValues);

        return com.aerospike.client.cdt.ListOperation.clear(binName, extractCTX(opValues));
    }

    private static Operation mapToListGetOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, INDEX_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, INDEX_KEY);

        String binName = getBinName(opValues);
        int index = getIndex(opValues);

        return com.aerospike.client.cdt.ListOperation.get(binName, index, extractCTX(opValues));
    }

    private static Operation mapToListGetByIndexOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, INDEX_KEY, LIST_RETURN_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, INDEX_KEY, LIST_RETURN_KEY);

        String binName = getBinName(opValues);
        int index = getIndex(opValues);
        int returnType = getListReturnType(opValues);

        return com.aerospike.client.cdt.ListOperation.getByIndex(binName, index, returnType, extractCTX(opValues));
    }

    private static Operation mapToListGetByIndexRangeOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, INDEX_KEY, LIST_RETURN_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, INDEX_KEY, LIST_RETURN_KEY, COUNT_KEY);

        String binName = getBinName(opValues);
        int index = getIndex(opValues);
        Integer count = getCount(opValues);
        int returnType = getListReturnType(opValues);

        if (count != null) {
            return com.aerospike.client.cdt.ListOperation.getByIndexRange(binName, index, count, returnType,
                    extractCTX(opValues));
        } else {
            return com.aerospike.client.cdt.ListOperation.getByIndexRange(binName, index, returnType,
                    extractCTX(opValues));
        }
    }

    private static Operation mapToListGetByRankOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, RANK_KEY, LIST_RETURN_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, RANK_KEY, LIST_RETURN_KEY);

        String binName = getBinName(opValues);
        int rank = getRank(opValues);
        int returnType = getListReturnType(opValues);

        return com.aerospike.client.cdt.ListOperation.getByRank(binName, rank, returnType, extractCTX(opValues));
    }

    private static Operation mapToListGetByRankRangeOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, RANK_KEY, LIST_RETURN_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, RANK_KEY, LIST_RETURN_KEY, COUNT_KEY);

        String binName = getBinName(opValues);
        int rank = getRank(opValues);
        Integer count = getCount(opValues);
        int returnType = getListReturnType(opValues);

        if (count != null) {
            return com.aerospike.client.cdt.ListOperation.getByRankRange(binName, rank, count, returnType,
                    extractCTX(opValues));
        } else {
            return com.aerospike.client.cdt.ListOperation.getByRankRange(binName, rank, returnType,
                    extractCTX(opValues));
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
            return com.aerospike.client.cdt.ListOperation.getByValueRelativeRankRange(binName, value, rank, count,
                    returnType, extractCTX(opValues));
        } else {
            return com.aerospike.client.cdt.ListOperation.getByValueRelativeRankRange(binName, value, rank, returnType,
                    extractCTX(opValues));
        }
    }

    private static Operation mapToListGetByValueOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, VALUE_KEY, LIST_RETURN_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, VALUE_KEY, LIST_RETURN_KEY);

        String binName = getBinName(opValues);
        Value value = getValue(opValues);
        int returnType = getListReturnType(opValues);

        return com.aerospike.client.cdt.ListOperation.getByValue(binName, value, returnType, extractCTX(opValues));
    }

    private static Operation mapToListGetByValueRangeOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, LIST_RETURN_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, LIST_RETURN_KEY, VALUE_BEGIN_KEY, VALUE_END_KEY);

        String binName = getBinName(opValues);
        Value valueBegin = getValueBegin(opValues);
        Value valueEnd = getValueEnd(opValues);
        int returnType = getListReturnType(opValues);

        return com.aerospike.client.cdt.ListOperation.getByValueRange(binName, valueBegin, valueEnd, returnType,
                extractCTX(opValues));
    }

    private static Operation mapToListGetByValueListOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, VALUES_KEY, LIST_RETURN_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, VALUES_KEY, LIST_RETURN_KEY);

        String binName = getBinName(opValues);
        List<Value> values = getValueList(opValues);
        int returnType = getListReturnType(opValues);

        return com.aerospike.client.cdt.ListOperation.getByValueList(binName, values, returnType, extractCTX(opValues));
    }

    private static Operation mapToListGetRangeOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, INDEX_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, INDEX_KEY, COUNT_KEY);

        String binName = getBinName(opValues);
        int index = getIndex(opValues);
        Integer count = getCount(opValues);

        if (count != null) {
            return com.aerospike.client.cdt.ListOperation.getRange(binName, index, count, extractCTX(opValues));
        } else {
            return com.aerospike.client.cdt.ListOperation.getRange(binName, index, extractCTX(opValues));
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
                return com.aerospike.client.cdt.ListOperation.increment(policy, binName, index, incr,
                        extractCTX(opValues));
            } else {
                return com.aerospike.client.cdt.ListOperation.increment(policy, binName, index, extractCTX(opValues));
            }
        } else {
            if (incr != null) {
                return com.aerospike.client.cdt.ListOperation.increment(binName, index, incr, extractCTX(opValues));
            } else {
                return com.aerospike.client.cdt.ListOperation.increment(binName, index, extractCTX(opValues));
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
            return com.aerospike.client.cdt.ListOperation.insert(binName, index, value, extractCTX(opValues));
        } else {
            return com.aerospike.client.cdt.ListOperation.insert(policy, binName, index, value, extractCTX(opValues));
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
            return com.aerospike.client.cdt.ListOperation.insertItems(binName, index, values, extractCTX(opValues));
        } else {
            return com.aerospike.client.cdt.ListOperation.insertItems(policy, binName, index, values,
                    extractCTX(opValues));
        }
    }

    private static Operation mapToListPopOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, INDEX_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, INDEX_KEY);

        String binName = getBinName(opValues);
        int index = getIndex(opValues);

        return com.aerospike.client.cdt.ListOperation.pop(binName, index, extractCTX(opValues));
    }

    private static Operation mapToListPopRangeOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, INDEX_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, INDEX_KEY, COUNT_KEY);

        String binName = getBinName(opValues);
        int index = getIndex(opValues);
        Integer count = getCount(opValues);

        if (count != null) {
            return com.aerospike.client.cdt.ListOperation.popRange(binName, index, count, extractCTX(opValues));
        } else {
            return com.aerospike.client.cdt.ListOperation.popRange(binName, index, extractCTX(opValues));
        }
    }

    private static Operation mapToListRemoveOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, INDEX_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, INDEX_KEY);

        String binName = getBinName(opValues);
        int index = getIndex(opValues);

        return com.aerospike.client.cdt.ListOperation.remove(binName, index, extractCTX(opValues));
    }

    private static Operation mapToListRemoveByIndexOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, INDEX_KEY, LIST_RETURN_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, INDEX_KEY, LIST_RETURN_KEY);

        String binName = getBinName(opValues);
        int index = getIndex(opValues);
        int returnType = getListReturnType(opValues);

        return com.aerospike.client.cdt.ListOperation.removeByIndex(binName, index, returnType, extractCTX(opValues));
    }

    private static Operation mapToListRemoveByIndexRangeOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, INDEX_KEY, LIST_RETURN_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, INDEX_KEY, LIST_RETURN_KEY, COUNT_KEY);

        String binName = getBinName(opValues);
        int index = getIndex(opValues);
        Integer count = getCount(opValues);
        int returnType = getListReturnType(opValues);

        if (count != null) {
            return com.aerospike.client.cdt.ListOperation.removeByIndexRange(binName, index, count, returnType,
                    extractCTX(opValues));
        }
        return com.aerospike.client.cdt.ListOperation.removeByIndexRange(binName, index, returnType,
                extractCTX(opValues));
    }

    private static Operation mapToListRemoveByRankOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, RANK_KEY, LIST_RETURN_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, RANK_KEY, LIST_RETURN_KEY);

        String binName = getBinName(opValues);
        int rank = getRank(opValues);
        int returnType = getListReturnType(opValues);

        return com.aerospike.client.cdt.ListOperation.removeByRank(binName, rank, returnType, extractCTX(opValues));
    }

    private static Operation mapToListRemoveByRankRangeOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, RANK_KEY, LIST_RETURN_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, RANK_KEY, LIST_RETURN_KEY, COUNT_KEY);

        String binName = getBinName(opValues);
        int rank = getRank(opValues);
        int returnType = getListReturnType(opValues);
        Integer count = getCount(opValues);

        if (count != null) {
            return com.aerospike.client.cdt.ListOperation.removeByRankRange(binName, rank, count, returnType,
                    extractCTX(opValues));
        }
        return com.aerospike.client.cdt.ListOperation.removeByRankRange(binName, rank, returnType,
                extractCTX(opValues));
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
            return com.aerospike.client.cdt.ListOperation.removeByValueRelativeRankRange(binName, value, rank, count,
                    returnType, extractCTX(opValues));
        }
        return com.aerospike.client.cdt.ListOperation.removeByValueRelativeRankRange(binName, value, rank, returnType,
                extractCTX(opValues));
    }

    private static Operation mapToListRemoveByValueOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, VALUE_KEY, LIST_RETURN_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, VALUE_KEY, LIST_RETURN_KEY);

        String binName = getBinName(opValues);
        Value value = getValue(opValues);
        int returnType = getListReturnType(opValues);

        return com.aerospike.client.cdt.ListOperation.removeByValue(binName, value, returnType, extractCTX(opValues));
    }

    private static Operation mapToListRemoveByValueRangeOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, LIST_RETURN_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, LIST_RETURN_KEY, VALUE_BEGIN_KEY, VALUE_END_KEY);

        String binName = getBinName(opValues);
        Value valueBegin = getValueBegin(opValues);
        Value valueEnd = getValueEnd(opValues);
        int returnType = getListReturnType(opValues);

        return com.aerospike.client.cdt.ListOperation.removeByValueRange(binName, valueBegin, valueEnd, returnType,
                extractCTX(opValues));
    }

    private static Operation mapToListRemoveByValueListOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, VALUES_KEY, LIST_RETURN_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, VALUES_KEY, LIST_RETURN_KEY);

        String binName = getBinName(opValues);
        List<Value> values = getValueList(opValues);
        int returnType = getListReturnType(opValues);

        return com.aerospike.client.cdt.ListOperation.removeByValueList(binName, values, returnType,
                extractCTX(opValues));
    }

    private static Operation mapToListRemoveRangeOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, INDEX_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, INDEX_KEY, COUNT_KEY);

        String binName = getBinName(opValues);
        int index = getIndex(opValues);
        Integer count = getCount(opValues);

        if (count != null) {
            return com.aerospike.client.cdt.ListOperation.removeRange(binName, index, count, extractCTX(opValues));
        }
        return com.aerospike.client.cdt.ListOperation.removeRange(binName, index, extractCTX(opValues));
    }

    private static Operation mapToListSetOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, INDEX_KEY, VALUE_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, INDEX_KEY, VALUE_KEY, LIST_POLICY_KEY);

        String binName = getBinName(opValues);
        Value value = getValue(opValues);
        ListPolicy policy = getListPolicy(opValues);
        int index = getIndex(opValues);

        if (policy != null) {
            return com.aerospike.client.cdt.ListOperation.set(policy, binName, index, value, extractCTX(opValues));
        }
        return com.aerospike.client.cdt.ListOperation.set(binName, index, value, extractCTX(opValues));
    }

    private static Operation mapToListSetOrderOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, LIST_ORDER_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, LIST_ORDER_KEY);

        String binName = getBinName(opValues);
        ListOrder order = getListOrder(opValues);

        return com.aerospike.client.cdt.ListOperation.setOrder(binName, order);
    }

    private static Operation mapToListSizeOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY);

        String binName = getBinName(opValues);

        return com.aerospike.client.cdt.ListOperation.size(binName, extractCTX(opValues));
    }

    private static Operation mapToListSortOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, LIST_SORT_FLAGS_KEY);

        String binName = getBinName(opValues);
        int sortFlags = getSortFlags(opValues);

        return com.aerospike.client.cdt.ListOperation.sort(binName, sortFlags, extractCTX(opValues));
    }

    private static Operation mapToListTrimOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, INDEX_KEY, COUNT_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, INDEX_KEY, COUNT_KEY);

        String binName = getBinName(opValues);
        int index = getIndex(opValues);
        int count = getCount(opValues);

        return com.aerospike.client.cdt.ListOperation.trim(binName, index, count, extractCTX(opValues));
    }

    private static Operation mapToListCreateOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, LIST_ORDER_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, LIST_ORDER_KEY, PAD_KEY);

        String binName = getBinName(opValues);
        ListOrder order = getListOrder(opValues);
        boolean pad = getBoolValue(opValues, PAD_KEY);

        return com.aerospike.client.cdt.ListOperation.create(binName, order, pad, extractCTX(opValues));
    }

    /* MAP OPERATIONS */

    private static Operation mapToMapClearOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY);

        String binName = getBinName(opValues);

        return com.aerospike.client.cdt.MapOperation.clear(binName, extractCTX(opValues));
    }

    private static Operation mapToMapGetByIndexOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, INDEX_KEY, MAP_RETURN_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, INDEX_KEY, MAP_RETURN_KEY);

        String binName = getBinName(opValues);
        int returnType = getMapReturnType(opValues);
        int index = getIndex(opValues);

        return com.aerospike.client.cdt.MapOperation.getByIndex(binName, index, returnType, extractCTX(opValues));
    }

    private static Operation mapToMapGetByIndexRangeOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, INDEX_KEY, MAP_RETURN_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, INDEX_KEY, MAP_RETURN_KEY, COUNT_KEY);

        String binName = getBinName(opValues);
        int returnType = getMapReturnType(opValues);
        int index = getIndex(opValues);
        Integer count = getCount(opValues);
        if (count == null) {
            return com.aerospike.client.cdt.MapOperation.getByIndexRange(binName, index, returnType,
                    extractCTX(opValues));
        }
        return com.aerospike.client.cdt.MapOperation.getByIndexRange(binName, index, count, returnType,
                extractCTX(opValues));
    }

    private static Operation mapToMapGetByKeyOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, MAP_KEY_KEY, MAP_RETURN_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, MAP_KEY_KEY, MAP_RETURN_KEY);

        String binName = getBinName(opValues);
        int returnType = getMapReturnType(opValues);
        Value key = getMapKey(opValues);

        return com.aerospike.client.cdt.MapOperation.getByKey(binName, key, returnType, extractCTX(opValues));
    }

    private static Operation mapToMapGetByKeyListOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, MAP_KEYS_KEY, MAP_RETURN_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, MAP_KEYS_KEY, MAP_RETURN_KEY);

        String binName = getBinName(opValues);
        int returnType = getMapReturnType(opValues);
        List<Value> keys = getMapKeys(opValues);

        return com.aerospike.client.cdt.MapOperation.getByKeyList(binName, keys, returnType, extractCTX(opValues));
    }

    private static Operation mapToMapGetByKeyRangeOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, MAP_RETURN_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, MAP_KEY_BEGIN_KEY, MAP_KEY_END_KEY, MAP_RETURN_KEY);

        String binName = getBinName(opValues);
        int returnType = getMapReturnType(opValues);
        Value keyBegin = getMapKeyBegin(opValues);
        Value keyEnd = getMapKeyEnd(opValues);

        return com.aerospike.client.cdt.MapOperation.getByKeyRange(binName, keyBegin, keyEnd, returnType,
                extractCTX(opValues));
    }

    private static Operation mapToMapGetByRankOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, RANK_KEY, MAP_RETURN_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, RANK_KEY, MAP_RETURN_KEY);

        String binName = getBinName(opValues);
        int rank = getRank(opValues);
        int returnType = getMapReturnType(opValues);
        return com.aerospike.client.cdt.MapOperation.getByRank(binName, rank, returnType, extractCTX(opValues));
    }

    private static Operation mapToMapGetByRankRangeOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, RANK_KEY, MAP_RETURN_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, RANK_KEY, COUNT_KEY, MAP_RETURN_KEY);

        String binName = getBinName(opValues);
        int rank = getRank(opValues);
        Integer count = getCount(opValues);
        int returnType = getMapReturnType(opValues);

        if (count != null) {
            return com.aerospike.client.cdt.MapOperation.getByRankRange(binName, rank, count, returnType,
                    extractCTX(opValues));
        }
        return com.aerospike.client.cdt.MapOperation.getByRankRange(binName, rank, returnType, extractCTX(opValues));
    }

    private static Operation mapToMapGetByValueOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, VALUE_KEY, MAP_RETURN_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, VALUE_KEY, MAP_RETURN_KEY);

        String binName = getBinName(opValues);
        Value value = getValue(opValues);
        int returnType = getMapReturnType(opValues);
        return com.aerospike.client.cdt.MapOperation.getByValue(binName, value, returnType, extractCTX(opValues));
    }

    private static Operation mapToMapGetByValueRangeOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, MAP_RETURN_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, VALUE_BEGIN_KEY, VALUE_END_KEY, MAP_RETURN_KEY);

        String binName = getBinName(opValues);
        Value valueBegin = getValueBegin(opValues);
        Value valueEnd = getValueEnd(opValues);
        int returnType = getMapReturnType(opValues);

        return com.aerospike.client.cdt.MapOperation.getByValueRange(binName, valueBegin, valueEnd, returnType,
                extractCTX(opValues));
    }

    private static Operation mapToMapGetByValueListOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, VALUES_KEY, MAP_RETURN_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, VALUES_KEY, MAP_RETURN_KEY);

        String binName = getBinName(opValues);
        List<Value> values = getValueList(opValues);
        int returnType = getMapReturnType(opValues);

        return com.aerospike.client.cdt.MapOperation.getByValueList(binName, values, returnType, extractCTX(opValues));
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
            return com.aerospike.client.cdt.MapOperation.getByKeyRelativeIndexRange(binName, value, index, returnType,
                    extractCTX(opValues));
        }
        return com.aerospike.client.cdt.MapOperation.getByKeyRelativeIndexRange(binName, value, index, count,
                returnType, extractCTX(opValues));
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
            return com.aerospike.client.cdt.MapOperation.getByValueRelativeRankRange(binName, value, rank, count,
                    returnType, extractCTX(opValues));
        }
        return com.aerospike.client.cdt.MapOperation.getByValueRelativeRankRange(binName, value, rank, returnType,
                extractCTX(opValues));
    }

    private static Operation mapToMapIncrementOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, INCR_KEY, MAP_KEY_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, INCR_KEY, MAP_POLICY_KEY, MAP_KEY_KEY);

        String binName = getBinName(opValues);
        MapPolicy policy = getMapPolicy(opValues);
        Value incr = getIncr(opValues);
        Value key = getMapKey(opValues);

        return com.aerospike.client.cdt.MapOperation.increment(policy, binName, key, incr, extractCTX(opValues));
    }

    private static Operation mapToMapPutOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, VALUE_KEY, MAP_KEY_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, VALUE_KEY, MAP_POLICY_KEY, MAP_KEY_KEY);

        MapPolicy policy = getMapPolicy(opValues);
        String binName = getBinName(opValues);
        Value value = getValue(opValues);
        Value key = getMapKey(opValues);

        return com.aerospike.client.cdt.MapOperation.put(policy, binName, key, value, extractCTX(opValues));
    }

    private static Operation mapToMapPutItemsOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, MAP_VALUES_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, MAP_VALUES_KEY, MAP_POLICY_KEY);

        String binName = getBinName(opValues);
        MapPolicy policy = getMapPolicy(opValues);
        Map<Value, Value> putItems = getMapValues(opValues);

        return com.aerospike.client.cdt.MapOperation.putItems(policy, binName, putItems, extractCTX(opValues));
    }

    private static Operation mapToMapRemoveByIndexOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, INDEX_KEY, MAP_RETURN_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, INDEX_KEY, MAP_RETURN_KEY);

        String binName = getBinName(opValues);
        int returnType = getMapReturnType(opValues);
        int index = getIndex(opValues);

        return com.aerospike.client.cdt.MapOperation.removeByIndex(binName, index, returnType, extractCTX(opValues));
    }

    private static Operation mapToMapRemoveByIndexRangeOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, INDEX_KEY, MAP_RETURN_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, INDEX_KEY, COUNT_KEY, MAP_RETURN_KEY);

        String binName = getBinName(opValues);
        int returnType = getMapReturnType(opValues);
        int index = getIndex(opValues);
        Integer count = getCount(opValues);

        if (count != null) {
            return com.aerospike.client.cdt.MapOperation.removeByIndexRange(binName, index, count, returnType,
                    extractCTX(opValues));
        }
        return com.aerospike.client.cdt.MapOperation.removeByIndexRange(binName, index, returnType,
                extractCTX(opValues));
    }

    private static Operation mapToMapRemoveByKeyOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, MAP_KEY_KEY, MAP_RETURN_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, MAP_KEY_KEY, MAP_RETURN_KEY);

        String binName = getBinName(opValues);
        int returnType = getMapReturnType(opValues);
        Value key = getMapKey(opValues);

        return com.aerospike.client.cdt.MapOperation.removeByKey(binName, key, returnType, extractCTX(opValues));
    }

    private static Operation mapToMapRemoveByKeyRangeOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, MAP_RETURN_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, MAP_KEY_BEGIN_KEY, MAP_KEY_END_KEY, MAP_RETURN_KEY);

        String binName = getBinName(opValues);
        int returnType = getMapReturnType(opValues);
        Value keyBegin = getMapKeyBegin(opValues);
        Value keyEnd = getMapKeyEnd(opValues);

        return com.aerospike.client.cdt.MapOperation.removeByKeyRange(binName, keyBegin, keyEnd, returnType,
                extractCTX(opValues));
    }

    private static Operation mapToMapRemoveByRankOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, RANK_KEY, MAP_RETURN_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, RANK_KEY, MAP_RETURN_KEY);

        String binName = getBinName(opValues);
        int rank = getRank(opValues);
        int returnType = getMapReturnType(opValues);

        return com.aerospike.client.cdt.MapOperation.removeByRank(binName, rank, returnType, extractCTX(opValues));
    }

    private static Operation mapToMapRemoveByRankRangeOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, RANK_KEY, MAP_RETURN_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, RANK_KEY, COUNT_KEY, MAP_RETURN_KEY);

        String binName = getBinName(opValues);
        int rank = getRank(opValues);
        Integer count = getCount(opValues);
        int returnType = getMapReturnType(opValues);

        if (count != null) {
            return com.aerospike.client.cdt.MapOperation.removeByRankRange(binName, rank, count, returnType,
                    extractCTX(opValues));
        }
        return com.aerospike.client.cdt.MapOperation.removeByRankRange(binName, rank, returnType, extractCTX(opValues));
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
            return com.aerospike.client.cdt.MapOperation.removeByKeyRelativeIndexRange(binName, value, index, count,
                    returnType, extractCTX(opValues));
        }
        return com.aerospike.client.cdt.MapOperation.removeByKeyRelativeIndexRange(binName, value, index, returnType,
                extractCTX(opValues));
    }

    private static Operation mapToMapRemoveByValueRelRankRangeOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, RANK_KEY, VALUE_KEY, MAP_RETURN_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, RANK_KEY, VALUE_KEY, COUNT_KEY, MAP_RETURN_KEY);

        String binName = getBinName(opValues);
        Value value = getValue(opValues);
        int rank = getRank(opValues);
        Integer count = getCount(opValues);
        int returnType = getMapReturnType(opValues);

        if (count != null) {
            return com.aerospike.client.cdt.MapOperation.removeByValueRelativeRankRange(binName, value, rank, count,
                    returnType, extractCTX(opValues));
        }
        return com.aerospike.client.cdt.MapOperation.removeByValueRelativeRankRange(binName, value, rank, returnType,
                extractCTX(opValues));
    }

    private static Operation mapToMapRemoveByValueOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, VALUE_KEY, MAP_RETURN_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, VALUE_KEY, MAP_RETURN_KEY);

        String binName = getBinName(opValues);
        Value value = getValue(opValues);
        int returnType = getMapReturnType(opValues);
        return com.aerospike.client.cdt.MapOperation.removeByValue(binName, value, returnType, extractCTX(opValues));
    }

    private static Operation mapToMapRemoveByValueRangeOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, MAP_RETURN_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, VALUE_BEGIN_KEY, VALUE_END_KEY, MAP_RETURN_KEY);

        String binName = getBinName(opValues);
        Value valueBegin = getValueBegin(opValues);
        Value valueEnd = getValueEnd(opValues);
        int returnType = getMapReturnType(opValues);

        return com.aerospike.client.cdt.MapOperation.removeByValueRange(binName, valueBegin, valueEnd, returnType,
                extractCTX(opValues));
    }

    private static Operation mapToMapRemoveByValueListOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, VALUES_KEY, MAP_RETURN_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, VALUES_KEY, MAP_RETURN_KEY);

        String binName = getBinName(opValues);
        List<Value> values = getValueList(opValues);
        int returnType = getMapReturnType(opValues);

        return com.aerospike.client.cdt.MapOperation.removeByValueList(binName, values, returnType,
                extractCTX(opValues));
    }

    private static Operation mapToMapSetMapPolicyOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, MAP_POLICY_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, MAP_POLICY_KEY);

        String binName = getBinName(opValues);
        MapPolicy policy = getMapPolicy(opValues);

        return com.aerospike.client.cdt.MapOperation.setMapPolicy(policy, binName, extractCTX(opValues));
    }

    private static Operation mapToMapSizeOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY);

        String binName = getBinName(opValues);

        return com.aerospike.client.cdt.MapOperation.size(binName, extractCTX(opValues));
    }

    private static Operation mapToMapCreateOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, MAP_ORDER_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, MAP_ORDER_KEY);

        String binName = getBinName(opValues);
        MapOrder order = getMapOrder(opValues);

        return com.aerospike.client.cdt.MapOperation.create(binName, order, extractCTX(opValues));
    }

    /* BIT OPERATIONS */

    private static Operation mapToBitResizeOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, BYTE_SIZE_KEY, BIT_RESIZE_FLAGS_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, BYTE_SIZE_KEY, BIT_RESIZE_FLAGS_KEY);

        String binName = getBinName(opValues);
        int byteSize = getIntValue(opValues, BYTE_SIZE_KEY);
        int resizeFlags = getIntValue(opValues, BIT_RESIZE_FLAGS_KEY);

        return BitOperation.resize(BitPolicy.Default, binName, byteSize, resizeFlags);
    }

    private static Operation mapToBitInsertOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, BYTE_OFFSET_KEY, VALUE_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, BYTE_OFFSET_KEY, VALUE_KEY);

        String binName = getBinName(opValues);
        int byteOffset = getIntValue(opValues, BYTE_OFFSET_KEY);
        byte[] value = getBitValue(opValues);

        return BitOperation.insert(BitPolicy.Default, binName, byteOffset, value);
    }

    private static Operation mapToBitRemoveOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, BYTE_OFFSET_KEY, BYTE_SIZE_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, BYTE_OFFSET_KEY, BYTE_SIZE_KEY);

        String binName = getBinName(opValues);
        int byteOffset = getIntValue(opValues, BYTE_OFFSET_KEY);
        int byteSize = getIntValue(opValues, BYTE_SIZE_KEY);

        return BitOperation.remove(BitPolicy.Default, binName, byteOffset, byteSize);
    }

    private static Operation mapToBitSetOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, BIT_OFFSET_KEY, BIT_SIZE_KEY, VALUE_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, BIT_OFFSET_KEY, BIT_SIZE_KEY, VALUE_KEY);

        String binName = getBinName(opValues);
        int bitOffset = getIntValue(opValues, BIT_OFFSET_KEY);
        int bitSize = getIntValue(opValues, BIT_SIZE_KEY);
        byte[] value = getBitValue(opValues);

        return BitOperation.set(BitPolicy.Default, binName, bitOffset, bitSize, value);
    }

    private static Operation mapToBitOrOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, BIT_OFFSET_KEY, BIT_SIZE_KEY, VALUE_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, BIT_OFFSET_KEY, BIT_SIZE_KEY, VALUE_KEY);

        String binName = getBinName(opValues);
        int bitOffset = getIntValue(opValues, BIT_OFFSET_KEY);
        int bitSize = getIntValue(opValues, BIT_SIZE_KEY);
        byte[] value = getBitValue(opValues);

        return BitOperation.or(BitPolicy.Default, binName, bitOffset, bitSize, value);
    }

    private static Operation mapToBitXorOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, BIT_OFFSET_KEY, BIT_SIZE_KEY, VALUE_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, BIT_OFFSET_KEY, BIT_SIZE_KEY, VALUE_KEY);

        String binName = getBinName(opValues);
        int bitOffset = getIntValue(opValues, BIT_OFFSET_KEY);
        int bitSize = getIntValue(opValues, BIT_SIZE_KEY);
        byte[] value = getBitValue(opValues);

        return BitOperation.xor(BitPolicy.Default, binName, bitOffset, bitSize, value);
    }

    private static Operation mapToBitAndOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, BIT_OFFSET_KEY, BIT_SIZE_KEY, VALUE_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, BIT_OFFSET_KEY, BIT_SIZE_KEY, VALUE_KEY);

        String binName = getBinName(opValues);
        int bitOffset = getIntValue(opValues, BIT_OFFSET_KEY);
        int bitSize = getIntValue(opValues, BIT_SIZE_KEY);
        byte[] value = getBitValue(opValues);

        return BitOperation.and(BitPolicy.Default, binName, bitOffset, bitSize, value);
    }

    private static Operation mapToBitNotOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, BIT_OFFSET_KEY, BIT_SIZE_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, BIT_OFFSET_KEY, BIT_SIZE_KEY);

        String binName = getBinName(opValues);
        int bitOffset = getIntValue(opValues, BIT_OFFSET_KEY);
        int bitSize = getIntValue(opValues, BIT_SIZE_KEY);

        return BitOperation.not(BitPolicy.Default, binName, bitOffset, bitSize);
    }

    private static Operation mapToBitLshiftOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, BIT_OFFSET_KEY, BIT_SIZE_KEY, BIT_SHIFT_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, BIT_OFFSET_KEY, BIT_SIZE_KEY, BIT_SHIFT_KEY);

        String binName = getBinName(opValues);
        int bitOffset = getIntValue(opValues, BIT_OFFSET_KEY);
        int bitSize = getIntValue(opValues, BIT_SIZE_KEY);
        int shift = getIntValue(opValues, BIT_SHIFT_KEY);

        return BitOperation.lshift(BitPolicy.Default, binName, bitOffset, bitSize, shift);
    }

    private static Operation mapToBitRshiftOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, BIT_OFFSET_KEY, BIT_SIZE_KEY, BIT_SHIFT_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, BIT_OFFSET_KEY, BIT_SIZE_KEY, BIT_SHIFT_KEY);

        String binName = getBinName(opValues);
        int bitOffset = getIntValue(opValues, BIT_OFFSET_KEY);
        int bitSize = getIntValue(opValues, BIT_SIZE_KEY);
        int shift = getIntValue(opValues, BIT_SHIFT_KEY);

        return BitOperation.rshift(BitPolicy.Default, binName, bitOffset, bitSize, shift);
    }

    private static Operation mapToBitAddOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, BIT_OFFSET_KEY, BIT_SIZE_KEY, VALUE_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, BIT_OFFSET_KEY, BIT_SIZE_KEY, VALUE_KEY, SIGNED_KEY,
                BIT_OVERFLOW_ACTION_KEY);

        String binName = getBinName(opValues);
        int bitOffset = getIntValue(opValues, BIT_OFFSET_KEY);
        int bitSize = getIntValue(opValues, BIT_SIZE_KEY);
        long value = getLongValue(opValues, VALUE_KEY);
        boolean signed = getBoolValue(opValues, SIGNED_KEY);
        BitOverflowAction action = getBitOverflowAction(opValues);

        return BitOperation.add(BitPolicy.Default, binName, bitOffset, bitSize, value, signed, action);
    }

    private static Operation mapToBitSubtractOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, BIT_OFFSET_KEY, BIT_SIZE_KEY, VALUE_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, BIT_OFFSET_KEY, BIT_SIZE_KEY, VALUE_KEY, SIGNED_KEY,
                BIT_OVERFLOW_ACTION_KEY);

        String binName = getBinName(opValues);
        int bitOffset = getIntValue(opValues, BIT_OFFSET_KEY);
        int bitSize = getIntValue(opValues, BIT_SIZE_KEY);
        long value = getLongValue(opValues, VALUE_KEY);
        boolean signed = getBoolValue(opValues, SIGNED_KEY);
        BitOverflowAction action = getBitOverflowAction(opValues);

        return BitOperation.subtract(BitPolicy.Default, binName, bitOffset, bitSize, value, signed, action);
    }

    private static Operation mapToBitSetIntOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, BIT_OFFSET_KEY, BIT_SIZE_KEY, VALUE_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, BIT_OFFSET_KEY, BIT_SIZE_KEY, VALUE_KEY);

        String binName = getBinName(opValues);
        int bitOffset = getIntValue(opValues, BIT_OFFSET_KEY);
        int bitSize = getIntValue(opValues, BIT_SIZE_KEY);
        long value = getLongValue(opValues, VALUE_KEY);

        return BitOperation.setInt(BitPolicy.Default, binName, bitOffset, bitSize, value);
    }

    private static Operation mapToBitGetOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, BIT_OFFSET_KEY, BIT_SIZE_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, BIT_OFFSET_KEY, BIT_SIZE_KEY);

        String binName = getBinName(opValues);
        int bitOffset = getIntValue(opValues, BIT_OFFSET_KEY);
        int bitSize = getIntValue(opValues, BIT_SIZE_KEY);

        return BitOperation.get(binName, bitOffset, bitSize);
    }

    private static Operation mapToBitCountOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, BIT_OFFSET_KEY, BIT_SIZE_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, BIT_OFFSET_KEY, BIT_SIZE_KEY);

        String binName = getBinName(opValues);
        int bitOffset = getIntValue(opValues, BIT_OFFSET_KEY);
        int bitSize = getIntValue(opValues, BIT_SIZE_KEY);

        return BitOperation.count(binName, bitOffset, bitSize);
    }

    private static Operation mapToBitLscanOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, BIT_OFFSET_KEY, BIT_SIZE_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, BIT_OFFSET_KEY, BIT_SIZE_KEY, VALUE_KEY);

        String binName = getBinName(opValues);
        int bitOffset = getIntValue(opValues, BIT_OFFSET_KEY);
        int bitSize = getIntValue(opValues, BIT_SIZE_KEY);
        boolean value = getBoolValue(opValues, VALUE_KEY);

        return BitOperation.lscan(binName, bitOffset, bitSize, value);
    }

    private static Operation mapToBitRscanOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, BIT_OFFSET_KEY, BIT_SIZE_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, BIT_OFFSET_KEY, BIT_SIZE_KEY, VALUE_KEY);

        String binName = getBinName(opValues);
        int bitOffset = getIntValue(opValues, BIT_OFFSET_KEY);
        int bitSize = getIntValue(opValues, BIT_SIZE_KEY);
        boolean value = getBoolValue(opValues, VALUE_KEY);

        return BitOperation.rscan(binName, bitOffset, bitSize, value);
    }

    private static Operation mapToBitGetIntOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, BIT_OFFSET_KEY, BIT_SIZE_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, BIT_OFFSET_KEY, BIT_SIZE_KEY, SIGNED_KEY);

        String binName = getBinName(opValues);
        int bitOffset = getIntValue(opValues, BIT_OFFSET_KEY);
        int bitSize = getIntValue(opValues, BIT_SIZE_KEY);
        boolean signed = getBoolValue(opValues, SIGNED_KEY);

        return BitOperation.getInt(binName, bitOffset, bitSize, signed);
    }

    /* HLL OPERATIONS */

    private static Operation mapToHLLInitOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, HLL_INDEX_BIT_COUNT_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, HLL_INDEX_BIT_COUNT_KEY, HLL_MIN_HASH_BIT_COUNT_KEY);

        String binName = getBinName(opValues);
        int indexBitCount = getIntValue(opValues, HLL_INDEX_BIT_COUNT_KEY);
        int minHashBitCount = -1;
        if (opValues.containsKey(HLL_MIN_HASH_BIT_COUNT_KEY)) {
            minHashBitCount = getIntValue(opValues, HLL_MIN_HASH_BIT_COUNT_KEY);
        }

        return HLLOperation.init(HLLPolicy.Default, binName, indexBitCount, minHashBitCount);
    }

    private static Operation mapToHLLAddOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, VALUES_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, VALUES_KEY, HLL_INDEX_BIT_COUNT_KEY, HLL_MIN_HASH_BIT_COUNT_KEY);

        String binName = getBinName(opValues);
        int indexBitCount = -1;
        int minHashBitCount = -1;
        if (opValues.containsKey(HLL_INDEX_BIT_COUNT_KEY)) {
            indexBitCount = getIntValue(opValues, HLL_INDEX_BIT_COUNT_KEY);
        }
        if (opValues.containsKey(HLL_MIN_HASH_BIT_COUNT_KEY)) {
            minHashBitCount = getIntValue(opValues, HLL_MIN_HASH_BIT_COUNT_KEY);
        }
        List<Value> list = getValueList(opValues);

        return HLLOperation.add(HLLPolicy.Default, binName, list, indexBitCount, minHashBitCount);
    }

    private static Operation mapToHLLSetUnionOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, VALUES_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, VALUES_KEY);

        String binName = getBinName(opValues);
        List<Value.HLLValue> list = getHLLValueList(opValues);

        return HLLOperation.setUnion(HLLPolicy.Default, binName, list);
    }

    private static Operation mapToHLLRefreshCountOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY);

        String binName = getBinName(opValues);

        return HLLOperation.refreshCount(binName);
    }

    private static Operation mapToHLLFoldOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, HLL_INDEX_BIT_COUNT_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, HLL_INDEX_BIT_COUNT_KEY);

        String binName = getBinName(opValues);
        int indexBitCount = getIntValue(opValues, HLL_INDEX_BIT_COUNT_KEY);

        return HLLOperation.fold(binName, indexBitCount);
    }

    private static Operation mapToHLLGetCountOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY);

        String binName = getBinName(opValues);

        return HLLOperation.getCount(binName);
    }

    private static Operation mapToHLLGetUnionOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, VALUES_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, VALUES_KEY);

        String binName = getBinName(opValues);
        List<Value.HLLValue> list = getHLLValueList(opValues);

        return HLLOperation.getUnion(binName, list);
    }

    private static Operation mapToHLLGetUnionCountOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, VALUES_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, VALUES_KEY);

        String binName = getBinName(opValues);
        List<Value.HLLValue> list = getHLLValueList(opValues);

        return HLLOperation.getUnionCount(binName, list);
    }

    private static Operation mapToHLLGetIntersectCountOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, VALUES_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, VALUES_KEY);

        String binName = getBinName(opValues);
        List<Value.HLLValue> list = getHLLValueList(opValues);

        return HLLOperation.getIntersectCount(binName, list);
    }

    private static Operation mapToHLLGetSimilarityOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY, VALUES_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY, VALUES_KEY);

        String binName = getBinName(opValues);
        List<Value.HLLValue> list = getHLLValueList(opValues);

        return HLLOperation.getSimilarity(binName, list);
    }

    private static Operation mapToHLLDescribeOp(Map<String, Object> opValues) {
        hasAllRequiredKeys(opValues, BIN_KEY);
        onlyHasAllowedKeys(opValues, BIN_KEY);

        String binName = getBinName(opValues);

        return HLLOperation.describe(binName);
    }

    /*
     * Ensure that opValues contains an entry for each of the specified keys
     */
    private static void hasAllRequiredKeys(Map<String, Object> opValues, String... requiredKeys) {
        if (opValues == null) {
            throw new InvalidOperationError("\"opValues\" entry must be provided.");
        }
        for (String requiredKey : requiredKeys) {
            if (!opValues.containsKey(requiredKey)) {
                throw new InvalidOperationError("Missing required key: " + requiredKey);
            }
        }
    }

    /*
     * Ensure that opValues does not contain any keys not contained in allowedKeys.
     */
    private static void onlyHasAllowedKeys(Map<String, Object> opValues, String... allowedKeys) {
        Set<String> allowedKeySet = Stream.concat(Arrays.stream(allowedKeys), Arrays.stream(new String[]{"ctx"}))
                .collect(Collectors.toSet());

        for (String providedKey : opValues.keySet()) {
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
    private static ListPolicy mapToListPolicy(Map<String, Object> mapPolicy) {
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
                for (String flagName : (List<String>) mapPolicy.get(WRITE_FLAGS_KEY)) {
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

    private static byte[] getBitValue(Map<String, Object> map) {
        if (map.containsKey(VALUE_KEY)) {
            return Base64.getDecoder().decode((String) map.get(VALUE_KEY));
        } else {
            throw new InvalidOperationError(String.format("Missing %s key", VALUE_KEY));
        }
    }

    static boolean getBoolValue(Map<String, Object> map, String key) {
        if (map.containsKey(key)) {
            try {
                return (boolean) map.get(key);
            } catch (ClassCastException e) {
                return Boolean.parseBoolean((String) map.get(key));
            }
        } else {
            return false;
        }
    }

    static long getLongValue(Map<String, Object> map, String key) {
        try {
            return (long) map.get(key);
        } catch (ClassCastException e) {
            try {
                return (int) map.get(key);
            } catch (ClassCastException cce) {
                try {
                    return Long.parseLong((String) map.get(key));
                } catch (NumberFormatException nfe) {
                    throw new InvalidOperationError(String.format("%s is not numeric", key));
                }
            }
        }
    }

    private static BitOverflowAction getBitOverflowAction(Map<String, Object> map) {
        if (map.containsKey(BIT_OVERFLOW_ACTION_KEY)) {
            return switch (((String) map.get(BIT_OVERFLOW_ACTION_KEY)).toUpperCase()) {
                case "SATURATE" -> BitOverflowAction.SATURATE;
                case "WRAP" -> BitOverflowAction.WRAP;
                default -> BitOverflowAction.FAIL;
            };
        } else {
            return BitOverflowAction.FAIL;
        }
    }

    static Value getValue(Map<String, Object> map) {
        Object val = map.get(VALUE_KEY);
        if (val == null) {
            throw new InvalidOperationError(String.format("Missing %s key", VALUE_KEY));
        }
        return Value.get(val);
    }

    static Value getValueBegin(Map<String, Object> map) {
        if (map.containsKey(VALUE_BEGIN_KEY)) {
            return Value.get(map.get(VALUE_BEGIN_KEY));
        } else {
            return null;
        }
    }

    static Value getValueEnd(Map<String, Object> map) {
        if (map.containsKey(VALUE_END_KEY)) {
            return Value.get(map.get(VALUE_END_KEY));
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    static List<Value> getValueList(Map<String, Object> map) {
        List<Object> values;
        try {
            values = (List<Object>) map.get(VALUES_KEY);
        } catch (ClassCastException cce) {
            throw new InvalidOperationError("values must be a list");
        }
        List<Value> valueList = new ArrayList<>();
        for (Object value : values) {
            if (value instanceof Map<?, ?>) {
                value = new TreeMap<>((Map<?, ?>) value);
            }
            valueList.add(Value.get(value));
        }
        return valueList;
    }

    @SuppressWarnings("unchecked")
    static List<Value.HLLValue> getHLLValueList(Map<String, Object> map) {
        List<String> values;
        try {
            values = (List<String>) map.get(VALUES_KEY);
        } catch (ClassCastException cce) {
            throw new InvalidOperationError("values must be a list");
        }
        List<Value.HLLValue> valueList = new ArrayList<>();
        for (String value : values) {
            valueList.add(new Value.HLLValue(Base64.getDecoder().decode(value)));
        }
        return valueList;
    }

    @SuppressWarnings("unchecked")
    static ListPolicy getListPolicy(Map<String, Object> map) {
        if (!map.containsKey(LIST_POLICY_KEY)) {
            return null;
        }
        try {
            return mapToListPolicy((Map<String, Object>) map.get(LIST_POLICY_KEY));
        } catch (ClassCastException cce) {
            throw new InvalidOperationError("List Policy must be a map");
        }
    }

    static String getBinName(Map<String, Object> map) {
        try {
            return (String) map.get(BIN_KEY);
        } catch (ClassCastException cce) {
            throw new InvalidOperationError("bin must be string");
        }
    }

    static Value getIncr(Map<String, Object> map) {
        return Value.get(map.get(INCR_KEY));
    }

    static Value getOptionalIncr(Map<String, Object> map) {
        if (map.containsKey(INCR_KEY)) {
            return Value.get(map.get(INCR_KEY));
        }
        return null;
    }

    static int getIndex(Map<String, Object> map) {
        try {
            return getIntValue(map, INDEX_KEY);
        } catch (ClassCastException cce) {
            throw new InvalidOperationError("index must be an integer");
        }
    }

    static int getRank(Map<String, Object> map) {
        try {
            return getIntValue(map, RANK_KEY);
        } catch (ClassCastException cce) {
            throw new InvalidOperationError("rank must be an integer");
        }
    }

    static Integer getCount(Map<String, Object> map) {
        if (map.containsKey((COUNT_KEY))) {
            try {
                return getIntValue(map, COUNT_KEY);
            } catch (ClassCastException cce) {
                throw new InvalidOperationError("count must be an integer");
            }
        }
        return null;
    }

    static int getSortFlags(Map<String, Object> map) {
        String flagString;
        try {
            flagString = (String) map.get(LIST_SORT_FLAGS_KEY);
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
    static int getListReturnType(Map<String, Object> map) {
        boolean inverted = getInverted(map);
        String returnTypeString;
        try {
            returnTypeString = (String) map.get(LIST_RETURN_KEY);
        } catch (ClassCastException cce) {
            throw new InvalidOperationError("listReturnType must be a string");
        }

        com.aerospike.restclient.domain.operationmodels.ListReturnType listReturnType = ListReturnType.valueOf(
                returnTypeString);
        return listReturnType.toListReturnType(inverted);
    }

    /*
     * Get an integer suitable for passing as a list return type
     * this is the value of the LIST_RETURN_KEY field bitwise or'd with the value of the INVERTED_KEY field
     */
    static int getMapReturnType(Map<String, Object> map) {
        String returnTypeString;
        boolean inverted = getInverted(map);
        try {
            returnTypeString = (String) map.get(MAP_RETURN_KEY);
        } catch (ClassCastException cce) {
            throw new InvalidOperationError("mapReturnType must be a string");
        }

        com.aerospike.restclient.domain.operationmodels.MapReturnType mapReturnType = MapReturnType.valueOf(
                returnTypeString);

        return mapReturnType.toMapReturnType(inverted);
    }

    static boolean getInverted(Map<String, Object> map) {
        if (map.containsKey(INVERTED_KEY)) {
            try {
                return (boolean) map.get(INVERTED_KEY);
            } catch (ClassCastException cce) {
                throw new InvalidOperationError("inverted must be a boolean value");
            }
        }
        return false;
    }

    static ListOrder getListOrder(Map<String, Object> map) {
        String orderString;
        try {
            orderString = (String) map.get(LIST_ORDER_KEY);
        } catch (ClassCastException cce) {
            throw new InvalidOperationError("listOrder must be a string");
        }
        try {
            return ListOrder.valueOf(orderString);
        } catch (IllegalArgumentException e) {
            throw new InvalidOperationError("Invalid List order: " + orderString);
        }
    }

    static MapOrder getMapOrder(Map<String, Object> map) {
        String orderString;
        try {
            orderString = (String) map.get(MAP_ORDER_KEY);
        } catch (ClassCastException cce) {
            throw new InvalidOperationError("mapOrder must be a string");
        }
        try {
            return MapOrder.valueOf(orderString);
        } catch (IllegalArgumentException e) {
            throw new InvalidOperationError("Invalid Map order: " + orderString);
        }
    }

    @SuppressWarnings("unchecked")
    private static MapPolicy getMapPolicy(Map<String, Object> opValues) {
        if (!opValues.containsKey(MAP_POLICY_KEY)) {
            return MapPolicy.Default;
        }
        try {
            return mapToMapPolicy((Map<String, Object>) opValues.get(MAP_POLICY_KEY));
        } catch (ClassCastException cce) {
            throw new InvalidOperationError("mapPolicy must be an object");
        }
    }

    static Value getDecr(Map<String, Object> map) {
        return Value.get(map.get(DECR_KEY));
    }

    static Value getMapKey(Map<String, Object> map) {
        return Value.get(map.get(MAP_KEY_KEY));
    }

    @SuppressWarnings("unchecked")
    public static Map<Value, Value> getMapValues(Map<String, Object> map) {
        Map<Value, Value> valueMap = new HashMap<>();
        Map<Object, Object> objMap;
        try {
            objMap = (Map<Object, Object>) map.get(MAP_VALUES_KEY);
        } catch (ClassCastException cce) {
            throw new InvalidOperationError("map must be an object");
        }
        for (Object key : objMap.keySet()) {
            valueMap.put(Value.get(key), Value.get(objMap.get(key)));
        }

        return valueMap;
    }

    static Value getMapKeyBegin(Map<String, Object> map) {
        Object beginVal = map.get(MAP_KEY_BEGIN_KEY);
        return beginVal == null ? null : Value.get(beginVal);
    }

    static Value getMapKeyEnd(Map<String, Object> map) {
        Object endVal = map.get(MAP_KEY_END_KEY);
        return endVal == null ? null : Value.get(endVal);
    }

    @SuppressWarnings("unchecked")
    static List<Value> getMapKeys(Map<String, Object> map) {
        List<Object> objList;
        try {
            objList = (List<Object>) map.get(MAP_KEYS_KEY);
        } catch (ClassCastException cce) {
            throw new InvalidOperationError("keys must be a list");
        }
        List<Value> valueList = new ArrayList<>();
        for (Object value : objList) {
            valueList.add(Value.get(value));
        }
        return valueList;
    }

    private static MapPolicy mapToMapPolicy(Map<String, Object> mapPolicy) {
        if (mapPolicy == null) {
            return MapPolicy.Default;
        }
        MapOrder order;

        String orderString = (String) mapPolicy.get(ORDER_KEY);
        if (orderString == null) {
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
            } catch (IllegalArgumentException e) {
                throw new InvalidOperationError("Invalid write mode " + strWriteMode);
            }
        }

        return new MapPolicy(order, writeMode);
    }

    private static int getMapWriteFlags(Map<String, Object> mapPolicy) {
        try {
            int flags = 0;
            @SuppressWarnings("unchecked") List<String> writeFlags = (List<String>) mapPolicy.get(WRITE_FLAGS_KEY);
            for (String flagName : writeFlags) {
                switch (flagName) {
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
                        throw new InvalidOperationError(String.format("Unknown mapWriteFlags: %s", flagName));
                }
            }
            return flags;
        } catch (ClassCastException ccn) {
            throw new InvalidOperationError("writeFlags must be a list of strings");
        }
    }

    private static int getIntValue(Map<String, Object> map, String key) {
        Object nVal = map.get(key);
        try {
            return (int) nVal;
        } catch (ClassCastException cce) {
            try {
                return Math.toIntExact((Long) nVal);
            } catch (ArithmeticException e) {
                throw new InvalidOperationError(String.format("%s is too large", key));
            }
        }
    }

    private static com.aerospike.client.cdt.CTX[] extractCTX(Map<String, Object> opValues) {
        List<Map<String, Object>> ctxList = (List<Map<String, Object>>) opValues.get("ctx");

        if (ctxList == null) {
            return null;
        }

        return ctxList.stream().map(OperationConverter::mapToCTX).toArray(com.aerospike.client.cdt.CTX[]::new);
    }

    private static com.aerospike.client.cdt.CTX mapToCTX(Map<String, Object> ctxMao) {
        CTX restCTX = mapper.convertValue(ctxMao, CTX.class);
        return restCTX.toCTX();
    }
}