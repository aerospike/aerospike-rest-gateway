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

import com.aerospike.client.policy.AuthMode;
import com.aerospike.client.policy.ClientPolicy;
import com.aerospike.restclient.util.TLSPolicyBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class ClientPolicyConfig {

    /* Client policies */
    @Value("${aerospike.restclient.clientpolicy.user:#{null}}")
    String username;
    @Value("${aerospike.restclient.clientpolicy.password:#{null}}")
    String password;
    @Value("${aerospike.restclient.clientpolicy.clusterName:#{null}}")
    String clusterName;
    @Value("${aerospike.restclient.clientpolicy.authMode:#{null}}")
    String authMode;

    @Value("${aerospike.restclient.clientpolicy.connPoolsPerNode:#{null}}")
    Integer connPoolsPerNode;
    @Value("${aerospike.restclient.clientpolicy.minConnsPerNode:#{null}}")
    Integer minConnsPerNode;
    @Value("${aerospike.restclient.clientpolicy.maxConnsPerNode:#{null}}")
    Integer maxConnsPerNode;
    @Value("${aerospike.restclient.clientpolicy.maxSocketIdle:#{null}}")
    Integer maxSocketIdle;
    @Value("${aerospike.restclient.clientpolicy.maxErrorRate:#{null}}")
    Integer maxErrorRate;
    @Value("${aerospike.restclient.clientpolicy.tendInterval:#{null}}")
    Integer tendInterval;
    @Value("${aerospike.restclient.clientpolicy.timeout:#{null}}")
    Integer timeout;
    @Value("${aerospike.restclient.clientpolicy.loginTimeout:#{null}}")
    Integer loginTimeout;

    @Value("${aerospike.restclient.clientpolicy.failIfNotConnected:#{null}}")
    Boolean failIfNotConnected;
    @Value("${aerospike.restclient.clientpolicy.sharedThreadPool:#{null}}")
    Boolean sharedThreadPool;
    @Value("${aerospike.restclient.clientpolicy.useServicesAlternate:#{null}}")
    Boolean useServicesAlternate;

    @Value("${aerospike.restclient.clientpolicy.rackIds:#{null}}")
    Integer[] rackIds;
    @Value("${aerospike.restclient.clientpolicy.errorRateWindow:#{null}}")
    Integer errorRateWindow;

    /* Read policies */
    @Bean
    public ClientPolicy ConfigClientPolicy(@Autowired TLSPolicyBuilder builder) {
        ClientPolicy clientPolicy = new ClientPolicy();

        if (username != null) {
            clientPolicy.user = username;
        }

        if (password != null) {
            clientPolicy.password = password;
        }

        if (clusterName != null) {
            clientPolicy.clusterName = clusterName;
        }

        if (authMode != null) {
            clientPolicy.authMode = AuthMode.valueOf(authMode.toUpperCase());
        }

        if (connPoolsPerNode != null) {
            clientPolicy.connPoolsPerNode = connPoolsPerNode;
        }

        if (minConnsPerNode != null) {
            clientPolicy.minConnsPerNode = minConnsPerNode;
        }

        if (maxConnsPerNode != null) {
            clientPolicy.maxConnsPerNode = maxConnsPerNode;
        }

        if (maxSocketIdle != null) {
            clientPolicy.maxSocketIdle = maxSocketIdle;
        }

        if (maxErrorRate != null) {
            clientPolicy.maxErrorRate = maxErrorRate;
        }

        if (tendInterval != null) {
            clientPolicy.tendInterval = tendInterval;
        }

        if (timeout != null) {
            clientPolicy.timeout = timeout;
        }

        if (loginTimeout != null) {
            clientPolicy.loginTimeout = loginTimeout;
        }

        if (failIfNotConnected != null) {
            clientPolicy.failIfNotConnected = failIfNotConnected;
        }

        if (sharedThreadPool != null) {
            clientPolicy.sharedThreadPool = sharedThreadPool;
        }

        if (useServicesAlternate != null) {
            clientPolicy.useServicesAlternate = useServicesAlternate;
        }

        if (rackIds != null && rackIds.length > 0) {
            clientPolicy.rackIds = Arrays.asList(rackIds);
        }

        if (errorRateWindow != null) {
            clientPolicy.errorRateWindow = errorRateWindow;
        }

        clientPolicy.tlsPolicy = builder.build();

        return clientPolicy;
    }

}
