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

import com.aerospike.client.policy.QueryPolicy;
import com.aerospike.client.query.Statement;
import com.aerospike.restclient.domain.RestClientError;
import com.aerospike.restclient.domain.auth.AuthDetails;
import com.aerospike.restclient.domain.querymodels.QueryRequestBody;
import com.aerospike.restclient.domain.querymodels.QueryResponseBody;
import com.aerospike.restclient.service.AerospikeQueryService;
import com.aerospike.restclient.util.APIParamDescriptors;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.aerospike.restclient.util.HeaderHandler;
import com.aerospike.restclient.util.RequestParamHandler;
import com.aerospike.restclient.util.annotations.ASRestClientQueryPolicyQueryParams;
import com.aerospike.restclient.util.annotations.ASRestClientStatementQueryParams;
import com.aerospike.restclient.util.annotations.DefaultRestClientAPIResponses;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Query Operations", description = "Read records in specified namespace, set.")
@RestController
@RequestMapping("/v1/query")
public class QueryController {

    public static final String QUERY_NOTES = "Return multiple records from the server in a query request.";
    public static final String QUERY_PARTITION_RANGE_NOTES = "Return multiple records from the server in a query request using the provided partition range.";

    @Autowired
    private AerospikeQueryService service;

    @io.swagger.v3.oas.annotations.Operation(summary = QUERY_NOTES, operationId = "performNamespaceSetQuery")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200", description = "Query multiple records successfully."
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
    @PostMapping(
            value = "/{namespace}/{set}",
            consumes = {"application/json", "application/msgpack"},
            produces = {"application/json", "application/msgpack"}
    )
    @ASRestClientQueryPolicyQueryParams
    @ASRestClientStatementQueryParams
    public QueryResponseBody performQuery(@Parameter(
            description = APIParamDescriptors.NAMESPACE_NOTES, required = true
    ) @PathVariable(value = "namespace") String namespace, @Parameter(
            description = APIParamDescriptors.SET_NOTES, required = true
    ) @PathVariable(value = "set") String set,
                                          @Parameter(hidden = true) @RequestParam MultiValueMap<String, String> requestParams,
                                          @RequestBody QueryRequestBody body,
                                          @RequestHeader(value = "Authorization", required = false) String basicAuth) {
        QueryPolicy policy = RequestParamHandler.getQueryPolicy(requestParams.toSingleValueMap());
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);
        boolean getToken = RequestParamHandler.getGetToken(requestParams);
        Statement stmt = RequestParamHandler.getStatement(requestParams);

        stmt.setNamespace(namespace);
        stmt.setSetName(set);

        if (body.filter != null) {
            stmt.setFilter(body.filter.toFilter());
        }

        return service.query(authDetails, policy, stmt, getToken, body.from);
    }

    @io.swagger.v3.oas.annotations.Operation(summary = QUERY_NOTES, operationId = "performNamespaceQuery")
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
    @PostMapping(
            value = "/{namespace}",
            consumes = {"application/json", "application/msgpack"},
            produces = {"application/json", "application/msgpack"}
    )
    @ASRestClientQueryPolicyQueryParams
    @ASRestClientStatementQueryParams
    public QueryResponseBody performQuery(@Parameter(
            description = APIParamDescriptors.NAMESPACE_NOTES, required = true
    ) @PathVariable(value = "namespace") String namespace,
                                          @Parameter(hidden = true) @RequestParam MultiValueMap<String, String> requestParams,
                                          @RequestBody QueryRequestBody body,
                                          @RequestHeader(value = "Authorization", required = false) String basicAuth) {
        QueryPolicy policy = RequestParamHandler.getQueryPolicy(requestParams.toSingleValueMap());
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);
        boolean getToken = RequestParamHandler.getGetToken(requestParams);
        Statement stmt = RequestParamHandler.getStatement(requestParams);

        stmt.setNamespace(namespace);

        if (body.filter != null) {
            stmt.setFilter(body.filter.toFilter());
        }

        return service.query(authDetails, policy, stmt, getToken, body.from);
    }

    @io.swagger.v3.oas.annotations.Operation(
            summary = QUERY_PARTITION_RANGE_NOTES, operationId = "performNamespaceSetRangeQuery"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200", description = "Query multiple records successfully."
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
    @PostMapping(
            value = "/{namespace}/{set}/{begin}/{count}",
            consumes = {"application/json", "application/msgpack"},
            produces = {"application/json", "application/msgpack"}
    )
    @ASRestClientQueryPolicyQueryParams
    @ASRestClientStatementQueryParams
    public QueryResponseBody performQuery(@Parameter(
            description = APIParamDescriptors.NAMESPACE_NOTES, required = true
    ) @PathVariable(value = "namespace") String namespace, @Parameter(
            description = APIParamDescriptors.SET_NOTES, required = true
    ) @PathVariable(value = "set") String set, @Parameter(
            description = APIParamDescriptors.QUERY_PARTITION_BEGIN_NOTES,
            required = true
    ) @PathVariable(
            value = AerospikeAPIConstants.QUERY_PARTITION_BEGIN
    ) int begin, @Parameter(
            description = APIParamDescriptors.QUERY_PARTITION_COUNT_NOTES,
            required = true
    ) @PathVariable(
            value = AerospikeAPIConstants.QUERY_PARTITION_COUNT
    ) int count, @Parameter(hidden = true) @RequestParam MultiValueMap<String, String> requestParams,
                                          @RequestBody QueryRequestBody body,
                                          @RequestHeader(value = "Authorization", required = false) String basicAuth) {

        QueryPolicy policy = RequestParamHandler.getQueryPolicy(requestParams.toSingleValueMap());
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);
        boolean getToken = RequestParamHandler.getGetToken(requestParams);
        Statement stmt = RequestParamHandler.getStatement(requestParams);

        stmt.setNamespace(namespace);
        stmt.setSetName(set);

        if (body.filter != null) {
            stmt.setFilter(body.filter.toFilter());
        }

        return service.query(authDetails, policy, stmt, getToken, body.from, begin, count);
    }

    @io.swagger.v3.oas.annotations.Operation(
            summary = QUERY_PARTITION_RANGE_NOTES, operationId = "performNamespaceRangeQuery"
    )
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
    @PostMapping(
            value = "/{namespace}/{begin}/{count}",
            consumes = {"application/json", "application/msgpack"},
            produces = {"application/json", "application/msgpack"}
    )
    @ASRestClientQueryPolicyQueryParams
    @ASRestClientStatementQueryParams
    public QueryResponseBody performQuery(@Parameter(
            description = APIParamDescriptors.NAMESPACE_NOTES, required = true
    ) @PathVariable(value = "namespace") String namespace, @Parameter(
            description = APIParamDescriptors.QUERY_PARTITION_BEGIN_NOTES,
            required = true
    ) @PathVariable(
            value = AerospikeAPIConstants.QUERY_PARTITION_BEGIN
    ) int begin, @Parameter(
            description = APIParamDescriptors.QUERY_PARTITION_COUNT_NOTES,
            required = true
    ) @PathVariable(
            value = AerospikeAPIConstants.QUERY_PARTITION_COUNT
    ) int count, @Parameter(hidden = true) @RequestParam MultiValueMap<String, String> requestParams,
                                          @RequestBody QueryRequestBody body,
                                          @RequestHeader(value = "Authorization", required = false) String basicAuth) {
        QueryPolicy policy = RequestParamHandler.getQueryPolicy(requestParams.toSingleValueMap());
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);
        boolean getToken = RequestParamHandler.getGetToken(requestParams);
        Statement stmt = RequestParamHandler.getStatement(requestParams);

        stmt.setNamespace(namespace);

        if (body.filter != null) {
            stmt.setFilter(body.filter.toFilter());
        }

        return service.query(authDetails, policy, stmt, getToken, body.from, begin, count);
    }
}