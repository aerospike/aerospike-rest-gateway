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
package com.aerospike.restclient.domain.querymodels;

import com.aerospike.restclient.ASTestMapper;
import com.aerospike.restclient.ASTestUtils;
import com.aerospike.restclient.config.JSONMessageConverter;
import com.aerospike.restclient.config.MsgPackConverter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.HashMap;
import java.util.Map;

import static com.aerospike.restclient.util.AerospikeAPIConstants.QueryFilterTypes.EQUAL_LONG;

@RunWith(Parameterized.class)
public class QueryRequestBodyTest {

    private final ASTestMapper mapper;
    private final static String ns = "test";
    private final static String set = "ctx";

    @Parameterized.Parameters
    public static Object[] getParams() {
        return new Object[]{
                new JsonQueryBodyMapper(), new MsgPackQueryBodyMapper(),
                };
    }

    public QueryRequestBodyTest(ASTestMapper mapper) {
        this.mapper = mapper;
    }

    @Test
    public void testEmptyMapToRestClientQueryBody() {
        Map<String, Object> ctxMap = new HashMap<>();

        try {
            mapper.bytesToObject(mapper.objectToBytes(ctxMap));
        } catch (Exception e) {
            Assert.fail("Should have mapped to RestClientQueryBody");
            // Success
        }
    }

    @Test
    public void testMapsToRestClientQueryBody() {
        Map<String, Object> restMap = new HashMap<>();
        restMap.put("from", "from-pagination-token");
        Map<String, Object> filter = new HashMap<>();
        filter.put("type", EQUAL_LONG);
        filter.put("value", 1L);
        restMap.put("filter", filter);

        QueryEqualLongFilter expectedFilter = new QueryEqualLongFilter();
        expectedFilter.value = 1L;

        try {
            QueryRequestBody restQuery = (QueryRequestBody) mapper.bytesToObject(mapper.objectToBytes(restMap));
            Assert.assertEquals(restQuery.from, "from-pagination-token");
            ASTestUtils.compareFilter(expectedFilter.toFilter(), restQuery.filter.toFilter());
        } catch (Exception e) {
            Assert.fail(String.format("Should have mapped to RestClientQueryBody %s", e));
        }
    }
}

class JsonQueryBodyMapper extends ASTestMapper {

    public JsonQueryBodyMapper() {
        super(JSONMessageConverter.getJSONObjectMapper(), QueryRequestBody.class);
    }
}

class MsgPackQueryBodyMapper extends ASTestMapper {

    public MsgPackQueryBodyMapper() {
        super(MsgPackConverter.getASMsgPackObjectMapper(), QueryRequestBody.class);
    }
}
