package com.aerospike.restclient.domain.operationmodels;

import org.junit.Assert;
import org.junit.Test;

public class HLLAddOperationTest {

    @Test
    public void getIndexBitCount() {
        HLLAddOperation op = new HLLAddOperation("op", null);
        Assert.assertNull(op.getIndexBitCount());
        op.setIndexBitCount(2);
        Assert.assertEquals(Integer.valueOf(2), op.getIndexBitCount());
    }

    @Test
    public void getMinHashBitCount() {
        HLLAddOperation op = new HLLAddOperation("op", null);
        Assert.assertNull(op.getMinHashBitCount());
        op.setMinHashBitCount(2);
        Assert.assertEquals(Integer.valueOf(2), op.getMinHashBitCount());
    }
}