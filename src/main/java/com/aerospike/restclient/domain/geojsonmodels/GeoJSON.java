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
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = AeroCircle.class, name = AerospikeAPIConstants.GeoJSON.Types.AERO_CIRCLE),
                @JsonSubTypes.Type(value = Point.class, name = AerospikeAPIConstants.GeoJSON.Types.POINT),
                @JsonSubTypes.Type(value = Polygon.class, name = AerospikeAPIConstants.GeoJSON.Types.POLYGON),
        }
)
@Schema(
        name = "GeoJSON",
        description = "A geoJSON AeroCirlce, Point, or Polygon object.",
        externalDocs = @ExternalDocumentation(url = "https://docs.aerospike.com/server/guide/data-types/geospatial")
)
abstract public class GeoJSON {
    @Schema(
            description = "The type of geoJSON geometry this object represents.",
            allowableValues = {
                    AerospikeAPIConstants.GeoJSON.Types.POLYGON,
                    AerospikeAPIConstants.GeoJSON.Types.POINT,
                    AerospikeAPIConstants.GeoJSON.Types.AERO_CIRCLE
            },
            required = true
    )
    public final String type = null;

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
