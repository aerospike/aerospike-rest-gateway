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
package com.aerospike.restclient.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aerospike.client.policy.BatchPolicy;
import com.aerospike.restclient.domain.RestClientBatchReadBody;
import com.aerospike.restclient.domain.RestClientBatchReadResponse;
import com.aerospike.restclient.domain.RestClientError;
import com.aerospike.restclient.service.AerospikeBatchService;
import com.aerospike.restclient.util.RequestParamHandler;
import com.aerospike.restclient.util.annotations.ASRestClientBatchPolicyQueryParams;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags="Batch Read Operations", description="Retrieve multiple records from the server.")
@RestController
@RequestMapping("/v1/batch")
public class BatchController {

	@Autowired private AerospikeBatchService service;

	@ApiOperation(value="Return multiple records from the server in a single request.", nickname="performBatchGet")
	@RequestMapping(method=RequestMethod.POST, consumes={"application/json", "application/msgpack"}, produces={"application/json", "application/msgpack"})
	@ApiResponses(value= {
			@ApiResponse(code=404, response=RestClientError.class, message = "Non existent namespace used in one or more key.",
					examples= @Example(value = {@ExampleProperty(mediaType="Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
			@ApiResponse(code=403, response=RestClientError.class, message = "Not authorized to access the resource",
			examples= @Example(value = {@ExampleProperty(mediaType="Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
			@ApiResponse(code=400, response=RestClientError.class, message = "Invalid parameters or request",
			examples= @Example(value = {@ExampleProperty(mediaType="Example json", value = "{'inDoubt': false, 'message': 'A message' ")}))

	})
	@ASRestClientBatchPolicyQueryParams
	public List<RestClientBatchReadResponse> performBatchGet(@RequestBody List<RestClientBatchReadBody> batchKeys,
			@ApiIgnore @RequestParam Map<String, String>requestParams) {

		BatchPolicy policy = RequestParamHandler.getBatchPolicy(requestParams);

		return service.batchGet(batchKeys, policy);
	}

}