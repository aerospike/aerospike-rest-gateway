package com.aerospike.restclient.controllers;

import com.aerospike.client.policy.QueryPolicy;
import com.aerospike.client.Operation;
import com.aerospike.client.query.Statement;
import com.aerospike.restclient.domain.RestClientError;
import com.aerospike.restclient.domain.RestClientOperation;
import com.aerospike.restclient.domain.auth.AuthDetails;
import com.aerospike.restclient.domain.querymodels.RestClientQueryBody;
import com.aerospike.restclient.domain.querymodels.RestClientQueryResponse;
import com.aerospike.restclient.service.AerospikeQueryService;
import com.aerospike.restclient.util.*;
import com.aerospike.restclient.util.annotations.ASRestClientQueryPolicyQueryParams;
import com.aerospike.restclient.util.annotations.ASRestClientQueryQueryParams;
import com.aerospike.restclient.util.annotations.DefaultRestClientAPIResponses;
import com.aerospike.restclient.util.converters.OperationConverter;
import com.aerospike.restclient.util.converters.StatementConverter;
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
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Query multiple records successfully."),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid parameters or request.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))),
            @ApiResponse(
                    responseCode = "403",
                    description = "Not authorized to access the resource.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Namespace or set does not exist.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class)))
    })
    @DefaultRestClientAPIResponses
    @PostMapping(value = "/{namespace}/{set}", consumes = {"application/json", "application/msgpack"}, produces = {"application/json", "application/msgpack"})
    @ASRestClientQueryPolicyQueryParams
    @ASRestClientQueryQueryParams
    public RestClientQueryResponse performQuery(
            @Parameter(description = APIParamDescriptors.NAMESPACE_NOTES, required = true) @PathVariable(value = "namespace") String namespace,
            @Parameter(description = APIParamDescriptors.SET_NOTES, required = true) @PathVariable(value = "set") String set,
            @Parameter(hidden = true) @RequestParam MultiValueMap<String, String> requestParams,
            @RequestBody RestClientQueryBody body,
            @RequestHeader(value = "Authorization", required = false) String basicAuth) {

        QueryPolicy policy = RequestParamHandler.getQueryPolicy(requestParams.toSingleValueMap());
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);
        Statement stmt = StatementConverter.statementFromMultiMap(requestParams);

        stmt.setNamespace(namespace);
        stmt.setSetName(set);

        if (body.filter != null) {
            stmt.setFilter(body.filter.toFilter());
        }

        return service.query(authDetails, stmt, requestParams.toSingleValueMap(), policy, body.from);
    }

    @io.swagger.v3.oas.annotations.Operation(summary = QUERY_NOTES, operationId = "performNamespaceQuery")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Scan multiple records successfully."),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid parameters or request.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))),
            @ApiResponse(
                    responseCode = "403",
                    description = "Not authorized to access the resource.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Namespace or set does not exist.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class)))
    })
    @DefaultRestClientAPIResponses
    @PostMapping(value = "/{namespace}", consumes = {"application/json", "application/msgpack"}, produces = {"application/json", "application/msgpack"})
    @ASRestClientQueryPolicyQueryParams
    @ASRestClientQueryQueryParams
    public RestClientQueryResponse performQuery(
            @Parameter(description = APIParamDescriptors.NAMESPACE_NOTES, required = true) @PathVariable(value = "namespace") String namespace,
            @Parameter(hidden = true) @RequestParam MultiValueMap<String, String> requestParams,
            @RequestBody RestClientQueryBody body,
            @RequestHeader(value = "Authorization", required = false) String basicAuth) {

        QueryPolicy policy = RequestParamHandler.getQueryPolicy(requestParams.toSingleValueMap());
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);
        Statement stmt = StatementConverter.statementFromMultiMap(requestParams);
        stmt.setNamespace(namespace);

        // Should this happen in the service or controller?
        if (body.filter != null) {
            stmt.setFilter(body.filter.toFilter());
        }

        return service.query(authDetails, stmt, requestParams.toSingleValueMap(), policy, body.from);
    }

    @io.swagger.v3.oas.annotations.Operation(summary = QUERY_PARTITION_RANGE_NOTES, operationId = "performNamespaceSetRangeQuery")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Query multiple records successfully."),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid parameters or request.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))),
            @ApiResponse(
                    responseCode = "403",
                    description = "Not authorized to access the resource.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Namespace or set does not exist.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class)))
    })
    @DefaultRestClientAPIResponses
    @PostMapping(value = "/{namespace}/{set}/{begin}/{count}", consumes = {"application/json", "application/msgpack"}, produces = {"application/json", "application/msgpack"})
    @ASRestClientQueryPolicyQueryParams
    @ASRestClientQueryQueryParams
    public RestClientQueryResponse performQuery(
            @Parameter(description = APIParamDescriptors.NAMESPACE_NOTES, required = true) @PathVariable(value = "namespace") String namespace,
            @Parameter(description = APIParamDescriptors.SET_NOTES, required = true) @PathVariable(value = "set") String set,
            @Parameter(description = APIParamDescriptors.QUERY_PARTITION_BEGIN_NOTES, required = true) @PathVariable(value = AerospikeAPIConstants.QUERY_PARTITION_BEGIN) int begin,
            @Parameter(description = APIParamDescriptors.QUERY_PARTITION_COUNT_NOTES, required = true) @PathVariable(value = AerospikeAPIConstants.QUERY_PARTITION_COUNT) int count,
            @Parameter(hidden = true) @RequestParam MultiValueMap<String, String> requestParams,
            @RequestBody RestClientQueryBody body,
            @RequestHeader(value = "Authorization", required = false) String basicAuth) {

        QueryPolicy policy = RequestParamHandler.getQueryPolicy(requestParams.toSingleValueMap());
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);
        Statement stmt = StatementConverter.statementFromMultiMap(requestParams);

        stmt.setNamespace(namespace);
        stmt.setSetName(set);

        if (body.filter != null) {
            stmt.setFilter(body.filter.toFilter());
        }

        return service.query(authDetails, stmt, requestParams.toSingleValueMap(), policy, body.from, begin, count);
    }

    @io.swagger.v3.oas.annotations.Operation(summary = QUERY_PARTITION_RANGE_NOTES, operationId = "performNamespaceRangeQuery")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Scan multiple records successfully."),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid parameters or request.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))),
            @ApiResponse(
                    responseCode = "403",
                    description = "Not authorized to access the resource.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Namespace or set does not exist.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class)))
    })
    @DefaultRestClientAPIResponses
    @PostMapping(value = "/{namespace}/{begin}/{count}", consumes = {"application/json", "application/msgpack"}, produces = {"application/json", "application/msgpack"})
    @ASRestClientQueryPolicyQueryParams
    @ASRestClientQueryQueryParams
    public RestClientQueryResponse performQuery(
            @Parameter(description = APIParamDescriptors.NAMESPACE_NOTES, required = true) @PathVariable(value = "namespace") String namespace,
            @Parameter(description = APIParamDescriptors.QUERY_PARTITION_BEGIN_NOTES, required = true) @PathVariable(value = AerospikeAPIConstants.QUERY_PARTITION_BEGIN) int begin,
            @Parameter(description = APIParamDescriptors.QUERY_PARTITION_COUNT_NOTES, required = true) @PathVariable(value = AerospikeAPIConstants.QUERY_PARTITION_COUNT) int count,
            @Parameter(hidden = true) @RequestParam MultiValueMap<String, String> requestParams,
            @RequestBody RestClientQueryBody body,
            @RequestHeader(value = "Authorization", required = false) String basicAuth) {

        QueryPolicy policy = RequestParamHandler.getQueryPolicy(requestParams.toSingleValueMap());
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);
        Statement stmt = StatementConverter.statementFromMultiMap(requestParams);
        stmt.setNamespace(namespace);

        if (body.filter != null) {
            stmt.setFilter(body.filter.toFilter());
        }

        return service.query(authDetails, stmt, requestParams.toSingleValueMap(), policy, body.from, begin, count);
    }
}