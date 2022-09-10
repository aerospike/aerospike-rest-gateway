package com.aerospike.restclient.service;

import com.aerospike.client.policy.QueryPolicy;
import com.aerospike.client.query.Statement;
import com.aerospike.restclient.domain.auth.AuthDetails;
import com.aerospike.restclient.domain.querymodels.QueryResponseBody;

public interface AerospikeQueryService {

    QueryResponseBody query(AuthDetails authDetails, QueryPolicy policy, Statement stmt, boolean getToken,
                            String fromToken);

    QueryResponseBody query(AuthDetails authDetails, QueryPolicy policy, Statement stmt, boolean getToken,
                            String fromToken,
                            int start, int count);
}
