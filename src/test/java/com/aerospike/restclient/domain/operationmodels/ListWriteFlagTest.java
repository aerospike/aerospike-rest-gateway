package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.cdt.ListWriteFlags;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class ListWriteFlagTest {

    @Parameterized.Parameters
    public static Object[][] getParams() {
        return new Object[][]{
                new Object[]{ListWriteFlag.DEFAULT, ListWriteFlags.DEFAULT},
                new Object[]{ListWriteFlag.ADD_UNIQUE, ListWriteFlags.ADD_UNIQUE},
                new Object[]{ListWriteFlag.INSERT_BOUNDED, ListWriteFlags.INSERT_BOUNDED},
                new Object[]{ListWriteFlag.NO_FAIL, ListWriteFlags.NO_FAIL},
                new Object[]{ListWriteFlag.PARTIAL, ListWriteFlags.PARTIAL},
                };
    }

    ListWriteFlag enum_;
    int flag;

    public ListWriteFlagTest(ListWriteFlag enum_, int flag) {
        this.enum_ = enum_;
        this.flag = flag;
    }

    @Test
    public void testFlag() {
        Assert.assertEquals(enum_.flag, flag);
    }
}

