/*
 * Copyright 2019 Aerospike, Inc.
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
package com.aerospike.restclient.converters;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.aerospike.restclient.util.AerospikeAPIConstants.RecordKeyType;
import com.aerospike.restclient.util.RequestParamHandler;
import com.aerospike.restclient.util.RestClientErrors;

public class RequestParamHandlerTests {

	@Test
	public void getBinsFromMultiMapTest() {
		String[] expectedBins = {"bin1", "bin2", "bin3"};
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.put(AerospikeAPIConstants.RECORD_BINS, Arrays.asList(expectedBins));

		String[] actualBins = RequestParamHandler.getBinsFromMap(params);
		Assert.assertArrayEquals(expectedBins, actualBins);
	}

	@Test
	public void getKeyTypeFromMultiMapTest() {
		RecordKeyType expectedType = RecordKeyType.DIGEST;
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.put(AerospikeAPIConstants.KEY_TYPE, Arrays.asList(expectedType.toString()));

		RecordKeyType actualType = RequestParamHandler.getKeyTypeFromMap(params);
		Assert.assertEquals(expectedType, actualType);
	}

	@Test(expected=RestClientErrors.InvalidPolicyValueError.class)
	public void getInvalidKeyTypeFromMultiMapTest() {
		String fakeKeyType = "FAKE_KEY_TYPE";
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.put(AerospikeAPIConstants.KEY_TYPE, Arrays.asList(fakeKeyType));
		RequestParamHandler.getKeyTypeFromMap(params);
	}

	@Test
	public void getKeyTypeFromMapTest() {
		RecordKeyType expectedType = RecordKeyType.DIGEST;
		Map<String, String> params = new HashMap<>();
		params.put(AerospikeAPIConstants.KEY_TYPE, expectedType.toString());

		RecordKeyType actualType = RequestParamHandler.getKeyTypeFromMap(params);
		Assert.assertEquals(expectedType, actualType);
	}

	@Test(expected=RestClientErrors.InvalidPolicyValueError.class)
	public void getInvalidKeyTypeFromMapTest() {
		String fakeKeyType = "FAKE_KEY_TYPE";
		Map<String, String> params = new HashMap<>();
		params.put(AerospikeAPIConstants.KEY_TYPE, fakeKeyType);

		RequestParamHandler.getKeyTypeFromMap(params);
	}
}
