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

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.AerospikeException;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TruncateTestsError {

    private MockMvc mockMVC;
    private String truncateSplitPoint;
    DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    private final String testEndpoint = "/v1/truncate/";
    private final Key otherKey = new Key("test", "otherset", 0);
    private List<Key> preCutoffKeys;
    private List<Key> postCutoffKeys;
    @Autowired
    private AerospikeClient client;

    @Autowired
    private WebApplicationContext wac;

    @Before
    public void setup() throws InterruptedException {
        mockMVC = MockMvcBuilders.webAppContextSetup(wac).build();
        preCutoffKeys = new ArrayList<Key>();
        postCutoffKeys = new ArrayList<Key>();
        for (int i = 0; i < 10; i++) {
            Key key = new Key("test", "truncate", i);
            preCutoffKeys.add(key);
            Bin bin = new Bin("before", "the cutoff");
            client.put(null, key, bin);
        }
        /* Truncate is asynchronous, so wait for it to propagate to other servers */
        for (int i = 10; i < 20; i++) {
            Key key = new Key("test", "truncate", i);
            Bin bin = new Bin("after", "the cutoff");
            client.put(null, key, bin);
            postCutoffKeys.add(key);
        }

        // Store a record in a different set
        client.put(null, otherKey, new Bin("a", "b"));
    }

    @After
    public void clean() {
        for (Key key : preCutoffKeys) {
            try {
                client.delete(null, key);
            } catch (AerospikeException e) {
            }
        }
        for (Key key : postCutoffKeys) {
            try {
                client.delete(null, key);
            } catch (AerospikeException e) {
            }
        }
        client.delete(null, otherKey);
    }

    @Test
    public void TruncateWithInvalidDate() throws Exception {
        Map<String, Object> binMap = new HashMap<String, Object>();

        binMap.put("string", "Aerospike");

        mockMVC.perform(delete(testEndpoint + "test/truncate?date=January20157pm")).andExpect(status().isBadRequest());
    }

    @Test
    public void TruncateDateInTheFuture() throws Exception {
        Map<String, Object> binMap = new HashMap<String, Object>();

        GregorianCalendar cal = (GregorianCalendar) GregorianCalendar.getInstance();
        /* This test will start failing in 2200 */
        cal.set(GregorianCalendar.YEAR, 2200);
        truncateSplitPoint = formatter.format(cal.toZonedDateTime());

        binMap.put("string", "Aerospike");

        mockMVC.perform(delete(testEndpoint + "test/truncate?date=" + truncateSplitPoint))
                .andExpect(status().isInternalServerError());

    }
}
