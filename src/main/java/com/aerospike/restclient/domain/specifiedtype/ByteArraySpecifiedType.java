package com.aerospike.restclient.domain.specifiedtype;

import com.aerospike.restclient.util.AerospikeAPIConstants;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(hidden = false)
public class ByteArraySpecifiedType {
    @Schema(required = true, allowableValues = {AerospikeAPIConstants.SpecifiedType.Types.byteArray})
    String type = AerospikeAPIConstants.SpecifiedType.Types.byteArray;

    @Schema(required = true)
    byte[] value;
}
