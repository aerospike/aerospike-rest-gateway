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
import com.aerospike.restclient.util.RestClientErrors;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Lookup list by index offset.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/" + AerospikeAPIConstants.AS_CLIENT_VERSION + "/com/aerospike/client/cdt/CTX.html")
)
public class ListIndexCTX extends CTX {
    @Schema(
            description = "The type of context this object represents. It is always " + AerospikeAPIConstants.CTX.LIST_INDEX,
            allowableValues = {AerospikeAPIConstants.CTX.LIST_INDEX},
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    public final String type = AerospikeAPIConstants.CTX.LIST_INDEX;

    @Schema(
            description = "If the index is negative, the resolved index starts backwards from end of list. If an index is out of bounds, a parameter error will be returned. Examples:\n" + "* 0: First item.\n" + "* 4: Fifth item.\n" + "* -1: Last item.\n" + "* -3: Third to last item.",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    public Integer index;

    public ListIndexCTX() {
    }

    public ListIndexCTX(int index) {
        this.index = index;
    }

    @Override
    public com.aerospike.client.cdt.CTX toCTX() {
        if (index == null) {
            // TODO: Use javax.validation for our model validation. Not sure on performance implications.
            throw new RestClientErrors.InvalidCTXError("Invalid context. index is required");
        }
        return com.aerospike.client.cdt.CTX.listIndex(index);
    }
}



