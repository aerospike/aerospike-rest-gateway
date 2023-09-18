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

import com.aerospike.client.cdt.MapOrder;

import java.util.List;

public class MapPolicy {

    private MapOrder order;

    private List<MapWriteFlag> writeFlags;

    public MapPolicy() {
    }

    public MapOrder getOrder() {
        return order;
    }

    public void setOrder(MapOrder order) {
        this.order = order;
    }

    public List<MapWriteFlag> getWriteFlags() {
        return writeFlags;
    }

    public void setWriteFlags(List<MapWriteFlag> writeFlags) {
        this.writeFlags = writeFlags;
    }

    public com.aerospike.client.cdt.MapPolicy toMapPolicy() {
        if (order == null && (writeFlags == null || writeFlags.isEmpty())) {
            return com.aerospike.client.cdt.MapPolicy.Default;
        }

        if (order == null) {
            order = MapOrder.UNORDERED;
        }

        int flags = 0;

        if (writeFlags != null) {
            flags = writeFlags.stream().reduce(0, (acc, flag) -> acc | flag.flag, Integer::sum);
        }

        return new com.aerospike.client.cdt.MapPolicy(order, flags);
    }
}
