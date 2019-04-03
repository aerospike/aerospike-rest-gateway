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

	//Entries in the JSON representation of a record;
	public static final String RECORD_KEY = "key";
	public static final String RECORD_BINS = "bins";
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

	//Index Types
	public static final String NUMERIC_INDEX = "numeric";
	public static final String STRING_INDEX = "string";
	public static final String GEO2DSPHERE = "geo";

	//Collection Types
	public static final String MAPKEYS_INDEX = "mapkeys";
	public static final String MAPVALUES_INDEX = "mapvalues";
	public static final String LIST_INDEX = "list";

	//POLICY KEYS

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

	//WRITE POLICY KEYS
	public static final String COMMIT_LEVEL = "commitLevel";
	public static final String DURABLE_DELETE = "durableDelete";
	public static final String EXPIRATION = "expiration";
	public static final String GENERATION = "generation";
	public static final String GENERATION_POLICY = "generationPolicy";
	public static final String RECORD_EXISTS_ACTION = "recordExistsAction";
	public static final String RESPOND_ALL_OPS = "respondAllOps";

	//BATCH POLICY KEYS
	public static final String ALLOW_INLINE = "allowInline";
	public static final String MAX_CONCURRENT_THREADS = "maxConcurrentThreads";
	public static final String SEND_SET_NAME = "sendSetName";
	public static final String USE_BATCH_DIRECT = "useBatchDirect";

	//INFO POLICY KEYS
	public static final String TIMEOUT = "timeout";

	//Operation Fields
	public static final String OPERATION_FIELD = "operation";
	public static final String OPERATION_VALUES_FIELD = "opValues";

	//Operation constants. Used to decode for the /operate endpoints
	public static final String OPERATION_ADD = "ADD";
	public static final String OPERATION_APPEND = "APPEND";
	public static final String OPERATION_GET = "GET";
	public static final String OPERATION_PREPEND = "PREPEND";
	public static final String OPERATION_READ = "READ";
	public static final String OPERATION_GET_HEADER = "GET_HEADER";
	public static final String OPERATION_TOUCH = "TOUCH";
	public static final String OPERATION_PUT = "PUT";

	// List Operation Constants
	public static final String OPERATION_LIST_APPEND = "LIST_APPEND";
	public static final String OPERATION_LIST_APPEND_ITEMS = "LIST_APPEND_ITEMS";
	public static final String OPERATION_LIST_CLEAR = "LIST_CLEAR";
	public static final String OPERATION_LIST_GET = "LIST_GET";
	public static final String OPERATION_LIST_GET_BY_INDEX = "LIST_GET_BY_INDEX";
	public static final String OPERATION_LIST_GET_BY_INDEX_RANGE = "LIST_GET_BY_INDEX_RANGE";
	public static final String OPERATION_LIST_GET_BY_RANK = "LIST_GET_BY_RANK";
	public static final String OPERATION_LIST_GET_BY_RANK_RANGE = "LIST_GET_BY_RANK_RANGE";
	public static final String OPERATION_LIST_GET_BY_VALUE = "LIST_GET_BY_VALUE";
	public static final String OPERATION_LIST_GET_BY_VALUE_RANGE = "LIST_GET_BY_VALUE_RANGE";
	public static final String OPERATION_LIST_GET_BY_VALUE_LIST = "LIST_GET_BY_VALUE_LIST";
	public static final String OPERATION_LIST_GET_RANGE = "LIST_GET_RANGE";
	public static final String OPERATION_LIST_INCREMENT = "LIST_INCREMENT";
	public static final String OPERATION_LIST_INSERT = "LIST_INSERT";
	public static final String OPERATION_LIST_INSERT_ITEMS = "LIST_INSERT_ITEMS";
	public static final String OPERATION_LIST_POP = "LIST_POP";
	public static final String OPERATION_LIST_POP_RANGE = "LIST_POP_RANGE";
	public static final String OPERATION_LIST_REMOVE = "LIST_REMOVE";
	public static final String OPERATION_LIST_REMOVE_BY_INDEX = "LIST_REMOVE_BY_INDEX";
	public static final String OPERATION_LIST_REMOVE_BY_INDEX_RANGE = "LIST_REMOVE_BY_INDEX_RANGE";
	public static final String OPERATION_LIST_REMOVE_BY_RANK = "LIST_REMOVE_BY_RANK";
	public static final String OPERATION_LIST_REMOVE_BY_RANK_RANGE = "LIST_REMOVE_BY_RANK_RANGE";
	public static final String OPERATION_LIST_REMOVE_BY_VALUE = "LIST_REMOVE_BY_VALUE";
	public static final String OPERATION_LIST_REMOVE_BY_VALUE_RANGE = "LIST_REMOVE_BY_VALUE_RANGE";
	public static final String OPERATION_LIST_REMOVE_BY_VALUE_LIST = "LIST_REMOVE_BY_VALUE_LIST";
	public static final String OPERATION_LIST_REMOVE_RANGE = "LIST_REMOVE_RANGE";
	public static final String OPERATION_LIST_SET = "LIST_SET";
	public static final String OPERATION_LIST_SET_ORDER = "LIST_SET_ORDER";
	public static final String OPERATION_LIST_SIZE = "LIST_SIZE";
	public static final String OPERATION_LIST_SORT = "LIST_SORT";
	public static final String OPERATION_LIST_TRIM = "LIST_TRIM";

	// Map Operation Constants
	public static final String OPERATION_MAP_CLEAR = "MAP_CLEAR";
	public static final String OPERATION_MAP_DECREMENT = "MAP_DECREMENT";
	public static final String OPERATION_MAP_GET_BY_INDEX = "MAP_GET_BY_INDEX";
	public static final String OPERATION_MAP_GET_BY_INDEX_RANGE = "MAP_GET_BY_INDEX_RANGE";
	public static final String OPERATION_MAP_GET_BY_KEY = "MAP_GET_BY_KEY";
	public static final String OPERATION_MAP_GET_BY_KEY_LIST = "MAP_GET_BY_KEY_LIST";
	public static final String OPERATION_MAP_GET_BY_KEY_RANGE = "MAP_GET_BY_KEY_RANGE";
	public static final String OPERATION_MAP_GET_BY_RANK = "MAP_GET_BY_RANK";
	public static final String OPERATION_MAP_GET_BY_RANK_RANGE = "MAP_GET_BY_RANK_RANGE";
	public static final String OPERATION_MAP_GET_BY_VALUE = "MAP_GET_BY_VALUE";
	public static final String OPERATION_MAP_GET_BY_VALUE_RANGE = "MAP_GET_BY_VALUE_RANGE";
	public static final String OPERATION_MAP_GET_BY_VALUE_LIST = "MAP_GET_BY_VALUE_LIST";
	public static final String OPERATION_MAP_INCREMENT = "MAP_INCREMENT";
	public static final String OPERATION_MAP_PUT = "MAP_PUT";
	public static final String OPERATION_MAP_PUT_ITEMS = "MAP_PUT_ITEMS";
	public static final String OPERATION_MAP_REMOVE_BY_INDEX = "MAP_REMOVE_BY_INDEX";
	public static final String OPERATION_MAP_REMOVE_BY_INDEX_RANGE = "MAP_REMOVE_BY_INDEX_RANGE";
	public static final String OPERATION_MAP_REMOVE_BY_KEY = "MAP_REMOVE_BY_KEY";
	public static final String OPERATION_MAP_REMOVE_BY_KEY_RANGE = "MAP_REMOVE_BY_KEY_RANGE";
	public static final String OPERATION_MAP_REMOVE_BY_RANK = "MAP_REMOVE_BY_RANK";
	public static final String OPERATION_MAP_REMOVE_BY_RANK_RANGE = "MAP_REMOVE_BY_RANK_RANGE";
	public static final String OPERATION_MAP_REMOVE_BY_VALUE = "MAP_REMOVE_BY_VALUE";
	public static final String OPERATION_MAP_REMOVE_BY_VALUE_RANGE = "MAP_REMOVE_BY_VALUE_RANGE";
	public static final String OPERATION_MAP_REMOVE_BY_VALUE_LIST = "MAP_REMOVE_BY_VALUE_LIST";
	public static final String OPERATION_MAP_SET_MAP_POLICY = "MAP_SET_MAP_POLICY";
	public static final String OPERATION_MAP_SIZE = "MAP_SIZE";

	public enum RecordKeyType {
		STRING, INTEGER, BYTES, DIGEST
	}
}
