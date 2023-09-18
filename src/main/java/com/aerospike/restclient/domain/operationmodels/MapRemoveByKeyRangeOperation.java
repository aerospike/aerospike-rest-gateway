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

@Schema(
        description = "Remove and return map values with keys in the specified range. If `keyBegin` is omitted, all map values with key values less than `keyEnd` will be removed and returned. If `keyEnd` is omitted, all map values with a key greater than or equal to `keyBegin` will be removed and returned.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/" + AerospikeAPIConstants.AS_CLIENT_VERSION + "/com/aerospike/client/cdt/MapOperation.html")
)
public class MapRemoveByKeyRangeOperation extends MapOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.MAP_REMOVE_BY_KEY_RANGE,
            requiredMode = Schema.RequiredMode.REQUIRED,
            allowableValues = {OperationTypes.MAP_REMOVE_BY_KEY_RANGE}
    )
    public final String type = OperationTypes.MAP_REMOVE_BY_KEY_RANGE;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private final MapReturnType mapReturnType;

    private boolean inverted;

    private Object keyBegin;

    private Object keyEnd;

    @JsonCreator
    public MapRemoveByKeyRangeOperation(
            @JsonProperty(value = "binName")
            @Schema(name = "binName", requiredMode = Schema.RequiredMode.REQUIRED) String binName,
            @JsonProperty(value = "mapReturnType", required = true) MapReturnType mapReturnType
    ) {
        super(binName);
        this.mapReturnType = mapReturnType;
        inverted = false;
    }

    public Object getKeyBegin() {
        return keyBegin;
    }

    public void setKeyBegin(Object keyBegin) {
        this.keyBegin = keyBegin;
    }

    public Object getKeyEnd() {
        return keyEnd;
    }

    public void setKeyEnd(Object keyEnd) {
        this.keyEnd = keyEnd;
    }

    public boolean isInverted() {
        return inverted;
    }

    public void setInverted(boolean inverted) {
        this.inverted = inverted;
    }

    @Override
    public com.aerospike.client.Operation toOperation() {
        com.aerospike.client.cdt.CTX[] asCTX = getASCTX();
        Value begin = null;
        Value end = null;

        if (keyBegin != null) {
            begin = Value.get(keyBegin);
        }

        if (keyEnd != null) {
            end = Value.get(keyEnd);
        }

        return com.aerospike.client.cdt.MapOperation.removeByKeyRange(binName, begin, end,
                mapReturnType.toMapReturnType(inverted), asCTX);
    }
}
