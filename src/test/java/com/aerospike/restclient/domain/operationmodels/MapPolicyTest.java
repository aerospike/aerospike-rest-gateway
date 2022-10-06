package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.cdt.MapOrder;
import com.aerospike.client.cdt.MapWriteFlags;
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
public class MapPolicyTest {
    ASTestMapper mapper;

    @Parameterized.Parameters
    public static Object[] getParams() {
        return new Object[]{
                new JsonMapPolicyMapper(), new MsgPackMapPolicyMapper(),
                };
    }

    public MapPolicyTest(ASTestMapper mapper) {
        this.mapper = mapper;
    }

    @Test
    public void testEmptyMapPolicy() throws Exception {
        Map<String, Object> policyMap = new HashMap<>();

        MapPolicy MapPolicy = (MapPolicy) mapper.bytesToObject(mapper.objectToBytes(policyMap));

        Assert.assertNull(MapPolicy.getOrder());
        Assert.assertNull(MapPolicy.getWriteFlags());

        com.aerospike.client.cdt.MapPolicy asMapPolicy = MapPolicy.toMapPolicy();
        Assert.assertEquals(asMapPolicy.attributes, com.aerospike.client.cdt.MapPolicy.Default.attributes);
        Assert.assertEquals(asMapPolicy.flags, com.aerospike.client.cdt.MapPolicy.Default.flags);
    }

    @Test
    public void toMapPolicyWithOrderAndWriteFlags() throws Exception {
        Map<String, Object> policyMap = new HashMap<>();
        policyMap.put("order", "UNORDERED");
        policyMap.put("writeFlags", new String[]{"NO_FAIL", "CREATE_ONLY"});
        List<MapWriteFlag> expectedWriteFlags = new ArrayList<>();
        expectedWriteFlags.add(MapWriteFlag.NO_FAIL);
        expectedWriteFlags.add(MapWriteFlag.CREATE_ONLY);

        MapPolicy MapPolicy = (MapPolicy) mapper.bytesToObject(mapper.objectToBytes(policyMap));

        Assert.assertEquals(MapPolicy.getOrder(), MapOrder.UNORDERED);
        ASTestUtils.compareCollection(expectedWriteFlags, MapPolicy.getWriteFlags());

        com.aerospike.client.cdt.MapPolicy asMapPolicy = MapPolicy.toMapPolicy();
        Assert.assertEquals(asMapPolicy.attributes, MapOrder.UNORDERED.attributes);
        Assert.assertEquals(asMapPolicy.flags, MapWriteFlags.NO_FAIL | MapWriteFlags.CREATE_ONLY);
    }

    @Test
    public void toMapPolicyWithNoWriteFlags() throws Exception {
        Map<String, Object> policyMap = new HashMap<>();
        policyMap.put("order", "UNORDERED");

        MapPolicy MapPolicy = (MapPolicy) mapper.bytesToObject(mapper.objectToBytes(policyMap));

        Assert.assertEquals(MapPolicy.getOrder(), MapOrder.UNORDERED);

        com.aerospike.client.cdt.MapPolicy asMapPolicy = MapPolicy.toMapPolicy();
        Assert.assertEquals(asMapPolicy.attributes, MapOrder.UNORDERED.attributes);
        Assert.assertEquals(asMapPolicy.flags, 0);
    }
}

class JsonMapPolicyMapper extends ASTestMapper {

    public JsonMapPolicyMapper() {
        super(JSONMessageConverter.getJSONObjectMapper(), MapPolicy.class);
    }
}

class MsgPackMapPolicyMapper extends ASTestMapper {

    public MsgPackMapPolicyMapper() {
        super(MsgPackConverter.getASMsgPackObjectMapper(), MapPolicy.class);
    }
}