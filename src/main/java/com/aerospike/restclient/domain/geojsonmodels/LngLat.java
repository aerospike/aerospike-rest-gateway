package com.aerospike.restclient.domain.geojsonmodels;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;


@JsonFormat(shape = JsonFormat.Shape.ARRAY)
@JsonPropertyOrder({"longitude", "latitude"})
@Schema(
        required = true,
        description = "A 2 element array describing a position of the form [longitude, latitude]",
        example = "[37.421331, -122.098820]"
)
public class LngLat {
    public double longitude;
    public double latitude;

    public LngLat() {
    }

    public LngLat(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LngLat lngLat = (LngLat) o;
        return Double.compare(lngLat.longitude, longitude) == 0 && Double.compare(lngLat.latitude,
                latitude) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(longitude, latitude);
    }
}