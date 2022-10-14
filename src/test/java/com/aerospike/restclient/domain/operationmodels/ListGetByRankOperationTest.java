package com.aerospike.restclient.domain.operationmodels;

import org.junit.Assert;
import org.junit.Test;

public class ListGetByRankOperationTest {

    @Test
    public void isInverted() {
        ListGetByRankOperation op = new ListGetByRankOperation("bin", 1, ListReturnType.RANK);
        Assert.assertFalse(op.isInverted());
        op.setInverted(true);
        Assert.assertTrue(op.isInverted());
    }
}
