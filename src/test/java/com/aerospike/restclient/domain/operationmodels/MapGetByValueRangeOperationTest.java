package com.aerospike.restclient.domain.operationmodels;

import org.junit.Assert;
import org.junit.Test;

public class MapGetByValueRangeOperationTest {
    @Test
    public void isInverted() {
        MapGetByValueRangeOperation op = new MapGetByValueRangeOperation("bin", MapReturnType.RANK);
        Assert.assertFalse(op.isInverted());
        op.setInverted(true);
        Assert.assertTrue(op.isInverted());
    }

    @Test
    public void getValueBegin() {
        MapGetByValueRangeOperation op = new MapGetByValueRangeOperation("bin", MapReturnType.RANK);
        Assert.assertNull(op.getValueBegin());
        op.setValueBegin(true);
        Assert.assertTrue((Boolean) op.getValueBegin());
    }

    @Test
    public void getValueEnd() {
        MapGetByValueRangeOperation op = new MapGetByValueRangeOperation("bin", MapReturnType.RANK);
        Assert.assertNull(op.getValueEnd());
        op.setValueEnd(true);
        Assert.assertTrue((Boolean) op.getValueEnd());
    }
}
