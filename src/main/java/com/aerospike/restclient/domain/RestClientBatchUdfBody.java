package com.aerospike.restclient.domain;

import com.aerospike.client.BatchRecord;
import com.aerospike.client.BatchUDF;
import com.aerospike.client.Value;
import com.aerospike.client.policy.BatchUDFPolicy;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public class RestClientBatchUdfBody extends RestClientBatchRecordBody {
    @Schema(description = "Package or lua module name.")
    public String packageName;

    @Schema(description = "Package or lua module name.")
    public String functionName;

    @Schema(description = "Optional arguments to lua function.")
    public Value[] functionArgs;

    //TODO
    @Schema(description = "TODO")
    public BatchUDFPolicy policy;

    public RestClientBatchUdfBody() {}

    public BatchRecord toBatchRecord() {
        BatchUDFPolicy policy = new BatchUDFPolicy();
        return new BatchUDF(policy, key.toKey(), packageName, functionName, functionArgs);
    }
}

