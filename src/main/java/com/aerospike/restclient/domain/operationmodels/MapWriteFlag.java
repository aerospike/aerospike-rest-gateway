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

import com.aerospike.client.cdt.MapWriteFlags;

/*
 * Wraps constants in Aerospike client class MapWriteFlags
 */
public enum MapWriteFlag {
    /**
     * Default.  Allow create or update.
     */
    DEFAULT(MapWriteFlags.DEFAULT),

    /**
     * If the key already exists, the item will be denied.
     * If the key does not exist, a new item will be created.
     */
    CREATE_ONLY(MapWriteFlags.CREATE_ONLY),

    /**
     * If the key already exists, the item will be overwritten.
     * If the key does not exist, the item will be denied.
     */
    UPDATE_ONLY(MapWriteFlags.UPDATE_ONLY),

    /**
     * Do not raise error if a map item is denied due to write flag constraints.
     */
    NO_FAIL(MapWriteFlags.NO_FAIL),

    /**
     * Allow other valid map items to be committed if a map item is denied due to
     * write flag constraints.
     */
    PARTIAL(MapWriteFlags.PARTIAL);

    public final int flag;

    MapWriteFlag(int flag) {
        this.flag = flag;
    }
}
