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

import com.aerospike.client.policy.ScanPolicy;
import com.aerospike.restclient.domain.RestClientError;
import com.aerospike.restclient.domain.auth.AuthDetails;
import com.aerospike.restclient.domain.scanmodels.RestClientScanResponse;
import com.aerospike.restclient.service.AerospikeScanService;
import com.aerospike.restclient.util.APIParamDescriptors;
import com.aerospike.restclient.util.HeaderHandler;
import com.aerospike.restclient.util.RequestParamHandler;
import com.aerospike.restclient.util.annotations.ASRestClientScanPolicyQueryParams;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

@Api(tags = "Scan Operations", description = "Read records in specified namespace, set.")
@RestController
@RequestMapping("/v1/scan")
public class ScanController {

    @Autowired
    private AerospikeScanService service;

    @ApiOperation(value = "Return multiple records from the server in a scan request.", nickname = "performScan")
    @RequestMapping(method = RequestMethod.GET, value = "/{namespace}/{set}",
            produces = {"application/json", "application/msgpack"})
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 404, response = RestClientError.class, message = "Namespace or set does not exist",
                    examples = @Example(value = {@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
            @ApiResponse(code = 403, response = RestClientError.class, message = "Not authorized to access the resource",
                    examples = @Example(value = {@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
            @ApiResponse(code = 400, response = RestClientError.class, message = "Invalid parameters or request",
                    examples = @Example(value = {@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' ")}))

    })
    @ASRestClientScanPolicyQueryParams
    public RestClientScanResponse performScan(
            @ApiParam(value = APIParamDescriptors.NAMESPACE_NOTES, required = true) @PathVariable(value = "namespace") String namespace,
            @ApiParam(value = APIParamDescriptors.SET_NOTES, required = true) @PathVariable(value = "set") String set,
            @ApiIgnore @RequestParam Map<String, String> requestParams,
            @RequestHeader(value = "Authorization", required = false) String basicAuth) {

        ScanPolicy policy = RequestParamHandler.getScanPolicy(requestParams);
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);

        return service.scan(authDetails, requestParams, policy, namespace, set);
    }

    @ApiOperation(value = "Return multiple records from the server in a scan request.", nickname = "performScan")
    @RequestMapping(method = RequestMethod.GET, value = "/{namespace}",
            produces = {"application/json", "application/msgpack"})
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 404, response = RestClientError.class, message = "Namespace does not exist",
                    examples = @Example(value = {@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
            @ApiResponse(code = 403, response = RestClientError.class, message = "Not authorized to access the resource",
                    examples = @Example(value = {@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
            @ApiResponse(code = 400, response = RestClientError.class, message = "Invalid parameters or request",
                    examples = @Example(value = {@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' ")}))

    })
    @ASRestClientScanPolicyQueryParams
    public RestClientScanResponse performScan(
            @ApiParam(value = APIParamDescriptors.NAMESPACE_NOTES, required = true) @PathVariable(value = "namespace") String namespace,
            @ApiIgnore @RequestParam Map<String, String> requestParams,
            @RequestHeader(value = "Authorization", required = false) String basicAuth) {

        ScanPolicy policy = RequestParamHandler.getScanPolicy(requestParams);
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);

        return service.scan(authDetails, requestParams, policy, namespace, null);
    }
}
