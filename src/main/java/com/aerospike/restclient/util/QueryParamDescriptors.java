package com.aerospike.restclient.util;

public class QueryParamDescriptors {

	// General Policy Notes
	public static final String POLICY_SEND_KEY_NOTES = "Send user defined key in addition to hash digest on both reads and writes.";
	public static final String POLICY_SEND_KEY_DEFAULT = "false";

	public static final String POLICY_REPLICA_NOTES = "Replica algorithm used to determine the target node for a single record command.";
	public static final String POLICY_REPLICA_ALLOWABLE_VALUES = "MASTER, MASTER_PROLES, SEQUENCE, RANDOM";
	public static final String POLICY_REPLICA_DEFAULT = "false";

	public static final String KEYTYPE_NOTES = "The Type of the userKey.";
	public static final String KEYTYPE_ALLOWABLE_VALUES = "STRING, INTEGER, BYTES, DIGEST";
	public static final String KEYTYPE_DEFAULT = "STRING";

	public static final String BINS_NOTES = "Optionally specify a set of bins to return when fetching a record. If omitted, all bins will be returned.";

	public static final String POLICY_READMODESC_NOTES = "Read policy for SC (strong consistency) namespaces. Determines SC read consistency options.";
	public static final String POLICY_READMODESC_ALLOWABLE_VALUES="ALLOW_REPLICA, ALLOW_UNAVAILABLE, LINEARIZE, SESSION";
	public static final String POLICY_READMODESC_DEFAULT="SESSION";

	public static final String POLICY_READMODEAP_NOTES = "Read policy for AP (availability) namespaces. How duplicates should be consulted in a read operation. "
			+ "Only makes a difference during migrations and only applicable in AP mode.";
	public static final String POLICY_READMODEAP_ALLOWABLE_VALUES="ALL, ONE";
	public static final String POLICY_READMODEAP_DEFAULT="ONE";

	// Write Operation Policies
	public static final String WRITE_POLICY_EXPIRATION_NOTES = "Record expiration. Also known as ttl (time to live). Seconds record will live before being removed by the server.";
	public static final String WRITE_POLICY_GEN_NOTES = "Expected generation. Generation is the number of times a record has been modified (including creation) on the server.";

	public static final String WRITE_POLICY_DURABLE_DELETE_NOTES = "If the transaction results in a record deletion, leave a tombstone for the record.";
	public static final String WRITE_POLICY_DURABLE_DELETE_DEFAULT = "false";

	public static final String WRITE_POLICY_COMMIT_LEVEL_NOTES = "Desired consistency guarantee when committing a transaction on the server.";
	public static final String WRITE_POLICY_COMMIT_LEVEL_DEFAULT = "COMMIT_ALL";
	public static final String WRITE_POLICY_COMMIT_LEVEL_ALLOWABLE_VALUES = "COMMIT_ALL, COMMIT_MASTER";

	public static final String WRITE_POLICY_GEN_POLICY_NOTES = "Qualify how to handle record writes based on record generation.";
	public static final String WRITE_POLICY_GEN_POLICY_DEFAULT = "NONE";
	public static final String WRITE_POLICY_GEN_POLICY_ALLOWABLE_VALUES = "NONE, EXPECT_GEN_EQUAL, EXPECT_GEN_GT";

	public static final String WRITE_POLICY_RECORD_EXISTS_NOTES = "How to handle the existence of the record. This is ignored for POST/PUT/UPDATE kvs methods.";
	public static final String WRITE_POLICY_RECORD_EXISTS_DEFAULT = "UPDATE";
	public static final String WRITE_POLICY_RECORD_EXISTS_ALLOWABLE_VALUES = "UPDATE, UPDATE_ONLY, REPLACE, REPLACE_ONLY, CREATE_ONLY";
	
}
