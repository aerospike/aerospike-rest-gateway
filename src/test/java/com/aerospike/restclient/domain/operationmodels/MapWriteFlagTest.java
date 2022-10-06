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
