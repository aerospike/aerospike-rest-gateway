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

import com.aerospike.client.Value;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

// This is only a subset of the GeoJSON object supported by aerospike. More will be required
// if this model is implemented in the Bin parser. Currently, it is only being used by QueryFilters.
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = AeroCircle.class, name = AerospikeAPIConstants.GeoJSON.Types.AERO_CIRCLE),
        @JsonSubTypes.Type(value = Point.class, name = AerospikeAPIConstants.GeoJSON.Types.POINT),
        @JsonSubTypes.Type(value = Polygon.class, name = AerospikeAPIConstants.GeoJSON.Types.POLYGON),
})
@Schema(
        name = "GeoJSON",
        description = "A geoJSON AeroCirlce, Point, or Polygon object.",
        externalDocs = @ExternalDocumentation(url = "https://docs.aerospike.com/server/guide/data-types/geospatial"),
        oneOf = {AeroCircle.class, Point.class, Polygon.class,}
)
public abstract class GeoJSON {

    private final ObjectMapper mapper = new ObjectMapper();

    public Value toValue() {
        return new Value.GeoJSONValue(toString());
    }

    public String writeValueAsString() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
