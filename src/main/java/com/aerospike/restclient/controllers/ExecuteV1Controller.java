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

import com.aerospike.client.policy.WritePolicy;
import com.aerospike.restclient.domain.RestClientError;
import com.aerospike.restclient.domain.RestClientOperation;
import com.aerospike.restclient.domain.auth.AuthDetails;
import com.aerospike.restclient.domain.executemodels.RestClientExecuteTask;
import com.aerospike.restclient.domain.executemodels.RestClientExecuteTaskStatus;
import com.aerospike.restclient.service.AerospikeExecuteService;
import com.aerospike.restclient.util.APIParamDescriptors;
import com.aerospike.restclient.util.HeaderHandler;
import com.aerospike.restclient.util.RequestParamHandler;
import com.aerospike.restclient.util.annotations.ASRestClientWritePolicyQueryParams;
import com.aerospike.restclient.util.annotations.DefaultRestClientAPIResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Deprecated
@Tag(name = "Execute Operations", description = "Execute operations in background scan/query.")
@RestController
@RequestMapping("/v1/execute")
public class ExecuteV1Controller {

    public static final String EXECUTE_NOTES = "Perform multiple operations in background scan/query.";
    public static final String EXECUTE_STATUS_NOTES = "Get status of background scan by task id.";
    public static final String TASK_ID_NOTES = "Background scan task id.";
    public static final String OPERATIONS_PARAM_NOTES = "An array of operation objects specifying the operations to perform on the record.";

    @Autowired
    private AerospikeExecuteService service;

    @Operation(summary = EXECUTE_NOTES, operationId = "executeScanNamespaceSet")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Multiple operations in background scan/query run successfully."
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
            ), @ApiResponse(
                    responseCode = "409",
                    description = "Generation conflict.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            )
            }
    )
    @DefaultRestClientAPIResponses
    @PostMapping(
            value = "/scan/{namespace}/{set}",
            consumes = {"application/json", "application/msgpack"},
            produces = {"application/json", "application/msgpack"}
    )
    @ASRestClientWritePolicyQueryParams
    public RestClientExecuteTask executeScanNamespaceSet(@Parameter(
            description = APIParamDescriptors.NAMESPACE_NOTES, required = true
    ) @PathVariable(value = "namespace") String namespace, @Parameter(
            description = APIParamDescriptors.SET_NOTES, required = true
    ) @PathVariable(value = "set") String set, @Parameter(
            description = OPERATIONS_PARAM_NOTES, required = true
    ) @RequestBody List<RestClientOperation> operations,
                                                         @Parameter(hidden = true) @RequestParam Map<String, String> requestParams,
                                                         @RequestHeader(
                                                                 value = "Authorization", required = false
                                                         ) String basicAuth) {

        WritePolicy policy = RequestParamHandler.getWritePolicy(requestParams);
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);

        return service.executeV1Scan(authDetails, namespace, set, operations, policy, requestParams);
    }

    @Operation(summary = EXECUTE_NOTES, operationId = "executeScanNamespace")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Multiple operations in background scan/query run successfully."
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
            ), @ApiResponse(
                    responseCode = "409",
                    description = "Generation conflict.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            )
            }
    )
    @DefaultRestClientAPIResponses
    @PostMapping(
            value = "/scan/{namespace}",
            consumes = {"application/json", "application/msgpack"},
            produces = {"application/json", "application/msgpack"}
    )
    @ASRestClientWritePolicyQueryParams
    public RestClientExecuteTask executeScanNamespace(@Parameter(
            description = APIParamDescriptors.NAMESPACE_NOTES, required = true
    ) @PathVariable(value = "namespace") String namespace, @Parameter(
            description = OPERATIONS_PARAM_NOTES, required = true
    ) @RequestBody List<RestClientOperation> operations,
                                                      @Parameter(hidden = true) @RequestParam Map<String, String> requestParams,
                                                      @RequestHeader(
                                                              value = "Authorization", required = false
                                                      ) String basicAuth) {

        WritePolicy policy = RequestParamHandler.getWritePolicy(requestParams);
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);

        return service.executeV1Scan(authDetails, namespace, null, operations, policy, requestParams);
    }

    @Operation(description = EXECUTE_STATUS_NOTES, operationId = "executeScanStatus")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200", description = "Status of background scan read successfully."
                    ), @ApiResponse(
                    responseCode = "400",
                    description = "Invalid parameters or request.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            ), @ApiResponse(
                    responseCode = "403",
                    description = "Not authorized to access the resource.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            )
            }
    )
    @DefaultRestClientAPIResponses
    @GetMapping(value = "/scan/status/{taskId}", produces = {"application/json", "application/msgpack"})
    public RestClientExecuteTaskStatus executeScanStatus(
            @Parameter(description = TASK_ID_NOTES, required = true) @PathVariable(value = "taskId") String taskId,
            @RequestHeader(value = "Authorization", required = false) String basicAuth) {

        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);
        return service.queryScanStatus(authDetails, taskId);
    }
}
