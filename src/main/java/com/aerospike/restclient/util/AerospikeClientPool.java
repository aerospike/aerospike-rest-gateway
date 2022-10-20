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

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Host;
import com.aerospike.client.Value;
import com.aerospike.client.policy.ClientPolicy;
import com.aerospike.restclient.domain.auth.AuthDetails;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.google.common.hash.Hashing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class AerospikeClientPool {

    private static final Logger logger = LoggerFactory.getLogger(AerospikeClientPool.class);
    private static final String DEFAULT_CLIENT_KEY = "DEFAULT_CLIENT_KEY";

    private final Cache<String, AerospikeClient> clientPool;
    private final ClientPolicy clientPolicy;

    private final int port;
    private final String hostList;
    private final String hostname;

    public AerospikeClientPool(int poolSize, ClientPolicy clientPolicy, int port, String hostList, String hostname,
                               AerospikeClient defaultClient, boolean useBoolBin) {
        RemovalListener<String, AerospikeClient> removalListener = AerospikeClientPool::onClientRemoval;

        this.clientPool = CacheBuilder.newBuilder().maximumSize(poolSize).removalListener(removalListener).build();

        if (defaultClient != null) {
            this.clientPool.put(DEFAULT_CLIENT_KEY, defaultClient);
        }
        this.clientPolicy = clientPolicy;
        this.port = port;
        this.hostList = hostList;
        this.hostname = hostname;

        Value.UseBoolBin = useBoolBin;
    }

    public AerospikeClient getClient(AuthDetails authDetails) {
        if (authDetails == null) {
            return getClient();
        }

        String key = buildPoolKey(authDetails);
        return Optional.ofNullable(clientPool.getIfPresent(key)).orElseGet(() -> {
            ClientPolicy policy = new ClientPolicy(clientPolicy);
            policy.user = authDetails.getUser();
            policy.password = authDetails.getPassword();
            AerospikeClient client;

            logger.info("Init a new AerospikeClient");
            /* A list of hosts was provided, parse it into host array*/
            if (hostList != null) {
                client = new AerospikeClient(policy, Host.parseHosts(hostList, port));

            } else {
                /* No list of hosts provided, try with a single host name */
                client = new AerospikeClient(policy, hostname, port);
            }

            clientPool.put(key, client);
            return client;
        });
    }

    protected AerospikeClient getClient() {
        AerospikeClient client = clientPool.getIfPresent(DEFAULT_CLIENT_KEY);
        if (client == null) {
            throw new RestClientErrors.UnauthorizedError();
        }

        return client;
    }

    @SuppressWarnings("UnstableApiUsage")
    protected String buildPoolKey(AuthDetails authDetails) {
        return Hashing.sha256().hashBytes(authDetails.toString().getBytes()).toString();
    }

    public static void onClientRemoval(RemovalNotification<String, AerospikeClient> removal) {
        AerospikeClient client = removal.getValue();

        if (client != null) {
            client.close(); // tear down properly
        }
    }
}
