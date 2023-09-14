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
package com.aerospike.restclient.domain.querymodels;

import com.aerospike.restclient.domain.RestClientKeyRecord;
import com.aerospike.restclient.domain.scanmodels.Pagination;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Schema(description = "Body of query response.")
public class QueryResponseBody {
    @Schema(description = "Records returned from query.", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty(required = true)
    private final List<RestClientKeyRecord> records;

    @Schema(description = "Pagination details.")
    private final Pagination pagination;

    public QueryResponseBody(int initialCapacity) {
        records = Collections.synchronizedList(new ArrayList<>(initialCapacity));
        pagination = new Pagination();
    }

    public QueryResponseBody() {
        this(0);
    }

    public List<RestClientKeyRecord> getRecords() {
        return records;
    }

    public void addRecord(RestClientKeyRecord record) {
        records.add(record);
    }

    public int size() {
        return records.size();
    }

    public Pagination getPagination() {
        return pagination;
    }
}
