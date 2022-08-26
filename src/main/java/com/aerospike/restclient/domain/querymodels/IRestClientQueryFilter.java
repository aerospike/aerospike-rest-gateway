package com.aerospike.restclient.domain.querymodels;

import com.aerospike.client.query.Filter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(

)
public interface IRestClientQueryFilter {
    public Filter toFilter();
}
