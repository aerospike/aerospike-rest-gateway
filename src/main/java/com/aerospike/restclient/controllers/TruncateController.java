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

import com.aerospike.restclient.domain.RestClientError;
import com.aerospike.restclient.domain.auth.AuthDetails;
import com.aerospike.restclient.service.AerospikeTruncateService;
import com.aerospike.restclient.util.HeaderHandler;
import com.aerospike.restclient.util.annotations.DefaultRestClientAPIResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Truncate Operations", description = "Remove multiple records from the server.")
@RestController
@RequestMapping("/v1/truncate")
public class TruncateController {

    public static final String DATE_QUERY_PARAM_NOTES = "All records last updated before this date/time will be truncated. If not specified, all records will be truncated.\n " + "This is a string representation of a date time utilizing the ISO-8601 extended offset date-time format. example: 2019-12-03T10:15:30+01:00";
    public static final String DATE_QUERY_PARAM_EXAMPLE = "2019-12-03T10:15:30+01:00";

    @Autowired
    private AerospikeTruncateService truncateService;

    @Operation(summary = "Truncate records in a specified namespace.", operationId = "truncateNamespace")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "202", description = "Request to truncate record has been accepted."
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
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    @DeleteMapping(value = "/{namespace}", produces = {"application/json", "application/msgpack"})
    public void truncateNamespace(
            @Parameter(description = "The namespace whose records will be truncated.", required = true) @PathVariable(
                    value = "namespace"
            ) String namespace, @Parameter(
            description = DATE_QUERY_PARAM_NOTES,
            example = DATE_QUERY_PARAM_EXAMPLE
    ) @RequestParam(value = "date", required = false) String dateString,
            @RequestHeader(value = "Authorization", required = false) String basicAuth) {

        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);

        truncateService.truncate(authDetails, namespace, null, dateString);
    }

    @Operation(summary = "Truncate records in a specified namespace and set.", operationId = "truncateSet")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "202", description = "Request to truncate record has been accepted."
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
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    @DeleteMapping(value = "/{namespace}/{set}", produces = {"application/json", "application/msgpack"})
    public void truncateSet(
            @Parameter(description = "The namespace whose records will be truncated", required = true) @PathVariable(
                    value = "namespace"
            ) String namespace, @Parameter(
            description = "The set, in the specified namespace, whose records will be truncated",
            required = true
    ) @PathVariable(value = "set") String set, @Parameter(
            description = DATE_QUERY_PARAM_NOTES,
            example = DATE_QUERY_PARAM_EXAMPLE
    ) @RequestParam(value = "date", required = false) String dateString,
            @RequestHeader(value = "Authorization", required = false) String basicAuth) {

        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);

        truncateService.truncate(authDetails, namespace, set, dateString);
    }
}
