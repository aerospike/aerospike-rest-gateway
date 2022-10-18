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
package com.aerospike.restclient.handlers;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.policy.InfoPolicy;

import java.util.Calendar;

public class TruncateHandler {

    private final AerospikeClient client;

    public TruncateHandler(AerospikeClient client) {
        this.client = client;
    }

    public void truncate(InfoPolicy policy, String ns, String set, Calendar beforeLastUpdate) {
        client.truncate(policy, ns, set, beforeLastUpdate);
    }

    public static TruncateHandler create(AerospikeClient client) {
        return new TruncateHandler(client);
    }
}
