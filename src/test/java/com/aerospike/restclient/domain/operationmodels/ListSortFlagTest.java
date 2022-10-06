package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.cdt.ListSortFlags;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class ListSortFlagTest {

    @Parameterized.Parameters
    public static Object[][] getParams() {
        return new Object[][]{
                new Object[]{ListSortFlag.DEFAULT, ListSortFlags.DEFAULT},
                new Object[]{ListSortFlag.DROP_DUPLICATES, ListSortFlags.DROP_DUPLICATES},
                };
    }

    ListSortFlag enum_;
    int flag;

    public ListSortFlagTest(ListSortFlag enum_, int flag) {
        this.enum_ = enum_;
        this.flag = flag;
    }

    @Test
    public void toListSortFlagTest() {
        Assert.assertEquals(enum_.flag, flag);
    }
}