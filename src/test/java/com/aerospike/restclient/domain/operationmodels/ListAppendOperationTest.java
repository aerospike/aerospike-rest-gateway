package com.aerospike.restclient.domain.operationmodels;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ListAppendOperationTest {

    @Test
    public void getMinHashBitCount() {
        ListAppendOperation op = new ListAppendOperation("bin", new ArrayList<>());
        Assert.assertNull(op.getListPolicy());
        ListPolicy policy = new ListPolicy();
        List<ListWriteFlag> flags = new ArrayList<>();
        flags.add(ListWriteFlag.ADD_UNIQUE);
        policy.setWriteFlags(flags);
        op.setListPolicy(policy);
        Assert.assertEquals(policy, op.getListPolicy());
    }
}

