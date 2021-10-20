package com.aerospike.restclient.domain;

import io.swagger.v3.oas.annotations.media.Schema;

public class RestClientRoleQuota {

    @Schema(description = "Maximum reads per second limit.")
    private int readQuota;

    @Schema(description = "Maximum writes per second limit.")
    private int writeQuota;

    public int getReadQuota() {
        return this.readQuota;
    }

    public void setReadQuota(int readQuota) {
        this.readQuota = readQuota;
    }

    public int getWriteQuota() {
        return this.writeQuota;
    }

    public void setWriteQuota(int writeQuota) {
        this.writeQuota = writeQuota;
    }

    public RestClientRoleQuota() {
    }
}
