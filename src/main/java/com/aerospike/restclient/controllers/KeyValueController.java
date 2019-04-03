/*
 * Copyright 2019 Aerospike, Inc.
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

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.aerospike.client.policy.Policy;
import com.aerospike.client.policy.RecordExistsAction;
import com.aerospike.client.policy.WritePolicy;
import com.aerospike.restclient.domain.RestClientError;
import com.aerospike.restclient.domain.RestClientRecord;
import com.aerospike.restclient.service.AerospikeRecordService;
import com.aerospike.restclient.util.AerospikeAPIConstants.RecordKeyType;
import com.aerospike.restclient.util.RequestParamHandler;
import com.aerospike.restclient.util.annotations.ASRestClientPolicyQueryParams;
import com.aerospike.restclient.util.annotations.ASRestClientWritePolicyQueryParams;
import com.aerospike.restclient.util.deserializers.MsgPackBinParser;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags="Key Value Operations", description="Perform simple operations on a single record.")
@RestController
@RequestMapping("/v1/kvs")
public class KeyValueController {

	@Autowired private AerospikeRecordService service;

	/*
	 **************************************************
	 *                     GET                        *
	 **************************************************
	 */
	@RequestMapping(method=RequestMethod.GET, value="/{namespace}/{set}/{key}",
			produces= {"application/json", "application/msgpack"})
	@ApiOperation(value="${RestClient.KVS.GetRecord.notes}", nickname="getRecordNamespaceSetKey")
	@ResponseStatus(HttpStatus.OK)
	@ApiResponses(value= {
			@ApiResponse(code=404, response=RestClientError.class, message = "Record not found.",
					examples= @Example(value = {@ExampleProperty(mediaType="Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
			@ApiResponse(code=403, response=RestClientError.class, message = "Not authorized to access the resource",
			examples= @Example(value = {@ExampleProperty(mediaType="Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
			@ApiResponse(code=400, response=RestClientError.class, message = "Invalid parameters or request",
			examples= @Example(value = {@ExampleProperty(mediaType="Example json", value = "{'inDoubt': false, 'message': 'A message' ")}))

	})
	@ASRestClientPolicyQueryParams
	public RestClientRecord
	getRecordNamespaceSetKey(
			@ApiParam(value="${RestClient.Record.namespace.notes}", required=true) @PathVariable(value="namespace")String namespace,
			@ApiParam(value="${RestClient.Record.set.notes}", required=true) @PathVariable(value="set") String set,
			@ApiParam(value="${RestClient.Record.userkey.notes}", required=true) @PathVariable(value="key")String key,
			@ApiIgnore @RequestParam MultiValueMap<String, String> requestParams) {


		String[] bins = RequestParamHandler.getBinsFromMap(requestParams);
		RecordKeyType keyType = RequestParamHandler.getKeyTypeFromMap(requestParams);
		Policy policy = RequestParamHandler.getPolicy(requestParams);

		return service.fetchRecord(namespace, set, key, bins, keyType, policy);
	}

	@RequestMapping(method=RequestMethod.GET, value="/{namespace}/{key}",
			produces= {"application/json", "application/msgpack"})
	@ApiOperation(value="${RestClient.KVS.GetRecord.notes}", nickname="getRecordNamespaceKey")
	@ResponseStatus(HttpStatus.OK)
	@ApiResponses(value= {
			@ApiResponse(code=404, response=RestClientError.class, message = "Record not found.",
					examples= @Example(value = {@ExampleProperty(mediaType="Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
			@ApiResponse(code=403, response=RestClientError.class, message = "Not authorized to access the resource",
			examples= @Example(value = {@ExampleProperty(mediaType="Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
			@ApiResponse(code=400, response=RestClientError.class, message = "Invalid parameters or request",
			examples= @Example(value = {@ExampleProperty(mediaType="Example json", value = "{'inDoubt': false, 'message': 'A message' ")}))

	})
	@ASRestClientPolicyQueryParams
	public RestClientRecord
	getRecordNamespaceKey(
			@ApiParam(value="${RestClient.Record.namespace.notes}", required=true) @PathVariable(value="namespace")String namespace,
			@ApiParam(value="${RestClient.Record.userkey.notes}", required=true) @PathVariable(value="key")String key,
			@ApiIgnore @RequestParam MultiValueMap<String, String> requestParams) {

		String[] bins = RequestParamHandler.getBinsFromMap(requestParams);
		RecordKeyType keyType = RequestParamHandler.getKeyTypeFromMap(requestParams);
		Policy policy = RequestParamHandler.getPolicy(requestParams);

		return service.fetchRecord(namespace, null, key, bins, keyType, policy);
	}

	/*
	 **************************************************
	 *                     DELETE                     *
	 **************************************************
	 */
	@RequestMapping(method=RequestMethod.DELETE, value="/{namespace}/{set}/{key}",
			produces= {"application/json", "application/msgpack"})
	@ApiOperation(value="${RestClient.KVS.DeleteRecord.notes}", nickname="deleteRecordNamespaceSetKey")
	@ResponseStatus(value=HttpStatus.NO_CONTENT)
	@ApiResponses(value= {
			@ApiResponse(code=404, response=RestClientError.class, message = "Record not found.",
					examples= @Example(value = {@ExampleProperty(mediaType="Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
			@ApiResponse(code=403, response=RestClientError.class, message = "Not authorized to access the resource",
			examples= @Example(value = {@ExampleProperty(mediaType="Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
			@ApiResponse(code=400, response=RestClientError.class, message = "Invalid parameters or request",
			examples= @Example(value = {@ExampleProperty(mediaType="Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
			@ApiResponse(code=409, response=RestClientError.class, message = "Generation mismatch for operation",
			examples= @Example(value = {@ExampleProperty(mediaType="Example json", value = "{'inDoubt': false, 'message': 'A message' ")}))
	})
	@ASRestClientWritePolicyQueryParams
	public void
	deleteRecordNamespaceSetKey(
			@ApiParam(value="${RestClient.Record.namespace.notes}", required=true) @PathVariable(value="namespace")String namespace,
			@ApiParam(value="${RestClient.Record.set.notes}", required=true) @PathVariable(value="set") String set,
			@ApiParam(value="${RestClient.Record.userkey.notes}", required=true) @PathVariable(value="key")String key,
			@ApiIgnore @RequestParam Map<String, String>requestParams) {

		RecordKeyType keyType = RequestParamHandler.getKeyTypeFromMap(requestParams);
		WritePolicy policy = RequestParamHandler.getWritePolicy(requestParams);

		service.deleteRecord(namespace, set, key, keyType, policy);
	}

	@RequestMapping(method=RequestMethod.DELETE, value="/{namespace}/{key}",
			produces= {"application/json", "application/msgpack"})
	@ApiOperation(value="${RestClient.KVS.DeleteRecord.notes}", nickname="deleteRecordNamespaceKey")
	@ResponseStatus(value=HttpStatus.NO_CONTENT)
	@ApiResponses(value= {
			@ApiResponse(code=404, response=RestClientError.class, message = "Record not found.",
					examples= @Example(value = {@ExampleProperty(mediaType="Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
			@ApiResponse(code=403, response=RestClientError.class, message = "Not authorized to access the resource",
			examples= @Example(value = {@ExampleProperty(mediaType="Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
			@ApiResponse(code=400, response=RestClientError.class, message = "Invalid parameters or request",
			examples= @Example(value = {@ExampleProperty(mediaType="Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
			@ApiResponse(code=409, response=RestClientError.class, message = "Generation mismatch for operation",
			examples= @Example(value = {@ExampleProperty(mediaType="Example json", value = "{'inDoubt': false, 'message': 'A message' ")}))
	})
	@ASRestClientWritePolicyQueryParams
	public void
	deleteRecordNamespaceKey(
			@ApiParam(value="${RestClient.Record.namespace.notes}", required=true) @PathVariable(value="namespace")String namespace,
			@ApiParam(value="${RestClient.Record.userkey.notes}", required=true) @PathVariable(value="key")String key,
			@ApiIgnore @RequestParam Map<String, String>requestParams) {

		RecordKeyType keyType = RequestParamHandler.getKeyTypeFromMap(requestParams);
		WritePolicy policy = RequestParamHandler.getWritePolicy(requestParams);

		service.deleteRecord(namespace, null, key, keyType, policy);
	}

	/*
	 **************************************************
	 *                     PUT                        *
	 **************************************************
	 */
	@RequestMapping(method=RequestMethod.PUT, value="/{namespace}/{set}/{key}",
			consumes="application/json", produces= {"application/json", "application/msgpack"})
	@ApiOperation(value="${RestClient.KVS.ReplaceRecord.notes}", consumes="application/json, application/msgpack", nickname="replaceRecordNamespaceSetKey")
	@ResponseStatus(value=HttpStatus.NO_CONTENT)
	@ApiResponses(value= {
			@ApiResponse(code=404, response=RestClientError.class, message = "Record does not exist.",
					examples= @Example(value = {@ExampleProperty(mediaType="Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
			@ApiResponse(code=403, response=RestClientError.class, message = "Not authorized to access the resource",
			examples= @Example(value = {@ExampleProperty(mediaType="Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
			@ApiResponse(code=400, response=RestClientError.class, message = "Invalid parameters or request",
			examples= @Example(value = {@ExampleProperty(mediaType="Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
			@ApiResponse(code=409, response=RestClientError.class, message = "Generation mismatch for operation",
			examples= @Example(value = {@ExampleProperty(mediaType="Example json", value = "{'inDoubt': false, 'message': 'A message' ")}))
	})
	@ASRestClientWritePolicyQueryParams
	public void
	replaceRecordNamespaceSetKey(
			@ApiParam(value="${RestClient.Record.namespace.notes}", required=true) @PathVariable(value="namespace")String namespace,
			@ApiParam(value="${RestClient.Record.set.notes}", required=true) @PathVariable(value="set") String set,
			@ApiParam(value="${RestClient.Record.userkey.notes}", required=true) @PathVariable(value="key")String key,
			@ApiParam(value="${RestClient.Record.bins.notes}", required=true) @RequestBody Map<String, Object>bins,
			@ApiIgnore @RequestParam Map<String, String>requestParams) {

		RecordKeyType keyType = RequestParamHandler.getKeyTypeFromMap(requestParams);
		WritePolicy policy = RequestParamHandler.getWritePolicy(requestParams, RecordExistsAction.REPLACE_ONLY);

		service.storeRecord(namespace, set, key, bins, keyType, policy);
	}


	@RequestMapping(method=RequestMethod.PUT, value="/{namespace}/{set}/{key}",
			consumes="application/msgpack", produces= {"application/json", "application/msgpack"})
	@ApiIgnore
	@ResponseStatus(value=HttpStatus.NO_CONTENT)
	public void
	replaceRecordNamespaceSetKeyMP(
			@PathVariable(value="namespace")String namespace,
			@PathVariable(value="set") String set,
			@PathVariable(value="key")String key,
			InputStream dataStream,
			@RequestParam Map<String, String>requestParams) {

		RecordKeyType keyType = RequestParamHandler.getKeyTypeFromMap(requestParams);
		WritePolicy policy = RequestParamHandler.getWritePolicy(requestParams, RecordExistsAction.REPLACE_ONLY);

		Map<String, Object>bins = binsFromMsgPackStream(dataStream);
		service.storeRecord(namespace, set, key, bins, keyType, policy);
	}

	@RequestMapping(method=RequestMethod.PUT, value="/{namespace}/{key}",
			consumes="application/json", produces= {"application/json", "application/msgpack"})
	@ApiOperation(value="${RestClient.KVS.ReplaceRecord.notes}", consumes="application/json, application/msgpack", nickname="replaceRecordNamespaceKey")
	@ResponseStatus(value=HttpStatus.NO_CONTENT)
	@ApiResponses(value= {
			@ApiResponse(code=404, response=RestClientError.class, message = "Record does not exist.",
					examples= @Example(value = {@ExampleProperty(mediaType="Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
			@ApiResponse(code=403, response=RestClientError.class, message = "Not authorized to access the resource",
			examples= @Example(value = {@ExampleProperty(mediaType="Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
			@ApiResponse(code=400, response=RestClientError.class, message = "Invalid parameters or request",
			examples= @Example(value = {@ExampleProperty(mediaType="Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
			@ApiResponse(code=409, response=RestClientError.class, message = "Generation mismatch for operation",
			examples= @Example(value = {@ExampleProperty(mediaType="Example json", value = "{'inDoubt': false, 'message': 'A message' ")}))
	})
	@ASRestClientWritePolicyQueryParams
	public void
	replaceRecordNamespaceKey(
			@ApiParam(value="${RestClient.Record.namespace.notes}", required=true) @PathVariable(value="namespace")String namespace,
			@ApiParam(value="${RestClient.Record.userkey.notes}", required=true) @PathVariable(value="key")String key,
			@ApiParam(value="${RestClient.Record.bins.notes}", required=true) @RequestBody Map<String, Object>bins,
			@ApiIgnore @RequestParam Map<String, String>requestParams) {

		RecordKeyType keyType = RequestParamHandler.getKeyTypeFromMap(requestParams);
		WritePolicy policy = RequestParamHandler.getWritePolicy(requestParams, RecordExistsAction.REPLACE_ONLY);

		service.storeRecord(namespace, null, key, bins, keyType, policy);
	}

	@RequestMapping(method=RequestMethod.PUT, value="/{namespace}/{key}",
			consumes="application/msgpack", produces= {"application/json", "application/msgpack"})
	@ApiIgnore
	@ResponseStatus(value=HttpStatus.NO_CONTENT)
	public void
	replaceRecordNamespaceKeyMP(
			@PathVariable(value="namespace")String namespace,
			@PathVariable(value="key")String key,
			InputStream dataStream,
			@RequestParam Map<String, String>requestParams) {

		RecordKeyType keyType = RequestParamHandler.getKeyTypeFromMap(requestParams);
		WritePolicy policy = RequestParamHandler.getWritePolicy(requestParams, RecordExistsAction.REPLACE_ONLY);

		Map<String, Object>bins = binsFromMsgPackStream(dataStream);
		service.storeRecord(namespace, null, key, bins, keyType, policy);
	}

	/*
	 **************************************************
	 *                     POST                       *
	 **************************************************
	 */
	@RequestMapping(method=RequestMethod.POST, value="/{namespace}/{set}/{key}",
			consumes={"application/json"}, produces= {"application/json", "application/msgpack"})
	/* A note on this consumes: We need to manually handle the input stream to get meaningful content out of a msgpack request,
	 * But otherwise the endpoint is the same*/
	@ApiOperation(value="${RestClient.KVS.CreateRecord.notes}", consumes="application/json, application/msgpack", nickname="createRecordNamespaceSetKey")
	@ResponseStatus(value=HttpStatus.CREATED)
	@ApiResponses(value= {
			@ApiResponse(code=404, response=RestClientError.class, message = "Namespace does not exist",
					examples= @Example(value = {@ExampleProperty(mediaType="Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
			@ApiResponse(code=403, response=RestClientError.class, message = "Not authorized to access the resource",
			examples= @Example(value = {@ExampleProperty(mediaType="Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
			@ApiResponse(code=400, response=RestClientError.class, message = "Invalid parameters or request",
			examples= @Example(value = {@ExampleProperty(mediaType="Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
			@ApiResponse(code=409, response=RestClientError.class, message = "Record Already exists",
			examples= @Example(value = {@ExampleProperty(mediaType="Example json", value = "{'inDoubt': false, 'message': 'A message' ")}))

	})
	@ASRestClientWritePolicyQueryParams
	public void
	createRecordNamespaceSetKey(
			@ApiParam(value="${RestClient.Record.namespace.notes}", required=true) @PathVariable(value="namespace")String namespace,
			@ApiParam(value="${RestClient.Record.set.notes}", required=true) @PathVariable(value="set") String set,
			@ApiParam(value="${RestClient.Record.userkey.notes}", required=true) @PathVariable(value="key")String key,
			@ApiParam(value="${RestClient.Record.bins.notes}", required=true) @RequestBody Map<String, Object>bins,
			@ApiIgnore @RequestParam Map<String, String>requestParams) {

		RecordKeyType keyType = RequestParamHandler.getKeyTypeFromMap(requestParams);
		WritePolicy policy = RequestParamHandler.getWritePolicy(requestParams, RecordExistsAction.CREATE_ONLY);

		service.storeRecord(namespace, set, key, bins, keyType, policy);
	}

	@ApiIgnore
	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(method=RequestMethod.POST, value="/{namespace}/{set}/{key}",
	consumes={"application/msgpack"}, produces={"application/json", "application/msgpack"})
	public void
	createRecordNamespaceSetKeyMP(
			@PathVariable(value="namespace")String namespace,
			@PathVariable(value="set") String set,
			@PathVariable(value="key")String key,
			InputStream dataStream,
			@RequestParam Map<String, String>requestParams) {

		RecordKeyType keyType = RequestParamHandler.getKeyTypeFromMap(requestParams);
		WritePolicy policy = RequestParamHandler.getWritePolicy(requestParams, RecordExistsAction.CREATE_ONLY);

		Map<String, Object>bins = binsFromMsgPackStream(dataStream);

		service.storeRecord(namespace, set, key, bins, keyType, policy);
	}

	@RequestMapping(method=RequestMethod.POST, value="/{namespace}/{key}",
			consumes={"application/json"}, produces= {"application/json", "application/msgpack"})
	@ApiOperation(value="${RestClient.KVS.CreateRecord.notes}", consumes="application/json, application/msgpack", nickname="createRecordNamespaceKey")
	@ResponseStatus(value=HttpStatus.CREATED)
	@ApiResponses(value= {
			@ApiResponse(code=404, response=RestClientError.class, message = "Namespace does not exist",
					examples= @Example(value = {@ExampleProperty(mediaType="Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
			@ApiResponse(code=403, response=RestClientError.class, message = "Not authorized to access the resource",
			examples= @Example(value = {@ExampleProperty(mediaType="Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
			@ApiResponse(code=400, response=RestClientError.class, message = "Invalid parameters or request",
			examples= @Example(value = {@ExampleProperty(mediaType="Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
			@ApiResponse(code=409, response=RestClientError.class, message = "Record Already exists",
			examples= @Example(value = {@ExampleProperty(mediaType="Example json", value = "{'inDoubt': false, 'message': 'A message' ")}))

	})
	@ASRestClientWritePolicyQueryParams
	public void
	createRecordNamespaceKey(
			@ApiParam(value="${RestClient.Record.namespace.notes}", required=true) @PathVariable(value="namespace")String namespace,
			@ApiParam(value="${RestClient.Record.userkey.notes}", required=true) @PathVariable(value="key")String key,
			@ApiParam(value="${RestClient.Record.bins.notes}", required=true) @RequestBody Map<String, Object>bins,
			@ApiIgnore @RequestParam Map<String, String>requestParams) {

		RecordKeyType keyType = RequestParamHandler.getKeyTypeFromMap(requestParams);
		WritePolicy policy = RequestParamHandler.getWritePolicy(requestParams, RecordExistsAction.CREATE_ONLY);

		service.storeRecord(namespace, null, key, bins, keyType, policy);
	}

	@ApiIgnore
	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(method=RequestMethod.POST, value="/{namespace}/{key}",
	consumes={"application/msgpack"}, produces= {"application/json", "application/msgpack"})
	public void
	createRecordNamespaceKeyMP(
			@PathVariable(value="namespace")String namespace,
			@PathVariable(value="key")String key,
			InputStream dataStream,
			@RequestParam Map<String, String>requestParams) {

		RecordKeyType keyType = RequestParamHandler.getKeyTypeFromMap(requestParams);
		WritePolicy policy = RequestParamHandler.getWritePolicy(requestParams, RecordExistsAction.CREATE_ONLY);

		Map<String, Object>bins = binsFromMsgPackStream(dataStream);

		service.storeRecord(namespace, null, key, bins, keyType, policy);
	}
	/*
	 **************************************************
	 *                     PATCH                      *
	 **************************************************
	 */

	@RequestMapping(method=RequestMethod.PATCH, value="/{namespace}/{set}/{key}",
			consumes={"application/json"}, produces= {"application/json", "application/msgpack"})
	@ResponseStatus(value=HttpStatus.NO_CONTENT)
	@ApiOperation(value="${RestClient.KVS.UpdateRecord.notes}", consumes="application/json, application/msgpack", nickname="updateRecordNamespaceSetKey")
	@ApiResponses(value= {
			@ApiResponse(code=404, response=RestClientError.class, message = "Record does not exist.",
					examples= @Example(value = {@ExampleProperty(mediaType="Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
			@ApiResponse(code=403, response=RestClientError.class, message = "Not authorized to access the resource",
			examples= @Example(value = {@ExampleProperty(mediaType="Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
			@ApiResponse(code=400, response=RestClientError.class, message = "Invalid parameters or request",
			examples= @Example(value = {@ExampleProperty(mediaType="Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
			@ApiResponse(code=409, response=RestClientError.class, message = "Generation mismatch for operation",
			examples= @Example(value = {@ExampleProperty(mediaType="Example json", value = "{'inDoubt': false, 'message': 'A message' ")}))
	})
	@ASRestClientWritePolicyQueryParams
	public void
	updateRecordNamespaceSetKey(
			@ApiParam(value="${RestClient.Record.namespace.notes}", required=true) @PathVariable(value="namespace")String namespace,
			@ApiParam(value="${RestClient.Record.set.notes}", required=true) @PathVariable(value="set") String set,
			@ApiParam(value="${RestClient.Record.userkey.notes}", required=true) @PathVariable(value="key")String key,
			@ApiParam(value="${RestClient.Record.bins.notes}", required=true) @RequestBody Map<String, Object>bins,
			@ApiIgnore @RequestParam Map<String, String>requestParams) {

		RecordKeyType keyType = RequestParamHandler.getKeyTypeFromMap(requestParams);
		WritePolicy policy = RequestParamHandler.getWritePolicy(requestParams, RecordExistsAction.UPDATE_ONLY);
		service.storeRecord(namespace, set, key, bins, keyType, policy);
	}


	@RequestMapping(method=RequestMethod.PATCH, value="/{namespace}/{set}/{key}",
			consumes="application/msgpack", produces= {"application/json", "application/msgpack"})
	@ResponseStatus(value=HttpStatus.NO_CONTENT)
	@ApiIgnore
	public void
	updateRecordNamespaceSetKeyMP(
			@PathVariable(value="namespace")String namespace,
			@PathVariable(value="set") String set,
			@PathVariable(value="key")String key,
			InputStream dataStream,
			@RequestParam Map<String, String>requestParams) {

		RecordKeyType keyType = RequestParamHandler.getKeyTypeFromMap(requestParams);
		WritePolicy policy = RequestParamHandler.getWritePolicy(requestParams, RecordExistsAction.UPDATE_ONLY);

		Map<String, Object>bins = binsFromMsgPackStream(dataStream);

		service.storeRecord(namespace, set, key, bins, keyType, policy);
	}

	@RequestMapping(method=RequestMethod.PATCH, value="/{namespace}/{key}",
			consumes="application/json", produces= {"application/json", "application/msgpack"})
	@ResponseStatus(value=HttpStatus.NO_CONTENT)
	@ApiOperation(value="${RestClient.KVS.UpdateRecord.notes}", consumes="application/json, application/msgpack", nickname="updateRecordNamespaceKey")
	@ApiResponses(value= {
			@ApiResponse(code=404, response=RestClientError.class, message = "Record does not exist.",
					examples= @Example(value = {@ExampleProperty(mediaType="Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
			@ApiResponse(code=403, response=RestClientError.class, message = "Not authorized to access the resource",
			examples= @Example(value = {@ExampleProperty(mediaType="Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
			@ApiResponse(code=400, response=RestClientError.class, message = "Invalid parameters or request",
			examples= @Example(value = {@ExampleProperty(mediaType="Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
			@ApiResponse(code=409, response=RestClientError.class, message = "Generation mismatch for operation",
			examples= @Example(value = {@ExampleProperty(mediaType="Example json", value = "{'inDoubt': false, 'message': 'A message' ")}))
	})
	@ASRestClientWritePolicyQueryParams
	public void
	updateRecordNamespaceKey(
			@ApiParam(value="${RestClient.Record.namespace.notes}", required=true) @PathVariable(value="namespace")String namespace,
			@ApiParam(value="${RestClient.Record.userkey.notes}", required=true) @PathVariable(value="key")String key,
			@ApiParam(value="${RestClient.Record.bins.notes}", required=true) @RequestBody Map<String, Object>bins,
			@ApiIgnore @RequestParam Map<String, String>requestParams) {

		RecordKeyType keyType = RequestParamHandler.getKeyTypeFromMap(requestParams);
		WritePolicy policy = RequestParamHandler.getWritePolicy(requestParams, RecordExistsAction.UPDATE_ONLY);
		service.storeRecord(namespace, null, key, bins, keyType, policy);
	}

	@RequestMapping(method=RequestMethod.PATCH, value="/{namespace}/{key}",
			consumes="application/msgpack", produces= {"application/json", "application/msgpack"})
	@ResponseStatus(value=HttpStatus.NO_CONTENT)
	@ApiIgnore
	public void
	updateRecordNamespaceKeyMP(
			@PathVariable(value="namespace")String namespace,
			@PathVariable(value="key")String key,
			InputStream dataStream,
			@RequestParam Map<String, String>requestParams) {

		RecordKeyType keyType = RequestParamHandler.getKeyTypeFromMap(requestParams);
		WritePolicy policy = RequestParamHandler.getWritePolicy(requestParams, RecordExistsAction.UPDATE_ONLY);

		Map<String, Object>bins = binsFromMsgPackStream(dataStream);

		service.storeRecord(namespace, null, key, bins, keyType, policy);
	}
	/*
	 **************************************************
	 *                     HEAD                       *
	 **************************************************
	 */
	@RequestMapping(method=RequestMethod.HEAD, value="/{namespace}/{set}/{key}", produces= {"application/json", "application/msgpack"})
	@ResponseStatus(value=HttpStatus.OK)
	@ApiResponses(value= {@ApiResponse(code=404, message = "Record does not exist.")})
	@ApiOperation(nickname="recordExistsNamespaceSetKey", value="Check if a record exists")
	public void
	recordExistsNamespaceSetKey(
			@ApiParam(value="${RestClient.Record.namespace.notes}", required=true) @PathVariable(value="namespace")String namespace,
			@ApiParam(value="${RestClient.Record.set.notes}", required=true) @PathVariable(value="set") String set,
			@ApiParam(value="${RestClient.Record.userkey.notes}", required=true) @PathVariable(value="key")String key,
			@ApiIgnore HttpServletResponse res,
			@ApiParam(name="keytype", value="${RestClient.Policy.keytype.notes}", required=false, defaultValue="STRING") @RequestParam(value="keytype", required=false)RecordKeyType keyType)
	{

		if (!service.recordExists(namespace, set, key, keyType)) {
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
	@RequestMapping(method=RequestMethod.HEAD, value="/{namespace}/{key}", produces= {"application/json", "application/msgpack"})
	@ResponseStatus(value=HttpStatus.OK)
	@ApiResponses(value= {@ApiResponse(code=404, message = "Record does not exist.")})
	@ApiOperation(value="Check if a record exists", nickname="recordExistsNamespaceKey")
	public void
	recordExistsNamespaceKey(
			@ApiParam(value="${RestClient.Record.namespace.notes}", required=true) @PathVariable(value="namespace")String namespace,
			@ApiParam(value="${RestClient.Record.userkey.notes}", required=true) @PathVariable(value="key")String key,
			@ApiIgnore HttpServletResponse res,
			@ApiParam(name="keytype", value="${RestClient.Policy.keytype.notes}", required=false, defaultValue="STRING") @RequestParam(value="keytype", required=false)RecordKeyType keyType)
	{

		if (!service.recordExists(namespace, null, key, keyType)) {
			try {
				res.sendError(404);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private Map<String, Object>binsFromMsgPackStream(InputStream dataStream) {
		MsgPackBinParser parser = new MsgPackBinParser(dataStream);
		return parser.parseBins();
	}
}