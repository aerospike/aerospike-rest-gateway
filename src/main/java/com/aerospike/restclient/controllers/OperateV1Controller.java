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
import com.aerospike.client.policy.WritePolicy;
import com.aerospike.restclient.domain.RestClientError;
import com.aerospike.restclient.domain.RestClientOperation;
import com.aerospike.restclient.domain.RestClientRecord;
import com.aerospike.restclient.domain.auth.AuthDetails;
import com.aerospike.restclient.service.AerospikeOperateService;
import com.aerospike.restclient.util.APIParamDescriptors;
import com.aerospike.restclient.util.AerospikeAPIConstants.RecordKeyType;
import com.aerospike.restclient.util.HeaderHandler;
import com.aerospike.restclient.util.RequestParamHandler;
import com.aerospike.restclient.util.annotations.ASRestClientOperateReadQueryParams;
import com.aerospike.restclient.util.annotations.ASRestClientParams;
import com.aerospike.restclient.util.annotations.ASRestClientWritePolicyQueryParams;
import com.aerospike.restclient.util.annotations.DefaultRestClientAPIResponses;
import com.aerospike.restclient.util.deserializers.MsgPackOperationsParser;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Deprecated()
@Tag(name = "Operate operations", description = "Perform multiple operations atomically on a single record.")
@RestController
@RequestMapping("/v1/operate")
public class OperateV1Controller {

    public static final String OPERATE_NOTES = "Perform multiple operations atomically on the specified record.";
    public static final String OPERATIONS_PARAM_NOTES = "An array of operation objects specifying the operations to perform on the record";
    public static final String BATCH_OPERATE_NOTES = "Perform read operations on multiple records.";

    @Autowired
    private AerospikeOperateService service;

    @Operation(summary = OPERATE_NOTES, operationId = "operateNamespaceSetKey")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Multiple operations on a record performed successfully."
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
            value = "/{namespace}/{set}/{key}",
            consumes = "application/json",
            produces = {"application/json", "application/msgpack"}
    )
    @ASRestClientParams.ASRestClientKeyTypeQueryParam
    @ASRestClientWritePolicyQueryParams
    public RestClientRecord operateNamespaceSetKey(@Parameter(
            description = APIParamDescriptors.NAMESPACE_NOTES, required = true
    ) @PathVariable(value = "namespace") String namespace, @Parameter(
            description = APIParamDescriptors.SET_NOTES, required = true
    ) @PathVariable(value = "set") String set, @Parameter(
            description = APIParamDescriptors.USERKEY_NOTES, required = true
    ) @PathVariable(value = "key") String key, @Parameter(
            description = OPERATIONS_PARAM_NOTES,
            required = true
    ) @RequestBody List<RestClientOperation> operations,
                                                   @Parameter(hidden = true) @RequestParam Map<String, String> requestParams,
                                                   @RequestHeader(
                                                           value = "Authorization",
                                                           required = false
                                                   ) String basicAuth) {

        WritePolicy policy = RequestParamHandler.getWritePolicy(requestParams);
        RecordKeyType keyType = RequestParamHandler.getKeyTypeFromMap(requestParams);
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);

        return service.operateV1(authDetails, namespace, set, key, operations, keyType, policy);
    }

    @Hidden
    @PostMapping(
            value = "/{namespace}/{set}/{key}",
            consumes = "application/msgpack",
            produces = {"application/json", "application/msgpack"}
    )
    public RestClientRecord operateNamespaceSetKeyMP(@PathVariable(value = "namespace") String namespace,
                                                     @PathVariable(value = "set") String set,
                                                     @PathVariable(value = "key") String key, InputStream dataStream,
                                                     @RequestParam Map<String, String> requestParams, @RequestHeader(
            value = "Authorization",
            required = false
    ) String basicAuth) {

        WritePolicy policy = RequestParamHandler.getWritePolicy(requestParams);
        RecordKeyType keyType = RequestParamHandler.getKeyTypeFromMap(requestParams);
        List<RestClientOperation> operations = operationsFromIStream(dataStream);
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);

        return service.operateV1(authDetails, namespace, set, key, operations, keyType, policy);
    }

    @Operation(summary = OPERATE_NOTES, operationId = "operateNamespaceKey")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Multiple operations on a record performed successfully."
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
            value = "/{namespace}/{key}",
            consumes = "application/json",
            produces = {"application/json", "application/msgpack"}
    )
    @ASRestClientParams.ASRestClientKeyTypeQueryParam
    @ASRestClientWritePolicyQueryParams
    public RestClientRecord operateNamespaceKey(@Parameter(
            description = APIParamDescriptors.NAMESPACE_NOTES, required = true
    ) @PathVariable(value = "namespace") String namespace, @Parameter(
            description = APIParamDescriptors.USERKEY_NOTES, required = true
    ) @PathVariable(value = "key") String key, @Parameter(
            description = OPERATIONS_PARAM_NOTES,
            required = true
    ) @RequestBody List<RestClientOperation> operations,
                                                @Parameter(hidden = true) @RequestParam Map<String, String> requestParams,
                                                @RequestHeader(
                                                        value = "Authorization",
                                                        required = false
                                                ) String basicAuth) {

        WritePolicy policy = RequestParamHandler.getWritePolicy(requestParams);
        RecordKeyType keyType = RequestParamHandler.getKeyTypeFromMap(requestParams);
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);

        return service.operateV1(authDetails, namespace, null, key, operations, keyType, policy);
    }

    @Hidden
    @PostMapping(
            value = "/{namespace}/{key}",
            consumes = "application/msgpack",
            produces = {"application/json", "application/msgpack"}
    )
    public RestClientRecord operateNamespaceKeyMP(@PathVariable(value = "namespace") String namespace,
                                                  @PathVariable(value = "key") String key, InputStream dataStream,
                                                  @RequestParam Map<String, String> requestParams, @RequestHeader(
            value = "Authorization",
            required = false
    ) String basicAuth) {

        WritePolicy policy = RequestParamHandler.getWritePolicy(requestParams);
        RecordKeyType keyType = RequestParamHandler.getKeyTypeFromMap(requestParams);
        List<RestClientOperation> operations = operationsFromIStream(dataStream);
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);

        return service.operateV1(authDetails, namespace, null, key, operations, keyType, policy);
    }

    private List<RestClientOperation> operationsFromIStream(InputStream dataStream) {
        MsgPackOperationsParser parser = new MsgPackOperationsParser(dataStream);
        return parser.parseOperations();
    }

    @Operation(summary = BATCH_OPERATE_NOTES, operationId = "operateBatchNamespaceSet")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Read operations on multiple records performed successfully.",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = RestClientRecord.class)))
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
            value = "/read/{namespace}/{set}",
            consumes = "application/json",
            produces = {"application/json", "application/msgpack"}
    )
    @ASRestClientParams.ASRestClientKeyTypeQueryParam
    @ASRestClientOperateReadQueryParams
    public RestClientRecord[] operateBatchNamespaceSet(@Parameter(
            description = APIParamDescriptors.NAMESPACE_NOTES, required = true
    ) @PathVariable(value = "namespace") String namespace, @Parameter(
            description = APIParamDescriptors.SET_NOTES, required = true
    ) @PathVariable(value = "set") String set, @Parameter(
            description = OPERATIONS_PARAM_NOTES,
            required = true
    ) @RequestBody List<RestClientOperation> operations,
                                                       @Parameter(hidden = true) @RequestParam MultiValueMap<String, String> requestParams,
                                                       @RequestHeader(
                                                               value = "Authorization",
                                                               required = false
                                                       ) String basicAuth) {

        BatchPolicy policy = RequestParamHandler.getBatchPolicy(requestParams.toSingleValueMap());
        String[] keys = RequestParamHandler.getKeysFromMap(requestParams);
        RecordKeyType keyType = RequestParamHandler.getKeyTypeFromMap(requestParams);
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);

        return service.operateV1(authDetails, namespace, set, keys, operations, keyType, policy);
    }

    @Hidden
    @PostMapping(
            value = "/read/{namespace}/{set}",
            consumes = "application/msgpack",
            produces = {"application/json", "application/msgpack"}
    )
    public RestClientRecord[] operateBatchNamespaceSetMP(@PathVariable(value = "namespace") String namespace,
                                                         @PathVariable(value = "set") String set,
                                                         InputStream dataStream,
                                                         @RequestParam MultiValueMap<String, String> requestParams,
                                                         @RequestHeader(
                                                                 value = "Authorization",
                                                                 required = false
                                                         ) String basicAuth) {

        BatchPolicy policy = RequestParamHandler.getBatchPolicy(requestParams.toSingleValueMap());
        String[] keys = RequestParamHandler.getKeysFromMap(requestParams);
        RecordKeyType keyType = RequestParamHandler.getKeyTypeFromMap(requestParams);
        List<RestClientOperation> operations = operationsFromIStream(dataStream);
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);

        return service.operateV1(authDetails, namespace, set, keys, operations, keyType, policy);
    }

    @Operation(summary = BATCH_OPERATE_NOTES, operationId = "operateBatchNamespace")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Read operations on multiple records performed successfully.",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = RestClientRecord.class)))
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
            value = "/read/{namespace}",
            consumes = "application/json",
            produces = {"application/json", "application/msgpack"}
    )
    @ASRestClientParams.ASRestClientKeyTypeQueryParam
    @ASRestClientOperateReadQueryParams
    public RestClientRecord[] operateBatchNamespace(@Parameter(
            description = APIParamDescriptors.NAMESPACE_NOTES, required = true
    ) @PathVariable(value = "namespace") String namespace, @Parameter(
            description = OPERATIONS_PARAM_NOTES,
            required = true
    ) @RequestBody List<RestClientOperation> operations,
                                                    @Parameter(hidden = true) @RequestParam MultiValueMap<String, String> requestParams,
                                                    @RequestHeader(
                                                            value = "Authorization",
                                                            required = false
                                                    ) String basicAuth) {

        BatchPolicy policy = RequestParamHandler.getBatchPolicy(requestParams.toSingleValueMap());
        String[] keys = RequestParamHandler.getKeysFromMap(requestParams);
        RecordKeyType keyType = RequestParamHandler.getKeyTypeFromMap(requestParams);
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);

        return service.operateV1(authDetails, namespace, null, keys, operations, keyType, policy);
    }

    @Hidden
    @PostMapping(
            value = "/read/{namespace}",
            consumes = "application/msgpack",
            produces = {"application/json", "application/msgpack"}
    )
    public RestClientRecord[] operateBatchNamespaceMP(@PathVariable(value = "namespace") String namespace,
                                                      InputStream dataStream,
                                                      @RequestParam MultiValueMap<String, String> requestParams,
                                                      @RequestHeader(
                                                              value = "Authorization",
                                                              required = false
                                                      ) String basicAuth) {

        BatchPolicy policy = RequestParamHandler.getBatchPolicy(requestParams.toSingleValueMap());
        String[] keys = RequestParamHandler.getKeysFromMap(requestParams);
        RecordKeyType keyType = RequestParamHandler.getKeyTypeFromMap(requestParams);
        List<RestClientOperation> operations = operationsFromIStream(dataStream);
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);

        return service.operateV1(authDetails, namespace, null, keys, operations, keyType, policy);
    }
}

