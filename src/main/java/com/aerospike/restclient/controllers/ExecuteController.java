/*
 * Copyright 2020 Aerospike, Inc.
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

import com.aerospike.client.policy.WritePolicy;
import com.aerospike.restclient.domain.RestClientError;
import com.aerospike.restclient.domain.RestClientExecuteTask;
import com.aerospike.restclient.domain.RestClientExecuteTaskStatus;
import com.aerospike.restclient.domain.RestClientOperation;
import com.aerospike.restclient.domain.auth.AuthDetails;
import com.aerospike.restclient.service.AerospikeExecuteService;
import com.aerospike.restclient.util.APIParamDescriptors;
import com.aerospike.restclient.util.HeaderHandler;
import com.aerospike.restclient.util.RequestParamHandler;
import com.aerospike.restclient.util.annotations.ASRestClientWritePolicyQueryParams;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;

@Api(tags = "Execute Operations", description = "Execute operations in background scan/query.")
@RestController
@RequestMapping("/v1/execute")
public class ExecuteController {

    public static final String EXECUTE_NOTES = "Perform multiple operations in background scan/query.";
    public static final String EXECUTE_STATUS_NOTES = "Get status of background scan by task id.";
    public static final String TASK_ID_NOTES = "Background scan task id.";
    public static final String OPERATIONS_PARAM_NOTES = "An array of operation objects specifying the operations to perform on the record.";

    @Autowired
    private AerospikeExecuteService service;

    @RequestMapping(method = RequestMethod.POST, value = "/scan/{namespace}/{set}",
            consumes = {"application/json", "application/msgpack"},
            produces = {"application/json", "application/msgpack"}
    )
    @ApiOperation(value = EXECUTE_NOTES, consumes = "application/json, application/msgpack", nickname = "executeScanNamespaceSet")
    @ASRestClientWritePolicyQueryParams
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 404, response = RestClientError.class, message = "Namespace or set does not exist",
                    examples = @Example(value = {@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
            @ApiResponse(code = 403, response = RestClientError.class, message = "Not authorized to access the resource",
                    examples = @Example(value = {@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
            @ApiResponse(code = 400, response = RestClientError.class, message = "Invalid parameters or request",
                    examples = @Example(value = {@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
            @ApiResponse(code = 409, response = RestClientError.class, message = "Generation conflict",
                    examples = @Example(value = {@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' ")}))

    })
    public RestClientExecuteTask executeScanNamespaceSet(
            @ApiParam(value = APIParamDescriptors.NAMESPACE_NOTES, required = true) @PathVariable(value = "namespace") String namespace,
            @ApiParam(value = APIParamDescriptors.SET_NOTES, required = true) @PathVariable(value = "set") String set,
            @ApiParam(value = OPERATIONS_PARAM_NOTES, required = true)
            @RequestBody List<RestClientOperation> operations,
            @ApiIgnore @RequestParam Map<String, String> requestParams,
            @RequestHeader(value = "Authorization", required = false) String basicAuth) {

        WritePolicy policy = RequestParamHandler.getWritePolicy(requestParams);
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);

        return service.executeScan(authDetails, namespace, set, operations, policy, requestParams);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/scan/{namespace}",
            consumes = {"application/json", "application/msgpack"},
            produces = {"application/json", "application/msgpack"}
    )
    @ApiOperation(value = EXECUTE_NOTES, consumes = "application/json, application/msgpack", nickname = "executeScanNamespace")
    @ASRestClientWritePolicyQueryParams
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 404, response = RestClientError.class, message = "Namespace does not exist",
                    examples = @Example(value = {@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
            @ApiResponse(code = 403, response = RestClientError.class, message = "Not authorized to access the resource",
                    examples = @Example(value = {@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
            @ApiResponse(code = 400, response = RestClientError.class, message = "Invalid parameters or request",
                    examples = @Example(value = {@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
            @ApiResponse(code = 409, response = RestClientError.class, message = "Generation conflict",
                    examples = @Example(value = {@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' ")}))

    })
    public RestClientExecuteTask executeScanNamespace(
            @ApiParam(value = APIParamDescriptors.NAMESPACE_NOTES, required = true) @PathVariable(value = "namespace") String namespace,
            @ApiParam(value = OPERATIONS_PARAM_NOTES, required = true)
            @RequestBody List<RestClientOperation> operations,
            @ApiIgnore @RequestParam Map<String, String> requestParams,
            @RequestHeader(value = "Authorization", required = false) String basicAuth) {

        WritePolicy policy = RequestParamHandler.getWritePolicy(requestParams);
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);

        return service.executeScan(authDetails, namespace, null, operations, policy, requestParams);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/scan/status/{taskId}",
            produces = {"application/json", "application/msgpack"})
    @ApiOperation(value = EXECUTE_STATUS_NOTES, nickname = "executeScanStatus")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 403, response = RestClientError.class, message = "Not authorized to access the resource",
                    examples = @Example(value = {@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
            @ApiResponse(code = 400, response = RestClientError.class, message = "Invalid parameters or request",
                    examples = @Example(value = {@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' ")}))

    })
    public RestClientExecuteTaskStatus executeScanStatus(
            @ApiParam(value = TASK_ID_NOTES, required = true) @PathVariable(value = "taskId") String taskId,
            @RequestHeader(value = "Authorization", required = false) String basicAuth) {

        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);
        return service.queryScanStatus(authDetails, taskId);
    }
}
