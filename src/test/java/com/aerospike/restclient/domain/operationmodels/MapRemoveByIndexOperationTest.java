package com.aerospike.restclient.domain.operationmodels;

import org.junit.Assert;
import org.junit.Test;

public class MapRemoveByIndexOperationTest {
    @Test
    public void isInverted() {
        MapRemoveByIndexOperation op = new MapRemoveByIndexOperation("bin", 1, MapReturnType.RANK);
        Assert.assertFalse(op.isInverted());
        op.setInverted(true);
        Assert.assertTrue(op.isInverted());
    }
}
