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
package com.aerospike.restclient.domain.operationmodels;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = HLLInitOperation.class, name = OperationTypes.HLL_INIT),
        @JsonSubTypes.Type(value = HLLAddOperation.class, name = OperationTypes.HLL_ADD),
        @JsonSubTypes.Type(value = HLLSetUnionOperation.class, name = OperationTypes.HLL_SET_UNION),
        @JsonSubTypes.Type(value = HLLRefreshCountOperation.class, name = OperationTypes.HLL_SET_COUNT),
        @JsonSubTypes.Type(value = HLLFoldOperation.class, name = OperationTypes.HLL_FOLD),
        @JsonSubTypes.Type(value = HLLGetCountOperation.class, name = OperationTypes.HLL_COUNT),
        @JsonSubTypes.Type(value = HLLGetUnionOperation.class, name = OperationTypes.HLL_UNION),
        @JsonSubTypes.Type(value = HLLGetUnionCountOperation.class, name = OperationTypes.HLL_UNION_COUNT),
        @JsonSubTypes.Type(value = HLLGetIntersectionCountOperation.class, name = OperationTypes.HLL_INTERSECT_COUNT),
        @JsonSubTypes.Type(value = HLLGetSimilarityOperation.class, name = OperationTypes.HLL_SIMILARITY),
        @JsonSubTypes.Type(value = HLLDescribeOperation.class, name = OperationTypes.HLL_DESCRIBE),
})
@Schema(
        description = "The base type for describing all HLL operations. Should not be used directly.",
        oneOf = {
                HLLInitOperation.class,
                HLLAddOperation.class,
                HLLSetUnionOperation.class,
                HLLRefreshCountOperation.class,
                HLLFoldOperation.class,
                HLLGetCountOperation.class,
                HLLGetUnionOperation.class,
                HLLGetUnionCountOperation.class,
                HLLGetIntersectionCountOperation.class,
                HLLGetSimilarityOperation.class,
                HLLDescribeOperation.class
        }
)
public abstract class HLLOperation extends Operation {
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    protected String binName;

    protected HLLOperation(String binName) {
        this.binName = binName;
    }
}
