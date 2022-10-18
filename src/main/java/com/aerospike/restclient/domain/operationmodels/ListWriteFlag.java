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

import com.aerospike.client.cdt.ListWriteFlags;

/*
 * Wraps constants in Aerospike client class ListWriteFlags
 */
public enum ListWriteFlag {
    /**
     * Default. Allow duplicate values and insertions at any index.
     */
    DEFAULT(ListWriteFlags.DEFAULT),

    /**
     * Only add unique values
     */
    ADD_UNIQUE(ListWriteFlags.ADD_UNIQUE),

    /**
     * Enforce list boundaries when inserting. Do not allow values to be inserted at index outside current list boundaries.
     */
    INSERT_BOUNDED(ListWriteFlags.INSERT_BOUNDED),

    /**
     * Do not raise error if a list item fails due to write flag constraints.
     */
    NO_FAIL(ListWriteFlags.NO_FAIL),

    /**
     * Allow other valid list items to be committed if a list item fails due to write flag constraints.
     */
    PARTIAL(ListWriteFlags.PARTIAL);

    public final int flag;

    ListWriteFlag(int flag) {
        this.flag = flag;
    }
}
