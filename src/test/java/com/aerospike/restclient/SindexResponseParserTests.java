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

import com.aerospike.client.AerospikeException;
import com.aerospike.restclient.util.InfoResponseParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class SindexResponseParserTests {
    @Test
    public void testSindexResponseSplitsIntoMaps() throws Exception {
        String responseString = "ns=bar:set=demo:indexname=int_index_1:bin=intbin1:type=NUMERIC:indextype=NONE:path=intbin1:state=RW;" + "ns=test:set=demo:indexname=int_index_1:bin=intbin1:type=NUMERIC:indextype=NONE:path=intbin1:state=RW;" + "ns=test:set=demo:indexname=str_index1:bin=strbin1:type=STRING:indextype=NONE:path=strbin1:state=RW;\n";

        List<Map<String, String>> indexInfoAry = InfoResponseParser.getIndexInformation(responseString);

        Assert.assertEquals(indexInfoAry.size(), 3);
    }

    @Test
    public void testSindexResponseGetsIndexInfo() throws Exception {
        String responseString = "ns=bar:set=demo:indexname=int_index_1:bin=intbin1:type=NUMERIC:indextype=NONE:path=intbin1:state=RW;";
        List<Map<String, String>> indexInfoAry = InfoResponseParser.getIndexInformation(responseString);
        Map<String, String> indexInfo = indexInfoAry.get(0);

        Assert.assertTrue(indexInfoNameEquals(indexInfo, "int_index_1"));
        Assert.assertTrue(indexInfoBinEquals(indexInfo, "intbin1"));
        Assert.assertTrue(indexInfoNamespaceEquals(indexInfo, "bar"));
        Assert.assertTrue(indexInfoSetEquals(indexInfo, "demo"));
        Assert.assertTrue(indexInfoTypeEquals(indexInfo, "NUMERIC"));
        Assert.assertTrue(indexInfoIndexTypeEquals(indexInfo, "NONE"));
    }

    @Test
    public void testEmptySindexResponse() throws Exception {
        /*
         * If there are no indexes we expect an empty list
         */
        String responseString = "";
        List<Map<String, String>> indexInfoAry = InfoResponseParser.getIndexInformation(responseString);
        Assert.assertEquals(0, indexInfoAry.size());
    }

    @Test(expected = AerospikeException.class)
    public void testInvalidNamespaceResponse() throws Exception {
        /*
         * If there are no indexes we expect an empty list
         */
        String responseString = "ns_type=unknown\n";
        InfoResponseParser.getIndexInformation(responseString);
    }

    private boolean indexInfoNameEquals(Map<String, String> indexInfo, String name) {
        return name.equals(indexInfo.get("indexname"));
    }

    private boolean indexInfoBinEquals(Map<String, String> indexInfo, String binName) {
        return binName.equals(indexInfo.get("bin"));
    }

    private boolean indexInfoNamespaceEquals(Map<String, String> indexInfo, String namespace) {
        return namespace.equals(indexInfo.get("ns"));
    }

    private boolean indexInfoSetEquals(Map<String, String> indexInfo, String set) {
        return set.equals(indexInfo.get("set"));
    }

    private boolean indexInfoTypeEquals(Map<String, String> indexInfo, String type) {
        return type.equals(indexInfo.get("type"));
    }

    private boolean indexInfoIndexTypeEquals(Map<String, String> indexInfo, String indexType) {
        return indexType.equals(indexInfo.get("indextype"));
    }

}

