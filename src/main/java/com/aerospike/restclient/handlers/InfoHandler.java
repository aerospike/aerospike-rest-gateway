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
import com.aerospike.client.AerospikeException;
import com.aerospike.client.Info;
import com.aerospike.client.ResultCode;
import com.aerospike.client.cluster.Node;
import com.aerospike.client.policy.InfoPolicy;
import com.aerospike.restclient.util.InfoResponseParser;
import com.aerospike.restclient.util.RestClientErrors;
import com.aerospike.restclient.util.RestClientErrors.AerospikeRestClientError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InfoHandler {

    private static final String nsNotFound = "ns_type=unknown";

    private final AerospikeClient client;

    public InfoHandler(AerospikeClient client) {
        this.client = client;
    }

    public List<String> infoRequestAll(String request) {
        List<String> responses = new ArrayList<>();
        for (Node node : client.getNodes()) {
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

    public List<Map<String, Object>> getNamespaceInfoMaps() {
        List<Map<String, Object>> namespaceList = new ArrayList<>();
        for (String ns : getNamespaces()) {
            namespaceList.add(getNamespaceInfoMap(ns));
        }

        return namespaceList;
    }

    /*
     * Returns an array containing all sets in the given namespace:
     * ["set1", "set2", ...]
     */
    private String[] getSets(String namespace) {
        String request = "sets/" + namespace;

        String response = singleInfoRequest(null, request);

        if (response.equals(nsNotFound)) {
            throw new AerospikeException(ResultCode.INVALID_NAMESPACE,
                    String.format("Namespace: %s not found.", namespace));
        }
        return InfoResponseParser.getSetsFromResponse(response);
    }

    /*
     * Returns an array ["namespace1", "namespace2",...]
     */
    private String[] getNamespaces() {
        String response = singleInfoRequest(null, "namespaces");
        return InfoResponseParser.getNamespacesFromResponse(response);
    }

    /*
     * Extracts the replication factor from a namespace;
     */
    private int getReplicationFactor(String namespace) {
        String request = "namespace/" + namespace;
        String response = singleInfoRequest(null, request);
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
        List<String> responses = infoRequestAll(setInfoRequest);

        for (String response : responses) {
            if (!response.trim().isEmpty()) {
                found = true;
            }
            objects += InfoResponseParser.getSetObjectCountFromResponse(response);
        }

        if (responses.size() == 0 || replFactor == 0) {
            throw new RestClientErrors.ClusterUnstableError("Cluster unstable, unable to return cluster information");
        }

        if (found) {
            return objects / Math.min(responses.size(), replFactor);
        }

        throw new AerospikeException(ResultCode.INVALID_NAMESPACE,
                String.format("Namspace/Set: %s/%s not found", namespace, set));
    }

    private Map<String, Object> getNamespaceInfoMap(String ns) {
        Map<String, Object> nsMap = new HashMap<>();
        nsMap.put("name", ns);
        nsMap.put("sets", getSetInfoMaps(ns));

        return nsMap;
    }

    /*
     * returns a list [{"name": "setName", "objectCount": #}, {"name": "setName2", "objectCount": #}..]
     */
    private List<Map<String, Object>> getSetInfoMaps(String ns) {
        List<Map<String, Object>> setList = new ArrayList<>();
        for (String set : getSets(ns)) {
            setList.add(getSetInfo(ns, set));
        }

        return setList;
    }

    /*
     * returns a map {"name": "setName", "objectCount": #}
     *
     */
    private Map<String, Object> getSetInfo(String ns, String set) {
        Map<String, Object> setMap = new HashMap<>();
        setMap.put("objectCount", getSetRecordCount(ns, set));
        setMap.put("name", set);

        return setMap;
    }

    public static InfoHandler create(AerospikeClient client) {
        return new InfoHandler(client);
    }

}
