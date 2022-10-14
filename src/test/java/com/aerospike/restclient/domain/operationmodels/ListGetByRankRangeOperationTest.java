package com.aerospike.restclient.domain.operationmodels;

import org.junit.Assert;
import org.junit.Test;

public class ListGetByRankRangeOperationTest {

    @Test
    public void getCount() {
        ListGetByRankRangeOperation op = new ListGetByRankRangeOperation("bin", 1, ListReturnType.RANK);
        Assert.assertNull(op.getCount());
        op.setCount(6);
        Assert.assertEquals(Integer.valueOf(6), op.getCount());
    }

    @Test
    public void isInverted() {
        ListGetByRankRangeOperation op = new ListGetByRankRangeOperation("bin", 1, ListReturnType.RANK);
        Assert.assertFalse(op.isInverted());
        op.setInverted(true);
        Assert.assertTrue(op.isInverted());
    }
}
