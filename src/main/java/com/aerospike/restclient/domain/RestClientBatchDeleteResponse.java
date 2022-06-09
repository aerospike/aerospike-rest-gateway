package com.aerospike.restclient.domain;

import com.aerospike.client.BatchDelete;
import com.aerospike.client.BatchRecord;

public class RestClientBatchDeleteResponse extends RestClientBatchRecordResponse {

    public RestClientBatchDeleteResponse() {}

    public RestClientBatchDeleteResponse(BatchDelete batchDelete) {
        super(batchDelete);
    }
}
