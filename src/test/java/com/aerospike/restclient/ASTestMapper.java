package com.aerospike.restclient;

import java.util.Map;

public interface ASTestMapper {
    Object mapToObject(Map<String, Object> brMap) throws Exception;
}
