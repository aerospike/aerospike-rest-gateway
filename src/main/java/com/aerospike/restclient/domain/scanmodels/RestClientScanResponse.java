/*
 * Copyright 2020 Aerospike, Inc.
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
package com.aerospike.restclient.domain.scanmodels;

import com.aerospike.restclient.domain.RestClientRecord;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

@ApiModel(value = "RestClientScanResponse")
public class RestClientScanResponse {

    @ApiModelProperty(value = "List of records for current page.")
    private List<RestClientRecord> records;

    @ApiModelProperty(value = "Pagination details.")
    private Pagination pagination;

    public RestClientScanResponse(int initialCapacity) {
        records = new ArrayList<>(initialCapacity);
        pagination = new Pagination();
    }

    public RestClientScanResponse() {
        this(0);
    }

    public List<RestClientRecord> getRecords() {
        return records;
    }

    public void addRecord(RestClientRecord record) {
        records.add(record);
    }

    public int size() {
        return records.size();
    }

    public Pagination getPagination() {
        return pagination;
    }

}
