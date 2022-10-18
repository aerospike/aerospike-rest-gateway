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
import com.aerospike.client.policy.Policy;
import com.aerospike.restclient.domain.RestClientError;
import com.aerospike.restclient.domain.RestClientIndex;
import com.aerospike.restclient.domain.auth.AuthDetails;
import com.aerospike.restclient.service.AerospikeSIndexService;
import com.aerospike.restclient.util.HeaderHandler;
import com.aerospike.restclient.util.ResponseExamples;
import com.aerospike.restclient.util.annotations.ASRestClientInfoPolicyQueryParams;
import com.aerospike.restclient.util.annotations.DefaultRestClientAPIResponses;
import com.aerospike.restclient.util.converters.policyconverters.InfoPolicyConverter;
import com.aerospike.restclient.util.converters.policyconverters.PolicyConverter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Secondary Index methods", description = "Manage secondary indexes.")
@RestController
@RequestMapping("/v1/index")
public class SIndexController {

    @Autowired
    private AerospikeSIndexService service;

    @Parameters(
            value = {
                    @Parameter(
                            name = "namespace",
                            description = "If specified, the list of returned indices will only contain entries from this namespace.",
                            schema = @Schema(type = "string"),
                            in = ParameterIn.QUERY
                    )
            }
    )
    @Operation(summary = "Return information about multiple secondary indexes.", operationId = "indexInformation")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200", description = "Information about secondary indexes read successfully."
                    ), @ApiResponse(
                    responseCode = "403",
                    description = "Not authorized to access the resource.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            ), @ApiResponse(
                    responseCode = "404",
                    description = "Specified namespace not found.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            )
            }
    )
    @DefaultRestClientAPIResponses
    @GetMapping(produces = {"application/json", "application/msgpack"})
    public List<RestClientIndex> indexInformation(
            @Parameter(hidden = true) @RequestParam Map<String, String> requestParams,
            @RequestHeader(value = "Authorization", required = false) String basicAuth) {

        String namespace = requestParams.get("namespace");
        InfoPolicy policy = InfoPolicyConverter.policyFromMap(requestParams);
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);

        return service.getIndexList(authDetails, namespace, policy);
    }

    @Operation(summary = "Create a secondary index.", operationId = "createIndex")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "202", description = "Request to create a secondary index has been accepted."
                    ), @ApiResponse(
                    responseCode = "400",
                    description = "Invalid index creation parameters.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            ), @ApiResponse(
                    responseCode = "403",
                    description = "Not authorized to access the resource.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            ), @ApiResponse(
                    responseCode = "409",
                    description = "Index with the same name already exists, or equivalent index exists.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            )
            }
    )
    @DefaultRestClientAPIResponses
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    @PostMapping(
            consumes = {"application/json", "application/msgpack"},
            produces = {"application/json", "application/msgpack"}
    )
    public void createIndex(@RequestBody RestClientIndex indexModel,
                            @Parameter(hidden = true) @RequestParam Map<String, String> policyMap,
                            @RequestHeader(value = "Authorization", required = false) String basicAuth) {

        Policy policy = PolicyConverter.policyFromMap(policyMap);
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);

        service.createIndex(authDetails, indexModel, policy);
    }

    @Operation(summary = "Remove a secondary Index", operationId = "dropIndex")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "202", description = "Request to remove a secondary index has been accepted."
                    ), @ApiResponse(
                    responseCode = "403",
                    description = "Not authorized to access the resource.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            ), @ApiResponse(
                    responseCode = "404",
                    description = "Specified Index does not exist.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            )
            }
    )
    @DefaultRestClientAPIResponses
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    @DeleteMapping(value = "/{namespace}/{name}", produces = {"application/json", "application/msgpack"})
    public void dropIndex(@Parameter(
            required = true,
            description = "The namespace containing the index"
    ) @PathVariable(value = "namespace") String namespace, @Parameter(
            required = true,
            description = "The name of the index"
    ) @PathVariable(value = "name") String name, @Parameter(hidden = true) @RequestParam Map<String, String> policyMap,
                          @RequestHeader(value = "Authorization", required = false) String basicAuth) {

        Policy policy = PolicyConverter.policyFromMap(policyMap);
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);

        service.dropIndex(authDetails, namespace, name, policy);
    }

    @Operation(summary = "Get Information about a single secondary index.", operationId = "getIndexStats")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Information about secondary index read successfully.",
                            content = @Content(
                                    examples = @ExampleObject(
                                            name = ResponseExamples.SINDEX_STATS_NAME,
                                            value = ResponseExamples.SINDEX_STATS_VALUE
                                    )
                            )
                    ), @ApiResponse(
                    responseCode = "403",
                    description = "Not authorized to access the resource.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            ), @ApiResponse(
                    responseCode = "404",
                    description = "Specified Index does not exist.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            )
            }
    )
    @DefaultRestClientAPIResponses
    @GetMapping(value = "/{namespace}/{name}", produces = {"application/json", "application/msgpack"})
    @ASRestClientInfoPolicyQueryParams
    public Map<String, String> getIndexStats(@Parameter(
            required = true,
            description = "The namespace containing the index"
    ) @PathVariable(value = "namespace") String namespace, @Parameter(
            required = true,
            description = "The name of the index"
    ) @PathVariable(value = "name") String name,
                                             @Parameter(hidden = true) @RequestParam Map<String, String> requestParams,
                                             @RequestHeader(
                                                     value = "Authorization",
                                                     required = false
                                             ) String basicAuth) {

        InfoPolicy policy = InfoPolicyConverter.policyFromMap(requestParams);
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);

        return service.indexStats(authDetails, namespace, name, policy);
    }
}
