package com.aerospike.restclient;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.List;
import java.util.Map;

public interface OperationV2Performer {
    Map<String, Object> performOperationsAndReturn(MockMvc mockMVC, String testEndpoint,
                                                   Map<String, Object> opsRequest);

    void performOperationsAndExpect(MockMvc mockMVC, String testEndpoint, List<Map<String, Object>> ops,
                                    ResultMatcher matcher) throws Exception;
}
