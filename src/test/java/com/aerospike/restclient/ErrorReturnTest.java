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
package com.aerospike.restclient;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import com.aerospike.client.AerospikeException;
import com.aerospike.client.ResultCode;
import com.aerospike.restclient.domain.RestClientError;
import com.aerospike.restclient.util.RestClientErrors.AerospikeRestClientError;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ErrorReturnTest {

	RestClientErrorHandler handler = new RestClientErrorHandler();

	@Test
	public void nonInDoubtErrorTest() {
		AerospikeException notInDoubt = new AerospikeException(ResultCode.PARAMETER_ERROR, "Not In Doubt");
		ResponseEntity<Object> retEntity = handler.handleAsError(notInDoubt);
		RestClientError returnedError = (RestClientError)retEntity.getBody();
		assert returnedError != null;
		assertFalse(returnedError.getInDoubt());
	}

	@Test
	public void inDoubtErrorTest() {
		AerospikeException notInDoubt = new AerospikeException(ResultCode.TIMEOUT, true);
		ResponseEntity<Object> retEntity = handler.handleAsError(notInDoubt);
		RestClientError returnedError = (RestClientError)retEntity.getBody();
		assert returnedError != null;
		assertTrue(returnedError.getInDoubt());
	}

	@Test
	public void internalErrorCodeTest() {
		AerospikeException notInDoubt = new AerospikeException(ResultCode.TIMEOUT, true);
		ResponseEntity<Object> retEntity = handler.handleAsError(notInDoubt);
		RestClientError returnedError = (RestClientError)retEntity.getBody();
		assert returnedError != null;
		assertEquals((Integer)ResultCode.TIMEOUT, returnedError.getInternalErrorCode());
	}

	@Test
	public void noInternalErrorCodeTest() {
		AerospikeRestClientError apiError = new AerospikeRestClientError();
		ResponseEntity<Object> retEntity = handler.handleAPIError(apiError);
		RestClientError returnedError = (RestClientError)retEntity.getBody();
		assert returnedError != null;
		assertNull(returnedError.getInternalErrorCode());
	}
}
