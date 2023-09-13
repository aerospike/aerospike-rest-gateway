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
package com.aerospike.restclient.converters;

import com.aerospike.client.Bin;
import com.aerospike.client.Value;
import com.aerospike.client.Value.GeoJSONValue;
import com.aerospike.restclient.util.converters.BinConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gnu.crypto.util.Base64;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.aerospike.restclient.util.converters.BinConverter.binFromObject;

public class BinConverterTests {

    @Test
    public void testStringBin() {
        singleObjectBinTest("aerospike");
    }

    @Test
    public void testLongBin() {
        singleObjectBinTest(5L);
    }

    @Test
    public void testFloatBin() {
        singleObjectBinTest(5L);
    }

    @Test
    public void testMapBin() {
        Map<String, Object> testMap = new HashMap<>();
        testMap.put("str", "hello");
        testMap.put("float", 3.14);
        testMap.put("long", 5L);
        singleObjectBinTest(testMap);
    }

    @Test
    public void testComplexCDTBin() {
        Map<String, Object> testMap = new HashMap<>();
        Map<String, Object> nestedMap = new HashMap<>();
        nestedMap.put("str", "world");
        nestedMap.put("int", 10);
        nestedMap.put("long", 5L);
        List<Object> nestedList = new ArrayList<>();
        nestedList.add(1);
        nestedList.add("str");
        testMap.put("str", "hello");
        testMap.put("float", 3.14);
        testMap.put("long", 5L);
        testMap.put("nestedMap", nestedMap);
        testMap.put("nestedList", nestedList);
        singleObjectBinTest(testMap);
    }

    @Test
    public void testBytesBin() {
        singleObjectBinTest(new byte[]{1, 2, 3});
    }

    // Really only tests whether the GeoJSONValue is passed through the BinConverter untouched.
    @Test
    public void testGeoJSONBin() {
        Bin testBin = new Bin("bin1", new GeoJSONValue("{\"coordinates\": [-122.0, 37.5], \"type\": \"Point\"}"));
        Map<String, Object> binMap = new HashMap<>();
        binMap.put("bin1", new GeoJSONValue("{\"coordinates\": [-122.0, 37.5], \"type\": \"Point\"}"));
        Bin[] bins = BinConverter.binsFromMap(binMap);
        Assert.assertTrue(binsContain(bins, testBin));
    }

    @Test
    public void testNullBin() {
        Bin testBin = new Bin("bin1", new Value.NullValue());
        Map<String, Object> binMap = new HashMap<>();
        binMap.put("bin1", null);
        Bin[] bins = BinConverter.binsFromMap(binMap);
        Assert.assertTrue(binsContain(bins, testBin));
    }

    @Test
    public void testMultipleBins() {
        Bin bin1 = new Bin("bin1", "str");
        Bin bin2 = new Bin("bin2", 3L);

        Map<String, Object> binMap = new HashMap<>();
        binMap.put(bin1.name, bin1.value.getObject());
        binMap.put(bin2.name, bin2.value.getObject());

        Bin[] bins = BinConverter.binsFromMap(binMap);
        Assert.assertEquals(2, bins.length);

        Assert.assertTrue(binsContain(bins, bin1));
        Assert.assertTrue(binsContain(bins, bin2));

    }

    @Test
    public void testGeoJSON() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> testMap = new HashMap<>();
        testMap.put("type", "Point");
        testMap.put("coordinates", new double[]{-122.0, 37.5});
        Bin testBin = new Bin("bin1", Value.getAsGeoJSON(mapper.writeValueAsString(testMap)));
        Map<String, Object> binMap = new HashMap<>();
        binMap.put("bin1", testMap);
        Bin[] bins = BinConverter.binsFromMap(binMap);
        Assert.assertTrue(binsContain(bins, testBin));
    }

    @Ignore("Fails. Will pass if we ever support geojson nested in CDTs with json. Currently, only supported using msgpack.")
    @Test
    public void testNestedGeoJSON() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> geoVal = new HashMap<>();
        geoVal.put("type", "Point");
        geoVal.put("coordinates", new double[]{-122.0, 37.5});
        Map<String, Object> mapBin = new HashMap<>();
        mapBin.put("map", geoVal);

        Map<String, Object> testMapBin = new HashMap<>();
        testMapBin.put("map", Value.getAsGeoJSON(mapper.writeValueAsString(geoVal)));
        Bin testBin = new Bin("bin1", testMapBin);
        Map<String, Object> binMap = new HashMap<>();
        binMap.put("bin1", mapBin);
        Bin[] bins = BinConverter.binsFromMap(binMap);
        Assert.assertTrue(binsContain(bins, testBin));
    }

    @Test
    public void testBase64GeoJSONBin() {
        String geoJSONStr = "{\"coordinates\": [-122.0, 37.5], \"type\": \"Point\"}";
        String geoJSONBase64Str = Base64.encode(geoJSONStr.getBytes(StandardCharsets.UTF_8));
        Map<String, Object> testMap = new HashMap<>();
        testMap.put("type", "GEO_JSON");
        testMap.put("value", geoJSONBase64Str);
        Bin testBin = new Bin("bin1", Value.getAsGeoJSON(geoJSONStr));
        Map<String, Object> binMap = new HashMap<>();
        binMap.put("bin1", testMap);
        Bin[] bins = BinConverter.binsFromMap(binMap);
        Assert.assertTrue(binsContain(bins, testBin));
    }

    @Test
    public void testBase64BytesBin() {
        byte[] bytes = "foobar".getBytes(StandardCharsets.UTF_8);
        String base64Bytes = Base64.encode(bytes);
        Map<String, Object> testMap = new HashMap<>();
        testMap.put("type", "BYTE_ARRAY");
        testMap.put("value", base64Bytes);
        Bin testBin = new Bin("bin1", Value.get(bytes));
        Map<String, Object> binMap = new HashMap<>();
        binMap.put("bin1", testMap);
        Bin[] bins = BinConverter.binsFromMap(binMap);
        Assert.assertTrue(binsContain(bins, testBin));
    }

    private void singleObjectBinTest(Object binValue) {
        Bin testBin = binFromObject("bin1", binValue);
        Map<String, Object> binMap = new HashMap<>();
        binMap.put("bin1", binValue);
        Bin[] bins = BinConverter.binsFromMap(binMap);
        Assert.assertTrue(binsContain(bins, testBin));
    }

    private boolean binsContain(Bin[] bins, Bin target) {
        for (Bin bin : bins) {
            if (bin.equals(target)) {
                return true;
            }
        }
        return false;
    }
}
