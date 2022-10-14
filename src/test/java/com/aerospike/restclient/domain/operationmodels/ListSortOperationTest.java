package com.aerospike.restclient.domain.operationmodels;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ListSortOperationTest {

    @Test
    public void getSortFlags() {
        ListSortOperation op = new ListSortOperation("bin");
        Assert.assertNull(op.getSortFlags());
        List<ListSortFlag> flags = new ArrayList<>();
        flags.add(ListSortFlag.DROP_DUPLICATES);
        op.setSortFlags(flags);
        Assert.assertEquals(flags, op.getSortFlags());
    }
}
