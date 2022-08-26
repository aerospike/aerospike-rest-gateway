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

@ASRestClientParams.ASRestClientRecordBinsQueryParam
@Parameters(value = {
        @Parameter(
                name = AerospikeAPIConstants.QUERY_INDEX_NAME,
                description = "TODO",
                schema = @Schema(type = "string"),
                in = ParameterIn.QUERY),
        @Parameter(
                name = AerospikeAPIConstants.MAX_RECORDS,
                description = "TODO",
                schema = @Schema(type = "integer"),
                in = ParameterIn.QUERY),
        @Parameter(
                name = AerospikeAPIConstants.RECORDS_PER_SECOND,
                description = APIDescriptors.SCAN_POLICY_INCLUDE_BIN_DATA_NOTES,
                schema = @Schema(type = "boolean"),
                in = ParameterIn.QUERY),
        @Parameter(
                name = AerospikeAPIConstants.QUERY_PARTITION_BEGIN,
                description = "TODO",
                schema = @Schema(type = "integer"),
                in = ParameterIn.QUERY),
        @Parameter(
                name = AerospikeAPIConstants.QUERY_PARTITION_COUNT,
                description = "TODO",
                schema = @Schema(type = "integer"),
                in = ParameterIn.QUERY),
})
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ASRestClientPolicyQueryParams
public @interface ASRestClientQueryQueryParams {
}
