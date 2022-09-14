package com.aerospike.restclient.domain.querymodels;

import com.aerospike.client.query.Filter;
import com.aerospike.client.query.IndexCollectionType;
import com.aerospike.restclient.domain.geojsonmodels.LngLatRad;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.aerospike.restclient.util.annotations.ASRestClientSchemas;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Geospatial filter for points contained inside and AeroCircle object.  Only allowed on bin which has a secondary index defined.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/query/Filter.html")
)
public class QueryGeoWithinRadiusFilter extends QueryFilter {
    @Schema(
            description = "The type of query filter this object represents. It is always " + AerospikeAPIConstants.QueryFilterTypes.GEOWITHIN_RADIUS,
            required = true,
            allowableValues = AerospikeAPIConstants.QueryFilterTypes.GEOWITHIN_RADIUS
    )
    final public String type = AerospikeAPIConstants.QueryFilterTypes.GEOWITHIN_RADIUS;

    @Schema(description = "Array of longitude, latitude, and radius describing a circle.", required = true)
    public LngLatRad circle;

    @ASRestClientSchemas.IndexCollectionType
    IndexCollectionType collectionType = IndexCollectionType.DEFAULT;

    public QueryGeoWithinRadiusFilter() {
    }

    @Override
    public Filter toFilter() {
        return Filter.geoWithinRadius(binName, collectionType, circle.latLng.longitude,
                circle.latLng.latitude, circle.radius, getCTXArray());
    }
}

