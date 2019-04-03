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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.aerospike.client.AerospikeException;
import com.aerospike.client.ResultCode;
import com.aerospike.restclient.handlers.ClusterHandler;
import com.aerospike.restclient.handlers.InfoHandler;
import com.aerospike.restclient.util.InfoResponseParser;

@Service
public class AerospikeClusterServiceV1 implements AerospikeClusterService {

	private static final String nsNotFound = "ns_type=unknown";

	private InfoHandler infoHandler;
	private ClusterHandler clusterHandler;

	public AerospikeClusterServiceV1(InfoHandler infoHandler, ClusterHandler clusterHandler) {
		this.infoHandler = infoHandler;
		this.clusterHandler = clusterHandler;
	}



	/*
	 * Returns an object showing clusterinformation
	 * {
	 *  "nodes": [
	 *  		{"name": "node1"},
	 *  		{"name": "node2"}
	 *  ],
	 *  "namespaces" : [
	 *  		{
	 *  			"name" : "ns1",
	 *  			"sets" : [
	 *  				{"name" :"set1", "objectCount": #},
	 *  			]
	 *  		}, ...
	 *  ]
	 *
	 * }
	 */
	@Override
	public Map<String, Object> getClusterInfo() {
		Map<String, Object>clusterInfo = new HashMap<String, Object>();

		clusterInfo.put("nodes", getNodeMaps());
		clusterInfo.put("namespaces", getNamespaceInfoMaps());

		return clusterInfo;
	}


	/*
	 * Returns an array containing all sets in the given namespace:
	 * ["set1", "set2", ...]
	 */
	private String[] getSets(String namespace) {
		String request = "sets/" + namespace;

		String response = infoHandler.singleInfoRequest(null, request);

		if (response.equals(nsNotFound)) {
			throw new AerospikeException(ResultCode.INVALID_NAMESPACE, String.format("Namespace: %s not found.", namespace));
		}
		return InfoResponseParser.getSetsFromResponse(response);
	}

	/*
	 * Returns an array ["namespace1", "namespace2",...]
	 */
	private String[] getNamespaces() {
		String response = infoHandler.singleInfoRequest(null, "namespaces");
		return InfoResponseParser.getNamespacesFromResponse(response);
	}

	/*
	 * Extracts the replication factor from a namespace;
	 */
	private int getReplicationFactor(String namespace) {
		String request = "namespace/" + namespace;
		String response = infoHandler.singleInfoRequest(null, request);
		return InfoResponseParser.getReplicationFactor(response, namespace);
	}


	/*
	 * Returns the number of objects in a specific Namespace/set
	 */
	private long getSetRecordCount(String namespace, String set) {
		int replFactor = getReplicationFactor(namespace);

		long objects = 0;
		boolean found = false;

		String setInfoRequest = "sets/" + namespace + "/" + set;
		List<String>responses = infoHandler.infoRequestAll(null, setInfoRequest);

		for (String response: responses) {
			if (!response.trim().equals("")) {
				found = true;
			}
			objects += InfoResponseParser.getSetObjectCountFromResponse(response);
		}
		if (found) {
			return objects / Math.min(responses.size(), replFactor);
		}
		throw new AerospikeException(ResultCode.INVALID_NAMESPACE, String.format("Namspace/Set: %s/%s not found", namespace, set));
	}

	/*
	 * Return list [{"name": "nodeName1"}, {"name":"nodeName2"}]
	 */
	private List<Map<String, Object>>getNodeMaps(){
		List<Map<String, Object>>nodeList = new ArrayList<Map<String, Object>>();
		for (String nodeName : clusterHandler.nodeNames()) {
			Map<String, Object>nodeMap = new HashMap<String, Object>();
			nodeMap.put("name", nodeName);
			nodeList.add(nodeMap);
		}
		return nodeList;
	}

	private List<Map<String, Object>>getNamespaceInfoMaps() {
		List<Map<String, Object>>namespaceList = new ArrayList<Map<String, Object>>();
		for (String ns : getNamespaces()) {
			namespaceList.add(getNamespaceInfoMap(ns));
		}
		return namespaceList;
	}

	private Map<String, Object>getNamespaceInfoMap(String ns) {
		Map<String, Object>nsMap = new HashMap<String, Object>();
		nsMap.put("name", ns);
		nsMap.put("sets", getSetInfoMaps(ns));
		return nsMap;
	}

	/*
	 * returns a list [{"name": "setName", "objectCount": #}, {"name": "setName2", "objectCount": #}..]
	 */
	private List<Map<String, Object>>getSetInfoMaps(String ns) {
		List<Map<String, Object>>setList = new ArrayList<Map<String, Object>>();
		for (String set : getSets(ns)) {
			setList.add(getSetInfo(ns, set));
		}
		return setList;
	}

	/*
	 * returns a map {"name": "setName", "objectCount": #}
	 *
	 */
	private Map<String, Object>getSetInfo(String ns, String set) {
		Map<String, Object>setMap = new HashMap<String, Object>();
		setMap.put("objectCount", getSetRecordCount(ns, set));
		setMap.put("name", set);
		return setMap;
	}
}