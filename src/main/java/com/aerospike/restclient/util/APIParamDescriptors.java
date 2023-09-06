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

public class APIParamDescriptors {
    public static final String NAMESPACE_NOTES = "Namespace for the record; equivalent to database name.";
    public static final String SET_NOTES = "Set for the record; equivalent to database table.";
    public static final String USERKEY_NOTES = "Userkey for the record.";
    public static final String STORE_BINS_NOTES = "Bins to be stored in the record. This is a mapping from a string bin name to a value. " + "Value can be a String, integer, floating point number, list, map, bytearray, or GeoJSON value.";

    public static final String QUERY_PARTITION_BEGIN_NOTES = "Start partition id (0 - 4095)";
    public static final String QUERY_PARTITION_COUNT_NOTES = "Number of partitions";
}
