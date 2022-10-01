package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.client.cdt.MapWriteFlags;

public enum MapWriteFlag {
    /**
     * Default.  Allow create or update.
     */
    DEFAULT(MapWriteFlags.DEFAULT),

    /**
     * If the key already exists, the item will be denied.
     * If the key does not exist, a new item will be created.
     */
    CREATE_ONLY(MapWriteFlags.CREATE_ONLY),

    /**
     * If the key already exists, the item will be overwritten.
     * If the key does not exist, the item will be denied.
     */
    UPDATE_ONLY(MapWriteFlags.UPDATE_ONLY),

    /**
     * Do not raise error if a map item is denied due to write flag constraints.
     */
    NO_FAIL(MapWriteFlags.NO_FAIL),

    /**
     * Allow other valid map items to be committed if a map item is denied due to
     * write flag constraints.
     */
    PARTIAL(MapWriteFlags.PARTIAL);

    public final int flag;

    private MapWriteFlag(int flag) {
        this.flag = flag;
    }
}

