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
public class MapReturnTypeTest {

    @Parameterized.Parameters
    public static Object[][] getParams() {
        return new Object[][]{
                new Object[]{MapReturnType.COUNT, false, com.aerospike.client.cdt.MapReturnType.COUNT},
                new Object[]{
                        MapReturnType.COUNT,
                        true,
                        com.aerospike.client.cdt.MapReturnType.COUNT | com.aerospike.client.cdt.MapReturnType.INVERTED
                },
                new Object[]{MapReturnType.INDEX, false, com.aerospike.client.cdt.MapReturnType.INDEX},
                new Object[]{
                        MapReturnType.INDEX,
                        true,
                        com.aerospike.client.cdt.MapReturnType.INDEX | com.aerospike.client.cdt.MapReturnType.INVERTED
                },
                new Object[]{MapReturnType.EXISTS, false, com.aerospike.client.cdt.MapReturnType.EXISTS},
                new Object[]{
                        MapReturnType.EXISTS,
                        true,
                        com.aerospike.client.cdt.MapReturnType.EXISTS | com.aerospike.client.cdt.MapReturnType.INVERTED
                },
                new Object[]{MapReturnType.NONE, false, com.aerospike.client.cdt.MapReturnType.NONE},
                new Object[]{
                        MapReturnType.NONE,
                        true,
                        com.aerospike.client.cdt.MapReturnType.NONE | com.aerospike.client.cdt.MapReturnType.INVERTED
                },
                new Object[]{MapReturnType.RANK, false, com.aerospike.client.cdt.MapReturnType.RANK},
                new Object[]{
                        MapReturnType.RANK,
                        true,
                        com.aerospike.client.cdt.MapReturnType.RANK | com.aerospike.client.cdt.MapReturnType.INVERTED
                },
                new Object[]{
                        MapReturnType.REVERSE_INDEX, false, com.aerospike.client.cdt.MapReturnType.REVERSE_INDEX
                },
                new Object[]{
                        MapReturnType.REVERSE_INDEX,
                        true,
                        com.aerospike.client.cdt.MapReturnType.REVERSE_INDEX | com.aerospike.client.cdt.MapReturnType.INVERTED
                },
                new Object[]{MapReturnType.REVERSE_RANK, false, com.aerospike.client.cdt.MapReturnType.REVERSE_RANK},
                new Object[]{
                        MapReturnType.REVERSE_RANK,
                        true,
                        com.aerospike.client.cdt.MapReturnType.REVERSE_RANK | com.aerospike.client.cdt.MapReturnType.INVERTED
                },
                new Object[]{MapReturnType.VALUE, false, com.aerospike.client.cdt.MapReturnType.VALUE},
                new Object[]{
                        MapReturnType.VALUE,
                        true,
                        com.aerospike.client.cdt.MapReturnType.VALUE | com.aerospike.client.cdt.MapReturnType.INVERTED
                },
                new Object[]{
                        MapReturnType.KEY,
                        true,
                        com.aerospike.client.cdt.MapReturnType.KEY | com.aerospike.client.cdt.MapReturnType.INVERTED
                },
                new Object[]{
                        MapReturnType.VALUE,
                        true,
                        com.aerospike.client.cdt.MapReturnType.VALUE | com.aerospike.client.cdt.MapReturnType.INVERTED
                },
                new Object[]{
                        MapReturnType.EXISTS,
                        true,
                        com.aerospike.client.cdt.MapReturnType.EXISTS | com.aerospike.client.cdt.MapReturnType.INVERTED
                },
                };
    }

    MapReturnType enum_;
    boolean inverted;
    int flag;

    public MapReturnTypeTest(MapReturnType enum_, boolean inverted, int flag) {
        this.enum_ = enum_;
        this.inverted = inverted;
        this.flag = flag;
    }

    @Test
    public void toMapReturnType() {
        Assert.assertEquals(enum_.toMapReturnType(inverted), flag);
    }
}
