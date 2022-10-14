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
