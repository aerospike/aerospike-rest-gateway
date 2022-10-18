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

import com.aerospike.client.Value;
import com.aerospike.client.query.Filter;
import com.aerospike.client.query.IndexCollectionType;
import com.aerospike.restclient.ASTestMapper;
import com.aerospike.restclient.ASTestUtils;
import com.aerospike.restclient.IASTestMapper;
import com.aerospike.restclient.config.JSONMessageConverter;
import com.aerospike.restclient.config.MsgPackConverter;
import com.aerospike.restclient.domain.ctxmodels.*;
import com.aerospike.restclient.domain.geojsonmodels.LngLat;
import com.aerospike.restclient.domain.geojsonmodels.LngLatRad;
import com.aerospike.restclient.domain.geojsonmodels.Point;
import com.aerospike.restclient.domain.geojsonmodels.Polygon;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(Parameterized.class)
public class QueryFilterTest {

    private final IASTestMapper mapper;

    private final String bin = "test-bin";

    @Parameterized.Parameters
    public static Object[] getParams() {
        return new Object[]{
                new JsonQueryBinFilterMapper(), new MsgPackQueryBinFilterMapper(),
                };
    }

    public QueryFilterTest(IASTestMapper mapper) {
        this.mapper = mapper;
    }

    @Test
    public void testEmptyMapDoesNotMapToQueryBody() {
        Map<String, Object> ctxMap = new HashMap<>();

        try {
            mapper.bytesToObject(mapper.objectToBytes(ctxMap));
            Assert.fail("Should have not mapped to CTX");
        } catch (Exception e) {
            // Success
        }
    }

    @Test
    public void testMapsToQueryStringEqualFilter() {
        Map<String, Object> filterMap = new HashMap<>();
        filterMap.put("type", AerospikeAPIConstants.QueryFilterTypes.EQUAL_STRING);
        filterMap.put("binName", bin);
        filterMap.put("ctx", new ArrayList<CTX>() {
        });
        filterMap.put("value", "str");

        try {
            QueryEqualsStringFilter restCTX = (QueryEqualsStringFilter) mapper.bytesToObject(
                    mapper.objectToBytes(filterMap));
            Assert.assertEquals(AerospikeAPIConstants.QueryFilterTypes.EQUAL_STRING, restCTX.type);
            Assert.assertEquals(bin, restCTX.binName);
            Assert.assertEquals("str", restCTX.value);
            ASTestUtils.compareCollection(new ArrayList<CTX>() {
            }, restCTX.ctx);
        } catch (Exception e) {
            Assert.fail(String.format("Should have mapped to QueryBinFilter %s", e));
        }
    }

    @Test
    public void testMapsToQueryLongEqualFilter() {
        Map<String, Object> filterMap = new HashMap<>();
        filterMap.put("type", AerospikeAPIConstants.QueryFilterTypes.EQUAL_LONG);
        filterMap.put("binName", bin);
        filterMap.put("ctx", new ArrayList<CTX>() {
        });
        filterMap.put("value", 1L);

        try {
            QueryEqualLongFilter restCTX = (QueryEqualLongFilter) mapper.bytesToObject(mapper.objectToBytes(filterMap));
            Assert.assertEquals(AerospikeAPIConstants.QueryFilterTypes.EQUAL_LONG, restCTX.type);
            Assert.assertEquals(bin, restCTX.binName);
            Assert.assertEquals(Long.valueOf(1), restCTX.value);
            ASTestUtils.compareCollection(new ArrayList<CTX>() {
            }, restCTX.ctx);
        } catch (Exception e) {
            Assert.fail(String.format("Should have mapped to QueryBinFilter %s", e));
        }
    }

    @Test
    public void testBinEqualFilterWithIntegerToASFilter() {
        Filter expected = Filter.equal(bin, 5, com.aerospike.client.cdt.CTX.listIndex(-1),
                com.aerospike.client.cdt.CTX.mapKey(Value.get("key")));

        QueryEqualLongFilter restClientFilter = new QueryEqualLongFilter();
        restClientFilter.binName = bin;
        restClientFilter.value = 5L;
        restClientFilter.ctx = new ArrayList<CTX>();
        restClientFilter.ctx.add(new ListIndexCTX(-1));
        restClientFilter.ctx.add(new MapKeyCTX("key"));

        ASTestUtils.compareFilter(expected, restClientFilter.toFilter());
    }

    @Test
    public void testBinEqualFilterWithStringToASFilter() {
        Filter expected = Filter.equal(bin, "str-val", com.aerospike.client.cdt.CTX.listIndex(-1),
                com.aerospike.client.cdt.CTX.mapKey(Value.get("key")));

        QueryEqualsStringFilter restClientFilter = new QueryEqualsStringFilter();
        restClientFilter.binName = bin;
        restClientFilter.value = "str-val";
        restClientFilter.ctx = new ArrayList<CTX>();
        restClientFilter.ctx.add(new ListIndexCTX(-1));
        restClientFilter.ctx.add(new MapKeyCTX("key"));

        ASTestUtils.compareFilter(expected, restClientFilter.toFilter());
    }

    @Test
    public void testMapsToQueryBinRangeFilter() {
        Map<String, Object> filterMap = new HashMap<>();
        filterMap.put("type", AerospikeAPIConstants.QueryFilterTypes.RANGE);
        filterMap.put("binName", bin);
        filterMap.put("ctx", new ArrayList<CTX>() {
        });
        filterMap.put("begin", 1);
        filterMap.put("end", 99);

        try {
            QueryRangeFilter restCTX = (QueryRangeFilter) mapper.bytesToObject(mapper.objectToBytes(filterMap));
            Assert.assertEquals(AerospikeAPIConstants.QueryFilterTypes.RANGE, restCTX.type);
            Assert.assertEquals(bin, restCTX.binName);
            Assert.assertEquals(1, restCTX.begin);
            Assert.assertEquals(99, restCTX.end);
            Assert.assertEquals(IndexCollectionType.DEFAULT, restCTX.collectionType);
            ASTestUtils.compareCollection(new ArrayList<CTX>() {
            }, restCTX.ctx);
        } catch (Exception e) {
            Assert.fail(String.format("Should have mapped to QueryBinFilter %s", e));
        }
    }

    @Test
    public void testBinRangeFilterToASFilter() {
        Filter expected = Filter.range(bin, IndexCollectionType.LIST, 10, 100,
                com.aerospike.client.cdt.CTX.listRank(-1), com.aerospike.client.cdt.CTX.mapValue(Value.get(3.14159)));

        QueryRangeFilter restClientFilter = new QueryRangeFilter();
        restClientFilter.binName = bin;
        restClientFilter.collectionType = IndexCollectionType.LIST;
        restClientFilter.begin = 10;
        restClientFilter.end = 100;
        restClientFilter.ctx = new ArrayList<CTX>();
        restClientFilter.ctx.add(new ListRankCTX(-1));
        restClientFilter.ctx.add(new MapValueCTX(3.14159));

        ASTestUtils.compareFilter(expected, restClientFilter.toFilter());
    }

    @Test
    public void testMapsToQueryBinContainsFilter() {
        Map<String, Object> filterMap = new HashMap<>();
        filterMap.put("type", AerospikeAPIConstants.QueryFilterTypes.CONTAINS_LONG);
        filterMap.put("binName", bin);
        filterMap.put("ctx", new ArrayList<CTX>() {
        });
        filterMap.put("value", 1);

        try {
            QueryContainsLongFilter restCTX = (QueryContainsLongFilter) mapper.bytesToObject(
                    mapper.objectToBytes(filterMap));
            Assert.assertEquals(AerospikeAPIConstants.QueryFilterTypes.CONTAINS_LONG, restCTX.type);
            Assert.assertEquals(bin, restCTX.binName);
            Assert.assertEquals(Long.valueOf(1), restCTX.value);
            Assert.assertEquals(IndexCollectionType.DEFAULT, restCTX.collectionType);
            ASTestUtils.compareCollection(new ArrayList<CTX>() {
            }, restCTX.ctx);
        } catch (Exception e) {
            Assert.fail(String.format("Should have mapped to QueryBinFilter %s", e));
        }
    }

    @Test
    public void testBinContainsFilterWithIntegerToASFilter() {
        Filter expected = Filter.contains(bin, IndexCollectionType.MAPKEYS, 5,
                com.aerospike.client.cdt.CTX.listIndex(-1), com.aerospike.client.cdt.CTX.mapKey(Value.get("key")));

        QueryContainsLongFilter restClientFilter = new QueryContainsLongFilter();
        restClientFilter.binName = bin;
        restClientFilter.value = 5L;
        restClientFilter.ctx = new ArrayList<CTX>();
        restClientFilter.ctx.add(new ListIndexCTX(-1));
        restClientFilter.ctx.add(new MapKeyCTX("key"));
        restClientFilter.collectionType = IndexCollectionType.MAPKEYS;

        ASTestUtils.compareFilter(expected, restClientFilter.toFilter());
    }

    @Test
    public void testBinContainsFilterWithStringToASFilter() {
        Filter expected = Filter.contains(bin, IndexCollectionType.LIST, "str-val",
                com.aerospike.client.cdt.CTX.listIndex(-1), com.aerospike.client.cdt.CTX.mapKey(Value.get("key")));

        QueryContainsStringFilter restClientFilter = new QueryContainsStringFilter();
        restClientFilter.binName = bin;
        restClientFilter.value = "str-val";
        restClientFilter.ctx = new ArrayList<CTX>();
        restClientFilter.ctx.add(new ListIndexCTX(-1));
        restClientFilter.ctx.add(new MapKeyCTX("key"));
        restClientFilter.collectionType = IndexCollectionType.LIST;

        ASTestUtils.compareFilter(expected, restClientFilter.toFilter());
    }

    @Test
    public void testMapsToQueryBinGeoWithinRegionFilter() {
        Map<String, Object> filterMap = new HashMap<>();
        double[][] coord = new double[][]{
                new double[]{1, 2}, new double[]{3, 4}, new double[]{4, 5},
                };
        filterMap.put("type", AerospikeAPIConstants.QueryFilterTypes.GEOWITHIN_REGION);
        filterMap.put("binName", bin);
        filterMap.put("ctx", new ArrayList<CTX>() {
        });
        filterMap.put("polygon", coord);

        List<LngLat> expectedLngLat = new ArrayList<LngLat>();
        expectedLngLat.add(new LngLat(1, 2));
        expectedLngLat.add(new LngLat(3, 4));
        expectedLngLat.add(new LngLat(4, 5));

        try {
            QueryGeoWithinPolygonFilter restCTX = (QueryGeoWithinPolygonFilter) mapper.bytesToObject(
                    mapper.objectToBytes(filterMap));
            Assert.assertEquals(AerospikeAPIConstants.QueryFilterTypes.GEOWITHIN_REGION, restCTX.type);
            Assert.assertEquals(bin, restCTX.binName);
            Assert.assertEquals(expectedLngLat, restCTX.polygon);
            Assert.assertEquals(IndexCollectionType.DEFAULT, restCTX.collectionType);
            ASTestUtils.compareCollection(new ArrayList<CTX>() {
            }, restCTX.ctx);
        } catch (Exception e) {
            Assert.fail(String.format("Should have mapped to QueryBinFilter %s", e));
        }
    }

    @Test
    public void testBinGeoWithinRegionFilterToASFilter() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Polygon region = new Polygon(new LngLat(1, 2));
        Filter expected = Filter.geoWithinRegion(bin, IndexCollectionType.MAPKEYS, mapper.writeValueAsString(region),
                com.aerospike.client.cdt.CTX.listIndex(-1), com.aerospike.client.cdt.CTX.mapKey(Value.get("key")));

        QueryGeoWithinPolygonFilter restClientFilter = new QueryGeoWithinPolygonFilter();
        List<LngLat> polygon = new ArrayList<>();
        polygon.add(new LngLat(1, 2));
        restClientFilter.binName = bin;
        restClientFilter.polygon = polygon;
        restClientFilter.ctx = new ArrayList<CTX>();
        restClientFilter.ctx.add(new ListIndexCTX(-1));
        restClientFilter.ctx.add(new MapKeyCTX("key"));
        restClientFilter.collectionType = IndexCollectionType.MAPKEYS;

        ASTestUtils.compareFilter(expected, restClientFilter.toFilter());
    }

    @Test
    public void testMapsToQueryBinGeoWithinRadiusFilter() {
        Map<String, Object> filterMap = new HashMap<>();
        List<Object> circle = new ArrayList<>();
        filterMap.put("type", AerospikeAPIConstants.QueryFilterTypes.GEOWITHIN_RADIUS);
        filterMap.put("binName", bin);
        filterMap.put("ctx", new ArrayList<CTX>() {
        });
        filterMap.put("circle", circle);
        circle.add(new double[]{1.2345, 6.789});
        circle.add(3.14159);

        try {
            QueryGeoWithinRadiusFilter restCTX = (QueryGeoWithinRadiusFilter) mapper.bytesToObject(
                    mapper.objectToBytes(filterMap));
            Assert.assertEquals(AerospikeAPIConstants.QueryFilterTypes.GEOWITHIN_RADIUS, restCTX.type);
            Assert.assertEquals(bin, restCTX.binName);
            Assert.assertEquals(1.2345, restCTX.circle.latLng.longitude, 0);
            Assert.assertEquals(6.789, restCTX.circle.latLng.latitude, 0);
            Assert.assertEquals(3.14159, restCTX.circle.radius, 0);
            Assert.assertEquals(IndexCollectionType.DEFAULT, restCTX.collectionType);
            ASTestUtils.compareCollection(new ArrayList<CTX>() {
            }, restCTX.ctx);
        } catch (Exception e) {
            Assert.fail(String.format("Should have mapped to QueryBinFilter %s", e));
        }
    }

    @Test
    public void testBinGeoWithinRadiusFilterToASFilter() {
        Filter expected = Filter.geoWithinRadius(bin, IndexCollectionType.MAPKEYS, 1.2345, 6.789, 3.14159,
                com.aerospike.client.cdt.CTX.listIndex(-1), com.aerospike.client.cdt.CTX.mapKey(Value.get("key")));

        QueryGeoWithinRadiusFilter restClientFilter = new QueryGeoWithinRadiusFilter();
        restClientFilter.binName = bin;
        restClientFilter.circle = new LngLatRad(1.2345, 6.789, 3.14159);
        restClientFilter.ctx = new ArrayList<CTX>();
        restClientFilter.ctx.add(new ListIndexCTX(-1));
        restClientFilter.ctx.add(new MapKeyCTX("key"));
        restClientFilter.collectionType = IndexCollectionType.MAPKEYS;

        ASTestUtils.compareFilter(expected, restClientFilter.toFilter());
    }

    @Test
    public void testMapsToQueryBinGeoContainsPointFilter() {
        Map<String, Object> filterMap = new HashMap<>();
        filterMap.put("type", AerospikeAPIConstants.QueryFilterTypes.GEOCONTAINS_POINT);
        filterMap.put("binName", bin);
        filterMap.put("ctx", new ArrayList<CTX>() {
        });
        filterMap.put("point", new double[]{1.2, 2.3});

        try {
            QueryGeoContainsPointFilter restCTX = (QueryGeoContainsPointFilter) mapper.bytesToObject(
                    mapper.objectToBytes(filterMap));
            Assert.assertEquals(AerospikeAPIConstants.QueryFilterTypes.GEOCONTAINS_POINT, restCTX.type);
            Assert.assertEquals(bin, restCTX.binName);
            Assert.assertEquals(new LngLat(1.2, 2.3), restCTX.point);
            Assert.assertEquals(IndexCollectionType.DEFAULT, restCTX.collectionType);
            ASTestUtils.compareCollection(new ArrayList<CTX>() {
            }, restCTX.ctx);
        } catch (Exception e) {
            Assert.fail(String.format("Should have mapped to QueryBinFilter %s", e));
        }
    }

    @Test
    public void testBinGeoContainsPointFilterToASFilter() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Point point = new Point(new LngLat(1, 2));
        Filter expected = Filter.geoContains(bin, IndexCollectionType.MAPKEYS, mapper.writeValueAsString(point),
                com.aerospike.client.cdt.CTX.listIndex(-1), com.aerospike.client.cdt.CTX.mapKey(Value.get("key")));

        QueryGeoContainsPointFilter restClientFilter = new QueryGeoContainsPointFilter();
        restClientFilter.binName = bin;
        restClientFilter.point = new LngLat(1, 2);
        restClientFilter.ctx = new ArrayList<CTX>();
        restClientFilter.ctx.add(new ListIndexCTX(-1));
        restClientFilter.ctx.add(new MapKeyCTX("key"));
        restClientFilter.collectionType = IndexCollectionType.MAPKEYS;

        ASTestUtils.compareFilter(expected, restClientFilter.toFilter());
    }
}

class JsonQueryBinFilterMapper extends ASTestMapper {

    public JsonQueryBinFilterMapper() {
        super(JSONMessageConverter.getJSONObjectMapper(), QueryFilter.class);
    }
}

class MsgPackQueryBinFilterMapper extends ASTestMapper {

    public MsgPackQueryBinFilterMapper() {
        super(MsgPackConverter.getASMsgPackObjectMapper(), QueryFilter.class);
    }
}
