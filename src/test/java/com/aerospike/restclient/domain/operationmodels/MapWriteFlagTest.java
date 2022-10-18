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
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class MapWriteFlagTest {

    @Parameterized.Parameters
    public static Object[][] getParams() {
        return new Object[][]{
                new Object[]{MapWriteFlag.DEFAULT, MapWriteFlags.DEFAULT},
                new Object[]{MapWriteFlag.CREATE_ONLY, MapWriteFlags.CREATE_ONLY},
                new Object[]{MapWriteFlag.UPDATE_ONLY, MapWriteFlags.UPDATE_ONLY},
                new Object[]{MapWriteFlag.NO_FAIL, MapWriteFlags.NO_FAIL},
                new Object[]{MapWriteFlag.PARTIAL, MapWriteFlags.PARTIAL},
                };
    }

    MapWriteFlag enum_;
    int flag;

    public MapWriteFlagTest(MapWriteFlag enum_, int flag) {
        this.enum_ = enum_;
        this.flag = flag;
    }

    @Test
    public void testFlag() {
        Assert.assertEquals(enum_.flag, flag);
    }
}
