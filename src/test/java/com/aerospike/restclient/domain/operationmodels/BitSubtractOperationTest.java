package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.operation.BitOverflowAction;
import org.junit.Assert;
import org.junit.Test;

public class BitSubtractOperationTest {

    @Test
    public void isSigned() {
        BitSubtractOperation op = new BitSubtractOperation("bit", 0, 0, 1);
        Assert.assertFalse(op.isSigned());
        op.setSigned(true);
        Assert.assertTrue(op.isSigned());
    }

    @Test
    public void getAction() {
        BitSubtractOperation op = new BitSubtractOperation("bit", 0, 0, 1);
        Assert.assertEquals(BitOverflowAction.FAIL, op.getAction());
        op.setAction(BitOverflowAction.SATURATE);
        Assert.assertEquals(BitOverflowAction.SATURATE, op.getAction());
    }
}