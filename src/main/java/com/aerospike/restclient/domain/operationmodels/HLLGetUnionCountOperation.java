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

import com.aerospike.client.Value;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(
        description = "Server returns estimated number of elements that would be contained by the union of these HLL objects.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/" + AerospikeAPIConstants.AS_CLIENT_VERSION + "/com/aerospike/client/operation/HLLOperation.html")
)
public class HLLGetUnionCountOperation extends HLLOperation {
    @Schema(
            description = "The type of operation. It is always " + OperationTypes.HLL_UNION_COUNT,
            requiredMode = Schema.RequiredMode.REQUIRED,
            allowableValues = {OperationTypes.HLL_UNION_COUNT}
    )
    public final String type = OperationTypes.HLL_UNION_COUNT;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private final List<byte[]> values;

    @JsonCreator
    public HLLGetUnionCountOperation(
            @JsonProperty("binName") String binName,
            @JsonProperty("values") List<byte[]> values
    ) {
        super(binName);
        this.values = values;
    }

    @Override
    public com.aerospike.client.Operation toOperation() {
        List<Value.HLLValue> asVals = values.stream().map(Value.HLLValue::new).toList();
        return com.aerospike.client.operation.HLLOperation.getUnionCount(binName, asVals);
    }
}
