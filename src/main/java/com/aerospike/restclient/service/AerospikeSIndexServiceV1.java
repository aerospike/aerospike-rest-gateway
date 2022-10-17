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

import com.aerospike.client.policy.InfoPolicy;
import com.aerospike.client.policy.Policy;
import com.aerospike.client.query.IndexCollectionType;
import com.aerospike.client.query.IndexType;
import com.aerospike.restclient.domain.RestClientIndex;
import com.aerospike.restclient.domain.auth.AuthDetails;
import com.aerospike.restclient.handlers.IndexHandler;
import com.aerospike.restclient.handlers.InfoHandler;
import com.aerospike.restclient.util.AerospikeClientPool;
import com.aerospike.restclient.util.InfoResponseParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AerospikeSIndexServiceV1 implements AerospikeSIndexService {

    @Autowired
    private AerospikeClientPool clientPool;

    /* Index Methods using index methods from client */
    @Override
    public void createIndex(AuthDetails authDetails, RestClientIndex indexModel, Policy policy) {
        String indexName = indexModel.getName();
        String binName = indexModel.getBin();
        String namespace = indexModel.getNamespace();
        String setName = indexModel.getSet();
        IndexType indexType = indexModel.getIndexType();
        IndexCollectionType collectionType = indexModel.getCollectionType();

        IndexHandler indexHandler = IndexHandler.create(clientPool.getClient(authDetails));
        if (collectionType == null) {
            indexHandler.createIndex(policy, namespace, setName, indexName, binName, indexType);
        } else {
            indexHandler.createIndex(policy, namespace, setName, indexName, binName, indexType, collectionType);
        }
    }

    @Override
    public void dropIndex(AuthDetails authDetails, String namespace, String indexName, Policy policy) {
        IndexHandler indexHandler = IndexHandler.create(clientPool.getClient(authDetails));
        indexHandler.deleteIndex(policy, namespace, null, indexName);
    }

    /* Index Methods using info methods from client */
    @Override
    public List<RestClientIndex> getIndexList(AuthDetails authDetails, String namespace, InfoPolicy policy) {
        String request = getSindexListCommand(namespace);

        String response = InfoHandler.create(clientPool.getClient(authDetails)).singleInfoRequest(policy, request);

        List<Map<String, String>> sindexInfos = InfoResponseParser.getIndexInformation(response);
        return sindexInfos.stream().map(RestClientIndex::new).toList();
    }

    @Override
    public Map<String, String> indexStats(AuthDetails authDetails, String namespace, String name, InfoPolicy policy) {
        String request = "sindex/" + namespace + "/" + name;
        String response = InfoHandler.create(clientPool.getClient(authDetails)).singleInfoRequest(policy, request);

        return InfoResponseParser.getIndexStatInfo(response);
    }

    private String getSindexListCommand(String namespace) {
        return (namespace == null || namespace.isEmpty()) ? "sindex" : "sindex/" + namespace;
    }
}
