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

/* Annotation which tells swagger to render all of the Scan Policy options as query parameters */
@Parameters(
        value = {
                @Parameter(
                        name = AerospikeAPIConstants.MAX_RECORDS,
                        description = APIDescriptors.SCAN_POLICY_MAX_RECORDS_NOTES,
                        schema = @Schema(type = "integer"),
                        in = ParameterIn.QUERY
                ), @Parameter(
                name = AerospikeAPIConstants.RECORDS_PER_SECOND,
                description = APIDescriptors.SCAN_POLICY_RECORDS_PER_SECOND_NOTES,
                schema = @Schema(type = "integer"),
                in = ParameterIn.QUERY
        ), @Parameter(
                name = AerospikeAPIConstants.MAX_CONCURRENT_NODES,
                description = APIDescriptors.SCAN_POLICY_MAX_CONCURRENT_NODES_NOTES,
                schema = @Schema(type = "integer"),
                in = ParameterIn.QUERY
        ), @Parameter(
                name = AerospikeAPIConstants.CONCURRENT_NODES,
                description = APIDescriptors.SCAN_POLICY_CONCURRENT_NODES_NOTES,
                schema = @Schema(type = "boolean"),
                in = ParameterIn.QUERY
        ), @Parameter(
                name = AerospikeAPIConstants.INCLUDE_BIN_DATA,
                description = APIDescriptors.SCAN_POLICY_INCLUDE_BIN_DATA_NOTES,
                schema = @Schema(type = "boolean"),
                in = ParameterIn.QUERY
        ),
        }
)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ASRestClientPolicyQueryParams
public @interface ASRestClientScanPolicyQueryParams {
}
