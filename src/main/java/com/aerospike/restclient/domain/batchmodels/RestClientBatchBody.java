package com.aerospike.restclient.domain.batchmodels;

import java.util.List;

public class RestClientBatchBody {
    // TODO: Make the input and return type a map instead of a list for easier backwards compatibility changes
    List<RestClientBatchRecordBody> batchOps;
}
