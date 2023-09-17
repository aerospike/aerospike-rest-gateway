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
import com.aerospike.client.query.IndexCollectionType;
import com.aerospike.restclient.domain.geojsonmodels.LngLatRad;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.aerospike.restclient.util.annotations.ASRestClientSchemas;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Geospatial filter for points contained inside and AeroCircle object.  Only allowed on bin which has a secondary index defined.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/" + AerospikeAPIConstants.AS_CLIENT_VERSION + "/com/aerospike/client/query/Filter.html")
)
public class QueryGeoWithinRadiusFilter extends QueryFilter {
    @Schema(
            description = "The type of query filter this object represents. It is always " + AerospikeAPIConstants.QueryFilterTypes.GEOWITHIN_RADIUS,
            requiredMode = Schema.RequiredMode.REQUIRED,
            allowableValues = {AerospikeAPIConstants.QueryFilterTypes.GEOWITHIN_RADIUS}
    )
    public final String type = AerospikeAPIConstants.QueryFilterTypes.GEOWITHIN_RADIUS;

    @Schema(description = "Array of longitude, latitude, and radius describing a circle.", requiredMode = Schema.RequiredMode.REQUIRED)
    public LngLatRad circle;

    @ASRestClientSchemas.IndexCollectionType
    IndexCollectionType collectionType = IndexCollectionType.DEFAULT;

    public QueryGeoWithinRadiusFilter() {
    }

    @Override
    public Filter toFilter() {
        return Filter.geoWithinRadius(binName, collectionType, circle.latLng.longitude, circle.latLng.latitude,
                circle.radius, getCTXArray());
    }
}
