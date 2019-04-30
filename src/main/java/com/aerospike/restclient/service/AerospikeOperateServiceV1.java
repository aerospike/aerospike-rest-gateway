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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aerospike.client.Key;
import com.aerospike.client.Operation;
import com.aerospike.client.Record;
import com.aerospike.client.policy.WritePolicy;
import com.aerospike.restclient.domain.RestClientOperation;
import com.aerospike.restclient.domain.RestClientRecord;
import com.aerospike.restclient.handlers.OperateHandler;
import com.aerospike.restclient.util.AerospikeAPIConstants.RecordKeyType;
import com.aerospike.restclient.util.KeyBuilder;
import com.aerospike.restclient.util.RestClientErrors;
import com.aerospike.restclient.util.converters.OperationsConverter;

@Service
public class AerospikeOperateServiceV1 implements AerospikeOperateService {

	private OperateHandler handler;

	@Autowired
	public AerospikeOperateServiceV1(OperateHandler handler) {
		this.handler = handler;
	}

	@Override
	public RestClientRecord operate(String namespace, String set, String key, List<Map<String, Object>> opsMaps,
			RecordKeyType keyType, WritePolicy policy) {

		Operation[] operations = OperationsConverter.mapListToOperationsArray(opsMaps);
		Key opKey = KeyBuilder.buildKey(namespace, set, key, keyType);
		Record fetchedRecord = handler.operate(policy, opKey, operations);
		if (fetchedRecord == null) {
			throw new RestClientErrors.RecordNotFoundError();
		}
		return new RestClientRecord(fetchedRecord);
	}

	@Override
	public RestClientRecord operateRCOps(String namespace, String set, String key, List<RestClientOperation> opsList,
			RecordKeyType keyType, WritePolicy policy) {
		List<Map<String, Object>> opsMapsList = opsList.stream().map(op -> op.toMap()).collect(Collectors.toList());

		Operation[] operations = OperationsConverter.mapListToOperationsArray(opsMapsList);
		Key opKey = KeyBuilder.buildKey(namespace, set, key, keyType);
		Record fetchedRecord = handler.operate(policy, opKey, operations);
		if (fetchedRecord == null) {
			throw new RestClientErrors.RecordNotFoundError();
		}
		return new RestClientRecord(fetchedRecord);

	}
}