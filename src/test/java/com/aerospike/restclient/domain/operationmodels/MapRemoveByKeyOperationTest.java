package com.aerospike.restclient.domain.operationmodels;

import org.junit.Assert;
import org.junit.Test;

public class MapRemoveByKeyOperationTest {
    @Test
    public void isInverted() {
        MapRemoveByKeyOperation op = new MapRemoveByKeyOperation("bin", "key", MapReturnType.RANK);
        Assert.assertFalse(op.isInverted());
        op.setInverted(true);
        Assert.assertTrue(op.isInverted());
    }
}
