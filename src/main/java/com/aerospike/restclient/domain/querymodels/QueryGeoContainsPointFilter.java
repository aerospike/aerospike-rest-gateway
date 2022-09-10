package com.aerospike.restclient.domain.querymodels;

import com.aerospike.client.query.Filter;
import com.aerospike.client.query.IndexCollectionType;
import com.aerospike.restclient.domain.geojsonmodels.LngLat;
import com.aerospike.restclient.domain.geojsonmodels.Point;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.aerospike.restclient.util.annotations.ASRestClientSchemas;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Geospatial filter for regions that contain a point.  Only allowed on bins which has a secondary index defined.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/query/Filter.html")
)
public class QueryGeoContainsPointFilter extends QueryFilter {
    @Schema(
            description = "The type of query filter this object represents. It is always " + AerospikeAPIConstants.QueryFilterTypes.GEOCONTAINS_POINT,
            required = true,
            allowableValues = AerospikeAPIConstants.QueryFilterTypes.GEOCONTAINS_POINT
    )
    final public String filterType = AerospikeAPIConstants.QueryFilterTypes.GEOCONTAINS_POINT;

    @Schema(description = "Longitude and Latitude of a point", required = true)
    public LngLat point;

    @ASRestClientSchemas.IndexCollectionType
    public IndexCollectionType collectionType = IndexCollectionType.DEFAULT;

    public QueryGeoContainsPointFilter() {
    }

    @Override
    public Filter toFilter() {
        return Filter.geoContains(binName, collectionType, new Point(point).writeValueAsString(), getCTXArray());
    }
}
