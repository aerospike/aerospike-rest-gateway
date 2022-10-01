package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.cdt.MapOrder;

import java.util.List;

public class MapPolicy {

    private MapOrder order;

    private List<MapWriteFlag> writeFlags;

    public MapPolicy() {
    }

    public MapOrder getOrder() {
        return order;
    }

    public void setOrder(MapOrder order) {
        this.order = order;
    }

    public List<MapWriteFlag> getWriteFlags() {
        return writeFlags;
    }

    public void setWriteFlags(List<MapWriteFlag> writeFlags) {
        this.writeFlags = writeFlags;
    }

    public com.aerospike.client.cdt.MapPolicy toMapPolicy() {
        if (order == null && (writeFlags == null || writeFlags.size() == 0)) {
            return com.aerospike.client.cdt.MapPolicy.Default;
        }

        if (order == null) {
            order = MapOrder.UNORDERED;
        }

        int flags = 0;

        if (writeFlags != null) {
            flags = writeFlags.stream().reduce(0, (acc, flag) -> acc | flag.flag, Integer::sum);
        }

        return new com.aerospike.client.cdt.MapPolicy(order, flags);
    }
}
