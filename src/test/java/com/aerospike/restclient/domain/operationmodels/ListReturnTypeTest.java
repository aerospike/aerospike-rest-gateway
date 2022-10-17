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
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class ListReturnTypeTest {

    @Parameterized.Parameters
    public static Object[][] getParams() {
        return new Object[][]{
                new Object[]{ListReturnType.COUNT, false, com.aerospike.client.cdt.ListReturnType.COUNT},
                new Object[]{
                        ListReturnType.COUNT,
                        true,
                        com.aerospike.client.cdt.ListReturnType.COUNT | com.aerospike.client.cdt.ListReturnType.INVERTED
                },
                new Object[]{ListReturnType.INDEX, false, com.aerospike.client.cdt.ListReturnType.INDEX},
                new Object[]{
                        ListReturnType.INDEX,
                        true,
                        com.aerospike.client.cdt.ListReturnType.INDEX | com.aerospike.client.cdt.ListReturnType.INVERTED
                },
                new Object[]{ListReturnType.EXISTS, false, com.aerospike.client.cdt.ListReturnType.EXISTS},
                new Object[]{
                        ListReturnType.EXISTS,
                        true,
                        com.aerospike.client.cdt.ListReturnType.EXISTS | com.aerospike.client.cdt.ListReturnType.INVERTED
                },
                new Object[]{ListReturnType.NONE, false, com.aerospike.client.cdt.ListReturnType.NONE},
                new Object[]{
                        ListReturnType.NONE,
                        true,
                        com.aerospike.client.cdt.ListReturnType.NONE | com.aerospike.client.cdt.ListReturnType.INVERTED
                },
                new Object[]{ListReturnType.RANK, false, com.aerospike.client.cdt.ListReturnType.RANK},
                new Object[]{
                        ListReturnType.RANK,
                        true,
                        com.aerospike.client.cdt.ListReturnType.RANK | com.aerospike.client.cdt.ListReturnType.INVERTED
                },
                new Object[]{
                        ListReturnType.REVERSE_INDEX, false, com.aerospike.client.cdt.ListReturnType.REVERSE_INDEX
                },
                new Object[]{
                        ListReturnType.REVERSE_INDEX,
                        true,
                        com.aerospike.client.cdt.ListReturnType.REVERSE_INDEX | com.aerospike.client.cdt.ListReturnType.INVERTED
                },
                new Object[]{ListReturnType.REVERSE_RANK, false, com.aerospike.client.cdt.ListReturnType.REVERSE_RANK},
                new Object[]{
                        ListReturnType.REVERSE_RANK,
                        true,
                        com.aerospike.client.cdt.ListReturnType.REVERSE_RANK | com.aerospike.client.cdt.ListReturnType.INVERTED
                },
                new Object[]{ListReturnType.VALUE, false, com.aerospike.client.cdt.ListReturnType.VALUE},
                new Object[]{
                        ListReturnType.VALUE,
                        true,
                        com.aerospike.client.cdt.ListReturnType.VALUE | com.aerospike.client.cdt.ListReturnType.INVERTED
                },
                };
    }

    ListReturnType enum_;
    boolean inverted;
    int flag;

    public ListReturnTypeTest(ListReturnType enum_, boolean inverted, int flag) {
        this.enum_ = enum_;
        this.inverted = inverted;
        this.flag = flag;
    }

    @Test
    public void toListReturnType() {
        Assert.assertEquals(enum_.toListReturnType(inverted), flag);
    }
}

