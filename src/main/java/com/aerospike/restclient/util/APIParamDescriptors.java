package com.aerospike.restclient.util;

public class APIParamDescriptors {
	public static final String NAMESPACE_NOTES = "Namespace for the record; equivalent to database name.";
	public static final String SET_NOTES = "Set for the record; equivalent to database table.";
	public static final String USERKEY_NOTES = "Userkey for the record.";
	public static final String STORE_BINS_NOTES = "Bins to be stored in the record. This is a mapping from a string bin name to a value. "
			+ "Value can be a String, integer, floating point number, list, map, bytearray, or GeoJSON value. Bytearrays and GeoJSON can "
			+ "only be sent using MessagePack\n "
			+ "example: {\"bin1\":5, \"bin2\":\"hello\", \"bin3\": [1,2,3], \"bin4\": {\"one\": 1}}";

	public static final String QUERY_PARTITION_BEGIN_NOTES = "TODO";
	public static final String QUERY_PARTITION_COUNT_NOTES = "TODO";
}
