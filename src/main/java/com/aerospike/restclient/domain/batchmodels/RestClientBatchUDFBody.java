package com.aerospike.restclient.domain.batchmodels;

import com.aerospike.client.BatchUDF;
import com.aerospike.client.Value;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.aerospike.restclient.util.RestClientErrors;
import com.aerospike.restclient.util.deserializers.ObjectDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class RestClientBatchUDFBody extends RestClientBatchRecordBody {

    @Schema(description = "List of bins to limit the record response to.", allowableValues = AerospikeAPIConstants.BATCH_TYPE_UDF, required = true)
    @JsonProperty(required = true)
    public final String batchType = AerospikeAPIConstants.BATCH_TYPE_UDF;

    @Schema(description = "Package or lua module name.")
    @JsonProperty(required = true)
    public String packageName;

    @Schema(description = "Package or lua module name.")
    @JsonProperty(required = true)
    public String functionName;

    @Schema(description = "Optional arguments to lua function.")
    @JsonDeserialize(contentUsing = ObjectDeserializer.class)
    public List<Object> functionArgs;

    @Schema(description = "Policy attributes used for this batch udf operation.")
    public RestClientBatchUDFPolicy policy;

    public RestClientBatchUDFBody() {
    }

    public BatchUDF toBatchRecord() {
        if (key == null) {
            throw new RestClientErrors.InvalidKeyError("Key for a batch udf may not be null");
        }

        Value[] values = null;

        if (functionArgs != null) {
            values = functionArgs.stream().map(Value::get).toArray(Value[]::new);
        }

        if (policy == null) {
            return new BatchUDF(key.toKey(), packageName, functionName, values);
        }

        return new BatchUDF(policy.toBatchUDFPolicy(), key.toKey(), packageName, functionName, values);
    }
}

