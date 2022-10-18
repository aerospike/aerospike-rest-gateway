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
package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.cdt.ListOrder;
import com.aerospike.client.cdt.ListWriteFlags;
import com.aerospike.restclient.ASTestMapper;
import com.aerospike.restclient.ASTestUtils;
import com.aerospike.restclient.config.JSONMessageConverter;
import com.aerospike.restclient.config.MsgPackConverter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(Parameterized.class)
public class ListPolicyTest {
    ASTestMapper mapper;

    @Parameterized.Parameters
    public static Object[] getParams() {
        return new Object[]{
                new JsonListPolicyMapper(), new MsgPackListPolicyMapper(),
                };
    }

    public ListPolicyTest(ASTestMapper mapper) {
        this.mapper = mapper;
    }

    @Test
    public void testEmptyListPolicy() throws Exception {
        Map<String, Object> policyMap = new HashMap<>();

        ListPolicy listPolicy = (ListPolicy) mapper.bytesToObject(mapper.objectToBytes(policyMap));

        Assert.assertNull(listPolicy.getOrder());
        Assert.assertNull(listPolicy.getWriteFlags());

        com.aerospike.client.cdt.ListPolicy asListPolicy = listPolicy.toListPolicy();
        Assert.assertEquals(asListPolicy.attributes, com.aerospike.client.cdt.ListPolicy.Default.attributes);
        Assert.assertEquals(asListPolicy.flags, com.aerospike.client.cdt.ListPolicy.Default.flags);
    }

    @Test
    public void toListPolicyWithOrderAndWriteFlags() throws Exception {
        Map<String, Object> policyMap = new HashMap<>();
        policyMap.put("order", "UNORDERED");
        policyMap.put("writeFlags", new String[]{"ADD_UNIQUE", "NO_FAIL"});
        List<ListWriteFlag> expectedWriteFlags = new ArrayList<ListWriteFlag>();
        expectedWriteFlags.add(ListWriteFlag.ADD_UNIQUE);
        expectedWriteFlags.add(ListWriteFlag.NO_FAIL);

        ListPolicy listPolicy = (ListPolicy) mapper.bytesToObject(mapper.objectToBytes(policyMap));

        Assert.assertEquals(listPolicy.getOrder(), ListOrder.UNORDERED);
        ASTestUtils.compareCollection(expectedWriteFlags, listPolicy.getWriteFlags());

        com.aerospike.client.cdt.ListPolicy asListPolicy = listPolicy.toListPolicy();
        Assert.assertEquals(asListPolicy.attributes, ListOrder.UNORDERED.attributes);
        Assert.assertEquals(asListPolicy.flags, ListWriteFlags.ADD_UNIQUE | ListWriteFlags.NO_FAIL);
    }

    @Test
    public void toListPolicyWithNoWriteFlags() throws Exception {
        Map<String, Object> policyMap = new HashMap<>();
        policyMap.put("order", "UNORDERED");

        ListPolicy listPolicy = (ListPolicy) mapper.bytesToObject(mapper.objectToBytes(policyMap));

        Assert.assertEquals(listPolicy.getOrder(), ListOrder.UNORDERED);

        com.aerospike.client.cdt.ListPolicy asListPolicy = listPolicy.toListPolicy();
        Assert.assertEquals(asListPolicy.attributes, ListOrder.UNORDERED.attributes);
        Assert.assertEquals(asListPolicy.flags, 0);
    }
}

class JsonListPolicyMapper extends ASTestMapper {

    public JsonListPolicyMapper() {
        super(JSONMessageConverter.getJSONObjectMapper(), ListPolicy.class);
    }
}

class MsgPackListPolicyMapper extends ASTestMapper {

    public MsgPackListPolicyMapper() {
        super(MsgPackConverter.getASMsgPackObjectMapper(), ListPolicy.class);
    }
}

