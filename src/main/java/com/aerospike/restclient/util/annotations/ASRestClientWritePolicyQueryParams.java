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

import com.aerospike.client.policy.CommitLevel;
import com.aerospike.client.policy.GenerationPolicy;
import com.aerospike.client.policy.RecordExistsAction;
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

/*
 * Annotations used by Swagger to report correct query parameters for the given API method.
 */
@Parameters(
        value = {
                @Parameter(
                        name = AerospikeAPIConstants.EXPIRATION,
                        description = APIDescriptors.WRITE_POLICY_EXPIRATION_NOTES,
                        schema = @Schema(type = "integer"),
                        in = ParameterIn.QUERY
                ), @Parameter(
                name = AerospikeAPIConstants.GENERATION,
                description = APIDescriptors.WRITE_POLICY_GEN_NOTES,
                schema = @Schema(type = "integer"),
                in = ParameterIn.QUERY
        ), @Parameter(
                name = AerospikeAPIConstants.DURABLE_DELETE,
                description = APIDescriptors.WRITE_POLICY_DURABLE_DELETE_NOTES,
                schema = @Schema(type = "boolean"),
                in = ParameterIn.QUERY
        ), @Parameter(
                name = AerospikeAPIConstants.RESPOND_ALL_OPS,
                description = APIDescriptors.WRITE_POLICY_RESPOND_ALL_OPS_NOTES,
                schema = @Schema(type = "boolean"),
                in = ParameterIn.QUERY
        ), @Parameter(
                name = AerospikeAPIConstants.COMMIT_LEVEL,
                description = APIDescriptors.WRITE_POLICY_COMMIT_LEVEL_NOTES,
                schema = @Schema(implementation = CommitLevel.class),
                in = ParameterIn.QUERY
        ), @Parameter(
                name = AerospikeAPIConstants.GENERATION_POLICY,
                description = APIDescriptors.WRITE_POLICY_GEN_POLICY_NOTES,
                schema = @Schema(implementation = GenerationPolicy.class),
                in = ParameterIn.QUERY
        ), @Parameter(
                name = AerospikeAPIConstants.RECORD_EXISTS_ACTION,
                description = APIDescriptors.WRITE_POLICY_RECORD_EXISTS_NOTES,
                schema = @Schema(implementation = RecordExistsAction.class),
                in = ParameterIn.QUERY
        )
        }
)
@ASRestClientPolicyQueryParams
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ASRestClientWritePolicyQueryParams {
}
