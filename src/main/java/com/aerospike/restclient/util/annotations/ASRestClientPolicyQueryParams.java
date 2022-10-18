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
package com.aerospike.restclient.util.annotations;

import com.aerospike.client.policy.ReadModeAP;
import com.aerospike.client.policy.ReadModeSC;
import com.aerospike.client.policy.Replica;
import com.aerospike.restclient.util.APIDescriptors;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Parameters(
        value = {
                @Parameter(
                        name = AerospikeAPIConstants.SEND_KEY,
                        description = APIDescriptors.POLICY_SEND_KEY_NOTES,
                        schema = @Schema(type = "boolean"),
                        in = ParameterIn.QUERY
                ), @Parameter(
                name = AerospikeAPIConstants.REPLICA,
                description = APIDescriptors.POLICY_REPLICA_NOTES,
                schema = @Schema(implementation = Replica.class),
                in = ParameterIn.QUERY
        ), @Parameter(
                name = AerospikeAPIConstants.READ_MODE_SC,
                description = APIDescriptors.POLICY_READMODESC_NOTES,
                schema = @Schema(implementation = ReadModeSC.class),
                in = ParameterIn.QUERY
        ), @Parameter(
                name = AerospikeAPIConstants.READ_MODE_AP,
                description = APIDescriptors.POLICY_READMODEAP_NOTES,
                schema = @Schema(implementation = ReadModeAP.class),
                in = ParameterIn.QUERY
        ), @Parameter(
                name = AerospikeAPIConstants.TOTAL_TIMEOUT,
                description = APIDescriptors.POLICY_TOTAL_TIMEOUT_NOTES,
                schema = @Schema(type = "integer"),
                in = ParameterIn.QUERY
        ), @Parameter(
                name = AerospikeAPIConstants.SOCKET_TIMEOUT,
                description = APIDescriptors.POLICY_SOCKET_TIMEOUT_NOTES,
                schema = @Schema(type = "integer"),
                in = ParameterIn.QUERY
        ), @Parameter(
                name = AerospikeAPIConstants.SLEEP_BETWEEN_RETRIES,
                description = APIDescriptors.POLICY_SLEEP_BETWEEN_RETRIES_NOTES,
                schema = @Schema(type = "integer"),
                in = ParameterIn.QUERY
        ), @Parameter(
                name = AerospikeAPIConstants.MAX_RETRIES,
                description = APIDescriptors.POLICY_MAX_RETRIES_NOTES,
                schema = @Schema(type = "integer"),
                in = ParameterIn.QUERY
        ), @Parameter(
                name = AerospikeAPIConstants.FILTER_EXP,
                description = APIDescriptors.POLICY_FILTER_EXP_NOTES,
                schema = @Schema(type = "string"),
                in = ParameterIn.QUERY
        ), @Parameter(
                name = AerospikeAPIConstants.COMPRESS,
                description = APIDescriptors.POLICY_COMPRESS_NOTES,
                schema = @Schema(type = "boolean"),
                in = ParameterIn.QUERY
        )
        }
)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ASRestClientPolicyQueryParams {
}
