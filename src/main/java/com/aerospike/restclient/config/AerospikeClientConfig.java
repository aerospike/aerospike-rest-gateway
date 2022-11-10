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
package com.aerospike.restclient.config;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.AerospikeException;
import com.aerospike.client.Host;
import com.aerospike.client.policy.ClientPolicy;
import com.aerospike.restclient.util.AerospikeClientPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

@Configuration
public class AerospikeClientConfig {
    private static final Logger logger = LoggerFactory.getLogger(AerospikeClientConfig.class);

    @Value("${aerospike.restclient.hostname:localhost}")
    String hostname;

    @Value("${aerospike.restclient.port:3000}")
    int port;

    @Value("${aerospike.restclient.hostlist:#{null}}")
    String hostList;

    @Value("${aerospike.restclient.requireAuthentication:false}")
    boolean requireAuthentication;

    @Bean
    @Retryable(
            value = {AerospikeException.class}, backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    public AerospikeClient configAerospikeClient(ClientPolicy policy) {
        if (requireAuthentication) {
            return null;
        }

        AerospikeClient client;

        logger.info("Init the AerospikeClient");
        /* A list of hosts was provided, parse it into host array */
        if (hostList != null) {
            client = new AerospikeClient(policy, Host.parseHosts(hostList, port));

        } else {
            /* No list of hosts provided, try with a single host name */
            client = new AerospikeClient(policy, hostname, port);
        }

        return client;
    }

    @Value("${aerospike.restclient.useBoolBin:false}")
    boolean useBoolBin;

    @Value("${aerospike.restclient.pool.size:16}")
    int poolSize;

    @Bean
    public AerospikeClientPool configAerospikeClientPool(ClientPolicy policy,
                                                         ObjectProvider<AerospikeClient> defaultClient) {
        return new AerospikeClientPool(poolSize, policy, port, hostList, hostname, defaultClient.getIfAvailable(),
                useBoolBin);
    }
}


