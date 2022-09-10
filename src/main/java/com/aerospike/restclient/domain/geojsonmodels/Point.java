package com.aerospike.restclient.domain.geojsonmodels;


import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;

@Schema(
        description = "GeoJSON Point geometry.",
        externalDocs = @ExternalDocumentation(url = "https://docs.aerospike.com/server/guide/data-types/geospatial")
)
public class Point extends GeoJSON {
    @Schema(
            description = "The type of geoJSON geometry this object represents. It is always " + AerospikeAPIConstants.GeoJSON.Types.POINT,
            allowableValues = {AerospikeAPIConstants.GeoJSON.Types.POINT},
            required = true
    )
    public final String type = AerospikeAPIConstants.GeoJSON.Types.POINT;
    private final ObjectMapper mapper = new ObjectMapper();
    public LngLat coordinates;

    public Point() {
    }

    public Point(LngLat coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return Objects.equals(coordinates, point.coordinates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordinates);
    }
}
