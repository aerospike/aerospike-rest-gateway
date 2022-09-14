package com.aerospike.restclient.domain.querymodels;

import com.aerospike.client.Key;
import com.aerospike.client.Record;
import com.aerospike.restclient.ASTestMapper;
import com.aerospike.restclient.config.JSONMessageConverter;
import com.aerospike.restclient.config.MsgPackConverter;
import com.aerospike.restclient.domain.RestClientKeyRecord;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

@RunWith(Parameterized.class)
public class QueryResponseBodyTest {

    ASTestMapper mapper;

    @Parameterized.Parameters
    public static Object[] getParams() {
        return new Object[]{
                new JsonQueryResponseMapper(),
                new MsgPackQueryResponseMapper(),
                };
    }

    public QueryResponseBodyTest(ASTestMapper mapper) {
        this.mapper = mapper;
    }

    @Test
    public void testRestClientQueryResponseAddRecords() {
        QueryResponseBody restQuery = new QueryResponseBody(10);

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

class JsonQueryResponseMapper extends ASTestMapper {

    public JsonQueryResponseMapper() {
        super(JSONMessageConverter.getJSONObjectMapper(), QueryResponseBody.class);
    }
}

class MsgPackQueryResponseMapper extends ASTestMapper {

    public MsgPackQueryResponseMapper() {
        super(MsgPackConverter.getASMsgPackObjectMapper(), QueryResponseBody.class);
    }
}
