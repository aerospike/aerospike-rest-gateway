package com.aerospike.restclient;

public interface IASTestMapper {
    public Object bytesToObject(byte[] bytes) throws Exception;

    public byte[] objectToBytes(Object object) throws Exception;
}
