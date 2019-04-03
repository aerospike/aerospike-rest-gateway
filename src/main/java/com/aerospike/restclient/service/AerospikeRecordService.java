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

import com.aerospike.client.policy.Policy;
import com.aerospike.client.policy.WritePolicy;
import com.aerospike.restclient.domain.RestClientRecord;
import com.aerospike.restclient.util.AerospikeAPIConstants.RecordKeyType;

public interface AerospikeRecordService {
	public void
	storeRecord(String namespace, String set, String key, Map<String, Object> binMap,
			RecordKeyType keyType, WritePolicy policy);

	public RestClientRecord
	fetchRecord(String namespace, String set, String key, String[] bins, RecordKeyType keyType, Policy policy);

	public void
	deleteRecord(String namespace, String set, String key, RecordKeyType keyType, WritePolicy policy);

	public boolean
	recordExists(String namespace, String set, String key, RecordKeyType keyType);
}