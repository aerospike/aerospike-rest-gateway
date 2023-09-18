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
package com.aerospike.restclient.domain.querymodels;

import com.aerospike.client.query.Filter;
import com.aerospike.restclient.domain.ctxmodels.CTX;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(
                value = QueryEqualsStringFilter.class,
                name = AerospikeAPIConstants.QueryFilterTypes.EQUAL_STRING
        ),
        @JsonSubTypes.Type(
                value = QueryEqualLongFilter.class, name = AerospikeAPIConstants.QueryFilterTypes.EQUAL_LONG
        ),
        @JsonSubTypes.Type(value = QueryRangeFilter.class, name = AerospikeAPIConstants.QueryFilterTypes.RANGE),
        @JsonSubTypes.Type(
                value = QueryContainsStringFilter.class,
                name = AerospikeAPIConstants.QueryFilterTypes.CONTAINS_STRING
        ),
        @JsonSubTypes.Type(
                value = QueryContainsLongFilter.class,
                name = AerospikeAPIConstants.QueryFilterTypes.CONTAINS_LONG
        ),
        @JsonSubTypes.Type(
                value = QueryGeoWithinPolygonFilter.class,
                name = AerospikeAPIConstants.QueryFilterTypes.GEOWITHIN_REGION
        ),
        @JsonSubTypes.Type(
                value = QueryGeoWithinRadiusFilter.class,
                name = AerospikeAPIConstants.QueryFilterTypes.GEOWITHIN_RADIUS
        ),
        @JsonSubTypes.Type(
                value = QueryGeoContainsPointFilter.class,
                name = AerospikeAPIConstants.QueryFilterTypes.GEOCONTAINS_POINT
        ),
})
@Schema(
        description = "QueryFilter base type. Only allowed on bin which has a secondary index defined.",
        oneOf = {
                QueryEqualsStringFilter.class,
                QueryEqualLongFilter.class,
                QueryRangeFilter.class,
                QueryContainsStringFilter.class,
                QueryContainsLongFilter.class,
                QueryGeoWithinPolygonFilter.class,
                QueryGeoWithinRadiusFilter.class,
                QueryGeoContainsPointFilter.class,
        },
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/" + AerospikeAPIConstants.AS_CLIENT_VERSION + "/com/aerospike/client/query/Filter.html")
)
public abstract class QueryFilter {
    @Schema(description = "The bin for which a secondary-index is defined.", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty(required = true)
    public String binName;

    @Schema(
            description = "An optional context for elements within a CDT which a secondary-index is defined.",
            externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/" + AerospikeAPIConstants.AS_CLIENT_VERSION + "/com/aerospike/client/cdt/CTX.html")
    )
    public List<CTX> ctx;

    public abstract Filter toFilter();

    protected com.aerospike.client.cdt.CTX[] getCTXArray() {
        com.aerospike.client.cdt.CTX[] asCTX = null;

        if (ctx != null) {
            asCTX = ctx.stream().map(CTX::toCTX).toArray(com.aerospike.client.cdt.CTX[]::new);
        }

        return asCTX;
    }
}
