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
package com.aerospike.restclient;

import com.aerospike.client.Key;
import com.aerospike.client.Value.BytesValue;
import com.aerospike.restclient.util.AerospikeAPIConstants.RecordKeyType;
import com.aerospike.restclient.util.KeyBuilder;
import com.aerospike.restclient.util.RestClientErrors;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Base64;

public class KeyBuilderTest {
    private final String testNS = "keybuilderNS";
    private final String testSet = "keybuilderSet";
    private final String testStrKey = "keybuilderStr";

    @Test
    public void testWithNoKeyType() {
        Key testKey = KeyBuilder.buildKey(testNS, testSet, testStrKey, null);
        Key realKey = new Key(testNS, testSet, testStrKey);

        Assert.assertTrue(keysEqual(testKey, realKey));
    }

    @Test
    public void testWithStringKeyType() {
        Key testKey = KeyBuilder.buildKey(testNS, testSet, testStrKey, RecordKeyType.STRING);
        Key realKey = new Key(testNS, testSet, testStrKey);

        Assert.assertTrue(keysEqual(testKey, realKey));
    }

    @Test
    public void testWithIntgerKeyType() {
        String longString = "5";
        Key testKey = KeyBuilder.buildKey(testNS, testSet, longString, RecordKeyType.INTEGER);
        Key realKey = new Key(testNS, testSet, 5);

        Assert.assertTrue(keysEqual(testKey, realKey));
    }

    @Test
    public void testWithBytesKeyType() {
        byte[] byteKey = {1, 2, 3, 4};
        String encodedBytes = Base64.getUrlEncoder().encodeToString(byteKey);

        Key testKey = KeyBuilder.buildKey(testNS, testSet, encodedBytes, RecordKeyType.BYTES);
        Key realKey = new Key(testNS, testSet, byteKey);

        Assert.assertTrue(keysEqual(testKey, realKey));
    }

    @Test
    public void testWithDigestKeyType() {
        // We need a valid digest, so build a key to get it.
        byte[] keyDigest = (new Key(testNS, testSet, testStrKey)).digest;

        // Build a key with just the digest.
        Key keyFromDigest = new Key(testNS, keyDigest, testSet, null);

        String encodedBytes = Base64.getUrlEncoder().encodeToString(keyDigest);

        Key testKey = KeyBuilder.buildKey(testNS, testSet, encodedBytes, RecordKeyType.DIGEST);
        Assert.assertTrue(keysEqual(testKey, keyFromDigest));
    }

    @Test
    public void testWithNoSet() {
        Key testKey = KeyBuilder.buildKey(testNS, null, testStrKey, null);
        Key realKey = new Key(testNS, null, testStrKey);

        Assert.assertTrue(keysEqual(testKey, realKey));
    }

    @Test(expected = RestClientErrors.InvalidKeyError.class)
    public void testWithInvalidIntegerKey() {
        KeyBuilder.buildKey(testNS, testSet, "five", RecordKeyType.INTEGER);
    }

    /*
     * Helper to compare two AerospikeClient keys
     */
    private boolean keysEqual(Key testKey, Key realKey) {
        if (!testKey.namespace.equals(realKey.namespace)) {
            return false;
        }
        if (testKey.setName != null) {
            if (!testKey.setName.equals(realKey.setName)) {
                return false;
            }
        } else if (realKey.setName != null) {
            return false;
        }

        if (testKey.userKey != null) {
            /* Need a different comparison for byte[] */
            if (testKey.userKey instanceof BytesValue) {
                if (!Arrays.equals((byte[]) testKey.userKey.getObject(), (byte[]) realKey.userKey.getObject())) {
                    return false;
                }
            } else if (!testKey.userKey.equals(realKey.userKey)) {
                return false;
            }
        } else if (realKey.userKey != null) {
            return false;
        }

        return Arrays.equals(testKey.digest, realKey.digest);
    }
}
