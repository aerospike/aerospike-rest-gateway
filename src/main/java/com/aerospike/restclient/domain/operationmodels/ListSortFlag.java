package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.cdt.ListSortFlags;

public enum ListSortFlag {
    /**
     * Default.  Preserve duplicate values when sorting list.
     */
    DEFAULT(ListSortFlags.DEFAULT),

    /**
     * Drop duplicate values when sorting list.
     */
    DROP_DUPLICATES(ListSortFlags.DROP_DUPLICATES);

    public final int flag;

    private ListSortFlag(int flag) {
        this.flag = flag;
    }
}
