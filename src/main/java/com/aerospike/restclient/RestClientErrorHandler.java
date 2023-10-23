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
import com.aerospike.restclient.domain.RestClientError;
import com.aerospike.restclient.util.RestClientErrors;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Locale;

@ControllerAdvice
public class RestClientErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({AerospikeException.class})
    public ResponseEntity<Object> handleAsError(AerospikeException ex) {
        return new ResponseEntity<>(new RestClientError(ex), getStatusCodeFromException(ex));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request
    ) {
        logger.warn(ex.getMessage());
        return new ResponseEntity<>(new RestClientError(ex.getMostSpecificCause().getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler({RestClientErrors.AerospikeRestClientError.class})
    public ResponseEntity<Object> handleAPIError(RestClientErrors.AerospikeRestClientError ex) {
        return new ResponseEntity<>(new RestClientError(ex), ex.getStatusCode());
    }

    @ExceptionHandler({JsonMappingException.class})
    public ResponseEntity<Object> handleJSONMappingException(JsonMappingException jme) {
        return new ResponseEntity<>(new RestClientError(jme.getMessage()), HttpStatus.BAD_REQUEST);
    }

    private HttpStatus getStatusCodeFromException(AerospikeException ex) {
        switch (ex.getResultCode()) {

            case ResultCode.KEY_NOT_FOUND_ERROR:
            case ResultCode.INDEX_NOTFOUND:
            case ResultCode.INVALID_ROLE:
            case ResultCode.INVALID_NAMESPACE:
                return HttpStatus.NOT_FOUND;

            case ResultCode.KEY_EXISTS_ERROR:
            case ResultCode.GENERATION_ERROR:
            case ResultCode.USER_ALREADY_EXISTS:
            case ResultCode.INDEX_ALREADY_EXISTS:
            case ResultCode.ROLE_ALREADY_EXISTS:
                return HttpStatus.CONFLICT;

            case ResultCode.BIN_NAME_TOO_LONG:
            case ResultCode.INDEX_NAME_MAXLEN:
            case ResultCode.INVALID_COMMAND:
            case ResultCode.INVALID_FIELD:
            case ResultCode.BIN_TYPE_ERROR:
            case ResultCode.PARAMETER_ERROR:
            case ResultCode.INVALID_PRIVILEGE:
            case ResultCode.BATCH_MAX_REQUESTS_EXCEEDED:
                return HttpStatus.BAD_REQUEST;

            case ResultCode.ROLE_VIOLATION:
            case ResultCode.ALWAYS_FORBIDDEN:
            case ResultCode.FAIL_FORBIDDEN:
            case ResultCode.BATCH_DISABLED:
                return HttpStatus.FORBIDDEN;

            case ResultCode.INVALID_USER:
            case ResultCode.INVALID_PASSWORD:
            case ResultCode.EXPIRED_PASSWORD:
            case ResultCode.FORBIDDEN_PASSWORD:
            case ResultCode.INVALID_CREDENTIAL:
            case ResultCode.SECURITY_NOT_SUPPORTED:
            case ResultCode.SECURITY_NOT_ENABLED:
                return HttpStatus.UNAUTHORIZED;

            case ResultCode.INVALID_NODE_ERROR:
            case ResultCode.BATCH_QUEUES_FULL:
            case ResultCode.BATCH_FAILED:
                return HttpStatus.INTERNAL_SERVER_ERROR;

            case ResultCode.MAX_RETRIES_EXCEEDED:
                if (ex.getMessage().toLowerCase(Locale.ENGLISH).contains("index not found")) {
                    return HttpStatus.NOT_FOUND;
                }

            default:
                return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
