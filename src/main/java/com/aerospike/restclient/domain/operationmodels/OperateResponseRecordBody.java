package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.restclient.domain.RestClientRecord;

public class OperateResponseRecordBody {
    private RestClientRecord record;

    public OperateResponseRecordBody(RestClientRecord record) {
        this.record = record;
    }

    public RestClientRecord getRecord() {
        return record;
    }

    public void setRecord(RestClientRecord record) {
        this.record = record;
    }
}
