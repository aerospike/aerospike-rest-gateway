package com.aerospike.restclient.domain.operationmodels;

import org.junit.Assert;
import org.junit.Test;

public class MapRemoveByKeyRangeOperationTest {
    @Test
    public void isInverted() {
        MapRemoveByKeyRangeOperation op = new MapRemoveByKeyRangeOperation("bin", MapReturnType.RANK);
        Assert.assertFalse(op.isInverted());
        op.setInverted(true);
        Assert.assertTrue(op.isInverted());
    }

    @Test
    public void getKeyBegin() {
        MapRemoveByKeyRangeOperation op = new MapRemoveByKeyRangeOperation("bin", MapReturnType.RANK);
        Assert.assertNull(op.getKeyBegin());
        op.setKeyBegin(true);
        Assert.assertTrue((Boolean) op.getKeyBegin());
    }

    @Test
    public void getKeyEnd() {
        MapRemoveByKeyRangeOperation op = new MapRemoveByKeyRangeOperation("bin", MapReturnType.RANK);
        Assert.assertNull(op.getKeyEnd());
        op.setKeyEnd(true);
        Assert.assertTrue((Boolean) op.getKeyEnd());
    }
}
