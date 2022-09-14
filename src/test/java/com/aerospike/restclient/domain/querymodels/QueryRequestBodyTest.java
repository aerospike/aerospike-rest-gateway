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

@RunWith(Parameterized.class)
public class QueryRequestBodyTest {

    private final ASTestMapper mapper;
    private final static String ns = "test";
    private final static String set = "ctx";

    @Parameterized.Parameters
    public static Object[] getParams() {
        return new Object[]{
                new JsonQueryBodyMapper(),
                new MsgPackQueryBodyMapper(),
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
        QueryEqualLongFilter filter = new QueryEqualLongFilter();
        filter.value = 1L;
        restMap.put("filter", filter);

        try {
            QueryRequestBody restQuery = (QueryRequestBody) mapper.bytesToObject(mapper.objectToBytes(restMap));
            Assert.assertEquals(restQuery.from, "from-pagination-token");
            ASTestUtils.compareFilter(filter.toFilter(), restQuery.filter.toFilter());
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
