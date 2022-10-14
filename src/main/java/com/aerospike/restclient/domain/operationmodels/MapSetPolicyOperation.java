package com.aerospike.restclient.domain.operationmodels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Set the policy for the map in the specified bin.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/MapOperation.html")
)
public class MapSetPolicyOperation extends MapOperation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.MAP_SET_POLICY,
            required = true,
            allowableValues = OperationTypes.MAP_SET_POLICY
    )
    final public String type = OperationTypes.MAP_SET_POLICY;

    @Schema(required = true)
    private final MapPolicy mapPolicy;

    @JsonCreator
    public MapSetPolicyOperation(@JsonProperty(value = "binName", required = true) String binName,
                                 @JsonProperty(value = "mapPolicy", required = true) MapPolicy mapPolicy) {
        super(binName);
        this.mapPolicy = mapPolicy;
    }

    @Override
    public com.aerospike.client.Operation toOperation() {
        com.aerospike.client.cdt.CTX[] asCTX = getASCTX();
        com.aerospike.client.cdt.MapPolicy asMapPolicy;

        if (mapPolicy == null) {
            asMapPolicy = com.aerospike.client.cdt.MapPolicy.Default;
        } else {
            asMapPolicy = mapPolicy.toMapPolicy();
        }

        return com.aerospike.client.cdt.MapOperation.setMapPolicy(asMapPolicy, binName, asCTX);
    }
}
