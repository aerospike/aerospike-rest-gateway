package com.aerospike.restclient.domain.operationmodels;

import org.junit.Assert;
import org.junit.Test;

public class HLLInitOperationTest {

    @Test
    public void getMinHashBitCount() {
        HLLInitOperation op = new HLLInitOperation("bin", 0);
        Assert.assertNull(op.getMinHashBitCount());
        op.setMinHashBitCount(8);
        Assert.assertEquals(Integer.valueOf(8), op.getMinHashBitCount());
    }
}

