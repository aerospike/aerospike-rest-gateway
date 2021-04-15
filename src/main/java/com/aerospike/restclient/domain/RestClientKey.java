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
package com.aerospike.restclient.domain;

import com.aerospike.client.Key;
import com.aerospike.client.Value.BytesValue;
import com.aerospike.client.Value.IntegerValue;
import com.aerospike.client.Value.LongValue;
import com.aerospike.client.Value.StringValue;
import com.aerospike.restclient.util.AerospikeAPIConstants.RecordKeyType;
import com.aerospike.restclient.util.KeyBuilder;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Base64;
import java.util.Base64.Encoder;

@ApiModel(value="Key")
public class RestClientKey {

	@ApiModelProperty(required=true, example="testNS")
	public String namespace;

	@JsonProperty(value="setName")
	@ApiModelProperty(example="testSet")
	public String setName;

	@JsonProperty(value="keytype")
	@ApiModelProperty(
			value="Enum describing the type of the userKey. This field is omitted in MessagePack responses.",
			example="STRING")
	public RecordKeyType keytype;

	@ApiModelProperty(required=true,
			value="The user key, it may be a string, integer, or URL safe Base64 encoded bytes.",
			example="userKey")
	public Object userKey;

	public RestClientKey() {}

	public RestClientKey(Key realKey) {
		Encoder encoder = Base64.getUrlEncoder();
		namespace = realKey.namespace;
		setName = realKey.setName;

		if (realKey.userKey != null) {
			if (realKey.userKey instanceof StringValue) {
				userKey = realKey.userKey.toString();
				keytype = RecordKeyType.STRING;
			} else if (realKey.userKey instanceof IntegerValue || realKey.userKey instanceof LongValue) {
				userKey = realKey.userKey.getObject();
				keytype = RecordKeyType.INTEGER;
			} else if(realKey.userKey instanceof BytesValue) {
				userKey = encoder.encodeToString((byte[])realKey.userKey.getObject());
				keytype = RecordKeyType.BYTES;
			}
		} else {
			userKey = encoder.encodeToString(realKey.digest);
			keytype = RecordKeyType.DIGEST;
		}
	}

	public Key toKey() {
		return KeyBuilder.buildKey(namespace, setName, userKey.toString(), keytype);
	}
}
