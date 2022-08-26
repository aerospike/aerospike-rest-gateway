package com.aerospike.restclient.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;
import java.math.BigDecimal;

public class JSONMessageConverter extends MappingJackson2HttpMessageConverter {

    JSONMessageConverter() {
        super(getJSONObjectMapper());
    }

    public static ObjectMapper getJSONObjectMapper() {
        ObjectMapper jsonMapper = new ObjectMapper(new JsonFactory());
        SimpleModule recordModule = new SimpleModule();
        jsonMapper.registerModule(recordModule);
        jsonMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return jsonMapper;
    }
}