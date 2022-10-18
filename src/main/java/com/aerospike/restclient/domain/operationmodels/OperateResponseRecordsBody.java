package com.aerospike.restclient.domain.operationmodels;

import com.aerospike.restclient.domain.RestClientRecord;

public class OperateResponseRecordsBody {
    private RestClientRecord[] records;

    public OperateResponseRecordsBody(RestClientRecord[] records) {
        this.records = records;
    }

    public RestClientRecord[] getRecords() {
        return records;
    }

    public void setRecords(RestClientRecord[] records) {
        this.records = records;
    }
}
