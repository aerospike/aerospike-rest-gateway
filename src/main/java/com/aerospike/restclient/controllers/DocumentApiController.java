/*
 * Copyright 2021 Aerospike, Inc.
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
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.aerospike.restclient.util.HeaderHandler;
import com.aerospike.restclient.util.RequestParamHandler;
import com.aerospike.restclient.util.annotations.ASRestClientPolicyQueryParams;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Api(tags = "Document API Operations", description = "Perform operations on records using JSONPath queries.")
@RestController
@RequestMapping("/v1/document")
public class DocumentApiController {

    public static final String NAMESPACE_NOTES = "Namespace for the record; equivalent to database name.";
    public static final String SET_NOTES = "Set for the record; equivalent to database table.";
    public static final String USER_KEY_NOTES = "User key for the record.";
    public static final String BIN_NAME_NOTES = "Bin name for the document.";
    public static final String JSON_PATH_NOTES = "JSONPath expression specifies a path to an element " +
            "(or a set of elements) in a JSON structure. ";

    public static final String GET_DOCUMENT_NOTES = "Retrieve the object in the document with key documentKey" +
            " that is referenced by the JSON path.";
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
    @RequestMapping(method = RequestMethod.GET, value = "/{namespace}/{key}",
            produces = {"application/json", "application/msgpack"})
    @ApiOperation(value = GET_DOCUMENT_NOTES, nickname = "getDocumentObject")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 404, response = RestClientError.class, message = "Record not found.",
                    examples = @Example(value = {@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
            @ApiResponse(code = 403, response = RestClientError.class, message = "Not authorized to access the resource",
                    examples = @Example(value = {@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
            @ApiResponse(code = 400, response = RestClientError.class, message = "Invalid parameters or request",
                    examples = @Example(value = {@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' ")}))
    })
    @ASRestClientPolicyQueryParams
    public Map<String, Object>
    getDocumentObject(
            @ApiParam(value = NAMESPACE_NOTES, required = true) @PathVariable(value = "namespace") String namespace,
            @ApiParam(value = USER_KEY_NOTES, required = true) @PathVariable(value = "key") String key,
            @ApiIgnore @RequestParam MultiValueMap<String, String> requestParams,
            @RequestHeader(value = "Authorization", required = false) String basicAuth) {

        AerospikeAPIConstants.RecordKeyType keyType = RequestParamHandler.getKeyTypeFromMap(requestParams);
        Policy policy = RequestParamHandler.getPolicy(requestParams);
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);
        List<String> bins = Arrays.asList(RequestParamHandler.getBinsFromMap(requestParams));
        String jsonPath = RequestParamHandler.getJsonPathFromMap(requestParams);

        return service.getObject(authDetails, namespace, null, key, bins, jsonPath, keyType, policy);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{namespace}/{set}/{key}",
            produces = {"application/json", "application/msgpack"})
    @ApiOperation(value = GET_DOCUMENT_NOTES, nickname = "getDocumentObjectSet")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 404, response = RestClientError.class, message = "Record not found.",
                    examples = @Example(value = {@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
            @ApiResponse(code = 403, response = RestClientError.class, message = "Not authorized to access the resource",
                    examples = @Example(value = {@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
            @ApiResponse(code = 400, response = RestClientError.class, message = "Invalid parameters or request",
                    examples = @Example(value = {@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' ")}))
    })
    @ASRestClientPolicyQueryParams
    public Map<String, Object>
    getDocumentObjectSet(
            @ApiParam(value = NAMESPACE_NOTES, required = true) @PathVariable(value = "namespace") String namespace,
            @ApiParam(value = SET_NOTES, required = true) @PathVariable(value = "set") String set,
            @ApiParam(value = USER_KEY_NOTES, required = true) @PathVariable(value = "key") String key,
            @ApiIgnore @RequestParam MultiValueMap<String, String> requestParams,
            @RequestHeader(value = "Authorization", required = false) String basicAuth) {

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
    @RequestMapping(method = RequestMethod.PUT, value = "/{namespace}/{key}",
            produces = {"application/json", "application/msgpack"})
    @ApiOperation(value = PUT_DOCUMENT_NOTES, nickname = "putDocumentObject")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 404, response = RestClientError.class, message = "Record not found.",
                    examples = @Example(value = {@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
            @ApiResponse(code = 403, response = RestClientError.class, message = "Not authorized to access the resource",
                    examples = @Example(value = {@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
            @ApiResponse(code = 400, response = RestClientError.class, message = "Invalid parameters or request",
                    examples = @Example(value = {@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' ")}))
    })
    @ASRestClientPolicyQueryParams
    public void
    putDocumentObject(
            @ApiParam(value = NAMESPACE_NOTES, required = true) @PathVariable(value = "namespace") String namespace,
            @ApiParam(value = USER_KEY_NOTES, required = true) @PathVariable(value = "key") String key,
            @ApiParam(value = JSON_OBJECT_NOTES, required = true) @RequestBody Object jsonObject,
            @ApiIgnore @RequestParam MultiValueMap<String, String> requestParams,
            @RequestHeader(value = "Authorization", required = false) String basicAuth) {

        AerospikeAPIConstants.RecordKeyType keyType = RequestParamHandler.getKeyTypeFromMap(requestParams);
        WritePolicy policy = RequestParamHandler.getWritePolicy(requestParams.toSingleValueMap());
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);
        List<String> bins = Arrays.asList(RequestParamHandler.getBinsFromMap(requestParams));
        String jsonPath = RequestParamHandler.getJsonPathFromMap(requestParams);

        service.putObject(authDetails, namespace, null, key, bins, jsonPath, jsonObject, keyType, policy);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{namespace}/{set}/{key}",
            produces = {"application/json", "application/msgpack"})
    @ApiOperation(value = PUT_DOCUMENT_NOTES, nickname = "putDocumentObjectSet")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 404, response = RestClientError.class, message = "Record not found.",
                    examples = @Example(value = {@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
            @ApiResponse(code = 403, response = RestClientError.class, message = "Not authorized to access the resource",
                    examples = @Example(value = {@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
            @ApiResponse(code = 400, response = RestClientError.class, message = "Invalid parameters or request",
                    examples = @Example(value = {@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' ")}))
    })
    @ASRestClientPolicyQueryParams
    public void
    putDocumentObjectSet(
            @ApiParam(value = NAMESPACE_NOTES, required = true) @PathVariable(value = "namespace") String namespace,
            @ApiParam(value = SET_NOTES, required = true) @PathVariable(value = "set") String set,
            @ApiParam(value = USER_KEY_NOTES, required = true) @PathVariable(value = "key") String key,
            @ApiParam(value = JSON_OBJECT_NOTES, required = true) @RequestBody Object jsonObject,
            @ApiIgnore @RequestParam MultiValueMap<String, String> requestParams,
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
    @RequestMapping(method = RequestMethod.POST, value = "/{namespace}/{key}",
            produces = {"application/json", "application/msgpack"})
    @ApiOperation(value = APPEND_DOCUMENT_NOTES, nickname = "appendDocumentObject")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 404, response = RestClientError.class, message = "Record not found.",
                    examples = @Example(value = {@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
            @ApiResponse(code = 403, response = RestClientError.class, message = "Not authorized to access the resource",
                    examples = @Example(value = {@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
            @ApiResponse(code = 400, response = RestClientError.class, message = "Invalid parameters or request",
                    examples = @Example(value = {@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' ")}))
    })
    @ASRestClientPolicyQueryParams
    public void
    appendDocumentObject(
            @ApiParam(value = NAMESPACE_NOTES, required = true) @PathVariable(value = "namespace") String namespace,
            @ApiParam(value = USER_KEY_NOTES, required = true) @PathVariable(value = "key") String key,
            @ApiParam(value = JSON_OBJECT_NOTES, required = true) @RequestBody Object jsonObject,
            @ApiIgnore @RequestParam MultiValueMap<String, String> requestParams,
            @RequestHeader(value = "Authorization", required = false) String basicAuth) {

        AerospikeAPIConstants.RecordKeyType keyType = RequestParamHandler.getKeyTypeFromMap(requestParams);
        WritePolicy policy = RequestParamHandler.getWritePolicy(requestParams.toSingleValueMap());
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);
        List<String> bins = Arrays.asList(RequestParamHandler.getBinsFromMap(requestParams));
        String jsonPath = RequestParamHandler.getJsonPathFromMap(requestParams);

        service.appendObject(authDetails, namespace, null, key, bins, jsonPath, jsonObject, keyType, policy);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{namespace}/{set}/{key}",
            produces = {"application/json", "application/msgpack"})
    @ApiOperation(value = APPEND_DOCUMENT_NOTES, nickname = "appendDocumentObjectSet")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(code = 404, response = RestClientError.class, message = "Record not found.",
                    examples = @Example(value = {@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
            @ApiResponse(code = 403, response = RestClientError.class, message = "Not authorized to access the resource",
                    examples = @Example(value = {@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
            @ApiResponse(code = 400, response = RestClientError.class, message = "Invalid parameters or request",
                    examples = @Example(value = {@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' ")}))
    })
    @ASRestClientPolicyQueryParams
    public void
    appendDocumentObjectSet(
            @ApiParam(value = NAMESPACE_NOTES, required = true) @PathVariable(value = "namespace") String namespace,
            @ApiParam(value = SET_NOTES, required = true) @PathVariable(value = "set") String set,
            @ApiParam(value = USER_KEY_NOTES, required = true) @PathVariable(value = "key") String key,
            @ApiParam(value = JSON_OBJECT_NOTES, required = true) @RequestBody Object jsonObject,
            @ApiIgnore @RequestParam MultiValueMap<String, String> requestParams,
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
    @RequestMapping(method = RequestMethod.DELETE, value = "/{namespace}/{key}",
            produces = {"application/json", "application/msgpack"})
    @ApiOperation(value = DELETE_DOCUMENT_NOTES, nickname = "deleteDocumentObject")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(value = {
            @ApiResponse(code = 404, response = RestClientError.class, message = "Record not found.",
                    examples = @Example(value = {@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
            @ApiResponse(code = 403, response = RestClientError.class, message = "Not authorized to access the resource",
                    examples = @Example(value = {@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
            @ApiResponse(code = 400, response = RestClientError.class, message = "Invalid parameters or request",
                    examples = @Example(value = {@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' ")}))
    })
    @ASRestClientPolicyQueryParams
    public void
    deleteDocumentObject(
            @ApiParam(value = NAMESPACE_NOTES, required = true) @PathVariable(value = "namespace") String namespace,
            @ApiParam(value = USER_KEY_NOTES, required = true) @PathVariable(value = "key") String key,
            @ApiIgnore @RequestParam MultiValueMap<String, String> requestParams,
            @RequestHeader(value = "Authorization", required = false) String basicAuth) {

        AerospikeAPIConstants.RecordKeyType keyType = RequestParamHandler.getKeyTypeFromMap(requestParams);
        WritePolicy policy = RequestParamHandler.getWritePolicy(requestParams.toSingleValueMap());
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);
        List<String> bins = Arrays.asList(RequestParamHandler.getBinsFromMap(requestParams));
        String jsonPath = RequestParamHandler.getJsonPathFromMap(requestParams);

        service.deleteObject(authDetails, namespace, null, key, bins, jsonPath, keyType, policy);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{namespace}/{set}/{key}",
            produces = {"application/json", "application/msgpack"})
    @ApiOperation(value = DELETE_DOCUMENT_NOTES, nickname = "deleteDocumentObjectSet")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(value = {
            @ApiResponse(code = 404, response = RestClientError.class, message = "Record not found.",
                    examples = @Example(value = {@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
            @ApiResponse(code = 403, response = RestClientError.class, message = "Not authorized to access the resource",
                    examples = @Example(value = {@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
            @ApiResponse(code = 400, response = RestClientError.class, message = "Invalid parameters or request",
                    examples = @Example(value = {@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' ")}))
    })
    @ASRestClientPolicyQueryParams
    public void
    deleteDocumentObjectSet(
            @ApiParam(value = NAMESPACE_NOTES, required = true) @PathVariable(value = "namespace") String namespace,
            @ApiParam(value = SET_NOTES, required = true) @PathVariable(value = "set") String set,
            @ApiParam(value = USER_KEY_NOTES, required = true) @PathVariable(value = "key") String key,
            @ApiIgnore @RequestParam MultiValueMap<String, String> requestParams,
            @RequestHeader(value = "Authorization", required = false) String basicAuth) {

        AerospikeAPIConstants.RecordKeyType keyType = RequestParamHandler.getKeyTypeFromMap(requestParams);
        WritePolicy policy = RequestParamHandler.getWritePolicy(requestParams.toSingleValueMap());
        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);
        List<String> bins = Arrays.asList(RequestParamHandler.getBinsFromMap(requestParams));
        String jsonPath = RequestParamHandler.getJsonPathFromMap(requestParams);

        service.deleteObject(authDetails, namespace, set, key, bins, jsonPath, keyType, policy);
    }
}
