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
package com.aerospike.restclient;

import com.aerospike.client.AerospikeException;
import com.aerospike.client.ResultCode;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RunWith(org.junit.runners.Parameterized.class)
public class ExceptionHTTPCodesTest {

    RestClientErrorHandler handler = new RestClientErrorHandler();
    HttpStatus expectedStatus;
    AerospikeException testException;

    @Parameters
    public static Object[][] params() {
        return new Object[][]{
                {ResultCode.KEY_NOT_FOUND_ERROR, HttpStatus.NOT_FOUND},
                {ResultCode.INDEX_NOTFOUND, HttpStatus.NOT_FOUND},
                {ResultCode.INVALID_ROLE, HttpStatus.NOT_FOUND},
                {ResultCode.INVALID_NAMESPACE, HttpStatus.NOT_FOUND},

                {ResultCode.GENERATION_ERROR, HttpStatus.CONFLICT},
                {ResultCode.ROLE_ALREADY_EXISTS, HttpStatus.CONFLICT},
                {ResultCode.KEY_EXISTS_ERROR, HttpStatus.CONFLICT},
                {ResultCode.INDEX_ALREADY_EXISTS, HttpStatus.CONFLICT},
                {ResultCode.USER_ALREADY_EXISTS, HttpStatus.CONFLICT},

                {ResultCode.BIN_NAME_TOO_LONG, HttpStatus.BAD_REQUEST},
                {ResultCode.PARAMETER_ERROR, HttpStatus.BAD_REQUEST},
                {ResultCode.INVALID_COMMAND, HttpStatus.BAD_REQUEST},
                {ResultCode.INVALID_FIELD, HttpStatus.BAD_REQUEST},
                {ResultCode.BIN_TYPE_ERROR, HttpStatus.BAD_REQUEST},
                {ResultCode.INDEX_NAME_MAXLEN, HttpStatus.BAD_REQUEST},
                {ResultCode.INVALID_PRIVILEGE, HttpStatus.BAD_REQUEST},
                {ResultCode.BATCH_MAX_REQUESTS_EXCEEDED, HttpStatus.BAD_REQUEST},

                {ResultCode.ROLE_VIOLATION, HttpStatus.FORBIDDEN},

                {ResultCode.INVALID_USER, HttpStatus.UNAUTHORIZED},
                {ResultCode.INVALID_PASSWORD, HttpStatus.UNAUTHORIZED},
                {ResultCode.EXPIRED_PASSWORD, HttpStatus.UNAUTHORIZED},
                {ResultCode.FORBIDDEN_PASSWORD, HttpStatus.UNAUTHORIZED},
                {ResultCode.INVALID_CREDENTIAL, HttpStatus.UNAUTHORIZED},
                {ResultCode.SECURITY_NOT_SUPPORTED, HttpStatus.UNAUTHORIZED},
                {ResultCode.SECURITY_NOT_ENABLED, HttpStatus.UNAUTHORIZED},

                {ResultCode.INVALID_NODE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR},
                {ResultCode.BATCH_QUEUES_FULL, HttpStatus.INTERNAL_SERVER_ERROR},
                {ResultCode.BATCH_FAILED, HttpStatus.INTERNAL_SERVER_ERROR},
                };
    }

    public ExceptionHTTPCodesTest(int exceptionCode, HttpStatus expectedStatus) {
        this.expectedStatus = expectedStatus;
        this.testException = new AerospikeException(exceptionCode);
    }

    @Test
    public void correctStatusCodeTest() {
        ResponseEntity<Object> retEntity = handler.handleAsError(testException);
        Assert.assertEquals(expectedStatus, retEntity.getStatusCode());
    }

}
