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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;

@JsonFormat(shape = JsonFormat.Shape.ARRAY)
@JsonPropertyOrder({"latLng", "radius"})
@Schema(
        requiredMode = Schema.RequiredMode.REQUIRED,
        description = "A 2 element array describing a circle of the form [[longitude, latitude], radius].",
        example = "[[37.421331, -122.098820], 3.14159]"
)
public class LngLatRad {
    public LngLat latLng;
    public double radius;

    public LngLatRad() {
    }

    public LngLatRad(double longitude, double latitude, double radius) {
        this.latLng = new LngLat(longitude, latitude);
        this.radius = radius;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LngLatRad lngLatRad = (LngLatRad) o;
        return Double.compare(lngLatRad.radius, radius) == 0 && Objects.equals(latLng, lngLatRad.latLng);
    }

    @Override
    public int hashCode() {
        return Objects.hash(latLng, radius);
    }
}
