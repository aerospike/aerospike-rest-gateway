//package com.aerospike.restclient.domain;
//
//import com.aerospike.client.exp.Expression;
//import com.aerospike.client.policy.BatchUDFPolicy;
//import com.aerospike.client.policy.CommitLevel;
//import io.swagger.v3.oas.annotations.media.Schema;
//
//public class RestClientBatchUDFPolicyBody {
//    @Schema(description = "Desired consistency guarantee when committing a transaction on the server.")
//    public CommitLevel commitLevel;
//
//    @Schema(description = "If the transaction results in a record deletion, leave a tombstone for the record.")
//    public boolean durableDelete;
//
//    @Schema(description = "Record expiration.")
//    public int expiration;
//
//    @Schema(description = "Optional expression filter.")
//    public Expression filterExpression;
//
//    @Schema(description = "Send user defined key in addition to hash digest.")
//    public boolean sendKey;
//
//    public RestClientBatchUDFPolicyBody() {}
//
//    public BatchUDFPolicy toBatchUDFPolicy() {
//        BatchUDFPolicy policy = new BatchUDFPolicy();
//        policy.commitLevel = commitLevel;
//        policy.durableDelete = durableDelete;
//        policy.expiration = expiration;
//        policy.filterExp = filterExpression;
//        policy.sendKey = sendKey;
//
//        return policy;
//    }
//}
