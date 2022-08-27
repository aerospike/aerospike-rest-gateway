package com.aerospike.restclient.domain.ctxmodels;

import com.aerospike.client.Value;
import com.aerospike.client.cdt.CTX;
import com.aerospike.client.cdt.ListOrder;
import com.aerospike.client.cdt.MapOrder;
import com.aerospike.restclient.ASTestMapper;
import com.aerospike.restclient.ASTestUtils;
import com.aerospike.restclient.IASTestMapper;
import com.aerospike.restclient.config.JSONMessageConverter;
import com.aerospike.restclient.config.MsgPackConverter;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.HashMap;
import java.util.Map;

@RunWith(Parameterized.class)
public class RestClientCTXTest {

    private final IASTestMapper mapper;
    private final static String ns = "test";
    private final static String set = "ctx";

    @Parameterized.Parameters
    public static Object[] getParams() {
        return new Object[]{
                new JsonCTXMapper(),
                new MsgPackCTXMapper()
        };
    }

    public RestClientCTXTest(IASTestMapper mapper) {
        this.mapper = mapper;
    }

    @Test
    public void testEmptyMapDoesNotMapToRestClientCTX() {
        Map<String, Object> ctxMap = new HashMap<>();

        try {
            mapper.bytesToObject(mapper.objectToBytes(ctxMap));
            Assert.fail("Should have not mapped to RestClientCTX");
        } catch (Exception e) {
            // Success
        }
    }

    @Test
    public void testMapsToRestClientCTXListIndex() {
        Map<String, Object> ctxMap = new HashMap<>();
        ctxMap.put("ctxType", AerospikeAPIConstants.LIST_INDEX);
        ctxMap.put("index", 1);

        try {
            RestClientCTXListIndex restCTX = (RestClientCTXListIndex) mapper.bytesToObject(mapper.objectToBytes(ctxMap));
            Assert.assertEquals(restCTX.ctxType, AerospikeAPIConstants.LIST_INDEX);
            Assert.assertEquals(restCTX.index.intValue(), 1);
        } catch (Exception e) {
            Assert.fail(String.format("Should have mapped to RestClientCTXListIndex %s", e));
        }
    }

    @Test
    public void testRestClientCTXListIndexToASCTX() {
        CTX expected = CTX.listIndex(5);

        RestClientCTXListIndex listIndex = new RestClientCTXListIndex();
        listIndex.index = 5;

        ASTestUtils.compareCTX(expected, listIndex.toCTX());
    }

    @Test
    public void testMapsToRestClientCTXListIndexCreate() {
        Map<String, Object> ctxMap = new HashMap<>();
        ctxMap.put("ctxType", AerospikeAPIConstants.LIST_INDEX_CREATE);
        ctxMap.put("index", 1);
        ctxMap.put("order", ListOrder.ORDERED);
        ctxMap.put("pad", true);


        try {
            RestClientCTXListIndexCreate restCTX = (RestClientCTXListIndexCreate) mapper.bytesToObject(mapper.objectToBytes(ctxMap));
            Assert.assertEquals(restCTX.ctxType, AerospikeAPIConstants.LIST_INDEX_CREATE);
            Assert.assertEquals(restCTX.index.intValue(), 1);
            Assert.assertEquals(restCTX.order, ListOrder.ORDERED);
            Assert.assertTrue(restCTX.pad);
        } catch (Exception e) {
            Assert.fail(String.format("Should have mapped to RestClientCTXListIndexCreate %s", e));
        }
    }

    @Test
    public void testRestClientCTXListIndexCreateToASCTX() {
        CTX expected = CTX.listIndexCreate(5, ListOrder.UNORDERED, false);

        RestClientCTXListIndexCreate restCTX = new RestClientCTXListIndexCreate();
        restCTX.index = 5;
        restCTX.order = ListOrder.UNORDERED;
        restCTX.pad = false;

        ASTestUtils.compareCTX(expected, restCTX.toCTX());
    }

    @Test
    public void testMapsToRestClientCTXListRank() {
        Map<String, Object> ctxMap = new HashMap<>();
        ctxMap.put("ctxType", AerospikeAPIConstants.LIST_RANK);
        ctxMap.put("rank", 1);


        try {
            RestClientCTXListRank restCTX = (RestClientCTXListRank) mapper.bytesToObject(mapper.objectToBytes(ctxMap));
            Assert.assertEquals(restCTX.ctxType, AerospikeAPIConstants.LIST_RANK);
            Assert.assertEquals(restCTX.rank.intValue(), 1);
        } catch (Exception e) {
            Assert.fail(String.format("Should have mapped to RestClientCTXListIndexCreate %s", e));
        }
    }

    @Test
    public void testRestClientCTXListRankToASCTX() {
        CTX expected = CTX.listRank(5);

        RestClientCTXListRank restCTX = new RestClientCTXListRank();
        restCTX.rank = 5;

        ASTestUtils.compareCTX(expected, restCTX.toCTX());
    }

    // Checking all value types for ListValue. Will assume it works for the rest of the ctx types

    @Test
    public void tesMapToRestClientCTXListValueWithStrVal() {
        Map<String, Object> ctxMap = new HashMap<>();
        ctxMap.put("ctxType", AerospikeAPIConstants.LIST_VALUE);
        ctxMap.put("value", "abc");

        try {
            RestClientCTXListValue restCTX = (RestClientCTXListValue) mapper.bytesToObject(mapper.objectToBytes(ctxMap));
            Assert.assertEquals(restCTX.ctxType, AerospikeAPIConstants.LIST_VALUE);
            Assert.assertEquals(restCTX.value, "abc");
        } catch (Exception e) {
            Assert.fail(String.format("Should have mapped to RestClientCTXListIndexCreate %s", e));
        }
    }

    @Test
    public void tesMapToRestClientCTXListValueWithIntVal() {
        Map<String, Object> ctxMap = new HashMap<>();
        ctxMap.put("ctxType", AerospikeAPIConstants.LIST_VALUE);
        ctxMap.put("value", 9);

        try {
            RestClientCTXListValue restCTX = (RestClientCTXListValue) mapper.bytesToObject(mapper.objectToBytes(ctxMap));
            Assert.assertEquals(restCTX.ctxType, AerospikeAPIConstants.LIST_VALUE);
            Assert.assertEquals(restCTX.value, 9);
        } catch (Exception e) {
            Assert.fail(String.format("Should have mapped to RestClientCTXListIndexCreate %s", e));
        }
    }

    @Test
    public void tesMapToRestClientCTXListValueWithFloat() {
        Map<String, Object> ctxMap = new HashMap<>();
        ctxMap.put("ctxType", AerospikeAPIConstants.LIST_VALUE);
        ctxMap.put("value", 3.14159);

        try {
            RestClientCTXListValue restCTX = (RestClientCTXListValue) mapper.bytesToObject(mapper.objectToBytes(ctxMap));
            Assert.assertEquals(restCTX.ctxType, AerospikeAPIConstants.LIST_VALUE);
            Assert.assertEquals(restCTX.value, 3.14159);
        } catch (Exception e) {
            Assert.fail(String.format("Should have mapped to RestClientCTXListIndexCreate %s", e));
        }
    }

    @Test
    public void tesMapToRestClientCTXListValueWithBool() {
        Map<String, Object> ctxMap = new HashMap<>();
        ctxMap.put("ctxType", AerospikeAPIConstants.LIST_VALUE);
        ctxMap.put("value", true);

        try {
            RestClientCTXListValue restCTX = (RestClientCTXListValue) mapper.bytesToObject(mapper.objectToBytes(ctxMap));
            Assert.assertEquals(restCTX.ctxType, AerospikeAPIConstants.LIST_VALUE);
            Assert.assertEquals(restCTX.value, true);
        } catch (Exception e) {
            Assert.fail(String.format("Should have mapped to RestClientCTXListIndexCreate %s", e));
        }
    }

    @Test
    public void testRestClientCTXListValueToASCTX() {
        CTX expected = CTX.listValue(Value.get(3.14159));

        RestClientCTXListValue restCTX = new RestClientCTXListValue();
        restCTX.value = 3.14159;

        ASTestUtils.compareCTX(expected, restCTX.toCTX());
    }

    @Test
    public void tesMapToRestClientCTXMapIndex() {
        Map<String, Object> ctxMap = new HashMap<>();
        ctxMap.put("ctxType", AerospikeAPIConstants.MAP_INDEX);
        ctxMap.put("index", 11);


        try {
            RestClientCTXMapIndex restCTX = (RestClientCTXMapIndex) mapper.bytesToObject(mapper.objectToBytes(ctxMap));
            Assert.assertEquals(restCTX.ctxType, AerospikeAPIConstants.MAP_INDEX);
            Assert.assertEquals(restCTX.index.intValue(), 11);
        } catch (Exception e) {
            Assert.fail(String.format("Should have mapped to RestClientCTXListIndexCreate %s", e));
        }
    }

    @Test
    public void testRestClientCTXMapIndexToASCTX() {
        CTX expected = CTX.mapIndex(99);

        RestClientCTXMapIndex mapIndex = new RestClientCTXMapIndex();
        mapIndex.index = 99;

        ASTestUtils.compareCTX(expected, mapIndex.toCTX());
    }

    @Test
    public void tesMapToRestClientCTXMapRank() {
        Map<String, Object> ctxMap = new HashMap<>();
        ctxMap.put("ctxType", AerospikeAPIConstants.MAP_RANK);
        ctxMap.put("rank", 11);


        try {
            RestClientCTXMapRank restCTX = (RestClientCTXMapRank) mapper.bytesToObject(mapper.objectToBytes(ctxMap));
            Assert.assertEquals(restCTX.ctxType, AerospikeAPIConstants.MAP_RANK);
            Assert.assertEquals(restCTX.rank.intValue(), 11);
        } catch (Exception e) {
            Assert.fail(String.format("Should have mapped to RestClientCTXListIndexCreate %s", e));
        }
    }

    @Test
    public void testRestClientCTXMapRankToASCTX() {
        CTX expected = CTX.mapRank(99);

        RestClientCTXMapRank mapIndex = new RestClientCTXMapRank();
        mapIndex.rank = 99;

        ASTestUtils.compareCTX(expected, mapIndex.toCTX());
    }

    @Test
    public void tesMapToRestClientCTXMapKey() {
        Map<String, Object> ctxMap = new HashMap<>();
        ctxMap.put("ctxType", AerospikeAPIConstants.MAP_KEY);
        ctxMap.put("key", true);

        try {
            RestClientCTXMapKey restCTX = (RestClientCTXMapKey) mapper.bytesToObject(mapper.objectToBytes(ctxMap));
            Assert.assertEquals(restCTX.ctxType, AerospikeAPIConstants.MAP_KEY);
            Assert.assertEquals(restCTX.key, true);
        } catch (Exception e) {
            Assert.fail(String.format("Should have mapped to RestClientCTXListIndexCreate %s", e));
        }
    }

    @Test
    public void testRestClientCTXMapKeyToASCTX() {
        CTX expected = CTX.mapKey(Value.get(3.14159));

        RestClientCTXMapKey restCTX = new RestClientCTXMapKey();
        restCTX.key = 3.14159;

        ASTestUtils.compareCTX(expected, restCTX.toCTX());
    }

    @Test
    public void tesMapToRestClientCTXMapKeyCreate() {
        Map<String, Object> ctxMap = new HashMap<>();
        ctxMap.put("ctxType", AerospikeAPIConstants.MAP_KEY_CREATE);
        ctxMap.put("key", true);
        ctxMap.put("order", MapOrder.KEY_VALUE_ORDERED);

        try {
            RestClientCTXMapKeyCreate restCTX = (RestClientCTXMapKeyCreate) mapper.bytesToObject(mapper.objectToBytes(ctxMap));
            Assert.assertEquals(restCTX.ctxType, AerospikeAPIConstants.MAP_KEY_CREATE);
            Assert.assertEquals(restCTX.key, true);
            Assert.assertEquals(restCTX.order, MapOrder.KEY_VALUE_ORDERED);
        } catch (Exception e) {
            Assert.fail(String.format("Should have mapped to RestClientCTXListIndexCreate %s", e));
        }
    }

    @Test
    public void testRestClientCTXMapKeyCreateToASCTX() {
        CTX expected = CTX.mapKeyCreate(Value.get(3.14159), MapOrder.KEY_VALUE_ORDERED);

        RestClientCTXMapKeyCreate restCTX = new RestClientCTXMapKeyCreate();
        restCTX.key = 3.14159;
        restCTX.order = MapOrder.KEY_VALUE_ORDERED;

        ASTestUtils.compareCTX(expected, restCTX.toCTX());
    }

    @Test
    public void tesMapToRestClientCTXMapValue() {
        Map<String, Object> ctxMap = new HashMap<>();
        ctxMap.put("ctxType", AerospikeAPIConstants.MAP_VALUE);
        ctxMap.put("value", 10);

        try {
            RestClientCTXMapValue restCTX = (RestClientCTXMapValue) mapper.bytesToObject(mapper.objectToBytes(ctxMap));
            Assert.assertEquals(AerospikeAPIConstants.MAP_VALUE, restCTX.ctxType);
            Assert.assertEquals(10, restCTX.value);
        } catch (Exception e) {
            Assert.fail(String.format("Should have mapped to RestClientCTXListIndexCreate %s", e));
        }
    }

    @Test
    public void testRestClientCTXMapValueToASCTX() {
        CTX expected = CTX.mapValue(Value.get(3.14159));

        RestClientCTXMapValue restCTX = new RestClientCTXMapValue();
        restCTX.value = 3.14159;

        ASTestUtils.compareCTX(expected, restCTX.toCTX());
    }
    
}

class JsonCTXMapper extends ASTestMapper {

    public JsonCTXMapper() {
        super(JSONMessageConverter.getJSONObjectMapper(), RestClientCTX.class);
    }
}

class MsgPackCTXMapper extends ASTestMapper {

    public MsgPackCTXMapper() {
        super(MsgPackConverter.getASMsgPackObjectMapper(), RestClientCTX.class);
    }
}
