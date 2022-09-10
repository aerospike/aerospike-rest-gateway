package com.aerospike.restclient.domain.geojsonmodels;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.models.annotations.OpenAPI31;

import java.util.ArrayList;

@JsonFormat(shape = JsonFormat.Shape.ARRAY)
@JsonPropertyOrder({"latLng", "radius"})
@Schema(required = true, description = "A 2 element array describing a circle of the form [[longitude, latitude], radius].", example = "[[37.421331, -122.098820], 3.14159]")
public class LngLatRad {
    public LngLat latLng;
    public double radius;

    public LngLatRad() {}

    public LngLatRad(double longitude, double latitude, double radius) {
        this.latLng = new LngLat(longitude, latitude);
        this.radius = radius;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LngLatRad)) {
            return false;
        }
        if (((LngLatRad) obj).radius != radius) {
            return false;
        }
        return ((LngLatRad) obj).latLng.equals(latLng);
    }
}
