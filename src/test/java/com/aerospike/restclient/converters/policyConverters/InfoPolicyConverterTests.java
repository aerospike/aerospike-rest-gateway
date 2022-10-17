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
package com.aerospike.restclient.converters.policyConverters;

import com.aerospike.client.policy.InfoPolicy;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.aerospike.restclient.util.converters.policyconverters.InfoPolicyConverter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class InfoPolicyConverterTests {

    Map<String, String> policyMap;

    @Before
    public void setup() {
        policyMap = new HashMap<String, String>();
    }

    @Test
    public void testTimeout() {
        policyMap.put(AerospikeAPIConstants.TIMEOUT, "333");
        InfoPolicy policy = InfoPolicyConverter.policyFromMap(policyMap);
        Assert.assertEquals(policy.timeout, 333);
    }

}
