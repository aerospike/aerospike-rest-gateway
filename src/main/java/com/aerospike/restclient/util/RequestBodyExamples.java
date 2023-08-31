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
    public static final String BINS_VALUE = "{\"intBin\":5, \"strBin\":\"hello\", \"listBin\": [1,2,3], \"dictBin\": {\"one\": 1}, \"geoJsonBin\": {\"type\": \"Point\", \"coordinates\": [1.123, 4.156]}, \"byteArrayBin\": {\"type\": \"BYTE_ARRAY\", \"value\": \"YWVyb3NwaWtlCg==\"}}";
}
