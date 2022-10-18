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
package com.aerospike.restclient.converters;

import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.aerospike.restclient.util.AerospikeOperation;
import com.aerospike.restclient.util.RestClientErrors.InvalidOperationError;
import com.aerospike.restclient.util.converters.OperationConverter;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class OperationConverterErrors {

    @Test(expected = InvalidOperationError.class)
    public void testUnknownOp() {
        Map<String, Object> op = new HashMap<>();
        op.put(AerospikeAPIConstants.OPERATION_FIELD, "the_board_game");
        OperationConverter.convertMapToOperation(op);
    }

    @Test(expected = InvalidOperationError.class)
    public void testOperationWithoutOperationKey() {
        Map<String, Object> op = new HashMap<>();
        OperationConverter.convertMapToOperation(op);
    }

    @Test(expected = InvalidOperationError.class)
    public void testMissingOpValues() {
        Map<String, Object> op = new HashMap<>();
        op.put(AerospikeAPIConstants.OPERATION_FIELD, AerospikeOperation.READ);
        OperationConverter.convertMapToOperation(op);
    }

    /*
     * If the user accidentally provides an extra key at the top level, we should error out.
     */
    @Test(expected = InvalidOperationError.class)
    public void testAdditionalTopLevelValue() {
        Map<String, Object> opValues = new HashMap<>();
        Map<String, Object> op = new HashMap<>();
        op.put(AerospikeAPIConstants.OPERATION_FIELD, AerospikeOperation.READ);
        op.put("An Extra", "Field");
        opValues.put("bin", "binname");
        op.put(AerospikeAPIConstants.OPERATION_VALUES_FIELD, opValues);
        OperationConverter.convertMapToOperation(op);
    }

    @Test(expected = InvalidOperationError.class)
    public void testMissingValueInOpvalue() {
        /*
         * This should look like {
         * "operation": "READ",
         * "opValues": {"bin":"name"}
         *
         * }
         * "We omit the "bin" entry
         */
        Map<String, Object> op = new HashMap<>();
        op.put(AerospikeAPIConstants.OPERATION_FIELD, AerospikeOperation.READ);
        Map<String, Object> op_values = new HashMap<>();
        op.put(AerospikeAPIConstants.OPERATION_VALUES_FIELD, op_values);
        OperationConverter.convertMapToOperation(op);
    }

    @Test(expected = InvalidOperationError.class)
    public void testExtraValueInOpValue() {
        /*
         * This should look like {
         * "operation": "READ",
         * "opValues": {"bin":"name"}
         *
         * }
         * "We omit the "bin" entry
         */
        Map<String, Object> op = new HashMap<>();
        op.put(AerospikeAPIConstants.OPERATION_FIELD, AerospikeOperation.READ);
        Map<String, Object> op_values = new HashMap<>();
        op_values.put("bin", "binname");
        op_values.put("extra_val", "arbitrary");
        op.put(AerospikeAPIConstants.OPERATION_VALUES_FIELD, op_values);
        OperationConverter.convertMapToOperation(op);
    }

    @Test(expected = InvalidOperationError.class)
    public void testStringValueIsNonString() {
        /*
         * This should look like {
         * "operation": "READ",
         * "opValues": {"bin":"name"}
         *
         * }
         * "We omit the "bin" entry
         */
        Map<String, Object> op = new HashMap<>();
        op.put(AerospikeAPIConstants.OPERATION_FIELD, AerospikeOperation.READ);
        Map<String, Object> op_values = new HashMap<>();
        op_values.put("bin", 3.14);
        op.put(AerospikeAPIConstants.OPERATION_VALUES_FIELD, op_values);

        OperationConverter.convertMapToOperation(op);
    }

    @Test(expected = InvalidOperationError.class)
    public void testIntegerValueIsNonInt() {
        /*
         * This should look like {
         * "operation": "READ",
         * "opValues": {"bin":"name"}
         *
         * }
         * "We omit the "bin" entry
         */
        Map<String, Object> op = new HashMap<>();
        op.put(AerospikeAPIConstants.OPERATION_FIELD, AerospikeOperation.LIST_GET);
        Map<String, Object> op_values = new HashMap<>();
        op_values.put("bin", "list_bin");
        op_values.put("index", "notanumber");

        op.put(AerospikeAPIConstants.OPERATION_VALUES_FIELD, op_values);
        OperationConverter.convertMapToOperation(op);
    }
}
