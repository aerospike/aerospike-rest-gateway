package com.aerospike.restclient.domain.ctxmodels;

import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "CTX",
        description = "Nested CDT context. Identifies the location of nested list/map to apply the operation.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/CTX.html")
)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = ListIndexCTX.class, name = AerospikeAPIConstants.CTX.LIST_INDEX),
                @JsonSubTypes.Type(value = ListRankCTX.class, name = AerospikeAPIConstants.CTX.LIST_RANK),
                @JsonSubTypes.Type(value = ListValueCTX.class, name = AerospikeAPIConstants.CTX.LIST_VALUE),
                @JsonSubTypes.Type(value = MapIndexCTX.class, name = AerospikeAPIConstants.CTX.MAP_INDEX),
                @JsonSubTypes.Type(value = MapRankCTX.class, name = AerospikeAPIConstants.CTX.MAP_RANK),
                @JsonSubTypes.Type(value = MapKeyCTX.class, name = AerospikeAPIConstants.CTX.MAP_KEY),
                @JsonSubTypes.Type(value = MapValueCTX.class, name = AerospikeAPIConstants.CTX.MAP_VALUE),
                @JsonSubTypes.Type(value = MapKeyCreateCTX.class, name = AerospikeAPIConstants.CTX.MAP_KEY_CREATE),
                @JsonSubTypes.Type(
                        value = ListIndexCreateCTX.class,
                        name = AerospikeAPIConstants.CTX.LIST_INDEX_CREATE
                ),
        }
)
abstract public class CTX {
    @Schema(
            description = "The type of geoJSON geometry this object represents.", allowableValues = {
            AerospikeAPIConstants.CTX.LIST_INDEX,
            AerospikeAPIConstants.CTX.LIST_RANK,
            AerospikeAPIConstants.CTX.LIST_VALUE,
            AerospikeAPIConstants.CTX.MAP_INDEX,
            AerospikeAPIConstants.CTX.MAP_RANK,
            AerospikeAPIConstants.CTX.MAP_KEY,
            AerospikeAPIConstants.CTX.MAP_VALUE,
            AerospikeAPIConstants.CTX.MAP_KEY_CREATE,
            AerospikeAPIConstants.CTX.LIST_INDEX_CREATE
    }, required = true
    )
    public final String type = null;

    abstract public com.aerospike.client.cdt.CTX toCTX();
}
