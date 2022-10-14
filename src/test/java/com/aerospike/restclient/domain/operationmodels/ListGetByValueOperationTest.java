package com.aerospike.restclient.domain.operationmodels;

import org.junit.Assert;
import org.junit.Test;

public class ListGetByValueOperationTest {
    @Test
    public void isInverted() {
        ListGetByValueOperation op = new ListGetByValueOperation("bin", 1, ListReturnType.RANK);
        Assert.assertFalse(op.isInverted());
        op.setInverted(true);
        Assert.assertTrue(op.isInverted());
    }
}
