package com.aerospike.restclient;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExpressionParserTest {

    @Autowired
    private AerospikeClient client;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMVC;
    private final RecordDeserializer recordDeserializer;
    private final String currentMediaType;
    private final String expParameter;

    private static Stream<Arguments> mappers() {
        return Stream.of(
                Arguments.of(new JSONRestRecordDeserializer(), MediaType.APPLICATION_JSON.toString(), true, "predexp"),
                Arguments.of(new MsgPackRestRecordDeserializer(), "application/msgpack", true, "predexp"),
                Arguments.of(new JSONRestRecordDeserializer(), MediaType.APPLICATION_JSON.toString(), false, "predexp"),
                Arguments.of(new MsgPackRestRecordDeserializer(), "application/msgpack", false, "predexp"),

                Arguments.of(new JSONRestRecordDeserializer(), MediaType.APPLICATION_JSON.toString(), true, "filterexp"),
                Arguments.of(new MsgPackRestRecordDeserializer(), "application/msgpack", true, "filterexp"),
                Arguments.of(new JSONRestRecordDeserializer(), MediaType.APPLICATION_JSON.toString(), false, "filterexp"),
                Arguments.of(new MsgPackRestRecordDeserializer(), "application/msgpack", false, "filterexp")
        );
    }

    private final Key testKey;
    private final String noBinEndpoint;

    @BeforeEach
    public void setup() {
        mockMVC = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @AfterEach
    public void clean() {
        client.delete(null, this.testKey);
    }

    public ExpressionParserTest(RecordDeserializer deserializer, String mt, boolean useSet, String expParameter) {
        this.recordDeserializer = deserializer;
        this.currentMediaType = mt;
        this.expParameter = expParameter;

        if (useSet) {
            testKey = new Key("test", "junit", "getput");
            noBinEndpoint = ASTestUtils.buildEndpoint("kvs", "test", "junit", "getput");
        } else {
            testKey = new Key("test", null, "getput");
            noBinEndpoint = ASTestUtils.buildEndpoint("kvs", "test", "getput");
        }
    }

    @Test
    public void GetInteger() throws Exception {
        Map<String, Object> binMap = new HashMap<>();

        Bin intBin = new Bin("integer", 10);
        binMap.put(intBin.name, intBin.value.toInteger());

        client.put(null, testKey, intBin);
        List<String> exps = Arrays.asList(
                "integer > 1",
                "not(integer < 1) and LAST_UPDATE(>=, 1577880000)",
                "DIGEST_MODULO(3, ==, 1) or not VOID_TIME(!=, 1577880000)" //VOID_TIME is 0
        );
        for (String predexp : exps) {
            String encoded = Base64.getUrlEncoder().encodeToString(predexp.getBytes());
            String endpoint = buildEndpoint(encoded);
            MockHttpServletResponse res = mockMVC.perform(
                    get(endpoint).contentType(MediaType.APPLICATION_JSON).accept(currentMediaType)
            ).andExpect(status().isOk()).andReturn().getResponse();
            Map<String, Object> resObject = recordDeserializer.getReturnedBins(res);
            assertTrue(ASTestUtils.compareMapStringObj(resObject, binMap));
        }
    }

    @Test
    public void GetString() throws Exception {
        Map<String, Object> binMap = new HashMap<>();
        Bin intBin = new Bin("string", "aerospike");

        binMap.put("string", "aerospike");

        client.put(null, testKey, intBin);
        List<String> exps = Arrays.asList(
                "string == aerospike",
                "not(string == hello) and STRING_REGEX(string, \"[\\s]*\")",
                "(string == aerospike) or int == 100"
        );
        for (String predexp : exps) {
            String encoded = Base64.getUrlEncoder().encodeToString(predexp.getBytes());
            String endpoint = buildEndpoint(encoded);
            MockHttpServletResponse res = mockMVC.perform(
                    get(endpoint).contentType(MediaType.APPLICATION_JSON).accept(currentMediaType)
            ).andExpect(status().isOk()).andReturn().getResponse();
            Map<String, Object> resObject = recordDeserializer.getReturnedBins(res);
            assertTrue(ASTestUtils.compareMapStringObj(resObject, binMap));
        }
    }

    @Test
    public void GetList() throws Exception {
        Map<String, Object> binMap = new HashMap<>();
        List<String> putList = Arrays.asList("a", "e", "r", "o");
        Bin intBin = new Bin("list", putList);

        binMap.put("list", putList);

        client.put(null, testKey, intBin);
        List<String> exps = Arrays.asList(
                "LIST_ITERATE_OR(list, ==, r)",
                "LIST_ITERATE_AND(list, !=, b)",
                "(LIST_ITERATE_OR(list, ==, r) and LIST_ITERATE_AND(list, !=, b)) or str == hello"
        );
        for (String predexp : exps) {
            String encoded = Base64.getUrlEncoder().encodeToString(predexp.getBytes());
            String endpoint = buildEndpoint(encoded);
            MockHttpServletResponse res = mockMVC.perform(
                    get(endpoint).contentType(MediaType.APPLICATION_JSON).accept(currentMediaType)
            ).andExpect(status().isOk()).andReturn().getResponse();
            Map<String, Object> resObject = recordDeserializer.getReturnedBins(res);
            assertTrue(ASTestUtils.compareMapStringObj(resObject, binMap));
        }
    }

    @Test
    public void GetMap() throws Exception {
        Map<String, Object> binMap = new HashMap<>();
        Map<Object, Object> putMap = new HashMap<>();
        putMap.put("aero", "spike");
        putMap.put("int", 5);

        Bin intBin = new Bin("map", putMap);

        binMap.put("map", putMap);

        client.put(null, testKey, intBin);
        List<String> exps = Arrays.asList(
                "MAPKEY_ITERATE_OR(map, ==, aero)",
                "MAPVAL_ITERATE_OR(map, ==, 5)",
                "MAPKEY_ITERATE_AND(map, !=, z) or MAPVAL_ITERATE_AND(map, !=, 500)"
        );
        for (String predexp : exps) {
            String encoded = Base64.getUrlEncoder().encodeToString(predexp.getBytes());
            String endpoint = buildEndpoint(encoded);
            MockHttpServletResponse res = mockMVC.perform(
                    get(endpoint).contentType(MediaType.APPLICATION_JSON).accept(currentMediaType)
            ).andExpect(status().isOk()).andReturn().getResponse();
            Map<String, Object> resObject = recordDeserializer.getReturnedBins(res);
            assertTrue(ASTestUtils.compareMapStringObj(resObject, binMap));
        }
    }

    private String buildEndpoint(String encoded) {
        return noBinEndpoint + "?" + expParameter + "=" + encoded;
    }
}
