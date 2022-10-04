package com.aerospike.restclient.domain.operationmodels;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = HLLInitOperation.class, name = OperationTypes.HLL_INIT),
                @JsonSubTypes.Type(value = HLLAddOperation.class, name = OperationTypes.HLL_ADD),
                @JsonSubTypes.Type(value = HLLSetUnionOperation.class, name = OperationTypes.HLL_SET_UNION),
                @JsonSubTypes.Type(value = HLLRefreshCountOperation.class, name = OperationTypes.HLL_SET_COUNT),
                @JsonSubTypes.Type(value = HLLFoldOperation.class, name = OperationTypes.HLL_FOLD),
                @JsonSubTypes.Type(value = HLLGetCountOperation.class, name = OperationTypes.HLL_COUNT),
                @JsonSubTypes.Type(value = HLLGetUnionOperation.class, name = OperationTypes.HLL_UNION),
                @JsonSubTypes.Type(value = HLLGetUnionCountOperation.class, name = OperationTypes.HLL_UNION_COUNT),
                @JsonSubTypes.Type(
                        value = HLLGetIntersectionCountOperation.class, name = OperationTypes.HLL_INTERSECT_COUNT
                ),
                @JsonSubTypes.Type(value = HLLGetSimilarityOperation.class, name = OperationTypes.HLL_SIMILARITY),
                @JsonSubTypes.Type(value = HLLDescribeOperation.class, name = OperationTypes.HLL_DESCRIBE),
        }
)
@Schema(
        description = "TODO", oneOf = {
        HLLInitOperation.class,
        HLLAddOperation.class,
        HLLSetUnionOperation.class,
        HLLRefreshCountOperation.class,
        HLLFoldOperation.class,
        HLLGetCountOperation.class,
        HLLGetUnionOperation.class,
        HLLGetUnionCountOperation.class,
        HLLGetIntersectionCountOperation.class,
        HLLGetSimilarityOperation.class,
        HLLDescribeOperation.class,

}
)
abstract public class HLLOperation extends Operation {
    @Schema(required = true)
    protected String binName;

    public HLLOperation(String binName) {
        this.binName = binName;
    }

    public String getBinName() {
        return binName;
    }

    public void setBinName(String binName) {
        this.binName = binName;
    }
}

