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

public class APIDescriptors {

    // General Policy Notes
    public static final String POLICY_SEND_KEY_NOTES = "Send user defined key in addition to hash digest on both reads and writes.";

    public static final String POLICY_REPLICA_NOTES = "Replica algorithm used to determine the target node for a single record command.";

    public static final String RECORD_KEY_NOTES = "Record keys to perform operations on.";
    public static final String KEYTYPE_NOTES = "The Type of the userKey.";

    public static final String BINS_NOTES = "Optionally specify a set of bins to return when fetching a record in the form \"?recordBins=bin1&recordBins=bin2\", or \"?recordBins[0]=bin1&recordBins[1]=bin2\" If omitted, all bins will be returned.";

    public static final String POLICY_READMODESC_NOTES = "Read policy for SC (strong consistency) namespaces. Determines SC read consistency options.";

    public static final String POLICY_READMODEAP_NOTES = "Read policy for AP (availability) namespaces. How duplicates should be consulted in a read operation. " + "Only makes a difference during migrations and only applicable in AP mode.";

    public static final String POLICY_TOTAL_TIMEOUT_NOTES = "Total transaction timeout in milliseconds.";

    public static final String POLICY_SOCKET_TIMEOUT_NOTES = "Socket idle timeout in milliseconds when processing a database command.";

    public static final String POLICY_SLEEP_BETWEEN_RETRIES_NOTES = "Milliseconds to sleep between retries.";

    public static final String POLICY_MAX_RETRIES_NOTES = "Maximum number of retries before aborting the current transaction.\n" + "The initial attempt is not counted as a retry.";

    public static final String POLICY_FILTER_EXP_NOTES = "Optional Filter Expression (introduced in Aerospike Database 5.2.0) " + "in infix notation DSL.";

    public static final String POLICY_COMPRESS_NOTES = "Use zlib compression on command buffers sent to the server and responses received " + "from the server when the buffer size is greater than 128 bytes.";

    // Info Policy Notes
    public static final String INFO_POLICY_TIMEOUT = "Info command socket timeout in milliseconds.";

    // Write Operation Policies
    public static final String WRITE_POLICY_EXPIRATION_NOTES = "Record expiration. Also known as ttl (time to live). Seconds record will live before being removed by the server.";
    public static final String WRITE_POLICY_GEN_NOTES = "Expected generation. Generation is the number of times a record has been modified (including creation) on the server.";

    public static final String WRITE_POLICY_DURABLE_DELETE_NOTES = "If the transaction results in a record deletion, leave a tombstone for the record.";

    public static final String WRITE_POLICY_RESPOND_ALL_OPS_NOTES = "For client operate(), return a result for every operation.";

    public static final String WRITE_POLICY_COMMIT_LEVEL_NOTES = "Desired consistency guarantee when committing a transaction on the server.";

    public static final String WRITE_POLICY_GEN_POLICY_NOTES = "Qualify how to handle record writes based on record generation.";

    public static final String WRITE_POLICY_RECORD_EXISTS_NOTES = "How to handle the existence of the record. This is ignored for POST/PUT/UPDATE kvs methods.";
    // Batch Operation Policies
    public static final String BATCH_POLICY_ALLOW_INLINE_NOTES = "Allow batch to be processed immediately in the server's receiving thread when the server " + "deems it to be appropriate.  If false, the batch will always be processed in separate " + "transaction threads.  This field is only relevant for the new batch index protocol.";

    public static final String BATCH_POLICY_ALLOW_INLINE_SSD_NOTES = "Allow batch to be processed immediately in the server's receiving thread for SSD namespaces. If false, the batch will always be processed in separate service threads. Server versions < 6.0 ignore this field.\n" + "Inline processing can introduce the possibility of unfairness because the server can process the entire batch before moving onto the next command.";

    public static final String BATCH_POLICY_MAX_CONCURRENT_THREADS_NOTES = "Maximum number of concurrent synchronous batch request threads to server nodes at any point in time. " + "If there are 16 node/namespace combinations requested and maxConcurrentThreads is 8, " + "then batch requests will be made for 8 node/namespace combinations in parallel threads. " + "When a request completes, a new request will be issued until all 16 requests are complete.";

    public static final String BATCH_POLICY_RESPOND_ALL_KEYS_NOTES = "Should all batch keys be attempted regardless of errors. This field is used on both the client and server. The client handles node specific errors and the server handles key specific errors.\n" + "If true, every batch key is attempted regardless of previous key specific errors. Node specific errors such as timeouts stop keys to that node, but keys directed at other nodes will continue to be processed.\n" + "If false, the server will stop the batch to its node on most key specific errors. The exceptions are com.aerospike.client.ResultCode.KEY_NOT_FOUND_ERROR and com.aerospike.client.ResultCode.FILTERED_OUT which never stop the batch. The client will stop the entire batch on node specific errors for sync commands that are run in sequence (maxConcurrentThreads == 1). The client will not stop the entire batch for async commands or sync commands run in parallel.\n" + "Server versions < 6.0 do not support this field and treat this value as false for key specific errors.";

    // BatchWrite, BatchRead, BatchUDF, BatchDelete Polices
    public static final String BATCH_POLICY_FILTER_EXP_NOTES = "Optional expression filter. If filterExp exists and evaluates to false, the specific batch key request is not performed and RecordClientBatchRecordResponse.ResultCode is set to " + "FILTERED_OUT. " + "If exists, this filter overrides the batch parent policy filterExp " + "for the specific key in batch commands that allow a different policy per key. " + "Otherwise, this filter is ignored.";

    public static final String BATCH_POLICY_RECORD_EXIST_ACTION_NOTES = "Qualify how to handle writes where the record already exists.";
    public static final String BATCH_POLICY_COMMIT_LEVEL_NOTES = "Desired consistency guarantee when committing a transaction on the server. The default (COMMIT_ALL) indicates that the server should wait for master and all replica commits to be successful before returning success to the client.";
    public static final String BATCH_POLICY_GENERATION_POLICY_NOTES = "Qualify how to handle record deletes based on record generation. The default (NONE) indicates that the generation is not used to restrict deletes.";
    public static final String BATCH_POLICY_GENERATION_NOTES = "Expected generation. Generation is the number of times a record has been modified (including creation) on the server.\n" + "This field is only relevant when generationPolicy is not NONE.";
    public static final String BATCH_POLICY_EXPIRATION_NOTES = "Record expiration. Also known as ttl (time to live). Seconds record will live before being removed by the server.\n" + "Expiration values:\n" + "   1. -2: Do not change ttl when record is updated.\n" + "   2. -1: Never expire.\n" + "   3. 0: Default to namespace configuration variable \"default-ttl\" on the server.\n" + "   4. greater than 0: Actual ttl in seconds.";
    public static final String BATCH_POLICY_DURABLE_DELETE_NOTES = "If the transaction results in a record deletion, leave a tombstone for the record. This prevents deleted records from reappearing after node failures. Valid for Aerospike Server Enterprise Edition only.";
    public static final String BATCH_POLICY_SEND_KEY_NOTES = "Send user defined key in addition to hash digest. If true, the key will be stored with the record on the server.";
    public static final String BATCH_POLICY_READMODESC_NOTES = POLICY_READMODESC_NOTES;
    public static final String BATCH_POLICY_READMODEAP_NOTES = POLICY_READMODEAP_NOTES;

    // Scan Operation Policies
    public static final String SCAN_POLICY_MAX_RECORDS_NOTES = "Number of records to return. Required for pagination. This number is divided by the number of nodes involved in the query. The actual number of records returned may be less than maxRecords if node record counts are small and unbalanced across nodes.";

    public static final String SCAN_POLICY_RECORDS_PER_SECOND_NOTES = "Limit returned records per second (rps) rate for each server.\n" + "Do not apply rps limit if recordsPerSecond is zero.";

    public static final String SCAN_POLICY_MAX_CONCURRENT_NODES_NOTES = "Maximum number of concurrent requests to server nodes at any point in time. " + "If there are 16 nodes in the cluster and maxConcurrentNodes is 8, then scan requests " + "will be made to 8 nodes in parallel.  When a scan completes, a new scan request will " + "be issued until all 16 nodes have been scanned.\n" + "This field is only relevant when concurrentNodes is true.";

    public static final String SCAN_POLICY_CONCURRENT_NODES_NOTES = "Should scan requests be issued in parallel.";

    public static final String SCAN_POLICY_INCLUDE_BIN_DATA_NOTES = "Should bin data be retrieved. If false, only record digests (and user keys " + "if stored on the server) are retrieved.";

    // Scan parameters
    public static final String SCAN_FROM_TOKEN_NOTES = "Next page token parameter.";

    // Query Operation Policy
    public static final String QUERY_POLICY_FAIL_ON_CLUSTER_CHANGE = "Terminate query if cluster is in migration state. If the server supports partition queries or the query filter is null (scan), this field is ignored.";
    public static final String QUERY_POLICY_SHORT_QUERY = "Is query expected to return less than 100 records. If true, the server will optimize the query for a small record set. This field is ignored for aggregation queries, background queries and server versions < 6.0.";

    // Query Statement Policy
    public static final String QUERY_STMT_INDEX_NAME = "Optional query index filter. This filter is applied to the secondary index on query. Query index filters must reference a bin which has a secondary index defined.";

    // JSONPath parameters
    public static final String JSON_PATH_NOTES = "JSONPath query parameter.";
    public static final String JSON_PATH_BINS_NOTES = "Specify a set of bins to handle the JSONPath query.";

}
