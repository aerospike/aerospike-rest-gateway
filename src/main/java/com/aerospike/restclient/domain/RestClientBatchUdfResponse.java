package com.aerospike.restclient.domain;

import com.aerospike.client.BatchDelete;
import com.aerospike.client.BatchUDF;
import com.aerospike.client.Value;
import io.swagger.v3.oas.annotations.media.Schema;

public class RestClientBatchUdfResponse extends RestClientBatchRecordResponse {
    @Schema(description = "Package or lua module name.")
    public String packageName;

    @Schema(description = "Package or lua module name.")
    public String functionName;

    @Schema(description = "Optional arguments to lua function.")
    public Value[] functionArgs;

    public RestClientBatchUdfResponse() {}

    public RestClientBatchUdfResponse(BatchUDF batchUDF) {
        super(batchUDF);
        packageName = batchUDF.packageName;
        functionName = batchUDF.functionName;
        functionArgs = batchUDF.functionArgs;
    }
}
