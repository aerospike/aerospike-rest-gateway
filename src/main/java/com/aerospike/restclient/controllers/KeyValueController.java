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
import com.aerospike.client.policy.RecordExistsAction;
import com.aerospike.client.policy.WritePolicy;
import com.aerospike.restclient.domain.RestClientError;
import com.aerospike.restclient.domain.RestClientRecord;
import com.aerospike.restclient.domain.auth.AuthDetails;
import com.aerospike.restclient.service.AerospikeRecordService;
import com.aerospike.restclient.util.APIDescriptors;
import com.aerospike.restclient.util.AerospikeAPIConstants.RecordKeyType;
import com.aerospike.restclient.util.HeaderHandler;
import com.aerospike.restclient.util.RequestBodyExamples;
import com.aerospike.restclient.util.RequestParamHandler;
import com.aerospike.restclient.util.annotations.ASRestClientParams;
import com.aerospike.restclient.util.annotations.ASRestClientPolicyQueryParams;
import com.aerospike.restclient.util.annotations.ASRestClientWritePolicyQueryParams;
import com.aerospike.restclient.util.annotations.DefaultRestClientAPIResponses;
import com.aerospike.restclient.util.deserializers.MsgPackBinParser;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Tag(name = "Key Value Operations", description = "Perform simple operations on a single record.")
@RestController
@RequestMapping("/v1/kvs")
public class KeyValueController {

    public static final String NAMESPACE_NOTES = "Namespace for the record; equivalent to database name.";
    public static final String SET_NOTES = "Set for the record; equivalent to database table.";
    public static final String USERKEY_NOTES = "Userkey for the record.";
    public static final String STORE_BINS_NOTES = "Bins to be stored in the record. This is a mapping from a string bin name to a value. " + "Value can be a String, integer, floating point number, list, map, bytearray, or GeoJSON value. For more information on data formats, older APIs, and msgpack: https://github.com/aerospike/aerospike-rest-gateway/blob/master/docs/data-formats.md";
    public static final String GET_RECORD_NOTES = "Return the metadata and bins for a record.";
    public static final String UPDATE_RECORD_NOTES = "Merge the provided bins into the record.";
    public static final String CREATE_RECORD_NOTES = "Create a new record with the provided bins into the record.";
    public static final String REPLACE_RECORD_NOTES = "Replace the bins of the specified record.";
    public static final String DELETE_RECORD_NOTES = "Delete the specified record.";

    @Autowired
    private AerospikeRecordService service;

    /*
     **************************************************
     *                     GET                        *
     **************************************************
     */
    @Operation(summary = GET_RECORD_NOTES, operationId = "getRecordNamespaceSetKey")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200", description = "Metadata and bins for a record returned successfully."
                    ), @ApiResponse(
                    responseCode = "400",
                    description = "Invalid parameters or request",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            ), @ApiResponse(
                    responseCode = "403",
                    description = "Not authorized to access the resource",
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
    @ASRestClientParams.ASRestClientRecordBinsQueryParam
    @ASRestClientPolicyQueryParams
    public RestClientRecord getRecordNamespaceSetKey(
            @Parameter(
                    description = NAMESPACE_NOTES, required = true
            ) @PathVariable(value = "namespace") String namespace,
            @Parameter(description = SET_NOTES, required = true) @PathVariable(
                    value = "set"
            ) String set, @Parameter(
            description = USERKEY_NOTES, required = true
    ) @PathVariable(value = "key") String key,
            @Parameter(hidden = true) @RequestParam MultiValueMap<String, String> requestParams, @RequestHeader(
            value = "Authorization", required = false
    ) String basicAuth
    ) {

        String[] bins = RequestParamHandler.getBinsFromMap(requestParams);
        RecordKeyType keyType = RequestParamHandler.getKeyTypeFromMap(requestParams);
        Policy policy = RequestParamHandler.getPolicy(requestParams);
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);

        return service.fetchRecord(authDetails, namespace, set, key, bins, keyType, policy);
    }

    @Operation(summary = GET_RECORD_NOTES, operationId = "getRecordNamespaceKey")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200", description = "Metadata and bins for a record returned successfully."
                    ), @ApiResponse(
                    responseCode = "400",
                    description = "Invalid parameters or request",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            ), @ApiResponse(
                    responseCode = "403",
                    description = "Not authorized to access the resource",
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
    @ASRestClientParams.ASRestClientRecordBinsQueryParam
    @ASRestClientPolicyQueryParams
    public RestClientRecord getRecordNamespaceKey(
            @Parameter(
                    description = NAMESPACE_NOTES, required = true
            ) @PathVariable(value = "namespace") String namespace, @Parameter(
            description = USERKEY_NOTES, required = true
    ) @PathVariable(value = "key") String key,
            @Parameter(hidden = true) @RequestParam MultiValueMap<String, String> requestParams, @RequestHeader(
            value = "Authorization", required = false
    ) String basicAuth
    ) {

        String[] bins = RequestParamHandler.getBinsFromMap(requestParams);
        RecordKeyType keyType = RequestParamHandler.getKeyTypeFromMap(requestParams);
        Policy policy = RequestParamHandler.getPolicy(requestParams);
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);

        return service.fetchRecord(authDetails, namespace, null, key, bins, keyType, policy);
    }

    /*
     **************************************************
     *                     DELETE                     *
     **************************************************
     */
    @Operation(summary = DELETE_RECORD_NOTES, operationId = "deleteRecordNamespaceSetKey")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "204", description = "Deleted a record successfully, no content expected."
                    ), @ApiResponse(
                    responseCode = "400",
                    description = "Invalid parameters or request",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            ), @ApiResponse(
                    responseCode = "403",
                    description = "Not authorized to access the resource",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            ), @ApiResponse(
                    responseCode = "404",
                    description = "Record not found.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            ), @ApiResponse(
                    responseCode = "409",
                    description = "Generation mismatch for operation.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            )
            }
    )
    @DefaultRestClientAPIResponses
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{namespace}/{set}/{key}", produces = {"application/json", "application/msgpack"})
    @ASRestClientParams.ASRestClientKeyTypeQueryParam
    @ASRestClientWritePolicyQueryParams
    public void deleteRecordNamespaceSetKey(
            @Parameter(
                    description = NAMESPACE_NOTES, required = true
            ) @PathVariable(value = "namespace") String namespace, @Parameter(
            description = SET_NOTES, required = true
    ) @PathVariable(value = "set") String set, @Parameter(
            description = USERKEY_NOTES, required = true
    ) @PathVariable(value = "key") String key,
            @Parameter(hidden = true) @RequestParam Map<String, String> requestParams, @RequestHeader(
            value = "Authorization", required = false
    ) String basicAuth
    ) {

        RecordKeyType keyType = RequestParamHandler.getKeyTypeFromMap(requestParams);
        WritePolicy policy = RequestParamHandler.getWritePolicy(requestParams);
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);

        service.deleteRecord(authDetails, namespace, set, key, keyType, policy);
    }

    @Operation(summary = DELETE_RECORD_NOTES, operationId = "deleteRecordNamespaceKey")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "204", description = "Deleted a record successfully, no content expected."
                    ), @ApiResponse(
                    responseCode = "400",
                    description = "Invalid parameters or request",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            ), @ApiResponse(
                    responseCode = "403",
                    description = "Not authorized to access the resource",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            ), @ApiResponse(
                    responseCode = "404",
                    description = "Record not found.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            ), @ApiResponse(
                    responseCode = "409",
                    description = "Generation mismatch for operation.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            )
            }
    )
    @DefaultRestClientAPIResponses
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{namespace}/{key}", produces = {"application/json", "application/msgpack"})
    @ASRestClientParams.ASRestClientKeyTypeQueryParam
    @ASRestClientWritePolicyQueryParams
    public void deleteRecordNamespaceKey(
            @Parameter(
                    description = NAMESPACE_NOTES, required = true
            ) @PathVariable(value = "namespace") String namespace, @Parameter(
            description = USERKEY_NOTES, required = true
    ) @PathVariable(value = "key") String key,
            @Parameter(hidden = true) @RequestParam Map<String, String> requestParams,
            @RequestHeader(value = "Authorization", required = false) String basicAuth
    ) {

        RecordKeyType keyType = RequestParamHandler.getKeyTypeFromMap(requestParams);
        WritePolicy policy = RequestParamHandler.getWritePolicy(requestParams);
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);

        service.deleteRecord(authDetails, namespace, null, key, keyType, policy);
    }

    /*
     **************************************************
     *                     PUT                        *
     **************************************************
     */
    @Operation(summary = REPLACE_RECORD_NOTES, operationId = "replaceRecordNamespaceSetKey")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "204", description = "Modified record successfully, no content expected."
                    ), @ApiResponse(
                    responseCode = "400",
                    description = "Invalid parameters or request",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            ), @ApiResponse(
                    responseCode = "403",
                    description = "Not authorized to access the resource",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            ), @ApiResponse(
                    responseCode = "404",
                    description = "Record not found.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            ), @ApiResponse(
                    responseCode = "409",
                    description = "Generation mismatch for operation.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            )
            }
    )
    @DefaultRestClientAPIResponses
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PutMapping(
            value = "/{namespace}/{set}/{key}", consumes = "application/json", produces = {"application/json"}
    )
    @ASRestClientParams.ASRestClientKeyTypeQueryParam
    @ASRestClientWritePolicyQueryParams
    public void replaceRecordNamespaceSetKey(
            @Parameter(
                    description = NAMESPACE_NOTES, required = true
            ) @PathVariable(value = "namespace") String namespace, @Parameter(
            description = SET_NOTES, required = true
    ) @PathVariable(value = "set") String set, @Parameter(description = USERKEY_NOTES, required = true) @PathVariable(
            value = "key"
    ) String key, @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = STORE_BINS_NOTES, required = true, content = @Content(
            examples = @ExampleObject(
                    name = RequestBodyExamples.BINS_NAME, value = RequestBodyExamples.BINS_VALUE
            )
    )
    ) @RequestBody Map<String, Object> bins, @Parameter(hidden = true) @RequestParam Map<String, String> requestParams,
            @RequestHeader(
                    value = "Authorization", required = false
            ) String basicAuth
    ) {

        RecordKeyType keyType = RequestParamHandler.getKeyTypeFromMap(requestParams);
        WritePolicy policy = RequestParamHandler.getWritePolicy(requestParams, RecordExistsAction.REPLACE_ONLY);
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);

        service.storeRecord(authDetails, namespace, set, key, bins, keyType, policy);
    }

    @Operation(summary = REPLACE_RECORD_NOTES, operationId = "replaceRecordNamespaceSetKeyMP")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "204", description = "Modified record successfully, no content expected."
                    ), @ApiResponse(
                    responseCode = "400",
                    description = "Invalid parameters or request",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            ), @ApiResponse(
                    responseCode = "403",
                    description = "Not authorized to access the resource",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            ), @ApiResponse(
                    responseCode = "404",
                    description = "Record not found.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            ), @ApiResponse(
                    responseCode = "409",
                    description = "Generation mismatch for operation.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            )
            }
    )
    @DefaultRestClientAPIResponses
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PutMapping(
            value = "/{namespace}/{set}/{key}", consumes = "application/msgpack", produces = {"application/msgpack"}
    )
    @ASRestClientParams.ASRestClientKeyTypeQueryParam
    @ASRestClientWritePolicyQueryParams
    public void replaceRecordNamespaceSetKeyMP(
            @PathVariable(value = "namespace") String namespace, @PathVariable(value = "set") String set,
            @PathVariable(value = "key") String key, InputStream dataStream,
            @Parameter(hidden = true) @RequestParam Map<String, String> requestParams, @RequestHeader(
            value = "Authorization", required = false
    ) String basicAuth
    ) {

        RecordKeyType keyType = RequestParamHandler.getKeyTypeFromMap(requestParams);
        WritePolicy policy = RequestParamHandler.getWritePolicy(requestParams, RecordExistsAction.REPLACE_ONLY);
        Map<String, Object> bins = binsFromMsgPackStream(dataStream);
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);

        service.storeRecord(authDetails, namespace, set, key, bins, keyType, policy);
    }

    @Operation(summary = REPLACE_RECORD_NOTES, operationId = "replaceRecordNamespaceKey")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "204", description = "Modified record successfully, no content expected."
                    ), @ApiResponse(
                    responseCode = "400",
                    description = "Invalid parameters or request",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            ), @ApiResponse(
                    responseCode = "403",
                    description = "Not authorized to access the resource",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            ), @ApiResponse(
                    responseCode = "404",
                    description = "Record not found.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            ), @ApiResponse(
                    responseCode = "409",
                    description = "Generation mismatch for operation.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            )
            }
    )
    @DefaultRestClientAPIResponses
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PutMapping(
            value = "/{namespace}/{key}",
            consumes = "application/json",
            produces = {"application/json", "application/msgpack"}
    )
    @ASRestClientParams.ASRestClientKeyTypeQueryParam
    @ASRestClientWritePolicyQueryParams
    public void replaceRecordNamespaceKey(
            @Parameter(
                    description = NAMESPACE_NOTES, required = true
            ) @PathVariable(value = "namespace") String namespace, @Parameter(
            description = USERKEY_NOTES, required = true
    ) @PathVariable(value = "key") String key, @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = STORE_BINS_NOTES, required = true, content = @Content(
            examples = @ExampleObject(
                    name = RequestBodyExamples.BINS_NAME, value = RequestBodyExamples.BINS_VALUE
            )
    )
    ) @RequestBody Map<String, Object> bins, @Parameter(hidden = true) @RequestParam Map<String, String> requestParams,
            @RequestHeader(value = "Authorization", required = false) String basicAuth
    ) {

        RecordKeyType keyType = RequestParamHandler.getKeyTypeFromMap(requestParams);
        WritePolicy policy = RequestParamHandler.getWritePolicy(requestParams, RecordExistsAction.REPLACE_ONLY);
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);

        service.storeRecord(authDetails, namespace, null, key, bins, keyType, policy);
    }

    @Hidden
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PutMapping(
            value = "/{namespace}/{key}",
            consumes = "application/msgpack",
            produces = {"application/json", "application/msgpack"}
    )
    public void replaceRecordNamespaceKeyMP(
            @PathVariable(value = "namespace") String namespace, @PathVariable(value = "key") String key,
            InputStream dataStream, @RequestParam Map<String, String> requestParams, @RequestHeader(
            value = "Authorization", required = false
    ) String basicAuth
    ) {

        RecordKeyType keyType = RequestParamHandler.getKeyTypeFromMap(requestParams);
        WritePolicy policy = RequestParamHandler.getWritePolicy(requestParams, RecordExistsAction.REPLACE_ONLY);
        Map<String, Object> bins = binsFromMsgPackStream(dataStream);
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);

        service.storeRecord(authDetails, namespace, null, key, bins, keyType, policy);
    }

    /*
     **************************************************
     *                     POST                       *
     **************************************************
     */
    @Operation(summary = CREATE_RECORD_NOTES, operationId = "createRecordNamespaceSetKey")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201", description = "Created a new record successfully."
                    ), @ApiResponse(
                    responseCode = "400",
                    description = "Invalid parameters or request",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            ), @ApiResponse(
                    responseCode = "403",
                    description = "Not authorized to access the resource",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            ), @ApiResponse(
                    responseCode = "404",
                    description = "Namespace does not exist.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            ), @ApiResponse(
                    responseCode = "409",
                    description = "Record Already exists.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            )
            }
    )
    @DefaultRestClientAPIResponses
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(
            value = "/{namespace}/{set}/{key}",
            consumes = {"application/json"},
            produces = {"application/json", "application/msgpack"}
    )
    @ASRestClientParams.ASRestClientKeyTypeQueryParam
    @ASRestClientWritePolicyQueryParams
    public void createRecordNamespaceSetKey(
            @Parameter(
                    description = NAMESPACE_NOTES, required = true
            ) @PathVariable(value = "namespace") String namespace, @Parameter(
            description = SET_NOTES, required = true
    ) @PathVariable(value = "set") String set, @Parameter(
            description = USERKEY_NOTES, required = true
    ) @PathVariable(value = "key") String key, @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = STORE_BINS_NOTES, required = true, content = @Content(
            examples = @ExampleObject(
                    name = RequestBodyExamples.BINS_NAME, value = RequestBodyExamples.BINS_VALUE
            )
    )
    ) @RequestBody Map<String, Object> bins, @Parameter(hidden = true) @RequestParam Map<String, String> requestParams,
            @RequestHeader(
                    value = "Authorization", required = false
            ) String basicAuth
    ) {

        RecordKeyType keyType = RequestParamHandler.getKeyTypeFromMap(requestParams);
        WritePolicy policy = RequestParamHandler.getWritePolicy(requestParams, RecordExistsAction.CREATE_ONLY);
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);

        service.storeRecord(authDetails, namespace, set, key, bins, keyType, policy);
    }

    @Hidden
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(
            value = "/{namespace}/{set}/{key}",
            consumes = {"application/msgpack"},
            produces = {"application/json", "application/msgpack"}
    )
    public void createRecordNamespaceSetKeyMP(
            @PathVariable(value = "namespace") String namespace, @PathVariable(value = "set") String set,
            @PathVariable(value = "key") String key, InputStream dataStream,
            @RequestParam Map<String, String> requestParams, @RequestHeader(
            value = "Authorization", required = false
    ) String basicAuth
    ) {

        RecordKeyType keyType = RequestParamHandler.getKeyTypeFromMap(requestParams);
        WritePolicy policy = RequestParamHandler.getWritePolicy(requestParams, RecordExistsAction.CREATE_ONLY);
        Map<String, Object> bins = binsFromMsgPackStream(dataStream);
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);

        service.storeRecord(authDetails, namespace, set, key, bins, keyType, policy);
    }

    @Operation(summary = CREATE_RECORD_NOTES, operationId = "createRecordNamespaceKey")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201", description = "Created a new record successfully."
                    ), @ApiResponse(
                    responseCode = "400",
                    description = "Invalid parameters or request",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            ), @ApiResponse(
                    responseCode = "403",
                    description = "Not authorized to access the resource",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            ), @ApiResponse(
                    responseCode = "404",
                    description = "Namespace does not exist.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            ), @ApiResponse(
                    responseCode = "409",
                    description = "Record Already exists.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            )
            }
    )
    @DefaultRestClientAPIResponses
    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping(
            value = "/{namespace}/{key}",
            consumes = {"application/json"},
            produces = {"application/json", "application/msgpack"}
    )
    @ASRestClientParams.ASRestClientKeyTypeQueryParam
    @ASRestClientWritePolicyQueryParams
    public void createRecordNamespaceKey(
            @Parameter(
                    description = NAMESPACE_NOTES, required = true
            ) @PathVariable(value = "namespace") String namespace, @Parameter(
            description = USERKEY_NOTES, required = true
    ) @PathVariable(value = "key") String key, @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = STORE_BINS_NOTES, required = true, content = @Content(
            examples = @ExampleObject(
                    name = RequestBodyExamples.BINS_NAME, value = RequestBodyExamples.BINS_VALUE
            )
    )
    ) @RequestBody Map<String, Object> bins, @Parameter(hidden = true) @RequestParam Map<String, String> requestParams,
            @RequestHeader(value = "Authorization", required = false) String basicAuth
    ) {

        RecordKeyType keyType = RequestParamHandler.getKeyTypeFromMap(requestParams);
        WritePolicy policy = RequestParamHandler.getWritePolicy(requestParams, RecordExistsAction.CREATE_ONLY);
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);

        service.storeRecord(authDetails, namespace, null, key, bins, keyType, policy);
    }

    @Hidden
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(
            value = "/{namespace}/{key}",
            consumes = {"application/msgpack"},
            produces = {"application/json", "application/msgpack"}
    )
    public void createRecordNamespaceKeyMP(
            @PathVariable(value = "namespace") String namespace, @PathVariable(value = "key") String key,
            InputStream dataStream, @RequestParam Map<String, String> requestParams,
            @RequestHeader(value = "Authorization", required = false) String basicAuth
    ) {

        RecordKeyType keyType = RequestParamHandler.getKeyTypeFromMap(requestParams);
        WritePolicy policy = RequestParamHandler.getWritePolicy(requestParams, RecordExistsAction.CREATE_ONLY);
        Map<String, Object> bins = binsFromMsgPackStream(dataStream);
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);

        service.storeRecord(authDetails, namespace, null, key, bins, keyType, policy);
    }

    /*
     **************************************************
     *                     PATCH                      *
     **************************************************
     */
    @Operation(summary = UPDATE_RECORD_NOTES, operationId = "updateRecordNamespaceSetKey")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "204", description = "Modified record successfully, no content expected."
                    ), @ApiResponse(
                    responseCode = "400",
                    description = "Invalid parameters or request",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            ), @ApiResponse(
                    responseCode = "403",
                    description = "Not authorized to access the resource",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            ), @ApiResponse(
                    responseCode = "404",
                    description = "Record does not exists.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            ), @ApiResponse(
                    responseCode = "409",
                    description = "Generation mismatch for operation.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            )
            }
    )
    @DefaultRestClientAPIResponses
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PatchMapping(
            value = "/{namespace}/{set}/{key}",
            consumes = {"application/json"},
            produces = {"application/json", "application/msgpack"}
    )
    @ASRestClientParams.ASRestClientKeyTypeQueryParam
    @ASRestClientWritePolicyQueryParams
    public void updateRecordNamespaceSetKey(
            @Parameter(
                    description = NAMESPACE_NOTES, required = true
            ) @PathVariable(value = "namespace") String namespace, @Parameter(
            description = SET_NOTES, required = true
    ) @PathVariable(value = "set") String set, @Parameter(
            description = USERKEY_NOTES, required = true
    ) @PathVariable(value = "key") String key, @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = STORE_BINS_NOTES, required = true, content = @Content(
            examples = @ExampleObject(
                    name = RequestBodyExamples.BINS_NAME, value = RequestBodyExamples.BINS_VALUE
            )
    )
    ) @RequestBody Map<String, Object> bins, @Parameter(hidden = true) @RequestParam Map<String, String> requestParams,
            @RequestHeader(
                    value = "Authorization", required = false
            ) String basicAuth
    ) {

        RecordKeyType keyType = RequestParamHandler.getKeyTypeFromMap(requestParams);
        WritePolicy policy = RequestParamHandler.getWritePolicy(requestParams, RecordExistsAction.UPDATE_ONLY);
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);

        service.storeRecord(authDetails, namespace, set, key, bins, keyType, policy);
    }

    @Hidden
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PatchMapping(
            value = "/{namespace}/{set}/{key}",
            consumes = "application/msgpack",
            produces = {"application/json", "application/msgpack"}
    )
    public void updateRecordNamespaceSetKeyMP(
            @PathVariable(value = "namespace") String namespace, @PathVariable(value = "set") String set,
            @PathVariable(value = "key") String key, InputStream dataStream,
            @RequestParam Map<String, String> requestParams, @RequestHeader(
            value = "Authorization", required = false
    ) String basicAuth
    ) {

        RecordKeyType keyType = RequestParamHandler.getKeyTypeFromMap(requestParams);
        WritePolicy policy = RequestParamHandler.getWritePolicy(requestParams, RecordExistsAction.UPDATE_ONLY);
        Map<String, Object> bins = binsFromMsgPackStream(dataStream);
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);

        service.storeRecord(authDetails, namespace, set, key, bins, keyType, policy);
    }

    @Operation(summary = UPDATE_RECORD_NOTES, operationId = "updateRecordNamespaceKey")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "204", description = "Modified record successfully, no content expected."
                    ), @ApiResponse(
                    responseCode = "400",
                    description = "Invalid parameters or request",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            ), @ApiResponse(
                    responseCode = "403",
                    description = "Not authorized to access the resource",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            ), @ApiResponse(
                    responseCode = "404",
                    description = "Record does not exists.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            ), @ApiResponse(
                    responseCode = "409",
                    description = "Generation mismatch for operation.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            )
            }
    )
    @DefaultRestClientAPIResponses
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PatchMapping(
            value = "/{namespace}/{key}",
            consumes = "application/json",
            produces = {"application/json", "application/msgpack"}
    )
    @ASRestClientParams.ASRestClientKeyTypeQueryParam
    @ASRestClientWritePolicyQueryParams
    public void updateRecordNamespaceKey(
            @Parameter(
                    description = NAMESPACE_NOTES, required = true
            ) @PathVariable(value = "namespace") String namespace, @Parameter(
            description = USERKEY_NOTES, required = true
    ) @PathVariable(value = "key") String key, @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = STORE_BINS_NOTES, required = true, content = @Content(
            examples = @ExampleObject(
                    name = RequestBodyExamples.BINS_NAME, value = RequestBodyExamples.BINS_VALUE
            )
    )
    ) @RequestBody Map<String, Object> bins, @Parameter(hidden = true) @RequestParam Map<String, String> requestParams,
            @RequestHeader(value = "Authorization", required = false) String basicAuth
    ) {

        RecordKeyType keyType = RequestParamHandler.getKeyTypeFromMap(requestParams);
        WritePolicy policy = RequestParamHandler.getWritePolicy(requestParams, RecordExistsAction.UPDATE_ONLY);
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);

        service.storeRecord(authDetails, namespace, null, key, bins, keyType, policy);
    }

    @Hidden
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @PatchMapping(
            value = "/{namespace}/{key}",
            consumes = "application/msgpack",
            produces = {"application/json", "application/msgpack"}
    )
    public void updateRecordNamespaceKeyMP(
            @PathVariable(value = "namespace") String namespace, @PathVariable(value = "key") String key,
            InputStream dataStream, @RequestParam Map<String, String> requestParams,
            @RequestHeader(value = "Authorization", required = false) String basicAuth
    ) {

        RecordKeyType keyType = RequestParamHandler.getKeyTypeFromMap(requestParams);
        WritePolicy policy = RequestParamHandler.getWritePolicy(requestParams, RecordExistsAction.UPDATE_ONLY);
        Map<String, Object> bins = binsFromMsgPackStream(dataStream);
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);

        service.storeRecord(authDetails, namespace, null, key, bins, keyType, policy);
    }

    /*
     **************************************************
     *                     HEAD                       *
     **************************************************
     */
    @Operation(summary = "recordExistsNamespaceSetKey", operationId = "Check if a record exists")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200", description = "Record exists indication returned successfully."
                    ), @ApiResponse(
                    responseCode = "404",
                    description = "Record does not exists.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            )
            }
    )
    @DefaultRestClientAPIResponses
    @RequestMapping(
            method = RequestMethod.HEAD,
            value = "/{namespace}/{set}/{key}",
            produces = {"application/json", "application/msgpack"}
    )
    public void recordExistsNamespaceSetKey(
            @Parameter(
                    description = NAMESPACE_NOTES, required = true
            ) @PathVariable(value = "namespace") String namespace, @Parameter(
            description = SET_NOTES, required = true
    ) @PathVariable(value = "set") String set, @Parameter(
            description = USERKEY_NOTES, required = true
    ) @PathVariable(value = "key") String key, @Parameter(hidden = true) HttpServletResponse res, @Parameter(
            name = "keytype", description = APIDescriptors.KEYTYPE_NOTES
    ) @RequestParam(value = "keytype", required = false) RecordKeyType keyType, @RequestHeader(
            value = "Authorization", required = false
    ) String basicAuth
    ) {

        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);
        if (!service.recordExists(authDetails, namespace, set, key, keyType)) {
            try {
                res.sendError(404);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /*
     **************************************************
     *                     HEAD                       *
     **************************************************
     */
    @Operation(summary = "Check if a record exists", operationId = "recordExistsNamespaceKey")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200", description = "Record exists indication returned successfully."
                    ), @ApiResponse(
                    responseCode = "404",
                    description = "Record does not exists.",
                    content = @Content(schema = @Schema(implementation = RestClientError.class))
            )
            }
    )
    @ApiResponse(responseCode = "404", description = "Record does not exist.")
    @DefaultRestClientAPIResponses
    @RequestMapping(
            method = RequestMethod.HEAD,
            value = "/{namespace}/{key}",
            produces = {"application/json", "application/msgpack"}
    )
    public void recordExistsNamespaceKey(
            @Parameter(
                    description = NAMESPACE_NOTES, required = true
            ) @PathVariable(value = "namespace") String namespace, @Parameter(
            description = USERKEY_NOTES, required = true
    ) @PathVariable(value = "key") String key, @Parameter(hidden = true) HttpServletResponse res, @Parameter(
            name = "keytype", description = APIDescriptors.KEYTYPE_NOTES
    ) @RequestParam(value = "keytype", required = false) RecordKeyType keyType,
            @RequestHeader(value = "Authorization", required = false) String basicAuth
    ) {

        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);
        if (!service.recordExists(authDetails, namespace, null, key, keyType)) {
            try {
                res.sendError(404);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private Map<String, Object> binsFromMsgPackStream(InputStream dataStream) {
        MsgPackBinParser parser = new MsgPackBinParser(dataStream);
        return parser.parseBins();
    }
}