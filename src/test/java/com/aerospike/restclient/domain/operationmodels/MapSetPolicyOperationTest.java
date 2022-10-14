package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.Operation;
import org.junit.Assert;
import org.junit.Test;

public class MapSetPolicyOperationTest {
    @Test
    public void toOperationPolicyIsNull() {
        MapSetPolicyOperation op = new MapSetPolicyOperation("bin", null);
        Operation asOp = op.toOperation();

        Assert.assertEquals(Operation.Type.MAP_MODIFY, asOp.type);
        Assert.assertEquals("bin", asOp.binName);
    }

    @Test
    public void toOperationWithPolicy() {
        MapSetPolicyOperation op = new MapSetPolicyOperation("bin", new MapPolicy());
        Operation asOp = op.toOperation();

        Assert.assertEquals(Operation.Type.MAP_MODIFY, asOp.type);
        Assert.assertEquals("bin", asOp.binName);
    }
}