package com.aerospike.restclient;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.client.Value;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(Parameterized.class)
@SpringBootTest
public class ExpressionParserTest {

    /* Needed to run as a Spring Boot test */
    @ClassRule
    public static final SpringClassRule springClassRule = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @Autowired
    private AerospikeClient client;

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMVC;
    private final RecordDeserializer recordDeserializer;
    private final String currentMediaType;
    private final String expParameter;

    /*
     * Returns a two item array, of mediatype and record deserializer
     */
    @Parameterized.Parameters
    public static Object[] mappers() {
        return new Object[][]{
                {new JSONRestRecordDeserializer(), MediaType.APPLICATION_JSON.toString(), true, "predexp"},
                {new MsgPackRestRecordDeserializer(), "application/msgpack", true, "predexp"},
                {new JSONRestRecordDeserializer(), MediaType.APPLICATION_JSON.toString(), false, "predexp"},
                {new MsgPackRestRecordDeserializer(), "application/msgpack", false, "predexp"},

                {new JSONRestRecordDeserializer(), MediaType.APPLICATION_JSON.toString(), true, "filterexp"},
                {new MsgPackRestRecordDeserializer(), "application/msgpack", true, "filterexp"},
                {new JSONRestRecordDeserializer(), MediaType.APPLICATION_JSON.toString(), false, "filterexp"},
                {new MsgPackRestRecordDeserializer(), "application/msgpack", false, "filterexp"},
        };
    }

    private final Key testKey;
    private final Key intKey;
    private final Key bytesKey;

    private List<Key> keysToRemove;

    // Endpoint to receive all requests
    private final String noBinEndpoint;
    private final String intEndpoint;
    private final String bytesEndpoint;
    private final String digestEndpoint;

    @Before
    public void setup() {
        mockMVC = MockMvcBuilders.webAppContextSetup(wac).build();
        keysToRemove = new ArrayList<>();
    }

    @After
    public void clean() {
        client.delete(null, this.testKey);
        for (Key key : keysToRemove) {
            client.delete(null, key);
        }
    }

    public ExpressionParserTest(RecordDeserializer deserializer, String mt, boolean useSet, String expParameter) {
        this.recordDeserializer = deserializer;
        this.currentMediaType = mt;
        byte[] keyBytes = {1, 127, 127, 1};
        this.expParameter = expParameter;

        if (useSet) {
            testKey = new Key("test", "junit", "getput");
            intKey = new Key("test", "junit", 1);

            bytesKey = new Key("test", "junit", new Value.BytesValue(keyBytes));

            noBinEndpoint = ASTestUtils.buildEndpoint("kvs", "test", "junit", "getput");

            intEndpoint = ASTestUtils.buildEndpoint("kvs", "test", "junit", "1") + "?keytype=INTEGER";

            String keyDigest = Base64.getUrlEncoder().encodeToString(this.testKey.digest);
            digestEndpoint = ASTestUtils.buildEndpoint("kvs",
                    this.testKey.namespace, this.testKey.setName, keyDigest) + "?keytype=DIGEST";

            String b64byteStr = Base64.getUrlEncoder().encodeToString(keyBytes);
            bytesEndpoint = ASTestUtils.buildEndpoint("kvs", "test", "junit", b64byteStr) + "?keytype=BYTES";
        } else {
            testKey = new Key("test", null, "getput");
            intKey = new Key("test", null, 1);

            bytesKey = new Key("test", null, new Value.BytesValue(keyBytes));

            noBinEndpoint = ASTestUtils.buildEndpoint("kvs", "test", "getput");

            intEndpoint = ASTestUtils.buildEndpoint("kvs", "test", "1") + "?keytype=INTEGER";

            String keyDigest = Base64.getUrlEncoder().encodeToString(this.testKey.digest);
            digestEndpoint = ASTestUtils.buildEndpoint("kvs",
                    this.testKey.namespace, keyDigest) + "?keytype=DIGEST";

            String b64byteStr = Base64.getUrlEncoder().encodeToString(keyBytes);
            bytesEndpoint = ASTestUtils.buildEndpoint("kvs", "test", b64byteStr) + "?keytype=BYTES";
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
            Assert.assertTrue(ASTestUtils.compareMapStringObj(resObject, binMap));
        }
        keysToRemove.add(testKey);
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
            Assert.assertTrue(ASTestUtils.compareMapStringObj(resObject, binMap));
        }
        keysToRemove.add(testKey);
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
            Assert.assertTrue(ASTestUtils.compareMapStringObj(resObject, binMap));
        }
        keysToRemove.add(testKey);
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
            Assert.assertTrue(ASTestUtils.compareMapStringObj(resObject, binMap));
        }
        keysToRemove.add(testKey);
    }

    private String buildEndpoint(String encoded) {
        return noBinEndpoint + "?" + expParameter + "=" + encoded;
    }
}
