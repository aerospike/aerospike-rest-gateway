package com.aerospike.restclient.domain;

import com.aerospike.client.BatchDelete;
import com.aerospike.client.BatchRecord;
import com.aerospike.client.policy.BatchDeletePolicy;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;


public class RestClientBatchDeleteBody extends RestClientBatchRecordBody {



    //TODO
    @Schema(description = "TODO")
    public BatchDeletePolicy policy;

    @Override
    public BatchRecord toBatchRecord() {
        // TODO
        return new BatchDelete(policy, key.toKey());
    }
}

