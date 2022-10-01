package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.cdt.ListOrder;

import java.util.List;

public class ListPolicy {
    private ListOrder order;

    private List<ListWriteFlag> writeFlags;

    public ListPolicy() {
    }

    public ListOrder getOrder() {
        return order;
    }

    public void setOrder(ListOrder order) {
        this.order = order;
    }

    public List<ListWriteFlag> getWriteFlags() {
        return writeFlags;
    }

    public void setWriteFlags(List<ListWriteFlag> writeFlags) {
        this.writeFlags = writeFlags;
    }

    public com.aerospike.client.cdt.ListPolicy toListPolicy() {
        if (order == null && (writeFlags == null || writeFlags.size() == 0)) {
            return com.aerospike.client.cdt.ListPolicy.Default;
        }

        if (order == null) {
            order = ListOrder.UNORDERED;
        }

        int flags = 0;

        if (writeFlags != null) {
            flags = writeFlags.stream().reduce(0, (acc, flag) -> acc | flag.flag, Integer::sum);
        }

        return new com.aerospike.client.cdt.ListPolicy(order, flags);
    }
}
