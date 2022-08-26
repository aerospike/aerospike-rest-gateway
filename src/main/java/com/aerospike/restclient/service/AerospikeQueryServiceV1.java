package com.aerospike.restclient.service;

import com.aerospike.client.policy.QueryPolicy;
import com.aerospike.client.query.Statement;
import com.aerospike.restclient.domain.auth.AuthDetails;
import com.aerospike.restclient.domain.querymodels.RestClientQueryResponse;
import com.aerospike.restclient.handlers.QueryHandler;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.aerospike.restclient.util.AerospikeClientPool;
import com.aerospike.restclient.util.RestClientErrors;
import com.aerospike.restclient.util.converters.PolicyValueConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class AerospikeQueryServiceV1 implements AerospikeQueryService {

    @Autowired
    private AerospikeClientPool clientPool;

    @Override
    public RestClientQueryResponse query(AuthDetails authDetails, Statement stmt, Map<String, String> requestParams,
                                       QueryPolicy policy, String fromToken) {
        boolean getToken = false;

        if (requestParams.containsKey(AerospikeAPIConstants.GET_TOKEN)) {
            getToken = PolicyValueConverter.getBoolValue(requestParams.get(AerospikeAPIConstants.GET_TOKEN));
        }

        try {
            return QueryHandler.create(clientPool.getClient(authDetails))
                    .queryPartition(policy, stmt, fromToken, getToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public RestClientQueryResponse query(AuthDetails authDetails, Statement stmt, Map<String, String> requestParams,
                                         QueryPolicy policy, String fromToken, int beginPart, int partCount) {
        boolean getToken = false;

        if (requestParams.containsKey(AerospikeAPIConstants.GET_TOKEN)) {
            getToken = PolicyValueConverter.getBoolValue(requestParams.get(AerospikeAPIConstants.GET_TOKEN));
        }


        try {
            return QueryHandler.create(clientPool.getClient(authDetails))
                    .queryPartition(policy, stmt, fromToken, getToken, beginPart, partCount);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
