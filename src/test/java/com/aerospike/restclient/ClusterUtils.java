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
import com.aerospike.client.Info;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ClusterUtils {

    private static final Logger logger = LoggerFactory.getLogger(ClusterUtils.class);

    private ClusterUtils() {
    }

    public static boolean isEnterpriseEdition(AerospikeClient client) {
        String infoEdition = Info.request(null, client.getCluster().getRandomNode(), "edition");
        return infoEdition.contains("Enterprise");
    }

    public static boolean isSecurityEnabled(AerospikeClient client) {
        if (isEnterpriseEdition(client)) {
            try {
                client.queryRoles(null);
                return true;
            } catch (AerospikeException e) {
                logger.info("Aerospike Server Enterprise Edition security disabled");
            }
        }
        return false;
    }
}
