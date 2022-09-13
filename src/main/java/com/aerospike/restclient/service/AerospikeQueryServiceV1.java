package com.aerospike.restclient.service;

import com.aerospike.client.AerospikeException;
import com.aerospike.client.policy.QueryPolicy;
import com.aerospike.client.query.Statement;
import com.aerospike.restclient.domain.auth.AuthDetails;
import com.aerospike.restclient.domain.querymodels.QueryResponseBody;
import com.aerospike.restclient.handlers.QueryHandler;
import com.aerospike.restclient.util.AerospikeClientPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AerospikeQueryServiceV1 implements AerospikeQueryService {

    @Autowired
    private AerospikeClientPool clientPool;

    @Override
    public QueryResponseBody query(AuthDetails authDetails, QueryPolicy policy, Statement stmt, boolean getToken,
                                   String fromToken) {
        try {
            return QueryHandler.create(clientPool.getClient(authDetails)).queryPartition(policy, stmt, fromToken,
                    getToken);
        } catch (IOException e) {
            throw new AerospikeException(e);
        }
    }

    @Override
    public QueryResponseBody query(AuthDetails authDetails, QueryPolicy policy, Statement stmt, boolean getToken,
                                   String fromToken, int beginPart, int partCount) {
        try {
            return QueryHandler.create(clientPool.getClient(authDetails)).queryPartition(policy, stmt, fromToken,
                    getToken, beginPart, partCount);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
