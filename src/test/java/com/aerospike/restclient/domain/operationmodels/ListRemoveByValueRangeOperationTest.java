package com.aerospike.restclient.domain.operationmodels;

import org.junit.Assert;
import org.junit.Test;

public class ListRemoveByValueRangeOperationTest {
    @Test
    public void isInverted() {
        ListRemoveByValueRangeOperation op = new ListRemoveByValueRangeOperation("bin", ListReturnType.RANK);
        Assert.assertFalse(op.isInverted());
        op.setInverted(true);
        Assert.assertTrue(op.isInverted());
    }

    @Test
    public void getValueBegin() {
        ListRemoveByValueRangeOperation op = new ListRemoveByValueRangeOperation("bin", ListReturnType.RANK);
        Assert.assertNull(op.getValueBegin());
        op.setValueBegin(true);
        Assert.assertTrue((Boolean) op.getValueBegin());
    }

    @Test
    public void getValueEnd() {
        ListRemoveByValueRangeOperation op = new ListRemoveByValueRangeOperation("bin", ListReturnType.RANK);
        Assert.assertNull(op.getValueEnd());
        op.setValueEnd(true);
        Assert.assertTrue((Boolean) op.getValueEnd());
    }
}
