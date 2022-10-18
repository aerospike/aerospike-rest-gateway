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
package com.aerospike.restclient.domain;

import com.aerospike.client.query.IndexCollectionType;
import com.aerospike.client.query.IndexType;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

public class RestClientIndex {

    @JsonProperty("type")
    IndexType indexType;

    @JsonProperty("collection_type")
    IndexCollectionType collectionType;

    @Schema(example = "testNS")
    String namespace;

    @Schema(example = "testSet")
    String set;

    @Schema(description = "The bin which is indexed", example = "ageBin")
    String bin;

    @Schema(description = "The name of the index. This must be unique per set", example = "ageIndex")
    String name;

    public IndexType getIndexType() {
        return this.indexType;
    }

    public IndexCollectionType getCollectionType() {
        return this.collectionType == null ? IndexCollectionType.DEFAULT : this.collectionType;
    }

    public String getNamespace() {
        return this.namespace;
    }

    public String getSet() {
        return this.set;
    }

    public String getBin() {
        return this.bin;
    }

    public String getName() {
        return this.name;
    }

    public void setIndexType(IndexType iType) {
        this.indexType = iType;
    }

    public void setCollectionType(IndexCollectionType cType) {
        this.collectionType = cType;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public void setSet(String set) {
        this.set = set;
    }

    public void setBin(String bin) {
        this.bin = bin;
    }

    public void setName(String name) {
        this.name = name;
    }

    /* Convert an info call to a RestClientIndex */
    @Schema(hidden = true)
    private static final String NAME_KEY = "indexname";
    @Schema(hidden = true)
    private static final String NS_KEY = "ns";
    @Schema(hidden = true)
    private static final String SET_KEY = "set";
    @Schema(hidden = true)
    private static final String BIN_KEY = "bin";
    @Schema(hidden = true)
    private static final String INDEX_TYPE_KEY = "type";
    @Schema(hidden = true)
    private static final String COLLECTION_TYPE_KEY = "indextype";

    public RestClientIndex() {
    }

    public RestClientIndex(Map<String, String> idxInfo) {
        name = idxInfo.get(NAME_KEY);
        namespace = idxInfo.get(NS_KEY);
        set = normalizeSet(idxInfo.get(SET_KEY));
        bin = idxInfo.get(BIN_KEY);
        indexType = normalizeIndexType(idxInfo.get(INDEX_TYPE_KEY));
        collectionType = normalizeCollectionType(idxInfo.get(COLLECTION_TYPE_KEY));
    }

    private String normalizeSet(String set) {
        if (set == null || set.equals("NULL")) {
            return null;
        }
        return set;
    }

    private IndexType normalizeIndexType(String idxType) {
        idxType = idxType.toUpperCase();

        if (idxType.equals("GEOJSON")) {
            return IndexType.GEO2DSPHERE;
        }
        return IndexType.valueOf(idxType);
    }

    static IndexCollectionType normalizeCollectionType(String collType) {
        collType = collType.toUpperCase();

        if (collType.equals("NONE")) {
            return IndexCollectionType.DEFAULT;
        }
        return IndexCollectionType.valueOf(collType);
    }
}
