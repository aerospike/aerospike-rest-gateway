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
            required = true
    )
    public final String type = AerospikeAPIConstants.GeoJSON.Types.AERO_CIRCLE;

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

