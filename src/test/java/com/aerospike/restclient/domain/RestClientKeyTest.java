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
package com.aerospike.restclient.domain;

import com.aerospike.client.Key;
import com.aerospike.restclient.ASTestUtils;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.aerospike.restclient.util.AerospikeAPIConstants.RecordKeyType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.HashMap;
import java.util.Map;

public class RestClientKeyTest {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String ns = "test";
    private static final String set = "set";
    private static final Decoder decoder = Base64.getUrlDecoder();
    private static final Encoder encoder = Base64.getUrlEncoder();
    private static final TypeReference<RestClientKey> rcKeyType = new TypeReference<RestClientKey>() {
    };

    @Test
    public void testNoArgConstructor() {
        new RestClientKey();
    }

    @Test
    public void testCreationFromStringKey() {
        Key testKey = new Key(ns, set, "key");
        RestClientKey constructedKey = new RestClientKey(testKey);

        Assert.assertEquals(testKey.namespace, constructedKey.namespace);
        Assert.assertEquals(testKey.setName, constructedKey.setName);
        Assert.assertEquals("key", constructedKey.userKey);
        byte[] constructedDigest = constructedKey.toKey().digest;
        Assert.assertEquals(constructedKey.keyType, RecordKeyType.STRING);
        Assert.assertArrayEquals(constructedDigest, testKey.digest);
    }

    @Test
    public void testCreationFromIntegerKey() {
        Key testKey = new Key(ns, set, 5L);
        RestClientKey constructedKey = new RestClientKey(testKey);

        Assert.assertEquals(testKey.namespace, constructedKey.namespace);
        Assert.assertEquals(testKey.setName, constructedKey.setName);
        Assert.assertEquals(5L, constructedKey.userKey);
        Assert.assertEquals(constructedKey.keyType, RecordKeyType.INTEGER);

        byte[] constructedDigest = constructedKey.toKey().digest;
        Assert.assertArrayEquals(constructedDigest, testKey.digest);
    }

    @Test
    public void testCreationFromBytesKey() {
        byte[] testBytes = {1, 2, 3, 4};
        Key testKey = new Key(ns, set, testBytes);
        RestClientKey constructedKey = new RestClientKey(testKey);

        Assert.assertEquals(testKey.namespace, constructedKey.namespace);
        Assert.assertEquals(testKey.setName, constructedKey.setName);
        Assert.assertEquals(constructedKey.keyType, RecordKeyType.BYTES);

        Assert.assertArrayEquals(testBytes, decoder.decode((String) constructedKey.userKey));
        byte[] constructedDigest = constructedKey.toKey().digest;
        Assert.assertArrayEquals(constructedDigest, testKey.digest);
    }

    @Test
    public void testCreationWithDigest() {
        byte[] testBytes = new byte[20];
        Key testKey = new Key(ns, testBytes, null, null);
        RestClientKey constructedKey = new RestClientKey(testKey);

        Assert.assertEquals(testKey.namespace, constructedKey.namespace);

        byte[] constructedDigest = constructedKey.toKey().digest;
        Assert.assertArrayEquals(constructedDigest, testKey.digest);
    }

    @Test
    public void testConversionToStringKey() {
        Key expectedKey = new Key(ns, set, "test");
        RestClientKey rcKey = new RestClientKey();
        rcKey.keyType = RecordKeyType.STRING;
        rcKey.setName = set;
        rcKey.namespace = ns;
        rcKey.userKey = "test";

        Assert.assertTrue(ASTestUtils.compareKeys(expectedKey, rcKey.toKey()));
    }

    @Test
    public void testConversionToLongKey() {
        Key expectedKey = new Key(ns, set, 5L);
        RestClientKey rcKey = new RestClientKey();
        rcKey.keyType = RecordKeyType.INTEGER;
        rcKey.setName = set;
        rcKey.namespace = ns;
        rcKey.userKey = 5;

        Assert.assertTrue(ASTestUtils.compareKeys(expectedKey, rcKey.toKey()));
    }

    @Test
    public void testConversionToBytesKey() {
        byte[] keyBytes = {1, 2, 3, 4};
        Key expectedKey = new Key(ns, set, keyBytes);
        RestClientKey rcKey = new RestClientKey();
        rcKey.keyType = RecordKeyType.BYTES;
        rcKey.setName = set;
        rcKey.namespace = ns;
        rcKey.userKey = encoder.encodeToString(keyBytes);

        Assert.assertTrue(ASTestUtils.compareKeys(expectedKey, rcKey.toKey()));
    }

    @Test
    public void testConversionToDigestKey() {
        byte[] testBytes = new byte[20];
        Key expectedKey = new Key(ns, testBytes, null, null);
        RestClientKey rcKey = new RestClientKey();
        rcKey.keyType = RecordKeyType.DIGEST;
        rcKey.namespace = ns;
        rcKey.userKey = encoder.encodeToString(testBytes);

        Assert.assertTrue(ASTestUtils.compareKeys(expectedKey, rcKey.toKey()));
    }

    @Test
    public void testConversionToDigest() {
        byte[] testBytes = new byte[20];
        Key testKey = new Key(ns, testBytes, null, null);
        RestClientKey constructedKey = new RestClientKey(testKey);

        Assert.assertEquals(testKey.namespace, constructedKey.namespace);

        byte[] constructedDigest = constructedKey.toKey().digest;
        Assert.assertArrayEquals(constructedDigest, testKey.digest);
    }

    @Test
    public void testTwoWayConversionStringKey() {
        Key expectedKey = new Key(ns, set, "test");
        RestClientKey rcKey = new RestClientKey(expectedKey);

        Assert.assertTrue(ASTestUtils.compareKeys(expectedKey, rcKey.toKey()));
    }

    @Test
    public void testConversionTwoWayLongKey() {
        Key expectedKey = new Key(ns, set, 5L);
        RestClientKey rcKey = new RestClientKey(expectedKey);

        Assert.assertTrue(ASTestUtils.compareKeys(expectedKey, rcKey.toKey()));
    }

    @Test
    public void testConversionTwoWayToBytesKey() {
        byte[] keyBytes = {1, 2, 3, 4};
        Key expectedKey = new Key(ns, set, keyBytes);
        RestClientKey rcKey = new RestClientKey(expectedKey);

        Assert.assertTrue(ASTestUtils.compareKeys(expectedKey, rcKey.toKey()));
    }

    @Test
    public void testConversionTwoWayToDigestKey() {
        byte[] testBytes = new byte[20];
        Key expectedKey = new Key(ns, testBytes, null, null);
        RestClientKey rcKey = new RestClientKey(expectedKey);

        Assert.assertTrue(ASTestUtils.compareKeys(expectedKey, rcKey.toKey()));
    }

    @Test
    public void testObjectMappedStringKey() throws Exception {
        String jsonKey = getJsonRCKey(ns, set, "key", RecordKeyType.STRING);
        RestClientKey rcKey = mapper.readValue(jsonKey, rcKeyType);

        Assert.assertEquals(rcKey.namespace, ns);
        Assert.assertEquals(rcKey.setName, set);
        Assert.assertEquals(rcKey.userKey, "key");
        Assert.assertEquals(rcKey.keyType, RecordKeyType.STRING);
    }

    @Test
    public void testObjectMappedIntegerKey() throws Exception {
        String jsonKey = getJsonRCKey(ns, set, 5L, RecordKeyType.INTEGER);
        RestClientKey rcKey = mapper.readValue(jsonKey, rcKeyType);

        Assert.assertEquals(rcKey.namespace, ns);
        Assert.assertEquals(rcKey.setName, set);
        Assert.assertEquals(rcKey.userKey, 5);
        Assert.assertEquals(rcKey.keyType, RecordKeyType.INTEGER);
    }

    @Test
    public void testObjectMappedDigestKey() throws Exception {
        byte[] testBytes = new byte[20];
        String strBytes = encoder.encodeToString(testBytes);
        String jsonKey = getJsonRCKey(ns, set, strBytes, RecordKeyType.DIGEST);
        RestClientKey rcKey = mapper.readValue(jsonKey, rcKeyType);

        Assert.assertEquals(rcKey.namespace, ns);
        Assert.assertEquals(rcKey.setName, set);
        Assert.assertEquals(rcKey.userKey, strBytes);
        Assert.assertEquals(rcKey.keyType, RecordKeyType.DIGEST);
    }

    @Test
    public void testObjectMappedBYTESKey() throws Exception {
        byte[] keyBytes = {1, 2, 3, 4};
        String strBytes = encoder.encodeToString(keyBytes);

        String jsonKey = getJsonRCKey(ns, set, strBytes, RecordKeyType.BYTES);
        RestClientKey rcKey = mapper.readValue(jsonKey, rcKeyType);

        Assert.assertEquals(rcKey.namespace, ns);
        Assert.assertEquals(rcKey.setName, set);
        Assert.assertEquals(rcKey.userKey, strBytes);
        Assert.assertEquals(rcKey.keyType, RecordKeyType.BYTES);
    }

    private String getJsonRCKey(String namespace, String set, Object userKey,
                                RecordKeyType keyType) throws JsonProcessingException {
        Map<String, Object> keyMap = new HashMap<>();
        keyMap.put(AerospikeAPIConstants.NAMESPACE, namespace);
        if (set != null) {
            keyMap.put(AerospikeAPIConstants.SETNAME, set);
        }
        if (userKey != null) {
            keyMap.put(AerospikeAPIConstants.USER_KEY, userKey);
        }
        if (keyType != null) {
            keyMap.put(AerospikeAPIConstants.KEY_TYPE, keyType.toString());
        }
        return mapper.writeValueAsString(keyMap);
    }

}
