/*
 * Copyright 2020 Aerospike, Inc.
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

public class QueryParamDescriptors {

    // General Policy Notes
    public static final String POLICY_SEND_KEY_NOTES = "Send user defined key in addition to hash digest on both reads and writes.";

    public static final String POLICY_REPLICA_NOTES = "Replica algorithm used to determine the target node for a single record command.";
    public static final String POLICY_REPLICA_ALLOWABLE_VALUES = "MASTER, MASTER_PROLES, SEQUENCE, RANDOM";

    public static final String KEYTYPE_NOTES = "The Type of the userKey.";
    public static final String KEYTYPE_ALLOWABLE_VALUES = "STRING, INTEGER, BYTES, DIGEST";

    public static final String BINS_NOTES = "Optionally specify a set of bins to return when fetching a record. If omitted, all bins will be returned.";

    public static final String POLICY_READMODESC_NOTES = "Read policy for SC (strong consistency) namespaces. Determines SC read consistency options.";
    public static final String POLICY_READMODESC_ALLOWABLE_VALUES = "ALLOW_REPLICA, ALLOW_UNAVAILABLE, LINEARIZE, SESSION";

    public static final String POLICY_READMODEAP_NOTES = "Read policy for AP (availability) namespaces. How duplicates should be consulted in a read operation. "
            + "Only makes a difference during migrations and only applicable in AP mode.";
    public static final String POLICY_READMODEAP_ALLOWABLE_VALUES = "ALL, ONE";

    public static final String POLICY_TOTAL_TIMEOUT_NOTES = "Total transaction timeout in milliseconds.";

    public static final String POLICY_SOCKET_TIMEOUT_NOTES = "Socket idle timeout in milliseconds when processing a database command.";

    public static final String POLICY_SLEEP_BETWEEN_RETRIES_NOTES = "Milliseconds to sleep between retries.";

    public static final String POLICY_MAX_RETRIES_NOTES = "Maximum number of retries before aborting the current transaction.\n" +
            "The initial attempt is not counted as a retry.";

    public static final String POLICY_PRED_EXP_NOTES = "Optional Predicate Expression filter (obsolete as of Aerospike Database 5.2.0) " +
            "in infix notation DSL. If the Predicate Expression exists and evaluates to false, the transaction is ignored.";

    public static final String POLICY_FILTER_EXP_NOTES = "Optional Filter Expression (introduced in Aerospike Database 5.2.0) " +
            "in infix notation DSL.";

    public static final String POLICY_COMPRESS_NOTES = "Use zlib compression on command buffers sent to the server and responses received " +
            "from the server when the buffer size is greater than 128 bytes.";

    // Write Operation Policies
    public static final String WRITE_POLICY_EXPIRATION_NOTES = "Record expiration. Also known as ttl (time to live). Seconds record will live before being removed by the server.";
    public static final String WRITE_POLICY_GEN_NOTES = "Expected generation. Generation is the number of times a record has been modified (including creation) on the server.";

    public static final String WRITE_POLICY_DURABLE_DELETE_NOTES = "If the transaction results in a record deletion, leave a tombstone for the record.";

    public static final String WRITE_POLICY_RESPOND_ALL_OPS_NOTES = "For client operate(), return a result for every operation.";

    public static final String WRITE_POLICY_COMMIT_LEVEL_NOTES = "Desired consistency guarantee when committing a transaction on the server.";
    public static final String WRITE_POLICY_COMMIT_LEVEL_ALLOWABLE_VALUES = "COMMIT_ALL, COMMIT_MASTER";

    public static final String WRITE_POLICY_GEN_POLICY_NOTES = "Qualify how to handle record writes based on record generation.";
    public static final String WRITE_POLICY_GEN_POLICY_ALLOWABLE_VALUES = "NONE, EXPECT_GEN_EQUAL, EXPECT_GEN_GT";

    public static final String WRITE_POLICY_RECORD_EXISTS_NOTES = "How to handle the existence of the record. This is ignored for POST/PUT/UPDATE kvs methods.";
    public static final String WRITE_POLICY_RECORD_EXISTS_ALLOWABLE_VALUES = "UPDATE, UPDATE_ONLY, REPLACE, REPLACE_ONLY, CREATE_ONLY";

    // Batch Operation Policies
    public static final String BATCH_POLICY_ALLOW_INLINE_NOTES = "Allow batch to be processed immediately in the server's receiving thread when the server " +
            "deems it to be appropriate.  If false, the batch will always be processed in separate " +
            "transaction threads.  This field is only relevant for the new batch index protocol.";

    public static final String BATCH_POLICY_MAX_CONCURRENT_THREADS_NOTES = "Maximum number of concurrent synchronous batch request threads to server nodes at any point in time. " +
            "If there are 16 node/namespace combinations requested and maxConcurrentThreads is 8, " +
            "then batch requests will be made for 8 node/namespace combinations in parallel threads. " +
            "When a request completes, a new request will be issued until all 16 requests are complete.";

    public static final String BATCH_POLICY_SEND_SET_NAME_NOTES = "Send set name field to server for every key in the batch for batch index protocol.\n" +
            "This is only necessary when authentication is enabled and security roles are defined " +
            "on a per set basis.";

    // Scan Operation Policies
    public static final String SCAN_POLICY_MAX_RECORDS_NOTES = "Number of records to return.";

    public static final String SCAN_POLICY_SCAN_PERCENT_NOTES = "Percent of data to scan. Valid integer range is 1 to 100.\n" +
            "This field is supported on server versions < 4.9.\n" +
            "For server versions >= 4.9, use maxRecords.";

    public static final String SCAN_POLICY_RECORDS_PER_SECOND_NOTES = "Limit returned records per second (rps) rate for each server.\n" +
            "Do not apply rps limit if recordsPerSecond is zero.";

    public static final String SCAN_POLICY_MAX_CONCURRENT_NODES_NOTES = "Maximum number of concurrent requests to server nodes at any point in time. " +
            "If there are 16 nodes in the cluster and maxConcurrentNodes is 8, then scan requests " +
            "will be made to 8 nodes in parallel.  When a scan completes, a new scan request will " +
            "be issued until all 16 nodes have been scanned.\n" +
            "This field is only relevant when concurrentNodes is true.";

    public static final String SCAN_POLICY_CONCURRENT_NODES_NOTES = "Should scan requests be issued in parallel.";

    public static final String SCAN_POLICY_INCLUDE_BIN_DATA_NOTES = "Should bin data be retrieved. If false, only record digests (and user keys " +
            "if stored on the server) are retrieved.";

    public static final String SCAN_POLICY_FAIL_ON_CLUSTER_CHANGE_NOTES = "Terminate scan if cluster in migration state. " +
            "Only used for server versions < 4.9.";

    // Scan parameters
    public static final String SCAN_FROM_TOKEN_NOTES = "Next page token parameter.";
}
