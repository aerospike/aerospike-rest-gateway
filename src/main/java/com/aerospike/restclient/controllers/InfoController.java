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

import com.aerospike.client.policy.InfoPolicy;
import com.aerospike.restclient.domain.RestClientError;
import com.aerospike.restclient.domain.auth.AuthDetails;
import com.aerospike.restclient.service.AerospikeInfoService;
import com.aerospike.restclient.util.HeaderHandler;
import com.aerospike.restclient.util.RequestBodyExamples;
import com.aerospike.restclient.util.ResponseExamples;
import com.aerospike.restclient.util.annotations.ASRestClientInfoPolicyQueryParams;
import com.aerospike.restclient.util.annotations.DefaultRestClientAPIResponses;
import com.aerospike.restclient.util.converters.policyconverters.InfoPolicyConverter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/info")
@Tag(name = "Info Operations", description = "Send info commands to nodes in the Aerospike cluster.")
public class InfoController {

    @Autowired
    private AerospikeInfoService service;

    @Operation(summary = "Send a list of info commands to a random node in the cluster", operationId = "infoAny")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Commands sent successfully.",
                            content = @Content(
                                    examples = @ExampleObject(
                                            name = ResponseExamples.SUCCESS_INFO_NAME,
                                            value = ResponseExamples.SUCCESS_INFO_VALUE
                                    )
                            )
                    ), @ApiResponse(
                    responseCode = "403",
                    description = "Not authorized to perform the info command.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            )
            }
    )
    @DefaultRestClientAPIResponses
    @ASRestClientInfoPolicyQueryParams
    @PostMapping(
            consumes = {"application/json", "application/msgpack"},
            produces = {"application/json", "application/msgpack"}
    )
    Map<String, String> infoAny(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "An array of info commands to send to the server. See https://www.aerospike.com/docs/reference/info/ for a list of valid commands.",
            required = true,
            content = @Content(
                    examples = @ExampleObject(
                            name = RequestBodyExamples.REQUESTS_INFO_NAME,
                            value = RequestBodyExamples.REQUESTS_INFO_VALUE
                    )
            )
    ) @RequestBody String[] requests, @Parameter(hidden = true) @RequestParam Map<String, String> infoMap,
                                @RequestHeader(value = "Authorization", required = false) String basicAuth) {

        InfoPolicy policy = InfoPolicyConverter.policyFromMap(infoMap);
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);

        return service.infoAny(authDetails, requests, policy);
    }

    @Operation(summary = "Send a list of info commands to a specific node in the cluster.", operationId = "infoNode")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Commands sent successfully.",
                            content = @Content(
                                    examples = @ExampleObject(
                                            name = ResponseExamples.SUCCESS_INFO_NAME,
                                            value = ResponseExamples.SUCCESS_INFO_VALUE
                                    )
                            )
                    ), @ApiResponse(
                    responseCode = "403",
                    description = "Not authorized to perform the info command",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            ), @ApiResponse(
                    responseCode = "404",
                    description = "The specified Node does not exist.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            )
            }
    )
    @DefaultRestClientAPIResponses
    @PostMapping(
            value = "/{node}",
            consumes = {"application/json", "application/msgpack"},
            produces = {"application/json", "application/msgpack"}
    )
    Map<String, String> infoNode(@Parameter(
            name = "node",
            description = "The node ID for the node which will receive the info commands.",
            required = true
    ) @PathVariable(value = "node") String node, @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "An array of info commands to send to the server. See https://www.aerospike.com/docs/reference/info/ for a list of valid commands.",
            required = true,
            content = @Content(
                    examples = @ExampleObject(
                            name = RequestBodyExamples.REQUESTS_INFO_NAME,
                            value = RequestBodyExamples.REQUESTS_INFO_VALUE
                    )
            )
    ) @RequestBody String[] requests, @Parameter(hidden = true) @RequestParam Map<String, String> infoMap,
                                 @RequestHeader(value = "Authorization", required = false) String basicAuth) {

        InfoPolicy policy = InfoPolicyConverter.policyFromMap(infoMap);
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);

        return service.infoNodeName(authDetails, node, requests, policy);
    }
}
