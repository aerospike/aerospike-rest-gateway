package com.aerospike.restclient.domain.operationmodels;

import org.junit.Assert;
import org.junit.Test;

public class ListRemoveByIndexRangeOperationTest {

    @Test
    public void getCount() {
        ListGetByIndexRangeOperation op = new ListGetByIndexRangeOperation("bin", 1, ListReturnType.RANK);
        Assert.assertNull(op.getCount());
        op.setCount(6);
        Assert.assertEquals(Integer.valueOf(6), op.getCount());
    }

    @Test
    public void isInverted() {
        ListGetByIndexRangeOperation op = new ListGetByIndexRangeOperation("bin", 1, ListReturnType.RANK);
        Assert.assertFalse(op.isInverted());
        op.setInverted(true);
        Assert.assertTrue(op.isInverted());
    }
}
