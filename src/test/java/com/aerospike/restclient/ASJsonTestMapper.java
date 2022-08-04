package com.aerospike.restclient;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Type;
import java.util.Map;

public class ASJsonTestMapper implements ASTestMapper {
    private final ObjectMapper mapper;
    private final JavaType t;

    public ASJsonTestMapper(ObjectMapper mapper, Type t) {

        this.mapper = mapper;
        this.mapper.disable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
        this.t = this.mapper.constructType(t);
    }

    @Override
    public Object mapToObject(Map<String, Object> brMap) throws Exception {
        return mapper.readValue(mapper.writeValueAsString(brMap), this.t);
    }
}
