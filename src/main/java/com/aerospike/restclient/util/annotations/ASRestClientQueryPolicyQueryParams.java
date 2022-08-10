package com.aerospike.restclient.util.annotations;

import com.aerospike.restclient.util.APIDescriptors;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Parameters(value = {
        @Parameter(
                name = AerospikeAPIConstants.MAX_CONCURRENT_NODES,
                description = APIDescriptors.SCAN_POLICY_MAX_CONCURRENT_NODES_NOTES,
                schema = @Schema(type = "integer"),
                in = ParameterIn.QUERY),
        @Parameter(
                name = AerospikeAPIConstants.RECORD_QUEUE_SIZE,
                description = "TODO",
                schema = @Schema(type = "integer"),
                in = ParameterIn.QUERY),
        @Parameter(
                name = AerospikeAPIConstants.INCLUDE_BIN_DATA,
                description = APIDescriptors.SCAN_POLICY_INCLUDE_BIN_DATA_NOTES,
                schema = @Schema(type = "boolean"),
                in = ParameterIn.QUERY),
        @Parameter(
                name = AerospikeAPIConstants.FAIL_ON_CLUSTER_CHANGE,
                description = "TODO",
                schema = @Schema(type = "boolean"),
                in = ParameterIn.QUERY),
        @Parameter(
                name = AerospikeAPIConstants.SHORT_QUERY,
                description = "TODO",
                schema = @Schema(type = "boolean"),
                in = ParameterIn.QUERY),
})
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ASRestClientPolicyQueryParams
public @interface ASRestClientQueryPolicyQueryParams {
}

