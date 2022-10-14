package com.aerospike.restclient.domain.operationmodels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Server returns integer from byte[] bin starting at bitOffset for bitSize. Signed indicates if bits should be treated as a signed number.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/latest/com/aerospike/client/operation/BitOperation.html")
)
public class BitGetIntOperation extends BitOperation {
    @Schema(
            description = "The type of operation. It is always " + OperationTypes.BIT_GET_INT,
            required = true,
            allowableValues = OperationTypes.BIT_GET_INT
    )
    final public String type = OperationTypes.BIT_GET_INT;

    @Schema(required = true)
    private final int bitOffset;

    @Schema(required = true)
    private final int bitSize;

    private final boolean signed;

    @JsonCreator
    public BitGetIntOperation(@JsonProperty(value = "binName", required = true) String binName,
                              @JsonProperty(value = "bitOffset", required = true) int bitOffset,
                              @JsonProperty(value = "bitSize", required = true) int bitSize, @JsonProperty(
            value = "signed", defaultValue = "false"
    ) boolean signed) {
        super(binName);
        this.bitOffset = bitOffset;
        this.bitSize = bitSize;
        this.signed = signed;
    }

    public com.aerospike.client.Operation toOperation() {
        return com.aerospike.client.operation.BitOperation.getInt(binName, bitOffset, bitSize, signed);
    }
}
