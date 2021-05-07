package com.aerospike.restclient.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "RestClientRoleQuota")
public class RestClientRoleQuota {

    @ApiModelProperty(value = "Maximum reads per second limit.")
    private int readQuota;

    @ApiModelProperty(value = "Maximum writes per second limit.")
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
