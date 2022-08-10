package com.aerospike.restclient.domain.querymodels;

import com.aerospike.client.query.Filter;

public interface IRestClientQueryFilter {
    public Filter toFilter();
}
