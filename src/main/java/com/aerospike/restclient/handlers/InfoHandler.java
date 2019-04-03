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
package com.aerospike.restclient.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Info;
import com.aerospike.client.cluster.Node;
import com.aerospike.client.policy.InfoPolicy;
import com.aerospike.restclient.util.RestClientErrors.AerospikeRestClientError;

public class InfoHandler {

	private AerospikeClient client;

	public InfoHandler(AerospikeClient client) {
		this.client = client;
	}

	public List<String> infoRequestAll(InfoPolicy policy, String request) {
		List<String> responses = new ArrayList<>();

		for (Node node: client.getNodes()) {
			responses.add(Info.request(node, request));
		}
		return responses;
	}

	public String singleInfoRequest(InfoPolicy policy, String request) {
		Node[] nodes = client.getNodes();
		if (nodes.length == 0) {
			throw new AerospikeRestClientError("No nodes available");
		}
		Node node = nodes[0];
		return Info.request(policy, node, request);
	}

	public Map<String, String> multiInfoRequest(InfoPolicy policy, String[] requests) {
		Node[] nodes = client.getNodes();
		if (nodes.length == 0) {
			throw new AerospikeRestClientError("No nodes available");
		}
		Node node = nodes[0];
		return Info.request(policy, node, requests);
	}

	public String singleInfoRequest(InfoPolicy policy, String nodeName, String request) {
		Node node = client.getNode(nodeName);
		return Info.request(policy, node, request);
	}

	public Map<String, String> multiInfoRequest(InfoPolicy policy, String nodeName, String[] requests) {
		Node node = client.getNode(nodeName);
		return Info.request(policy, node, requests);
	}
}
