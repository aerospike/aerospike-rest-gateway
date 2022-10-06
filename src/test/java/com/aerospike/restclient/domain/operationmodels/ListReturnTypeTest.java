package com.aerospike.restclient.domain.operationmodels;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class ListReturnTypeTest {

    @Parameterized.Parameters
    public static Object[][] getParams() {
        return new Object[][]{
                new Object[]{ListReturnType.COUNT, false, com.aerospike.client.cdt.ListReturnType.COUNT},
                new Object[]{
                        ListReturnType.COUNT,
                        true,
                        com.aerospike.client.cdt.ListReturnType.COUNT | com.aerospike.client.cdt.ListReturnType.INVERTED
                },
                new Object[]{ListReturnType.INDEX, false, com.aerospike.client.cdt.ListReturnType.INDEX},
                new Object[]{
                        ListReturnType.INDEX,
                        true,
                        com.aerospike.client.cdt.ListReturnType.INDEX | com.aerospike.client.cdt.ListReturnType.INVERTED
                },
                new Object[]{ListReturnType.EXISTS, false, com.aerospike.client.cdt.ListReturnType.EXISTS},
                new Object[]{
                        ListReturnType.EXISTS,
                        true,
                        com.aerospike.client.cdt.ListReturnType.EXISTS | com.aerospike.client.cdt.ListReturnType.INVERTED
                },
                new Object[]{ListReturnType.NONE, false, com.aerospike.client.cdt.ListReturnType.NONE},
                new Object[]{
                        ListReturnType.NONE,
                        true,
                        com.aerospike.client.cdt.ListReturnType.NONE | com.aerospike.client.cdt.ListReturnType.INVERTED
                },
                new Object[]{ListReturnType.RANK, false, com.aerospike.client.cdt.ListReturnType.RANK},
                new Object[]{
                        ListReturnType.RANK,
                        true,
                        com.aerospike.client.cdt.ListReturnType.RANK | com.aerospike.client.cdt.ListReturnType.INVERTED
                },
                new Object[]{
                        ListReturnType.REVERSE_INDEX, false, com.aerospike.client.cdt.ListReturnType.REVERSE_INDEX
                },
                new Object[]{
                        ListReturnType.REVERSE_INDEX,
                        true,
                        com.aerospike.client.cdt.ListReturnType.REVERSE_INDEX | com.aerospike.client.cdt.ListReturnType.INVERTED
                },
                new Object[]{ListReturnType.REVERSE_RANK, false, com.aerospike.client.cdt.ListReturnType.REVERSE_RANK},
                new Object[]{
                        ListReturnType.REVERSE_RANK,
                        true,
                        com.aerospike.client.cdt.ListReturnType.REVERSE_RANK | com.aerospike.client.cdt.ListReturnType.INVERTED
                },
                new Object[]{ListReturnType.VALUE, false, com.aerospike.client.cdt.ListReturnType.VALUE},
                new Object[]{
                        ListReturnType.VALUE,
                        true,
                        com.aerospike.client.cdt.ListReturnType.VALUE | com.aerospike.client.cdt.ListReturnType.INVERTED
                },
                };
    }

    ListReturnType enum_;
    boolean inverted;
    int flag;

    public ListReturnTypeTest(ListReturnType enum_, boolean inverted, int flag) {
        this.enum_ = enum_;
        this.inverted = inverted;
        this.flag = flag;
    }

    @Test
    public void toListReturnType() {
        Assert.assertEquals(enum_.toListReturnType(inverted), flag);
    }
}

