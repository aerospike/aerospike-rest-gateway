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

public enum ListReturnType {
    COUNT, INDEX, EXISTS, NONE, RANK, REVERSE_INDEX, REVERSE_RANK, VALUE;

    public int toListReturnType(boolean inverted) {
        int invertedFlag = inverted ? com.aerospike.client.cdt.ListReturnType.INVERTED : 0;

        return switch (this) {
            case COUNT -> com.aerospike.client.cdt.ListReturnType.COUNT | invertedFlag;
            case INDEX -> com.aerospike.client.cdt.ListReturnType.INDEX | invertedFlag;
            case NONE -> com.aerospike.client.cdt.ListReturnType.NONE | invertedFlag;
            case RANK -> com.aerospike.client.cdt.ListReturnType.RANK | invertedFlag;
            case REVERSE_INDEX -> com.aerospike.client.cdt.ListReturnType.REVERSE_INDEX | invertedFlag;
            case REVERSE_RANK -> com.aerospike.client.cdt.ListReturnType.REVERSE_RANK | invertedFlag;
            case VALUE -> com.aerospike.client.cdt.ListReturnType.VALUE | invertedFlag;
            case EXISTS -> com.aerospike.client.cdt.ListReturnType.EXISTS | invertedFlag;
        };
    }
}