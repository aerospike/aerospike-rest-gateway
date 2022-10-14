package com.aerospike.restclient.domain.operationmodels;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class ListRemoveByValueListOperationTest {
    @Test
    public void isInverted() {
        ListGetByValueListOperation op = new ListGetByValueListOperation("bin", ListReturnType.RANK, new ArrayList<>());
        Assert.assertFalse(op.isInverted());
        op.setInverted(true);
        Assert.assertTrue(op.isInverted());
    }
}
