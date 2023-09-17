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
package com.aerospike.restclient.domain.ctxmodels;

import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "CTX",
        description = "The base type for describing a nested CDT context. Identifies the location of nested list/map to apply the operation.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/" + AerospikeAPIConstants.AS_CLIENT_VERSION + "/com/aerospike/client/cdt/CTX.html"),
        oneOf = {
                ListIndexCTX.class,
                ListRankCTX.class,
                ListValueCTX.class,
                MapIndexCTX.class,
                MapRankCTX.class,
                MapKeyCTX.class,
                MapValueCTX.class,
                MapKeyCreateCTX.class,
                ListIndexCreateCTX.class,
        }
)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ListIndexCTX.class, name = AerospikeAPIConstants.CTX.LIST_INDEX),
        @JsonSubTypes.Type(value = ListRankCTX.class, name = AerospikeAPIConstants.CTX.LIST_RANK),
        @JsonSubTypes.Type(value = ListValueCTX.class, name = AerospikeAPIConstants.CTX.LIST_VALUE),
        @JsonSubTypes.Type(value = MapIndexCTX.class, name = AerospikeAPIConstants.CTX.MAP_INDEX),
        @JsonSubTypes.Type(value = MapRankCTX.class, name = AerospikeAPIConstants.CTX.MAP_RANK),
        @JsonSubTypes.Type(value = MapKeyCTX.class, name = AerospikeAPIConstants.CTX.MAP_KEY),
        @JsonSubTypes.Type(value = MapValueCTX.class, name = AerospikeAPIConstants.CTX.MAP_VALUE),
        @JsonSubTypes.Type(value = MapKeyCreateCTX.class, name = AerospikeAPIConstants.CTX.MAP_KEY_CREATE),
        @JsonSubTypes.Type(value = ListIndexCreateCTX.class, name = AerospikeAPIConstants.CTX.LIST_INDEX_CREATE),
})
public abstract class CTX {
    public abstract com.aerospike.client.cdt.CTX toCTX();
}
