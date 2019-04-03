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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.aerospike.client.AerospikeClient;
import com.aerospike.restclient.handlers.AdminHandler;
import com.aerospike.restclient.handlers.BatchHandler;
import com.aerospike.restclient.handlers.ClusterHandler;
import com.aerospike.restclient.handlers.IndexHandler;
import com.aerospike.restclient.handlers.InfoHandler;
import com.aerospike.restclient.handlers.OperateHandler;
import com.aerospike.restclient.handlers.RecordHandler;
import com.aerospike.restclient.handlers.TruncateHandler;

@Configuration
public class HandlerConfig {

	@Bean
	@Autowired
	TruncateHandler getTruncateHandler(AerospikeClient client) {
		return new TruncateHandler(client);
	}

	@Bean
	@Autowired
	OperateHandler getOperateHandler(AerospikeClient client) {
		return new OperateHandler(client);
	}

	@Bean
	@Autowired
	BatchHandler getBatchHandler(AerospikeClient client) {
		return new BatchHandler(client);
	}

	@Bean
	@Autowired
	RecordHandler getRecordHandler(AerospikeClient client) {
		return new RecordHandler(client);
	}

	@Bean
	@Autowired
	AdminHandler getAdminHandler(AerospikeClient client) {
		return new AdminHandler(client);
	}

	@Bean
	@Autowired
	InfoHandler getInfoHandler(AerospikeClient client) {
		return new InfoHandler(client);
	}

	@Bean
	@Autowired
	IndexHandler getIndexHandler(AerospikeClient client) {
		return new IndexHandler(client);
	}

	@Bean
	@Autowired
	ClusterHandler getClusterHandler(AerospikeClient client) {
		return new ClusterHandler(client);
	}
}
