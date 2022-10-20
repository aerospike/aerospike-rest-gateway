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
package com.aerospike.restclient.util;

public final class AerospikeAPIConstants {

    public static final String AS_CLIENT_VERSION = "6.1.2";

    // Entries in the JSON representation of a record;
    public static final String RECORD_KEY = "key";
    public static final String RECORD_BINS = "recordBins";
    public static final String RECORD_GENERATION = "generation";
    public static final String RECORD_EXPIRATION = "expiration";
    public static final String RECORD_TTL = "ttl";
    public static final String JSON_PATH = "jsonPath";

    // Entries in a JSON representation of a key
    public static final String PRIMARY_KEY = "pk";
    public static final String USER_KEY = "userKey";
    public static final String NAMESPACE = "namespace";
    public static final String SET = "set";
    public static final String SETNAME = "setName";
    public static final String DIGEST = "digest";
    public static final String TYPE = "type";

    public static final String KEY_TYPE = "keytype";

    public enum RecordKeyType {
        STRING, INTEGER, BYTES, DIGEST
    }

    // Index Types
    public static final String NUMERIC_INDEX = "numeric";
    public static final String STRING_INDEX = "string";
    public static final String GEO2DSPHERE = "geo";

    // Collection Types
    public static final String MAPKEYS_INDEX = "mapkeys";
    public static final String MAPVALUES_INDEX = "mapvalues";

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
    public static final String ALLOW_INLINE_SSD = "allowInlineSSD";
    public static final String MAX_CONCURRENT_THREADS = "maxConcurrentThreads";
    public static final String RESPOND_ALL_KEYS = "respondAllKeys";

    // BATCH TYPE KEYS
    public static final String BATCH_TYPE_READ = "READ";
    public static final String BATCH_TYPE_WRITE = "WRITE";
    public static final String BATCH_TYPE_UDF = "UDF";
    public static final String BATCH_TYPE_DELETE = "DELETE";

    // SCAN POLICY KEYS (Merged into Query subsystem in 6.0)
    public static final String MAX_RECORDS = "maxRecords";
    public static final String MAX_RECORDS_DEFAULT = "10000";
    public static final String RECORDS_PER_SECOND = "recordsPerSecond";
    public static final String MAX_CONCURRENT_NODES = "maxConcurrentNodes";
    public static final String CONCURRENT_NODES = "concurrentNodes";
    public static final String INCLUDE_BIN_DATA = "includeBinData";

    public static final String FROM_TOKEN = "from";
    public static final String GET_TOKEN = "getToken";

    // QUERY POLICY KEYS
    public static final String RECORD_QUEUE_SIZE = "recordsQueueSize";
    public static final String FAIL_ON_CLUSTER_CHANGE = "failOnClusterChange";
    public static final String SHORT_QUERY = "shortQuery";

    // QUERY KEYS
    public static final String QUERY_INDEX_NAME = "indexName";
    public static final String QUERY_PARTITION_BEGIN = "begin";
    public static final String QUERY_PARTITION_COUNT = "count";

    // QUERY FILTER TYPES
    public final static class QueryFilterTypes {
        public static final String EQUAL_STRING = "EQUALS_STRING";
        public static final String EQUAL_LONG = "EQUALS_LONG";
        public static final String RANGE = "RANGE";
        public static final String CONTAINS_STRING = "CONTAINS_STRING";
        public static final String CONTAINS_LONG = "CONTAINS_LONG";
        public static final String GEOWITHIN_REGION = "GEO_WITHIN_REGION";
        public static final String GEOWITHIN_RADIUS = "GEO_WITHIN_RADIUS";
        public static final String GEOCONTAINS_POINT = "GEO_CONTAINS_POINT";
    }

    // INFO POLICY KEYS
    public static final String TIMEOUT = "timeout";

    // Operation Fields
    public static final String OPERATION_FIELD = "operation";
    public static final String OPERATION_VALUES_FIELD = "opValues";

    // CTX Types
    public static class CTX {
        public static final String LIST_INDEX = "listIndex";
        public static final String LIST_INDEX_CREATE = "listIndexCreate";
        public static final String LIST_RANK = "listRank";
        public static final String LIST_VALUE = "listValue";
        public static final String MAP_INDEX = "mapIndex";
        public static final String MAP_RANK = "mapRank";
        public static final String MAP_KEY = "mapKey";
        public static final String MAP_KEY_CREATE = "mapKeyCreate";
        public static final String MAP_VALUE = "mapValue";
    }

    // GEOJSON
    public final static class GeoJSON {
        public static class Types {
            public static final String POINT = "Point";
            public static final String POLYGON = "Polygon";
            public static final String AERO_CIRCLE = "AeroCircle";
        }

        public static class Keys {
            public static final String TYPE = "type";
            public static final String COORDINATES = "coordinates";
            public static final String GEOMETRY = "geometry";
            public static final String PROPERTIES = "properties";
        }
    }

    // SpecifiedType used for representing bytes & geojson (deprecated) in json.  TODO make a model
    public static class SpecifiedType {
        public enum Type {
            BYTE_ARRAY, GEO_JSON,
        }

        public static class Keys {
            public static final String specifiedTypeKey = "type";
            public static final String specifiedValueKey = "value";
        }
    }

}
