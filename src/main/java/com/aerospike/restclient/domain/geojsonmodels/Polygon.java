package com.aerospike.restclient.domain.geojsonmodels;

import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
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
            required = true
    )
    public final String type = AerospikeAPIConstants.GeoJSON.Types.POLYGON;

    @Schema(required = true)
    public List<LngLat> coordinates;

    public Polygon() {
    }

    public Polygon(List<LngLat> polygon) {
        coordinates = polygon;
    }

    public Polygon(LngLat... polygon) {
        coordinates = Arrays.asList(polygon);
    }

    private final ObjectMapper mapper = new ObjectMapper();

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

