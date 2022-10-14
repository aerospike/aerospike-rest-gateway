package com.aerospike.restclient.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

public class JSONMessageConverter extends MappingJackson2HttpMessageConverter {

    JSONMessageConverter() {
        super(getJSONObjectMapper());
    }

    public static ObjectMapper getJSONObjectMapper() {
        ObjectMapper jsonMapper = new ObjectMapper(new JsonFactory());
        SimpleModule recordModule = new SimpleModule();
        jsonMapper.registerModule(recordModule);
        jsonMapper.registerModule(new ParameterNamesModule());
        jsonMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return jsonMapper;
    }
}