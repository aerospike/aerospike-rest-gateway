package com.aerospike.restclient.config;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.policy.ClientPolicy;
import com.aerospike.restclient.util.AerospikeClientPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AerospikeClientPoolConfig {
    private static final Logger logger = LoggerFactory.getLogger(AerospikeClientPoolConfig.class);

    @Value("${aerospike.restclient.hostname:localhost}")
    String hostname;

    @Value("${aerospike.restclient.port:3000}")
    int port;

    @Value("${aerospike.restclient.hostlist:#{null}}")
    String hostList;

    @Value("${aerospike.restclient.useBoolBin:false}")
    boolean useBoolBin;

    @Value("${aerospike.restclient.pool.size:16}")
    int poolSize;
    @Autowired
    ClientPolicy policy;

    @Autowired
    AerospikeClient defaultClient;

    @Bean
    public AerospikeClientPool configAerospikeClientPool() {
        return new AerospikeClientPool(poolSize, policy, port, hostList, hostname, defaultClient, useBoolBin);
    }
}
