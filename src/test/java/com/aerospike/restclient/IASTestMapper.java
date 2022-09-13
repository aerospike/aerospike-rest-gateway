package com.aerospike.restclient;

public interface IASTestMapper {
    Object bytesToObject(byte[] bytes) throws Exception;

    byte[] objectToBytes(Object object) throws Exception;
}
