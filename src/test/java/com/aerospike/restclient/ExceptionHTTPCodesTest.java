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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.aerospike.client.AerospikeException;
import com.aerospike.client.ResultCode;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExceptionHTTPCodesTest {

	RestClientErrorHandler handler = new RestClientErrorHandler();
	HttpStatus expectedStatus;
	AerospikeException testException;

	private static Stream<Arguments> params(){
		return Stream.of(
				Arguments.of(ResultCode.KEY_NOT_FOUND_ERROR, HttpStatus.NOT_FOUND),
				Arguments.of(ResultCode.INDEX_NOTFOUND, HttpStatus.NOT_FOUND),
				Arguments.of(ResultCode.INVALID_ROLE, HttpStatus.NOT_FOUND),
				Arguments.of(ResultCode.GENERATION_ERROR, HttpStatus.CONFLICT),
				Arguments.of(ResultCode.ROLE_ALREADY_EXISTS, HttpStatus.CONFLICT),
				Arguments.of(ResultCode.KEY_EXISTS_ERROR, HttpStatus.CONFLICT),
				Arguments.of(ResultCode.INDEX_ALREADY_EXISTS, HttpStatus.CONFLICT),
				Arguments.of(ResultCode.USER_ALREADY_EXISTS, HttpStatus.CONFLICT),
				Arguments.of(ResultCode.BIN_NAME_TOO_LONG, HttpStatus.BAD_REQUEST),
				Arguments.of(ResultCode.PARAMETER_ERROR, HttpStatus.BAD_REQUEST),
				Arguments.of(ResultCode.INVALID_COMMAND, HttpStatus.BAD_REQUEST),
				Arguments.of(ResultCode.INVALID_FIELD, HttpStatus.BAD_REQUEST),
				Arguments.of(ResultCode.BIN_TYPE_ERROR, HttpStatus.BAD_REQUEST),
				Arguments.of(ResultCode.INDEX_NAME_MAXLEN, HttpStatus.BAD_REQUEST),
				Arguments.of(ResultCode.INVALID_PRIVILEGE, HttpStatus.BAD_REQUEST),
				Arguments.of(ResultCode.ROLE_VIOLATION, HttpStatus.FORBIDDEN),
				Arguments.of(ResultCode.INVALID_USER, HttpStatus.UNAUTHORIZED),
				Arguments.of(ResultCode.INVALID_PASSWORD, HttpStatus.UNAUTHORIZED),
				Arguments.of(ResultCode.EXPIRED_PASSWORD, HttpStatus.UNAUTHORIZED),
				Arguments.of(ResultCode.FORBIDDEN_PASSWORD, HttpStatus.UNAUTHORIZED),
				Arguments.of(ResultCode.INVALID_CREDENTIAL, HttpStatus.UNAUTHORIZED),
				Arguments.of(ResultCode.SECURITY_NOT_SUPPORTED, HttpStatus.UNAUTHORIZED),
				Arguments.of(ResultCode.SECURITY_NOT_ENABLED, HttpStatus.UNAUTHORIZED),
				Arguments.of(ResultCode.INVALID_NODE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR),
				Arguments.of(ResultCode.INVALID_NAMESPACE, HttpStatus.INTERNAL_SERVER_ERROR)
		);
	}

	@ParameterizedTest
	@MethodSource("params")
	void addParams(int exceptionCode, HttpStatus expectedStatus) {
		this.expectedStatus = expectedStatus;
		this.testException = new AerospikeException(exceptionCode);
	}

	@Test
	public void correctStatusCodeTest() {
		ResponseEntity<Object> retEntity = handler.handleAsError(testException);
		assertEquals(retEntity.getStatusCode(), expectedStatus);
	}
}
