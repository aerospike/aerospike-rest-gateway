package com.aerospike.restclient.domain;

import com.aerospike.client.BatchUDF;
import com.aerospike.client.Value;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.aerospike.restclient.util.RestClientErrors;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.IOException;
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
    @JsonDeserialize(contentUsing = BigDecimalDeserializer.class)
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

// JSON Object Mapper by default deserializes doubles to BigDecimal.
class BigDecimalDeserializer extends JsonDeserializer<Object> {

    @Override
    public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        ObjectCodec codec = jsonParser.getCodec();
        JsonNode jsonNode = codec.readTree(jsonParser);
        if (jsonNode.isBigDecimal()) {
            return jsonNode.asDouble();
        }
        return codec.treeToValue(jsonNode, Object.class);
    }
}