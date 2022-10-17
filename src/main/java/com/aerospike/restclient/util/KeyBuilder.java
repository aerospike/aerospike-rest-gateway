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
package com.aerospike.restclient.util;

import com.aerospike.client.Key;
import com.aerospike.restclient.util.AerospikeAPIConstants.RecordKeyType;

import java.util.Base64;

public class KeyBuilder {

    public static Key buildKey(String namespace, String set, String strKey, RecordKeyType keyType) {
        if (keyType == null) {
            return new Key(namespace, set, strKey);
        }
        switch (keyType) {
            case STRING:
                return new Key(namespace, set, strKey);
            case INTEGER:
                return new Key(namespace, set, getLongFromstring(strKey));
            case BYTES: {
                return new Key(namespace, set, getByteArrayFromstring(strKey));
            }
            case DIGEST: {
                byte[] digestBytes = getByteArrayFromstring(strKey);
                if (digestBytes.length != 20) {
                    throw new RestClientErrors.InvalidKeyError(
                            String.format("Digest must be 20 bytes long: %s", strKey));
                }
                return new Key(namespace, digestBytes, set, null);
            }
            default:
                throw new RestClientErrors.InvalidKeyError(String.format("Invalid keyType value: %s", keyType));
        }
    }

    private static int getIntegerFromstring(String strKey) {
        try {
            return Integer.parseInt(strKey, 10);
        } catch (NumberFormatException nfe) {
            throw new RestClientErrors.InvalidKeyError(String.format("Invalid integer value: %s", strKey));
        }
    }

    private static long getLongFromstring(String strKey) {
        try {
            return Long.parseLong(strKey, 10);
        } catch (NumberFormatException nfe) {
            throw new RestClientErrors.InvalidKeyError(String.format("Invalid integer value: %s", strKey));
        }
    }

    private static byte[] getByteArrayFromstring(String strKey) {
        try {
            return Base64.getUrlDecoder().decode(strKey);
        } catch (IllegalArgumentException nfe) {
            throw new RestClientErrors.InvalidKeyError(String.format("Unable to decode bytes: %s", strKey));
        }
    }

}