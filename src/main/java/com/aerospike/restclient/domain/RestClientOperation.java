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
package com.aerospike.restclient.domain;

import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.aerospike.restclient.util.AerospikeOperation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.HashMap;
import java.util.Map;

/**
 * Deprecated. This class, in conjunction with OperationConverter should only be used in /v1/operate and /v1/execute until removed.
 * The class was designed to convert maps in the format "{"operation": operation, "opValues":{...}}" to
 * the appropriate aerospike java client operation. Now that the rest gateway has been transitioned to use swagger docs
 * all the Operations need to be represented with a model so that its format can be auto documented. An added plus is improved
 * error messages and the ability to independently design the schema for each model i.e. no need for "operation" and "opValues" keys.
 * Class containing static methods used for converting Java Maps to Aerospike Operations.
 */
@Deprecated
@Schema(
        description = "Deprecated in favor of more descriptive models.  The documentation for the old models can be found in the external documentation.",
        externalDocs = @ExternalDocumentation(url = "@Schema(description = \"Deprecated in favor of more descriptive models.  The documentation for the old models can be found in the external documentation.\", externalDocs = @ExternalDocumentation(url= \"\"))\n")
)
public class RestClientOperation {

    public RestClientOperation() {
    }

    public RestClientOperation(AerospikeOperation operation, Map<String, Object> values) {
        this.operation = operation;
        this.opValues = values;
    }

    @SuppressWarnings("unchecked")
    public RestClientOperation(Map<String, Object> opMap) {
        this.operation = AerospikeOperation.valueOf((String) opMap.get(AerospikeAPIConstants.OPERATION_FIELD));
        this.opValues = (Map<String, Object>) opMap.get(AerospikeAPIConstants.OPERATION_VALUES_FIELD);
    }

    @Schema(
            requiredMode = Schema.RequiredMode.REQUIRED,
            description = "Aerospike operation to perform on the record",
            example = "LIST_APPEND_ITEMS"
    )
    private AerospikeOperation operation;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "{\"bin\":\"listbin\", \"values\":[1,2,3]}")
    private Map<String, Object> opValues;

    public AerospikeOperation getOperation() {
        return this.operation;
    }

    public Map<String, Object> getOpValues() {
        return this.opValues;
    }

    public void setOperation(AerospikeOperation op) {
        this.operation = op;
    }

    public void setOpValues(Map<String, Object> opVals) {
        this.opValues = opVals;
    }

    @JsonIgnore
    public Map<String, Object> toMap() {
        Map<String, Object> mapRepresentation = new HashMap<>();
        mapRepresentation.put(AerospikeAPIConstants.OPERATION_FIELD, operation);
        mapRepresentation.put(AerospikeAPIConstants.OPERATION_VALUES_FIELD, opValues);
        return mapRepresentation;
    }
}
