package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.Bin;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Prepend a `value` to the item in the specified `binName`",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/Operation.html")
)
public class PrependOperation extends Operation {

    @Schema(
            description = "The type of operation. It is always " + OperationTypes.PREPEND,
            required = true,
            allowableValues = OperationTypes.PREPEND
    )
    final public String type = OperationTypes.PREPEND;

    public String getBinName() {
        return binName;
    }

    public void setBinName(String binName) {
        this.binName = binName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Schema(required = true)
    private String binName;

    @Schema(required = true)
    private String value;

    public PrependOperation() {
    }

    public PrependOperation(String binName, String value) {
        this.binName = binName;
        this.value = value;
    }

    @Override
    public com.aerospike.client.Operation toOperation() {
        return com.aerospike.client.Operation.prepend(new Bin(binName, value));
    }
}
