package com.aerospike.restclient.domain.geojsonmodels;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;

@JsonFormat(shape = JsonFormat.Shape.ARRAY)
@JsonPropertyOrder({"latLng", "radius"})
@Schema(
        required = true,
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
        return Double.compare(lngLatRad.radius, radius) == 0 && Objects.equals(latLng,
                lngLatRad.latLng);
    }

    @Override
    public int hashCode() {
        return Objects.hash(latLng, radius);
    }
}
