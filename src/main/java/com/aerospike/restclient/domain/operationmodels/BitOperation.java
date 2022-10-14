package com.aerospike.restclient.domain.operationmodels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = BitResizeOperation.class, name = OperationTypes.BIT_RESIZE),
                @JsonSubTypes.Type(value = BitInsertOperation.class, name = OperationTypes.BIT_INSERT),
                @JsonSubTypes.Type(value = BitRemoveOperation.class, name = OperationTypes.BIT_REMOVE),
                @JsonSubTypes.Type(value = BitSetOperation.class, name = OperationTypes.BIT_SET),
                @JsonSubTypes.Type(value = BitOrOperation.class, name = OperationTypes.BIT_OR),
                @JsonSubTypes.Type(value = BitXOrOperation.class, name = OperationTypes.BIT_XOR),
                @JsonSubTypes.Type(value = BitAndOperation.class, name = OperationTypes.BIT_AND),
                @JsonSubTypes.Type(value = BitNotOperation.class, name = OperationTypes.BIT_NOT),
                @JsonSubTypes.Type(value = BitLShiftOperation.class, name = OperationTypes.BIT_LSHIFT),
                @JsonSubTypes.Type(value = BitRShiftOperation.class, name = OperationTypes.BIT_RSHIFT),
                @JsonSubTypes.Type(value = BitAddOperation.class, name = OperationTypes.BIT_ADD),
                @JsonSubTypes.Type(value = BitSubtractOperation.class, name = OperationTypes.BIT_SUBTRACT),
                @JsonSubTypes.Type(value = BitSetOperation.class, name = OperationTypes.BIT_SET),
                @JsonSubTypes.Type(value = BitGetOperation.class, name = OperationTypes.BIT_GET),
                @JsonSubTypes.Type(value = BitCountOperation.class, name = OperationTypes.BIT_COUNT),
                @JsonSubTypes.Type(value = BitLScanOperation.class, name = OperationTypes.BIT_LSCAN),
                @JsonSubTypes.Type(value = BitRScanOperation.class, name = OperationTypes.BIT_RSCAN),
                @JsonSubTypes.Type(value = BitSetIntOperation.class, name = OperationTypes.BIT_SET_INT),
                @JsonSubTypes.Type(value = BitGetIntOperation.class, name = OperationTypes.BIT_GET_INT)
        }
)
@Schema(
        description = "The base type for describing all bit operations. Should not be used directly.", oneOf = {
        BitResizeOperation.class,
        BitInsertOperation.class,
        BitRemoveOperation.class,
        BitSetOperation.class,
        BitOrOperation.class,
        BitXOrOperation.class,
        BitAndOperation.class,
        BitNotOperation.class,
        BitLShiftOperation.class,
        BitRShiftOperation.class,
        BitAddOperation.class,
        BitSubtractOperation.class,
        BitSetOperation.class,
        BitGetOperation.class,
        BitCountOperation.class,
        BitLScanOperation.class,
        BitRScanOperation.class,
        BitSetIntOperation.class,
        BitGetIntOperation.class,
}

//        public (\w*)Operation\((\w*) (\w*)\, (\w*) (\w*)\) \{
//        public $1Operation($2 $3\, $4 $5\) {
        )
abstract public class BitOperation extends Operation {
    @Schema(required = true)
    protected String binName;

    @JsonCreator
    public BitOperation(@JsonProperty(value = "binName", required = true) String binName) {
        this.binName = binName;
    }
}

