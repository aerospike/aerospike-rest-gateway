package com.aerospike.restclient.domain.ctxmodels;

import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = ListIndexCTX.class, name = AerospikeAPIConstants.LIST_INDEX),
                @JsonSubTypes.Type(value = ListRankCTX.class, name = AerospikeAPIConstants.LIST_RANK),
                @JsonSubTypes.Type(value = ListValueCTX.class, name = AerospikeAPIConstants.LIST_VALUE),
                @JsonSubTypes.Type(value = MapIndexCTX.class, name = AerospikeAPIConstants.MAP_INDEX),
                @JsonSubTypes.Type(value = MapRankCTX.class, name = AerospikeAPIConstants.MAP_RANK),
                @JsonSubTypes.Type(value = MapKeyCTX.class, name = AerospikeAPIConstants.MAP_KEY),
                @JsonSubTypes.Type(value = MapValueCTX.class, name = AerospikeAPIConstants.MAP_VALUE),
                @JsonSubTypes.Type(value = MapKeyCreateCTX.class, name = AerospikeAPIConstants.MAP_KEY_CREATE),
                @JsonSubTypes.Type(value = ListIndexCreateCTX.class, name = AerospikeAPIConstants.LIST_INDEX_CREATE),
        }
)
@Schema(
        name = "CTX",
        description = "Nested CDT context. Identifies the location of nested list/map to apply the operation.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/CTX.html")
)
abstract public class CTX {
    @Schema(
            description = "The type of geoJSON geometry this object represents.", allowableValues = {
            AerospikeAPIConstants.LIST_INDEX,
            AerospikeAPIConstants.LIST_RANK,
            AerospikeAPIConstants.LIST_VALUE,
            AerospikeAPIConstants.MAP_INDEX,
            AerospikeAPIConstants.MAP_RANK,
            AerospikeAPIConstants.MAP_KEY,
            AerospikeAPIConstants.MAP_VALUE,
            AerospikeAPIConstants.MAP_KEY_CREATE,
            AerospikeAPIConstants.LIST_INDEX_CREATE
    }, required = true
    )
    public final String type = null;

    abstract public com.aerospike.client.cdt.CTX toCTX();
}
