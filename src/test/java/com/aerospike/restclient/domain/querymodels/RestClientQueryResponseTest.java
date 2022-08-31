package com.aerospike.restclient.domain.querymodels;

import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.restclient.ASTestMapper;
import com.aerospike.restclient.ASTestUtils;
import com.aerospike.restclient.IASTestMapper;
import com.aerospike.restclient.config.JSONMessageConverter;
import com.aerospike.restclient.config.MsgPackConverter;
import com.aerospike.restclient.domain.RestClientKeyRecord;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(Parameterized.class)
public class RestClientQueryResponseTest {

    private final IASTestMapper mapper;
    private final static String ns = "test";
    private final static String set = "ctx";

    @Parameterized.Parameters
    public static Object[] getParams() {
        return new Object[]{
                new JsonQueryBodyMapper(),
                new MsgPackQueryBodyMapper(),
        };
    }

    public RestClientQueryResponseTest(IASTestMapper mapper) {
        this.mapper = mapper;
    }

    @Test
    public void testRestClientQueryResponseAddRecords() {
        RestClientQueryResponse restQuery = new RestClientQueryResponse(10);

        for (int i = 0; i < 10; i++) {
            restQuery.addRecord(new RestClientKeyRecord(new Key("ns", "set", "key" + i), new Record(null, 0, 0)));
        }

        Assert.assertEquals(10, restQuery.size());

        List<RestClientKeyRecord> actualRecs = restQuery.getRecords();

        for (int i = 0; i < 10; i++) {
            Assert.assertEquals("key" + i, actualRecs.get(i).userKey);
        }
    }
}

class JsonQueryBodyMapper extends ASTestMapper {

    public JsonQueryBodyMapper() {
        super(JSONMessageConverter.getJSONObjectMapper(), RestClientQueryBody.class);
    }
}

class MsgPackQueryBodyMapper extends ASTestMapper {

    public MsgPackQueryBodyMapper() {
        super(MsgPackConverter.getASMsgPackObjectMapper(), RestClientQueryBody.class);
    }
}
