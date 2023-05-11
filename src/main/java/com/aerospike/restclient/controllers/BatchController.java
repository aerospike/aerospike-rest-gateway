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

import com.aerospike.client.policy.BatchPolicy;
import com.aerospike.restclient.domain.RestClientError;
import com.aerospike.restclient.domain.auth.AuthDetails;
import com.aerospike.restclient.domain.batchmodels.BatchRecord;
import com.aerospike.restclient.domain.batchmodels.BatchRecordResponse;
import com.aerospike.restclient.domain.batchmodels.BatchResponseBody;
import com.aerospike.restclient.service.AerospikeBatchService;
import com.aerospike.restclient.util.HeaderHandler;
import com.aerospike.restclient.util.RequestParamHandler;
import com.aerospike.restclient.util.annotations.ASRestClientBatchPolicyQueryParams;
import com.aerospike.restclient.util.annotations.DefaultRestClientAPIResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Batch Operations", description = "Retrieve multiple records from the server.")
@RestController
@RequestMapping("/v1/batch")
public class BatchController {

    @Autowired
    private AerospikeBatchService service;

    @Operation(
            summary = "Return multiple records from the server in a single request. Write, Delete, and UDFs are only allowed on aerospike servers 6.0+",
            operationId = "performBatch"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Batch Operation completed successfully.",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = BatchRecordResponse.class)))
                    ), @ApiResponse(
                    responseCode = "400",
                    description = "Invalid parameters or request. Write, Delete, and UDFs are only allowed on aerospike servers 6.0+",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            ), @ApiResponse(
                    responseCode = "403",
                    description = "Not authorized to access the resource.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            ), @ApiResponse(
                    responseCode = "404",
                    description = "Non existent namespace used in one or more key.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            )
            }
    )
    @DefaultRestClientAPIResponses
    @PostMapping(
            consumes = {"application/json", "application/msgpack"},
            produces = {"application/json", "application/msgpack"}
    )
    @ASRestClientBatchPolicyQueryParams
    public BatchResponseBody performBatch(@RequestBody List<BatchRecord> batchRecords,
                                          @Parameter(hidden = true) @RequestParam Map<String, String> requestParams,
                                          @RequestHeader(
                                                  value = "Authorization", required = false
                                          ) String basicAuth) {
        BatchPolicy policy = RequestParamHandler.getBatchPolicy(requestParams);
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);

        return service.batch(authDetails, batchRecords, policy);
    }
}
