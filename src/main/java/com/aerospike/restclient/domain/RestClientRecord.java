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

import java.util.Map;

import com.aerospike.client.Record;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/* Record describing what we return as a record in JSON */
@ApiModel(value="Record")
public class RestClientRecord {

	public RestClientRecord() {};

	public RestClientRecord(Record rec) {
		generation = rec.generation;
		ttl = rec.getTimeToLive();
		bins = rec.bins;
	}

	@ApiModelProperty(name="generation", value="The generation of the record.", example="2")
	public int generation;

	@ApiModelProperty(name="ttl", value="The time to live for the record, in seconds from now.", example="1000")
	public int ttl;

	@ApiModelProperty(name="bins", value="A mapping from binName to binValue",
			example="{\"bin1\": \"val1\", \"pi\": \"3.14\"}")
	public Map<String, Object>bins;
}
