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

public final class ResponseExamples {

    // admin
    public static final String USER_EXAMPLE_NAME = "User response example";
    public static final String USER_EXAMPLE_VALUE = "  {\n" + "    \"name\": \"user1\",\n" + "    \"roles\": [\n" + "      \"sys-admin\"\n" + "    ],\n" + "    \"readInfo\": [\n" + "      0\n" + "    ],\n" + "    \"writeInfo\": [\n" + "      0\n" + "    ],\n" + "    \"connsInUse\": 0\n" + "  }\n";
    public static final String USERS_EXAMPLE_NAME = "Users response example";
    public static final String USERS_EXAMPLE_VALUE = "[\n" + "  {\n" + "    \"name\": \"user1\",\n" + "    \"roles\": [\n" + "      \"sys-admin\"\n" + "    ],\n" + "    \"readInfo\": [\n" + "      0\n" + "    ],\n" + "    \"writeInfo\": [\n" + "      0\n" + "    ],\n" + "    \"connsInUse\": 0\n" + "  }\n" + "]";

    // cluster
    public static final String CLUSTER_INFO_NAME = "Cluster info response example";
    public static final String CLUSTER_INFO_VALUE = "{\n" + "   \"nodes\": [\n" + "      {\n" + "         \"name\": \"BB9020011AC4202\"\n" + "      }\n" + "   ],\n" + "   \"namespaces\": [\n" + "      {\n" + "         \"sets\": [\n" + "            {\n" + "               \"objectCount\": 1,\n" + "               \"name\": \"junit\"\n" + "            },\n" + "            {\n" + "               \"objectCount\": 0,\n" + "               \"name\": \"msgpack\"\n" + "            },\n" + "            {\n" + "               \"objectCount\": 0,\n" + "               \"name\": \"executeSet\"\n" + "            },\n" + "            {\n" + "               \"objectCount\": 0,\n" + "               \"name\": \"scanSet\"\n" + "            },\n" + "            {\n" + "               \"objectCount\": 0,\n" + "               \"name\": \"auth\"\n" + "            },\n" + "            {\n" + "               \"objectCount\": 0,\n" + "               \"name\": \"idxDemo\"\n" + "            },\n" + "            {\n" + "               \"objectCount\": 0,\n" + "               \"name\": \"truncate\"\n" + "            },\n" + "            {\n" + "               \"objectCount\": 0,\n" + "               \"name\": \"otherset\"\n" + "            }\n" + "         ],\n" + "         \"name\": \"test\"\n" + "      }\n" + "   ]\n" + "}";

    // document api
    public static final String GET_DOCUMENT_OBJECT_NAME = "Get document object response example";
    public static final String GET_DOCUMENT_OBJECT_VALUE = "{\n" + "   \"docBin2\": [\n" + "      \"A1\",\n" + "      \"B1\",\n" + "      \"C1\",\n" + "      \"D1\"\n" + "   ],\n" + "   \"docBin1\": [\n" + "      \"A1\",\n" + "      \"B1\",\n" + "      \"C1\",\n" + "      \"D1\"\n" + "   ]\n" + "}";

    // info
    public static final String SUCCESS_INFO_NAME = "Info response example";
    public static final String SUCCESS_INFO_VALUE = "{\"edition\": \"Aerospike Enterprise Edition\", \"name\":\"BB9DE9B1B270008\"}";

    // secondary indexes
    public static final String SINDEX_STATS_NAME = "Secondary index response example";
    public static final String SINDEX_STATS_VALUE = "{\n" + "   \"loadtime\": 0,\n" + "   \"delete_success\": 0,\n" + "   \"keys\": 0,\n" + "   \"nbtr_memory_used\": 0,\n" + "   \"delete_error\": 0,\n" + "   \"load_pct\": 100,\n" + "   \"stat_gc_recs\": 0,\n" + "   \"query_basic_abort\": 0,\n" + "   \"histogram\": false,\n" + "   \"entries\": 0,\n" + "   \"query_basic_error\": 0,\n" + "   \"query_basic_complete\": 0,\n" + "   \"ibtr_memory_used\": 18432,\n" + "   \"write_error\": 0,\n" + "   \"query_basic_avg_rec_count\": 0,\n" + "   \"write_success\": 0\n" + "}";
}
