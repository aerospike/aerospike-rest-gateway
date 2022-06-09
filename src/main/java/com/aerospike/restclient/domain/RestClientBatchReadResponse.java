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

import com.aerospike.client.BatchRead;
import io.swagger.v3.oas.annotations.media.Schema;

public class RestClientBatchReadResponse extends RestClientBatchRecordResponse {

	public RestClientBatchReadResponse() {}

	public RestClientBatchReadResponse(BatchRead batchRead) {
		super(batchRead);
		readAllBins = batchRead.readAllBins;
		binNames = batchRead.binNames;
	}

	@Schema(description = "Whether all bins should be returned with this record")
	public boolean readAllBins;

	@Schema(description = "List of bins to limit the record response to.", example = "[\"bin1\"]")
	public String[] binNames;
}
