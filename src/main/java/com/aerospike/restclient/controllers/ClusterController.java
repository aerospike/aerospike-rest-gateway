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

import com.aerospike.restclient.domain.auth.AuthDetails;
import com.aerospike.restclient.service.AerospikeClusterService;
import com.aerospike.restclient.util.HeaderHandler;
import com.aerospike.restclient.util.ResponseExamples;
import com.aerospike.restclient.util.annotations.DefaultRestClientAPIResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Tag(name = "Cluster information operations", description = "Retrieve basic information about the Aerospike cluster.")
@RestController
@RequestMapping("/v1/cluster")
class ClusterController {
    @Autowired
    private AerospikeClusterService service;

    @Operation(
            summary = "Return an object containing information about the Aerospike cluster.",
            operationId = "getClusterInfo"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cluster information read successfully.",
                            content = @Content(
                                    examples = @ExampleObject(
                                            name = ResponseExamples.CLUSTER_INFO_NAME,
                                            value = ResponseExamples.CLUSTER_INFO_VALUE
                                    )
                            )
                    )
            }
    )
    @DefaultRestClientAPIResponses
    @GetMapping(produces = {"application/json", "application/msgpack"})
    public Map<String, Object> getClusterInfo(
            @RequestHeader(value = "Authorization", required = false) String basicAuth) {
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);
        return service.getClusterInfo(authDetails);
    }
}
