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
package com.aerospike.restclient.domain.geojsonmodels;

import com.aerospike.restclient.util.AerospikeAPIConstants;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;

@Schema(
        description = "Aerospike's custom GeoJSON extension type to describe a circle.",
        externalDocs = @ExternalDocumentation(url = "https://docs.aerospike.com/server/guide/data-types/geospatial")
)
public class AeroCircle extends GeoJSON {
    @Schema(
            description = "The type of geoJSON geometry this object represents. It is always " + AerospikeAPIConstants.GeoJSON.Types.AERO_CIRCLE,
            allowableValues = {AerospikeAPIConstants.GeoJSON.Types.AERO_CIRCLE},
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    final public String type = AerospikeAPIConstants.GeoJSON.Types.AERO_CIRCLE;

    public LngLatRad coordinates;

    AeroCircle() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AeroCircle that = (AeroCircle) o;
        return Objects.equals(coordinates, that.coordinates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordinates);
    }
}

