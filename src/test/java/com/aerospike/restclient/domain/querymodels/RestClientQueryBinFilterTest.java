package com.aerospike.restclient.domain.querymodels;

import com.aerospike.client.Value;
import com.aerospike.client.cdt.CTX;
import com.aerospike.client.query.Filter;
import com.aerospike.client.query.IndexCollectionType;
import com.aerospike.restclient.ASTestMapper;
import com.aerospike.restclient.ASTestUtils;
import com.aerospike.restclient.IASTestMapper;
import com.aerospike.restclient.config.JSONMessageConverter;
import com.aerospike.restclient.config.MsgPackConverter;
import com.aerospike.restclient.domain.ctxmodels.*;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RunWith(Parameterized.class)
public class RestClientQueryBinFilterTest {

    private final IASTestMapper mapper;

    private final String bin = "test-bin";

    @Parameterized.Parameters
    public static Object[] getParams() {
        return new Object[]{
                new JsonQueryBinFilterMapper(),
                new MsgPackQueryBinFilterMapper(),
        };
    }

    public RestClientQueryBinFilterTest(IASTestMapper mapper) {
        this.mapper = mapper;
    }

    @Test
    public void testEmptyMapDoesNotMapToRestClientQueryBody() {
        Map<String, Object> ctxMap = new HashMap<>();

        try {
            Object obj = mapper.bytesToObject(mapper.objectToBytes(ctxMap));
            Assert.fail("Should have not mapped to RestClientCTX");
        } catch (Exception e) {
            // Success
        }
    }

    @Test
    public void testMapsToRestClientQueryBinEqualFilter() {
        Map<String, Object> filterMap = new HashMap<>();
        filterMap.put("filterType", AerospikeAPIConstants.QueryFilterTypes.EQUAL);
        filterMap.put("binName", bin);
        filterMap.put("ctx", new ArrayList<RestClientCTX>(){});
        filterMap.put("value", 1);

        try {
            RestClientQueryBinEqualFilter restCTX = (RestClientQueryBinEqualFilter) mapper.bytesToObject(mapper.objectToBytes(filterMap));
            Assert.assertEquals(AerospikeAPIConstants.QueryFilterTypes.EQUAL, restCTX.filterType);
            Assert.assertEquals(bin, restCTX.binName);
            Assert.assertEquals(1, restCTX.value);
            ASTestUtils.compareCollection(new ArrayList<RestClientCTX>(){}, restCTX.ctx);
        } catch (Exception e) {
            Assert.fail(String.format("Should have mapped to RestClientQueryBinFilter %s", e));
        }
    }

    @Test
    public void testBinEqualFilterWithIntegerToASFilter() {
        Filter expected = Filter.equal(bin, 5, CTX.listIndex(-1), CTX.mapKey(Value.get("key")));

        RestClientQueryBinEqualFilter restClientFilter = new RestClientQueryBinEqualFilter();
        restClientFilter.binName = bin;
        restClientFilter.value = 5;
        restClientFilter.ctx = new ArrayList<RestClientCTX>();
        restClientFilter.ctx.add(new RestClientCTXListIndex(-1));
        restClientFilter.ctx.add(new RestClientCTXMapKey("key"));

        ASTestUtils.compareFilter(expected, restClientFilter.toFilter());
    }

    @Test
    public void testBinEqualFilterWithStringToASFilter() {
        Filter expected = Filter.equal(bin, "str-val", CTX.listIndex(-1), CTX.mapKey(Value.get("key")));

        RestClientQueryBinEqualFilter restClientFilter = new RestClientQueryBinEqualFilter();
        restClientFilter.binName = bin;
        restClientFilter.value = "str-val";
        restClientFilter.ctx = new ArrayList<RestClientCTX>();
        restClientFilter.ctx.add(new RestClientCTXListIndex(-1));
        restClientFilter.ctx.add(new RestClientCTXMapKey("key"));

        ASTestUtils.compareFilter(expected, restClientFilter.toFilter());
    }

    @Test
    public void testMapsToRestClientQueryBinRangeFilter() {
        Map<String, Object> filterMap = new HashMap<>();
        filterMap.put("filterType", AerospikeAPIConstants.QueryFilterTypes.RANGE);
        filterMap.put("binName", bin);
        filterMap.put("ctx", new ArrayList<RestClientCTX>(){});
        filterMap.put("begin", 1);
        filterMap.put("end", 99);

        try {
            RestClientQueryBinRangeFilter restCTX = (RestClientQueryBinRangeFilter) mapper.bytesToObject(mapper.objectToBytes(filterMap));
            Assert.assertEquals(AerospikeAPIConstants.QueryFilterTypes.RANGE, restCTX.filterType);
            Assert.assertEquals(bin, restCTX.binName);
            Assert.assertEquals(1, restCTX.begin);
            Assert.assertEquals(99, restCTX.end);
            Assert.assertEquals(IndexCollectionType.DEFAULT, restCTX.collectionType);
            ASTestUtils.compareCollection(new ArrayList<RestClientCTX>(){}, restCTX.ctx);
        } catch (Exception e) {
            Assert.fail(String.format("Should have mapped to RestClientQueryBinFilter %s", e));
        }
    }

    @Test
    public void testBinRangeFilterToASFilter() {
        Filter expected = Filter.range(bin, IndexCollectionType.LIST, 10, 100, CTX.listRank(-1), CTX.mapValue(Value.get(3.14159)));

        RestClientQueryBinRangeFilter restClientFilter = new RestClientQueryBinRangeFilter();
        restClientFilter.binName = bin;
        restClientFilter.collectionType = IndexCollectionType.LIST;
        restClientFilter.begin = 10;
        restClientFilter.end = 100;
        restClientFilter.ctx = new ArrayList<RestClientCTX>();
        restClientFilter.ctx.add(new RestClientCTXListRank(-1));
        restClientFilter.ctx.add(new RestClientCTXMapValue(3.14159));

        ASTestUtils.compareFilter(expected, restClientFilter.toFilter());
    }

    @Test
    public void testMapsToRestClientQueryBinContainsFilter() {
        Map<String, Object> filterMap = new HashMap<>();
        filterMap.put("filterType", AerospikeAPIConstants.QueryFilterTypes.CONTAINS);
        filterMap.put("binName", bin);
        filterMap.put("ctx", new ArrayList<RestClientCTX>(){});
        filterMap.put("value", 1);

        try {
            RestClientQueryBinContainsFilter restCTX = (RestClientQueryBinContainsFilter) mapper.bytesToObject(mapper.objectToBytes(filterMap));
            Assert.assertEquals(AerospikeAPIConstants.QueryFilterTypes.CONTAINS, restCTX.filterType);
            Assert.assertEquals(bin, restCTX.binName);
            Assert.assertEquals(1, restCTX.value);
            Assert.assertEquals(IndexCollectionType.DEFAULT, restCTX.collectionType);
            ASTestUtils.compareCollection(new ArrayList<RestClientCTX>(){}, restCTX.ctx);
        } catch (Exception e) {
            Assert.fail(String.format("Should have mapped to RestClientQueryBinFilter %s", e));
        }
    }

    @Test
    public void testBinContainsFilterWithIntegerToASFilter() {
        Filter expected = Filter.contains(bin, IndexCollectionType.MAPKEYS, 5, CTX.listIndex(-1), CTX.mapKey(Value.get("key")));

        RestClientQueryBinContainsFilter restClientFilter = new RestClientQueryBinContainsFilter();
        restClientFilter.binName = bin;
        restClientFilter.value = 5;
        restClientFilter.ctx = new ArrayList<RestClientCTX>();
        restClientFilter.ctx.add(new RestClientCTXListIndex(-1));
        restClientFilter.ctx.add(new RestClientCTXMapKey("key"));
        restClientFilter.collectionType = IndexCollectionType.MAPKEYS;

        ASTestUtils.compareFilter(expected, restClientFilter.toFilter());
    }

    @Test
    public void testBinContainsFilterWithStringToASFilter() {
        Filter expected = Filter.contains(bin, IndexCollectionType.LIST, "str-val", CTX.listIndex(-1), CTX.mapKey(Value.get("key")));

        RestClientQueryBinContainsFilter restClientFilter = new RestClientQueryBinContainsFilter();
        restClientFilter.binName = bin;
        restClientFilter.value = "str-val";
        restClientFilter.ctx = new ArrayList<RestClientCTX>();
        restClientFilter.ctx.add(new RestClientCTXListIndex(-1));
        restClientFilter.ctx.add(new RestClientCTXMapKey("key"));
        restClientFilter.collectionType = IndexCollectionType.LIST;

        ASTestUtils.compareFilter(expected, restClientFilter.toFilter());
    }

    @Test
    public void testMapsToRestClientQueryBinGeoWithinRegionFilter() {
        Map<String, Object> filterMap = new HashMap<>();
        filterMap.put("filterType", AerospikeAPIConstants.QueryFilterTypes.GEOWITHIN_REGION);
        filterMap.put("binName", bin);
        filterMap.put("ctx", new ArrayList<RestClientCTX>(){});
        filterMap.put("region", "this-is-json-region");

        try {
            RestClientQueryBinGeoWithinRegionFilter restCTX = (RestClientQueryBinGeoWithinRegionFilter) mapper.bytesToObject(mapper.objectToBytes(filterMap));
            Assert.assertEquals(AerospikeAPIConstants.QueryFilterTypes.GEOWITHIN_REGION, restCTX.filterType);
            Assert.assertEquals(bin, restCTX.binName);
            Assert.assertEquals("this-is-json-region", restCTX.region);
            Assert.assertEquals(IndexCollectionType.DEFAULT, restCTX.collectionType);
            ASTestUtils.compareCollection(new ArrayList<RestClientCTX>(){}, restCTX.ctx);
        } catch (Exception e) {
            Assert.fail(String.format("Should have mapped to RestClientQueryBinFilter %s", e));
        }
    }

    @Test
    public void testBinGeoWithinRegionFilterToASFilter() {
        Filter expected = Filter.geoWithinRegion(bin, IndexCollectionType.MAPKEYS, "this-is-json-region", CTX.listIndex(-1), CTX.mapKey(Value.get("key")));

        RestClientQueryBinGeoWithinRegionFilter restClientFilter = new RestClientQueryBinGeoWithinRegionFilter();
        restClientFilter.binName = bin;
        restClientFilter.region = "this-is-json-region";
        restClientFilter.ctx = new ArrayList<RestClientCTX>();
        restClientFilter.ctx.add(new RestClientCTXListIndex(-1));
        restClientFilter.ctx.add(new RestClientCTXMapKey("key"));
        restClientFilter.collectionType = IndexCollectionType.MAPKEYS;

        ASTestUtils.compareFilter(expected, restClientFilter.toFilter());
    }

    @Test
    public void testMapsToRestClientQueryBinGeoWithinRadiusFilter() {
        Map<String, Object> filterMap = new HashMap<>();
        filterMap.put("filterType", AerospikeAPIConstants.QueryFilterTypes.GEOWITHIN_RADIUS);
        filterMap.put("binName", bin);
        filterMap.put("ctx", new ArrayList<RestClientCTX>(){});
        filterMap.put("latitude", 1.2345);
        filterMap.put("longitude", 6.789);
        filterMap.put("radius", 3.14159);

        try {
            RestClientQueryBinGeoWithinRadiusFilter restCTX = (RestClientQueryBinGeoWithinRadiusFilter) mapper.bytesToObject(mapper.objectToBytes(filterMap));
            Assert.assertEquals(AerospikeAPIConstants.QueryFilterTypes.GEOWITHIN_RADIUS, restCTX.filterType);
            Assert.assertEquals(bin, restCTX.binName);
            Assert.assertEquals(1.2345, restCTX.latitude, 0);
            Assert.assertEquals(6.789, restCTX.longitude, 0);
            Assert.assertEquals(3.14159, restCTX.radius, 0);
            Assert.assertEquals(IndexCollectionType.DEFAULT, restCTX.collectionType);
            ASTestUtils.compareCollection(new ArrayList<RestClientCTX>(){}, restCTX.ctx);
        } catch (Exception e) {
            Assert.fail(String.format("Should have mapped to RestClientQueryBinFilter %s", e));
        }
    }

    @Test
    public void testBinGeoWithinRadiusFilterToASFilter() {
        Filter expected = Filter.geoWithinRadius(bin, IndexCollectionType.MAPKEYS, 1.2345, 6.789, 3.14159, CTX.listIndex(-1), CTX.mapKey(Value.get("key")));

        RestClientQueryBinGeoWithinRadiusFilter restClientFilter = new RestClientQueryBinGeoWithinRadiusFilter();
        restClientFilter.binName = bin;
        restClientFilter.latitude = 1.2345;
        restClientFilter.longitude = 6.789;
        restClientFilter.radius = 3.14159;
        restClientFilter.ctx = new ArrayList<RestClientCTX>();
        restClientFilter.ctx.add(new RestClientCTXListIndex(-1));
        restClientFilter.ctx.add(new RestClientCTXMapKey("key"));
        restClientFilter.collectionType = IndexCollectionType.MAPKEYS;

        ASTestUtils.compareFilter(expected, restClientFilter.toFilter());
    }

    @Test
    public void testMapsToRestClientQueryBinGeoContainsPointFilter() {
        Map<String, Object> filterMap = new HashMap<>();
        filterMap.put("filterType", AerospikeAPIConstants.QueryFilterTypes.GEOCONTAINS_POINT);
        filterMap.put("binName", bin);
        filterMap.put("ctx", new ArrayList<RestClientCTX>(){});
        filterMap.put("point", "abcdef");

        try {
            RestClientQueryBinGeoContainsPointFilter restCTX = (RestClientQueryBinGeoContainsPointFilter) mapper.bytesToObject(mapper.objectToBytes(filterMap));
            Assert.assertEquals(AerospikeAPIConstants.QueryFilterTypes.GEOCONTAINS_POINT, restCTX.filterType);
            Assert.assertEquals(bin, restCTX.binName);
            Assert.assertEquals("abcdef", restCTX.point);
            Assert.assertEquals(IndexCollectionType.DEFAULT, restCTX.collectionType);
            ASTestUtils.compareCollection(new ArrayList<RestClientCTX>(){}, restCTX.ctx);
        } catch (Exception e) {
            Assert.fail(String.format("Should have mapped to RestClientQueryBinFilter %s", e));
        }
    }

    @Test
    public void testBinGeoContainsPointFilterToASFilter() {
        Filter expected = Filter.geoContains(bin, IndexCollectionType.MAPKEYS, "abcdef", CTX.listIndex(-1), CTX.mapKey(Value.get("key")));

        RestClientQueryBinGeoContainsPointFilter restClientFilter = new RestClientQueryBinGeoContainsPointFilter();
        restClientFilter.binName = bin;
        restClientFilter.point = "abcdef";
        restClientFilter.ctx = new ArrayList<RestClientCTX>();
        restClientFilter.ctx.add(new RestClientCTXListIndex(-1));
        restClientFilter.ctx.add(new RestClientCTXMapKey("key"));
        restClientFilter.collectionType = IndexCollectionType.MAPKEYS;

        ASTestUtils.compareFilter(expected, restClientFilter.toFilter());
    }
}

class JsonQueryBinFilterMapper extends ASTestMapper {

    public JsonQueryBinFilterMapper() {
        super(JSONMessageConverter.getJSONObjectMapper(), RestClientQueryFilter.class);
    }
}

class MsgPackQueryBinFilterMapper extends ASTestMapper {

    public MsgPackQueryBinFilterMapper() {
        super(MsgPackConverter.getASMsgPackObjectMapper(), RestClientQueryFilter.class);
    }
}
