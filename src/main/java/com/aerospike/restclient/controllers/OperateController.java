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
import com.aerospike.restclient.domain.RestClientOperation;
import com.aerospike.restclient.domain.RestClientRecord;
import com.aerospike.restclient.domain.auth.AuthDetails;
import com.aerospike.restclient.service.AerospikeOperateService;
import com.aerospike.restclient.util.APIParamDescriptors;
import com.aerospike.restclient.util.AerospikeAPIConstants.RecordKeyType;
import com.aerospike.restclient.util.HeaderHandler;
import com.aerospike.restclient.util.RequestParamHandler;
import com.aerospike.restclient.util.annotations.ASRestClientWritePolicyQueryParams;
import com.aerospike.restclient.util.deserializers.MsgPackOperationsParser;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Api(tags = "Operate operations", description = "Perform multiple operations atomically on a single record.")
@RestController
@RequestMapping("/v1/operate")
public class OperateController {

    public static final String OPERATE_NOTES = "Perform multiple operations atomically on the specified record.";
    public static final String OPERATIONS_PARAM_NOTES = "An array of operation objects specifying the operations to perform on the record";

    @Autowired
    private AerospikeOperateService service;

    @RequestMapping(method = RequestMethod.POST, value = "/{namespace}/{set}/{key}",
            consumes = "application/json",
            produces = {"application/json", "application/msgpack"}
    )
    @ApiOperation(value = OPERATE_NOTES, consumes = "application/json, application/msgpack", nickname = "operateNamespaceSetKey")
    @ASRestClientWritePolicyQueryParams
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 404, response = RestClientError.class, message = "Namespace or record does not exist",
                    examples = @Example(value = {@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
            @ApiResponse(code = 403, response = RestClientError.class, message = "Not authorized to access the resource",
                    examples = @Example(value = {@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
            @ApiResponse(code = 400, response = RestClientError.class, message = "Invalid parameters or request",
                    examples = @Example(value = {@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
            @ApiResponse(code = 409, response = RestClientError.class, message = "Generation conflict",
                    examples = @Example(value = {@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' ")}))

    })
    public RestClientRecord operateNamespaceSetKey(
            @ApiParam(value = APIParamDescriptors.NAMESPACE_NOTES, required = true) @PathVariable(value = "namespace") String namespace,
            @ApiParam(value = APIParamDescriptors.SET_NOTES, required = true) @PathVariable(value = "set") String set,
            @ApiParam(value = APIParamDescriptors.USERKEY_NOTES, required = true) @PathVariable(value = "key") String key,
            @ApiParam(value = OPERATIONS_PARAM_NOTES, required = true)
            @RequestBody List<RestClientOperation> operations,
            @ApiIgnore @RequestParam Map<String, String> requestParams,
            @RequestHeader(value = "Authorization", required = false) String basicAuth) {

        WritePolicy policy = RequestParamHandler.getWritePolicy(requestParams);
        RecordKeyType keyType = RequestParamHandler.getKeyTypeFromMap(requestParams);
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);

        return service.operateRCOps(authDetails, namespace, set, key, operations, keyType, policy);
    }

    @ApiIgnore
    @RequestMapping(method = RequestMethod.POST, value = "/{namespace}/{set}/{key}",
            consumes = "application/msgpack",
            produces = {"application/json", "application/msgpack"}
    )
    @ResponseStatus(HttpStatus.OK)
    public RestClientRecord operateNamespaceSetKeyMP(
            @PathVariable(value = "namespace") String namespace,
            @PathVariable(value = "set") String set,
            @PathVariable(value = "key") String key,
            InputStream dataStream,
            @RequestParam Map<String, String> requestParams,
            @RequestHeader(value = "Authorization", required = false) String basicAuth) {

        WritePolicy policy = RequestParamHandler.getWritePolicy(requestParams);
        RecordKeyType keyType = RequestParamHandler.getKeyTypeFromMap(requestParams);
        List<RestClientOperation> operations = operationsFromIstream(dataStream);
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);

        return service.operateRCOps(authDetails, namespace, set, key, operations, keyType, policy);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{namespace}/{key}",
            consumes = "application/json",
            produces = {"application/json", "application/msgpack"})
    @ApiOperation(value = OPERATE_NOTES, consumes = "application/json, application/msgpack", nickname = "operateNamespaceKey")
    @ASRestClientWritePolicyQueryParams
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 404, response = RestClientError.class, message = "Namespace or record does not exist",
                    examples = @Example(value = {@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
            @ApiResponse(code = 403, response = RestClientError.class, message = "Not authorized to access the resource",
                    examples = @Example(value = {@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
            @ApiResponse(code = 400, response = RestClientError.class, message = "Invalid parameters or request",
                    examples = @Example(value = {@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
            @ApiResponse(code = 409, response = RestClientError.class, message = "Generation conflict",
                    examples = @Example(value = {@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' ")}))

    })
    public RestClientRecord operateNamespaceKey(
            @ApiParam(value = APIParamDescriptors.NAMESPACE_NOTES, required = true) @PathVariable(value = "namespace") String namespace,
            @ApiParam(value = APIParamDescriptors.USERKEY_NOTES, required = true) @PathVariable(value = "key") String key,
            @ApiParam(value = OPERATIONS_PARAM_NOTES, required = true)
            @RequestBody List<RestClientOperation> operations,
            @ApiIgnore @RequestParam Map<String, String> requestParams,
            @RequestHeader(value = "Authorization", required = false) String basicAuth) {

        WritePolicy policy = RequestParamHandler.getWritePolicy(requestParams);
        RecordKeyType keyType = RequestParamHandler.getKeyTypeFromMap(requestParams);
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);

        return service.operateRCOps(authDetails, namespace, null, key, operations, keyType, policy);
    }

    @ApiIgnore
    @RequestMapping(method = RequestMethod.POST, value = "/{namespace}/{key}",
            consumes = "application/msgpack",
            produces = {"application/json", "application/msgpack"})
    @ResponseStatus(HttpStatus.OK)
    public RestClientRecord operateNamespaceKeyMP(
            @PathVariable(value = "namespace") String namespace,
            @PathVariable(value = "key") String key,
            InputStream dataStream,
            @RequestParam Map<String, String> requestParams,
            @RequestHeader(value = "Authorization", required = false) String basicAuth) {

        WritePolicy policy = RequestParamHandler.getWritePolicy(requestParams);
        RecordKeyType keyType = RequestParamHandler.getKeyTypeFromMap(requestParams);
        List<RestClientOperation> operations = operationsFromIstream(dataStream);
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);

        return service.operateRCOps(authDetails, namespace, null, key, operations, keyType, policy);
    }

    private List<RestClientOperation> operationsFromIstream(InputStream dataStream) {
        MsgPackOperationsParser parser = new MsgPackOperationsParser(dataStream);
        return parser.parseOperations();
    }
}
