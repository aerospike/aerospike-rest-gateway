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
package com.aerospike.restclient.util.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.aerospike.restclient.util.AerospikeAPIConstants;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

/*
 * Annotations used by Swagger to report correct query parameters for the given API method.
 */
@ApiImplicitParams(value= {

		@ApiImplicitParam(
				name=AerospikeAPIConstants.SEND_KEY, dataType="boolean", paramType="query",
				value="${RestClient.Policy.sendKey.notes}", defaultValue="false"),

		@ApiImplicitParam(
				name=AerospikeAPIConstants.REPLICA, dataType="string",
				paramType="query", allowableValues="MASTER, MASTER_PROLES, SEQUENCE, RANDOM",
				value="${RestClient.Policy.replica.notes}", defaultValue="SEQUENCE"),
		@ApiImplicitParam(
				name=AerospikeAPIConstants.KEY_TYPE, dataType="string",
				paramType="query", allowableValues="STRING, INTEGER, BYTES, DIGEST", defaultValue="STRING",
				value="${RestClient.Policy.keytype.notes}"),

		/** Write Policy Values **/
		@ApiImplicitParam(
				name=AerospikeAPIConstants.EXPIRATION, dataType="int", paramType="query",
				value="${RestClient.WritePolicy.expiration.notes}"),

		@ApiImplicitParam(
				name=AerospikeAPIConstants.GENERATION, dataType="int", paramType="query",
				value="${RestClient.WritePolicy.generation.notes}"),

		@ApiImplicitParam(
				name=AerospikeAPIConstants.DURABLE_DELETE, dataType="boolean", paramType="query",
				defaultValue="false", value="${RestClient.WritePolicy.durableDelete.notes}"),

		@ApiImplicitParam(
				name=AerospikeAPIConstants.COMMIT_LEVEL, dataType="string",
				paramType="query", allowableValues="COMMIT_ALL, COMMIT_MASTER", defaultValue="COMMIT_ALL",
				value="${RestClient.WritePolicy.commitLevel.notes}"),

		@ApiImplicitParam(
				name=AerospikeAPIConstants.GENERATION_POLICY, dataType="string",
				paramType="query", allowableValues="NONE, EXPECT_GEN_EQUAL, EXPECT_GEN_GT",
				defaultValue="NONE", value="${RestClient.WritePolicy.generationPolicy.notes}"),
		@ApiImplicitParam(
				value="How to handle the existence of the record. This is ignored for POST/PUT/UPDATE kvs methods.",
				name=AerospikeAPIConstants.RECORD_EXISTS_ACTION, dataType="string",
				paramType="query", allowableValues="UPDATE, UPDATE_ONLY, REPLACE, REPLACE_ONLY, CREATE_ONLY"),
		@ApiImplicitParam(
				name=AerospikeAPIConstants.READ_MODE_SC, dataType="string",
				paramType="query", allowableValues="ALLOW_REPLICA, ALLOW_UNAVAILABLE, LINEARIZE, SESSION",
				value="${RestClient.Policy.readmodeAP.notes}", defaultValue="SESSION"),
		@ApiImplicitParam(
				name=AerospikeAPIConstants.READ_MODE_AP, dataType="string",
				paramType="query", allowableValues="ALL, ONE",
				value="${RestClient.Policy.readmodeAP.notes}", defaultValue="ONE"),
})
@Retention(RetentionPolicy.RUNTIME)
public @interface ASRestClientWritePolicyQueryParams {}
