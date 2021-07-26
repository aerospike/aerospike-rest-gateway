/*
 * Copyright 2021 Aerospike, Inc.
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
package com.aerospike.restclient.util.annotations;

import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.aerospike.restclient.util.QueryParamDescriptors;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ApiImplicitParams(value = {
        @ApiImplicitParam(
                name = AerospikeAPIConstants.SEND_KEY,
                dataType = "boolean",
                paramType = "query",
                value = QueryParamDescriptors.POLICY_SEND_KEY_NOTES),
        @ApiImplicitParam(
                name = AerospikeAPIConstants.REPLICA,
                dataType = "string",
                paramType = "query",
                allowableValues = QueryParamDescriptors.POLICY_REPLICA_ALLOWABLE_VALUES,
                value = QueryParamDescriptors.POLICY_REPLICA_NOTES),
        @ApiImplicitParam(
                name = AerospikeAPIConstants.KEY_TYPE,
                dataType = "string",
                paramType = "query",
                allowableValues = QueryParamDescriptors.KEYTYPE_ALLOWABLE_VALUES,
                value = QueryParamDescriptors.KEYTYPE_NOTES),
        @ApiImplicitParam(
                name = AerospikeAPIConstants.RECORD_BINS,
                paramType = "query",
                value = QueryParamDescriptors.JSON_PATH_BINS_NOTES,
                dataType = "string",
                required = true,
                allowMultiple = true),
        @ApiImplicitParam(
                name = AerospikeAPIConstants.READ_MODE_SC,
                dataType = "string",
                paramType = "query",
                allowableValues = QueryParamDescriptors.POLICY_READMODESC_ALLOWABLE_VALUES,
                value = QueryParamDescriptors.POLICY_READMODESC_NOTES),
        @ApiImplicitParam(
                name = AerospikeAPIConstants.READ_MODE_AP,
                dataType = "string",
                paramType = "query",
                allowableValues = QueryParamDescriptors.POLICY_READMODEAP_ALLOWABLE_VALUES,
                value = QueryParamDescriptors.POLICY_READMODEAP_NOTES),
        @ApiImplicitParam(
                name = AerospikeAPIConstants.TOTAL_TIMEOUT,
                dataType = "int",
                paramType = "query",
                value = QueryParamDescriptors.POLICY_TOTAL_TIMEOUT_NOTES),
        @ApiImplicitParam(
                name = AerospikeAPIConstants.SOCKET_TIMEOUT,
                dataType = "int",
                paramType = "query",
                value = QueryParamDescriptors.POLICY_SOCKET_TIMEOUT_NOTES),
        @ApiImplicitParam(
                name = AerospikeAPIConstants.SLEEP_BETWEEN_RETRIES,
                dataType = "int",
                paramType = "query",
                value = QueryParamDescriptors.POLICY_SLEEP_BETWEEN_RETRIES_NOTES),
        @ApiImplicitParam(
                name = AerospikeAPIConstants.MAX_RETRIES,
                dataType = "int",
                paramType = "query",
                value = QueryParamDescriptors.POLICY_MAX_RETRIES_NOTES),
        @ApiImplicitParam(
                name = AerospikeAPIConstants.PRED_EXP,
                dataType = "string",
                paramType = "query",
                value = QueryParamDescriptors.POLICY_PRED_EXP_NOTES),
        @ApiImplicitParam(
                name = AerospikeAPIConstants.FILTER_EXP,
                dataType = "string",
                paramType = "query",
                value = QueryParamDescriptors.POLICY_FILTER_EXP_NOTES),
        @ApiImplicitParam(
                name = AerospikeAPIConstants.COMPRESS,
                dataType = "boolean",
                paramType = "query",
                value = QueryParamDescriptors.POLICY_COMPRESS_NOTES),
        @ApiImplicitParam(
                name = AerospikeAPIConstants.JSON_PATH,
                dataType = "string",
                paramType = "query",
                required = true,
                value = QueryParamDescriptors.JSON_PATH_NOTES),
})
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ASRestDocumentPolicyQueryParams {
}
