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

import com.aerospike.client.policy.Policy;
import com.aerospike.client.policy.WritePolicy;
import com.aerospike.restclient.domain.RestClientError;
import com.aerospike.restclient.domain.auth.AuthDetails;
import com.aerospike.restclient.service.AerospikeDocumentService;
import com.aerospike.restclient.util.*;
import com.aerospike.restclient.util.annotations.ASRestClientParams;
import com.aerospike.restclient.util.annotations.ASRestDocumentPolicyQueryParams;
import com.aerospike.restclient.util.annotations.ASRestDocumentWritePolicyQueryParams;
import com.aerospike.restclient.util.annotations.DefaultRestClientAPIResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Tag(name = "Document API Operations", description = "Perform operations on records using JSONPath queries.")
@RestController
@RequestMapping("/v1/document")
public class DocumentApiController {

    public static final String NAMESPACE_NOTES = "Namespace for the record; equivalent to database name.";
    public static final String SET_NOTES = "Set for the record; equivalent to database table.";
    public static final String USER_KEY_NOTES = "User key for the record.";
    public static final String BIN_NAME_NOTES = "Bin name for the document.";
    public static final String JSON_PATH_NOTES = "JSONPath expression specifies a path to an element " + "(or a set of elements) in a JSON structure. ";

    public static final String GET_DOCUMENT_NOTES = "Retrieve the object in the document with key documentKey" + " that is referenced by the JSON path.";
    public static final String PUT_DOCUMENT_NOTES = "Put a document.";
    public static final String APPEND_DOCUMENT_NOTES = "Append an object to a list in a document specified by a JSON path.";
    public static final String DELETE_DOCUMENT_NOTES = "Delete an object in a document specified by a JSON path.";
    public static final String JSON_OBJECT_NOTES = "JSON Object";

    @Autowired
    private AerospikeDocumentService service;

    /*
     **************************************************
     *                     GET                        *
     **************************************************
     */
    @Operation(summary = GET_DOCUMENT_NOTES, operationId = "getDocumentObject")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200", description = "Document read successfully.", content = @Content(
                            examples = @ExampleObject(
                                    name = ResponseExamples.GET_DOCUMENT_OBJECT_NAME,
                                    value = ResponseExamples.GET_DOCUMENT_OBJECT_VALUE
                            )
                    )
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
                    description = "Record not found.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            )
            }
    )
    @DefaultRestClientAPIResponses
    @GetMapping(value = "/{namespace}/{key}", produces = {"application/json", "application/msgpack"})
    @ASRestClientParams.ASRestClientKeyTypeQueryParam
    @ASRestDocumentPolicyQueryParams
    public Map<String, Object> getDocumentObject(@Parameter(
            description = NAMESPACE_NOTES, required = true
    ) @PathVariable(value = "namespace") String namespace, @Parameter(
            description = USER_KEY_NOTES, required = true
    ) @PathVariable(value = "key") String key,
                                                 @Parameter(hidden = true) @RequestParam MultiValueMap<String, String> requestParams,
                                                 @RequestHeader(
                                                         value = "Authorization", required = false
                                                 ) String basicAuth) {

        AerospikeAPIConstants.RecordKeyType keyType = RequestParamHandler.getKeyTypeFromMap(requestParams);
        Policy policy = RequestParamHandler.getPolicy(requestParams);
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);
        List<String> bins = Arrays.asList(RequestParamHandler.getBinsFromMap(requestParams));
        String jsonPath = RequestParamHandler.getJsonPathFromMap(requestParams);

        return service.getObject(authDetails, namespace, null, key, bins, jsonPath, keyType, policy);
    }

    @Operation(summary = GET_DOCUMENT_NOTES, operationId = "getDocumentObjectSet")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200", description = "Document read successfully.", content = @Content(
                            examples = @ExampleObject(
                                    name = ResponseExamples.GET_DOCUMENT_OBJECT_NAME,
                                    value = ResponseExamples.GET_DOCUMENT_OBJECT_VALUE
                            )
                    )
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
                    description = "Record not found.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            )
            }
    )
    @DefaultRestClientAPIResponses
    @GetMapping(value = "/{namespace}/{set}/{key}", produces = {"application/json", "application/msgpack"})
    @ASRestClientParams.ASRestClientKeyTypeQueryParam
    @ASRestDocumentPolicyQueryParams
    public Map<String, Object> getDocumentObjectSet(@Parameter(
            description = NAMESPACE_NOTES, required = true
    ) @PathVariable(value = "namespace") String namespace,
                                                    @Parameter(description = SET_NOTES, required = true) @PathVariable(
                                                            value = "set"
                                                    ) String set, @Parameter(
            description = USER_KEY_NOTES, required = true
    ) @PathVariable(value = "key") String key,
                                                    @Parameter(hidden = true) @RequestParam MultiValueMap<String, String> requestParams,
                                                    @RequestHeader(
                                                            value = "Authorization", required = false
                                                    ) String basicAuth) {

        AerospikeAPIConstants.RecordKeyType keyType = RequestParamHandler.getKeyTypeFromMap(requestParams);
        Policy policy = RequestParamHandler.getPolicy(requestParams);
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);
        List<String> bins = Arrays.asList(RequestParamHandler.getBinsFromMap(requestParams));
        String jsonPath = RequestParamHandler.getJsonPathFromMap(requestParams);

        return service.getObject(authDetails, namespace, set, key, bins, jsonPath, keyType, policy);
    }

    /*
     **************************************************
     *                     PUT                        *
     **************************************************
     */
    @Operation(summary = PUT_DOCUMENT_NOTES, operationId = "putDocumentObject")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "202", description = "Request to put a document has been accepted."
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
                    description = "Record not found.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            )
            }
    )
    @DefaultRestClientAPIResponses
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping(value = "/{namespace}/{key}", produces = {"application/json", "application/msgpack"})
    @ASRestClientParams.ASRestClientKeyTypeQueryParam
    @ASRestDocumentWritePolicyQueryParams
    public void putDocumentObject(@Parameter(
            description = NAMESPACE_NOTES, required = true
    ) @PathVariable(value = "namespace") String namespace, @Parameter(
            description = USER_KEY_NOTES, required = true
    ) @PathVariable(value = "key") String key, @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = JSON_OBJECT_NOTES, required = true, content = @Content(
            examples = @ExampleObject(
                    name = RequestBodyExamples.JSON_OBJECT_NAME, value = RequestBodyExamples.JSON_OBJECT_VALUE
            )
    )
    ) @RequestBody Object jsonObject,
                                  @Parameter(hidden = true) @RequestParam MultiValueMap<String, String> requestParams,
                                  @RequestHeader(value = "Authorization", required = false) String basicAuth) {

        AerospikeAPIConstants.RecordKeyType keyType = RequestParamHandler.getKeyTypeFromMap(requestParams);
        WritePolicy policy = RequestParamHandler.getWritePolicy(requestParams.toSingleValueMap());
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);
        List<String> bins = Arrays.asList(RequestParamHandler.getBinsFromMap(requestParams));
        String jsonPath = RequestParamHandler.getJsonPathFromMap(requestParams);

        service.putObject(authDetails, namespace, null, key, bins, jsonPath, jsonObject, keyType, policy);
    }

    @Operation(summary = PUT_DOCUMENT_NOTES, operationId = "putDocumentObjectSet")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "202", description = "Request to put a document has been accepted."
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
                    description = "Record not found.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            )
            }
    )
    @DefaultRestClientAPIResponses
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping(value = "/{namespace}/{set}/{key}", produces = {"application/json", "application/msgpack"})
    @ASRestClientParams.ASRestClientKeyTypeQueryParam
    @ASRestDocumentWritePolicyQueryParams
    public void putDocumentObjectSet(@Parameter(
            description = NAMESPACE_NOTES, required = true
    ) @PathVariable(value = "namespace") String namespace, @Parameter(
            description = SET_NOTES, required = true
    ) @PathVariable(value = "set") String set, @Parameter(
            description = USER_KEY_NOTES, required = true
    ) @PathVariable(value = "key") String key, @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = JSON_OBJECT_NOTES, required = true, content = @Content(
            examples = @ExampleObject(
                    name = RequestBodyExamples.JSON_OBJECT_NAME, value = RequestBodyExamples.JSON_OBJECT_VALUE
            )
    )
    ) @RequestBody Object jsonObject,
                                     @Parameter(hidden = true) @RequestParam MultiValueMap<String, String> requestParams,
                                     @RequestHeader(value = "Authorization", required = false) String basicAuth) {

        AerospikeAPIConstants.RecordKeyType keyType = RequestParamHandler.getKeyTypeFromMap(requestParams);
        WritePolicy policy = RequestParamHandler.getWritePolicy(requestParams.toSingleValueMap());
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);
        List<String> bins = Arrays.asList(RequestParamHandler.getBinsFromMap(requestParams));
        String jsonPath = RequestParamHandler.getJsonPathFromMap(requestParams);

        service.putObject(authDetails, namespace, set, key, bins, jsonPath, jsonObject, keyType, policy);
    }

    /*
     **************************************************
     *                     POST                       *
     **************************************************
     */
    @Operation(summary = APPEND_DOCUMENT_NOTES, operationId = "appendDocumentObject")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "202",
                            description = "Request to append an object to a list in a document has been accepted."
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
                    description = "Record not found.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            )
            }
    )
    @DefaultRestClientAPIResponses
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping(value = "/{namespace}/{key}", produces = {"application/json", "application/msgpack"})
    @ASRestClientParams.ASRestClientKeyTypeQueryParam
    @ASRestDocumentWritePolicyQueryParams
    public void appendDocumentObject(@Parameter(
            description = NAMESPACE_NOTES, required = true
    ) @PathVariable(value = "namespace") String namespace, @Parameter(
            description = USER_KEY_NOTES, required = true
    ) @PathVariable(value = "key") String key, @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = JSON_OBJECT_NOTES, required = true, content = @Content(
            examples = @ExampleObject(
                    name = RequestBodyExamples.JSON_OBJECT_NAME, value = RequestBodyExamples.JSON_OBJECT_VALUE
            )
    )
    ) @RequestBody Object jsonObject,
                                     @Parameter(hidden = true) @RequestParam MultiValueMap<String, String> requestParams,
                                     @RequestHeader(value = "Authorization", required = false) String basicAuth) {

        AerospikeAPIConstants.RecordKeyType keyType = RequestParamHandler.getKeyTypeFromMap(requestParams);
        WritePolicy policy = RequestParamHandler.getWritePolicy(requestParams.toSingleValueMap());
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);
        List<String> bins = Arrays.asList(RequestParamHandler.getBinsFromMap(requestParams));
        String jsonPath = RequestParamHandler.getJsonPathFromMap(requestParams);

        service.appendObject(authDetails, namespace, null, key, bins, jsonPath, jsonObject, keyType, policy);
    }

    @Operation(summary = APPEND_DOCUMENT_NOTES, operationId = "appendDocumentObjectSet")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "202",
                            description = "Request to append an object to a list in a document has been accepted."
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
                    description = "Record not found.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            )
            }
    )
    @DefaultRestClientAPIResponses
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping(value = "/{namespace}/{set}/{key}", produces = {"application/json", "application/msgpack"})
    @ASRestClientParams.ASRestClientKeyTypeQueryParam
    @ASRestDocumentWritePolicyQueryParams
    public void appendDocumentObjectSet(@Parameter(
            description = NAMESPACE_NOTES, required = true
    ) @PathVariable(value = "namespace") String namespace, @Parameter(
            description = SET_NOTES, required = true
    ) @PathVariable(value = "set") String set, @Parameter(
            description = USER_KEY_NOTES, required = true
    ) @PathVariable(value = "key") String key, @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = JSON_OBJECT_NOTES, required = true, content = @Content(
            examples = @ExampleObject(
                    name = RequestBodyExamples.JSON_OBJECT_NAME, value = RequestBodyExamples.JSON_OBJECT_VALUE
            )
    )
    ) @RequestBody Object jsonObject,
                                        @Parameter(hidden = true) @RequestParam MultiValueMap<String, String> requestParams,
                                        @RequestHeader(value = "Authorization", required = false) String basicAuth) {

        AerospikeAPIConstants.RecordKeyType keyType = RequestParamHandler.getKeyTypeFromMap(requestParams);
        WritePolicy policy = RequestParamHandler.getWritePolicy(requestParams.toSingleValueMap());
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);
        List<String> bins = Arrays.asList(RequestParamHandler.getBinsFromMap(requestParams));
        String jsonPath = RequestParamHandler.getJsonPathFromMap(requestParams);

        service.appendObject(authDetails, namespace, set, key, bins, jsonPath, jsonObject, keyType, policy);
    }

    /*
     **************************************************
     *                     DELETE                     *
     **************************************************
     */
    @Operation(summary = DELETE_DOCUMENT_NOTES, operationId = "deleteDocumentObject")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Deleted an object in a document successfully, no content expected."
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
                    description = "Record not found.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            )
            }
    )
    @DefaultRestClientAPIResponses
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{namespace}/{key}", produces = {"application/json", "application/msgpack"})
    @ASRestClientParams.ASRestClientKeyTypeQueryParam
    @ASRestDocumentWritePolicyQueryParams
    public void deleteDocumentObject(@Parameter(
            description = NAMESPACE_NOTES, required = true
    ) @PathVariable(value = "namespace") String namespace, @Parameter(
            description = USER_KEY_NOTES, required = true
    ) @PathVariable(value = "key") String key,
                                     @Parameter(hidden = true) @RequestParam MultiValueMap<String, String> requestParams,
                                     @RequestHeader(value = "Authorization", required = false) String basicAuth) {

        AerospikeAPIConstants.RecordKeyType keyType = RequestParamHandler.getKeyTypeFromMap(requestParams);
        WritePolicy policy = RequestParamHandler.getWritePolicy(requestParams.toSingleValueMap());
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);
        List<String> bins = Arrays.asList(RequestParamHandler.getBinsFromMap(requestParams));
        String jsonPath = RequestParamHandler.getJsonPathFromMap(requestParams);

        service.deleteObject(authDetails, namespace, null, key, bins, jsonPath, keyType, policy);
    }

    @Operation(summary = DELETE_DOCUMENT_NOTES, operationId = "deleteDocumentObjectSet")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Deleted an object in a document successfully, no content expected."
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
                    description = "Record not found.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            )
            }
    )
    @DefaultRestClientAPIResponses
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{namespace}/{set}/{key}", produces = {"application/json", "application/msgpack"})
    @ASRestClientParams.ASRestClientKeyTypeQueryParam
    @ASRestDocumentWritePolicyQueryParams
    public void deleteDocumentObjectSet(@Parameter(
            description = NAMESPACE_NOTES, required = true
    ) @PathVariable(value = "namespace") String namespace, @Parameter(
            description = SET_NOTES, required = true
    ) @PathVariable(value = "set") String set, @Parameter(
            description = USER_KEY_NOTES, required = true
    ) @PathVariable(value = "key") String key,
                                        @Parameter(hidden = true) @RequestParam MultiValueMap<String, String> requestParams,
                                        @RequestHeader(value = "Authorization", required = false) String basicAuth) {

        AerospikeAPIConstants.RecordKeyType keyType = RequestParamHandler.getKeyTypeFromMap(requestParams);
        WritePolicy policy = RequestParamHandler.getWritePolicy(requestParams.toSingleValueMap());
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);
        List<String> bins = Arrays.asList(RequestParamHandler.getBinsFromMap(requestParams));
        String jsonPath = RequestParamHandler.getJsonPathFromMap(requestParams);

        service.deleteObject(authDetails, namespace, set, key, bins, jsonPath, keyType, policy);
    }
}
