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
package com.aerospike.restclient.util;

public final class AerospikeAPIConstants {

	private AerospikeAPIConstants() {}

	// Entries in the JSON representation of a record;
	public static final String RECORD_KEY = "key";
	public static final String RECORD_BINS = "recordBins";
	public static final String RECORD_GENERATION = "generation";
	public static final String RECORD_EXPIRATION = "expiration";
	public static final String RECORD_TTL = "ttl";

	// Entries in a JSON representation of a key
	public static final String PRIMARY_KEY = "pk";
	public static final String USER_KEY = "userKey";
	public static final String NAMESPACE = "namespace";
	public static final String SET = "set";
	public static final String SETNAME = "setName";
	public static final String DIGEST = "digest";
	public static final String TYPE = "type";

	// Key types
	public static final String STRING_KEY = "string";
	public static final String INT_KEY = "integer";
	public static final String BYTES_KEY = "bytes";
	public static final String DIGEST_KEY = "digest";
	public static final String KEY_TYPE = "keytype";

	// Index Types
	public static final String NUMERIC_INDEX = "numeric";
	public static final String STRING_INDEX = "string";
	public static final String GEO2DSPHERE = "geo";

	// Collection Types
	public static final String MAPKEYS_INDEX = "mapkeys";
	public static final String MAPVALUES_INDEX = "mapvalues";
	public static final String LIST_INDEX = "list";

	// POLICY KEYS
	public static final String TOTAL_TIMEOUT = "totalTimeout";
	public static final String SOCKET_TIMEOUT = "socketTimeout";
	public static final String SLEEP_BETWEEN_RETRIES = "sleepBetweenRetries";
	public static final String SEND_KEY = "sendKey";
	public static final String REPLICA = "replica";
	public static final String PRIORITY = "priority";
	public static final String MAX_RETRIES = "maxRetries";
	public static final String LINEARIZE_READ = "linearizeRead";
	public static final String CONSISTENCY_LEVEL = "consistencyLevel";
	public static final String READ_MODE_AP = "readModeAP";
	public static final String READ_MODE_SC = "readModeSC";
	public static final String PRED_EXP = "predexp";
	public static final String FILTER_EXP = "filterexp";
	public static final String COMPRESS = "compress";

	// WRITE POLICY KEYS
	public static final String COMMIT_LEVEL = "commitLevel";
	public static final String DURABLE_DELETE = "durableDelete";
	public static final String EXPIRATION = "expiration";
	public static final String GENERATION = "generation";
	public static final String GENERATION_POLICY = "generationPolicy";
	public static final String RECORD_EXISTS_ACTION = "recordExistsAction";
	public static final String RESPOND_ALL_OPS = "respondAllOps";

	// BATCH POLICY KEYS
	public static final String ALLOW_INLINE = "allowInline";
	public static final String MAX_CONCURRENT_THREADS = "maxConcurrentThreads";
	public static final String SEND_SET_NAME = "sendSetName";
	public static final String USE_BATCH_DIRECT = "useBatchDirect";

	// SCAN POLICY KEYS
	public static final String MAX_RECORDS = "maxRecords";
	public static final String SCAN_PERCENT = "scanPercent";
	public static final String RECORDS_PER_SECOND = "recordsPerSecond";
	public static final String MAX_CONCURRENT_NODES = "maxConcurrentNodes";
	public static final String CONCURRENT_NODES = "concurrentNodes";
	public static final String INCLUDE_BIN_DATA = "includeBinData";
	public static final String FAIL_ON_CLUSTER_CHANGE = "failOnClusterChange";

	public static final String FROM_TOKEN = "from";

	// INFO POLICY KEYS
	public static final String TIMEOUT = "timeout";

	// Operation Fields
	public static final String OPERATION_FIELD = "operation";
	public static final String OPERATION_VALUES_FIELD = "opValues";

	public enum RecordKeyType {
		STRING, INTEGER, BYTES, DIGEST
	}
}
