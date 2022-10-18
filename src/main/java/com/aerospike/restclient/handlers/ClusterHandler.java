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
import com.aerospike.client.cluster.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClusterHandler {

    private final AerospikeClient client;

    public ClusterHandler(AerospikeClient client) {
        this.client = client;
    }

    /*
     * Return list [{"name": "nodeName1"}, {"name":"nodeName2"}]
     */
    public List<Map<String, Object>> getNodeMaps() {
        List<Map<String, Object>> nodeList = new ArrayList<>();
        for (String nodeName : nodeNames()) {
            Map<String, Object> nodeMap = new HashMap<>();
            nodeMap.put("name", nodeName);
            nodeList.add(nodeMap);
        }

        return nodeList;
    }

    private List<String> nodeNames() {
        return client.getNodeNames();
    }

    public Node[] getNodes() {
        return client.getNodes();
    }

    public static ClusterHandler create(AerospikeClient client) {
        return new ClusterHandler(client);
    }

}
