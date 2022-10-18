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

import java.util.ArrayList;

public class ListRemoveByValueListOperationTest {
    @Test
    public void isInverted() {
        ListGetByValueListOperation op = new ListGetByValueListOperation("bin", ListReturnType.RANK, new ArrayList<>());
        Assert.assertFalse(op.isInverted());
        op.setInverted(true);
        Assert.assertTrue(op.isInverted());
    }
}
