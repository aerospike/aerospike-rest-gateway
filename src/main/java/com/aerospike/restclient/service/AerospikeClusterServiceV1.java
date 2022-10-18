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
package com.aerospike.restclient.service;

import com.aerospike.restclient.domain.auth.AuthDetails;
import com.aerospike.restclient.handlers.ClusterHandler;
import com.aerospike.restclient.handlers.InfoHandler;
import com.aerospike.restclient.util.AerospikeClientPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AerospikeClusterServiceV1 implements AerospikeClusterService {

    @Autowired
    private AerospikeClientPool clientPool;

    /*
     * Returns an object showing cluster information
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
    public Map<String, Object> getClusterInfo(AuthDetails authDetails) {
        Map<String, Object> clusterInfo = new HashMap<>();

        clusterInfo.put("nodes", ClusterHandler.create(clientPool.getClient(authDetails)).getNodeMaps());
        clusterInfo.put("namespaces", InfoHandler.create(clientPool.getClient(authDetails)).getNamespaceInfoMaps());

        return clusterInfo;
    }
}
