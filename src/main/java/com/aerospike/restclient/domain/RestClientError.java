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
package com.aerospike.restclient.domain;

import com.aerospike.client.AerospikeException;
import com.aerospike.restclient.util.RestClientErrors.AerospikeRestClientError;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="RestClientError", description="Error object returned from unsuccesful operations.")
public class RestClientError {

	@ApiModelProperty(value="A message describing the cause of the error.", example="Error Message")
	private String message;

	@ApiModelProperty(
			value="A boolean specifying whether it was possible that the operation succeeded. This is only included if true.",
			required=false, example="false")

	private Boolean inDoubt;

	@ApiModelProperty(value="An internal error code for diagnostic purposes. This may be null",
			example="-3")
	private Integer internalErrorCode;

	public String getMessage() {
		return message;
	}

	public Boolean getInDoubt() {
		return inDoubt;
	}

	public Integer getInternalErrorCode() {
		return internalErrorCode;
	}

	public RestClientError(AerospikeException ex) {
		this.message = ex.getMessage();
		this.inDoubt = ex.getInDoubt();
		this.internalErrorCode = ex.getResultCode();
	}

	public RestClientError(AerospikeRestClientError ex) {
		this.message = ex.getErrorMessage();
		this.inDoubt = false;
		this.internalErrorCode = null;
	}

	public RestClientError(String message) {
		this.message = message;
		this.inDoubt = false;
		this.internalErrorCode = null;
	}
}
