package com.aerospike.restclient.domain.operationmodels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Server returns integer bit offset of the first specified value bit in byte[] bin starting at bitOffset for bitSize.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/latest/com/aerospike/client/operation/BitOperation.html")
)
public class BitLScanOperation extends BitOperation {
    @Schema(
            description = "The type of operation. It is always " + OperationTypes.BIT_LSCAN,
            required = true,
            allowableValues = OperationTypes.BIT_LSCAN
    )
    final public String type = OperationTypes.BIT_LSCAN;

    @Schema(required = true)
    private final int bitOffset;

    @Schema(required = true)
    private final int bitSize;

    @Schema(required = true)
    private final boolean value;

    @JsonCreator
    public BitLScanOperation(@JsonProperty(value = "binName", required = true) String binName,
                             @JsonProperty(value = "bitOffset", required = true) int bitOffset,
                             @JsonProperty(value = "bitSize", required = true) int bitSize,
                             @JsonProperty(value = "value", required = true) boolean value) {
        super(binName);
        this.bitOffset = bitOffset;
        this.bitSize = bitSize;
        this.value = value;
    }

    public com.aerospike.client.Operation toOperation() {
        return com.aerospike.client.operation.BitOperation.lscan(binName, bitOffset, bitSize, value);
    }
}
