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
import com.aerospike.restclient.util.QueryParamDescriptors;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

/*
 * Annotations used by Swagger to report correct query parameters for the given API method.
 */
@ApiImplicitParams(value= {

		@ApiImplicitParam(
				name=AerospikeAPIConstants.SEND_KEY,
				dataType="boolean",
				paramType="query",
				value=QueryParamDescriptors.POLICY_SEND_KEY_NOTES,
				defaultValue=QueryParamDescriptors.POLICY_SEND_KEY_DEFAULT),
		@ApiImplicitParam(
				name=AerospikeAPIConstants.REPLICA,
				dataType="string",
				paramType="query",
				allowableValues=QueryParamDescriptors.POLICY_REPLICA_ALLOWABLE_VALUES,
				value=QueryParamDescriptors.POLICY_REPLICA_NOTES,
				defaultValue=QueryParamDescriptors.POLICY_REPLICA_DEFAULT),
		@ApiImplicitParam(
				name=AerospikeAPIConstants.KEY_TYPE, dataType="string",
				paramType="query",
				allowableValues=QueryParamDescriptors.KEYTYPE_ALLOWABLE_VALUES,
				defaultValue=QueryParamDescriptors.KEYTYPE_DEFAULT,
				value=QueryParamDescriptors.KEYTYPE_NOTES),

		/** Write Policy Values **/
		@ApiImplicitParam(
				name=AerospikeAPIConstants.EXPIRATION,
				dataType="int",
				paramType="query",
				value=QueryParamDescriptors.WRITE_POLICY_EXPIRATION_NOTES),

		@ApiImplicitParam(
				name=AerospikeAPIConstants.GENERATION,
				dataType="int",
				paramType="query",
				value=QueryParamDescriptors.WRITE_POLICY_GEN_NOTES),

		@ApiImplicitParam(
				name=AerospikeAPIConstants.DURABLE_DELETE,
				dataType="boolean",
				paramType="query",
				defaultValue=QueryParamDescriptors.WRITE_POLICY_DURABLE_DELETE_DEFAULT,
				value=QueryParamDescriptors.WRITE_POLICY_DURABLE_DELETE_NOTES),

		@ApiImplicitParam(
				name=AerospikeAPIConstants.COMMIT_LEVEL,
				dataType="string",
				paramType="query",
				allowableValues=QueryParamDescriptors.WRITE_POLICY_COMMIT_LEVEL_ALLOWABLE_VALUES,
				defaultValue=QueryParamDescriptors.WRITE_POLICY_COMMIT_LEVEL_DEFAULT,
				value=QueryParamDescriptors.WRITE_POLICY_COMMIT_LEVEL_NOTES),

		@ApiImplicitParam(
				name=AerospikeAPIConstants.GENERATION_POLICY,
				dataType="string",
				paramType="query",
				allowableValues=QueryParamDescriptors.WRITE_POLICY_GEN_POLICY_ALLOWABLE_VALUES,
				defaultValue=QueryParamDescriptors.WRITE_POLICY_GEN_POLICY_DEFAULT,
				value=QueryParamDescriptors.WRITE_POLICY_GEN_POLICY_NOTES),
		@ApiImplicitParam(
				value=QueryParamDescriptors.WRITE_POLICY_RECORD_EXISTS_NOTES,
				name=AerospikeAPIConstants.RECORD_EXISTS_ACTION,
				dataType="string",
				paramType="query",
				defaultValue=QueryParamDescriptors.WRITE_POLICY_RECORD_EXISTS_DEFAULT,
				allowableValues=QueryParamDescriptors.WRITE_POLICY_RECORD_EXISTS_ALLOWABLE_VALUES),
		@ApiImplicitParam(
				name=AerospikeAPIConstants.READ_MODE_SC,
				dataType="string",
				paramType="query",
				allowableValues=QueryParamDescriptors.POLICY_READMODESC_ALLOWABLE_VALUES,
				value=QueryParamDescriptors.POLICY_READMODESC_NOTES,
				defaultValue=QueryParamDescriptors.POLICY_READMODESC_DEFAULT),
		@ApiImplicitParam(
				name=AerospikeAPIConstants.READ_MODE_AP,
				dataType="string",
				paramType="query",
				allowableValues=QueryParamDescriptors.POLICY_READMODEAP_ALLOWABLE_VALUES,
				value=QueryParamDescriptors.POLICY_READMODEAP_NOTES,
				defaultValue=QueryParamDescriptors.POLICY_READMODEAP_DEFAULT),
})
@Retention(RetentionPolicy.RUNTIME)
public @interface ASRestClientWritePolicyQueryParams {}
