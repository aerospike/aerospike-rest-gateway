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

import org.springframework.http.HttpStatus;

public class RestClientErrors {

    public static class AerospikeRestClientError extends RuntimeException {
        private static final long serialVersionUID = 1L;
        protected String message;

        public HttpStatus getStatusCode() {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }

        public AerospikeRestClientError(String format) {
            super(format);
            this.message = format;
        }

        public AerospikeRestClientError() {
            this("Rest Gateway error");
        }

        public String getErrorMessage() {
            return this.message;
        }
    }

    public static class InvalidRecordError extends AerospikeRestClientError {
        private static final long serialVersionUID = 1L;

        @Override
        public HttpStatus getStatusCode() {
            return HttpStatus.BAD_REQUEST;
        }
    }

    public static class UnauthorizedError extends AerospikeRestClientError {
        private static final long serialVersionUID = 1L;

        @Override
        public HttpStatus getStatusCode() {
            return HttpStatus.UNAUTHORIZED;
        }
    }

    public static class InvalidOperationError extends AerospikeRestClientError {
        private static final long serialVersionUID = 1L;

        public InvalidOperationError(String message) {
            super(message);
            this.message = message;
        }

        public InvalidOperationError() {
            this("Invalid Key type");
        }

        @Override
        public HttpStatus getStatusCode() {
            return HttpStatus.BAD_REQUEST;
        }
    }

    public static class MalformedMsgPackError extends AerospikeRestClientError {
        private static final long serialVersionUID = 1L;

        @Override
        public HttpStatus getStatusCode() {
            return HttpStatus.BAD_REQUEST;
        }

        public MalformedMsgPackError(String format) {
            super(format);
            this.message = format;
        }

        public MalformedMsgPackError() {
            this("Invalid msgpack format");
        }
    }

    public static class InvalidKeyError extends AerospikeRestClientError {
        private static final long serialVersionUID = 1L;

        @Override
        public HttpStatus getStatusCode() {
            return HttpStatus.BAD_REQUEST;
        }

        public InvalidKeyError(String message) {
            super(message);
            this.message = message;
        }

        public InvalidKeyError() {
            this("Invalid Key Structure");
        }
    }

    public static class InvalidPolicyValueError extends AerospikeRestClientError {
        private static final long serialVersionUID = 1L;

        @Override
        public HttpStatus getStatusCode() {
            return HttpStatus.BAD_REQUEST;
        }

        public InvalidPolicyValueError() {
            this("Invalid policy value");
        }

        public InvalidPolicyValueError(String reason) {
            super(reason);
            this.message = reason;
        }
    }

    public static class InvalidQueryFilterError extends AerospikeRestClientError {
        private static final long serialVersionUID = 1L;

        @Override
        public HttpStatus getStatusCode() {
            return HttpStatus.BAD_REQUEST;
        }

        public InvalidQueryFilterError() {
            this("Invalid query filter");
        }

        public InvalidQueryFilterError(String reason) {
            super(reason);
            this.message = reason;
        }
    }

    public static class InvalidQueryError extends AerospikeRestClientError {
        private static final long serialVersionUID = 1L;

        @Override
        public HttpStatus getStatusCode() {
            return HttpStatus.BAD_REQUEST;
        }

        public InvalidQueryError() {
            this("Invalid query statement");
        }

        public InvalidQueryError(String reason) {
            super(reason);
            this.message = reason;
        }
    }

    public static class RecordNotFoundError extends AerospikeRestClientError {
        private static final long serialVersionUID = 1L;

        @Override
        public HttpStatus getStatusCode() {
            return HttpStatus.NOT_FOUND;
        }

        public RecordNotFoundError() {
            this("Record not found");
        }

        public RecordNotFoundError(String reason) {
            super(reason);
            this.message = reason;
        }
    }

    public static class InvalidDateFormat extends AerospikeRestClientError {
        private static final long serialVersionUID = 1L;

        @Override
        public HttpStatus getStatusCode() {
            return HttpStatus.BAD_REQUEST;
        }

        public InvalidDateFormat() {
            this("Invalid Date Format");
        }

        public InvalidDateFormat(String dateStr) {
            super(String.format("Invalid date format: %s", dateStr));
        }
    }

    public static class ClusterUnstableError extends AerospikeRestClientError {
        private static final long serialVersionUID = 1L;

        @Override
        public HttpStatus getStatusCode() {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }

        public ClusterUnstableError() {
            this("Unable to complete operation, cluster is unstable.");
        }

        public ClusterUnstableError(String message) {
            super(message);
        }
    }

    public static class InvalidCTXError extends AerospikeRestClientError {
        private static final long serialVersionUID = 1L;

        @Override
        public HttpStatus getStatusCode() {
            return HttpStatus.BAD_REQUEST;
        }

        public InvalidCTXError() {
            this("Invalid context");
        }

        public InvalidCTXError(String reason) {
            super(reason);
            this.message = reason;
        }
    }

    public static class InvalidGeoJSON extends AerospikeRestClientError {
        private static final long serialVersionUID = 1L;

        @Override
        public HttpStatus getStatusCode() {
            return HttpStatus.BAD_REQUEST;
        }

        public InvalidGeoJSON() {
            this("Invalid GeoJSON");
        }

        public InvalidGeoJSON(String reason) {
            super(reason);
            this.message = reason;
        }
    }

    public static class InvalidBinValue extends AerospikeRestClientError {
        private static final long serialVersionUID = 1L;

        @Override
        public HttpStatus getStatusCode() {
            return HttpStatus.BAD_REQUEST;
        }

        public InvalidBinValue() {
            this("Invalid bin value");
        }

        public InvalidBinValue(String reason) {
            super(reason);
            this.message = reason;
        }
    }
}
