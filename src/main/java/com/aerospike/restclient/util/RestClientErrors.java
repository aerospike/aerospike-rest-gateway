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
package com.aerospike.restclient.util;

import org.springframework.http.HttpStatus;

public class RestClientErrors {

	public static class AerospikeRestClientError extends RuntimeException {
		protected String message;

		public HttpStatus getStatusCode() {
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
		public AerospikeRestClientError(String format) {
			super(format);
			this.message = format;
		}
		public AerospikeRestClientError() {
			this("Rest Client error");
		}
		public String getErrorMessage() {
			return this.message;
		}
		private static final long serialVersionUID = 1L;

	}

	public static class InvalidRecordError extends AerospikeRestClientError {
		public HttpStatus statusCode = HttpStatus.BAD_REQUEST;
		private static final long serialVersionUID = 1L;

	}

	public static class InvalidOperationError extends AerospikeRestClientError {
		private static final long serialVersionUID = 1L;

		public InvalidOperationError(String message) {
			super(String.format(message));
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

		private static final long serialVersionUID = 1L;

	}

	public static class InvalidKeyError extends AerospikeRestClientError {
		@Override
		public HttpStatus getStatusCode() {
			return HttpStatus.BAD_REQUEST;
		}
		private static final long serialVersionUID = 1L;
		public InvalidKeyError(String message) {
			super(message);
			this.message = message;
		}
		public InvalidKeyError() {
			this("Invalid Key Structure");
		}
	}

	public static class InvalidPolicyValueError extends AerospikeRestClientError {
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

		private static final long serialVersionUID = 1L;

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

}