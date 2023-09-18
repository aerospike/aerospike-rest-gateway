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
        description = "Increment the map value with the specified key by the specified amount.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/" + AerospikeAPIConstants.AS_CLIENT_VERSION + "/com/aerospike/client/cdt/MapOperation.html")
)
public class MapIncrementOperation extends MapOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.MAP_INCREMENT,
            requiredMode = Schema.RequiredMode.REQUIRED,
            allowableValues = {OperationTypes.MAP_INCREMENT}
    )
    public final String type = OperationTypes.MAP_INCREMENT;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private final Number incr;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private final Object key;

    private MapPolicy mapPolicy;

    @JsonCreator
    public MapIncrementOperation(
            @JsonProperty(value = "binName")
            @Schema(name = "binName", requiredMode = Schema.RequiredMode.REQUIRED) String binName,
            @JsonProperty(value = "incr")
            @Schema(name = "incr", requiredMode = Schema.RequiredMode.REQUIRED) Number incr,
            @JsonProperty(value = "key")
            @Schema(name = "key", requiredMode = Schema.RequiredMode.REQUIRED) Object key
    ) {
        super(binName);
        this.incr = incr;
        this.key = key;
    }

    public MapPolicy getMapPolicy() {
        return mapPolicy;
    }

    public void setMapPolicy(MapPolicy mapPolicy) {
        this.mapPolicy = mapPolicy;
    }

    @Override
    public com.aerospike.client.Operation toOperation() {
        com.aerospike.client.cdt.CTX[] asCTX = getASCTX();
        com.aerospike.client.cdt.MapPolicy asMapPolicy;

        if (mapPolicy == null) {
            asMapPolicy = com.aerospike.client.cdt.MapPolicy.Default;
        } else {
            asMapPolicy = mapPolicy.toMapPolicy();
        }

        Value asIncr;

        if (incr instanceof Integer) {
            asIncr = Value.get(incr.intValue());
        } else {
            asIncr = Value.get(incr.doubleValue());
        }

        return com.aerospike.client.cdt.MapOperation.increment(asMapPolicy, binName, Value.get(key), asIncr, asCTX);
    }
}
