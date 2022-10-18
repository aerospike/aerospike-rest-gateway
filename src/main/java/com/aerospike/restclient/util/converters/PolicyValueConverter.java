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
package com.aerospike.restclient.util.converters;

import com.aerospike.client.exp.Expression;
import com.aerospike.client.policy.*;
import com.aerospike.restclient.util.RestClientErrors;
import com.aerospike.restclient.util.converters.exp.FilterExpParser;

import java.util.Base64;

public class PolicyValueConverter {
    private static final FilterExpParser filterExpParser = new FilterExpParser();

    public static ReadModeAP getReadModeAP(String readModeAP) {
        try {
            return ReadModeAP.valueOf(readModeAP);
        } catch (IllegalArgumentException e) {
            throw new RestClientErrors.InvalidPolicyValueError("Invalid AP Read Mode: " + readModeAP);
        }
    }

    public static ReadModeSC getReadModeSC(String readModeSC) {
        try {
            return ReadModeSC.valueOf(readModeSC);
        } catch (IllegalArgumentException e) {
            throw new RestClientErrors.InvalidPolicyValueError("Invalid SC Read Mode: " + readModeSC);
        }
    }

    public static Expression getFilterExp(String filterExp) {
        if (filterExp == null) {
            return null;
        }

        try {
            try {
                return filterExpParser.parse(filterExp);
            } catch (Exception e) {
                return Expression.fromBytes(Base64.getUrlDecoder().decode(filterExp));
            }
        } catch (Exception e) {
            throw new RestClientErrors.InvalidPolicyValueError("Invalid Filter Expression: " + filterExp);
        }
    }

    public static boolean getCompress(String compress) {
        return getBoolValue(compress);
    }

    public static Replica getReplica(String replica) {
        try {
            return Replica.valueOf(replica);
        } catch (IllegalArgumentException e) {
            throw new RestClientErrors.InvalidPolicyValueError("Invalid replica: " + replica);
        }
    }

    public static CommitLevel getCommitLevel(String commitLevel) {
        try {
            return CommitLevel.valueOf(commitLevel);
        } catch (IllegalArgumentException e) {
            throw new RestClientErrors.InvalidPolicyValueError("Invalid CommitLevel: " + commitLevel);
        }
    }

    public static GenerationPolicy getGenerationPolicy(String generationPolicy) {
        try {
            return GenerationPolicy.valueOf(generationPolicy);
        } catch (IllegalArgumentException e) {
            throw new RestClientErrors.InvalidPolicyValueError("Invalid GenerationPolicy: " + generationPolicy);
        }
    }

    public static RecordExistsAction getRecordExistsAction(String recordExistsAction) {
        try {
            return RecordExistsAction.valueOf(recordExistsAction);
        } catch (IllegalArgumentException e) {
            throw new RestClientErrors.InvalidPolicyValueError("Invalid recordExistsAction: " + recordExistsAction);
        }
    }

    public static boolean getBoolValue(String testValue) {
        return testValue.equalsIgnoreCase("true");
    }

    public static int getIntValue(String intString) {
        try {
            return Integer.parseInt(intString, 10);
        } catch (NumberFormatException nfe) {
            throw new RestClientErrors.InvalidPolicyValueError(String.format("Invalid integer value: %s", intString));
        }
    }

    public static long getLongValue(String intString) {
        try {
            return Long.parseLong(intString, 10);
        } catch (NumberFormatException nfe) {
            throw new RestClientErrors.InvalidPolicyValueError(String.format("Invalid long value: %s", intString));
        }
    }
}
