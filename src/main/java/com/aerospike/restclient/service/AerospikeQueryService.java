package com.aerospike.restclient.service;

import com.aerospike.client.policy.QueryPolicy;
import com.aerospike.client.query.Statement;
import com.aerospike.restclient.domain.auth.AuthDetails;
import com.aerospike.restclient.domain.querymodels.RestClientQueryResponse;
import com.aerospike.restclient.domain.scanmodels.RestClientScanResponse;

import java.util.Map;

public interface AerospikeQueryService {

    RestClientQueryResponse query(AuthDetails authDetails, Statement stmt, Map<String, String> requestParams,
                                  QueryPolicy policy, String fromToken);
    RestClientQueryResponse query(AuthDetails authDetails, Statement stmt, Map<String, String> requestParams,
                                  QueryPolicy policy, String fromToken, int start, int count);
}
