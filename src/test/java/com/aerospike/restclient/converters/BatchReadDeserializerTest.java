/*
 * Copyright 2018 Aerospike, Inc.
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
package com.aerospike.restclient.converters;

import com.aerospike.restclient.domain.batchmodels.BatchRead;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

interface BatchReadBodyMapperTester {
    BatchRead getBatchRead(String ns, String set, String key, List<String> bins,
                           String keytype) throws Exception;

    BatchRead getBatchRead(String ns, byte[] digest) throws Exception;
}

class JSONBatchReadTester implements BatchReadBodyMapperTester {
    ObjectMapper mapper = null;

    public JSONBatchReadTester(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public BatchRead getBatchRead(String ns, String set, String key, List<String> bins,
                                  String keytype) throws Exception {
        Map<String, Object> batchReadBody = new HashMap<>();
        Map<String, String> keyMap = new HashMap<>();

        if (bins != null) {
            batchReadBody.put("binNames", bins);
        }
        if (ns != null) {
            keyMap.put("namespace", ns);
        }
        if (set != null) {
            keyMap.put("setName", set);
        }

        if (key != null) {
            keyMap.put("userKey", key);
        }

        if (keytype != null) {
            keyMap.put("keytype", keytype);
        }

        batchReadBody.put("key", keyMap);


        String batchReadStr = mapper.writeValueAsString(batchReadBody);
        return mapper.readValue(batchReadStr, BatchRead.class);
    }

    @Override
    public BatchRead getBatchRead(String ns, byte[] digest) throws Exception {
        Map<String, Object> batchReadBody = new HashMap<>();
        Map<String, String> keyMap = new HashMap<>();
        Encoder urlEncoder = Base64.getUrlEncoder();
        String strBytes = urlEncoder.encodeToString(digest);

        if (ns != null) {
            keyMap.put("namespace", ns);
        }
        keyMap.put("digest", strBytes);
        batchReadBody.put("key", keyMap);

        String batchReadStr = mapper.writeValueAsString(batchReadBody);
        return mapper.readValue(batchReadStr, BatchRead.class);

    }
}

class MsgPackBatchReadTester implements BatchReadBodyMapperTester {
    ObjectMapper mapper = null;

    public MsgPackBatchReadTester(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public BatchRead getBatchRead(String ns, String set, String key, List<String> bins,
                                  String keytype) throws Exception {
        Map<String, Object> batchReadBody = new HashMap<>();
        Map<String, String> keyMap = new HashMap<>();

        if (bins != null) {
            batchReadBody.put("binNames", bins);
        }
        if (ns != null) {
            keyMap.put("namespace", ns);
        }
        if (set != null) {
            keyMap.put("setName", set);
        }

        if (key != null) {
            keyMap.put("userKey", key);
        }

        if (keytype != null) {
            keyMap.put("keytype", keytype);
        }

        batchReadBody.put("key", keyMap);


        byte[] batchReadBytes = mapper.writeValueAsBytes(batchReadBody);
        return mapper.readValue(batchReadBytes, BatchRead.class);
    }

    @Override
    public BatchRead getBatchRead(String ns, byte[] digest) throws Exception {
        Map<String, Object> batchReadBody = new HashMap<>();
        Map<String, String> keyMap = new HashMap<>();
        Encoder urlEncoder = Base64.getUrlEncoder();
        String strBytes = urlEncoder.encodeToString(digest);

        if (ns != null) {
            keyMap.put("namespace", ns);
        }
        keyMap.put("digest", strBytes);
        batchReadBody.put("key", keyMap);

        byte[] batchReadBytes = mapper.writeValueAsBytes(batchReadBody);
        return mapper.readValue(batchReadBytes, BatchRead.class);

    }

}