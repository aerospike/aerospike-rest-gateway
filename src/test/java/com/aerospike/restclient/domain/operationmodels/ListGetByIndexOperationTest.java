package com.aerospike.restclient.domain.operationmodels;

import org.junit.Assert;
import org.junit.Test;

public class ListGetByIndexOperationTest {

    @Test
    public void isInverted() {
        ListGetByIndexOperation op = new ListGetByIndexOperation("bin", 1, ListReturnType.RANK);
        Assert.assertFalse(op.isInverted());
        op.setInverted(true);
        Assert.assertTrue(op.isInverted());
    }
}
