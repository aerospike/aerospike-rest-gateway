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
package com.aerospike.restclient.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.aerospike.client.policy.InfoPolicy;
import com.aerospike.restclient.handlers.InfoHandler;

@Service
public class AerospikeInfoServiceV1 implements AerospikeInfoService {

	private InfoHandler handler;

	public AerospikeInfoServiceV1(InfoHandler handler) {
		this.handler = handler;
	}

	@Override
	public Map<String, String> infoAny(String[] requests, InfoPolicy policy) {
		return handler.multiInfoRequest(policy, requests);
	}

	@Override
	public Map<String, String> infoNodeName(String nodeName, String[] requests, InfoPolicy policy) {
		return handler.multiInfoRequest(policy, nodeName, requests);
	}

}