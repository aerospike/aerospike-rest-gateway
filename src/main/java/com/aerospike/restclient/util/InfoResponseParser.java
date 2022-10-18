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

import com.aerospike.client.AerospikeException;
import com.aerospike.client.ResultCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InfoResponseParser {

    static Pattern setPattern = Pattern.compile("set=([^:]*):");
    static Pattern objectPattern = Pattern.compile(".*objects=(\\d*).*");

    // Newer versions of the server return effective_replication_factor
    static Pattern newReplicationFactorPattern = Pattern.compile(".*effective_replication_factor=(\\d*).*");
    // Depending on the server, the response will contain either replication-factor=# or repl-factor=
    static Pattern replicationFactorPattern = Pattern.compile(".*(?:replication-factor|repl-factor)=(\\d*).*");

    public static int getReplicationFactor(String response, String namespace) {
        try {
            Matcher newMatcher = newReplicationFactorPattern.matcher(response);
            if (newMatcher.matches()) {
                return Integer.parseInt(newMatcher.group(1));
            }
            Matcher replMatcher = replicationFactorPattern.matcher(response);
            if (replMatcher.matches()) {
                return Integer.parseInt(replMatcher.group(1));
            }
        } catch (NumberFormatException ignore) {
        }
        throw new AerospikeException(ResultCode.INVALID_NAMESPACE,
                String.format("Failed to fetch replication factor for namespace: %s ", namespace));
    }

    public static String[] getSetsFromResponse(String response) {
        List<String> allMatches = new ArrayList<>();
        Matcher setMatcher = setPattern.matcher(response);
        while (setMatcher.find()) {
            String m = setMatcher.group();
            allMatches.add(m.substring(4, m.length() - 1));
        }
        return allMatches.toArray(new String[0]);
    }

    public static String[] getNamespacesFromResponse(String response) {
        return response.split(";");
    }

    public static int getSetObjectCountFromResponse(String response) {
        Matcher objMatcher = objectPattern.matcher(response);
        int matches = 0;
        if (objMatcher.matches()) {
            try {
                matches = Integer.parseInt(objMatcher.group(1), 10);
            } catch (NumberFormatException e) {
                throw new RestClientErrors.AerospikeRestClientError(
                        String.format("Error parsing info response : %s", e.toString()));
            }
        }
        return matches;
    }

    public static int getNamespaceCountFromResponse(String response) {
        return InfoResponseParser.getNamespacesFromResponse(response).length;
    }

    public static List<Map<String, String>> getIndexInformation(String response) {
        List<Map<String, String>> indexMaps = new ArrayList<>();
        // Split the response into substrings for each individual index
        if (response.trim().isEmpty()) {
            return indexMaps;
        }
        if (response.trim().startsWith("ns_type=unknown")) {
            throw new AerospikeException(ResultCode.INVALID_NAMESPACE, "Namespace not found");
        }

        String[] indexInfoAry = response.trim().split(";");
        for (String indexInfoString : indexInfoAry) {
            indexMaps.add(getKeyValueMap(indexInfoString));
        }
        return indexMaps;
    }

    /* Convert a string of the type "k1=v1;k2=v2;k3=v3" into a map {k1:v1, k2:v2, k3:v3} */
    public static Map<String, String> getIndexStatInfo(String response) {
        if (response.trim().startsWith("FAIL:201")) {
            throw new AerospikeException(ResultCode.INDEX_NOTFOUND);
        }
        if (response.trim().startsWith("ns_type=unknown")) {
            throw new AerospikeException(ResultCode.INVALID_NAMESPACE, "Namespace for Index does not exist");
        }

        return getKeyValueMap(response, ";");
    }

    public static Map<String, String> getKeyValueMap(String keyValueString, String delimiter) {
        Map<String, String> indexMap = new HashMap<>();
        String[] kvPairs = keyValueString.trim().split(delimiter);

        for (String kvPair : kvPairs) {
            String[] kvAry = kvPair.split("=");
            indexMap.put(kvAry[0], kvAry[1]);
        }

        return indexMap;
    }

    /**
     * keyValueString is formatted as "k1=v1:k2=v2:k3=v3:..." Split them into a map {k1: v1, k2:v2, ...}
     */
    public static Map<String, String> getKeyValueMap(String keyValueString) {
        return getKeyValueMap(keyValueString, ":");
    }

}
