package com.aerospike.restclient.util.annotations;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

public @interface ASRestClientSchemas {
    @Schema(
            description = "Secondary index collection type.",
            defaultValue = "DEFAULT",
            externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/query/IndexCollectionType.html")
    )
    public @interface IndexCollectionType {
    }
}
