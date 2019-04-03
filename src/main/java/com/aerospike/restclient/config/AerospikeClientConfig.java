/*
 * Copyright 2019 Aerospike, Inc.
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Host;
import com.aerospike.client.policy.ClientPolicy;

@Configuration
public class AerospikeClientConfig {
	@Value("${aerospike.restclient.hostname:localhost}")
	String hostname;

	@Value("${aerospike.restclient.port:3000}")
	int port;

	@Value("${aerospike.restclient.hostlist:#{null}}") String hostList;

	@Autowired
	ClientPolicy policy;

	@Bean
	public AerospikeClient ConfigAerospikeClient() {
		AerospikeClient client;

		/* A list of hosts was provided, parse it into host array*/
		if (hostList != null) {
			client = new AerospikeClient(policy, Host.parseHosts(hostList, port));

		} else {
			/* No list of hosts provided, try with a single host name */
			client = new AerospikeClient(policy, hostname, port);
		}

		return client;
	}
}

