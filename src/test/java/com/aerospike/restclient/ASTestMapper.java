package com.aerospike.restclient;

import com.fasterxml.jackson.databind.ObjectMapper;


public class ASTestMapper implements IASTestMapper {
    private final ObjectMapper mapper;
    private final Class<?> t;

    @SuppressWarnings("unchecked")
    public ASTestMapper(ObjectMapper mapper, Class<?> type) {
        this.mapper = mapper;
        t = type;
    }

    @Override
    public Object bytesToObject(byte[] bytes) throws Exception {
        return mapper.readValue(bytes, t);
    }

    @Override
    public byte[] objectToBytes(Object object) throws Exception {
        return mapper.writeValueAsBytes(object);
    }
}
