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
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ASRestClientPolicyQueryParams
@Parameters(
        value = {
                @Parameter(
                        name = AerospikeAPIConstants.JSON_PATH,
                        description = APIDescriptors.JSON_PATH_NOTES,
                        schema = @Schema(type = "string", requiredMode = Schema.RequiredMode.REQUIRED),
                        required = true,
                        in = ParameterIn.QUERY
                ), @Parameter(
                name = AerospikeAPIConstants.RECORD_BINS,
                description = APIDescriptors.JSON_PATH_BINS_NOTES,
                array = @ArraySchema(schema = @Schema(type = "string", requiredMode = Schema.RequiredMode.REQUIRED)),
                required = true,
                in = ParameterIn.QUERY
        ),
        }
)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ASRestDocumentPolicyQueryParams {
}
