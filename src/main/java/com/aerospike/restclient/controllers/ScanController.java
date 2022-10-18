/*
 * Copyright 2022 Aerospike, Inc.
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
import com.aerospike.restclient.util.annotations.DefaultRestClientAPIResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Scan Operations", description = "Read records in specified namespace, set.")
@RestController
@RequestMapping("/v1/scan")
public class ScanController {

    @Autowired
    private AerospikeScanService service;

    @Operation(summary = "Return multiple records from the server in a scan request.", operationId = "performScan")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200", description = "Scan multiple records successfully."
                    ), @ApiResponse(
                    responseCode = "400",
                    description = "Invalid parameters or request.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            ), @ApiResponse(
                    responseCode = "403",
                    description = "Not authorized to access the resource.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            ), @ApiResponse(
                    responseCode = "404",
                    description = "Namespace or set does not exist.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            )
            }
    )
    @DefaultRestClientAPIResponses
    @GetMapping(value = "/{namespace}/{set}", produces = {"application/json", "application/msgpack"})
    @ASRestClientScanPolicyQueryParams
    public RestClientScanResponse performScan(@Parameter(
            description = APIParamDescriptors.NAMESPACE_NOTES,
            required = true
    ) @PathVariable(value = "namespace") String namespace, @Parameter(
            description = APIParamDescriptors.SET_NOTES,
            required = true
    ) @PathVariable(value = "set") String set,
                                              @Parameter(hidden = true) @RequestParam MultiValueMap<String, String> requestParams,
                                              @RequestHeader(
                                                      value = "Authorization",
                                                      required = false
                                              ) String basicAuth) {

        String[] bins = RequestParamHandler.getBinsFromMap(requestParams);
        ScanPolicy policy = RequestParamHandler.getScanPolicy(requestParams.toSingleValueMap());
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);

        return service.scan(authDetails, bins, requestParams.toSingleValueMap(), policy, namespace, set);
    }

    @Operation(summary = "Return multiple records from the server in a scan request.", operationId = "performScan")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200", description = "Scan multiple records successfully."
                    ), @ApiResponse(
                    responseCode = "400",
                    description = "Invalid parameters or request.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            ), @ApiResponse(
                    responseCode = "403",
                    description = "Not authorized to access the resource.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            ), @ApiResponse(
                    responseCode = "404",
                    description = "Namespace or set does not exist.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            )
            }
    )
    @DefaultRestClientAPIResponses
    @GetMapping(value = "/{namespace}", produces = {"application/json", "application/msgpack"})
    @ASRestClientScanPolicyQueryParams
    public RestClientScanResponse performScan(@Parameter(
            description = APIParamDescriptors.NAMESPACE_NOTES,
            required = true
    ) @PathVariable(value = "namespace") String namespace,
                                              @Parameter(hidden = true) @RequestParam MultiValueMap<String, String> requestParams,
                                              @RequestHeader(
                                                      value = "Authorization",
                                                      required = false
                                              ) String basicAuth) {

        String[] bins = RequestParamHandler.getBinsFromMap(requestParams);
        ScanPolicy policy = RequestParamHandler.getScanPolicy(requestParams.toSingleValueMap());
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);

        return service.scan(authDetails, bins, requestParams.toSingleValueMap(), policy, namespace, null);
    }
}

