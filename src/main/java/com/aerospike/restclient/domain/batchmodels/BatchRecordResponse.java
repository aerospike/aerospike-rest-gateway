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
package com.aerospike.restclient.domain.batchmodels;

import com.aerospike.client.BatchRecord;
import com.aerospike.client.ResultCode;
import com.aerospike.restclient.domain.RestClientKey;
import com.aerospike.restclient.domain.RestClientRecord;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "BatchRecordResponse",
        description = "Object returned in from a single batch operation",
        externalDocs = @ExternalDocumentation(url = "https://javadoc.io/doc/com.aerospike/aerospike-client/" + AerospikeAPIConstants.AS_CLIENT_VERSION + "/com/aerospike/client/BatchRecord.html")
)
public class BatchRecordResponse {

    @Schema(description = "Result code for this returned record.")
    public int resultCode;
    @Schema(description = "Message associated with resultCode.")
    public String resultCodeString;
    @Schema(description = "Record associated with the key. Null if the record was not found.")
    public RestClientRecord record;
    @Schema(description = "Key to retrieve a record.")
    public RestClientKey key;
    @Schema(description = "Is it possible that the write transaction may have completed even though an error occurred for this record.")
    public boolean inDoubt;

    public BatchRecordResponse() {
    }

    public BatchRecordResponse(BatchRecord batchRecord) {
        resultCode = batchRecord.resultCode;
        resultCodeString = ResultCode.getResultString(resultCode);
        record = batchRecord.record != null ? new RestClientRecord(batchRecord.record) : null;
        key = new RestClientKey(batchRecord.key);
        inDoubt = batchRecord.inDoubt;
    }
}
