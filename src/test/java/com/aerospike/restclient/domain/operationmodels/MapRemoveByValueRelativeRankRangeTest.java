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

import org.junit.Assert;
import org.junit.Test;

public class MapRemoveByValueRelativeRankRangeTest {

    @Test
    public void getCount() {
        MapRemoveByValueRelativeRankRange op = new MapRemoveByValueRelativeRankRange("bin", 1, 1, MapReturnType.RANK);
        Assert.assertNull(op.getCount());
        op.setCount(6);
        Assert.assertEquals(Integer.valueOf(6), op.getCount());
    }

    @Test
    public void isInverted() {
        MapRemoveByValueRelativeRankRange op = new MapRemoveByValueRelativeRankRange("bin", 1, 1, MapReturnType.RANK);
        Assert.assertFalse(op.isInverted());
        op.setInverted(true);
        Assert.assertTrue(op.isInverted());
    }
}

