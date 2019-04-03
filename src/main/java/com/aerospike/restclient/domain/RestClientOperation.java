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

import java.util.HashMap;
import java.util.Map;

import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("Operation")
public class RestClientOperation {

	public RestClientOperation() {}

	public RestClientOperation(String operation, Map<String, Object>values) {
		this.operation = operation;
		this.opValues = values;
	}

	@SuppressWarnings("unchecked")
	public RestClientOperation(Map<String, Object>opMap) {
		this.operation = (String) opMap.get(AerospikeAPIConstants.OPERATION_FIELD);
		this.opValues = (Map<String, Object>) opMap.get(AerospikeAPIConstants.OPERATION_VALUES_FIELD);
	}

	@ApiModelProperty(required=true)
	private String operation;

	@ApiModelProperty(required=true)
	private Map<String, Object> opValues;

	public String getOperation() {
		return this.operation;
	}

	public Map<String, Object>getOpValues(){
		return this.opValues;
	}

	public void setOperation(String op) {
		this.operation = op;
	}

	public void setOpValues(Map<String, Object>opVals) {
		this.opValues = opVals;
	}

	@JsonIgnore
	public Map<String, Object>toMap() {
		Map<String, Object>mapRepresentation = new HashMap<>();
		mapRepresentation.put(AerospikeAPIConstants.OPERATION_FIELD, operation);
		mapRepresentation.put(AerospikeAPIConstants.OPERATION_VALUES_FIELD, opValues);
		return mapRepresentation;
	}

}
