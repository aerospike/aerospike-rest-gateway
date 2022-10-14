package com.aerospike.restclient.domain.operationmodels;

import org.junit.Assert;
import org.junit.Test;

public class MapRemoveByValueRelativeRankRangeTest {

    @Test
    public void getCount() {
        MapRemoveByValueRelativeRankRange op = new MapRemoveByValueRelativeRankRange("bin", 1, 1, MapReturnType.RANK);
        Assert.assertNull(op.getCount());
        op.setCount(6);
        Assert.assertEquals(Integer.valueOf(6), op.getCount());
    }

    @Test
    public void isInverted() {
        MapRemoveByValueRelativeRankRange op = new MapRemoveByValueRelativeRankRange("bin", 1, 1, MapReturnType.RANK);
        Assert.assertFalse(op.isInverted());
        op.setInverted(true);
        Assert.assertTrue(op.isInverted());
    }
}

