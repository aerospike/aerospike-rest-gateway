package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.operation.BitOverflowAction;
import com.aerospike.client.operation.BitPolicy;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Server adds value to byte[] bin starting at bitOffset for bitSize. BitSize must be <= 64. Signed indicates if bits should be treated as a signed number. If add overflows/underflows, BitOverflowAction is used. Server does not return a value.",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/latest/com/aerospike/client/operation/BitOperation.html")
)
public class BitAddOperation extends BitOperation {
    @Schema(
            description = "The type of operation. It is always " + OperationTypes.BIT_ADD,
            required = true,
            allowableValues = OperationTypes.BIT_ADD
    )
    final public String type = OperationTypes.BIT_ADD;

    @Schema(required = true)
    private final int bitOffset;

    @Schema(required = true)
    private final int bitSize;

    @Schema(required = true)
    private final long value;

    private boolean signed;
    @Schema(defaultValue = "FAIL")
    private BitOverflowAction action = BitOverflowAction.FAIL;

    @JsonCreator
    public BitAddOperation(@JsonProperty(value = "binName", required = true) String binName,
                           @JsonProperty(value = "bitOffset", required = true) int bitOffset,
                           @JsonProperty(value = "bitSize", required = true) int bitSize,
                           @JsonProperty(value = "value", required = true) long value) {
        super(binName);
        this.bitOffset = bitOffset;
        this.bitSize = bitSize;
        this.value = value;
    }

    public boolean isSigned() {
        return signed;
    }

    public void setSigned(boolean signed) {
        this.signed = signed;
    }

    public BitOverflowAction getAction() {
        return action;
    }

    public void setAction(BitOverflowAction action) {
        this.action = action;
    }

    public com.aerospike.client.Operation toOperation() {
        return com.aerospike.client.operation.BitOperation.add(BitPolicy.Default, binName, bitOffset, bitSize, value,
                signed, action);
    }
}
