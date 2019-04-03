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

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.aerospike.client.policy.InfoPolicy;
import com.aerospike.client.policy.Policy;
import com.aerospike.client.query.IndexCollectionType;
import com.aerospike.client.query.IndexType;
import com.aerospike.restclient.domain.RestClientIndex;
import com.aerospike.restclient.handlers.IndexHandler;
import com.aerospike.restclient.handlers.InfoHandler;
import com.aerospike.restclient.util.InfoResponseParser;

@Service
public class AerospikeSindexServiceV1 implements AerospikeSindexService {

	private InfoHandler infoHandler;
	private IndexHandler indexHandler;

	public AerospikeSindexServiceV1(InfoHandler infoHandler, IndexHandler indexHandler) {
		this.infoHandler = infoHandler;
		this.indexHandler = indexHandler;
	}

	/* Index Methods using index methods from client */
	@Override
	public void createIndex(RestClientIndex indexModel, Policy policy) {
		String indexName = indexModel.getName();
		String binName = indexModel.getBin();
		String namespace = indexModel.getNamespace();
		String setName = indexModel.getSet();
		IndexType indexType = indexModel.getIndexType();
		IndexCollectionType collectionType = indexModel.getCollectionType();

		if (collectionType == null) {
			indexHandler.createIndex(policy, namespace, setName, indexName, binName, indexType);
		} else {
			indexHandler.createIndex(policy, namespace, setName, indexName, binName, indexType, collectionType);
		}
	}

	@Override
	public void dropIndex(String namespace, String indexName, Policy policy) {
		indexHandler.deleteIndex(policy, namespace, null, indexName);
	}

	/* Index Methods using info methods from client */
	@Override
	public List<RestClientIndex> getIndexList(String namespace, InfoPolicy policy) {
		String request = getSindexListCommand(namespace);

		String response = infoHandler.singleInfoRequest(policy, request);

		List<Map<String, String>> sindexInfos = InfoResponseParser.getIndexInformation(response);
		return sindexInfos.stream().map(si -> new RestClientIndex(si)).collect(Collectors.toList());
	}






	@Override
	public Map<String, String> indexStats(String namespace, String name, InfoPolicy policy) {
		String request = "sindex/" + namespace + "/" + name;
		String response = infoHandler.singleInfoRequest(policy, request);

		return InfoResponseParser.getIndexStatInfo(response);
	}

	private String getSindexListCommand(String namespace) {
		return (namespace == null || namespace == "") ? "sindex" : "sindex/" + namespace;
	}

}