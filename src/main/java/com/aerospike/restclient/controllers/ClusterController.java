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

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.aerospike.restclient.domain.swaggermodels.RestClientClusterInfoResponse;
import com.aerospike.restclient.service.AerospikeClusterService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags="Cluster information operations", description="Retrieve basic information about the Aerospike cluster.")
@RestController
@RequestMapping("/v1/cluster")
class ClusterController {
	@Autowired
	private AerospikeClusterService service;

	@ApiOperation(value="Return an object containing information about the Aerospike cluster.",
			response=RestClientClusterInfoResponse.class, nickname="getClusterInfo")
	@RequestMapping(method=RequestMethod.GET, produces={"application/json", "application/msgpack"})
	public Map<String, Object>getClusterInfo() {
		return service.getClusterInfo();
	}
}