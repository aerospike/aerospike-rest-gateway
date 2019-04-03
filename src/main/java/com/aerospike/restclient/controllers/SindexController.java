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
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.aerospike.client.policy.InfoPolicy;
import com.aerospike.client.policy.Policy;
import com.aerospike.restclient.domain.RestClientError;
import com.aerospike.restclient.domain.RestClientIndex;
import com.aerospike.restclient.service.AerospikeSindexService;
import com.aerospike.restclient.util.annotations.ASRestClientInfoPolicyQueryParams;
import com.aerospike.restclient.util.converters.policyconverters.InfoPolicyConverter;
import com.aerospike.restclient.util.converters.policyconverters.PolicyConverter;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags="Secondary Index methods", description="Manage secondary indices.")
@RestController
@RequestMapping("/v1/index")
class SindexController {

	@Autowired private AerospikeSindexService service;

	@ApiImplicitParams(value= {
			@ApiImplicitParam(name="namespace", required=false, paramType="query",
					value="If specified, the list of returned indices will only contain entries from this namespace.",
					dataType="String")
	})
	@ApiOperation(value="Return information about multiple secondary indices.", nickname="indexInformation")
	@RequestMapping(method=RequestMethod.GET, produces={"application/json", "application/msgpack"})
	@ApiResponses(value= {
			@ApiResponse(code=403, response=RestClientError.class, message = "Not authorized to access the resource.",
					examples= @Example(value = {@ExampleProperty(mediaType="Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
			@ApiResponse(code=404, response=RestClientError.class, message = "Specified namespace not found",
			examples= @Example(value = {@ExampleProperty(mediaType="Example json", value = "{'inDoubt': false, 'message': 'A message' ")}))
	})
	List<RestClientIndex> indexInformation(
			@ApiIgnore @RequestParam Map<String, String>requestParams) {

		String namespace = requestParams.get("namespace");
		InfoPolicy policy = InfoPolicyConverter.policyFromMap(requestParams);
		return service.getIndexList(namespace, policy);
	}

	@ApiOperation(value="Create a secondary index.", nickname="createIndex")
	@ResponseStatus(value=HttpStatus.ACCEPTED)
	@RequestMapping(method=RequestMethod.POST, consumes={"application/json", "application/msgpack"}, produces={"application/json", "application/msgpack"})
	@ApiResponses(value= {
			@ApiResponse(code=403, response=RestClientError.class, message = "Not authorized to access the resource.",
					examples= @Example(value = {@ExampleProperty(mediaType="Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
			@ApiResponse(code=400, response=RestClientError.class, message = "Invalid index creation parameters.",
			examples= @Example(value = {@ExampleProperty(mediaType="Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
			@ApiResponse(code=409, response=RestClientError.class, message = "Index with the same name already exists, or equivalent index exists.",
			examples= @Example(value = {@ExampleProperty(mediaType="Example json", value = "{'inDoubt': false, 'message': 'A message' ")}))
	})
	void createIndex(
			@RequestBody RestClientIndex indexModel,
			@ApiIgnore @RequestParam Map<String, String>policyMap) {

		Policy policy = PolicyConverter.policyFromMap(policyMap);
		service.createIndex(indexModel, policy);
	}

	@ApiOperation(value="Remove a secondary Index", nickname="dropIndex")
	@ResponseStatus(value=HttpStatus.ACCEPTED)
	@RequestMapping(method=RequestMethod.DELETE, value="/{namespace}/{name}", produces={"application/json", "application/msgpack"})
	@ApiResponses(value= {
			@ApiResponse(code=403, response=RestClientError.class, message = "Not authorized to access the resource.",
					examples= @Example(value = {@ExampleProperty(mediaType="Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
			@ApiResponse(code=404, response=RestClientError.class, message = "Specified Index does not exist.",
			examples= @Example(value = {@ExampleProperty(mediaType="Example json", value = "{'inDoubt': false, 'message': 'A message' ")}))
	})
	void dropIndex(
			@ApiParam(required=true, value="The namespace containing the index") @PathVariable(value="namespace")String namespace,
			@ApiParam(required=true, value="The name of the index") @PathVariable(value="name")String name,
			@ApiIgnore @RequestParam Map<String, String>policyMap) {

		Policy policy = PolicyConverter.policyFromMap(policyMap);
		service.dropIndex(namespace, name, policy);
	}

	@ApiOperation(value="Get Information about a single secondary index.", nickname="getIndexStats")
	@ASRestClientInfoPolicyQueryParams
	@RequestMapping(method=RequestMethod.GET, value="/{namespace}/{name}", produces={"application/json", "application/msgpack"})
	@ApiResponses(value= {
			@ApiResponse(code=403, response=RestClientError.class, message = "Not authorized to access the resource.",
					examples= @Example(value = {@ExampleProperty(mediaType="Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
			@ApiResponse(code=404, response=RestClientError.class, message = "Specified Index does not exist.",
			examples= @Example(value = {@ExampleProperty(mediaType="Example json", value = "{'inDoubt': false, 'message': 'A message' ")}))
	})
	Map<String, String> getIndexStats(
			@ApiParam(required=true, value="The namespace containing the index") @PathVariable(value="namespace")String namespace,
			@ApiParam(required=true, value="The name of the index") @PathVariable(value="name")String name,
			@ApiIgnore @RequestParam Map<String, String>requestParams) {

		InfoPolicy policy = InfoPolicyConverter.policyFromMap(requestParams);
		return service.indexStats(namespace, name, policy);
	}
}