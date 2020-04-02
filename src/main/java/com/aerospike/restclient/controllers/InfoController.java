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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aerospike.client.policy.InfoPolicy;
import com.aerospike.restclient.domain.RestClientError;
import com.aerospike.restclient.service.AerospikeInfoService;
import com.aerospike.restclient.util.converters.policyconverters.InfoPolicyConverter;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/v1/info")
@Api(tags="Info Operations", description="Send info commands to nodes in the Aerospike cluster.")
public class InfoController {

	@Autowired private AerospikeInfoService service;

	@ApiOperation(value="Send a list of info commands to a random node in the cluster", nickname="infoAny")
	@RequestMapping(method=RequestMethod.POST, consumes={"application/json", "application/msgpack"}, produces={"application/json", "application/msgpack"})
	@ApiResponses(value= {
			@ApiResponse(code=200, examples=@Example(value =
				{ @ExampleProperty(mediaType="Example json", value = "{\"edition\": \"Aerospike Enterprise Edition\", \"name\":\"BB9DE9B1B270008}") }), message = "Commands sent succesfully."),
			@ApiResponse(code=403, response=RestClientError.class, message = "Not authorized to perform the info command",
			examples= @Example(value = {@ExampleProperty(mediaType="Example json", value = "{'inDoubt': false, 'message': 'A message' ")}))})
	Map<String, String> infoAny(
			@ApiParam(
					required=true,
					name="infoRequests",
					examples=@Example(value =
				{ @ExampleProperty(mediaType="application/json", value = "['build', 'edition']") }),
					value="An array of info commands to send to the server. See https://www.aerospike.com/docs/reference/info/ for a list of valid commands.") @RequestBody String[] requests,
			@ApiIgnore @RequestParam Map<String, String>infoMap) {

		InfoPolicy policy = InfoPolicyConverter.policyFromMap(infoMap);
		return service.infoAny(requests, policy);
	}

	@ApiOperation(value="Send a list of info commands to a specific node in the cluster.", nickname="infoNode")
	@ApiResponses(value= {
			@ApiResponse(code=200, examples=@Example(value =
				{ @ExampleProperty(mediaType="Example json", value = "{\"edition\": \"Aerospike Enterprise Edition\", \"name\":\"BB9DE9B1B270008}") }), message = "Commands sent succesfully."),
			@ApiResponse(code=404, response=RestClientError.class, message = "The specified Node does not exist.",
			examples= @Example(value = {@ExampleProperty(mediaType="Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
			@ApiResponse(code=403, response=RestClientError.class, message = "Not authorized to perform the info command",
			examples= @Example(value = {@ExampleProperty(mediaType="Example json", value = "{'inDoubt': false, 'message': 'A message' ")}))})
	@RequestMapping(method=RequestMethod.POST, value="/{node}", consumes={"application/json", "application/msgpack"}, produces={"application/json", "application/msgpack"})
	Map<String, String> infoNode(
			@ApiParam(name="node", value="The node ID for the node which will receive the info commands.", required=true) @PathVariable(value="node")String node,
			@ApiParam(
					name="infoRequests",
					required=true,
					examples=@Example(value =
				{ @ExampleProperty(mediaType="application/json", value = "[build, edition]") }),
					value="An array of info commands to send to the server. See https://www.aerospike.com/docs/reference/info/ for a list of valid commands.") @RequestBody String[] requests,
			@ApiIgnore @RequestParam Map<String, String>infoMap) {

		InfoPolicy policy = InfoPolicyConverter.policyFromMap(infoMap);

		return service.infoNodeName(node, requests, policy);
	}

}