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

public class ListGetByValueRangeOperationTest {
    @Test
    public void isInverted() {
        ListGetByValueRangeOperation op = new ListGetByValueRangeOperation("bin", ListReturnType.RANK);
        Assert.assertFalse(op.isInverted());
        op.setInverted(true);
        Assert.assertTrue(op.isInverted());
    }

    @Test
    public void getValueBegin() {
        ListGetByValueRangeOperation op = new ListGetByValueRangeOperation("bin", ListReturnType.RANK);
        Assert.assertNull(op.getValueBegin());
        op.setValueBegin(true);
        Assert.assertTrue((Boolean) op.getValueBegin());
    }

    @Test
    public void getValueEnd() {
        ListGetByValueRangeOperation op = new ListGetByValueRangeOperation("bin", ListReturnType.RANK);
        Assert.assertNull(op.getValueEnd());
        op.setValueEnd(true);
        Assert.assertTrue((Boolean) op.getValueEnd());
    }
}
