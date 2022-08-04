package com.aerospike.restclient;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Type;
import java.util.Map;

public class ASMsgPackTestMapper implements ASTestMapper {
    private final ObjectMapper mapper;
    private final JavaType t;

    public ASMsgPackTestMapper(ObjectMapper mapper, Type t) {
        this.mapper = mapper;
        this.t = this.mapper.constructType(t);
    }

    @Override
    public Object mapToObject(Map<String, Object> brMap) throws Exception {
        return mapper.readValue(mapper.writeValueAsBytes(brMap), this.t);
    }
}
