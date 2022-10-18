/*
 * Copyright 2022 Aerospike, Inc.
 *
 * Portions may be licensed to Aerospike, Inc. under one or more contributor
 * license agreements WHICH ARE COMPATIBLE WITH THE APACHE LICENSE, VERSION 2.0.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.aerospike.restclient;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.client.query.Filter;
import com.aerospike.restclient.config.JSONMessageConverter;
import com.aerospike.restclient.config.MsgPackConverter;
import com.aerospike.restclient.domain.RestClientError;
import com.aerospike.restclient.domain.querymodels.QueryEqualLongFilter;
import com.aerospike.restclient.domain.querymodels.QueryFilter;
import com.aerospike.restclient.domain.querymodels.QueryRequestBody;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(Parameterized.class)
@SpringBootTest
public class QueryErrorTest {

    @ClassRule
    public static final SpringClassRule springClassRule = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    private MockMvc mockMVC;

    @Autowired
    private AerospikeClient client;

    @Autowired
    private WebApplicationContext wac;

    private final String namespace = "test";
    private final String setName = "queryError";
    private final String testEndpoint;

    private final QueryErrorHandler queryHandler;

    @Before
    public void setup() throws InterruptedException {
        mockMVC = MockMvcBuilders.webAppContextSetup(wac).build();
        Bin bin = new Bin("binWithNoSIndex", 1);
        client.put(null, new Key(namespace, setName, "key_" + 1), bin);
    }

    @After
    public void clean() throws InterruptedException {
        client.delete(null, new Key(namespace, setName, "key_" + 1));
    }

    @Parameterized.Parameters
    public static Object[][] getParams() {
        return new Object[][]{
                {new JSONQueryErrorHandler(), true},
                {new JSONQueryErrorHandler(), false},
                {new MsgPackQueryErrorHandler(), true},
                {new MsgPackQueryErrorHandler(), false}
        };
    }

    public QueryErrorTest(QueryErrorHandler handler, boolean useSet) {
        queryHandler = handler;

        if (useSet) {
            testEndpoint = String.format("/v1/query/%s/%s", namespace, setName);
        } else {
            testEndpoint = String.format("/v1/query/%s", namespace);
        }
    }

    @Test
    public void testNonExistentPath() throws Exception {
        String endpoint = testEndpoint.replace(namespace, "nonExistent");
        RestClientError res = queryHandler.perform(mockMVC, endpoint, new QueryRequestBody(), status().isNotFound());
        Assert.assertFalse(res.getInDoubt());
    }

    @Test
    public void testInvalidStartAndCount() throws Exception {
        String endpoint = String.join("/", testEndpoint, "200", "90832");
        RestClientError res = queryHandler.perform(mockMVC, endpoint, new QueryRequestBody(), status().isBadRequest());
        Assert.assertFalse(res.getInDoubt());
    }

    @Test
    public void testInvalidFilterType() throws Exception {
        class unknownFilter extends QueryFilter {
            final String type = "UNKNOWN_FILTER_TYPE";

            @Override
            public Filter toFilter() {
                return null;
            }
        }

        QueryRequestBody requestBody = new QueryRequestBody();
        requestBody.filter = new unknownFilter();
        RestClientError res = queryHandler.perform(mockMVC, testEndpoint, requestBody, status().isBadRequest());
        Assert.assertFalse(res.getInDoubt());
    }

    @Test
    public void testFilterWithNoSIndex() throws Exception {
        QueryRequestBody requestBody = new QueryRequestBody();
        QueryEqualLongFilter filter = new QueryEqualLongFilter();
        filter.value = 6L;
        filter.binName = "binWithNoSIndex";
        requestBody.filter = filter;
        RestClientError res = queryHandler.perform(mockMVC, testEndpoint, requestBody, status().isNotFound());
        Assert.assertFalse(res.getInDoubt());
    }

    @Test
    public void testFilterNonExistentBin() throws Exception {
        QueryRequestBody requestBody = new QueryRequestBody();
        QueryEqualLongFilter filter = new QueryEqualLongFilter();
        filter.value = 6L;
        filter.binName = "nonExistentBin";
        requestBody.filter = filter;
        // Not found because sindex is checked first
        RestClientError res = queryHandler.perform(mockMVC, testEndpoint, requestBody, status().isNotFound());
        Assert.assertFalse(res.getInDoubt());
    }
}

/*
 * The handler interface performs a query request via a json string, and returns a List<Map<String, Object>>
 * Implementations are provided for specifying JSON and MsgPack as return formats
 */
interface QueryErrorHandler {
    RestClientError perform(MockMvc mockMVC, String testEndpoint, QueryRequestBody payload,
                            ResultMatcher matcher) throws Exception;
}

class MsgPackQueryErrorHandler implements QueryErrorHandler {

    ASTestMapper msgPackMapper;

    public MsgPackQueryErrorHandler() {
        msgPackMapper = new ASTestMapper(MsgPackConverter.getASMsgPackObjectMapper(), RestClientError.class);
    }

    private RestClientError getQueryResponse(byte[] response) {
        RestClientError queryResponse = null;
        try {
            queryResponse = (RestClientError) msgPackMapper.bytesToObject(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return queryResponse;
    }

    @Override
    public RestClientError perform(MockMvc mockMVC, String testEndpoint, QueryRequestBody payload,
                                   ResultMatcher matcher) throws Exception {

        byte[] response = ASTestUtils.performOperationAndExpect(mockMVC, testEndpoint,
                msgPackMapper.objectToBytes(payload), matcher);

        return getQueryResponse(response);
    }

}

class JSONQueryErrorHandler implements QueryErrorHandler {

    ASTestMapper msgPackMapper;

    public JSONQueryErrorHandler() {
        msgPackMapper = new ASTestMapper(JSONMessageConverter.getJSONObjectMapper(), RestClientError.class);
    }

    private RestClientError getQueryResponse(byte[] response) {
        RestClientError queryResponse = null;
        try {
            queryResponse = (RestClientError) msgPackMapper.bytesToObject(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return queryResponse;
    }

    @Override
    public RestClientError perform(MockMvc mockMVC, String testEndpoint, QueryRequestBody payload,
                                   ResultMatcher matcher) throws Exception {
        byte[] response = ASTestUtils.performOperationAndExpect(mockMVC, testEndpoint,
                        new String(msgPackMapper.objectToBytes(payload), StandardCharsets.UTF_8), matcher)
                .getBytes(StandardCharsets.UTF_8);
        return getQueryResponse(response);
    }
}