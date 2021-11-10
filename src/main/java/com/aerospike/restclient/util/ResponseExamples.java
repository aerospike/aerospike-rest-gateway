package com.aerospike.restclient.util;

public final class ResponseExamples {

    // admin
    public static final String USER_EXAMPLE_NAME = "User response example";
    public static final String USER_EXAMPLE_VALUE = "  {\n" +
            "    \"name\": \"user1\",\n" +
            "    \"roles\": [\n" +
            "      \"sys-admin\"\n" +
            "    ],\n" +
            "    \"readInfo\": [\n" +
            "      0\n" +
            "    ],\n" +
            "    \"writeInfo\": [\n" +
            "      0\n" +
            "    ],\n" +
            "    \"connsInUse\": 0\n" +
            "  }\n";
    public static final String USERS_EXAMPLE_NAME = "Users response example";
    public static final String USERS_EXAMPLE_VALUE = "[\n" +
            "  {\n" +
            "    \"name\": \"user1\",\n" +
            "    \"roles\": [\n" +
            "      \"sys-admin\"\n" +
            "    ],\n" +
            "    \"readInfo\": [\n" +
            "      0\n" +
            "    ],\n" +
            "    \"writeInfo\": [\n" +
            "      0\n" +
            "    ],\n" +
            "    \"connsInUse\": 0\n" +
            "  }\n" +
            "]";

    // info
    public static final String REQUESTS_PARAM_INFO_NAME = "Array of info commands example";
    public static final String REQUESTS_PARAM_INFO_VALUE = "[\"build\", \"edition\"]";
    public static final String SUCCESS_INFO_NAME = "Info response example";
    public static final String SUCCESS_INFO_VALUE = "{\"edition\": \"Aerospike Enterprise Edition\", \"name\":\"BB9DE9B1B270008\"}";
}
