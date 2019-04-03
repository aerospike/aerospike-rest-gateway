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
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aerospike.client.BatchRead;
import com.aerospike.client.policy.BatchPolicy;
import com.aerospike.restclient.domain.RestClientBatchReadBody;
import com.aerospike.restclient.domain.RestClientBatchReadResponse;
import com.aerospike.restclient.handlers.BatchHandler;

@Service
public class AerospikeBatchServiceV1 implements AerospikeBatchService {
	private BatchHandler handler;

	@Autowired
	public AerospikeBatchServiceV1(BatchHandler handler) {
		this.handler = handler;
	}

	@Override
	public List<RestClientBatchReadResponse> batchGet(List<RestClientBatchReadBody> batchKeys, BatchPolicy policy) {
		List<BatchRead> batchReads = batchKeys.stream().map(bk -> bk.toBatchRead()).collect(Collectors.toList());
		handler.batchRead(policy, batchReads);

		return batchReads.stream().map(br -> new RestClientBatchReadResponse(br)).collect(Collectors.toList());
	}

}