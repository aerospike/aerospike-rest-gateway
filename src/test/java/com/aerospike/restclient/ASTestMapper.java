package com.aerospike.restclient;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Type;
import java.util.Map;


public class ASTestMapper implements IASTestMapper {
    private final ObjectMapper mapper;
    private final JavaType t;

    public ASTestMapper(ObjectMapper mapper, Type t) {
        this.mapper = mapper;
        this.t = this.mapper.constructType(t);
    }

    @Override
    public Object bytesToObject(byte[] bytes) throws Exception {
        return mapper.readValue(bytes, this.t);
    }

    @Override
    public byte[] objectToBytes(Object object) throws Exception {
        return mapper.writeValueAsBytes(object);
    }
}
