package com.aerospike.restclient.util.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

// JSON Object Mapper by default deserializes doubles to BigDecimal.
public class ObjectDeserializer extends JsonDeserializer<Object> {

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
