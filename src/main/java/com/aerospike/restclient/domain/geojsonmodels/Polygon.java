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

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Schema(
        description = "GeoJSON Polygon geometry.",
        externalDocs = @ExternalDocumentation(url = "https://docs.aerospike.com/server/guide/data-types/geospatial")
)
public class Polygon extends GeoJSON {
    @Schema(
            description = "The type of geoJSON geometry this object represents. It is always " + AerospikeAPIConstants.GeoJSON.Types.POLYGON,
            allowableValues = {AerospikeAPIConstants.GeoJSON.Types.POLYGON},
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    public final String type = AerospikeAPIConstants.GeoJSON.Types.POLYGON;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    public List<LngLat> coordinates;

    public Polygon() {
    }

    public Polygon(List<LngLat> polygon) {
        coordinates = polygon;
    }

    public Polygon(LngLat... polygon) {
        coordinates = Arrays.asList(polygon);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Polygon polygon = (Polygon) o;
        return Objects.equals(coordinates, polygon.coordinates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordinates);
    }
}
