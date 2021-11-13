package com.aerospike.restclient.util;

public class RequestBodyExamples {

    // admin
    public static final String ROLES_NAME = "List of roles request body example";
    public static final String ROLES_VALUE = "[\"read-write\", \"read-write-udf\"]";

    // document api
    public static final String JSON_OBJECT_NAME = "JSON object request body example";
    public static final String JSON_OBJECT_VALUE = "str3";

    // info
    public static final String REQUESTS_INFO_NAME = "Array of info commands request body example";
    public static final String REQUESTS_INFO_VALUE = "[\"build\", \"edition\"]";

    // key value
    public static final String BINS_NAME = "Bins request body example";
    public static final String BINS_VALUE = "{\"bin1\":5, \"bin2\":\"hello\", \"bin3\": [1,2,3], \"bin4\": {\"one\": 1}}";
}
