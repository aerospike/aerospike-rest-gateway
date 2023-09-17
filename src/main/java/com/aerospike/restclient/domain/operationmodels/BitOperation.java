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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = BitResizeOperation.class, name = OperationTypes.BIT_RESIZE),
        @JsonSubTypes.Type(value = BitInsertOperation.class, name = OperationTypes.BIT_INSERT),
        @JsonSubTypes.Type(value = BitRemoveOperation.class, name = OperationTypes.BIT_REMOVE),
        @JsonSubTypes.Type(value = BitSetOperation.class, name = OperationTypes.BIT_SET),
        @JsonSubTypes.Type(value = BitOrOperation.class, name = OperationTypes.BIT_OR),
        @JsonSubTypes.Type(value = BitXOrOperation.class, name = OperationTypes.BIT_XOR),
        @JsonSubTypes.Type(value = BitAndOperation.class, name = OperationTypes.BIT_AND),
        @JsonSubTypes.Type(value = BitNotOperation.class, name = OperationTypes.BIT_NOT),
        @JsonSubTypes.Type(value = BitLShiftOperation.class, name = OperationTypes.BIT_LSHIFT),
        @JsonSubTypes.Type(value = BitRShiftOperation.class, name = OperationTypes.BIT_RSHIFT),
        @JsonSubTypes.Type(value = BitAddOperation.class, name = OperationTypes.BIT_ADD),
        @JsonSubTypes.Type(value = BitSubtractOperation.class, name = OperationTypes.BIT_SUBTRACT),
        @JsonSubTypes.Type(value = BitSetOperation.class, name = OperationTypes.BIT_SET),
        @JsonSubTypes.Type(value = BitGetOperation.class, name = OperationTypes.BIT_GET),
        @JsonSubTypes.Type(value = BitCountOperation.class, name = OperationTypes.BIT_COUNT),
        @JsonSubTypes.Type(value = BitLScanOperation.class, name = OperationTypes.BIT_LSCAN),
        @JsonSubTypes.Type(value = BitRScanOperation.class, name = OperationTypes.BIT_RSCAN),
        @JsonSubTypes.Type(value = BitSetIntOperation.class, name = OperationTypes.BIT_SET_INT),
        @JsonSubTypes.Type(value = BitGetIntOperation.class, name = OperationTypes.BIT_GET_INT)
})
@Schema(
        description = "The base type for describing all bit operations. Should not be used directly.",
        oneOf = {
                BitResizeOperation.class,
                BitInsertOperation.class,
                BitRemoveOperation.class,
                BitSetOperation.class,
                BitOrOperation.class,
                BitXOrOperation.class,
                BitAndOperation.class,
                BitNotOperation.class,
                BitLShiftOperation.class,
                BitRShiftOperation.class,
                BitAddOperation.class,
                BitSubtractOperation.class,
                BitSetOperation.class,
                BitGetOperation.class,
                BitCountOperation.class,
                BitLScanOperation.class,
                BitRScanOperation.class,
                BitSetIntOperation.class,
                BitGetIntOperation.class,
        }
)
public abstract class BitOperation extends Operation {
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    protected String binName;

    @JsonCreator
    protected BitOperation(
            @JsonProperty(value = "binName")
            @Schema(name = "binName", requiredMode = Schema.RequiredMode.REQUIRED) String binName
    ) {
        this.binName = binName;
    }
}
