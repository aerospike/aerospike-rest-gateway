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
import com.aerospike.restclient.util.RestClientErrors;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.aerospike.client.AerospikeException;
import com.aerospike.client.ResultCode;

@RunWith(org.junit.runners.Parameterized.class)
public class ExceptionHTTPCodesTest {

	RestClientErrorHandler handler = new RestClientErrorHandler();
	HttpStatus expectedStatus;
	RuntimeException testException;

	@Parameters
	public static Object[][] params(){
		return new Object[][] {
			{new AerospikeException(ResultCode.KEY_NOT_FOUND_ERROR), HttpStatus.NOT_FOUND},
			{new AerospikeException(ResultCode.INDEX_NOTFOUND), HttpStatus.NOT_FOUND},
			{new AerospikeException(ResultCode.INVALID_ROLE), HttpStatus.NOT_FOUND},

			{new AerospikeException(ResultCode.GENERATION_ERROR), HttpStatus.CONFLICT},
			{new AerospikeException(ResultCode.ROLE_ALREADY_EXISTS), HttpStatus.CONFLICT},
			{new AerospikeException(ResultCode.KEY_EXISTS_ERROR), HttpStatus.CONFLICT},
			{new AerospikeException(ResultCode.INDEX_ALREADY_EXISTS), HttpStatus.CONFLICT},
			{new AerospikeException(ResultCode.USER_ALREADY_EXISTS), HttpStatus.CONFLICT},

			{new AerospikeException(ResultCode.BIN_NAME_TOO_LONG), HttpStatus.BAD_REQUEST},
			{new AerospikeException(ResultCode.PARAMETER_ERROR), HttpStatus.BAD_REQUEST},
			{new AerospikeException(ResultCode.INVALID_COMMAND), HttpStatus.BAD_REQUEST},
			{new AerospikeException(ResultCode.INVALID_FIELD), HttpStatus.BAD_REQUEST},
			{new AerospikeException(ResultCode.BIN_TYPE_ERROR), HttpStatus.BAD_REQUEST},
			{new AerospikeException(ResultCode.INDEX_NAME_MAXLEN), HttpStatus.BAD_REQUEST},
			{new AerospikeException(ResultCode.INVALID_PRIVILEGE), HttpStatus.BAD_REQUEST},

			{new AerospikeException(ResultCode.ROLE_VIOLATION), HttpStatus.FORBIDDEN},

			{new AerospikeException(ResultCode.INVALID_USER), HttpStatus.UNAUTHORIZED},
			{new AerospikeException(ResultCode.INVALID_PASSWORD), HttpStatus.UNAUTHORIZED},
			{new AerospikeException(ResultCode.EXPIRED_PASSWORD), HttpStatus.UNAUTHORIZED},
			{new AerospikeException(ResultCode.FORBIDDEN_PASSWORD), HttpStatus.UNAUTHORIZED},
			{new AerospikeException(ResultCode.INVALID_CREDENTIAL), HttpStatus.UNAUTHORIZED},
			{new AerospikeException(ResultCode.SECURITY_NOT_SUPPORTED), HttpStatus.UNAUTHORIZED},
			{new AerospikeException(ResultCode.SECURITY_NOT_ENABLED), HttpStatus.UNAUTHORIZED},

			{new AerospikeException(ResultCode.INVALID_NODE_ERROR), HttpStatus.INTERNAL_SERVER_ERROR},
			{new AerospikeException(ResultCode.INVALID_NAMESPACE), HttpStatus.INTERNAL_SERVER_ERROR},

			{new RestClientErrors.AerospikeRestClientError("error"), HttpStatus.INTERNAL_SERVER_ERROR},
			{new RestClientErrors.InvalidRecordError(), HttpStatus.BAD_REQUEST},
			{new RestClientErrors.UnauthorizedError(), HttpStatus.UNAUTHORIZED},
			{new RestClientErrors.InvalidOperationError(), HttpStatus.BAD_REQUEST},
			{new RestClientErrors.MalformedMsgPackError(), HttpStatus.BAD_REQUEST},
			{new RestClientErrors.InvalidKeyError(), HttpStatus.BAD_REQUEST},
			{new RestClientErrors.InvalidPolicyValueError(), HttpStatus.BAD_REQUEST},
			{new RestClientErrors.RecordNotFoundError(), HttpStatus.NOT_FOUND},
			{new RestClientErrors.InvalidDateFormat(), HttpStatus.BAD_REQUEST},
			{new RestClientErrors.ClusterUnstableError(), HttpStatus.INTERNAL_SERVER_ERROR},
			{new RestClientErrors.ClusterUnstableError(), HttpStatus.INTERNAL_SERVER_ERROR},
		};
	}

	public ExceptionHTTPCodesTest(RuntimeException exception, HttpStatus expectedStatus) {
		this.expectedStatus = expectedStatus;
		this.testException = exception;
	}

	@Test
	public void correctStatusCodeTest() {
		ResponseEntity<Object> retEntity;

		if (testException instanceof AerospikeException) {
			retEntity = handler.handleAsError((AerospikeException) testException);
		} else {
			retEntity = handler.handleAPIError((RestClientErrors.AerospikeRestClientError) testException);
		}

		Assert.assertEquals(retEntity.getStatusCode(), expectedStatus);
	}

}
