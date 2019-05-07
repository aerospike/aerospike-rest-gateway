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

/* Annotation which tells swagger to render all of the Batch Policy options as query parameters */
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
				defaultValue=QueryParamDescriptors.POLICY_REPLICA_DEFAULT)
})
@Retention(RetentionPolicy.RUNTIME)
public @interface ASRestClientBatchPolicyQueryParams {}
