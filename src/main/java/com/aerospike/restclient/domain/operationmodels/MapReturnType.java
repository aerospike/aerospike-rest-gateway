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
package com.aerospike.restclient.domain.operationmodels;

public enum MapReturnType {
    COUNT(com.aerospike.client.cdt.MapReturnType.COUNT),
    INDEX(com.aerospike.client.cdt.MapReturnType.INDEX),
    KEY(com.aerospike.client.cdt.MapReturnType.KEY),
    KEY_VALUE(com.aerospike.client.cdt.MapReturnType.KEY_VALUE),
    NONE(com.aerospike.client.cdt.MapReturnType.NONE),
    RANK(com.aerospike.client.cdt.MapReturnType.RANK),
    REVERSE_INDEX(com.aerospike.client.cdt.MapReturnType.REVERSE_INDEX),
    REVERSE_RANK(com.aerospike.client.cdt.MapReturnType.REVERSE_RANK),
    VALUE(com.aerospike.client.cdt.MapReturnType.VALUE),
    EXISTS(com.aerospike.client.cdt.MapReturnType.EXISTS),
    UNORDERED_MAP(com.aerospike.client.cdt.MapReturnType.UNORDERED_MAP),
    ORDERED_MAP(com.aerospike.client.cdt.MapReturnType.ORDERED_MAP);

    private final int returnType;

    MapReturnType(int returnType) {
        this.returnType = returnType;
    }

    public int toMapReturnType(boolean inverted) {
        int invertedFlag = inverted ? com.aerospike.client.cdt.MapReturnType.INVERTED : 0;
        return this.returnType | invertedFlag;
    }
}
