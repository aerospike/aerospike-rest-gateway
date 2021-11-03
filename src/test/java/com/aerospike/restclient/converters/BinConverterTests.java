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

import java.util.HashMap;
import java.util.Map;

import com.aerospike.client.Bin;
import com.aerospike.client.Value;
import com.aerospike.client.Value.GeoJSONValue;
import com.aerospike.restclient.util.converters.BinConverter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BinConverterTests {

	@Test
	public void testStringBin() {
		singleObjectBinTest("aerospike");
	}

	@Test
	public void testLongBin() {
		singleObjectBinTest(5L);
	}

	@Test
	public void testFloatBin() {
		singleObjectBinTest(5L);
	}

	@Test
	public void testAryBin() {
		singleObjectBinTest(new String[] {"aero", "spike"});
	}

	@Test
	public void testMapBin() {
		Map<String, Object>testMap = new HashMap<>();
		testMap.put("str", "hello");
		testMap.put("float", 3.14);
		testMap.put("float", 5L);
		singleObjectBinTest(testMap);
	}

	@Test
	public void testBytesBin() {
		singleObjectBinTest(new byte[] {1,2,3});
	}

	@Test
	public void testGeoJSONBin() {
		singleObjectBinTest(new GeoJSONValue("{\"coordinates\": [-122.0, 37.5], \"type\": \"Point\"}"));
	}

	@Test
	public void testNullBin() {
		Bin testBin = new Bin("bin1", new Value.NullValue());
		Map<String, Object>binMap = new HashMap<>();
		binMap.put("bin1", null);
		Bin[] bins = BinConverter.binsFromMap(binMap);
		assertTrue(binsContain(bins, testBin));
	}

	@Test
	public void testMultipleBins() {
		Bin bin1 = new Bin("bin1", "str");
		Bin bin2 = new Bin("bin2", 3L);

		Map<String, Object>binMap = new HashMap<>();
		binMap.put(bin1.name, bin1.value.getObject());
		binMap.put(bin2.name, bin2.value.getObject());

		Bin[] bins = BinConverter.binsFromMap(binMap);
		assertEquals(2, bins.length);

		assertTrue(binsContain(bins, bin1));
		assertTrue(binsContain(bins, bin2));

	}

	private void singleObjectBinTest(Object binValue) {
		Bin testBin = new Bin("bin1", binValue);
		Map<String, Object>binMap = new HashMap<>();
		binMap.put("bin1", binValue);
		Bin[] bins = BinConverter.binsFromMap(binMap);
		assertTrue(binsContain(bins, testBin));
	}

	private boolean binsContain(Bin[] bins, Bin target) {
		for (Bin bin : bins) {
			if (bin.equals(target)) {
				return true;
			}
		}
		return false;
	}
}
