package com.aerospike.restclient.util.annotations;

import com.aerospike.restclient.util.APIDescriptors;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public @interface ASRestClientParams {
    @Parameters(
            value = {
                    @Parameter(
                            name = AerospikeAPIConstants.RECORD_BINS,
                            description = APIDescriptors.BINS_NOTES,
                            array = @ArraySchema(schema = @Schema(type = "string")),
                            in = ParameterIn.QUERY
                    )
            }
    )
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ASRestClientRecordBinsQueryParam {
    }

    @Parameters(
            value = {
                    @Parameter(
                            name = AerospikeAPIConstants.KEY_TYPE,
                            description = APIDescriptors.KEYTYPE_NOTES,
                            schema = @Schema(implementation = AerospikeAPIConstants.RecordKeyType.class),
                            in = ParameterIn.QUERY
                    )
            }
    )
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ASRestClientKeyTypeQueryParam {
    }

    @Parameter(
            name = AerospikeAPIConstants.FROM_TOKEN,
            description = APIDescriptors.SCAN_FROM_TOKEN_NOTES,
            schema = @Schema(type = "string"),
            in = ParameterIn.QUERY
    )
    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ASRestClientFromScanQueryParam {
    }
}
