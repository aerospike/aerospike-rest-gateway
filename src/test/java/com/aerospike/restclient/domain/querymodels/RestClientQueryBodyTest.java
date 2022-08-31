package com.aerospike.restclient.domain.querymodels;

import com.aerospike.restclient.ASTestMapper;
import com.aerospike.restclient.ASTestUtils;
import com.aerospike.restclient.IASTestMapper;
import com.aerospike.restclient.config.JSONMessageConverter;
import com.aerospike.restclient.config.MsgPackConverter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.HashMap;
import java.util.Map;

@RunWith(Parameterized.class)
public class RestClientQueryBodyTest {

    private final IASTestMapper mapper;
    private final static String ns = "test";
    private final static String set = "ctx";

    @Parameterized.Parameters
    public static Object[] getParams() {
        return new Object[]{
                new JsonQueryResponseMapper(),
                new MsgPackQueryResponseMapper(),
        };
    }

    public RestClientQueryBodyTest(IASTestMapper mapper) {
        this.mapper = mapper;
    }

    @Test
    public void testEmptyMapToRestClientQueryBody() {
        Map<String, Object> ctxMap = new HashMap<>();

        try {
            RestClientQueryBody body = (RestClientQueryBody) mapper.bytesToObject(mapper.objectToBytes(ctxMap));
        } catch (Exception e) {
            Assert.fail("Should have mapped to RestClientQueryBody");
            // Success
        }
    }

    @Test
    public void testMapsToRestClientQueryBody() {
        Map<String, Object> restMap = new HashMap<>();
        restMap.put("from", "from-pagination-token");
        RestClientQueryBinEqualFilter filter = new RestClientQueryBinEqualFilter();
        filter.value = 1;
        restMap.put("filter", filter);

        try {
            RestClientQueryBody restQuery = (RestClientQueryBody) mapper.bytesToObject(mapper.objectToBytes(restMap));
            Assert.assertEquals(restQuery.from, "from-pagination-token");
            ASTestUtils.compareFilter(filter.toFilter(), restQuery.filter.toFilter());
        } catch (Exception e) {
            Assert.fail(String.format("Should have mapped to RestClientQueryBody %s", e));
        }
    }
}

class JsonQueryResponseMapper extends ASTestMapper {

    public JsonQueryResponseMapper() {
        super(JSONMessageConverter.getJSONObjectMapper(), RestClientQueryBody.class);
    }
}

class MsgPackQueryResponseMapper extends ASTestMapper {

    public MsgPackQueryResponseMapper() {
        super(MsgPackConverter.getASMsgPackObjectMapper(), RestClientQueryBody.class);
    }
}
