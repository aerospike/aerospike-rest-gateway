package com.aerospike.restclient.domain.batchmodels;

import java.util.List;


public class BatchResponseBody {
    // TODO: Make the input and return type a map instead of a list for easier backwards compatibility changes
    public List<BatchRecordResponse> batchRecords;
}
