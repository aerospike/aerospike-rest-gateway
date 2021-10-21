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
package com.aerospike.restclient.util.annotations;

import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.aerospike.restclient.util.QueryParamDescriptors;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Parameters(value = {
        @Parameter(
                name = AerospikeAPIConstants.SEND_KEY,
                description = QueryParamDescriptors.POLICY_SEND_KEY_NOTES,
                schema = @Schema(type = "boolean"),
                in = ParameterIn.QUERY),
        @Parameter(
                name = AerospikeAPIConstants.REPLICA,
                description = QueryParamDescriptors.POLICY_REPLICA_NOTES,
                schema = @Schema(type = "string", allowableValues = QueryParamDescriptors.POLICY_REPLICA_ALLOWABLE_VALUES),
                in = ParameterIn.QUERY),
        @Parameter(
                name = AerospikeAPIConstants.KEY_TYPE,
                description = QueryParamDescriptors.KEYTYPE_NOTES,
                schema = @Schema(type = "string", allowableValues = QueryParamDescriptors.KEYTYPE_ALLOWABLE_VALUES),
                in = ParameterIn.QUERY),
        @Parameter(
                name = AerospikeAPIConstants.RECORD_BINS,
                description = QueryParamDescriptors.BINS_NOTES,
                array = @ArraySchema(schema = @Schema(type = "string")),
                in = ParameterIn.QUERY),
        @Parameter(
                name = AerospikeAPIConstants.READ_MODE_SC,
                description = QueryParamDescriptors.POLICY_READMODESC_NOTES,
                schema = @Schema(type = "string", allowableValues = QueryParamDescriptors.POLICY_READMODESC_ALLOWABLE_VALUES),
                in = ParameterIn.QUERY),
        @Parameter(
                name = AerospikeAPIConstants.READ_MODE_AP,
                description = QueryParamDescriptors.POLICY_READMODEAP_NOTES,
                schema = @Schema(type = "string", allowableValues = QueryParamDescriptors.POLICY_READMODEAP_ALLOWABLE_VALUES),
                in = ParameterIn.QUERY),
        @Parameter(
                name = AerospikeAPIConstants.TOTAL_TIMEOUT,
                description = QueryParamDescriptors.POLICY_TOTAL_TIMEOUT_NOTES,
                schema = @Schema(type = "integer"),
                in = ParameterIn.QUERY),
        @Parameter(
                name = AerospikeAPIConstants.SOCKET_TIMEOUT,
                description = QueryParamDescriptors.POLICY_SOCKET_TIMEOUT_NOTES,
                schema = @Schema(type = "integer"),
                in = ParameterIn.QUERY),
        @Parameter(
                name = AerospikeAPIConstants.SLEEP_BETWEEN_RETRIES,
                description = QueryParamDescriptors.POLICY_SLEEP_BETWEEN_RETRIES_NOTES,
                schema = @Schema(type = "integer"),
                in = ParameterIn.QUERY),
        @Parameter(
                name = AerospikeAPIConstants.MAX_RETRIES,
                description = QueryParamDescriptors.POLICY_MAX_RETRIES_NOTES,
                schema = @Schema(type = "integer"),
                in = ParameterIn.QUERY),
        @Parameter(
                name = AerospikeAPIConstants.PRED_EXP,
                description = QueryParamDescriptors.POLICY_PRED_EXP_NOTES,
                schema = @Schema(type = "string"),
                in = ParameterIn.QUERY),
        @Parameter(
                name = AerospikeAPIConstants.FILTER_EXP,
                description = QueryParamDescriptors.POLICY_FILTER_EXP_NOTES,
                schema = @Schema(type = "string"),
                in = ParameterIn.QUERY),
        @Parameter(
                name = AerospikeAPIConstants.COMPRESS,
                description = QueryParamDescriptors.POLICY_COMPRESS_NOTES,
                schema = @Schema(type = "boolean"),
                in = ParameterIn.QUERY)
})
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ASRestClientPolicyQueryParams {
}
