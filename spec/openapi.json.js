openApiSpec = {
  "openapi": "3.0.1",
  "info": {
    "title": "Aerospike REST Gateway",
    "description": "REST Interface for Aerospike Database.",
    "contact": {
      "name": "Aerospike, Inc.",
      "url": "https://www.aerospike.com"
    },
    "license": {
      "name": "Apache 2.0 License",
      "url": "http://www.apache.org/licenses/LICENSE-2.0"
    },
    "version": "2.0.2"
  },
  "servers": [
    {
      "url": "http://localhost:8080",
      "description": "Generated server url"
    }
  ],
  "tags": [
    {
      "name": "Scan Operations",
      "description": "Read records in specified namespace, set."
    },
    {
      "name": "Admin Operations",
      "description": "Manage users and privileges."
    },
    {
      "name": "Secondary Index methods",
      "description": "Manage secondary indexes."
    },
    {
      "name": "Key Value Operations",
      "description": "Perform simple operations on a single record."
    },
    {
      "name": "Query Operations",
      "description": "Read records in specified namespace, set."
    },
    {
      "name": "Cluster information operations",
      "description": "Retrieve basic information about the Aerospike cluster."
    },
    {
      "name": "Execute Operations",
      "description": "Execute operations in background scan/query."
    },
    {
      "name": "Info Operations",
      "description": "Send info commands to nodes in the Aerospike cluster."
    },
    {
      "name": "Document API Operations",
      "description": "Perform operations on records using JSONPath queries."
    },
    {
      "name": "Truncate Operations",
      "description": "Remove multiple records from the server."
    },
    {
      "name": "Batch Operations",
      "description": "Retrieve multiple records from the server."
    },
    {
      "name": "Operate operations",
      "description": "Perform multiple operations atomically on a single record."
    }
  ],
  "paths": {
    "/v1/kvs/{namespace}/{set}/{key}": {
      "get": {
        "tags": [
          "Key Value Operations"
        ],
        "summary": "Return the metadata and bins for a record.",
        "operationId": "getRecordNamespaceSetKey",
        "parameters": [
          {
            "name": "namespace",
            "in": "path",
            "description": "Namespace for the record; equivalent to database name.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "set",
            "in": "path",
            "description": "Set for the record; equivalent to database table.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "key",
            "in": "path",
            "description": "Userkey for the record.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "keytype",
            "in": "query",
            "description": "The Type of the userKey.",
            "schema": {
              "type": "string",
              "enum": [
                "STRING",
                "INTEGER",
                "BYTES",
                "DIGEST"
              ]
            }
          },
          {
            "name": "recordBins",
            "in": "query",
            "description": "Optionally specify a set of bins to return when fetching a record. If omitted, all bins will be returned.",
            "schema": {
              "type": "array",
              "items": {
                "type": "string"
              }
            }
          },
          {
            "name": "sendKey",
            "in": "query",
            "description": "Send user defined key in addition to hash digest on both reads and writes.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "replica",
            "in": "query",
            "description": "Replica algorithm used to determine the target node for a single record command.",
            "schema": {
              "type": "string",
              "enum": [
                "MASTER",
                "MASTER_PROLES",
                "SEQUENCE",
                "PREFER_RACK",
                "RANDOM"
              ]
            }
          },
          {
            "name": "readModeSC",
            "in": "query",
            "description": "Read policy for SC (strong consistency) namespaces. Determines SC read consistency options.",
            "schema": {
              "type": "string",
              "enum": [
                "SESSION",
                "LINEARIZE",
                "ALLOW_REPLICA",
                "ALLOW_UNAVAILABLE"
              ]
            }
          },
          {
            "name": "readModeAP",
            "in": "query",
            "description": "Read policy for AP (availability) namespaces. How duplicates should be consulted in a read operation. Only makes a difference during migrations and only applicable in AP mode.",
            "schema": {
              "type": "string",
              "enum": [
                "ONE",
                "ALL"
              ]
            }
          },
          {
            "name": "totalTimeout",
            "in": "query",
            "description": "Total transaction timeout in milliseconds.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "socketTimeout",
            "in": "query",
            "description": "Socket idle timeout in milliseconds when processing a database command.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "sleepBetweenRetries",
            "in": "query",
            "description": "Milliseconds to sleep between retries.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "maxRetries",
            "in": "query",
            "description": "Maximum number of retries before aborting the current transaction.\nThe initial attempt is not counted as a retry.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "filterexp",
            "in": "query",
            "description": "Optional Filter Expression (introduced in Aerospike Database 5.2.0) in infix notation DSL.",
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "compress",
            "in": "query",
            "description": "Use zlib compression on command buffers sent to the server and responses received from the server when the buffer size is greater than 128 bytes.",
            "schema": {
              "type": "boolean"
            }
          }
        ],
        "responses": {
          "400": {
            "description": "Invalid parameters or request",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "200": {
            "description": "Metadata and bins for a record returned successfully.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientRecord"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientRecord"
                }
              }
            }
          },
          "403": {
            "description": "Not authorized to access the resource",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "404": {
            "description": "Record not found.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          }
        }
      },
      "put": {
        "tags": [
          "Key Value Operations"
        ],
        "summary": "Replace the bins of the specified record.",
        "operationId": "replaceRecordNamespaceSetKey_1",
        "parameters": [
          {
            "name": "namespace",
            "in": "path",
            "description": "Namespace for the record; equivalent to database name.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "set",
            "in": "path",
            "description": "Set for the record; equivalent to database table.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "key",
            "in": "path",
            "description": "Userkey for the record.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "requestParams",
            "in": "query",
            "required": true,
            "schema": {
              "type": "object",
              "additionalProperties": {
                "type": "string"
              }
            }
          },
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "keytype",
            "in": "query",
            "description": "The Type of the userKey.",
            "schema": {
              "type": "string",
              "enum": [
                "STRING",
                "INTEGER",
                "BYTES",
                "DIGEST"
              ]
            }
          },
          {
            "name": "expiration",
            "in": "query",
            "description": "Record expiration. Also known as ttl (time to live). Seconds record will live before being removed by the server.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "generation",
            "in": "query",
            "description": "Expected generation. Generation is the number of times a record has been modified (including creation) on the server.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "durableDelete",
            "in": "query",
            "description": "If the transaction results in a record deletion, leave a tombstone for the record.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "respondAllOps",
            "in": "query",
            "description": "For client operate(), return a result for every operation.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "commitLevel",
            "in": "query",
            "description": "Desired consistency guarantee when committing a transaction on the server.",
            "schema": {
              "type": "string",
              "enum": [
                "COMMIT_ALL",
                "COMMIT_MASTER"
              ]
            }
          },
          {
            "name": "generationPolicy",
            "in": "query",
            "description": "Qualify how to handle record writes based on record generation.",
            "schema": {
              "type": "string",
              "enum": [
                "NONE",
                "EXPECT_GEN_EQUAL",
                "EXPECT_GEN_GT"
              ]
            }
          },
          {
            "name": "recordExistsAction",
            "in": "query",
            "description": "How to handle the existence of the record. This is ignored for POST/PUT/UPDATE kvs methods.",
            "schema": {
              "type": "string",
              "enum": [
                "UPDATE",
                "UPDATE_ONLY",
                "REPLACE",
                "REPLACE_ONLY",
                "CREATE_ONLY"
              ]
            }
          },
          {
            "name": "sendKey",
            "in": "query",
            "description": "Send user defined key in addition to hash digest on both reads and writes.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "replica",
            "in": "query",
            "description": "Replica algorithm used to determine the target node for a single record command.",
            "schema": {
              "type": "string",
              "enum": [
                "MASTER",
                "MASTER_PROLES",
                "SEQUENCE",
                "PREFER_RACK",
                "RANDOM"
              ]
            }
          },
          {
            "name": "readModeSC",
            "in": "query",
            "description": "Read policy for SC (strong consistency) namespaces. Determines SC read consistency options.",
            "schema": {
              "type": "string",
              "enum": [
                "SESSION",
                "LINEARIZE",
                "ALLOW_REPLICA",
                "ALLOW_UNAVAILABLE"
              ]
            }
          },
          {
            "name": "readModeAP",
            "in": "query",
            "description": "Read policy for AP (availability) namespaces. How duplicates should be consulted in a read operation. Only makes a difference during migrations and only applicable in AP mode.",
            "schema": {
              "type": "string",
              "enum": [
                "ONE",
                "ALL"
              ]
            }
          },
          {
            "name": "totalTimeout",
            "in": "query",
            "description": "Total transaction timeout in milliseconds.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "socketTimeout",
            "in": "query",
            "description": "Socket idle timeout in milliseconds when processing a database command.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "sleepBetweenRetries",
            "in": "query",
            "description": "Milliseconds to sleep between retries.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "maxRetries",
            "in": "query",
            "description": "Maximum number of retries before aborting the current transaction.\nThe initial attempt is not counted as a retry.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "filterexp",
            "in": "query",
            "description": "Optional Filter Expression (introduced in Aerospike Database 5.2.0) in infix notation DSL.",
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "compress",
            "in": "query",
            "description": "Use zlib compression on command buffers sent to the server and responses received from the server when the buffer size is greater than 128 bytes.",
            "schema": {
              "type": "boolean"
            }
          }
        ],
        "requestBody": {
          "description": "Bins to be stored in the record. This is a mapping from a string bin name to a value. Value can be a String, integer, floating point number, list, map, bytearray, or GeoJSON value. Bytearrays and GeoJSON can only be sent using MessagePack\n example: {\"bin1\":5, \"bin2\":\"hello\", \"bin3\": [1,2,3], \"bin4\": {\"one\": 1}}",
          "content": {
            "application/json": {
              "schema": {
                "type": "object",
                "additionalProperties": {
                  "type": "object"
                }
              },
              "examples": {
                "Bins request body example": {
                  "description": "Bins request body example",
                  "value": {
                    "bin1": 5,
                    "bin2": "hello",
                    "bin3": [
                      1,
                      2,
                      3
                    ],
                    "bin4": {
                      "one": 1
                    }
                  }
                }
              }
            }
          },
          "required": true
        },
        "responses": {
          "400": {
            "description": "Invalid parameters or request",
            "content": {
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "403": {
            "description": "Not authorized to access the resource",
            "content": {
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "409": {
            "description": "Generation mismatch for operation.",
            "content": {
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "204": {
            "description": "Modified record successfully, no content expected."
          },
          "404": {
            "description": "Record not found.",
            "content": {
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          }
        }
      },
      "post": {
        "tags": [
          "Key Value Operations"
        ],
        "summary": "Create a new record with the provided bins into the record.",
        "operationId": "createRecordNamespaceSetKey",
        "parameters": [
          {
            "name": "namespace",
            "in": "path",
            "description": "Namespace for the record; equivalent to database name.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "set",
            "in": "path",
            "description": "Set for the record; equivalent to database table.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "key",
            "in": "path",
            "description": "Userkey for the record.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "keytype",
            "in": "query",
            "description": "The Type of the userKey.",
            "schema": {
              "type": "string",
              "enum": [
                "STRING",
                "INTEGER",
                "BYTES",
                "DIGEST"
              ]
            }
          },
          {
            "name": "expiration",
            "in": "query",
            "description": "Record expiration. Also known as ttl (time to live). Seconds record will live before being removed by the server.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "generation",
            "in": "query",
            "description": "Expected generation. Generation is the number of times a record has been modified (including creation) on the server.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "durableDelete",
            "in": "query",
            "description": "If the transaction results in a record deletion, leave a tombstone for the record.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "respondAllOps",
            "in": "query",
            "description": "For client operate(), return a result for every operation.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "commitLevel",
            "in": "query",
            "description": "Desired consistency guarantee when committing a transaction on the server.",
            "schema": {
              "type": "string",
              "enum": [
                "COMMIT_ALL",
                "COMMIT_MASTER"
              ]
            }
          },
          {
            "name": "generationPolicy",
            "in": "query",
            "description": "Qualify how to handle record writes based on record generation.",
            "schema": {
              "type": "string",
              "enum": [
                "NONE",
                "EXPECT_GEN_EQUAL",
                "EXPECT_GEN_GT"
              ]
            }
          },
          {
            "name": "recordExistsAction",
            "in": "query",
            "description": "How to handle the existence of the record. This is ignored for POST/PUT/UPDATE kvs methods.",
            "schema": {
              "type": "string",
              "enum": [
                "UPDATE",
                "UPDATE_ONLY",
                "REPLACE",
                "REPLACE_ONLY",
                "CREATE_ONLY"
              ]
            }
          },
          {
            "name": "sendKey",
            "in": "query",
            "description": "Send user defined key in addition to hash digest on both reads and writes.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "replica",
            "in": "query",
            "description": "Replica algorithm used to determine the target node for a single record command.",
            "schema": {
              "type": "string",
              "enum": [
                "MASTER",
                "MASTER_PROLES",
                "SEQUENCE",
                "PREFER_RACK",
                "RANDOM"
              ]
            }
          },
          {
            "name": "readModeSC",
            "in": "query",
            "description": "Read policy for SC (strong consistency) namespaces. Determines SC read consistency options.",
            "schema": {
              "type": "string",
              "enum": [
                "SESSION",
                "LINEARIZE",
                "ALLOW_REPLICA",
                "ALLOW_UNAVAILABLE"
              ]
            }
          },
          {
            "name": "readModeAP",
            "in": "query",
            "description": "Read policy for AP (availability) namespaces. How duplicates should be consulted in a read operation. Only makes a difference during migrations and only applicable in AP mode.",
            "schema": {
              "type": "string",
              "enum": [
                "ONE",
                "ALL"
              ]
            }
          },
          {
            "name": "totalTimeout",
            "in": "query",
            "description": "Total transaction timeout in milliseconds.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "socketTimeout",
            "in": "query",
            "description": "Socket idle timeout in milliseconds when processing a database command.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "sleepBetweenRetries",
            "in": "query",
            "description": "Milliseconds to sleep between retries.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "maxRetries",
            "in": "query",
            "description": "Maximum number of retries before aborting the current transaction.\nThe initial attempt is not counted as a retry.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "filterexp",
            "in": "query",
            "description": "Optional Filter Expression (introduced in Aerospike Database 5.2.0) in infix notation DSL.",
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "compress",
            "in": "query",
            "description": "Use zlib compression on command buffers sent to the server and responses received from the server when the buffer size is greater than 128 bytes.",
            "schema": {
              "type": "boolean"
            }
          }
        ],
        "requestBody": {
          "description": "Bins to be stored in the record. This is a mapping from a string bin name to a value. Value can be a String, integer, floating point number, list, map, bytearray, or GeoJSON value. Bytearrays and GeoJSON can only be sent using MessagePack\n example: {\"bin1\":5, \"bin2\":\"hello\", \"bin3\": [1,2,3], \"bin4\": {\"one\": 1}}",
          "content": {
            "application/json": {
              "schema": {
                "type": "object",
                "additionalProperties": {
                  "type": "object"
                }
              },
              "examples": {
                "Bins request body example": {
                  "description": "Bins request body example",
                  "value": {
                    "bin1": 5,
                    "bin2": "hello",
                    "bin3": [
                      1,
                      2,
                      3
                    ],
                    "bin4": {
                      "one": 1
                    }
                  }
                }
              }
            }
          },
          "required": true
        },
        "responses": {
          "400": {
            "description": "Invalid parameters or request",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "404": {
            "description": "Namespace does not exist.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "201": {
            "description": "Created a new record successfully."
          },
          "403": {
            "description": "Not authorized to access the resource",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "409": {
            "description": "Record Already exists.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          }
        }
      },
      "delete": {
        "tags": [
          "Key Value Operations"
        ],
        "summary": "Delete the specified record.",
        "operationId": "deleteRecordNamespaceSetKey",
        "parameters": [
          {
            "name": "namespace",
            "in": "path",
            "description": "Namespace for the record; equivalent to database name.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "set",
            "in": "path",
            "description": "Set for the record; equivalent to database table.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "key",
            "in": "path",
            "description": "Userkey for the record.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "keytype",
            "in": "query",
            "description": "The Type of the userKey.",
            "schema": {
              "type": "string",
              "enum": [
                "STRING",
                "INTEGER",
                "BYTES",
                "DIGEST"
              ]
            }
          },
          {
            "name": "expiration",
            "in": "query",
            "description": "Record expiration. Also known as ttl (time to live). Seconds record will live before being removed by the server.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "generation",
            "in": "query",
            "description": "Expected generation. Generation is the number of times a record has been modified (including creation) on the server.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "durableDelete",
            "in": "query",
            "description": "If the transaction results in a record deletion, leave a tombstone for the record.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "respondAllOps",
            "in": "query",
            "description": "For client operate(), return a result for every operation.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "commitLevel",
            "in": "query",
            "description": "Desired consistency guarantee when committing a transaction on the server.",
            "schema": {
              "type": "string",
              "enum": [
                "COMMIT_ALL",
                "COMMIT_MASTER"
              ]
            }
          },
          {
            "name": "generationPolicy",
            "in": "query",
            "description": "Qualify how to handle record writes based on record generation.",
            "schema": {
              "type": "string",
              "enum": [
                "NONE",
                "EXPECT_GEN_EQUAL",
                "EXPECT_GEN_GT"
              ]
            }
          },
          {
            "name": "recordExistsAction",
            "in": "query",
            "description": "How to handle the existence of the record. This is ignored for POST/PUT/UPDATE kvs methods.",
            "schema": {
              "type": "string",
              "enum": [
                "UPDATE",
                "UPDATE_ONLY",
                "REPLACE",
                "REPLACE_ONLY",
                "CREATE_ONLY"
              ]
            }
          },
          {
            "name": "sendKey",
            "in": "query",
            "description": "Send user defined key in addition to hash digest on both reads and writes.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "replica",
            "in": "query",
            "description": "Replica algorithm used to determine the target node for a single record command.",
            "schema": {
              "type": "string",
              "enum": [
                "MASTER",
                "MASTER_PROLES",
                "SEQUENCE",
                "PREFER_RACK",
                "RANDOM"
              ]
            }
          },
          {
            "name": "readModeSC",
            "in": "query",
            "description": "Read policy for SC (strong consistency) namespaces. Determines SC read consistency options.",
            "schema": {
              "type": "string",
              "enum": [
                "SESSION",
                "LINEARIZE",
                "ALLOW_REPLICA",
                "ALLOW_UNAVAILABLE"
              ]
            }
          },
          {
            "name": "readModeAP",
            "in": "query",
            "description": "Read policy for AP (availability) namespaces. How duplicates should be consulted in a read operation. Only makes a difference during migrations and only applicable in AP mode.",
            "schema": {
              "type": "string",
              "enum": [
                "ONE",
                "ALL"
              ]
            }
          },
          {
            "name": "totalTimeout",
            "in": "query",
            "description": "Total transaction timeout in milliseconds.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "socketTimeout",
            "in": "query",
            "description": "Socket idle timeout in milliseconds when processing a database command.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "sleepBetweenRetries",
            "in": "query",
            "description": "Milliseconds to sleep between retries.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "maxRetries",
            "in": "query",
            "description": "Maximum number of retries before aborting the current transaction.\nThe initial attempt is not counted as a retry.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "filterexp",
            "in": "query",
            "description": "Optional Filter Expression (introduced in Aerospike Database 5.2.0) in infix notation DSL.",
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "compress",
            "in": "query",
            "description": "Use zlib compression on command buffers sent to the server and responses received from the server when the buffer size is greater than 128 bytes.",
            "schema": {
              "type": "boolean"
            }
          }
        ],
        "responses": {
          "400": {
            "description": "Invalid parameters or request",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "204": {
            "description": "Deleted a record successfully, no content expected."
          },
          "403": {
            "description": "Not authorized to access the resource",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "409": {
            "description": "Generation mismatch for operation.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "404": {
            "description": "Record not found.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          }
        }
      },
      "head": {
        "tags": [
          "Key Value Operations"
        ],
        "summary": "recordExistsNamespaceSetKey",
        "operationId": "Check if a record exists",
        "parameters": [
          {
            "name": "namespace",
            "in": "path",
            "description": "Namespace for the record; equivalent to database name.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "set",
            "in": "path",
            "description": "Set for the record; equivalent to database table.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "key",
            "in": "path",
            "description": "Userkey for the record.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "keytype",
            "in": "query",
            "description": "The Type of the userKey.",
            "required": false,
            "schema": {
              "type": "string",
              "enum": [
                "STRING",
                "INTEGER",
                "BYTES",
                "DIGEST"
              ]
            }
          },
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "Record exists indication returned successfully."
          },
          "404": {
            "description": "Record does not exists.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          }
        }
      },
      "patch": {
        "tags": [
          "Key Value Operations"
        ],
        "summary": "Merge the provided bins into the record.",
        "operationId": "updateRecordNamespaceSetKey",
        "parameters": [
          {
            "name": "namespace",
            "in": "path",
            "description": "Namespace for the record; equivalent to database name.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "set",
            "in": "path",
            "description": "Set for the record; equivalent to database table.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "key",
            "in": "path",
            "description": "Userkey for the record.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "keytype",
            "in": "query",
            "description": "The Type of the userKey.",
            "schema": {
              "type": "string",
              "enum": [
                "STRING",
                "INTEGER",
                "BYTES",
                "DIGEST"
              ]
            }
          },
          {
            "name": "expiration",
            "in": "query",
            "description": "Record expiration. Also known as ttl (time to live). Seconds record will live before being removed by the server.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "generation",
            "in": "query",
            "description": "Expected generation. Generation is the number of times a record has been modified (including creation) on the server.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "durableDelete",
            "in": "query",
            "description": "If the transaction results in a record deletion, leave a tombstone for the record.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "respondAllOps",
            "in": "query",
            "description": "For client operate(), return a result for every operation.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "commitLevel",
            "in": "query",
            "description": "Desired consistency guarantee when committing a transaction on the server.",
            "schema": {
              "type": "string",
              "enum": [
                "COMMIT_ALL",
                "COMMIT_MASTER"
              ]
            }
          },
          {
            "name": "generationPolicy",
            "in": "query",
            "description": "Qualify how to handle record writes based on record generation.",
            "schema": {
              "type": "string",
              "enum": [
                "NONE",
                "EXPECT_GEN_EQUAL",
                "EXPECT_GEN_GT"
              ]
            }
          },
          {
            "name": "recordExistsAction",
            "in": "query",
            "description": "How to handle the existence of the record. This is ignored for POST/PUT/UPDATE kvs methods.",
            "schema": {
              "type": "string",
              "enum": [
                "UPDATE",
                "UPDATE_ONLY",
                "REPLACE",
                "REPLACE_ONLY",
                "CREATE_ONLY"
              ]
            }
          },
          {
            "name": "sendKey",
            "in": "query",
            "description": "Send user defined key in addition to hash digest on both reads and writes.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "replica",
            "in": "query",
            "description": "Replica algorithm used to determine the target node for a single record command.",
            "schema": {
              "type": "string",
              "enum": [
                "MASTER",
                "MASTER_PROLES",
                "SEQUENCE",
                "PREFER_RACK",
                "RANDOM"
              ]
            }
          },
          {
            "name": "readModeSC",
            "in": "query",
            "description": "Read policy for SC (strong consistency) namespaces. Determines SC read consistency options.",
            "schema": {
              "type": "string",
              "enum": [
                "SESSION",
                "LINEARIZE",
                "ALLOW_REPLICA",
                "ALLOW_UNAVAILABLE"
              ]
            }
          },
          {
            "name": "readModeAP",
            "in": "query",
            "description": "Read policy for AP (availability) namespaces. How duplicates should be consulted in a read operation. Only makes a difference during migrations and only applicable in AP mode.",
            "schema": {
              "type": "string",
              "enum": [
                "ONE",
                "ALL"
              ]
            }
          },
          {
            "name": "totalTimeout",
            "in": "query",
            "description": "Total transaction timeout in milliseconds.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "socketTimeout",
            "in": "query",
            "description": "Socket idle timeout in milliseconds when processing a database command.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "sleepBetweenRetries",
            "in": "query",
            "description": "Milliseconds to sleep between retries.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "maxRetries",
            "in": "query",
            "description": "Maximum number of retries before aborting the current transaction.\nThe initial attempt is not counted as a retry.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "filterexp",
            "in": "query",
            "description": "Optional Filter Expression (introduced in Aerospike Database 5.2.0) in infix notation DSL.",
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "compress",
            "in": "query",
            "description": "Use zlib compression on command buffers sent to the server and responses received from the server when the buffer size is greater than 128 bytes.",
            "schema": {
              "type": "boolean"
            }
          }
        ],
        "requestBody": {
          "description": "Bins to be stored in the record. This is a mapping from a string bin name to a value. Value can be a String, integer, floating point number, list, map, bytearray, or GeoJSON value. Bytearrays and GeoJSON can only be sent using MessagePack\n example: {\"bin1\":5, \"bin2\":\"hello\", \"bin3\": [1,2,3], \"bin4\": {\"one\": 1}}",
          "content": {
            "application/json": {
              "schema": {
                "type": "object",
                "additionalProperties": {
                  "type": "object"
                }
              },
              "examples": {
                "Bins request body example": {
                  "description": "Bins request body example",
                  "value": {
                    "bin1": 5,
                    "bin2": "hello",
                    "bin3": [
                      1,
                      2,
                      3
                    ],
                    "bin4": {
                      "one": 1
                    }
                  }
                }
              }
            }
          },
          "required": true
        },
        "responses": {
          "400": {
            "description": "Invalid parameters or request",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "404": {
            "description": "Record does not exists.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "403": {
            "description": "Not authorized to access the resource",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "409": {
            "description": "Generation mismatch for operation.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "204": {
            "description": "Modified record successfully, no content expected."
          }
        }
      }
    },
    "/v1/kvs/{namespace}/{key}": {
      "get": {
        "tags": [
          "Key Value Operations"
        ],
        "summary": "Return the metadata and bins for a record.",
        "operationId": "getRecordNamespaceKey",
        "parameters": [
          {
            "name": "namespace",
            "in": "path",
            "description": "Namespace for the record; equivalent to database name.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "key",
            "in": "path",
            "description": "Userkey for the record.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "keytype",
            "in": "query",
            "description": "The Type of the userKey.",
            "schema": {
              "type": "string",
              "enum": [
                "STRING",
                "INTEGER",
                "BYTES",
                "DIGEST"
              ]
            }
          },
          {
            "name": "recordBins",
            "in": "query",
            "description": "Optionally specify a set of bins to return when fetching a record. If omitted, all bins will be returned.",
            "schema": {
              "type": "array",
              "items": {
                "type": "string"
              }
            }
          },
          {
            "name": "sendKey",
            "in": "query",
            "description": "Send user defined key in addition to hash digest on both reads and writes.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "replica",
            "in": "query",
            "description": "Replica algorithm used to determine the target node for a single record command.",
            "schema": {
              "type": "string",
              "enum": [
                "MASTER",
                "MASTER_PROLES",
                "SEQUENCE",
                "PREFER_RACK",
                "RANDOM"
              ]
            }
          },
          {
            "name": "readModeSC",
            "in": "query",
            "description": "Read policy for SC (strong consistency) namespaces. Determines SC read consistency options.",
            "schema": {
              "type": "string",
              "enum": [
                "SESSION",
                "LINEARIZE",
                "ALLOW_REPLICA",
                "ALLOW_UNAVAILABLE"
              ]
            }
          },
          {
            "name": "readModeAP",
            "in": "query",
            "description": "Read policy for AP (availability) namespaces. How duplicates should be consulted in a read operation. Only makes a difference during migrations and only applicable in AP mode.",
            "schema": {
              "type": "string",
              "enum": [
                "ONE",
                "ALL"
              ]
            }
          },
          {
            "name": "totalTimeout",
            "in": "query",
            "description": "Total transaction timeout in milliseconds.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "socketTimeout",
            "in": "query",
            "description": "Socket idle timeout in milliseconds when processing a database command.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "sleepBetweenRetries",
            "in": "query",
            "description": "Milliseconds to sleep between retries.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "maxRetries",
            "in": "query",
            "description": "Maximum number of retries before aborting the current transaction.\nThe initial attempt is not counted as a retry.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "filterexp",
            "in": "query",
            "description": "Optional Filter Expression (introduced in Aerospike Database 5.2.0) in infix notation DSL.",
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "compress",
            "in": "query",
            "description": "Use zlib compression on command buffers sent to the server and responses received from the server when the buffer size is greater than 128 bytes.",
            "schema": {
              "type": "boolean"
            }
          }
        ],
        "responses": {
          "400": {
            "description": "Invalid parameters or request",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "200": {
            "description": "Metadata and bins for a record returned successfully.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientRecord"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientRecord"
                }
              }
            }
          },
          "403": {
            "description": "Not authorized to access the resource",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "404": {
            "description": "Record not found.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          }
        }
      },
      "put": {
        "tags": [
          "Key Value Operations"
        ],
        "summary": "Replace the bins of the specified record.",
        "operationId": "replaceRecordNamespaceKey",
        "parameters": [
          {
            "name": "namespace",
            "in": "path",
            "description": "Namespace for the record; equivalent to database name.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "key",
            "in": "path",
            "description": "Userkey for the record.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "keytype",
            "in": "query",
            "description": "The Type of the userKey.",
            "schema": {
              "type": "string",
              "enum": [
                "STRING",
                "INTEGER",
                "BYTES",
                "DIGEST"
              ]
            }
          },
          {
            "name": "expiration",
            "in": "query",
            "description": "Record expiration. Also known as ttl (time to live). Seconds record will live before being removed by the server.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "generation",
            "in": "query",
            "description": "Expected generation. Generation is the number of times a record has been modified (including creation) on the server.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "durableDelete",
            "in": "query",
            "description": "If the transaction results in a record deletion, leave a tombstone for the record.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "respondAllOps",
            "in": "query",
            "description": "For client operate(), return a result for every operation.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "commitLevel",
            "in": "query",
            "description": "Desired consistency guarantee when committing a transaction on the server.",
            "schema": {
              "type": "string",
              "enum": [
                "COMMIT_ALL",
                "COMMIT_MASTER"
              ]
            }
          },
          {
            "name": "generationPolicy",
            "in": "query",
            "description": "Qualify how to handle record writes based on record generation.",
            "schema": {
              "type": "string",
              "enum": [
                "NONE",
                "EXPECT_GEN_EQUAL",
                "EXPECT_GEN_GT"
              ]
            }
          },
          {
            "name": "recordExistsAction",
            "in": "query",
            "description": "How to handle the existence of the record. This is ignored for POST/PUT/UPDATE kvs methods.",
            "schema": {
              "type": "string",
              "enum": [
                "UPDATE",
                "UPDATE_ONLY",
                "REPLACE",
                "REPLACE_ONLY",
                "CREATE_ONLY"
              ]
            }
          },
          {
            "name": "sendKey",
            "in": "query",
            "description": "Send user defined key in addition to hash digest on both reads and writes.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "replica",
            "in": "query",
            "description": "Replica algorithm used to determine the target node for a single record command.",
            "schema": {
              "type": "string",
              "enum": [
                "MASTER",
                "MASTER_PROLES",
                "SEQUENCE",
                "PREFER_RACK",
                "RANDOM"
              ]
            }
          },
          {
            "name": "readModeSC",
            "in": "query",
            "description": "Read policy for SC (strong consistency) namespaces. Determines SC read consistency options.",
            "schema": {
              "type": "string",
              "enum": [
                "SESSION",
                "LINEARIZE",
                "ALLOW_REPLICA",
                "ALLOW_UNAVAILABLE"
              ]
            }
          },
          {
            "name": "readModeAP",
            "in": "query",
            "description": "Read policy for AP (availability) namespaces. How duplicates should be consulted in a read operation. Only makes a difference during migrations and only applicable in AP mode.",
            "schema": {
              "type": "string",
              "enum": [
                "ONE",
                "ALL"
              ]
            }
          },
          {
            "name": "totalTimeout",
            "in": "query",
            "description": "Total transaction timeout in milliseconds.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "socketTimeout",
            "in": "query",
            "description": "Socket idle timeout in milliseconds when processing a database command.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "sleepBetweenRetries",
            "in": "query",
            "description": "Milliseconds to sleep between retries.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "maxRetries",
            "in": "query",
            "description": "Maximum number of retries before aborting the current transaction.\nThe initial attempt is not counted as a retry.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "filterexp",
            "in": "query",
            "description": "Optional Filter Expression (introduced in Aerospike Database 5.2.0) in infix notation DSL.",
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "compress",
            "in": "query",
            "description": "Use zlib compression on command buffers sent to the server and responses received from the server when the buffer size is greater than 128 bytes.",
            "schema": {
              "type": "boolean"
            }
          }
        ],
        "requestBody": {
          "description": "Bins to be stored in the record. This is a mapping from a string bin name to a value. Value can be a String, integer, floating point number, list, map, bytearray, or GeoJSON value. Bytearrays and GeoJSON can only be sent using MessagePack\n example: {\"bin1\":5, \"bin2\":\"hello\", \"bin3\": [1,2,3], \"bin4\": {\"one\": 1}}",
          "content": {
            "application/json": {
              "schema": {
                "type": "object",
                "additionalProperties": {
                  "type": "object"
                }
              },
              "examples": {
                "Bins request body example": {
                  "description": "Bins request body example",
                  "value": {
                    "bin1": 5,
                    "bin2": "hello",
                    "bin3": [
                      1,
                      2,
                      3
                    ],
                    "bin4": {
                      "one": 1
                    }
                  }
                }
              }
            }
          },
          "required": true
        },
        "responses": {
          "400": {
            "description": "Invalid parameters or request",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "403": {
            "description": "Not authorized to access the resource",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "409": {
            "description": "Generation mismatch for operation.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "204": {
            "description": "Modified record successfully, no content expected."
          },
          "404": {
            "description": "Record not found.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          }
        }
      },
      "post": {
        "tags": [
          "Key Value Operations"
        ],
        "summary": "Create a new record with the provided bins into the record.",
        "operationId": "createRecordNamespaceKey",
        "parameters": [
          {
            "name": "namespace",
            "in": "path",
            "description": "Namespace for the record; equivalent to database name.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "key",
            "in": "path",
            "description": "Userkey for the record.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "keytype",
            "in": "query",
            "description": "The Type of the userKey.",
            "schema": {
              "type": "string",
              "enum": [
                "STRING",
                "INTEGER",
                "BYTES",
                "DIGEST"
              ]
            }
          },
          {
            "name": "expiration",
            "in": "query",
            "description": "Record expiration. Also known as ttl (time to live). Seconds record will live before being removed by the server.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "generation",
            "in": "query",
            "description": "Expected generation. Generation is the number of times a record has been modified (including creation) on the server.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "durableDelete",
            "in": "query",
            "description": "If the transaction results in a record deletion, leave a tombstone for the record.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "respondAllOps",
            "in": "query",
            "description": "For client operate(), return a result for every operation.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "commitLevel",
            "in": "query",
            "description": "Desired consistency guarantee when committing a transaction on the server.",
            "schema": {
              "type": "string",
              "enum": [
                "COMMIT_ALL",
                "COMMIT_MASTER"
              ]
            }
          },
          {
            "name": "generationPolicy",
            "in": "query",
            "description": "Qualify how to handle record writes based on record generation.",
            "schema": {
              "type": "string",
              "enum": [
                "NONE",
                "EXPECT_GEN_EQUAL",
                "EXPECT_GEN_GT"
              ]
            }
          },
          {
            "name": "recordExistsAction",
            "in": "query",
            "description": "How to handle the existence of the record. This is ignored for POST/PUT/UPDATE kvs methods.",
            "schema": {
              "type": "string",
              "enum": [
                "UPDATE",
                "UPDATE_ONLY",
                "REPLACE",
                "REPLACE_ONLY",
                "CREATE_ONLY"
              ]
            }
          },
          {
            "name": "sendKey",
            "in": "query",
            "description": "Send user defined key in addition to hash digest on both reads and writes.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "replica",
            "in": "query",
            "description": "Replica algorithm used to determine the target node for a single record command.",
            "schema": {
              "type": "string",
              "enum": [
                "MASTER",
                "MASTER_PROLES",
                "SEQUENCE",
                "PREFER_RACK",
                "RANDOM"
              ]
            }
          },
          {
            "name": "readModeSC",
            "in": "query",
            "description": "Read policy for SC (strong consistency) namespaces. Determines SC read consistency options.",
            "schema": {
              "type": "string",
              "enum": [
                "SESSION",
                "LINEARIZE",
                "ALLOW_REPLICA",
                "ALLOW_UNAVAILABLE"
              ]
            }
          },
          {
            "name": "readModeAP",
            "in": "query",
            "description": "Read policy for AP (availability) namespaces. How duplicates should be consulted in a read operation. Only makes a difference during migrations and only applicable in AP mode.",
            "schema": {
              "type": "string",
              "enum": [
                "ONE",
                "ALL"
              ]
            }
          },
          {
            "name": "totalTimeout",
            "in": "query",
            "description": "Total transaction timeout in milliseconds.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "socketTimeout",
            "in": "query",
            "description": "Socket idle timeout in milliseconds when processing a database command.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "sleepBetweenRetries",
            "in": "query",
            "description": "Milliseconds to sleep between retries.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "maxRetries",
            "in": "query",
            "description": "Maximum number of retries before aborting the current transaction.\nThe initial attempt is not counted as a retry.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "filterexp",
            "in": "query",
            "description": "Optional Filter Expression (introduced in Aerospike Database 5.2.0) in infix notation DSL.",
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "compress",
            "in": "query",
            "description": "Use zlib compression on command buffers sent to the server and responses received from the server when the buffer size is greater than 128 bytes.",
            "schema": {
              "type": "boolean"
            }
          }
        ],
        "requestBody": {
          "description": "Bins to be stored in the record. This is a mapping from a string bin name to a value. Value can be a String, integer, floating point number, list, map, bytearray, or GeoJSON value. Bytearrays and GeoJSON can only be sent using MessagePack\n example: {\"bin1\":5, \"bin2\":\"hello\", \"bin3\": [1,2,3], \"bin4\": {\"one\": 1}}",
          "content": {
            "application/json": {
              "schema": {
                "type": "object",
                "additionalProperties": {
                  "type": "object"
                }
              },
              "examples": {
                "Bins request body example": {
                  "description": "Bins request body example",
                  "value": {
                    "bin1": 5,
                    "bin2": "hello",
                    "bin3": [
                      1,
                      2,
                      3
                    ],
                    "bin4": {
                      "one": 1
                    }
                  }
                }
              }
            }
          },
          "required": true
        },
        "responses": {
          "400": {
            "description": "Invalid parameters or request",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "404": {
            "description": "Namespace does not exist.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "201": {
            "description": "Created a new record successfully."
          },
          "403": {
            "description": "Not authorized to access the resource",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "409": {
            "description": "Record Already exists.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          }
        }
      },
      "delete": {
        "tags": [
          "Key Value Operations"
        ],
        "summary": "Delete the specified record.",
        "operationId": "deleteRecordNamespaceKey",
        "parameters": [
          {
            "name": "namespace",
            "in": "path",
            "description": "Namespace for the record; equivalent to database name.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "key",
            "in": "path",
            "description": "Userkey for the record.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "keytype",
            "in": "query",
            "description": "The Type of the userKey.",
            "schema": {
              "type": "string",
              "enum": [
                "STRING",
                "INTEGER",
                "BYTES",
                "DIGEST"
              ]
            }
          },
          {
            "name": "expiration",
            "in": "query",
            "description": "Record expiration. Also known as ttl (time to live). Seconds record will live before being removed by the server.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "generation",
            "in": "query",
            "description": "Expected generation. Generation is the number of times a record has been modified (including creation) on the server.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "durableDelete",
            "in": "query",
            "description": "If the transaction results in a record deletion, leave a tombstone for the record.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "respondAllOps",
            "in": "query",
            "description": "For client operate(), return a result for every operation.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "commitLevel",
            "in": "query",
            "description": "Desired consistency guarantee when committing a transaction on the server.",
            "schema": {
              "type": "string",
              "enum": [
                "COMMIT_ALL",
                "COMMIT_MASTER"
              ]
            }
          },
          {
            "name": "generationPolicy",
            "in": "query",
            "description": "Qualify how to handle record writes based on record generation.",
            "schema": {
              "type": "string",
              "enum": [
                "NONE",
                "EXPECT_GEN_EQUAL",
                "EXPECT_GEN_GT"
              ]
            }
          },
          {
            "name": "recordExistsAction",
            "in": "query",
            "description": "How to handle the existence of the record. This is ignored for POST/PUT/UPDATE kvs methods.",
            "schema": {
              "type": "string",
              "enum": [
                "UPDATE",
                "UPDATE_ONLY",
                "REPLACE",
                "REPLACE_ONLY",
                "CREATE_ONLY"
              ]
            }
          },
          {
            "name": "sendKey",
            "in": "query",
            "description": "Send user defined key in addition to hash digest on both reads and writes.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "replica",
            "in": "query",
            "description": "Replica algorithm used to determine the target node for a single record command.",
            "schema": {
              "type": "string",
              "enum": [
                "MASTER",
                "MASTER_PROLES",
                "SEQUENCE",
                "PREFER_RACK",
                "RANDOM"
              ]
            }
          },
          {
            "name": "readModeSC",
            "in": "query",
            "description": "Read policy for SC (strong consistency) namespaces. Determines SC read consistency options.",
            "schema": {
              "type": "string",
              "enum": [
                "SESSION",
                "LINEARIZE",
                "ALLOW_REPLICA",
                "ALLOW_UNAVAILABLE"
              ]
            }
          },
          {
            "name": "readModeAP",
            "in": "query",
            "description": "Read policy for AP (availability) namespaces. How duplicates should be consulted in a read operation. Only makes a difference during migrations and only applicable in AP mode.",
            "schema": {
              "type": "string",
              "enum": [
                "ONE",
                "ALL"
              ]
            }
          },
          {
            "name": "totalTimeout",
            "in": "query",
            "description": "Total transaction timeout in milliseconds.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "socketTimeout",
            "in": "query",
            "description": "Socket idle timeout in milliseconds when processing a database command.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "sleepBetweenRetries",
            "in": "query",
            "description": "Milliseconds to sleep between retries.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "maxRetries",
            "in": "query",
            "description": "Maximum number of retries before aborting the current transaction.\nThe initial attempt is not counted as a retry.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "filterexp",
            "in": "query",
            "description": "Optional Filter Expression (introduced in Aerospike Database 5.2.0) in infix notation DSL.",
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "compress",
            "in": "query",
            "description": "Use zlib compression on command buffers sent to the server and responses received from the server when the buffer size is greater than 128 bytes.",
            "schema": {
              "type": "boolean"
            }
          }
        ],
        "responses": {
          "400": {
            "description": "Invalid parameters or request",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "204": {
            "description": "Deleted a record successfully, no content expected."
          },
          "403": {
            "description": "Not authorized to access the resource",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "409": {
            "description": "Generation mismatch for operation.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "404": {
            "description": "Record not found.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          }
        }
      },
      "head": {
        "tags": [
          "Key Value Operations"
        ],
        "summary": "Check if a record exists",
        "operationId": "recordExistsNamespaceKey",
        "parameters": [
          {
            "name": "namespace",
            "in": "path",
            "description": "Namespace for the record; equivalent to database name.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "key",
            "in": "path",
            "description": "Userkey for the record.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "keytype",
            "in": "query",
            "description": "The Type of the userKey.",
            "required": false,
            "schema": {
              "type": "string",
              "enum": [
                "STRING",
                "INTEGER",
                "BYTES",
                "DIGEST"
              ]
            }
          },
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "Record exists indication returned successfully."
          },
          "404": {
            "description": "Record does not exist.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          }
        }
      },
      "patch": {
        "tags": [
          "Key Value Operations"
        ],
        "summary": "Merge the provided bins into the record.",
        "operationId": "updateRecordNamespaceKey",
        "parameters": [
          {
            "name": "namespace",
            "in": "path",
            "description": "Namespace for the record; equivalent to database name.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "key",
            "in": "path",
            "description": "Userkey for the record.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "keytype",
            "in": "query",
            "description": "The Type of the userKey.",
            "schema": {
              "type": "string",
              "enum": [
                "STRING",
                "INTEGER",
                "BYTES",
                "DIGEST"
              ]
            }
          },
          {
            "name": "expiration",
            "in": "query",
            "description": "Record expiration. Also known as ttl (time to live). Seconds record will live before being removed by the server.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "generation",
            "in": "query",
            "description": "Expected generation. Generation is the number of times a record has been modified (including creation) on the server.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "durableDelete",
            "in": "query",
            "description": "If the transaction results in a record deletion, leave a tombstone for the record.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "respondAllOps",
            "in": "query",
            "description": "For client operate(), return a result for every operation.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "commitLevel",
            "in": "query",
            "description": "Desired consistency guarantee when committing a transaction on the server.",
            "schema": {
              "type": "string",
              "enum": [
                "COMMIT_ALL",
                "COMMIT_MASTER"
              ]
            }
          },
          {
            "name": "generationPolicy",
            "in": "query",
            "description": "Qualify how to handle record writes based on record generation.",
            "schema": {
              "type": "string",
              "enum": [
                "NONE",
                "EXPECT_GEN_EQUAL",
                "EXPECT_GEN_GT"
              ]
            }
          },
          {
            "name": "recordExistsAction",
            "in": "query",
            "description": "How to handle the existence of the record. This is ignored for POST/PUT/UPDATE kvs methods.",
            "schema": {
              "type": "string",
              "enum": [
                "UPDATE",
                "UPDATE_ONLY",
                "REPLACE",
                "REPLACE_ONLY",
                "CREATE_ONLY"
              ]
            }
          },
          {
            "name": "sendKey",
            "in": "query",
            "description": "Send user defined key in addition to hash digest on both reads and writes.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "replica",
            "in": "query",
            "description": "Replica algorithm used to determine the target node for a single record command.",
            "schema": {
              "type": "string",
              "enum": [
                "MASTER",
                "MASTER_PROLES",
                "SEQUENCE",
                "PREFER_RACK",
                "RANDOM"
              ]
            }
          },
          {
            "name": "readModeSC",
            "in": "query",
            "description": "Read policy for SC (strong consistency) namespaces. Determines SC read consistency options.",
            "schema": {
              "type": "string",
              "enum": [
                "SESSION",
                "LINEARIZE",
                "ALLOW_REPLICA",
                "ALLOW_UNAVAILABLE"
              ]
            }
          },
          {
            "name": "readModeAP",
            "in": "query",
            "description": "Read policy for AP (availability) namespaces. How duplicates should be consulted in a read operation. Only makes a difference during migrations and only applicable in AP mode.",
            "schema": {
              "type": "string",
              "enum": [
                "ONE",
                "ALL"
              ]
            }
          },
          {
            "name": "totalTimeout",
            "in": "query",
            "description": "Total transaction timeout in milliseconds.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "socketTimeout",
            "in": "query",
            "description": "Socket idle timeout in milliseconds when processing a database command.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "sleepBetweenRetries",
            "in": "query",
            "description": "Milliseconds to sleep between retries.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "maxRetries",
            "in": "query",
            "description": "Maximum number of retries before aborting the current transaction.\nThe initial attempt is not counted as a retry.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "filterexp",
            "in": "query",
            "description": "Optional Filter Expression (introduced in Aerospike Database 5.2.0) in infix notation DSL.",
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "compress",
            "in": "query",
            "description": "Use zlib compression on command buffers sent to the server and responses received from the server when the buffer size is greater than 128 bytes.",
            "schema": {
              "type": "boolean"
            }
          }
        ],
        "requestBody": {
          "description": "Bins to be stored in the record. This is a mapping from a string bin name to a value. Value can be a String, integer, floating point number, list, map, bytearray, or GeoJSON value. Bytearrays and GeoJSON can only be sent using MessagePack\n example: {\"bin1\":5, \"bin2\":\"hello\", \"bin3\": [1,2,3], \"bin4\": {\"one\": 1}}",
          "content": {
            "application/json": {
              "schema": {
                "type": "object",
                "additionalProperties": {
                  "type": "object"
                }
              },
              "examples": {
                "Bins request body example": {
                  "description": "Bins request body example",
                  "value": {
                    "bin1": 5,
                    "bin2": "hello",
                    "bin3": [
                      1,
                      2,
                      3
                    ],
                    "bin4": {
                      "one": 1
                    }
                  }
                }
              }
            }
          },
          "required": true
        },
        "responses": {
          "400": {
            "description": "Invalid parameters or request",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "404": {
            "description": "Record does not exists.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "403": {
            "description": "Not authorized to access the resource",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "409": {
            "description": "Generation mismatch for operation.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "204": {
            "description": "Modified record successfully, no content expected."
          }
        }
      }
    },
    "/v1/document/{namespace}/{set}/{key}": {
      "get": {
        "tags": [
          "Document API Operations"
        ],
        "summary": "Retrieve the object in the document with key documentKey that is referenced by the JSON path.",
        "operationId": "getDocumentObjectSet",
        "parameters": [
          {
            "name": "namespace",
            "in": "path",
            "description": "Namespace for the record; equivalent to database name.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "set",
            "in": "path",
            "description": "Set for the record; equivalent to database table.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "key",
            "in": "path",
            "description": "User key for the record.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "keytype",
            "in": "query",
            "description": "The Type of the userKey.",
            "schema": {
              "type": "string",
              "enum": [
                "STRING",
                "INTEGER",
                "BYTES",
                "DIGEST"
              ]
            }
          },
          {
            "name": "jsonPath",
            "in": "query",
            "description": "JSONPath query parameter.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "recordBins",
            "in": "query",
            "description": "Specify a set of bins to handle the JSONPath query.",
            "required": true,
            "schema": {
              "type": "array",
              "items": {
                "type": "string"
              }
            }
          },
          {
            "name": "sendKey",
            "in": "query",
            "description": "Send user defined key in addition to hash digest on both reads and writes.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "replica",
            "in": "query",
            "description": "Replica algorithm used to determine the target node for a single record command.",
            "schema": {
              "type": "string",
              "enum": [
                "MASTER",
                "MASTER_PROLES",
                "SEQUENCE",
                "PREFER_RACK",
                "RANDOM"
              ]
            }
          },
          {
            "name": "readModeSC",
            "in": "query",
            "description": "Read policy for SC (strong consistency) namespaces. Determines SC read consistency options.",
            "schema": {
              "type": "string",
              "enum": [
                "SESSION",
                "LINEARIZE",
                "ALLOW_REPLICA",
                "ALLOW_UNAVAILABLE"
              ]
            }
          },
          {
            "name": "readModeAP",
            "in": "query",
            "description": "Read policy for AP (availability) namespaces. How duplicates should be consulted in a read operation. Only makes a difference during migrations and only applicable in AP mode.",
            "schema": {
              "type": "string",
              "enum": [
                "ONE",
                "ALL"
              ]
            }
          },
          {
            "name": "totalTimeout",
            "in": "query",
            "description": "Total transaction timeout in milliseconds.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "socketTimeout",
            "in": "query",
            "description": "Socket idle timeout in milliseconds when processing a database command.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "sleepBetweenRetries",
            "in": "query",
            "description": "Milliseconds to sleep between retries.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "maxRetries",
            "in": "query",
            "description": "Maximum number of retries before aborting the current transaction.\nThe initial attempt is not counted as a retry.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "filterexp",
            "in": "query",
            "description": "Optional Filter Expression (introduced in Aerospike Database 5.2.0) in infix notation DSL.",
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "compress",
            "in": "query",
            "description": "Use zlib compression on command buffers sent to the server and responses received from the server when the buffer size is greater than 128 bytes.",
            "schema": {
              "type": "boolean"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "Document read successfully.",
            "content": {
              "application/json": {
                "examples": {
                  "Get document object response example": {
                    "description": "Get document object response example",
                    "value": {
                      "docBin2": [
                        "A1",
                        "B1",
                        "C1",
                        "D1"
                      ],
                      "docBin1": [
                        "A1",
                        "B1",
                        "C1",
                        "D1"
                      ]
                    }
                  }
                }
              },
              "application/msgpack": {
                "examples": {
                  "Get document object response example": {
                    "description": "Get document object response example",
                    "value": {
                      "docBin2": [
                        "A1",
                        "B1",
                        "C1",
                        "D1"
                      ],
                      "docBin1": [
                        "A1",
                        "B1",
                        "C1",
                        "D1"
                      ]
                    }
                  }
                }
              }
            }
          },
          "403": {
            "description": "Not authorized to access the resource.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "400": {
            "description": "Invalid parameters or request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "404": {
            "description": "Record not found.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          }
        }
      },
      "put": {
        "tags": [
          "Document API Operations"
        ],
        "summary": "Put a document.",
        "operationId": "putDocumentObjectSet",
        "parameters": [
          {
            "name": "namespace",
            "in": "path",
            "description": "Namespace for the record; equivalent to database name.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "set",
            "in": "path",
            "description": "Set for the record; equivalent to database table.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "key",
            "in": "path",
            "description": "User key for the record.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "keytype",
            "in": "query",
            "description": "The Type of the userKey.",
            "schema": {
              "type": "string",
              "enum": [
                "STRING",
                "INTEGER",
                "BYTES",
                "DIGEST"
              ]
            }
          },
          {
            "name": "jsonPath",
            "in": "query",
            "description": "JSONPath query parameter.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "recordBins",
            "in": "query",
            "description": "Specify a set of bins to handle the JSONPath query.",
            "required": true,
            "schema": {
              "type": "array",
              "items": {
                "type": "string"
              }
            }
          },
          {
            "name": "expiration",
            "in": "query",
            "description": "Record expiration. Also known as ttl (time to live). Seconds record will live before being removed by the server.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "generation",
            "in": "query",
            "description": "Expected generation. Generation is the number of times a record has been modified (including creation) on the server.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "durableDelete",
            "in": "query",
            "description": "If the transaction results in a record deletion, leave a tombstone for the record.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "respondAllOps",
            "in": "query",
            "description": "For client operate(), return a result for every operation.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "commitLevel",
            "in": "query",
            "description": "Desired consistency guarantee when committing a transaction on the server.",
            "schema": {
              "type": "string",
              "enum": [
                "COMMIT_ALL",
                "COMMIT_MASTER"
              ]
            }
          },
          {
            "name": "generationPolicy",
            "in": "query",
            "description": "Qualify how to handle record writes based on record generation.",
            "schema": {
              "type": "string",
              "enum": [
                "NONE",
                "EXPECT_GEN_EQUAL",
                "EXPECT_GEN_GT"
              ]
            }
          },
          {
            "name": "recordExistsAction",
            "in": "query",
            "description": "How to handle the existence of the record. This is ignored for POST/PUT/UPDATE kvs methods.",
            "schema": {
              "type": "string",
              "enum": [
                "UPDATE",
                "UPDATE_ONLY",
                "REPLACE",
                "REPLACE_ONLY",
                "CREATE_ONLY"
              ]
            }
          },
          {
            "name": "sendKey",
            "in": "query",
            "description": "Send user defined key in addition to hash digest on both reads and writes.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "replica",
            "in": "query",
            "description": "Replica algorithm used to determine the target node for a single record command.",
            "schema": {
              "type": "string",
              "enum": [
                "MASTER",
                "MASTER_PROLES",
                "SEQUENCE",
                "PREFER_RACK",
                "RANDOM"
              ]
            }
          },
          {
            "name": "readModeSC",
            "in": "query",
            "description": "Read policy for SC (strong consistency) namespaces. Determines SC read consistency options.",
            "schema": {
              "type": "string",
              "enum": [
                "SESSION",
                "LINEARIZE",
                "ALLOW_REPLICA",
                "ALLOW_UNAVAILABLE"
              ]
            }
          },
          {
            "name": "readModeAP",
            "in": "query",
            "description": "Read policy for AP (availability) namespaces. How duplicates should be consulted in a read operation. Only makes a difference during migrations and only applicable in AP mode.",
            "schema": {
              "type": "string",
              "enum": [
                "ONE",
                "ALL"
              ]
            }
          },
          {
            "name": "totalTimeout",
            "in": "query",
            "description": "Total transaction timeout in milliseconds.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "socketTimeout",
            "in": "query",
            "description": "Socket idle timeout in milliseconds when processing a database command.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "sleepBetweenRetries",
            "in": "query",
            "description": "Milliseconds to sleep between retries.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "maxRetries",
            "in": "query",
            "description": "Maximum number of retries before aborting the current transaction.\nThe initial attempt is not counted as a retry.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "filterexp",
            "in": "query",
            "description": "Optional Filter Expression (introduced in Aerospike Database 5.2.0) in infix notation DSL.",
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "compress",
            "in": "query",
            "description": "Use zlib compression on command buffers sent to the server and responses received from the server when the buffer size is greater than 128 bytes.",
            "schema": {
              "type": "boolean"
            }
          }
        ],
        "requestBody": {
          "description": "JSON Object",
          "content": {
            "application/json": {
              "schema": {
                "type": "object"
              },
              "examples": {
                "JSON object request body example": {
                  "description": "JSON object request body example",
                  "value": "str3"
                }
              }
            }
          },
          "required": true
        },
        "responses": {
          "403": {
            "description": "Not authorized to access the resource.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "202": {
            "description": "Request to put a document has been accepted."
          },
          "400": {
            "description": "Invalid parameters or request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "404": {
            "description": "Record not found.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          }
        }
      },
      "post": {
        "tags": [
          "Document API Operations"
        ],
        "summary": "Append an object to a list in a document specified by a JSON path.",
        "operationId": "appendDocumentObjectSet",
        "parameters": [
          {
            "name": "namespace",
            "in": "path",
            "description": "Namespace for the record; equivalent to database name.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "set",
            "in": "path",
            "description": "Set for the record; equivalent to database table.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "key",
            "in": "path",
            "description": "User key for the record.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "keytype",
            "in": "query",
            "description": "The Type of the userKey.",
            "schema": {
              "type": "string",
              "enum": [
                "STRING",
                "INTEGER",
                "BYTES",
                "DIGEST"
              ]
            }
          },
          {
            "name": "jsonPath",
            "in": "query",
            "description": "JSONPath query parameter.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "recordBins",
            "in": "query",
            "description": "Specify a set of bins to handle the JSONPath query.",
            "required": true,
            "schema": {
              "type": "array",
              "items": {
                "type": "string"
              }
            }
          },
          {
            "name": "expiration",
            "in": "query",
            "description": "Record expiration. Also known as ttl (time to live). Seconds record will live before being removed by the server.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "generation",
            "in": "query",
            "description": "Expected generation. Generation is the number of times a record has been modified (including creation) on the server.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "durableDelete",
            "in": "query",
            "description": "If the transaction results in a record deletion, leave a tombstone for the record.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "respondAllOps",
            "in": "query",
            "description": "For client operate(), return a result for every operation.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "commitLevel",
            "in": "query",
            "description": "Desired consistency guarantee when committing a transaction on the server.",
            "schema": {
              "type": "string",
              "enum": [
                "COMMIT_ALL",
                "COMMIT_MASTER"
              ]
            }
          },
          {
            "name": "generationPolicy",
            "in": "query",
            "description": "Qualify how to handle record writes based on record generation.",
            "schema": {
              "type": "string",
              "enum": [
                "NONE",
                "EXPECT_GEN_EQUAL",
                "EXPECT_GEN_GT"
              ]
            }
          },
          {
            "name": "recordExistsAction",
            "in": "query",
            "description": "How to handle the existence of the record. This is ignored for POST/PUT/UPDATE kvs methods.",
            "schema": {
              "type": "string",
              "enum": [
                "UPDATE",
                "UPDATE_ONLY",
                "REPLACE",
                "REPLACE_ONLY",
                "CREATE_ONLY"
              ]
            }
          },
          {
            "name": "sendKey",
            "in": "query",
            "description": "Send user defined key in addition to hash digest on both reads and writes.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "replica",
            "in": "query",
            "description": "Replica algorithm used to determine the target node for a single record command.",
            "schema": {
              "type": "string",
              "enum": [
                "MASTER",
                "MASTER_PROLES",
                "SEQUENCE",
                "PREFER_RACK",
                "RANDOM"
              ]
            }
          },
          {
            "name": "readModeSC",
            "in": "query",
            "description": "Read policy for SC (strong consistency) namespaces. Determines SC read consistency options.",
            "schema": {
              "type": "string",
              "enum": [
                "SESSION",
                "LINEARIZE",
                "ALLOW_REPLICA",
                "ALLOW_UNAVAILABLE"
              ]
            }
          },
          {
            "name": "readModeAP",
            "in": "query",
            "description": "Read policy for AP (availability) namespaces. How duplicates should be consulted in a read operation. Only makes a difference during migrations and only applicable in AP mode.",
            "schema": {
              "type": "string",
              "enum": [
                "ONE",
                "ALL"
              ]
            }
          },
          {
            "name": "totalTimeout",
            "in": "query",
            "description": "Total transaction timeout in milliseconds.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "socketTimeout",
            "in": "query",
            "description": "Socket idle timeout in milliseconds when processing a database command.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "sleepBetweenRetries",
            "in": "query",
            "description": "Milliseconds to sleep between retries.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "maxRetries",
            "in": "query",
            "description": "Maximum number of retries before aborting the current transaction.\nThe initial attempt is not counted as a retry.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "filterexp",
            "in": "query",
            "description": "Optional Filter Expression (introduced in Aerospike Database 5.2.0) in infix notation DSL.",
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "compress",
            "in": "query",
            "description": "Use zlib compression on command buffers sent to the server and responses received from the server when the buffer size is greater than 128 bytes.",
            "schema": {
              "type": "boolean"
            }
          }
        ],
        "requestBody": {
          "description": "JSON Object",
          "content": {
            "application/json": {
              "schema": {
                "type": "object"
              },
              "examples": {
                "JSON object request body example": {
                  "description": "JSON object request body example",
                  "value": "str3"
                }
              }
            }
          },
          "required": true
        },
        "responses": {
          "403": {
            "description": "Not authorized to access the resource.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "400": {
            "description": "Invalid parameters or request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "202": {
            "description": "Request to append an object to a list in a document has been accepted."
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "404": {
            "description": "Record not found.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          }
        }
      },
      "delete": {
        "tags": [
          "Document API Operations"
        ],
        "summary": "Delete an object in a document specified by a JSON path.",
        "operationId": "deleteDocumentObjectSet",
        "parameters": [
          {
            "name": "namespace",
            "in": "path",
            "description": "Namespace for the record; equivalent to database name.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "set",
            "in": "path",
            "description": "Set for the record; equivalent to database table.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "key",
            "in": "path",
            "description": "User key for the record.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "keytype",
            "in": "query",
            "description": "The Type of the userKey.",
            "schema": {
              "type": "string",
              "enum": [
                "STRING",
                "INTEGER",
                "BYTES",
                "DIGEST"
              ]
            }
          },
          {
            "name": "jsonPath",
            "in": "query",
            "description": "JSONPath query parameter.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "recordBins",
            "in": "query",
            "description": "Specify a set of bins to handle the JSONPath query.",
            "required": true,
            "schema": {
              "type": "array",
              "items": {
                "type": "string"
              }
            }
          },
          {
            "name": "expiration",
            "in": "query",
            "description": "Record expiration. Also known as ttl (time to live). Seconds record will live before being removed by the server.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "generation",
            "in": "query",
            "description": "Expected generation. Generation is the number of times a record has been modified (including creation) on the server.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "durableDelete",
            "in": "query",
            "description": "If the transaction results in a record deletion, leave a tombstone for the record.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "respondAllOps",
            "in": "query",
            "description": "For client operate(), return a result for every operation.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "commitLevel",
            "in": "query",
            "description": "Desired consistency guarantee when committing a transaction on the server.",
            "schema": {
              "type": "string",
              "enum": [
                "COMMIT_ALL",
                "COMMIT_MASTER"
              ]
            }
          },
          {
            "name": "generationPolicy",
            "in": "query",
            "description": "Qualify how to handle record writes based on record generation.",
            "schema": {
              "type": "string",
              "enum": [
                "NONE",
                "EXPECT_GEN_EQUAL",
                "EXPECT_GEN_GT"
              ]
            }
          },
          {
            "name": "recordExistsAction",
            "in": "query",
            "description": "How to handle the existence of the record. This is ignored for POST/PUT/UPDATE kvs methods.",
            "schema": {
              "type": "string",
              "enum": [
                "UPDATE",
                "UPDATE_ONLY",
                "REPLACE",
                "REPLACE_ONLY",
                "CREATE_ONLY"
              ]
            }
          },
          {
            "name": "sendKey",
            "in": "query",
            "description": "Send user defined key in addition to hash digest on both reads and writes.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "replica",
            "in": "query",
            "description": "Replica algorithm used to determine the target node for a single record command.",
            "schema": {
              "type": "string",
              "enum": [
                "MASTER",
                "MASTER_PROLES",
                "SEQUENCE",
                "PREFER_RACK",
                "RANDOM"
              ]
            }
          },
          {
            "name": "readModeSC",
            "in": "query",
            "description": "Read policy for SC (strong consistency) namespaces. Determines SC read consistency options.",
            "schema": {
              "type": "string",
              "enum": [
                "SESSION",
                "LINEARIZE",
                "ALLOW_REPLICA",
                "ALLOW_UNAVAILABLE"
              ]
            }
          },
          {
            "name": "readModeAP",
            "in": "query",
            "description": "Read policy for AP (availability) namespaces. How duplicates should be consulted in a read operation. Only makes a difference during migrations and only applicable in AP mode.",
            "schema": {
              "type": "string",
              "enum": [
                "ONE",
                "ALL"
              ]
            }
          },
          {
            "name": "totalTimeout",
            "in": "query",
            "description": "Total transaction timeout in milliseconds.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "socketTimeout",
            "in": "query",
            "description": "Socket idle timeout in milliseconds when processing a database command.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "sleepBetweenRetries",
            "in": "query",
            "description": "Milliseconds to sleep between retries.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "maxRetries",
            "in": "query",
            "description": "Maximum number of retries before aborting the current transaction.\nThe initial attempt is not counted as a retry.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "filterexp",
            "in": "query",
            "description": "Optional Filter Expression (introduced in Aerospike Database 5.2.0) in infix notation DSL.",
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "compress",
            "in": "query",
            "description": "Use zlib compression on command buffers sent to the server and responses received from the server when the buffer size is greater than 128 bytes.",
            "schema": {
              "type": "boolean"
            }
          }
        ],
        "responses": {
          "403": {
            "description": "Not authorized to access the resource.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "400": {
            "description": "Invalid parameters or request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "204": {
            "description": "Deleted an object in a document successfully, no content expected."
          },
          "404": {
            "description": "Record not found.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          }
        }
      }
    },
    "/v1/document/{namespace}/{key}": {
      "get": {
        "tags": [
          "Document API Operations"
        ],
        "summary": "Retrieve the object in the document with key documentKey that is referenced by the JSON path.",
        "operationId": "getDocumentObject",
        "parameters": [
          {
            "name": "namespace",
            "in": "path",
            "description": "Namespace for the record; equivalent to database name.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "key",
            "in": "path",
            "description": "User key for the record.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "keytype",
            "in": "query",
            "description": "The Type of the userKey.",
            "schema": {
              "type": "string",
              "enum": [
                "STRING",
                "INTEGER",
                "BYTES",
                "DIGEST"
              ]
            }
          },
          {
            "name": "jsonPath",
            "in": "query",
            "description": "JSONPath query parameter.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "recordBins",
            "in": "query",
            "description": "Specify a set of bins to handle the JSONPath query.",
            "required": true,
            "schema": {
              "type": "array",
              "items": {
                "type": "string"
              }
            }
          },
          {
            "name": "sendKey",
            "in": "query",
            "description": "Send user defined key in addition to hash digest on both reads and writes.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "replica",
            "in": "query",
            "description": "Replica algorithm used to determine the target node for a single record command.",
            "schema": {
              "type": "string",
              "enum": [
                "MASTER",
                "MASTER_PROLES",
                "SEQUENCE",
                "PREFER_RACK",
                "RANDOM"
              ]
            }
          },
          {
            "name": "readModeSC",
            "in": "query",
            "description": "Read policy for SC (strong consistency) namespaces. Determines SC read consistency options.",
            "schema": {
              "type": "string",
              "enum": [
                "SESSION",
                "LINEARIZE",
                "ALLOW_REPLICA",
                "ALLOW_UNAVAILABLE"
              ]
            }
          },
          {
            "name": "readModeAP",
            "in": "query",
            "description": "Read policy for AP (availability) namespaces. How duplicates should be consulted in a read operation. Only makes a difference during migrations and only applicable in AP mode.",
            "schema": {
              "type": "string",
              "enum": [
                "ONE",
                "ALL"
              ]
            }
          },
          {
            "name": "totalTimeout",
            "in": "query",
            "description": "Total transaction timeout in milliseconds.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "socketTimeout",
            "in": "query",
            "description": "Socket idle timeout in milliseconds when processing a database command.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "sleepBetweenRetries",
            "in": "query",
            "description": "Milliseconds to sleep between retries.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "maxRetries",
            "in": "query",
            "description": "Maximum number of retries before aborting the current transaction.\nThe initial attempt is not counted as a retry.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "filterexp",
            "in": "query",
            "description": "Optional Filter Expression (introduced in Aerospike Database 5.2.0) in infix notation DSL.",
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "compress",
            "in": "query",
            "description": "Use zlib compression on command buffers sent to the server and responses received from the server when the buffer size is greater than 128 bytes.",
            "schema": {
              "type": "boolean"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "Document read successfully.",
            "content": {
              "application/json": {
                "examples": {
                  "Get document object response example": {
                    "description": "Get document object response example",
                    "value": {
                      "docBin2": [
                        "A1",
                        "B1",
                        "C1",
                        "D1"
                      ],
                      "docBin1": [
                        "A1",
                        "B1",
                        "C1",
                        "D1"
                      ]
                    }
                  }
                }
              },
              "application/msgpack": {
                "examples": {
                  "Get document object response example": {
                    "description": "Get document object response example",
                    "value": {
                      "docBin2": [
                        "A1",
                        "B1",
                        "C1",
                        "D1"
                      ],
                      "docBin1": [
                        "A1",
                        "B1",
                        "C1",
                        "D1"
                      ]
                    }
                  }
                }
              }
            }
          },
          "403": {
            "description": "Not authorized to access the resource.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "400": {
            "description": "Invalid parameters or request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "404": {
            "description": "Record not found.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          }
        }
      },
      "put": {
        "tags": [
          "Document API Operations"
        ],
        "summary": "Put a document.",
        "operationId": "putDocumentObject",
        "parameters": [
          {
            "name": "namespace",
            "in": "path",
            "description": "Namespace for the record; equivalent to database name.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "key",
            "in": "path",
            "description": "User key for the record.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "keytype",
            "in": "query",
            "description": "The Type of the userKey.",
            "schema": {
              "type": "string",
              "enum": [
                "STRING",
                "INTEGER",
                "BYTES",
                "DIGEST"
              ]
            }
          },
          {
            "name": "jsonPath",
            "in": "query",
            "description": "JSONPath query parameter.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "recordBins",
            "in": "query",
            "description": "Specify a set of bins to handle the JSONPath query.",
            "required": true,
            "schema": {
              "type": "array",
              "items": {
                "type": "string"
              }
            }
          },
          {
            "name": "expiration",
            "in": "query",
            "description": "Record expiration. Also known as ttl (time to live). Seconds record will live before being removed by the server.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "generation",
            "in": "query",
            "description": "Expected generation. Generation is the number of times a record has been modified (including creation) on the server.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "durableDelete",
            "in": "query",
            "description": "If the transaction results in a record deletion, leave a tombstone for the record.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "respondAllOps",
            "in": "query",
            "description": "For client operate(), return a result for every operation.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "commitLevel",
            "in": "query",
            "description": "Desired consistency guarantee when committing a transaction on the server.",
            "schema": {
              "type": "string",
              "enum": [
                "COMMIT_ALL",
                "COMMIT_MASTER"
              ]
            }
          },
          {
            "name": "generationPolicy",
            "in": "query",
            "description": "Qualify how to handle record writes based on record generation.",
            "schema": {
              "type": "string",
              "enum": [
                "NONE",
                "EXPECT_GEN_EQUAL",
                "EXPECT_GEN_GT"
              ]
            }
          },
          {
            "name": "recordExistsAction",
            "in": "query",
            "description": "How to handle the existence of the record. This is ignored for POST/PUT/UPDATE kvs methods.",
            "schema": {
              "type": "string",
              "enum": [
                "UPDATE",
                "UPDATE_ONLY",
                "REPLACE",
                "REPLACE_ONLY",
                "CREATE_ONLY"
              ]
            }
          },
          {
            "name": "sendKey",
            "in": "query",
            "description": "Send user defined key in addition to hash digest on both reads and writes.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "replica",
            "in": "query",
            "description": "Replica algorithm used to determine the target node for a single record command.",
            "schema": {
              "type": "string",
              "enum": [
                "MASTER",
                "MASTER_PROLES",
                "SEQUENCE",
                "PREFER_RACK",
                "RANDOM"
              ]
            }
          },
          {
            "name": "readModeSC",
            "in": "query",
            "description": "Read policy for SC (strong consistency) namespaces. Determines SC read consistency options.",
            "schema": {
              "type": "string",
              "enum": [
                "SESSION",
                "LINEARIZE",
                "ALLOW_REPLICA",
                "ALLOW_UNAVAILABLE"
              ]
            }
          },
          {
            "name": "readModeAP",
            "in": "query",
            "description": "Read policy for AP (availability) namespaces. How duplicates should be consulted in a read operation. Only makes a difference during migrations and only applicable in AP mode.",
            "schema": {
              "type": "string",
              "enum": [
                "ONE",
                "ALL"
              ]
            }
          },
          {
            "name": "totalTimeout",
            "in": "query",
            "description": "Total transaction timeout in milliseconds.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "socketTimeout",
            "in": "query",
            "description": "Socket idle timeout in milliseconds when processing a database command.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "sleepBetweenRetries",
            "in": "query",
            "description": "Milliseconds to sleep between retries.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "maxRetries",
            "in": "query",
            "description": "Maximum number of retries before aborting the current transaction.\nThe initial attempt is not counted as a retry.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "filterexp",
            "in": "query",
            "description": "Optional Filter Expression (introduced in Aerospike Database 5.2.0) in infix notation DSL.",
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "compress",
            "in": "query",
            "description": "Use zlib compression on command buffers sent to the server and responses received from the server when the buffer size is greater than 128 bytes.",
            "schema": {
              "type": "boolean"
            }
          }
        ],
        "requestBody": {
          "description": "JSON Object",
          "content": {
            "application/json": {
              "schema": {
                "type": "object"
              },
              "examples": {
                "JSON object request body example": {
                  "description": "JSON object request body example",
                  "value": "str3"
                }
              }
            }
          },
          "required": true
        },
        "responses": {
          "403": {
            "description": "Not authorized to access the resource.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "202": {
            "description": "Request to put a document has been accepted."
          },
          "400": {
            "description": "Invalid parameters or request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "404": {
            "description": "Record not found.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          }
        }
      },
      "post": {
        "tags": [
          "Document API Operations"
        ],
        "summary": "Append an object to a list in a document specified by a JSON path.",
        "operationId": "appendDocumentObject",
        "parameters": [
          {
            "name": "namespace",
            "in": "path",
            "description": "Namespace for the record; equivalent to database name.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "key",
            "in": "path",
            "description": "User key for the record.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "keytype",
            "in": "query",
            "description": "The Type of the userKey.",
            "schema": {
              "type": "string",
              "enum": [
                "STRING",
                "INTEGER",
                "BYTES",
                "DIGEST"
              ]
            }
          },
          {
            "name": "jsonPath",
            "in": "query",
            "description": "JSONPath query parameter.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "recordBins",
            "in": "query",
            "description": "Specify a set of bins to handle the JSONPath query.",
            "required": true,
            "schema": {
              "type": "array",
              "items": {
                "type": "string"
              }
            }
          },
          {
            "name": "expiration",
            "in": "query",
            "description": "Record expiration. Also known as ttl (time to live). Seconds record will live before being removed by the server.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "generation",
            "in": "query",
            "description": "Expected generation. Generation is the number of times a record has been modified (including creation) on the server.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "durableDelete",
            "in": "query",
            "description": "If the transaction results in a record deletion, leave a tombstone for the record.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "respondAllOps",
            "in": "query",
            "description": "For client operate(), return a result for every operation.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "commitLevel",
            "in": "query",
            "description": "Desired consistency guarantee when committing a transaction on the server.",
            "schema": {
              "type": "string",
              "enum": [
                "COMMIT_ALL",
                "COMMIT_MASTER"
              ]
            }
          },
          {
            "name": "generationPolicy",
            "in": "query",
            "description": "Qualify how to handle record writes based on record generation.",
            "schema": {
              "type": "string",
              "enum": [
                "NONE",
                "EXPECT_GEN_EQUAL",
                "EXPECT_GEN_GT"
              ]
            }
          },
          {
            "name": "recordExistsAction",
            "in": "query",
            "description": "How to handle the existence of the record. This is ignored for POST/PUT/UPDATE kvs methods.",
            "schema": {
              "type": "string",
              "enum": [
                "UPDATE",
                "UPDATE_ONLY",
                "REPLACE",
                "REPLACE_ONLY",
                "CREATE_ONLY"
              ]
            }
          },
          {
            "name": "sendKey",
            "in": "query",
            "description": "Send user defined key in addition to hash digest on both reads and writes.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "replica",
            "in": "query",
            "description": "Replica algorithm used to determine the target node for a single record command.",
            "schema": {
              "type": "string",
              "enum": [
                "MASTER",
                "MASTER_PROLES",
                "SEQUENCE",
                "PREFER_RACK",
                "RANDOM"
              ]
            }
          },
          {
            "name": "readModeSC",
            "in": "query",
            "description": "Read policy for SC (strong consistency) namespaces. Determines SC read consistency options.",
            "schema": {
              "type": "string",
              "enum": [
                "SESSION",
                "LINEARIZE",
                "ALLOW_REPLICA",
                "ALLOW_UNAVAILABLE"
              ]
            }
          },
          {
            "name": "readModeAP",
            "in": "query",
            "description": "Read policy for AP (availability) namespaces. How duplicates should be consulted in a read operation. Only makes a difference during migrations and only applicable in AP mode.",
            "schema": {
              "type": "string",
              "enum": [
                "ONE",
                "ALL"
              ]
            }
          },
          {
            "name": "totalTimeout",
            "in": "query",
            "description": "Total transaction timeout in milliseconds.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "socketTimeout",
            "in": "query",
            "description": "Socket idle timeout in milliseconds when processing a database command.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "sleepBetweenRetries",
            "in": "query",
            "description": "Milliseconds to sleep between retries.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "maxRetries",
            "in": "query",
            "description": "Maximum number of retries before aborting the current transaction.\nThe initial attempt is not counted as a retry.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "filterexp",
            "in": "query",
            "description": "Optional Filter Expression (introduced in Aerospike Database 5.2.0) in infix notation DSL.",
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "compress",
            "in": "query",
            "description": "Use zlib compression on command buffers sent to the server and responses received from the server when the buffer size is greater than 128 bytes.",
            "schema": {
              "type": "boolean"
            }
          }
        ],
        "requestBody": {
          "description": "JSON Object",
          "content": {
            "application/json": {
              "schema": {
                "type": "object"
              },
              "examples": {
                "JSON object request body example": {
                  "description": "JSON object request body example",
                  "value": "str3"
                }
              }
            }
          },
          "required": true
        },
        "responses": {
          "403": {
            "description": "Not authorized to access the resource.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "400": {
            "description": "Invalid parameters or request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "202": {
            "description": "Request to append an object to a list in a document has been accepted."
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "404": {
            "description": "Record not found.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          }
        }
      },
      "delete": {
        "tags": [
          "Document API Operations"
        ],
        "summary": "Delete an object in a document specified by a JSON path.",
        "operationId": "deleteDocumentObject",
        "parameters": [
          {
            "name": "namespace",
            "in": "path",
            "description": "Namespace for the record; equivalent to database name.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "key",
            "in": "path",
            "description": "User key for the record.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "keytype",
            "in": "query",
            "description": "The Type of the userKey.",
            "schema": {
              "type": "string",
              "enum": [
                "STRING",
                "INTEGER",
                "BYTES",
                "DIGEST"
              ]
            }
          },
          {
            "name": "jsonPath",
            "in": "query",
            "description": "JSONPath query parameter.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "recordBins",
            "in": "query",
            "description": "Specify a set of bins to handle the JSONPath query.",
            "required": true,
            "schema": {
              "type": "array",
              "items": {
                "type": "string"
              }
            }
          },
          {
            "name": "expiration",
            "in": "query",
            "description": "Record expiration. Also known as ttl (time to live). Seconds record will live before being removed by the server.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "generation",
            "in": "query",
            "description": "Expected generation. Generation is the number of times a record has been modified (including creation) on the server.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "durableDelete",
            "in": "query",
            "description": "If the transaction results in a record deletion, leave a tombstone for the record.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "respondAllOps",
            "in": "query",
            "description": "For client operate(), return a result for every operation.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "commitLevel",
            "in": "query",
            "description": "Desired consistency guarantee when committing a transaction on the server.",
            "schema": {
              "type": "string",
              "enum": [
                "COMMIT_ALL",
                "COMMIT_MASTER"
              ]
            }
          },
          {
            "name": "generationPolicy",
            "in": "query",
            "description": "Qualify how to handle record writes based on record generation.",
            "schema": {
              "type": "string",
              "enum": [
                "NONE",
                "EXPECT_GEN_EQUAL",
                "EXPECT_GEN_GT"
              ]
            }
          },
          {
            "name": "recordExistsAction",
            "in": "query",
            "description": "How to handle the existence of the record. This is ignored for POST/PUT/UPDATE kvs methods.",
            "schema": {
              "type": "string",
              "enum": [
                "UPDATE",
                "UPDATE_ONLY",
                "REPLACE",
                "REPLACE_ONLY",
                "CREATE_ONLY"
              ]
            }
          },
          {
            "name": "sendKey",
            "in": "query",
            "description": "Send user defined key in addition to hash digest on both reads and writes.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "replica",
            "in": "query",
            "description": "Replica algorithm used to determine the target node for a single record command.",
            "schema": {
              "type": "string",
              "enum": [
                "MASTER",
                "MASTER_PROLES",
                "SEQUENCE",
                "PREFER_RACK",
                "RANDOM"
              ]
            }
          },
          {
            "name": "readModeSC",
            "in": "query",
            "description": "Read policy for SC (strong consistency) namespaces. Determines SC read consistency options.",
            "schema": {
              "type": "string",
              "enum": [
                "SESSION",
                "LINEARIZE",
                "ALLOW_REPLICA",
                "ALLOW_UNAVAILABLE"
              ]
            }
          },
          {
            "name": "readModeAP",
            "in": "query",
            "description": "Read policy for AP (availability) namespaces. How duplicates should be consulted in a read operation. Only makes a difference during migrations and only applicable in AP mode.",
            "schema": {
              "type": "string",
              "enum": [
                "ONE",
                "ALL"
              ]
            }
          },
          {
            "name": "totalTimeout",
            "in": "query",
            "description": "Total transaction timeout in milliseconds.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "socketTimeout",
            "in": "query",
            "description": "Socket idle timeout in milliseconds when processing a database command.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "sleepBetweenRetries",
            "in": "query",
            "description": "Milliseconds to sleep between retries.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "maxRetries",
            "in": "query",
            "description": "Maximum number of retries before aborting the current transaction.\nThe initial attempt is not counted as a retry.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "filterexp",
            "in": "query",
            "description": "Optional Filter Expression (introduced in Aerospike Database 5.2.0) in infix notation DSL.",
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "compress",
            "in": "query",
            "description": "Use zlib compression on command buffers sent to the server and responses received from the server when the buffer size is greater than 128 bytes.",
            "schema": {
              "type": "boolean"
            }
          }
        ],
        "responses": {
          "403": {
            "description": "Not authorized to access the resource.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "400": {
            "description": "Invalid parameters or request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "204": {
            "description": "Deleted an object in a document successfully, no content expected."
          },
          "404": {
            "description": "Record not found.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          }
        }
      }
    },
    "/v2/operate/{namespace}/{set}/{key}": {
      "post": {
        "tags": [
          "Operate operations"
        ],
        "summary": "Perform multiple operations atomically on the specified record.",
        "operationId": "operateNamespaceSetKey",
        "parameters": [
          {
            "name": "namespace",
            "in": "path",
            "description": "Namespace for the record; equivalent to database name.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "set",
            "in": "path",
            "description": "Set for the record; equivalent to database table.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "key",
            "in": "path",
            "description": "Userkey for the record.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "keytype",
            "in": "query",
            "description": "The Type of the userKey.",
            "schema": {
              "type": "string",
              "enum": [
                "STRING",
                "INTEGER",
                "BYTES",
                "DIGEST"
              ]
            }
          },
          {
            "name": "expiration",
            "in": "query",
            "description": "Record expiration. Also known as ttl (time to live). Seconds record will live before being removed by the server.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "generation",
            "in": "query",
            "description": "Expected generation. Generation is the number of times a record has been modified (including creation) on the server.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "durableDelete",
            "in": "query",
            "description": "If the transaction results in a record deletion, leave a tombstone for the record.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "respondAllOps",
            "in": "query",
            "description": "For client operate(), return a result for every operation.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "commitLevel",
            "in": "query",
            "description": "Desired consistency guarantee when committing a transaction on the server.",
            "schema": {
              "type": "string",
              "enum": [
                "COMMIT_ALL",
                "COMMIT_MASTER"
              ]
            }
          },
          {
            "name": "generationPolicy",
            "in": "query",
            "description": "Qualify how to handle record writes based on record generation.",
            "schema": {
              "type": "string",
              "enum": [
                "NONE",
                "EXPECT_GEN_EQUAL",
                "EXPECT_GEN_GT"
              ]
            }
          },
          {
            "name": "recordExistsAction",
            "in": "query",
            "description": "How to handle the existence of the record. This is ignored for POST/PUT/UPDATE kvs methods.",
            "schema": {
              "type": "string",
              "enum": [
                "UPDATE",
                "UPDATE_ONLY",
                "REPLACE",
                "REPLACE_ONLY",
                "CREATE_ONLY"
              ]
            }
          },
          {
            "name": "sendKey",
            "in": "query",
            "description": "Send user defined key in addition to hash digest on both reads and writes.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "replica",
            "in": "query",
            "description": "Replica algorithm used to determine the target node for a single record command.",
            "schema": {
              "type": "string",
              "enum": [
                "MASTER",
                "MASTER_PROLES",
                "SEQUENCE",
                "PREFER_RACK",
                "RANDOM"
              ]
            }
          },
          {
            "name": "readModeSC",
            "in": "query",
            "description": "Read policy for SC (strong consistency) namespaces. Determines SC read consistency options.",
            "schema": {
              "type": "string",
              "enum": [
                "SESSION",
                "LINEARIZE",
                "ALLOW_REPLICA",
                "ALLOW_UNAVAILABLE"
              ]
            }
          },
          {
            "name": "readModeAP",
            "in": "query",
            "description": "Read policy for AP (availability) namespaces. How duplicates should be consulted in a read operation. Only makes a difference during migrations and only applicable in AP mode.",
            "schema": {
              "type": "string",
              "enum": [
                "ONE",
                "ALL"
              ]
            }
          },
          {
            "name": "totalTimeout",
            "in": "query",
            "description": "Total transaction timeout in milliseconds.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "socketTimeout",
            "in": "query",
            "description": "Socket idle timeout in milliseconds when processing a database command.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "sleepBetweenRetries",
            "in": "query",
            "description": "Milliseconds to sleep between retries.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "maxRetries",
            "in": "query",
            "description": "Maximum number of retries before aborting the current transaction.\nThe initial attempt is not counted as a retry.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "filterexp",
            "in": "query",
            "description": "Optional Filter Expression (introduced in Aerospike Database 5.2.0) in infix notation DSL.",
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "compress",
            "in": "query",
            "description": "Use zlib compression on command buffers sent to the server and responses received from the server when the buffer size is greater than 128 bytes.",
            "schema": {
              "type": "boolean"
            }
          }
        ],
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/OperateRequestBody"
              }
            },
            "application/msgpack": {
              "schema": {
                "$ref": "#/components/schemas/OperateRequestBody"
              }
            }
          },
          "required": true
        },
        "responses": {
          "403": {
            "description": "Not authorized to access the resource.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "409": {
            "description": "Generation conflict.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "404": {
            "description": "Namespace or set does not exist.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "400": {
            "description": "Invalid parameters or request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "200": {
            "description": "Multiple operations on a record performed successfully.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/OperateResponseRecordBody"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/OperateResponseRecordBody"
                }
              }
            }
          }
        }
      }
    },
    "/v2/operate/{namespace}/{key}": {
      "post": {
        "tags": [
          "Operate operations"
        ],
        "summary": "Perform multiple operations atomically on the specified record.",
        "operationId": "operateNamespaceKey",
        "parameters": [
          {
            "name": "namespace",
            "in": "path",
            "description": "Namespace for the record; equivalent to database name.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "key",
            "in": "path",
            "description": "Userkey for the record.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "keytype",
            "in": "query",
            "description": "The Type of the userKey.",
            "schema": {
              "type": "string",
              "enum": [
                "STRING",
                "INTEGER",
                "BYTES",
                "DIGEST"
              ]
            }
          },
          {
            "name": "expiration",
            "in": "query",
            "description": "Record expiration. Also known as ttl (time to live). Seconds record will live before being removed by the server.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "generation",
            "in": "query",
            "description": "Expected generation. Generation is the number of times a record has been modified (including creation) on the server.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "durableDelete",
            "in": "query",
            "description": "If the transaction results in a record deletion, leave a tombstone for the record.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "respondAllOps",
            "in": "query",
            "description": "For client operate(), return a result for every operation.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "commitLevel",
            "in": "query",
            "description": "Desired consistency guarantee when committing a transaction on the server.",
            "schema": {
              "type": "string",
              "enum": [
                "COMMIT_ALL",
                "COMMIT_MASTER"
              ]
            }
          },
          {
            "name": "generationPolicy",
            "in": "query",
            "description": "Qualify how to handle record writes based on record generation.",
            "schema": {
              "type": "string",
              "enum": [
                "NONE",
                "EXPECT_GEN_EQUAL",
                "EXPECT_GEN_GT"
              ]
            }
          },
          {
            "name": "recordExistsAction",
            "in": "query",
            "description": "How to handle the existence of the record. This is ignored for POST/PUT/UPDATE kvs methods.",
            "schema": {
              "type": "string",
              "enum": [
                "UPDATE",
                "UPDATE_ONLY",
                "REPLACE",
                "REPLACE_ONLY",
                "CREATE_ONLY"
              ]
            }
          },
          {
            "name": "sendKey",
            "in": "query",
            "description": "Send user defined key in addition to hash digest on both reads and writes.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "replica",
            "in": "query",
            "description": "Replica algorithm used to determine the target node for a single record command.",
            "schema": {
              "type": "string",
              "enum": [
                "MASTER",
                "MASTER_PROLES",
                "SEQUENCE",
                "PREFER_RACK",
                "RANDOM"
              ]
            }
          },
          {
            "name": "readModeSC",
            "in": "query",
            "description": "Read policy for SC (strong consistency) namespaces. Determines SC read consistency options.",
            "schema": {
              "type": "string",
              "enum": [
                "SESSION",
                "LINEARIZE",
                "ALLOW_REPLICA",
                "ALLOW_UNAVAILABLE"
              ]
            }
          },
          {
            "name": "readModeAP",
            "in": "query",
            "description": "Read policy for AP (availability) namespaces. How duplicates should be consulted in a read operation. Only makes a difference during migrations and only applicable in AP mode.",
            "schema": {
              "type": "string",
              "enum": [
                "ONE",
                "ALL"
              ]
            }
          },
          {
            "name": "totalTimeout",
            "in": "query",
            "description": "Total transaction timeout in milliseconds.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "socketTimeout",
            "in": "query",
            "description": "Socket idle timeout in milliseconds when processing a database command.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "sleepBetweenRetries",
            "in": "query",
            "description": "Milliseconds to sleep between retries.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "maxRetries",
            "in": "query",
            "description": "Maximum number of retries before aborting the current transaction.\nThe initial attempt is not counted as a retry.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "filterexp",
            "in": "query",
            "description": "Optional Filter Expression (introduced in Aerospike Database 5.2.0) in infix notation DSL.",
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "compress",
            "in": "query",
            "description": "Use zlib compression on command buffers sent to the server and responses received from the server when the buffer size is greater than 128 bytes.",
            "schema": {
              "type": "boolean"
            }
          }
        ],
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/OperateRequestBody"
              }
            },
            "application/msgpack": {
              "schema": {
                "$ref": "#/components/schemas/OperateRequestBody"
              }
            }
          },
          "required": true
        },
        "responses": {
          "403": {
            "description": "Not authorized to access the resource.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "409": {
            "description": "Generation conflict.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "404": {
            "description": "Namespace or set does not exist.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "400": {
            "description": "Invalid parameters or request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "200": {
            "description": "Multiple operations on a record performed successfully.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/OperateResponseRecordBody"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/OperateResponseRecordBody"
                }
              }
            }
          }
        }
      }
    },
    "/v2/operate/read/{namespace}": {
      "post": {
        "tags": [
          "Operate operations"
        ],
        "summary": "Perform read operations on multiple records.",
        "operationId": "operateBatchNamespace",
        "parameters": [
          {
            "name": "namespace",
            "in": "path",
            "description": "Namespace for the record; equivalent to database name.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "keytype",
            "in": "query",
            "description": "The Type of the userKey.",
            "schema": {
              "type": "string",
              "enum": [
                "STRING",
                "INTEGER",
                "BYTES",
                "DIGEST"
              ]
            }
          },
          {
            "name": "allowInline",
            "in": "query",
            "description": "Allow batch to be processed immediately in the server\u0027s receiving thread when the server deems it to be appropriate.  If false, the batch will always be processed in separate transaction threads.  This field is only relevant for the new batch index protocol.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "allowInlineSSD",
            "in": "query",
            "description": "Allow batch to be processed immediately in the server\u0027s receiving thread for SSD namespaces. If false, the batch will always be processed in separate service threads. Server versions \u003c 6.0 ignore this field.\nInline processing can introduce the possibility of unfairness because the server can process the entire batch before moving onto the next command.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "maxConcurrentThreads",
            "in": "query",
            "description": "Maximum number of concurrent synchronous batch request threads to server nodes at any point in time. If there are 16 node/namespace combinations requested and maxConcurrentThreads is 8, then batch requests will be made for 8 node/namespace combinations in parallel threads. When a request completes, a new request will be issued until all 16 requests are complete.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "respondAllKeys",
            "in": "query",
            "description": "Should all batch keys be attempted regardless of errors. This field is used on both the client and server. The client handles node specific errors and the server handles key specific errors.\nIf true, every batch key is attempted regardless of previous key specific errors. Node specific errors such as timeouts stop keys to that node, but keys directed at other nodes will continue to be processed.\nIf false, the server will stop the batch to its node on most key specific errors. The exceptions are com.aerospike.client.ResultCode.KEY_NOT_FOUND_ERROR and com.aerospike.client.ResultCode.FILTERED_OUT which never stop the batch. The client will stop the entire batch on node specific errors for sync commands that are run in sequence (maxConcurrentThreads \u003d\u003d 1). The client will not stop the entire batch for async commands or sync commands run in parallel.\nServer versions \u003c 6.0 do not support this field and treat this value as false for key specific errors.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "key",
            "in": "query",
            "description": "Record keys to perform operations on.",
            "required": true,
            "schema": {
              "type": "array",
              "items": {
                "type": "string"
              }
            }
          },
          {
            "name": "sendKey",
            "in": "query",
            "description": "Send user defined key in addition to hash digest on both reads and writes.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "replica",
            "in": "query",
            "description": "Replica algorithm used to determine the target node for a single record command.",
            "schema": {
              "type": "string",
              "enum": [
                "MASTER",
                "MASTER_PROLES",
                "SEQUENCE",
                "PREFER_RACK",
                "RANDOM"
              ]
            }
          },
          {
            "name": "readModeSC",
            "in": "query",
            "description": "Read policy for SC (strong consistency) namespaces. Determines SC read consistency options.",
            "schema": {
              "type": "string",
              "enum": [
                "SESSION",
                "LINEARIZE",
                "ALLOW_REPLICA",
                "ALLOW_UNAVAILABLE"
              ]
            }
          },
          {
            "name": "readModeAP",
            "in": "query",
            "description": "Read policy for AP (availability) namespaces. How duplicates should be consulted in a read operation. Only makes a difference during migrations and only applicable in AP mode.",
            "schema": {
              "type": "string",
              "enum": [
                "ONE",
                "ALL"
              ]
            }
          },
          {
            "name": "totalTimeout",
            "in": "query",
            "description": "Total transaction timeout in milliseconds.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "socketTimeout",
            "in": "query",
            "description": "Socket idle timeout in milliseconds when processing a database command.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "sleepBetweenRetries",
            "in": "query",
            "description": "Milliseconds to sleep between retries.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "maxRetries",
            "in": "query",
            "description": "Maximum number of retries before aborting the current transaction.\nThe initial attempt is not counted as a retry.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "filterexp",
            "in": "query",
            "description": "Optional Filter Expression (introduced in Aerospike Database 5.2.0) in infix notation DSL.",
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "compress",
            "in": "query",
            "description": "Use zlib compression on command buffers sent to the server and responses received from the server when the buffer size is greater than 128 bytes.",
            "schema": {
              "type": "boolean"
            }
          }
        ],
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/OperateRequestBody"
              }
            },
            "application/msgpack": {
              "schema": {
                "$ref": "#/components/schemas/OperateRequestBody"
              }
            }
          },
          "required": true
        },
        "responses": {
          "403": {
            "description": "Not authorized to access the resource.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "409": {
            "description": "Generation conflict.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "404": {
            "description": "Namespace or set does not exist.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "200": {
            "description": "Read operations on multiple records performed successfully.",
            "content": {
              "application/json": {
                "schema": {
                  "type": "array",
                  "items": {
                    "$ref": "#/components/schemas/RestClientRecord"
                  }
                }
              },
              "application/msgpack": {
                "schema": {
                  "type": "array",
                  "items": {
                    "$ref": "#/components/schemas/RestClientRecord"
                  }
                }
              }
            }
          },
          "400": {
            "description": "Invalid parameters or request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          }
        }
      }
    },
    "/v2/operate/read/{namespace}/{set}": {
      "post": {
        "tags": [
          "Operate operations"
        ],
        "summary": "Perform read operations on multiple records.",
        "operationId": "operateBatchNamespaceSet",
        "parameters": [
          {
            "name": "namespace",
            "in": "path",
            "description": "Namespace for the record; equivalent to database name.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "set",
            "in": "path",
            "description": "Set for the record; equivalent to database table.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "keytype",
            "in": "query",
            "description": "The Type of the userKey.",
            "schema": {
              "type": "string",
              "enum": [
                "STRING",
                "INTEGER",
                "BYTES",
                "DIGEST"
              ]
            }
          },
          {
            "name": "allowInline",
            "in": "query",
            "description": "Allow batch to be processed immediately in the server\u0027s receiving thread when the server deems it to be appropriate.  If false, the batch will always be processed in separate transaction threads.  This field is only relevant for the new batch index protocol.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "allowInlineSSD",
            "in": "query",
            "description": "Allow batch to be processed immediately in the server\u0027s receiving thread for SSD namespaces. If false, the batch will always be processed in separate service threads. Server versions \u003c 6.0 ignore this field.\nInline processing can introduce the possibility of unfairness because the server can process the entire batch before moving onto the next command.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "maxConcurrentThreads",
            "in": "query",
            "description": "Maximum number of concurrent synchronous batch request threads to server nodes at any point in time. If there are 16 node/namespace combinations requested and maxConcurrentThreads is 8, then batch requests will be made for 8 node/namespace combinations in parallel threads. When a request completes, a new request will be issued until all 16 requests are complete.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "respondAllKeys",
            "in": "query",
            "description": "Should all batch keys be attempted regardless of errors. This field is used on both the client and server. The client handles node specific errors and the server handles key specific errors.\nIf true, every batch key is attempted regardless of previous key specific errors. Node specific errors such as timeouts stop keys to that node, but keys directed at other nodes will continue to be processed.\nIf false, the server will stop the batch to its node on most key specific errors. The exceptions are com.aerospike.client.ResultCode.KEY_NOT_FOUND_ERROR and com.aerospike.client.ResultCode.FILTERED_OUT which never stop the batch. The client will stop the entire batch on node specific errors for sync commands that are run in sequence (maxConcurrentThreads \u003d\u003d 1). The client will not stop the entire batch for async commands or sync commands run in parallel.\nServer versions \u003c 6.0 do not support this field and treat this value as false for key specific errors.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "key",
            "in": "query",
            "description": "Record keys to perform operations on.",
            "required": true,
            "schema": {
              "type": "array",
              "items": {
                "type": "string"
              }
            }
          },
          {
            "name": "sendKey",
            "in": "query",
            "description": "Send user defined key in addition to hash digest on both reads and writes.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "replica",
            "in": "query",
            "description": "Replica algorithm used to determine the target node for a single record command.",
            "schema": {
              "type": "string",
              "enum": [
                "MASTER",
                "MASTER_PROLES",
                "SEQUENCE",
                "PREFER_RACK",
                "RANDOM"
              ]
            }
          },
          {
            "name": "readModeSC",
            "in": "query",
            "description": "Read policy for SC (strong consistency) namespaces. Determines SC read consistency options.",
            "schema": {
              "type": "string",
              "enum": [
                "SESSION",
                "LINEARIZE",
                "ALLOW_REPLICA",
                "ALLOW_UNAVAILABLE"
              ]
            }
          },
          {
            "name": "readModeAP",
            "in": "query",
            "description": "Read policy for AP (availability) namespaces. How duplicates should be consulted in a read operation. Only makes a difference during migrations and only applicable in AP mode.",
            "schema": {
              "type": "string",
              "enum": [
                "ONE",
                "ALL"
              ]
            }
          },
          {
            "name": "totalTimeout",
            "in": "query",
            "description": "Total transaction timeout in milliseconds.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "socketTimeout",
            "in": "query",
            "description": "Socket idle timeout in milliseconds when processing a database command.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "sleepBetweenRetries",
            "in": "query",
            "description": "Milliseconds to sleep between retries.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "maxRetries",
            "in": "query",
            "description": "Maximum number of retries before aborting the current transaction.\nThe initial attempt is not counted as a retry.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "filterexp",
            "in": "query",
            "description": "Optional Filter Expression (introduced in Aerospike Database 5.2.0) in infix notation DSL.",
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "compress",
            "in": "query",
            "description": "Use zlib compression on command buffers sent to the server and responses received from the server when the buffer size is greater than 128 bytes.",
            "schema": {
              "type": "boolean"
            }
          }
        ],
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/OperateRequestBody"
              }
            },
            "application/msgpack": {
              "schema": {
                "$ref": "#/components/schemas/OperateRequestBody"
              }
            }
          },
          "required": true
        },
        "responses": {
          "403": {
            "description": "Not authorized to access the resource.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "409": {
            "description": "Generation conflict.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "404": {
            "description": "Namespace or set does not exist.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "200": {
            "description": "Read operations on multiple records performed successfully.",
            "content": {
              "application/json": {
                "schema": {
                  "type": "array",
                  "items": {
                    "$ref": "#/components/schemas/RestClientRecord"
                  }
                }
              },
              "application/msgpack": {
                "schema": {
                  "type": "array",
                  "items": {
                    "$ref": "#/components/schemas/RestClientRecord"
                  }
                }
              }
            }
          },
          "400": {
            "description": "Invalid parameters or request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          }
        }
      }
    },
    "/v2/execute/scan/{namespace}": {
      "post": {
        "tags": [
          "Execute Operations"
        ],
        "summary": "Perform multiple operations in background scan/query.",
        "operationId": "executeScanNamespace",
        "parameters": [
          {
            "name": "namespace",
            "in": "path",
            "description": "Namespace for the record; equivalent to database name.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "expiration",
            "in": "query",
            "description": "Record expiration. Also known as ttl (time to live). Seconds record will live before being removed by the server.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "generation",
            "in": "query",
            "description": "Expected generation. Generation is the number of times a record has been modified (including creation) on the server.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "durableDelete",
            "in": "query",
            "description": "If the transaction results in a record deletion, leave a tombstone for the record.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "respondAllOps",
            "in": "query",
            "description": "For client operate(), return a result for every operation.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "commitLevel",
            "in": "query",
            "description": "Desired consistency guarantee when committing a transaction on the server.",
            "schema": {
              "type": "string",
              "enum": [
                "COMMIT_ALL",
                "COMMIT_MASTER"
              ]
            }
          },
          {
            "name": "generationPolicy",
            "in": "query",
            "description": "Qualify how to handle record writes based on record generation.",
            "schema": {
              "type": "string",
              "enum": [
                "NONE",
                "EXPECT_GEN_EQUAL",
                "EXPECT_GEN_GT"
              ]
            }
          },
          {
            "name": "recordExistsAction",
            "in": "query",
            "description": "How to handle the existence of the record. This is ignored for POST/PUT/UPDATE kvs methods.",
            "schema": {
              "type": "string",
              "enum": [
                "UPDATE",
                "UPDATE_ONLY",
                "REPLACE",
                "REPLACE_ONLY",
                "CREATE_ONLY"
              ]
            }
          },
          {
            "name": "sendKey",
            "in": "query",
            "description": "Send user defined key in addition to hash digest on both reads and writes.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "replica",
            "in": "query",
            "description": "Replica algorithm used to determine the target node for a single record command.",
            "schema": {
              "type": "string",
              "enum": [
                "MASTER",
                "MASTER_PROLES",
                "SEQUENCE",
                "PREFER_RACK",
                "RANDOM"
              ]
            }
          },
          {
            "name": "readModeSC",
            "in": "query",
            "description": "Read policy for SC (strong consistency) namespaces. Determines SC read consistency options.",
            "schema": {
              "type": "string",
              "enum": [
                "SESSION",
                "LINEARIZE",
                "ALLOW_REPLICA",
                "ALLOW_UNAVAILABLE"
              ]
            }
          },
          {
            "name": "readModeAP",
            "in": "query",
            "description": "Read policy for AP (availability) namespaces. How duplicates should be consulted in a read operation. Only makes a difference during migrations and only applicable in AP mode.",
            "schema": {
              "type": "string",
              "enum": [
                "ONE",
                "ALL"
              ]
            }
          },
          {
            "name": "totalTimeout",
            "in": "query",
            "description": "Total transaction timeout in milliseconds.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "socketTimeout",
            "in": "query",
            "description": "Socket idle timeout in milliseconds when processing a database command.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "sleepBetweenRetries",
            "in": "query",
            "description": "Milliseconds to sleep between retries.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "maxRetries",
            "in": "query",
            "description": "Maximum number of retries before aborting the current transaction.\nThe initial attempt is not counted as a retry.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "filterexp",
            "in": "query",
            "description": "Optional Filter Expression (introduced in Aerospike Database 5.2.0) in infix notation DSL.",
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "compress",
            "in": "query",
            "description": "Use zlib compression on command buffers sent to the server and responses received from the server when the buffer size is greater than 128 bytes.",
            "schema": {
              "type": "boolean"
            }
          }
        ],
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/ExecuteRequestBody"
              }
            },
            "application/msgpack": {
              "schema": {
                "$ref": "#/components/schemas/ExecuteRequestBody"
              }
            }
          },
          "required": true
        },
        "responses": {
          "403": {
            "description": "Not authorized to access the resource.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "409": {
            "description": "Generation conflict.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "404": {
            "description": "Namespace or set does not exist.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "400": {
            "description": "Invalid parameters or request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "200": {
            "description": "Multiple operations in background scan/query run successfully.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientExecuteTask"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientExecuteTask"
                }
              }
            }
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          }
        }
      }
    },
    "/v2/execute/scan/{namespace}/{set}": {
      "post": {
        "tags": [
          "Execute Operations"
        ],
        "summary": "Perform multiple operations in background scan/query.",
        "operationId": "executeScanNamespaceSet",
        "parameters": [
          {
            "name": "namespace",
            "in": "path",
            "description": "Namespace for the record; equivalent to database name.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "set",
            "in": "path",
            "description": "Set for the record; equivalent to database table.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "expiration",
            "in": "query",
            "description": "Record expiration. Also known as ttl (time to live). Seconds record will live before being removed by the server.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "generation",
            "in": "query",
            "description": "Expected generation. Generation is the number of times a record has been modified (including creation) on the server.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "durableDelete",
            "in": "query",
            "description": "If the transaction results in a record deletion, leave a tombstone for the record.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "respondAllOps",
            "in": "query",
            "description": "For client operate(), return a result for every operation.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "commitLevel",
            "in": "query",
            "description": "Desired consistency guarantee when committing a transaction on the server.",
            "schema": {
              "type": "string",
              "enum": [
                "COMMIT_ALL",
                "COMMIT_MASTER"
              ]
            }
          },
          {
            "name": "generationPolicy",
            "in": "query",
            "description": "Qualify how to handle record writes based on record generation.",
            "schema": {
              "type": "string",
              "enum": [
                "NONE",
                "EXPECT_GEN_EQUAL",
                "EXPECT_GEN_GT"
              ]
            }
          },
          {
            "name": "recordExistsAction",
            "in": "query",
            "description": "How to handle the existence of the record. This is ignored for POST/PUT/UPDATE kvs methods.",
            "schema": {
              "type": "string",
              "enum": [
                "UPDATE",
                "UPDATE_ONLY",
                "REPLACE",
                "REPLACE_ONLY",
                "CREATE_ONLY"
              ]
            }
          },
          {
            "name": "sendKey",
            "in": "query",
            "description": "Send user defined key in addition to hash digest on both reads and writes.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "replica",
            "in": "query",
            "description": "Replica algorithm used to determine the target node for a single record command.",
            "schema": {
              "type": "string",
              "enum": [
                "MASTER",
                "MASTER_PROLES",
                "SEQUENCE",
                "PREFER_RACK",
                "RANDOM"
              ]
            }
          },
          {
            "name": "readModeSC",
            "in": "query",
            "description": "Read policy for SC (strong consistency) namespaces. Determines SC read consistency options.",
            "schema": {
              "type": "string",
              "enum": [
                "SESSION",
                "LINEARIZE",
                "ALLOW_REPLICA",
                "ALLOW_UNAVAILABLE"
              ]
            }
          },
          {
            "name": "readModeAP",
            "in": "query",
            "description": "Read policy for AP (availability) namespaces. How duplicates should be consulted in a read operation. Only makes a difference during migrations and only applicable in AP mode.",
            "schema": {
              "type": "string",
              "enum": [
                "ONE",
                "ALL"
              ]
            }
          },
          {
            "name": "totalTimeout",
            "in": "query",
            "description": "Total transaction timeout in milliseconds.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "socketTimeout",
            "in": "query",
            "description": "Socket idle timeout in milliseconds when processing a database command.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "sleepBetweenRetries",
            "in": "query",
            "description": "Milliseconds to sleep between retries.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "maxRetries",
            "in": "query",
            "description": "Maximum number of retries before aborting the current transaction.\nThe initial attempt is not counted as a retry.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "filterexp",
            "in": "query",
            "description": "Optional Filter Expression (introduced in Aerospike Database 5.2.0) in infix notation DSL.",
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "compress",
            "in": "query",
            "description": "Use zlib compression on command buffers sent to the server and responses received from the server when the buffer size is greater than 128 bytes.",
            "schema": {
              "type": "boolean"
            }
          }
        ],
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/ExecuteRequestBody"
              }
            },
            "application/msgpack": {
              "schema": {
                "$ref": "#/components/schemas/ExecuteRequestBody"
              }
            }
          },
          "required": true
        },
        "responses": {
          "403": {
            "description": "Not authorized to access the resource.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "409": {
            "description": "Generation conflict.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "404": {
            "description": "Namespace or set does not exist.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "400": {
            "description": "Invalid parameters or request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "200": {
            "description": "Multiple operations in background scan/query run successfully.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientExecuteTask"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientExecuteTask"
                }
              }
            }
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          }
        }
      }
    },
    "/v1/query/{namespace}": {
      "post": {
        "tags": [
          "Query Operations"
        ],
        "summary": "Return multiple records from the server in a query request.",
        "operationId": "performNamespaceQuery",
        "parameters": [
          {
            "name": "namespace",
            "in": "path",
            "description": "Namespace for the record; equivalent to database name.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "maxConcurrentNodes",
            "in": "query",
            "description": "Maximum number of concurrent requests to server nodes at any point in time. If there are 16 nodes in the cluster and maxConcurrentNodes is 8, then scan requests will be made to 8 nodes in parallel.  When a scan completes, a new scan request will be issued until all 16 nodes have been scanned.\nThis field is only relevant when concurrentNodes is true.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "includeBinData",
            "in": "query",
            "description": "Should bin data be retrieved. If false, only record digests (and user keys if stored on the server) are retrieved.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "failOnClusterChange",
            "in": "query",
            "description": "Terminate query if cluster is in migration state. If the server supports partition queries or the query filter is null (scan), this field is ignored.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "shortQuery",
            "in": "query",
            "description": "Is query expected to return less than 100 records. If true, the server will optimize the query for a small record set. This field is ignored for aggregation queries, background queries and server versions \u003c 6.0.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "indexName",
            "in": "query",
            "description": "Optional query index filter. This filter is applied to the secondary index on query. Query index filters must reference a bin which has a secondary index defined.",
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "maxRecords",
            "in": "query",
            "description": "Number of records to return. Required for pagination. This number is divided by the number of nodes involved in the query. The actual number of records returned may be less than maxRecords if node record counts are small and unbalanced across nodes.",
            "schema": {
              "type": "integer",
              "default": 10000
            }
          },
          {
            "name": "recordsPerSecond",
            "in": "query",
            "description": "Should bin data be retrieved. If false, only record digests (and user keys if stored on the server) are retrieved.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "sendKey",
            "in": "query",
            "description": "Send user defined key in addition to hash digest on both reads and writes.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "replica",
            "in": "query",
            "description": "Replica algorithm used to determine the target node for a single record command.",
            "schema": {
              "type": "string",
              "enum": [
                "MASTER",
                "MASTER_PROLES",
                "SEQUENCE",
                "PREFER_RACK",
                "RANDOM"
              ]
            }
          },
          {
            "name": "readModeSC",
            "in": "query",
            "description": "Read policy for SC (strong consistency) namespaces. Determines SC read consistency options.",
            "schema": {
              "type": "string",
              "enum": [
                "SESSION",
                "LINEARIZE",
                "ALLOW_REPLICA",
                "ALLOW_UNAVAILABLE"
              ]
            }
          },
          {
            "name": "readModeAP",
            "in": "query",
            "description": "Read policy for AP (availability) namespaces. How duplicates should be consulted in a read operation. Only makes a difference during migrations and only applicable in AP mode.",
            "schema": {
              "type": "string",
              "enum": [
                "ONE",
                "ALL"
              ]
            }
          },
          {
            "name": "totalTimeout",
            "in": "query",
            "description": "Total transaction timeout in milliseconds.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "socketTimeout",
            "in": "query",
            "description": "Socket idle timeout in milliseconds when processing a database command.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "sleepBetweenRetries",
            "in": "query",
            "description": "Milliseconds to sleep between retries.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "maxRetries",
            "in": "query",
            "description": "Maximum number of retries before aborting the current transaction.\nThe initial attempt is not counted as a retry.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "filterexp",
            "in": "query",
            "description": "Optional Filter Expression (introduced in Aerospike Database 5.2.0) in infix notation DSL.",
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "compress",
            "in": "query",
            "description": "Use zlib compression on command buffers sent to the server and responses received from the server when the buffer size is greater than 128 bytes.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "recordBins",
            "in": "query",
            "description": "Optionally specify a set of bins to return when fetching a record. If omitted, all bins will be returned.",
            "schema": {
              "type": "array",
              "items": {
                "type": "string"
              }
            }
          }
        ],
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/QueryRequestBody"
              }
            },
            "application/msgpack": {
              "schema": {
                "$ref": "#/components/schemas/QueryRequestBody"
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "Scan multiple records successfully.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/QueryResponseBody"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/QueryResponseBody"
                }
              }
            }
          },
          "403": {
            "description": "Not authorized to access the resource.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "404": {
            "description": "Namespace or set does not exist.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "400": {
            "description": "Invalid parameters or request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          }
        }
      }
    },
    "/v1/query/{namespace}/{set}": {
      "post": {
        "tags": [
          "Query Operations"
        ],
        "summary": "Return multiple records from the server in a query request.",
        "operationId": "performNamespaceSetQuery",
        "parameters": [
          {
            "name": "namespace",
            "in": "path",
            "description": "Namespace for the record; equivalent to database name.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "set",
            "in": "path",
            "description": "Set for the record; equivalent to database table.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "maxConcurrentNodes",
            "in": "query",
            "description": "Maximum number of concurrent requests to server nodes at any point in time. If there are 16 nodes in the cluster and maxConcurrentNodes is 8, then scan requests will be made to 8 nodes in parallel.  When a scan completes, a new scan request will be issued until all 16 nodes have been scanned.\nThis field is only relevant when concurrentNodes is true.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "includeBinData",
            "in": "query",
            "description": "Should bin data be retrieved. If false, only record digests (and user keys if stored on the server) are retrieved.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "failOnClusterChange",
            "in": "query",
            "description": "Terminate query if cluster is in migration state. If the server supports partition queries or the query filter is null (scan), this field is ignored.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "shortQuery",
            "in": "query",
            "description": "Is query expected to return less than 100 records. If true, the server will optimize the query for a small record set. This field is ignored for aggregation queries, background queries and server versions \u003c 6.0.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "indexName",
            "in": "query",
            "description": "Optional query index filter. This filter is applied to the secondary index on query. Query index filters must reference a bin which has a secondary index defined.",
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "maxRecords",
            "in": "query",
            "description": "Number of records to return. Required for pagination. This number is divided by the number of nodes involved in the query. The actual number of records returned may be less than maxRecords if node record counts are small and unbalanced across nodes.",
            "schema": {
              "type": "integer",
              "default": 10000
            }
          },
          {
            "name": "recordsPerSecond",
            "in": "query",
            "description": "Should bin data be retrieved. If false, only record digests (and user keys if stored on the server) are retrieved.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "sendKey",
            "in": "query",
            "description": "Send user defined key in addition to hash digest on both reads and writes.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "replica",
            "in": "query",
            "description": "Replica algorithm used to determine the target node for a single record command.",
            "schema": {
              "type": "string",
              "enum": [
                "MASTER",
                "MASTER_PROLES",
                "SEQUENCE",
                "PREFER_RACK",
                "RANDOM"
              ]
            }
          },
          {
            "name": "readModeSC",
            "in": "query",
            "description": "Read policy for SC (strong consistency) namespaces. Determines SC read consistency options.",
            "schema": {
              "type": "string",
              "enum": [
                "SESSION",
                "LINEARIZE",
                "ALLOW_REPLICA",
                "ALLOW_UNAVAILABLE"
              ]
            }
          },
          {
            "name": "readModeAP",
            "in": "query",
            "description": "Read policy for AP (availability) namespaces. How duplicates should be consulted in a read operation. Only makes a difference during migrations and only applicable in AP mode.",
            "schema": {
              "type": "string",
              "enum": [
                "ONE",
                "ALL"
              ]
            }
          },
          {
            "name": "totalTimeout",
            "in": "query",
            "description": "Total transaction timeout in milliseconds.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "socketTimeout",
            "in": "query",
            "description": "Socket idle timeout in milliseconds when processing a database command.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "sleepBetweenRetries",
            "in": "query",
            "description": "Milliseconds to sleep between retries.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "maxRetries",
            "in": "query",
            "description": "Maximum number of retries before aborting the current transaction.\nThe initial attempt is not counted as a retry.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "filterexp",
            "in": "query",
            "description": "Optional Filter Expression (introduced in Aerospike Database 5.2.0) in infix notation DSL.",
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "compress",
            "in": "query",
            "description": "Use zlib compression on command buffers sent to the server and responses received from the server when the buffer size is greater than 128 bytes.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "recordBins",
            "in": "query",
            "description": "Optionally specify a set of bins to return when fetching a record. If omitted, all bins will be returned.",
            "schema": {
              "type": "array",
              "items": {
                "type": "string"
              }
            }
          }
        ],
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/QueryRequestBody"
              }
            },
            "application/msgpack": {
              "schema": {
                "$ref": "#/components/schemas/QueryRequestBody"
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "Query multiple records successfully.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/QueryResponseBody"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/QueryResponseBody"
                }
              }
            }
          },
          "403": {
            "description": "Not authorized to access the resource.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "404": {
            "description": "Namespace or set does not exist.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "400": {
            "description": "Invalid parameters or request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          }
        }
      }
    },
    "/v1/query/{namespace}/{set}/{begin}/{count}": {
      "post": {
        "tags": [
          "Query Operations"
        ],
        "summary": "Return multiple records from the server in a query request using the provided partition range.",
        "operationId": "performNamespaceSetRangeQuery",
        "parameters": [
          {
            "name": "namespace",
            "in": "path",
            "description": "Namespace for the record; equivalent to database name.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "set",
            "in": "path",
            "description": "Set for the record; equivalent to database table.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "begin",
            "in": "path",
            "description": "Start partition id (0 - 4095)",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          },
          {
            "name": "count",
            "in": "path",
            "description": "Number of partitions",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          },
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "maxConcurrentNodes",
            "in": "query",
            "description": "Maximum number of concurrent requests to server nodes at any point in time. If there are 16 nodes in the cluster and maxConcurrentNodes is 8, then scan requests will be made to 8 nodes in parallel.  When a scan completes, a new scan request will be issued until all 16 nodes have been scanned.\nThis field is only relevant when concurrentNodes is true.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "includeBinData",
            "in": "query",
            "description": "Should bin data be retrieved. If false, only record digests (and user keys if stored on the server) are retrieved.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "failOnClusterChange",
            "in": "query",
            "description": "Terminate query if cluster is in migration state. If the server supports partition queries or the query filter is null (scan), this field is ignored.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "shortQuery",
            "in": "query",
            "description": "Is query expected to return less than 100 records. If true, the server will optimize the query for a small record set. This field is ignored for aggregation queries, background queries and server versions \u003c 6.0.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "indexName",
            "in": "query",
            "description": "Optional query index filter. This filter is applied to the secondary index on query. Query index filters must reference a bin which has a secondary index defined.",
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "maxRecords",
            "in": "query",
            "description": "Number of records to return. Required for pagination. This number is divided by the number of nodes involved in the query. The actual number of records returned may be less than maxRecords if node record counts are small and unbalanced across nodes.",
            "schema": {
              "type": "integer",
              "default": 10000
            }
          },
          {
            "name": "recordsPerSecond",
            "in": "query",
            "description": "Should bin data be retrieved. If false, only record digests (and user keys if stored on the server) are retrieved.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "sendKey",
            "in": "query",
            "description": "Send user defined key in addition to hash digest on both reads and writes.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "replica",
            "in": "query",
            "description": "Replica algorithm used to determine the target node for a single record command.",
            "schema": {
              "type": "string",
              "enum": [
                "MASTER",
                "MASTER_PROLES",
                "SEQUENCE",
                "PREFER_RACK",
                "RANDOM"
              ]
            }
          },
          {
            "name": "readModeSC",
            "in": "query",
            "description": "Read policy for SC (strong consistency) namespaces. Determines SC read consistency options.",
            "schema": {
              "type": "string",
              "enum": [
                "SESSION",
                "LINEARIZE",
                "ALLOW_REPLICA",
                "ALLOW_UNAVAILABLE"
              ]
            }
          },
          {
            "name": "readModeAP",
            "in": "query",
            "description": "Read policy for AP (availability) namespaces. How duplicates should be consulted in a read operation. Only makes a difference during migrations and only applicable in AP mode.",
            "schema": {
              "type": "string",
              "enum": [
                "ONE",
                "ALL"
              ]
            }
          },
          {
            "name": "totalTimeout",
            "in": "query",
            "description": "Total transaction timeout in milliseconds.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "socketTimeout",
            "in": "query",
            "description": "Socket idle timeout in milliseconds when processing a database command.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "sleepBetweenRetries",
            "in": "query",
            "description": "Milliseconds to sleep between retries.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "maxRetries",
            "in": "query",
            "description": "Maximum number of retries before aborting the current transaction.\nThe initial attempt is not counted as a retry.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "filterexp",
            "in": "query",
            "description": "Optional Filter Expression (introduced in Aerospike Database 5.2.0) in infix notation DSL.",
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "compress",
            "in": "query",
            "description": "Use zlib compression on command buffers sent to the server and responses received from the server when the buffer size is greater than 128 bytes.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "recordBins",
            "in": "query",
            "description": "Optionally specify a set of bins to return when fetching a record. If omitted, all bins will be returned.",
            "schema": {
              "type": "array",
              "items": {
                "type": "string"
              }
            }
          }
        ],
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/QueryRequestBody"
              }
            },
            "application/msgpack": {
              "schema": {
                "$ref": "#/components/schemas/QueryRequestBody"
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "Query multiple records successfully.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/QueryResponseBody"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/QueryResponseBody"
                }
              }
            }
          },
          "403": {
            "description": "Not authorized to access the resource.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "404": {
            "description": "Namespace or set does not exist.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "400": {
            "description": "Invalid parameters or request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          }
        }
      }
    },
    "/v1/query/{namespace}/{begin}/{count}": {
      "post": {
        "tags": [
          "Query Operations"
        ],
        "summary": "Return multiple records from the server in a query request using the provided partition range.",
        "operationId": "performNamespaceRangeQuery",
        "parameters": [
          {
            "name": "namespace",
            "in": "path",
            "description": "Namespace for the record; equivalent to database name.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "begin",
            "in": "path",
            "description": "Start partition id (0 - 4095)",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          },
          {
            "name": "count",
            "in": "path",
            "description": "Number of partitions",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int32"
            }
          },
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "maxConcurrentNodes",
            "in": "query",
            "description": "Maximum number of concurrent requests to server nodes at any point in time. If there are 16 nodes in the cluster and maxConcurrentNodes is 8, then scan requests will be made to 8 nodes in parallel.  When a scan completes, a new scan request will be issued until all 16 nodes have been scanned.\nThis field is only relevant when concurrentNodes is true.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "includeBinData",
            "in": "query",
            "description": "Should bin data be retrieved. If false, only record digests (and user keys if stored on the server) are retrieved.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "failOnClusterChange",
            "in": "query",
            "description": "Terminate query if cluster is in migration state. If the server supports partition queries or the query filter is null (scan), this field is ignored.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "shortQuery",
            "in": "query",
            "description": "Is query expected to return less than 100 records. If true, the server will optimize the query for a small record set. This field is ignored for aggregation queries, background queries and server versions \u003c 6.0.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "indexName",
            "in": "query",
            "description": "Optional query index filter. This filter is applied to the secondary index on query. Query index filters must reference a bin which has a secondary index defined.",
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "maxRecords",
            "in": "query",
            "description": "Number of records to return. Required for pagination. This number is divided by the number of nodes involved in the query. The actual number of records returned may be less than maxRecords if node record counts are small and unbalanced across nodes.",
            "schema": {
              "type": "integer",
              "default": 10000
            }
          },
          {
            "name": "recordsPerSecond",
            "in": "query",
            "description": "Should bin data be retrieved. If false, only record digests (and user keys if stored on the server) are retrieved.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "sendKey",
            "in": "query",
            "description": "Send user defined key in addition to hash digest on both reads and writes.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "replica",
            "in": "query",
            "description": "Replica algorithm used to determine the target node for a single record command.",
            "schema": {
              "type": "string",
              "enum": [
                "MASTER",
                "MASTER_PROLES",
                "SEQUENCE",
                "PREFER_RACK",
                "RANDOM"
              ]
            }
          },
          {
            "name": "readModeSC",
            "in": "query",
            "description": "Read policy for SC (strong consistency) namespaces. Determines SC read consistency options.",
            "schema": {
              "type": "string",
              "enum": [
                "SESSION",
                "LINEARIZE",
                "ALLOW_REPLICA",
                "ALLOW_UNAVAILABLE"
              ]
            }
          },
          {
            "name": "readModeAP",
            "in": "query",
            "description": "Read policy for AP (availability) namespaces. How duplicates should be consulted in a read operation. Only makes a difference during migrations and only applicable in AP mode.",
            "schema": {
              "type": "string",
              "enum": [
                "ONE",
                "ALL"
              ]
            }
          },
          {
            "name": "totalTimeout",
            "in": "query",
            "description": "Total transaction timeout in milliseconds.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "socketTimeout",
            "in": "query",
            "description": "Socket idle timeout in milliseconds when processing a database command.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "sleepBetweenRetries",
            "in": "query",
            "description": "Milliseconds to sleep between retries.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "maxRetries",
            "in": "query",
            "description": "Maximum number of retries before aborting the current transaction.\nThe initial attempt is not counted as a retry.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "filterexp",
            "in": "query",
            "description": "Optional Filter Expression (introduced in Aerospike Database 5.2.0) in infix notation DSL.",
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "compress",
            "in": "query",
            "description": "Use zlib compression on command buffers sent to the server and responses received from the server when the buffer size is greater than 128 bytes.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "recordBins",
            "in": "query",
            "description": "Optionally specify a set of bins to return when fetching a record. If omitted, all bins will be returned.",
            "schema": {
              "type": "array",
              "items": {
                "type": "string"
              }
            }
          }
        ],
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/QueryRequestBody"
              }
            },
            "application/msgpack": {
              "schema": {
                "$ref": "#/components/schemas/QueryRequestBody"
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "Scan multiple records successfully.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/QueryResponseBody"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/QueryResponseBody"
                }
              }
            }
          },
          "403": {
            "description": "Not authorized to access the resource.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "404": {
            "description": "Namespace or set does not exist.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "400": {
            "description": "Invalid parameters or request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          }
        }
      }
    },
    "/v1/operate/{namespace}/{set}/{key}": {
      "post": {
        "tags": [
          "Operate operations"
        ],
        "summary": "Perform multiple operations atomically on the specified record.",
        "operationId": "operateNamespaceSetKey_1",
        "parameters": [
          {
            "name": "namespace",
            "in": "path",
            "description": "Namespace for the record; equivalent to database name.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "set",
            "in": "path",
            "description": "Set for the record; equivalent to database table.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "key",
            "in": "path",
            "description": "Userkey for the record.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "keytype",
            "in": "query",
            "description": "The Type of the userKey.",
            "schema": {
              "type": "string",
              "enum": [
                "STRING",
                "INTEGER",
                "BYTES",
                "DIGEST"
              ]
            }
          },
          {
            "name": "expiration",
            "in": "query",
            "description": "Record expiration. Also known as ttl (time to live). Seconds record will live before being removed by the server.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "generation",
            "in": "query",
            "description": "Expected generation. Generation is the number of times a record has been modified (including creation) on the server.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "durableDelete",
            "in": "query",
            "description": "If the transaction results in a record deletion, leave a tombstone for the record.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "respondAllOps",
            "in": "query",
            "description": "For client operate(), return a result for every operation.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "commitLevel",
            "in": "query",
            "description": "Desired consistency guarantee when committing a transaction on the server.",
            "schema": {
              "type": "string",
              "enum": [
                "COMMIT_ALL",
                "COMMIT_MASTER"
              ]
            }
          },
          {
            "name": "generationPolicy",
            "in": "query",
            "description": "Qualify how to handle record writes based on record generation.",
            "schema": {
              "type": "string",
              "enum": [
                "NONE",
                "EXPECT_GEN_EQUAL",
                "EXPECT_GEN_GT"
              ]
            }
          },
          {
            "name": "recordExistsAction",
            "in": "query",
            "description": "How to handle the existence of the record. This is ignored for POST/PUT/UPDATE kvs methods.",
            "schema": {
              "type": "string",
              "enum": [
                "UPDATE",
                "UPDATE_ONLY",
                "REPLACE",
                "REPLACE_ONLY",
                "CREATE_ONLY"
              ]
            }
          },
          {
            "name": "sendKey",
            "in": "query",
            "description": "Send user defined key in addition to hash digest on both reads and writes.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "replica",
            "in": "query",
            "description": "Replica algorithm used to determine the target node for a single record command.",
            "schema": {
              "type": "string",
              "enum": [
                "MASTER",
                "MASTER_PROLES",
                "SEQUENCE",
                "PREFER_RACK",
                "RANDOM"
              ]
            }
          },
          {
            "name": "readModeSC",
            "in": "query",
            "description": "Read policy for SC (strong consistency) namespaces. Determines SC read consistency options.",
            "schema": {
              "type": "string",
              "enum": [
                "SESSION",
                "LINEARIZE",
                "ALLOW_REPLICA",
                "ALLOW_UNAVAILABLE"
              ]
            }
          },
          {
            "name": "readModeAP",
            "in": "query",
            "description": "Read policy for AP (availability) namespaces. How duplicates should be consulted in a read operation. Only makes a difference during migrations and only applicable in AP mode.",
            "schema": {
              "type": "string",
              "enum": [
                "ONE",
                "ALL"
              ]
            }
          },
          {
            "name": "totalTimeout",
            "in": "query",
            "description": "Total transaction timeout in milliseconds.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "socketTimeout",
            "in": "query",
            "description": "Socket idle timeout in milliseconds when processing a database command.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "sleepBetweenRetries",
            "in": "query",
            "description": "Milliseconds to sleep between retries.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "maxRetries",
            "in": "query",
            "description": "Maximum number of retries before aborting the current transaction.\nThe initial attempt is not counted as a retry.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "filterexp",
            "in": "query",
            "description": "Optional Filter Expression (introduced in Aerospike Database 5.2.0) in infix notation DSL.",
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "compress",
            "in": "query",
            "description": "Use zlib compression on command buffers sent to the server and responses received from the server when the buffer size is greater than 128 bytes.",
            "schema": {
              "type": "boolean"
            }
          }
        ],
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "type": "array",
                "description": "An array of operation objects specifying the operations to perform on the record",
                "items": {
                  "$ref": "#/components/schemas/RestClientOperation"
                }
              }
            }
          },
          "required": true
        },
        "responses": {
          "403": {
            "description": "Not authorized to access the resource.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "409": {
            "description": "Generation conflict.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "404": {
            "description": "Namespace or set does not exist.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "400": {
            "description": "Invalid parameters or request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "200": {
            "description": "Multiple operations on a record performed successfully.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientRecord"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientRecord"
                }
              }
            }
          }
        },
        "deprecated": true
      }
    },
    "/v1/operate/{namespace}/{key}": {
      "post": {
        "tags": [
          "Operate operations"
        ],
        "summary": "Perform multiple operations atomically on the specified record.",
        "operationId": "operateNamespaceKey_1",
        "parameters": [
          {
            "name": "namespace",
            "in": "path",
            "description": "Namespace for the record; equivalent to database name.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "key",
            "in": "path",
            "description": "Userkey for the record.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "keytype",
            "in": "query",
            "description": "The Type of the userKey.",
            "schema": {
              "type": "string",
              "enum": [
                "STRING",
                "INTEGER",
                "BYTES",
                "DIGEST"
              ]
            }
          },
          {
            "name": "expiration",
            "in": "query",
            "description": "Record expiration. Also known as ttl (time to live). Seconds record will live before being removed by the server.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "generation",
            "in": "query",
            "description": "Expected generation. Generation is the number of times a record has been modified (including creation) on the server.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "durableDelete",
            "in": "query",
            "description": "If the transaction results in a record deletion, leave a tombstone for the record.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "respondAllOps",
            "in": "query",
            "description": "For client operate(), return a result for every operation.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "commitLevel",
            "in": "query",
            "description": "Desired consistency guarantee when committing a transaction on the server.",
            "schema": {
              "type": "string",
              "enum": [
                "COMMIT_ALL",
                "COMMIT_MASTER"
              ]
            }
          },
          {
            "name": "generationPolicy",
            "in": "query",
            "description": "Qualify how to handle record writes based on record generation.",
            "schema": {
              "type": "string",
              "enum": [
                "NONE",
                "EXPECT_GEN_EQUAL",
                "EXPECT_GEN_GT"
              ]
            }
          },
          {
            "name": "recordExistsAction",
            "in": "query",
            "description": "How to handle the existence of the record. This is ignored for POST/PUT/UPDATE kvs methods.",
            "schema": {
              "type": "string",
              "enum": [
                "UPDATE",
                "UPDATE_ONLY",
                "REPLACE",
                "REPLACE_ONLY",
                "CREATE_ONLY"
              ]
            }
          },
          {
            "name": "sendKey",
            "in": "query",
            "description": "Send user defined key in addition to hash digest on both reads and writes.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "replica",
            "in": "query",
            "description": "Replica algorithm used to determine the target node for a single record command.",
            "schema": {
              "type": "string",
              "enum": [
                "MASTER",
                "MASTER_PROLES",
                "SEQUENCE",
                "PREFER_RACK",
                "RANDOM"
              ]
            }
          },
          {
            "name": "readModeSC",
            "in": "query",
            "description": "Read policy for SC (strong consistency) namespaces. Determines SC read consistency options.",
            "schema": {
              "type": "string",
              "enum": [
                "SESSION",
                "LINEARIZE",
                "ALLOW_REPLICA",
                "ALLOW_UNAVAILABLE"
              ]
            }
          },
          {
            "name": "readModeAP",
            "in": "query",
            "description": "Read policy for AP (availability) namespaces. How duplicates should be consulted in a read operation. Only makes a difference during migrations and only applicable in AP mode.",
            "schema": {
              "type": "string",
              "enum": [
                "ONE",
                "ALL"
              ]
            }
          },
          {
            "name": "totalTimeout",
            "in": "query",
            "description": "Total transaction timeout in milliseconds.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "socketTimeout",
            "in": "query",
            "description": "Socket idle timeout in milliseconds when processing a database command.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "sleepBetweenRetries",
            "in": "query",
            "description": "Milliseconds to sleep between retries.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "maxRetries",
            "in": "query",
            "description": "Maximum number of retries before aborting the current transaction.\nThe initial attempt is not counted as a retry.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "filterexp",
            "in": "query",
            "description": "Optional Filter Expression (introduced in Aerospike Database 5.2.0) in infix notation DSL.",
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "compress",
            "in": "query",
            "description": "Use zlib compression on command buffers sent to the server and responses received from the server when the buffer size is greater than 128 bytes.",
            "schema": {
              "type": "boolean"
            }
          }
        ],
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "type": "array",
                "description": "An array of operation objects specifying the operations to perform on the record",
                "items": {
                  "$ref": "#/components/schemas/RestClientOperation"
                }
              }
            }
          },
          "required": true
        },
        "responses": {
          "403": {
            "description": "Not authorized to access the resource.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "409": {
            "description": "Generation conflict.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "404": {
            "description": "Namespace or set does not exist.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "400": {
            "description": "Invalid parameters or request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "200": {
            "description": "Multiple operations on a record performed successfully.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientRecord"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientRecord"
                }
              }
            }
          }
        },
        "deprecated": true
      }
    },
    "/v1/operate/read/{namespace}": {
      "post": {
        "tags": [
          "Operate operations"
        ],
        "summary": "Perform read operations on multiple records.",
        "operationId": "operateBatchNamespace_1",
        "parameters": [
          {
            "name": "namespace",
            "in": "path",
            "description": "Namespace for the record; equivalent to database name.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "keytype",
            "in": "query",
            "description": "The Type of the userKey.",
            "schema": {
              "type": "string",
              "enum": [
                "STRING",
                "INTEGER",
                "BYTES",
                "DIGEST"
              ]
            }
          },
          {
            "name": "allowInline",
            "in": "query",
            "description": "Allow batch to be processed immediately in the server\u0027s receiving thread when the server deems it to be appropriate.  If false, the batch will always be processed in separate transaction threads.  This field is only relevant for the new batch index protocol.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "allowInlineSSD",
            "in": "query",
            "description": "Allow batch to be processed immediately in the server\u0027s receiving thread for SSD namespaces. If false, the batch will always be processed in separate service threads. Server versions \u003c 6.0 ignore this field.\nInline processing can introduce the possibility of unfairness because the server can process the entire batch before moving onto the next command.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "maxConcurrentThreads",
            "in": "query",
            "description": "Maximum number of concurrent synchronous batch request threads to server nodes at any point in time. If there are 16 node/namespace combinations requested and maxConcurrentThreads is 8, then batch requests will be made for 8 node/namespace combinations in parallel threads. When a request completes, a new request will be issued until all 16 requests are complete.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "respondAllKeys",
            "in": "query",
            "description": "Should all batch keys be attempted regardless of errors. This field is used on both the client and server. The client handles node specific errors and the server handles key specific errors.\nIf true, every batch key is attempted regardless of previous key specific errors. Node specific errors such as timeouts stop keys to that node, but keys directed at other nodes will continue to be processed.\nIf false, the server will stop the batch to its node on most key specific errors. The exceptions are com.aerospike.client.ResultCode.KEY_NOT_FOUND_ERROR and com.aerospike.client.ResultCode.FILTERED_OUT which never stop the batch. The client will stop the entire batch on node specific errors for sync commands that are run in sequence (maxConcurrentThreads \u003d\u003d 1). The client will not stop the entire batch for async commands or sync commands run in parallel.\nServer versions \u003c 6.0 do not support this field and treat this value as false for key specific errors.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "key",
            "in": "query",
            "description": "Record keys to perform operations on.",
            "required": true,
            "schema": {
              "type": "array",
              "items": {
                "type": "string"
              }
            }
          },
          {
            "name": "sendKey",
            "in": "query",
            "description": "Send user defined key in addition to hash digest on both reads and writes.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "replica",
            "in": "query",
            "description": "Replica algorithm used to determine the target node for a single record command.",
            "schema": {
              "type": "string",
              "enum": [
                "MASTER",
                "MASTER_PROLES",
                "SEQUENCE",
                "PREFER_RACK",
                "RANDOM"
              ]
            }
          },
          {
            "name": "readModeSC",
            "in": "query",
            "description": "Read policy for SC (strong consistency) namespaces. Determines SC read consistency options.",
            "schema": {
              "type": "string",
              "enum": [
                "SESSION",
                "LINEARIZE",
                "ALLOW_REPLICA",
                "ALLOW_UNAVAILABLE"
              ]
            }
          },
          {
            "name": "readModeAP",
            "in": "query",
            "description": "Read policy for AP (availability) namespaces. How duplicates should be consulted in a read operation. Only makes a difference during migrations and only applicable in AP mode.",
            "schema": {
              "type": "string",
              "enum": [
                "ONE",
                "ALL"
              ]
            }
          },
          {
            "name": "totalTimeout",
            "in": "query",
            "description": "Total transaction timeout in milliseconds.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "socketTimeout",
            "in": "query",
            "description": "Socket idle timeout in milliseconds when processing a database command.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "sleepBetweenRetries",
            "in": "query",
            "description": "Milliseconds to sleep between retries.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "maxRetries",
            "in": "query",
            "description": "Maximum number of retries before aborting the current transaction.\nThe initial attempt is not counted as a retry.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "filterexp",
            "in": "query",
            "description": "Optional Filter Expression (introduced in Aerospike Database 5.2.0) in infix notation DSL.",
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "compress",
            "in": "query",
            "description": "Use zlib compression on command buffers sent to the server and responses received from the server when the buffer size is greater than 128 bytes.",
            "schema": {
              "type": "boolean"
            }
          }
        ],
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "type": "array",
                "description": "An array of operation objects specifying the operations to perform on the record",
                "items": {
                  "$ref": "#/components/schemas/RestClientOperation"
                }
              }
            }
          },
          "required": true
        },
        "responses": {
          "403": {
            "description": "Not authorized to access the resource.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "409": {
            "description": "Generation conflict.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "404": {
            "description": "Namespace or set does not exist.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "200": {
            "description": "Read operations on multiple records performed successfully.",
            "content": {
              "application/json": {
                "schema": {
                  "type": "array",
                  "items": {
                    "$ref": "#/components/schemas/RestClientRecord"
                  }
                }
              },
              "application/msgpack": {
                "schema": {
                  "type": "array",
                  "items": {
                    "$ref": "#/components/schemas/RestClientRecord"
                  }
                }
              }
            }
          },
          "400": {
            "description": "Invalid parameters or request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          }
        },
        "deprecated": true
      }
    },
    "/v1/operate/read/{namespace}/{set}": {
      "post": {
        "tags": [
          "Operate operations"
        ],
        "summary": "Perform read operations on multiple records.",
        "operationId": "operateBatchNamespaceSet_1",
        "parameters": [
          {
            "name": "namespace",
            "in": "path",
            "description": "Namespace for the record; equivalent to database name.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "set",
            "in": "path",
            "description": "Set for the record; equivalent to database table.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "keytype",
            "in": "query",
            "description": "The Type of the userKey.",
            "schema": {
              "type": "string",
              "enum": [
                "STRING",
                "INTEGER",
                "BYTES",
                "DIGEST"
              ]
            }
          },
          {
            "name": "allowInline",
            "in": "query",
            "description": "Allow batch to be processed immediately in the server\u0027s receiving thread when the server deems it to be appropriate.  If false, the batch will always be processed in separate transaction threads.  This field is only relevant for the new batch index protocol.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "allowInlineSSD",
            "in": "query",
            "description": "Allow batch to be processed immediately in the server\u0027s receiving thread for SSD namespaces. If false, the batch will always be processed in separate service threads. Server versions \u003c 6.0 ignore this field.\nInline processing can introduce the possibility of unfairness because the server can process the entire batch before moving onto the next command.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "maxConcurrentThreads",
            "in": "query",
            "description": "Maximum number of concurrent synchronous batch request threads to server nodes at any point in time. If there are 16 node/namespace combinations requested and maxConcurrentThreads is 8, then batch requests will be made for 8 node/namespace combinations in parallel threads. When a request completes, a new request will be issued until all 16 requests are complete.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "respondAllKeys",
            "in": "query",
            "description": "Should all batch keys be attempted regardless of errors. This field is used on both the client and server. The client handles node specific errors and the server handles key specific errors.\nIf true, every batch key is attempted regardless of previous key specific errors. Node specific errors such as timeouts stop keys to that node, but keys directed at other nodes will continue to be processed.\nIf false, the server will stop the batch to its node on most key specific errors. The exceptions are com.aerospike.client.ResultCode.KEY_NOT_FOUND_ERROR and com.aerospike.client.ResultCode.FILTERED_OUT which never stop the batch. The client will stop the entire batch on node specific errors for sync commands that are run in sequence (maxConcurrentThreads \u003d\u003d 1). The client will not stop the entire batch for async commands or sync commands run in parallel.\nServer versions \u003c 6.0 do not support this field and treat this value as false for key specific errors.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "key",
            "in": "query",
            "description": "Record keys to perform operations on.",
            "required": true,
            "schema": {
              "type": "array",
              "items": {
                "type": "string"
              }
            }
          },
          {
            "name": "sendKey",
            "in": "query",
            "description": "Send user defined key in addition to hash digest on both reads and writes.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "replica",
            "in": "query",
            "description": "Replica algorithm used to determine the target node for a single record command.",
            "schema": {
              "type": "string",
              "enum": [
                "MASTER",
                "MASTER_PROLES",
                "SEQUENCE",
                "PREFER_RACK",
                "RANDOM"
              ]
            }
          },
          {
            "name": "readModeSC",
            "in": "query",
            "description": "Read policy for SC (strong consistency) namespaces. Determines SC read consistency options.",
            "schema": {
              "type": "string",
              "enum": [
                "SESSION",
                "LINEARIZE",
                "ALLOW_REPLICA",
                "ALLOW_UNAVAILABLE"
              ]
            }
          },
          {
            "name": "readModeAP",
            "in": "query",
            "description": "Read policy for AP (availability) namespaces. How duplicates should be consulted in a read operation. Only makes a difference during migrations and only applicable in AP mode.",
            "schema": {
              "type": "string",
              "enum": [
                "ONE",
                "ALL"
              ]
            }
          },
          {
            "name": "totalTimeout",
            "in": "query",
            "description": "Total transaction timeout in milliseconds.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "socketTimeout",
            "in": "query",
            "description": "Socket idle timeout in milliseconds when processing a database command.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "sleepBetweenRetries",
            "in": "query",
            "description": "Milliseconds to sleep between retries.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "maxRetries",
            "in": "query",
            "description": "Maximum number of retries before aborting the current transaction.\nThe initial attempt is not counted as a retry.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "filterexp",
            "in": "query",
            "description": "Optional Filter Expression (introduced in Aerospike Database 5.2.0) in infix notation DSL.",
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "compress",
            "in": "query",
            "description": "Use zlib compression on command buffers sent to the server and responses received from the server when the buffer size is greater than 128 bytes.",
            "schema": {
              "type": "boolean"
            }
          }
        ],
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "type": "array",
                "description": "An array of operation objects specifying the operations to perform on the record",
                "items": {
                  "$ref": "#/components/schemas/RestClientOperation"
                }
              }
            }
          },
          "required": true
        },
        "responses": {
          "403": {
            "description": "Not authorized to access the resource.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "409": {
            "description": "Generation conflict.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "404": {
            "description": "Namespace or set does not exist.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "200": {
            "description": "Read operations on multiple records performed successfully.",
            "content": {
              "application/json": {
                "schema": {
                  "type": "array",
                  "items": {
                    "$ref": "#/components/schemas/RestClientRecord"
                  }
                }
              },
              "application/msgpack": {
                "schema": {
                  "type": "array",
                  "items": {
                    "$ref": "#/components/schemas/RestClientRecord"
                  }
                }
              }
            }
          },
          "400": {
            "description": "Invalid parameters or request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          }
        },
        "deprecated": true
      }
    },
    "/v1/info": {
      "post": {
        "tags": [
          "Info Operations"
        ],
        "summary": "Send a list of info commands to a random node in the cluster",
        "operationId": "infoAny",
        "parameters": [
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "timeout",
            "in": "query",
            "description": "Info command socket timeout in milliseconds.",
            "schema": {
              "type": "integer"
            }
          }
        ],
        "requestBody": {
          "description": "An array of info commands to send to the server. See https://www.aerospike.com/docs/reference/info/ for a list of valid commands.",
          "content": {
            "application/json": {
              "schema": {
                "type": "array",
                "items": {
                  "type": "string"
                }
              },
              "examples": {
                "Array of info commands request body example": {
                  "description": "Array of info commands request body example",
                  "value": [
                    "build",
                    "edition"
                  ]
                }
              }
            },
            "application/msgpack": {
              "schema": {
                "type": "array",
                "items": {
                  "type": "string"
                }
              },
              "examples": {
                "Array of info commands request body example": {
                  "description": "Array of info commands request body example",
                  "value": [
                    "build",
                    "edition"
                  ]
                }
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "Commands sent successfully.",
            "content": {
              "application/json": {
                "examples": {
                  "Info response example": {
                    "description": "Info response example",
                    "value": {
                      "edition": "Aerospike Enterprise Edition",
                      "name": "BB9DE9B1B270008"
                    }
                  }
                }
              },
              "application/msgpack": {
                "examples": {
                  "Info response example": {
                    "description": "Info response example",
                    "value": {
                      "edition": "Aerospike Enterprise Edition",
                      "name": "BB9DE9B1B270008"
                    }
                  }
                }
              }
            }
          },
          "403": {
            "description": "Not authorized to perform the info command.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          }
        }
      }
    },
    "/v1/info/{node}": {
      "post": {
        "tags": [
          "Info Operations"
        ],
        "summary": "Send a list of info commands to a specific node in the cluster.",
        "operationId": "infoNode",
        "parameters": [
          {
            "name": "node",
            "in": "path",
            "description": "The node ID for the node which will receive the info commands.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          }
        ],
        "requestBody": {
          "description": "An array of info commands to send to the server. See https://www.aerospike.com/docs/reference/info/ for a list of valid commands.",
          "content": {
            "application/json": {
              "schema": {
                "type": "array",
                "items": {
                  "type": "string"
                }
              },
              "examples": {
                "Array of info commands request body example": {
                  "description": "Array of info commands request body example",
                  "value": [
                    "build",
                    "edition"
                  ]
                }
              }
            },
            "application/msgpack": {
              "schema": {
                "type": "array",
                "items": {
                  "type": "string"
                }
              },
              "examples": {
                "Array of info commands request body example": {
                  "description": "Array of info commands request body example",
                  "value": [
                    "build",
                    "edition"
                  ]
                }
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "Commands sent successfully.",
            "content": {
              "application/json": {
                "examples": {
                  "Info response example": {
                    "description": "Info response example",
                    "value": {
                      "edition": "Aerospike Enterprise Edition",
                      "name": "BB9DE9B1B270008"
                    }
                  }
                }
              },
              "application/msgpack": {
                "examples": {
                  "Info response example": {
                    "description": "Info response example",
                    "value": {
                      "edition": "Aerospike Enterprise Edition",
                      "name": "BB9DE9B1B270008"
                    }
                  }
                }
              }
            }
          },
          "404": {
            "description": "The specified Node does not exist.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "403": {
            "description": "Not authorized to perform the info command",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          }
        }
      }
    },
    "/v1/index": {
      "get": {
        "tags": [
          "Secondary Index methods"
        ],
        "summary": "Return information about multiple secondary indexes.",
        "operationId": "indexInformation",
        "parameters": [
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "namespace",
            "in": "query",
            "description": "If specified, the list of returned indices will only contain entries from this namespace.",
            "schema": {
              "type": "string"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "Information about secondary indexes read successfully.",
            "content": {
              "application/json": {
                "schema": {
                  "type": "array",
                  "items": {
                    "$ref": "#/components/schemas/RestClientIndex"
                  }
                }
              },
              "application/msgpack": {
                "schema": {
                  "type": "array",
                  "items": {
                    "$ref": "#/components/schemas/RestClientIndex"
                  }
                }
              }
            }
          },
          "403": {
            "description": "Not authorized to access the resource.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "404": {
            "description": "Specified namespace not found.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          }
        }
      },
      "post": {
        "tags": [
          "Secondary Index methods"
        ],
        "summary": "Create a secondary index.",
        "operationId": "createIndex",
        "parameters": [
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          }
        ],
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/RestClientIndex"
              }
            },
            "application/msgpack": {
              "schema": {
                "$ref": "#/components/schemas/RestClientIndex"
              }
            }
          },
          "required": true
        },
        "responses": {
          "202": {
            "description": "Request to create a secondary index has been accepted."
          },
          "403": {
            "description": "Not authorized to access the resource.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "400": {
            "description": "Invalid index creation parameters.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "409": {
            "description": "Index with the same name already exists, or equivalent index exists.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          }
        }
      }
    },
    "/v1/execute/scan/{namespace}": {
      "post": {
        "tags": [
          "Execute Operations"
        ],
        "summary": "Perform multiple operations in background scan/query.",
        "operationId": "executeScanNamespace_1",
        "parameters": [
          {
            "name": "namespace",
            "in": "path",
            "description": "Namespace for the record; equivalent to database name.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "expiration",
            "in": "query",
            "description": "Record expiration. Also known as ttl (time to live). Seconds record will live before being removed by the server.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "generation",
            "in": "query",
            "description": "Expected generation. Generation is the number of times a record has been modified (including creation) on the server.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "durableDelete",
            "in": "query",
            "description": "If the transaction results in a record deletion, leave a tombstone for the record.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "respondAllOps",
            "in": "query",
            "description": "For client operate(), return a result for every operation.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "commitLevel",
            "in": "query",
            "description": "Desired consistency guarantee when committing a transaction on the server.",
            "schema": {
              "type": "string",
              "enum": [
                "COMMIT_ALL",
                "COMMIT_MASTER"
              ]
            }
          },
          {
            "name": "generationPolicy",
            "in": "query",
            "description": "Qualify how to handle record writes based on record generation.",
            "schema": {
              "type": "string",
              "enum": [
                "NONE",
                "EXPECT_GEN_EQUAL",
                "EXPECT_GEN_GT"
              ]
            }
          },
          {
            "name": "recordExistsAction",
            "in": "query",
            "description": "How to handle the existence of the record. This is ignored for POST/PUT/UPDATE kvs methods.",
            "schema": {
              "type": "string",
              "enum": [
                "UPDATE",
                "UPDATE_ONLY",
                "REPLACE",
                "REPLACE_ONLY",
                "CREATE_ONLY"
              ]
            }
          },
          {
            "name": "sendKey",
            "in": "query",
            "description": "Send user defined key in addition to hash digest on both reads and writes.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "replica",
            "in": "query",
            "description": "Replica algorithm used to determine the target node for a single record command.",
            "schema": {
              "type": "string",
              "enum": [
                "MASTER",
                "MASTER_PROLES",
                "SEQUENCE",
                "PREFER_RACK",
                "RANDOM"
              ]
            }
          },
          {
            "name": "readModeSC",
            "in": "query",
            "description": "Read policy for SC (strong consistency) namespaces. Determines SC read consistency options.",
            "schema": {
              "type": "string",
              "enum": [
                "SESSION",
                "LINEARIZE",
                "ALLOW_REPLICA",
                "ALLOW_UNAVAILABLE"
              ]
            }
          },
          {
            "name": "readModeAP",
            "in": "query",
            "description": "Read policy for AP (availability) namespaces. How duplicates should be consulted in a read operation. Only makes a difference during migrations and only applicable in AP mode.",
            "schema": {
              "type": "string",
              "enum": [
                "ONE",
                "ALL"
              ]
            }
          },
          {
            "name": "totalTimeout",
            "in": "query",
            "description": "Total transaction timeout in milliseconds.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "socketTimeout",
            "in": "query",
            "description": "Socket idle timeout in milliseconds when processing a database command.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "sleepBetweenRetries",
            "in": "query",
            "description": "Milliseconds to sleep between retries.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "maxRetries",
            "in": "query",
            "description": "Maximum number of retries before aborting the current transaction.\nThe initial attempt is not counted as a retry.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "filterexp",
            "in": "query",
            "description": "Optional Filter Expression (introduced in Aerospike Database 5.2.0) in infix notation DSL.",
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "compress",
            "in": "query",
            "description": "Use zlib compression on command buffers sent to the server and responses received from the server when the buffer size is greater than 128 bytes.",
            "schema": {
              "type": "boolean"
            }
          }
        ],
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "type": "array",
                "description": "An array of operation objects specifying the operations to perform on the record.",
                "items": {
                  "$ref": "#/components/schemas/RestClientOperation"
                }
              }
            },
            "application/msgpack": {
              "schema": {
                "type": "array",
                "description": "An array of operation objects specifying the operations to perform on the record.",
                "items": {
                  "$ref": "#/components/schemas/RestClientOperation"
                }
              }
            }
          },
          "required": true
        },
        "responses": {
          "403": {
            "description": "Not authorized to access the resource.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "409": {
            "description": "Generation conflict.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "404": {
            "description": "Namespace or set does not exist.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "400": {
            "description": "Invalid parameters or request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "200": {
            "description": "Multiple operations in background scan/query run successfully.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientExecuteTask"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientExecuteTask"
                }
              }
            }
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          }
        },
        "deprecated": true
      }
    },
    "/v1/execute/scan/{namespace}/{set}": {
      "post": {
        "tags": [
          "Execute Operations"
        ],
        "summary": "Perform multiple operations in background scan/query.",
        "operationId": "executeScanNamespaceSet_1",
        "parameters": [
          {
            "name": "namespace",
            "in": "path",
            "description": "Namespace for the record; equivalent to database name.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "set",
            "in": "path",
            "description": "Set for the record; equivalent to database table.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "expiration",
            "in": "query",
            "description": "Record expiration. Also known as ttl (time to live). Seconds record will live before being removed by the server.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "generation",
            "in": "query",
            "description": "Expected generation. Generation is the number of times a record has been modified (including creation) on the server.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "durableDelete",
            "in": "query",
            "description": "If the transaction results in a record deletion, leave a tombstone for the record.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "respondAllOps",
            "in": "query",
            "description": "For client operate(), return a result for every operation.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "commitLevel",
            "in": "query",
            "description": "Desired consistency guarantee when committing a transaction on the server.",
            "schema": {
              "type": "string",
              "enum": [
                "COMMIT_ALL",
                "COMMIT_MASTER"
              ]
            }
          },
          {
            "name": "generationPolicy",
            "in": "query",
            "description": "Qualify how to handle record writes based on record generation.",
            "schema": {
              "type": "string",
              "enum": [
                "NONE",
                "EXPECT_GEN_EQUAL",
                "EXPECT_GEN_GT"
              ]
            }
          },
          {
            "name": "recordExistsAction",
            "in": "query",
            "description": "How to handle the existence of the record. This is ignored for POST/PUT/UPDATE kvs methods.",
            "schema": {
              "type": "string",
              "enum": [
                "UPDATE",
                "UPDATE_ONLY",
                "REPLACE",
                "REPLACE_ONLY",
                "CREATE_ONLY"
              ]
            }
          },
          {
            "name": "sendKey",
            "in": "query",
            "description": "Send user defined key in addition to hash digest on both reads and writes.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "replica",
            "in": "query",
            "description": "Replica algorithm used to determine the target node for a single record command.",
            "schema": {
              "type": "string",
              "enum": [
                "MASTER",
                "MASTER_PROLES",
                "SEQUENCE",
                "PREFER_RACK",
                "RANDOM"
              ]
            }
          },
          {
            "name": "readModeSC",
            "in": "query",
            "description": "Read policy for SC (strong consistency) namespaces. Determines SC read consistency options.",
            "schema": {
              "type": "string",
              "enum": [
                "SESSION",
                "LINEARIZE",
                "ALLOW_REPLICA",
                "ALLOW_UNAVAILABLE"
              ]
            }
          },
          {
            "name": "readModeAP",
            "in": "query",
            "description": "Read policy for AP (availability) namespaces. How duplicates should be consulted in a read operation. Only makes a difference during migrations and only applicable in AP mode.",
            "schema": {
              "type": "string",
              "enum": [
                "ONE",
                "ALL"
              ]
            }
          },
          {
            "name": "totalTimeout",
            "in": "query",
            "description": "Total transaction timeout in milliseconds.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "socketTimeout",
            "in": "query",
            "description": "Socket idle timeout in milliseconds when processing a database command.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "sleepBetweenRetries",
            "in": "query",
            "description": "Milliseconds to sleep between retries.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "maxRetries",
            "in": "query",
            "description": "Maximum number of retries before aborting the current transaction.\nThe initial attempt is not counted as a retry.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "filterexp",
            "in": "query",
            "description": "Optional Filter Expression (introduced in Aerospike Database 5.2.0) in infix notation DSL.",
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "compress",
            "in": "query",
            "description": "Use zlib compression on command buffers sent to the server and responses received from the server when the buffer size is greater than 128 bytes.",
            "schema": {
              "type": "boolean"
            }
          }
        ],
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "type": "array",
                "description": "An array of operation objects specifying the operations to perform on the record.",
                "items": {
                  "$ref": "#/components/schemas/RestClientOperation"
                }
              }
            },
            "application/msgpack": {
              "schema": {
                "type": "array",
                "description": "An array of operation objects specifying the operations to perform on the record.",
                "items": {
                  "$ref": "#/components/schemas/RestClientOperation"
                }
              }
            }
          },
          "required": true
        },
        "responses": {
          "403": {
            "description": "Not authorized to access the resource.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "409": {
            "description": "Generation conflict.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "404": {
            "description": "Namespace or set does not exist.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "400": {
            "description": "Invalid parameters or request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "200": {
            "description": "Multiple operations in background scan/query run successfully.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientExecuteTask"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientExecuteTask"
                }
              }
            }
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          }
        },
        "deprecated": true
      }
    },
    "/v1/batch": {
      "post": {
        "tags": [
          "Batch Operations"
        ],
        "summary": "Return multiple records from the server in a single request.",
        "operationId": "performBatchGet",
        "parameters": [
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "allowInline",
            "in": "query",
            "description": "Allow batch to be processed immediately in the server\u0027s receiving thread when the server deems it to be appropriate.  If false, the batch will always be processed in separate transaction threads.  This field is only relevant for the new batch index protocol.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "allowInlineSSD",
            "in": "query",
            "description": "Allow batch to be processed immediately in the server\u0027s receiving thread for SSD namespaces. If false, the batch will always be processed in separate service threads. Server versions \u003c 6.0 ignore this field.\nInline processing can introduce the possibility of unfairness because the server can process the entire batch before moving onto the next command.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "maxConcurrentThreads",
            "in": "query",
            "description": "Maximum number of concurrent synchronous batch request threads to server nodes at any point in time. If there are 16 node/namespace combinations requested and maxConcurrentThreads is 8, then batch requests will be made for 8 node/namespace combinations in parallel threads. When a request completes, a new request will be issued until all 16 requests are complete.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "respondAllKeys",
            "in": "query",
            "description": "Should all batch keys be attempted regardless of errors. This field is used on both the client and server. The client handles node specific errors and the server handles key specific errors.\nIf true, every batch key is attempted regardless of previous key specific errors. Node specific errors such as timeouts stop keys to that node, but keys directed at other nodes will continue to be processed.\nIf false, the server will stop the batch to its node on most key specific errors. The exceptions are com.aerospike.client.ResultCode.KEY_NOT_FOUND_ERROR and com.aerospike.client.ResultCode.FILTERED_OUT which never stop the batch. The client will stop the entire batch on node specific errors for sync commands that are run in sequence (maxConcurrentThreads \u003d\u003d 1). The client will not stop the entire batch for async commands or sync commands run in parallel.\nServer versions \u003c 6.0 do not support this field and treat this value as false for key specific errors.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "sendKey",
            "in": "query",
            "description": "Send user defined key in addition to hash digest on both reads and writes.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "replica",
            "in": "query",
            "description": "Replica algorithm used to determine the target node for a single record command.",
            "schema": {
              "type": "string",
              "enum": [
                "MASTER",
                "MASTER_PROLES",
                "SEQUENCE",
                "PREFER_RACK",
                "RANDOM"
              ]
            }
          },
          {
            "name": "readModeSC",
            "in": "query",
            "description": "Read policy for SC (strong consistency) namespaces. Determines SC read consistency options.",
            "schema": {
              "type": "string",
              "enum": [
                "SESSION",
                "LINEARIZE",
                "ALLOW_REPLICA",
                "ALLOW_UNAVAILABLE"
              ]
            }
          },
          {
            "name": "readModeAP",
            "in": "query",
            "description": "Read policy for AP (availability) namespaces. How duplicates should be consulted in a read operation. Only makes a difference during migrations and only applicable in AP mode.",
            "schema": {
              "type": "string",
              "enum": [
                "ONE",
                "ALL"
              ]
            }
          },
          {
            "name": "totalTimeout",
            "in": "query",
            "description": "Total transaction timeout in milliseconds.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "socketTimeout",
            "in": "query",
            "description": "Socket idle timeout in milliseconds when processing a database command.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "sleepBetweenRetries",
            "in": "query",
            "description": "Milliseconds to sleep between retries.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "maxRetries",
            "in": "query",
            "description": "Maximum number of retries before aborting the current transaction.\nThe initial attempt is not counted as a retry.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "filterexp",
            "in": "query",
            "description": "Optional Filter Expression (introduced in Aerospike Database 5.2.0) in infix notation DSL.",
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "compress",
            "in": "query",
            "description": "Use zlib compression on command buffers sent to the server and responses received from the server when the buffer size is greater than 128 bytes.",
            "schema": {
              "type": "boolean"
            }
          }
        ],
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "type": "array",
                "items": {
                  "oneOf": [
                    {
                      "$ref": "#/components/schemas/BatchDelete"
                    },
                    {
                      "$ref": "#/components/schemas/BatchRead"
                    },
                    {
                      "$ref": "#/components/schemas/BatchUDF"
                    },
                    {
                      "$ref": "#/components/schemas/BatchWrite"
                    }
                  ]
                }
              }
            },
            "application/msgpack": {
              "schema": {
                "type": "array",
                "items": {
                  "oneOf": [
                    {
                      "$ref": "#/components/schemas/BatchDelete"
                    },
                    {
                      "$ref": "#/components/schemas/BatchRead"
                    },
                    {
                      "$ref": "#/components/schemas/BatchUDF"
                    },
                    {
                      "$ref": "#/components/schemas/BatchWrite"
                    }
                  ]
                }
              }
            }
          },
          "required": true
        },
        "responses": {
          "404": {
            "description": "Non existent namespace used in one or more key.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "403": {
            "description": "Not authorized to access the resource.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "200": {
            "description": "Batch Operation completed successfully.",
            "content": {
              "application/json": {
                "schema": {
                  "type": "array",
                  "items": {
                    "$ref": "#/components/schemas/BatchRecordResponse"
                  }
                }
              },
              "application/msgpack": {
                "schema": {
                  "type": "array",
                  "items": {
                    "$ref": "#/components/schemas/BatchRecordResponse"
                  }
                }
              }
            }
          },
          "400": {
            "description": "Invalid parameters or request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          }
        }
      }
    },
    "/v1/admin/user": {
      "get": {
        "tags": [
          "Admin Operations"
        ],
        "summary": "Return a list of information about users.",
        "operationId": "getUsers",
        "parameters": [
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "Users information read successfully.",
            "content": {
              "application/json": {
                "schema": {
                  "type": "array",
                  "items": {
                    "$ref": "#/components/schemas/User"
                  }
                },
                "examples": {
                  "Users response example": {
                    "description": "Users response example",
                    "value": [
                      {
                        "name": "user1",
                        "roles": [
                          "sys-admin"
                        ],
                        "readInfo": [
                          0
                        ],
                        "writeInfo": [
                          0
                        ],
                        "connsInUse": 0
                      }
                    ]
                  }
                }
              },
              "application/msgpack": {
                "schema": {
                  "type": "array",
                  "items": {
                    "$ref": "#/components/schemas/User"
                  }
                },
                "examples": {
                  "Users response example": {
                    "description": "Users response example",
                    "value": [
                      {
                        "name": "user1",
                        "roles": [
                          "sys-admin"
                        ],
                        "readInfo": [
                          0
                        ],
                        "writeInfo": [
                          0
                        ],
                        "connsInUse": 0
                      }
                    ]
                  }
                }
              }
            }
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "403": {
            "description": "Not authorized to read user information.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          }
        }
      },
      "post": {
        "tags": [
          "Admin Operations"
        ],
        "summary": "Create a new user.",
        "operationId": "createUser",
        "parameters": [
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          }
        ],
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/RestClientUserModel"
              }
            },
            "application/msgpack": {
              "schema": {
                "$ref": "#/components/schemas/RestClientUserModel"
              }
            }
          },
          "required": true
        },
        "responses": {
          "202": {
            "description": "Request to create a user has been accepted."
          },
          "409": {
            "description": "User already exists.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "403": {
            "description": "Not authorized to create users.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "400": {
            "description": "Invalid user creation parameters.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          }
        }
      }
    },
    "/v1/admin/user/{user}/role": {
      "post": {
        "tags": [
          "Admin Operations"
        ],
        "summary": "Grant a set of roles to the specified user.",
        "operationId": "grantRoles",
        "parameters": [
          {
            "name": "user",
            "in": "path",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          }
        ],
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "type": "array",
                "items": {
                  "type": "string"
                }
              },
              "examples": {
                "List of roles request body example": {
                  "description": "List of roles request body example",
                  "value": [
                    "read-write",
                    "read-write-udf"
                  ]
                }
              }
            },
            "application/msgpack": {
              "schema": {
                "type": "array",
                "items": {
                  "type": "string"
                }
              },
              "examples": {
                "List of roles request body example": {
                  "description": "List of roles request body example",
                  "value": [
                    "read-write",
                    "read-write-udf"
                  ]
                }
              }
            }
          },
          "required": true
        },
        "responses": {
          "202": {
            "description": "Request to grant set of roles to a user has been accepted."
          },
          "400": {
            "description": "Invalid role parameters.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "403": {
            "description": "Not authorized to modify users.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "404": {
            "description": "Specified user not found.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          }
        }
      }
    },
    "/v1/admin/role": {
      "get": {
        "tags": [
          "Admin Operations"
        ],
        "summary": "Return a list of all roles registered with the Aerospike cluster.",
        "operationId": "getRoles",
        "parameters": [
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "Roles information read successfully.",
            "content": {
              "application/json": {
                "schema": {
                  "type": "array",
                  "items": {
                    "$ref": "#/components/schemas/RestClientRole"
                  }
                }
              },
              "application/msgpack": {
                "schema": {
                  "type": "array",
                  "items": {
                    "$ref": "#/components/schemas/RestClientRole"
                  }
                }
              }
            }
          },
          "403": {
            "description": "Not authorized to read role information.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          }
        }
      },
      "post": {
        "tags": [
          "Admin Operations"
        ],
        "summary": "Create a role on the Aerospike cluster.",
        "operationId": "createRole",
        "parameters": [
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          }
        ],
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/RestClientRole"
              }
            },
            "application/msgpack": {
              "schema": {
                "$ref": "#/components/schemas/RestClientRole"
              }
            }
          },
          "required": true
        },
        "responses": {
          "403": {
            "description": "Not authorized to create roles.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "400": {
            "description": "Invalid role creation parameters.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "202": {
            "description": "Request to create a role has been accepted."
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "409": {
            "description": "Role already exists.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          }
        }
      }
    },
    "/v1/admin/role/{name}/quota": {
      "post": {
        "tags": [
          "Admin Operations"
        ],
        "summary": "Set maximum reads/writes per second limits for a role.",
        "operationId": "setRoleQuotas",
        "parameters": [
          {
            "name": "name",
            "in": "path",
            "description": "The name of the role to which quotas will be set.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          }
        ],
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/RestClientRoleQuota"
              }
            },
            "application/msgpack": {
              "schema": {
                "$ref": "#/components/schemas/RestClientRoleQuota"
              }
            }
          },
          "required": true
        },
        "responses": {
          "403": {
            "description": "Not authorized to create roles.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "400": {
            "description": "Invalid role creation parameters.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "202": {
            "description": "Request to modify a role has been accepted."
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "409": {
            "description": "Role already exists.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          }
        }
      }
    },
    "/v1/admin/role/{name}/privilege": {
      "post": {
        "tags": [
          "Admin Operations"
        ],
        "summary": "Grant a list of privileges to a specific role.",
        "operationId": "grantPrivileges",
        "parameters": [
          {
            "name": "name",
            "in": "path",
            "description": "The name of the role to which privileges will be granted.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          }
        ],
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "type": "array",
                "items": {
                  "$ref": "#/components/schemas/RestClientPrivilege"
                }
              }
            },
            "application/msgpack": {
              "schema": {
                "type": "array",
                "items": {
                  "$ref": "#/components/schemas/RestClientPrivilege"
                }
              }
            }
          },
          "required": true
        },
        "responses": {
          "404": {
            "description": "Specified role not found.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "202": {
            "description": "Request to modify a role has been accepted."
          },
          "400": {
            "description": "Invalid privilege parameters.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "403": {
            "description": "Not authorized to modify roles.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          }
        }
      }
    },
    "/v1/admin/user/{user}": {
      "get": {
        "tags": [
          "Admin Operations"
        ],
        "summary": "Return information about a specific user.",
        "operationId": "getUser",
        "parameters": [
          {
            "name": "user",
            "in": "path",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          }
        ],
        "responses": {
          "404": {
            "description": "Specified user not found.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "200": {
            "description": "User information read successfully.",
            "content": {
              "application/json": {
                "examples": {
                  "User response example": {
                    "description": "User response example",
                    "value": {
                      "name": "user1",
                      "roles": [
                        "sys-admin"
                      ],
                      "readInfo": [
                        0
                      ],
                      "writeInfo": [
                        0
                      ],
                      "connsInUse": 0
                    }
                  }
                }
              },
              "application/msgpack": {
                "examples": {
                  "User response example": {
                    "description": "User response example",
                    "value": {
                      "name": "user1",
                      "roles": [
                        "sys-admin"
                      ],
                      "readInfo": [
                        0
                      ],
                      "writeInfo": [
                        0
                      ],
                      "connsInUse": 0
                    }
                  }
                }
              }
            }
          },
          "403": {
            "description": "Not authorized to read user information.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          }
        }
      },
      "delete": {
        "tags": [
          "Admin Operations"
        ],
        "summary": "Remove a user.",
        "operationId": "dropUser",
        "parameters": [
          {
            "name": "user",
            "in": "path",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          }
        ],
        "responses": {
          "404": {
            "description": "Specified user not found.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "403": {
            "description": "Not authorized to delete users.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "202": {
            "description": "Request to delete a user has been accepted."
          }
        }
      },
      "patch": {
        "tags": [
          "Admin Operations"
        ],
        "summary": "Change the password of the specified user.",
        "operationId": "changePassword",
        "parameters": [
          {
            "name": "user",
            "in": "path",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          }
        ],
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "type": "string"
              }
            },
            "application/msgpack": {
              "schema": {
                "type": "string"
              }
            }
          },
          "required": true
        },
        "responses": {
          "403": {
            "description": "Not authorized to modify users.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "404": {
            "description": "Specified user not found.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "202": {
            "description": "Request to change user\u0027s password has been accepted."
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          }
        }
      }
    },
    "/v1/admin/user/{user}/role/delete": {
      "patch": {
        "tags": [
          "Admin Operations"
        ],
        "summary": "Revoke a set of roles from the specified user.",
        "operationId": "revokeRoles",
        "parameters": [
          {
            "name": "user",
            "in": "path",
            "description": "The user from which to revoke roles",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          }
        ],
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "type": "array",
                "items": {
                  "type": "string"
                }
              },
              "examples": {
                "List of roles request body example": {
                  "description": "List of roles request body example",
                  "value": [
                    "read-write",
                    "read-write-udf"
                  ]
                }
              }
            },
            "application/msgpack": {
              "schema": {
                "type": "array",
                "items": {
                  "type": "string"
                }
              },
              "examples": {
                "List of roles request body example": {
                  "description": "List of roles request body example",
                  "value": [
                    "read-write",
                    "read-write-udf"
                  ]
                }
              }
            }
          },
          "required": true
        },
        "responses": {
          "202": {
            "description": "Request to revoke set of roles from a user has been accepted."
          },
          "400": {
            "description": "Invalid role parameters.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "403": {
            "description": "Not authorized to modify users.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "404": {
            "description": "Specified user not found.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          }
        }
      }
    },
    "/v1/admin/role/{name}/privilege/delete": {
      "patch": {
        "tags": [
          "Admin Operations"
        ],
        "summary": "Remove a list of privileges from a specific role.",
        "operationId": "revokePrivileges",
        "parameters": [
          {
            "name": "name",
            "in": "path",
            "description": "The name of the role from which privileges will be removed.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          }
        ],
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "type": "array",
                "items": {
                  "$ref": "#/components/schemas/RestClientPrivilege"
                }
              }
            },
            "application/msgpack": {
              "schema": {
                "type": "array",
                "items": {
                  "$ref": "#/components/schemas/RestClientPrivilege"
                }
              }
            }
          },
          "required": true
        },
        "responses": {
          "404": {
            "description": "Specified role not found.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "202": {
            "description": "Request to modify a role has been accepted."
          },
          "400": {
            "description": "Invalid privilege parameters.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "403": {
            "description": "Not authorized to modify roles.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          }
        }
      }
    },
    "/v2/execute/scan/status/{taskId}": {
      "get": {
        "tags": [
          "Execute Operations"
        ],
        "description": "Get status of background scan by task id.",
        "operationId": "executeScanStatus",
        "parameters": [
          {
            "name": "taskId",
            "in": "path",
            "description": "Background scan task id.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          }
        ],
        "responses": {
          "403": {
            "description": "Not authorized to access the resource.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "200": {
            "description": "Status of background scan read successfully.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientExecuteTaskStatus"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientExecuteTaskStatus"
                }
              }
            }
          },
          "400": {
            "description": "Invalid parameters or request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          }
        }
      }
    },
    "/v1/scan/{namespace}": {
      "get": {
        "tags": [
          "Scan Operations"
        ],
        "summary": "Return multiple records from the server in a scan request.",
        "operationId": "performScan",
        "parameters": [
          {
            "name": "namespace",
            "in": "path",
            "description": "Namespace for the record; equivalent to database name.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "maxRecords",
            "in": "query",
            "description": "Number of records to return. Required for pagination. This number is divided by the number of nodes involved in the query. The actual number of records returned may be less than maxRecords if node record counts are small and unbalanced across nodes.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "recordsPerSecond",
            "in": "query",
            "description": "Limit returned records per second (rps) rate for each server.\nDo not apply rps limit if recordsPerSecond is zero.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "maxConcurrentNodes",
            "in": "query",
            "description": "Maximum number of concurrent requests to server nodes at any point in time. If there are 16 nodes in the cluster and maxConcurrentNodes is 8, then scan requests will be made to 8 nodes in parallel.  When a scan completes, a new scan request will be issued until all 16 nodes have been scanned.\nThis field is only relevant when concurrentNodes is true.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "concurrentNodes",
            "in": "query",
            "description": "Should scan requests be issued in parallel.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "includeBinData",
            "in": "query",
            "description": "Should bin data be retrieved. If false, only record digests (and user keys if stored on the server) are retrieved.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "sendKey",
            "in": "query",
            "description": "Send user defined key in addition to hash digest on both reads and writes.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "replica",
            "in": "query",
            "description": "Replica algorithm used to determine the target node for a single record command.",
            "schema": {
              "type": "string",
              "enum": [
                "MASTER",
                "MASTER_PROLES",
                "SEQUENCE",
                "PREFER_RACK",
                "RANDOM"
              ]
            }
          },
          {
            "name": "readModeSC",
            "in": "query",
            "description": "Read policy for SC (strong consistency) namespaces. Determines SC read consistency options.",
            "schema": {
              "type": "string",
              "enum": [
                "SESSION",
                "LINEARIZE",
                "ALLOW_REPLICA",
                "ALLOW_UNAVAILABLE"
              ]
            }
          },
          {
            "name": "readModeAP",
            "in": "query",
            "description": "Read policy for AP (availability) namespaces. How duplicates should be consulted in a read operation. Only makes a difference during migrations and only applicable in AP mode.",
            "schema": {
              "type": "string",
              "enum": [
                "ONE",
                "ALL"
              ]
            }
          },
          {
            "name": "totalTimeout",
            "in": "query",
            "description": "Total transaction timeout in milliseconds.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "socketTimeout",
            "in": "query",
            "description": "Socket idle timeout in milliseconds when processing a database command.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "sleepBetweenRetries",
            "in": "query",
            "description": "Milliseconds to sleep between retries.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "maxRetries",
            "in": "query",
            "description": "Maximum number of retries before aborting the current transaction.\nThe initial attempt is not counted as a retry.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "filterexp",
            "in": "query",
            "description": "Optional Filter Expression (introduced in Aerospike Database 5.2.0) in infix notation DSL.",
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "compress",
            "in": "query",
            "description": "Use zlib compression on command buffers sent to the server and responses received from the server when the buffer size is greater than 128 bytes.",
            "schema": {
              "type": "boolean"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "Scan multiple records successfully.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientScanResponse"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientScanResponse"
                }
              }
            }
          },
          "403": {
            "description": "Not authorized to access the resource.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "404": {
            "description": "Namespace or set does not exist.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "400": {
            "description": "Invalid parameters or request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          }
        }
      }
    },
    "/v1/scan/{namespace}/{set}": {
      "get": {
        "tags": [
          "Scan Operations"
        ],
        "summary": "Return multiple records from the server in a scan request.",
        "operationId": "performScan_1",
        "parameters": [
          {
            "name": "namespace",
            "in": "path",
            "description": "Namespace for the record; equivalent to database name.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "set",
            "in": "path",
            "description": "Set for the record; equivalent to database table.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "maxRecords",
            "in": "query",
            "description": "Number of records to return. Required for pagination. This number is divided by the number of nodes involved in the query. The actual number of records returned may be less than maxRecords if node record counts are small and unbalanced across nodes.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "recordsPerSecond",
            "in": "query",
            "description": "Limit returned records per second (rps) rate for each server.\nDo not apply rps limit if recordsPerSecond is zero.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "maxConcurrentNodes",
            "in": "query",
            "description": "Maximum number of concurrent requests to server nodes at any point in time. If there are 16 nodes in the cluster and maxConcurrentNodes is 8, then scan requests will be made to 8 nodes in parallel.  When a scan completes, a new scan request will be issued until all 16 nodes have been scanned.\nThis field is only relevant when concurrentNodes is true.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "concurrentNodes",
            "in": "query",
            "description": "Should scan requests be issued in parallel.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "includeBinData",
            "in": "query",
            "description": "Should bin data be retrieved. If false, only record digests (and user keys if stored on the server) are retrieved.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "sendKey",
            "in": "query",
            "description": "Send user defined key in addition to hash digest on both reads and writes.",
            "schema": {
              "type": "boolean"
            }
          },
          {
            "name": "replica",
            "in": "query",
            "description": "Replica algorithm used to determine the target node for a single record command.",
            "schema": {
              "type": "string",
              "enum": [
                "MASTER",
                "MASTER_PROLES",
                "SEQUENCE",
                "PREFER_RACK",
                "RANDOM"
              ]
            }
          },
          {
            "name": "readModeSC",
            "in": "query",
            "description": "Read policy for SC (strong consistency) namespaces. Determines SC read consistency options.",
            "schema": {
              "type": "string",
              "enum": [
                "SESSION",
                "LINEARIZE",
                "ALLOW_REPLICA",
                "ALLOW_UNAVAILABLE"
              ]
            }
          },
          {
            "name": "readModeAP",
            "in": "query",
            "description": "Read policy for AP (availability) namespaces. How duplicates should be consulted in a read operation. Only makes a difference during migrations and only applicable in AP mode.",
            "schema": {
              "type": "string",
              "enum": [
                "ONE",
                "ALL"
              ]
            }
          },
          {
            "name": "totalTimeout",
            "in": "query",
            "description": "Total transaction timeout in milliseconds.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "socketTimeout",
            "in": "query",
            "description": "Socket idle timeout in milliseconds when processing a database command.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "sleepBetweenRetries",
            "in": "query",
            "description": "Milliseconds to sleep between retries.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "maxRetries",
            "in": "query",
            "description": "Maximum number of retries before aborting the current transaction.\nThe initial attempt is not counted as a retry.",
            "schema": {
              "type": "integer"
            }
          },
          {
            "name": "filterexp",
            "in": "query",
            "description": "Optional Filter Expression (introduced in Aerospike Database 5.2.0) in infix notation DSL.",
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "compress",
            "in": "query",
            "description": "Use zlib compression on command buffers sent to the server and responses received from the server when the buffer size is greater than 128 bytes.",
            "schema": {
              "type": "boolean"
            }
          }
        ],
        "responses": {
          "200": {
            "description": "Scan multiple records successfully.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientScanResponse"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientScanResponse"
                }
              }
            }
          },
          "403": {
            "description": "Not authorized to access the resource.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "404": {
            "description": "Namespace or set does not exist.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "400": {
            "description": "Invalid parameters or request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          }
        }
      }
    },
    "/v1/index/{namespace}/{name}": {
      "get": {
        "tags": [
          "Secondary Index methods"
        ],
        "summary": "Get Information about a single secondary index.",
        "operationId": "getIndexStats",
        "parameters": [
          {
            "name": "namespace",
            "in": "path",
            "description": "The namespace containing the index",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "name",
            "in": "path",
            "description": "The name of the index",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "timeout",
            "in": "query",
            "description": "Info command socket timeout in milliseconds.",
            "schema": {
              "type": "integer"
            }
          }
        ],
        "responses": {
          "403": {
            "description": "Not authorized to access the resource.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "404": {
            "description": "Specified Index does not exist.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "200": {
            "description": "Information about secondary index read successfully.",
            "content": {
              "application/json": {
                "examples": {
                  "Secondary index response example": {
                    "description": "Secondary index response example",
                    "value": {
                      "loadtime": 0,
                      "delete_success": 0,
                      "keys": 0,
                      "nbtr_memory_used": 0,
                      "delete_error": 0,
                      "load_pct": 100,
                      "stat_gc_recs": 0,
                      "query_basic_abort": 0,
                      "histogram": false,
                      "entries": 0,
                      "query_basic_error": 0,
                      "query_basic_complete": 0,
                      "ibtr_memory_used": 18432,
                      "write_error": 0,
                      "query_basic_avg_rec_count": 0,
                      "write_success": 0
                    }
                  }
                }
              },
              "application/msgpack": {
                "examples": {
                  "Secondary index response example": {
                    "description": "Secondary index response example",
                    "value": {
                      "loadtime": 0,
                      "delete_success": 0,
                      "keys": 0,
                      "nbtr_memory_used": 0,
                      "delete_error": 0,
                      "load_pct": 100,
                      "stat_gc_recs": 0,
                      "query_basic_abort": 0,
                      "histogram": false,
                      "entries": 0,
                      "query_basic_error": 0,
                      "query_basic_complete": 0,
                      "ibtr_memory_used": 18432,
                      "write_error": 0,
                      "query_basic_avg_rec_count": 0,
                      "write_success": 0
                    }
                  }
                }
              }
            }
          }
        }
      },
      "delete": {
        "tags": [
          "Secondary Index methods"
        ],
        "summary": "Remove a secondary Index",
        "operationId": "dropIndex",
        "parameters": [
          {
            "name": "namespace",
            "in": "path",
            "description": "The namespace containing the index",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "name",
            "in": "path",
            "description": "The name of the index",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          }
        ],
        "responses": {
          "202": {
            "description": "Request to remove a secondary index has been accepted."
          },
          "403": {
            "description": "Not authorized to access the resource.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "404": {
            "description": "Specified Index does not exist.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          }
        }
      }
    },
    "/v1/execute/scan/status/{taskId}": {
      "get": {
        "tags": [
          "Execute Operations"
        ],
        "description": "Get status of background scan by task id.",
        "operationId": "executeScanStatus_1",
        "parameters": [
          {
            "name": "taskId",
            "in": "path",
            "description": "Background scan task id.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          }
        ],
        "responses": {
          "403": {
            "description": "Not authorized to access the resource.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "200": {
            "description": "Status of background scan read successfully.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientExecuteTaskStatus"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientExecuteTaskStatus"
                }
              }
            }
          },
          "400": {
            "description": "Invalid parameters or request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          }
        },
        "deprecated": true
      }
    },
    "/v1/cluster": {
      "get": {
        "tags": [
          "Cluster information operations"
        ],
        "summary": "Return an object containing information about the Aerospike cluster.",
        "operationId": "getClusterInfo",
        "parameters": [
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          }
        ],
        "responses": {
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "200": {
            "description": "Cluster information read successfully.",
            "content": {
              "application/json": {
                "examples": {
                  "Cluster info response example": {
                    "description": "Cluster info response example",
                    "value": {
                      "nodes": [
                        {
                          "name": "BB9020011AC4202"
                        }
                      ],
                      "namespaces": [
                        {
                          "sets": [
                            {
                              "objectCount": 1,
                              "name": "junit"
                            },
                            {
                              "objectCount": 0,
                              "name": "msgpack"
                            },
                            {
                              "objectCount": 0,
                              "name": "executeSet"
                            },
                            {
                              "objectCount": 0,
                              "name": "scanSet"
                            },
                            {
                              "objectCount": 0,
                              "name": "auth"
                            },
                            {
                              "objectCount": 0,
                              "name": "idxDemo"
                            },
                            {
                              "objectCount": 0,
                              "name": "truncate"
                            },
                            {
                              "objectCount": 0,
                              "name": "otherset"
                            }
                          ],
                          "name": "test"
                        }
                      ]
                    }
                  }
                }
              },
              "application/msgpack": {
                "examples": {
                  "Cluster info response example": {
                    "description": "Cluster info response example",
                    "value": {
                      "nodes": [
                        {
                          "name": "BB9020011AC4202"
                        }
                      ],
                      "namespaces": [
                        {
                          "sets": [
                            {
                              "objectCount": 1,
                              "name": "junit"
                            },
                            {
                              "objectCount": 0,
                              "name": "msgpack"
                            },
                            {
                              "objectCount": 0,
                              "name": "executeSet"
                            },
                            {
                              "objectCount": 0,
                              "name": "scanSet"
                            },
                            {
                              "objectCount": 0,
                              "name": "auth"
                            },
                            {
                              "objectCount": 0,
                              "name": "idxDemo"
                            },
                            {
                              "objectCount": 0,
                              "name": "truncate"
                            },
                            {
                              "objectCount": 0,
                              "name": "otherset"
                            }
                          ],
                          "name": "test"
                        }
                      ]
                    }
                  }
                }
              }
            }
          }
        }
      }
    },
    "/v1/admin/role/{name}": {
      "get": {
        "tags": [
          "Admin Operations"
        ],
        "summary": "Get information about a specific role.",
        "operationId": "getRole",
        "parameters": [
          {
            "name": "name",
            "in": "path",
            "description": "The name of the role whose information should be retrieved.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          }
        ],
        "responses": {
          "404": {
            "description": "Specified role not found.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "403": {
            "description": "Not authorized to read role information.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "200": {
            "description": "Role read successfully.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientRole"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientRole"
                }
              }
            }
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          }
        }
      },
      "delete": {
        "tags": [
          "Admin Operations"
        ],
        "summary": "Remove a specific role from the Aerospike cluster.",
        "operationId": "dropRole",
        "parameters": [
          {
            "name": "name",
            "in": "path",
            "description": "The name of the role to remove.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          }
        ],
        "responses": {
          "404": {
            "description": "Specified role not found.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "403": {
            "description": "Not authorized to remove roles.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "202": {
            "description": "Request to remove a role has been accepted."
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          }
        }
      }
    },
    "/v1/truncate/{namespace}": {
      "delete": {
        "tags": [
          "Truncate Operations"
        ],
        "summary": "Truncate records in a specified namespace.",
        "operationId": "truncateNamespace",
        "parameters": [
          {
            "name": "namespace",
            "in": "path",
            "description": "The namespace whose records will be truncated.",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "date",
            "in": "query",
            "description": "All records last updated before this date/time will be truncated. If not specified, all records will be truncated.\n This is a string representation of a date time utilizing the ISO-8601 extended offset date-time format. example: 2019-12-03T10:15:30+01:00",
            "required": false,
            "schema": {
              "type": "string"
            },
            "example": "2019-12-03T10:15:30+01:00"
          },
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          }
        ],
        "responses": {
          "403": {
            "description": "Not authorized to access the resource.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "202": {
            "description": "Request to truncate record has been accepted."
          },
          "400": {
            "description": "Invalid parameters or request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          }
        }
      }
    },
    "/v1/truncate/{namespace}/{set}": {
      "delete": {
        "tags": [
          "Truncate Operations"
        ],
        "summary": "Truncate records in a specified namespace and set.",
        "operationId": "truncateSet",
        "parameters": [
          {
            "name": "namespace",
            "in": "path",
            "description": "The namespace whose records will be truncated",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "set",
            "in": "path",
            "description": "The set, in the specified namespace, whose records will be truncated",
            "required": true,
            "schema": {
              "type": "string"
            }
          },
          {
            "name": "date",
            "in": "query",
            "description": "All records last updated before this date/time will be truncated. If not specified, all records will be truncated.\n This is a string representation of a date time utilizing the ISO-8601 extended offset date-time format. example: 2019-12-03T10:15:30+01:00",
            "required": false,
            "schema": {
              "type": "string"
            },
            "example": "2019-12-03T10:15:30+01:00"
          },
          {
            "name": "Authorization",
            "in": "header",
            "required": false,
            "schema": {
              "type": "string"
            }
          }
        ],
        "responses": {
          "403": {
            "description": "Not authorized to access the resource.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "202": {
            "description": "Request to truncate record has been accepted."
          },
          "400": {
            "description": "Invalid parameters or request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          },
          "500": {
            "description": "REST Gateway encountered an error while processing the request.",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              },
              "application/msgpack": {
                "schema": {
                  "$ref": "#/components/schemas/RestClientError"
                }
              }
            }
          }
        }
      }
    }
  },
  "components": {
    "schemas": {
      "RestClientError": {
        "required": [
          "inDoubt"
        ],
        "type": "object",
        "properties": {
          "message": {
            "type": "string",
            "description": "A message describing the cause of the error.",
            "example": "Error Message"
          },
          "inDoubt": {
            "type": "boolean",
            "description": "A boolean specifying whether it was possible that the operation succeeded. This is only included if true.",
            "example": false
          },
          "internalErrorCode": {
            "type": "integer",
            "description": "An internal error code for diagnostic purposes. This may be null",
            "format": "int32",
            "example": -3
          }
        }
      },
      "AddOperation": {
        "type": "object",
        "description": "Increment the value of an item in the specified `binName` by the value of `incr`",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/Operation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/Operation"
          },
          {
            "type": "object",
            "properties": {
              "binName": {
                "type": "string",
                "writeOnly": true
              },
              "incr": {
                "type": "number",
                "writeOnly": true
              }
            }
          }
        ]
      },
      "AppendOperation": {
        "type": "object",
        "description": "Append a `value` to the item in the specified `binName`",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/Operation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/Operation"
          },
          {
            "type": "object",
            "properties": {
              "binName": {
                "type": "string",
                "writeOnly": true
              },
              "value": {
                "type": "string",
                "writeOnly": true
              }
            }
          }
        ]
      },
      "BitAddOperation": {
        "type": "object",
        "description": "Server adds value to byte[] bin starting at bitOffset for bitSize. BitSize must be \u003c\u003d 64. Signed indicates if bits should be treated as a signed number. If add overflows/underflows, BitOverflowAction is used. Server does not return a value.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/operation/BitOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/BitOperation"
          },
          {
            "type": "object",
            "properties": {
              "bitOffset": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              },
              "bitSize": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              },
              "value": {
                "type": "integer",
                "format": "int64",
                "writeOnly": true
              },
              "signed": {
                "type": "boolean"
              },
              "action": {
                "type": "string",
                "default": "FAIL",
                "enum": [
                  "FAIL",
                  "SATURATE",
                  "WRAP"
                ]
              }
            }
          }
        ]
      },
      "BitAndOperation": {
        "type": "object",
        "description": "Server performs bitwise \"and\" on value and byte[] bin at bitOffset for bitSize.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/operation/BitOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/BitOperation"
          },
          {
            "type": "object",
            "properties": {
              "bitOffset": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              },
              "bitSize": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              },
              "value": {
                "type": "array",
                "writeOnly": true,
                "items": {
                  "type": "string",
                  "format": "byte"
                }
              }
            }
          }
        ]
      },
      "BitCountOperation": {
        "type": "object",
        "description": "Server returns integer count of set bits from byte[] bin starting at bitOffset for bitSize.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/operation/BitOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/BitOperation"
          },
          {
            "type": "object",
            "properties": {
              "bitOffset": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              },
              "bitSize": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              }
            }
          }
        ]
      },
      "BitGetIntOperation": {
        "type": "object",
        "description": "Server returns integer from byte[] bin starting at bitOffset for bitSize. Signed indicates if bits should be treated as a signed number.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/operation/BitOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/BitOperation"
          },
          {
            "type": "object",
            "properties": {
              "bitOffset": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              },
              "bitSize": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              },
              "signed": {
                "type": "boolean",
                "writeOnly": true
              }
            }
          }
        ]
      },
      "BitGetOperation": {
        "type": "object",
        "description": "Server returns bits from byte[] bin starting at bitOffset for bitSize.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/operation/BitOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/BitOperation"
          },
          {
            "type": "object",
            "properties": {
              "bitOffset": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              },
              "bitSize": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              }
            }
          }
        ]
      },
      "BitInsertOperation": {
        "type": "object",
        "description": "Server inserts value bytes into byte[] bin at byteOffset. Server does not return a value.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/operation/BitOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/BitOperation"
          },
          {
            "type": "object",
            "properties": {
              "byteOffset": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              },
              "value": {
                "type": "array",
                "writeOnly": true,
                "items": {
                  "type": "string",
                  "format": "byte"
                }
              }
            }
          }
        ]
      },
      "BitLScanOperation": {
        "type": "object",
        "description": "Server returns integer bit offset of the first specified value bit in byte[] bin starting at bitOffset for bitSize.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/operation/BitOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/BitOperation"
          },
          {
            "type": "object",
            "properties": {
              "bitOffset": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              },
              "bitSize": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              },
              "value": {
                "type": "boolean",
                "writeOnly": true
              }
            }
          }
        ]
      },
      "BitLShiftOperation": {
        "type": "object",
        "description": "Server shifts left byte[] bin starting at bitOffset for bitSize. Server does not return a value.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/operation/BitOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/BitOperation"
          },
          {
            "type": "object",
            "properties": {
              "bitOffset": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              },
              "bitSize": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              },
              "shift": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              }
            }
          }
        ]
      },
      "BitNotOperation": {
        "type": "object",
        "description": "Server negates byte[] bin starting at bitOffset for bitSize. Server does not return a value.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/operation/BitOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/BitOperation"
          },
          {
            "type": "object",
            "properties": {
              "bitOffset": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              },
              "bitSize": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              }
            }
          }
        ]
      },
      "BitOperation": {
        "required": [
          "type"
        ],
        "type": "object",
        "description": "The base type for describing all bit operations. Should not be used directly.",
        "discriminator": {
          "propertyName": "type"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/Operation"
          },
          {
            "type": "object",
            "properties": {
              "binName": {
                "type": "string",
                "writeOnly": true
              },
              "type": {
                "type": "string"
              }
            }
          }
        ],
        "oneOf": [
          {
            "$ref": "#/components/schemas/BitResizeOperation"
          },
          {
            "$ref": "#/components/schemas/BitInsertOperation"
          },
          {
            "$ref": "#/components/schemas/BitRemoveOperation"
          },
          {
            "$ref": "#/components/schemas/BitSetOperation"
          },
          {
            "$ref": "#/components/schemas/BitOrOperation"
          },
          {
            "$ref": "#/components/schemas/BitXOrOperation"
          },
          {
            "$ref": "#/components/schemas/BitAndOperation"
          },
          {
            "$ref": "#/components/schemas/BitNotOperation"
          },
          {
            "$ref": "#/components/schemas/BitLShiftOperation"
          },
          {
            "$ref": "#/components/schemas/BitRShiftOperation"
          },
          {
            "$ref": "#/components/schemas/BitAddOperation"
          },
          {
            "$ref": "#/components/schemas/BitSubtractOperation"
          },
          {
            "$ref": "#/components/schemas/BitGetOperation"
          },
          {
            "$ref": "#/components/schemas/BitCountOperation"
          },
          {
            "$ref": "#/components/schemas/BitLScanOperation"
          },
          {
            "$ref": "#/components/schemas/BitRScanOperation"
          },
          {
            "$ref": "#/components/schemas/BitSetIntOperation"
          },
          {
            "$ref": "#/components/schemas/BitGetIntOperation"
          }
        ]
      },
      "BitOrOperation": {
        "type": "object",
        "description": "Server performs bitwise \"or\" on value and byte[] bin at bitOffset for bitSize. Server does not return a value.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/operation/BitOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/BitOperation"
          },
          {
            "type": "object",
            "properties": {
              "bitOffset": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              },
              "bitSize": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              },
              "value": {
                "type": "array",
                "writeOnly": true,
                "items": {
                  "type": "string",
                  "format": "byte"
                }
              }
            }
          }
        ]
      },
      "BitRScanOperation": {
        "type": "object",
        "description": "Server returns integer bit offset of the last specified value bit in byte[] bin starting at bitOffset for bitSize.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/operation/BitOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/BitOperation"
          },
          {
            "type": "object",
            "properties": {
              "bitOffset": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              },
              "bitSize": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              },
              "value": {
                "type": "boolean",
                "writeOnly": true
              }
            }
          }
        ]
      },
      "BitRShiftOperation": {
        "type": "object",
        "description": "Server shifts right byte[] bin starting at bitOffset for bitSize. Server does not return a value.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/operation/BitOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/BitOperation"
          },
          {
            "type": "object",
            "properties": {
              "bitOffset": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              },
              "bitSize": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              },
              "shift": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              }
            }
          }
        ]
      },
      "BitRemoveOperation": {
        "type": "object",
        "description": "Server removes bytes from byte[] bin at byteOffset for byteSize. Server does not return a value.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/operation/BitOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/BitOperation"
          },
          {
            "type": "object",
            "properties": {
              "byteOffset": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              },
              "byteSize": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              }
            }
          }
        ]
      },
      "BitResizeOperation": {
        "type": "object",
        "description": "Server resizes byte[] to byteSize according to resizeFlags (See BitResizeFlags). Server does not return a value.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/operation/BitOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/BitOperation"
          },
          {
            "type": "object",
            "properties": {
              "byteSize": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              },
              "resizeFlags": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              }
            }
          }
        ]
      },
      "BitSetIntOperation": {
        "type": "object",
        "description": "Server sets value to byte[] bin starting at bitOffset for bitSize. Size must be \u003c\u003d 64. Server does not return a value.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/operation/BitOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/BitOperation"
          },
          {
            "type": "object",
            "properties": {
              "bitOffset": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              },
              "bitSize": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              },
              "value": {
                "type": "integer",
                "format": "int64",
                "writeOnly": true
              }
            }
          }
        ]
      },
      "BitSetOperation": {
        "type": "object",
        "description": "Server sets value on byte[] bin at bitOffset for bitSize. Server does not return a value.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/operation/BitOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/BitOperation"
          },
          {
            "type": "object",
            "properties": {
              "bitOffset": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              },
              "bitSize": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              },
              "value": {
                "type": "array",
                "writeOnly": true,
                "items": {
                  "type": "string",
                  "format": "byte"
                }
              }
            }
          }
        ]
      },
      "BitSubtractOperation": {
        "type": "object",
        "description": "Server subtracts value from byte[] bin starting at bitOffset for bitSize. BitSize must be \u003c\u003d 64. Signed indicates if bits should be treated as a signed number. If add overflows/underflows, BitOverflowAction is used. Server does not return a value.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/operation/BitOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/BitOperation"
          },
          {
            "type": "object",
            "properties": {
              "bitOffset": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              },
              "bitSize": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              },
              "value": {
                "type": "integer",
                "format": "int64",
                "writeOnly": true
              },
              "signed": {
                "type": "boolean"
              },
              "action": {
                "type": "string",
                "default": "FAIL",
                "enum": [
                  "FAIL",
                  "SATURATE",
                  "WRAP"
                ]
              }
            }
          }
        ]
      },
      "BitXOrOperation": {
        "type": "object",
        "description": "Server performs bitwise \"xor\" on value and byte[] bin at bitOffset for bitSize.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/operation/BitOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/BitOperation"
          },
          {
            "type": "object",
            "properties": {
              "bitOffset": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              },
              "bitSize": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              },
              "value": {
                "type": "array",
                "writeOnly": true,
                "items": {
                  "type": "string",
                  "format": "byte"
                }
              }
            }
          }
        ]
      },
      "CTX": {
        "required": [
          "type"
        ],
        "type": "object",
        "properties": {
          "type": {
            "type": "string"
          }
        },
        "description": "The base type for describing a nested CDT context. Identifies the location of nested list/map to apply the operation.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/CTX.html"
        },
        "discriminator": {
          "propertyName": "type"
        },
        "oneOf": [
          {
            "$ref": "#/components/schemas/ListIndexCTX"
          },
          {
            "$ref": "#/components/schemas/ListRankCTX"
          },
          {
            "$ref": "#/components/schemas/ListValueCTX"
          },
          {
            "$ref": "#/components/schemas/MapIndexCTX"
          },
          {
            "$ref": "#/components/schemas/MapRankCTX"
          },
          {
            "$ref": "#/components/schemas/MapKeyCTX"
          },
          {
            "$ref": "#/components/schemas/MapValueCTX"
          },
          {
            "$ref": "#/components/schemas/MapKeyCreateCTX"
          },
          {
            "$ref": "#/components/schemas/ListIndexCreateCTX"
          }
        ]
      },
      "DeleteOperation": {
        "type": "object",
        "description": "Delete a record.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/Operation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/Operation"
          }
        ]
      },
      "GetHeaderOperation": {
        "type": "object",
        "description": "Return metadata about a record.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/Operation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/Operation"
          }
        ]
      },
      "GetOperation": {
        "type": "object",
        "description": "Return the contents of a record",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/Operation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/Operation"
          },
          {
            "type": "object",
            "properties": {
              "binName": {
                "type": "string",
                "writeOnly": true
              }
            }
          }
        ]
      },
      "HLLAddOperation": {
        "type": "object",
        "description": "Server adds values to HLL set. If HLL bin does not exist, use indexBitCount and optionally minHashBitCount to create HLL bin. Server returns number of entries that caused HLL to update a register.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/operation/HLLOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/HLLOperation"
          },
          {
            "type": "object",
            "properties": {
              "binName": {
                "type": "string",
                "writeOnly": true
              },
              "values": {
                "type": "array",
                "writeOnly": true,
                "items": {
                  "type": "object"
                }
              },
              "indexBitCount": {
                "type": "integer",
                "format": "int32"
              },
              "minHashBitCount": {
                "type": "integer",
                "format": "int32"
              }
            }
          }
        ]
      },
      "HLLDescribeOperation": {
        "type": "object",
        "description": "Server returns indexBitCount and minHashBitCount used to create HLL bin in a list of longs. The list size is 2.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/operation/HLLOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/HLLOperation"
          },
          {
            "type": "object",
            "properties": {
              "binName": {
                "type": "string",
                "writeOnly": true
              }
            }
          }
        ]
      },
      "HLLFoldOperation": {
        "type": "object",
        "description": "Servers folds indexBitCount to the specified value. This can only be applied when minHashBitCount on the HLL bin is 0. Server does not return a value.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/operation/HLLOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/HLLOperation"
          },
          {
            "type": "object",
            "properties": {
              "binName": {
                "type": "string",
                "writeOnly": true
              },
              "indexBitCount": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              }
            }
          }
        ]
      },
      "HLLGetCountOperation": {
        "type": "object",
        "description": "Server returns estimated number of elements in the HLL bin.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/operation/HLLOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/HLLOperation"
          },
          {
            "type": "object",
            "properties": {
              "binName": {
                "type": "string",
                "writeOnly": true
              }
            }
          }
        ]
      },
      "HLLGetIntersectionCountOperation": {
        "type": "object",
        "description": "Server returns estimated number of elements that would be contained by the intersection of these HLL objects.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/operation/HLLOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/HLLOperation"
          },
          {
            "type": "object",
            "properties": {
              "binName": {
                "type": "string",
                "writeOnly": true
              },
              "values": {
                "type": "array",
                "writeOnly": true,
                "items": {
                  "type": "array",
                  "items": {
                    "type": "string",
                    "format": "byte"
                  }
                }
              }
            }
          }
        ]
      },
      "HLLGetSimilarityOperation": {
        "type": "object",
        "description": "Server returns estimated similarity of these HLL objects. Return type is a double.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/operation/HLLOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/HLLOperation"
          },
          {
            "type": "object",
            "properties": {
              "binName": {
                "type": "string",
                "writeOnly": true
              },
              "values": {
                "type": "array",
                "writeOnly": true,
                "items": {
                  "type": "array",
                  "items": {
                    "type": "string",
                    "format": "byte"
                  }
                }
              }
            }
          }
        ]
      },
      "HLLGetUnionCountOperation": {
        "type": "object",
        "description": "Server returns estimated number of elements that would be contained by the union of these HLL objects.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/operation/HLLOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/HLLOperation"
          },
          {
            "type": "object",
            "properties": {
              "binName": {
                "type": "string",
                "writeOnly": true
              },
              "values": {
                "type": "array",
                "writeOnly": true,
                "items": {
                  "type": "array",
                  "items": {
                    "type": "string",
                    "format": "byte"
                  }
                }
              }
            }
          }
        ]
      },
      "HLLGetUnionOperation": {
        "type": "object",
        "description": "Server returns an HLL object that is the union of all specified HLL objects in the list with the HLL bin.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/operation/HLLOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/HLLOperation"
          },
          {
            "type": "object",
            "properties": {
              "binName": {
                "type": "string",
                "writeOnly": true
              },
              "values": {
                "type": "array",
                "writeOnly": true,
                "items": {
                  "type": "array",
                  "items": {
                    "type": "string",
                    "format": "byte"
                  }
                }
              }
            }
          }
        ]
      },
      "HLLInitOperation": {
        "type": "object",
        "description": "Server creates a new HLL or resets an existing HLL. Server does not return a value.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/operation/HLLOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/HLLOperation"
          },
          {
            "type": "object",
            "properties": {
              "binName": {
                "type": "string",
                "writeOnly": true
              },
              "indexBitCount": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              },
              "minHashBitCount": {
                "type": "integer",
                "format": "int32"
              }
            }
          }
        ]
      },
      "HLLOperation": {
        "required": [
          "type"
        ],
        "type": "object",
        "description": "The base type for describing all HLL operations. Should not be used directly.",
        "discriminator": {
          "propertyName": "type"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/Operation"
          },
          {
            "type": "object",
            "properties": {
              "type": {
                "type": "string"
              }
            }
          }
        ],
        "oneOf": [
          {
            "$ref": "#/components/schemas/HLLInitOperation"
          },
          {
            "$ref": "#/components/schemas/HLLAddOperation"
          },
          {
            "$ref": "#/components/schemas/HLLSetUnionOperation"
          },
          {
            "$ref": "#/components/schemas/HLLRefreshCountOperation"
          },
          {
            "$ref": "#/components/schemas/HLLFoldOperation"
          },
          {
            "$ref": "#/components/schemas/HLLGetCountOperation"
          },
          {
            "$ref": "#/components/schemas/HLLGetUnionOperation"
          },
          {
            "$ref": "#/components/schemas/HLLGetUnionCountOperation"
          },
          {
            "$ref": "#/components/schemas/HLLGetIntersectionCountOperation"
          },
          {
            "$ref": "#/components/schemas/HLLGetSimilarityOperation"
          },
          {
            "$ref": "#/components/schemas/HLLDescribeOperation"
          }
        ]
      },
      "HLLRefreshCountOperation": {
        "type": "object",
        "description": "Server updates the cached count (if stale) and returns the count.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/operation/HLLOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/HLLOperation"
          },
          {
            "type": "object",
            "properties": {
              "binName": {
                "type": "string",
                "writeOnly": true
              }
            }
          }
        ]
      },
      "HLLSetUnionOperation": {
        "type": "object",
        "description": "Server sets union of specified HLL objects with HLL bin. Server does not return a value.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/operation/HLLOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/HLLOperation"
          },
          {
            "type": "object",
            "properties": {
              "binName": {
                "type": "string",
                "writeOnly": true
              },
              "values": {
                "type": "array",
                "writeOnly": true,
                "items": {
                  "type": "array",
                  "items": {
                    "type": "string",
                    "format": "byte"
                  }
                }
              }
            }
          }
        ]
      },
      "ListAppendItemsOperation": {
        "type": "object",
        "description": "Append multiple items to a list stored in the specified bin.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/ListOperation"
          },
          {
            "type": "object",
            "properties": {
              "values": {
                "type": "array",
                "writeOnly": true,
                "items": {
                  "type": "object"
                }
              },
              "listPolicy": {
                "$ref": "#/components/schemas/ListPolicy"
              }
            }
          }
        ]
      },
      "ListAppendOperation": {
        "type": "object",
        "description": "Append a `value` to a list stored in the specified `binName`.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/ListOperation"
          },
          {
            "type": "object",
            "properties": {
              "value": {
                "type": "object",
                "writeOnly": true
              },
              "listPolicy": {
                "$ref": "#/components/schemas/ListPolicy"
              }
            }
          }
        ]
      },
      "ListClearOperation": {
        "type": "object",
        "description": "Empty a list stored in the specified bin.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/ListOperation"
          }
        ]
      },
      "ListCreateOperation": {
        "type": "object",
        "description": "Create a list in the specified bin.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/ListOperation"
          },
          {
            "type": "object",
            "properties": {
              "order": {
                "type": "string",
                "writeOnly": true,
                "enum": [
                  "UNORDERED",
                  "ORDERED"
                ]
              },
              "pad": {
                "type": "boolean"
              }
            }
          }
        ]
      },
      "ListGetByIndexOperation": {
        "type": "object",
        "description": "Return an item, located a specific index, from a list in the specified bin. The value of `listReturnType` determines what will be returned. Requires Aerospike Server `3.16.0.1` or greater.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/ListOperation"
          },
          {
            "type": "object",
            "properties": {
              "index": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              },
              "listReturnType": {
                "type": "string",
                "writeOnly": true,
                "enum": [
                  "COUNT",
                  "INDEX",
                  "EXISTS",
                  "NONE",
                  "RANK",
                  "REVERSE_INDEX",
                  "REVERSE_RANK",
                  "VALUE"
                ]
              },
              "inverted": {
                "type": "boolean"
              }
            }
          }
        ]
      },
      "ListGetByIndexRangeOperation": {
        "type": "object",
        "description": "Return a specified amount of items beginning at a specific index, from a list in the specified bin. If `count` is not provided, all items from `index` until the end of the list will be returned. Requires Aerospike Server `3.16.0.1` or later",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/ListOperation"
          },
          {
            "type": "object",
            "properties": {
              "index": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              },
              "listReturnType": {
                "type": "string",
                "writeOnly": true,
                "enum": [
                  "COUNT",
                  "INDEX",
                  "EXISTS",
                  "NONE",
                  "RANK",
                  "REVERSE_INDEX",
                  "REVERSE_RANK",
                  "VALUE"
                ]
              },
              "inverted": {
                "type": "boolean"
              },
              "count": {
                "type": "integer",
                "format": "int32"
              }
            }
          }
        ]
      },
      "ListGetByRankOperation": {
        "type": "object",
        "description": "Return a list item with the specified `rank`. Requires Aerospike Server `3.16.0.1` or later.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/ListOperation"
          },
          {
            "type": "object",
            "properties": {
              "rank": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              },
              "listReturnType": {
                "type": "string",
                "writeOnly": true,
                "enum": [
                  "COUNT",
                  "INDEX",
                  "EXISTS",
                  "NONE",
                  "RANK",
                  "REVERSE_INDEX",
                  "REVERSE_RANK",
                  "VALUE"
                ]
              },
              "inverted": {
                "type": "boolean"
              }
            }
          }
        ]
      },
      "ListGetByRankRangeOperation": {
        "type": "object",
        "description": "Return `count` items beginning with the specified rank. If `count` is omitted, all items beginning with specified rank will be returned. Requires Aerospike Server `3.16.0.1` or later.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/ListOperation"
          },
          {
            "type": "object",
            "properties": {
              "rank": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              },
              "listReturnType": {
                "type": "string",
                "writeOnly": true,
                "enum": [
                  "COUNT",
                  "INDEX",
                  "EXISTS",
                  "NONE",
                  "RANK",
                  "REVERSE_INDEX",
                  "REVERSE_RANK",
                  "VALUE"
                ]
              },
              "inverted": {
                "type": "boolean"
              },
              "count": {
                "type": "integer",
                "format": "int32"
              }
            }
          }
        ]
      },
      "ListGetByValueListOperation": {
        "type": "object",
        "description": "Return all items in a list with values that are contained in the specified list of values. Requires Aerospike Server `3.16.0.1` or later.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/ListOperation"
          },
          {
            "type": "object",
            "properties": {
              "listReturnType": {
                "type": "string",
                "writeOnly": true,
                "enum": [
                  "COUNT",
                  "INDEX",
                  "EXISTS",
                  "NONE",
                  "RANK",
                  "REVERSE_INDEX",
                  "REVERSE_RANK",
                  "VALUE"
                ]
              },
              "values": {
                "type": "array",
                "writeOnly": true,
                "items": {
                  "type": "object"
                }
              },
              "inverted": {
                "type": "boolean"
              }
            }
          }
        ]
      },
      "ListGetByValueOperation": {
        "type": "object",
        "description": "Return all items in a list with a value matching a specified value.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/ListOperation"
          },
          {
            "type": "object",
            "properties": {
              "value": {
                "type": "object",
                "writeOnly": true
              },
              "listReturnType": {
                "type": "string",
                "writeOnly": true,
                "enum": [
                  "COUNT",
                  "INDEX",
                  "EXISTS",
                  "NONE",
                  "RANK",
                  "REVERSE_INDEX",
                  "REVERSE_RANK",
                  "VALUE"
                ]
              },
              "inverted": {
                "type": "boolean"
              }
            }
          }
        ]
      },
      "ListGetByValueRangeOperation": {
        "type": "object",
        "description": "Return all items in a list with values between `valueBegin` and `valueEnd`. If `valueBegin` is omitted, all items with a value less than `valueEnd` will be returned. If `valueEnd` is omitted, all items with a value greater than `valueBegin` will be returned. Requires Aerospike Server `3.16.0.1` or later.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/ListOperation"
          },
          {
            "type": "object",
            "properties": {
              "listReturnType": {
                "type": "string",
                "writeOnly": true,
                "enum": [
                  "COUNT",
                  "INDEX",
                  "EXISTS",
                  "NONE",
                  "RANK",
                  "REVERSE_INDEX",
                  "REVERSE_RANK",
                  "VALUE"
                ]
              },
              "inverted": {
                "type": "boolean"
              },
              "valueBegin": {
                "type": "object"
              },
              "valueEnd": {
                "type": "object"
              }
            }
          }
        ]
      },
      "ListGetByValueRelativeRankRangeOperation": {
        "type": "object",
        "description": "TODO",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/MapOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/ListOperation"
          },
          {
            "type": "object",
            "properties": {
              "rank": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              },
              "value": {
                "type": "object",
                "writeOnly": true
              },
              "listReturnType": {
                "type": "string",
                "writeOnly": true,
                "enum": [
                  "COUNT",
                  "INDEX",
                  "EXISTS",
                  "NONE",
                  "RANK",
                  "REVERSE_INDEX",
                  "REVERSE_RANK",
                  "VALUE"
                ]
              },
              "inverted": {
                "type": "boolean"
              },
              "count": {
                "type": "integer",
                "format": "int32"
              }
            }
          }
        ]
      },
      "ListGetOperation": {
        "type": "object",
        "description": "Return an item, located a specific index, from a list in the specified bin.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/ListOperation"
          },
          {
            "type": "object",
            "properties": {
              "index": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              }
            }
          }
        ]
      },
      "ListGetRangeOperation": {
        "type": "object",
        "description": "Get `count` items from the list beginning with the specified index. If `count` is omitted, all items from `index` to the end of the list will be returned.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/ListOperation"
          },
          {
            "type": "object",
            "properties": {
              "index": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              },
              "count": {
                "type": "integer",
                "format": "int32"
              }
            }
          }
        ]
      },
      "ListIncrementOperation": {
        "type": "object",
        "description": "Increment the value of a an item of a list at the specified index, by the value of `incr`",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/ListOperation"
          },
          {
            "type": "object",
            "properties": {
              "index": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              },
              "incr": {
                "type": "number",
                "writeOnly": true
              },
              "listPolicy": {
                "$ref": "#/components/schemas/ListPolicy"
              }
            }
          }
        ]
      },
      "ListIndexCTX": {
        "required": [
          "index",
          "type"
        ],
        "type": "object",
        "description": "Lookup list by index offset.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/CTX.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/CTX"
          },
          {
            "type": "object",
            "properties": {
              "type": {
                "type": "string",
                "description": "The type of context this object represents. It is always listIndex",
                "enum": [
                  "listIndex"
                ]
              },
              "index": {
                "type": "integer",
                "description": "If the index is negative, the resolved index starts backwards from end of list. If an index is out of bounds, a parameter error will be returned. Examples:\n* 0: First item.\n* 4: Fifth item.\n* -1: Last item.\n* -3: Third to last item.",
                "format": "int32"
              }
            }
          }
        ]
      },
      "ListIndexCreateCTX": {
        "required": [
          "index",
          "type"
        ],
        "type": "object",
        "description": "Lookup list by base list\u0027s index offset. If the list at index offset is not found, create it with the given sort order at that index offset.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/CTX.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/CTX"
          },
          {
            "type": "object",
            "properties": {
              "type": {
                "type": "string",
                "description": "The type of context this object represents. It is always listIndexCreate",
                "enum": [
                  "listIndexCreate"
                ]
              },
              "index": {
                "type": "integer",
                "description": "If the index is negative, the resolved index starts backwards from end of list. If an index is out of bounds, a parameter error will be returned. Examples:\n* 0: First item.\n* 4: Fifth item.\n* -1: Last item.\n* -3: Third to last item.",
                "format": "int32"
              },
              "order": {
                "type": "string",
                "description": "List storage order.",
                "default": "UNORDERED",
                "enum": [
                  "UNORDERED",
                  "ORDERED"
                ]
              },
              "pad": {
                "type": "boolean",
                "description": "If pad is true and the index offset is greater than the bounds of the base list, nil entries will be inserted before the newly created list."
              }
            }
          }
        ]
      },
      "ListInsertItemsOperation": {
        "type": "object",
        "description": "Insert multiple items into a list at the specified `index`.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/ListOperation"
          },
          {
            "type": "object",
            "properties": {
              "index": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              },
              "values": {
                "type": "array",
                "writeOnly": true,
                "items": {
                  "type": "object"
                }
              },
              "listPolicy": {
                "$ref": "#/components/schemas/ListPolicy"
              }
            }
          }
        ]
      },
      "ListInsertOperation": {
        "type": "object",
        "description": "Insert a value into a list at the specified index.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/ListOperation"
          },
          {
            "type": "object",
            "properties": {
              "index": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              },
              "value": {
                "type": "object",
                "writeOnly": true
              },
              "listPolicy": {
                "$ref": "#/components/schemas/ListPolicy"
              }
            }
          }
        ]
      },
      "ListOperation": {
        "required": [
          "type"
        ],
        "type": "object",
        "description": "The base type for describing all cdt list operations. Should not be used directly.",
        "discriminator": {
          "propertyName": "type"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/Operation"
          },
          {
            "type": "object",
            "properties": {
              "binName": {
                "type": "string",
                "writeOnly": true
              },
              "ctx": {
                "type": "array",
                "items": {
                  "$ref": "#/components/schemas/CTX"
                }
              },
              "type": {
                "type": "string"
              }
            }
          }
        ],
        "oneOf": [
          {
            "$ref": "#/components/schemas/ListAppendOperation"
          },
          {
            "$ref": "#/components/schemas/ListAppendItemsOperation"
          },
          {
            "$ref": "#/components/schemas/ListCreateOperation"
          },
          {
            "$ref": "#/components/schemas/ListClearOperation"
          },
          {
            "$ref": "#/components/schemas/ListGetOperation"
          },
          {
            "$ref": "#/components/schemas/ListGetByIndexOperation"
          },
          {
            "$ref": "#/components/schemas/ListGetByIndexRangeOperation"
          },
          {
            "$ref": "#/components/schemas/ListGetByRankOperation"
          },
          {
            "$ref": "#/components/schemas/ListGetByRankRangeOperation"
          },
          {
            "$ref": "#/components/schemas/ListGetByValueRelativeRankRangeOperation"
          },
          {
            "$ref": "#/components/schemas/ListGetByValueOperation"
          },
          {
            "$ref": "#/components/schemas/ListGetByValueRangeOperation"
          },
          {
            "$ref": "#/components/schemas/ListGetByValueListOperation"
          },
          {
            "$ref": "#/components/schemas/ListGetRangeOperation"
          },
          {
            "$ref": "#/components/schemas/ListIncrementOperation"
          },
          {
            "$ref": "#/components/schemas/ListInsertOperation"
          },
          {
            "$ref": "#/components/schemas/ListInsertItemsOperation"
          },
          {
            "$ref": "#/components/schemas/ListPopOperation"
          },
          {
            "$ref": "#/components/schemas/ListPopRangeOperation"
          },
          {
            "$ref": "#/components/schemas/ListRemoveOperation"
          },
          {
            "$ref": "#/components/schemas/ListRemoveByIndexOperation"
          },
          {
            "$ref": "#/components/schemas/ListRemoveByIndexRangeOperation"
          },
          {
            "$ref": "#/components/schemas/ListRemoveByRankOperation"
          },
          {
            "$ref": "#/components/schemas/ListRemoveByRankRangeOperation"
          },
          {
            "$ref": "#/components/schemas/ListRemoveByValueRelativeRankRangeOperation"
          },
          {
            "$ref": "#/components/schemas/ListRemoveByValueOperation"
          },
          {
            "$ref": "#/components/schemas/ListRemoveByValueRangeOperation"
          },
          {
            "$ref": "#/components/schemas/ListRemoveByValueListOperation"
          },
          {
            "$ref": "#/components/schemas/ListRemoveRangeOperation"
          },
          {
            "$ref": "#/components/schemas/ListSetOperation"
          },
          {
            "$ref": "#/components/schemas/ListSetOrderOperation"
          },
          {
            "$ref": "#/components/schemas/ListSizeOperation"
          },
          {
            "$ref": "#/components/schemas/ListSortOperation"
          },
          {
            "$ref": "#/components/schemas/ListTrimOperation"
          }
        ]
      },
      "ListPolicy": {
        "type": "object",
        "properties": {
          "order": {
            "type": "string",
            "enum": [
              "UNORDERED",
              "ORDERED"
            ]
          },
          "writeFlags": {
            "type": "array",
            "items": {
              "type": "string",
              "enum": [
                "DEFAULT",
                "ADD_UNIQUE",
                "INSERT_BOUNDED",
                "NO_FAIL",
                "PARTIAL"
              ]
            }
          }
        }
      },
      "ListPopOperation": {
        "type": "object",
        "description": "Remove and return a list value at the specified `index`.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/ListOperation"
          },
          {
            "type": "object",
            "properties": {
              "index": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              }
            }
          }
        ]
      },
      "ListPopRangeOperation": {
        "type": "object",
        "description": "Remove and return `count` items beginning at the specified `index` from the list. If `count` is omitted, all items beginning from `index` will be removed and returned.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/ListOperation"
          },
          {
            "type": "object",
            "properties": {
              "index": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              },
              "count": {
                "type": "integer",
                "format": "int32"
              }
            }
          }
        ]
      },
      "ListRankCTX": {
        "required": [
          "rank",
          "type"
        ],
        "type": "object",
        "description": "Lookup list by rank",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/CTX.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/CTX"
          },
          {
            "type": "object",
            "properties": {
              "type": {
                "type": "string",
                "description": "The type of context this object represents. It is always listRank",
                "enum": [
                  "listRank"
                ]
              },
              "rank": {
                "type": "integer",
                "description": "* 0 \u003d smallest value\n* N \u003d Nth smallest value\n* -1 \u003d largest value",
                "format": "int32"
              }
            }
          }
        ]
      },
      "ListRemoveByIndexOperation": {
        "type": "object",
        "description": "Remove a list item at the specified index. Requires Aerospike Server `3.16.0.1` or later.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/ListOperation"
          },
          {
            "type": "object",
            "properties": {
              "index": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              },
              "listReturnType": {
                "type": "string",
                "writeOnly": true,
                "enum": [
                  "COUNT",
                  "INDEX",
                  "EXISTS",
                  "NONE",
                  "RANK",
                  "REVERSE_INDEX",
                  "REVERSE_RANK",
                  "VALUE"
                ]
              },
              "inverted": {
                "type": "boolean"
              }
            }
          }
        ]
      },
      "ListRemoveByIndexRangeOperation": {
        "type": "object",
        "description": "Remove and return `count` items beginning at the specified `index` from the list. If `count` is omitted, all items beginning from `index` will be removed and returned. Requires Aerospike Server 3.16.0.1 or later",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/ListOperation"
          },
          {
            "type": "object",
            "properties": {
              "index": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              },
              "listReturnType": {
                "type": "string",
                "writeOnly": true,
                "enum": [
                  "COUNT",
                  "INDEX",
                  "EXISTS",
                  "NONE",
                  "RANK",
                  "REVERSE_INDEX",
                  "REVERSE_RANK",
                  "VALUE"
                ]
              },
              "inverted": {
                "type": "boolean"
              },
              "count": {
                "type": "integer",
                "format": "int32"
              }
            }
          }
        ]
      },
      "ListRemoveByRankOperation": {
        "type": "object",
        "description": "Remove a list item with the specified rank. Requires Aerospike Server `3.16.0.1` or later",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/ListOperation"
          },
          {
            "type": "object",
            "properties": {
              "rank": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              },
              "listReturnType": {
                "type": "string",
                "writeOnly": true,
                "enum": [
                  "COUNT",
                  "INDEX",
                  "EXISTS",
                  "NONE",
                  "RANK",
                  "REVERSE_INDEX",
                  "REVERSE_RANK",
                  "VALUE"
                ]
              },
              "inverted": {
                "type": "boolean"
              }
            }
          }
        ]
      },
      "ListRemoveByRankRangeOperation": {
        "type": "object",
        "description": "Remove `count` items from a list, beginning with the item with the specified `rank`. If `count` is omitted, all items beginning with the specified `rank` will be removed and returned. Requires Aerospike Server `3.16.0.1` or later.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/ListOperation"
          },
          {
            "type": "object",
            "properties": {
              "rank": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              },
              "listReturnType": {
                "type": "string",
                "writeOnly": true,
                "enum": [
                  "COUNT",
                  "INDEX",
                  "EXISTS",
                  "NONE",
                  "RANK",
                  "REVERSE_INDEX",
                  "REVERSE_RANK",
                  "VALUE"
                ]
              },
              "count": {
                "type": "integer",
                "format": "int32"
              }
            }
          }
        ]
      },
      "ListRemoveByValueListOperation": {
        "type": "object",
        "description": "Remove all items from the list with values contained in the specified list of values. Requires Aerospike Server `3.16.0.1` or later",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/ListOperation"
          },
          {
            "type": "object",
            "properties": {
              "values": {
                "type": "array",
                "writeOnly": true,
                "items": {
                  "type": "object"
                }
              },
              "listReturnType": {
                "type": "string",
                "writeOnly": true,
                "enum": [
                  "COUNT",
                  "INDEX",
                  "EXISTS",
                  "NONE",
                  "RANK",
                  "REVERSE_INDEX",
                  "REVERSE_RANK",
                  "VALUE"
                ]
              },
              "inverted": {
                "type": "boolean"
              }
            }
          }
        ]
      },
      "ListRemoveByValueOperation": {
        "type": "object",
        "description": "Remove and return list entries with a value equal to the specified value. Requires Aerospike Server `3.16.0.`1 or later",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/ListOperation"
          },
          {
            "type": "object",
            "properties": {
              "value": {
                "type": "object",
                "writeOnly": true
              },
              "listReturnType": {
                "type": "string",
                "writeOnly": true,
                "enum": [
                  "COUNT",
                  "INDEX",
                  "EXISTS",
                  "NONE",
                  "RANK",
                  "REVERSE_INDEX",
                  "REVERSE_RANK",
                  "VALUE"
                ]
              },
              "inverted": {
                "type": "boolean"
              }
            }
          }
        ]
      },
      "ListRemoveByValueRangeOperation": {
        "type": "object",
        "description": "Remove all items from the list with values between `valueBegin` and `valueEnd`. If `valueBegin` is omitted all items with a value less than `valueEnd` will be removed. If `valueEnd` is omitted all items with a value greater than `valueBegin` will be removed. Requires Aerospike Server `3.16.0.1` or later.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/ListOperation"
          },
          {
            "type": "object",
            "properties": {
              "listReturnType": {
                "type": "string",
                "writeOnly": true,
                "enum": [
                  "COUNT",
                  "INDEX",
                  "EXISTS",
                  "NONE",
                  "RANK",
                  "REVERSE_INDEX",
                  "REVERSE_RANK",
                  "VALUE"
                ]
              },
              "inverted": {
                "type": "boolean"
              },
              "valueBegin": {
                "type": "object"
              },
              "valueEnd": {
                "type": "object"
              }
            }
          }
        ]
      },
      "ListRemoveByValueRelativeRankRangeOperation": {
        "type": "object",
        "description": "TODO",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/MapOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/ListOperation"
          },
          {
            "type": "object",
            "properties": {
              "rank": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              },
              "value": {
                "type": "object",
                "writeOnly": true
              },
              "listReturnType": {
                "type": "string",
                "writeOnly": true,
                "enum": [
                  "COUNT",
                  "INDEX",
                  "EXISTS",
                  "NONE",
                  "RANK",
                  "REVERSE_INDEX",
                  "REVERSE_RANK",
                  "VALUE"
                ]
              },
              "inverted": {
                "type": "boolean"
              },
              "count": {
                "type": "integer",
                "format": "int32"
              }
            }
          }
        ]
      },
      "ListRemoveOperation": {
        "type": "object",
        "description": "Remove a list item at the specified index.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/ListOperation"
          },
          {
            "type": "object",
            "properties": {
              "index": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              }
            }
          }
        ]
      },
      "ListRemoveRangeOperation": {
        "type": "object",
        "description": "Remove `count` items beginning at the specified `index` from the list. If `count` is omitted, all items beginning from `index` will be removed and returned.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/ListOperation"
          },
          {
            "type": "object",
            "properties": {
              "index": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              },
              "count": {
                "type": "integer",
                "format": "int32"
              }
            }
          }
        ]
      },
      "ListSetOperation": {
        "type": "object",
        "description": "Set the value at the specified index to the specified value.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/ListOperation"
          },
          {
            "type": "object",
            "properties": {
              "index": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              },
              "value": {
                "type": "object",
                "writeOnly": true
              },
              "listPolicy": {
                "$ref": "#/components/schemas/ListPolicy"
              }
            }
          }
        ]
      },
      "ListSetOrderOperation": {
        "type": "object",
        "description": "Set an ordering for the list.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/ListOperation"
          },
          {
            "type": "object",
            "properties": {
              "listOrder": {
                "type": "string",
                "writeOnly": true,
                "enum": [
                  "UNORDERED",
                  "ORDERED"
                ]
              }
            }
          }
        ]
      },
      "ListSizeOperation": {
        "type": "object",
        "description": "Return the size of the list.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/ListOperation"
          }
        ]
      },
      "ListSortOperation": {
        "type": "object",
        "description": "Perform a sort operation on the list. Requires Aerospike Server `3.16.0.1` or later.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/ListOperation"
          },
          {
            "type": "object",
            "properties": {
              "sortFlags": {
                "type": "array",
                "items": {
                  "type": "string",
                  "enum": [
                    "DEFAULT",
                    "DROP_DUPLICATES"
                  ]
                }
              }
            }
          }
        ]
      },
      "ListTrimOperation": {
        "type": "object",
        "description": "Trim the list to the specified range. Items with indexes in the range `[index, index + count)` will be retained.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/ListOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/ListOperation"
          },
          {
            "type": "object",
            "properties": {
              "index": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              },
              "count": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              }
            }
          }
        ]
      },
      "ListValueCTX": {
        "required": [
          "type",
          "value"
        ],
        "type": "object",
        "description": "Lookup list by value",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/CTX.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/CTX"
          },
          {
            "type": "object",
            "properties": {
              "type": {
                "type": "string",
                "description": "The type of context this object represents. It is always listValue",
                "enum": [
                  "listValue"
                ]
              },
              "value": {
                "type": "object"
              }
            }
          }
        ]
      },
      "MapClearOperation": {
        "type": "object",
        "description": "Empty the specified map.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/MapOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/MapOperation"
          }
        ]
      },
      "MapCreateOperation": {
        "type": "object",
        "description": "Empty the specified map.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/MapOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/MapOperation"
          },
          {
            "type": "object",
            "properties": {
              "mapOrder": {
                "type": "string",
                "writeOnly": true,
                "enum": [
                  "UNORDERED",
                  "KEY_ORDERED",
                  "KEY_VALUE_ORDERED"
                ]
              }
            }
          }
        ]
      },
      "MapGetByIndexOperation": {
        "type": "object",
        "description": "Get the map item at the specified `index`.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/MapOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/MapOperation"
          },
          {
            "type": "object",
            "properties": {
              "index": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              },
              "mapReturnType": {
                "type": "string",
                "writeOnly": true,
                "enum": [
                  "COUNT",
                  "INDEX",
                  "KEY",
                  "KEY_VALUE",
                  "NONE",
                  "RANK",
                  "REVERSE_INDEX",
                  "REVERSE_RANK",
                  "VALUE",
                  "EXISTS"
                ]
              },
              "inverted": {
                "type": "boolean"
              }
            }
          }
        ]
      },
      "MapGetByIndexRangeOperation": {
        "type": "object",
        "description": "Get all map items with indexes in the range `[index, index + count)` . If `count` is omitted, all items beginning with the item at the specified index will be returned.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/MapOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/MapOperation"
          },
          {
            "type": "object",
            "properties": {
              "index": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              },
              "mapReturnType": {
                "type": "string",
                "writeOnly": true,
                "enum": [
                  "COUNT",
                  "INDEX",
                  "KEY",
                  "KEY_VALUE",
                  "NONE",
                  "RANK",
                  "REVERSE_INDEX",
                  "REVERSE_RANK",
                  "VALUE",
                  "EXISTS"
                ]
              },
              "inverted": {
                "type": "boolean"
              },
              "count": {
                "type": "integer",
                "format": "int32"
              }
            }
          }
        ]
      },
      "MapGetByKeyListOperation": {
        "type": "object",
        "description": "Remove values with the specified keys from the map. Requires Aerospike Server `3.16.0.1` or later",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/MapOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/MapOperation"
          },
          {
            "type": "object",
            "properties": {
              "keys": {
                "type": "array",
                "writeOnly": true,
                "items": {
                  "type": "object"
                }
              },
              "mapReturnType": {
                "type": "string",
                "writeOnly": true,
                "enum": [
                  "COUNT",
                  "INDEX",
                  "KEY",
                  "KEY_VALUE",
                  "NONE",
                  "RANK",
                  "REVERSE_INDEX",
                  "REVERSE_RANK",
                  "VALUE",
                  "EXISTS"
                ]
              },
              "inverted": {
                "type": "boolean"
              }
            }
          }
        ]
      },
      "MapGetByKeyOperation": {
        "type": "object",
        "description": "Return the value with the specified key from the map.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/MapOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/MapOperation"
          },
          {
            "type": "object",
            "properties": {
              "key": {
                "type": "object",
                "writeOnly": true
              },
              "mapReturnType": {
                "type": "string",
                "writeOnly": true,
                "enum": [
                  "COUNT",
                  "INDEX",
                  "KEY",
                  "KEY_VALUE",
                  "NONE",
                  "RANK",
                  "REVERSE_INDEX",
                  "REVERSE_RANK",
                  "VALUE",
                  "EXISTS"
                ]
              },
              "inverted": {
                "type": "boolean"
              }
            }
          }
        ]
      },
      "MapGetByKeyRangeOperation": {
        "type": "object",
        "description": "Return map values with keys in the specified range. If `keyBegin` is omitted, all map values with key values less than `keyEnd` will be returned. If `keyEnd` is omitted, all map values with a key greater than or equal to `keyBegin` will be returned.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/MapOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/MapOperation"
          },
          {
            "type": "object",
            "properties": {
              "mapReturnType": {
                "type": "string",
                "writeOnly": true,
                "enum": [
                  "COUNT",
                  "INDEX",
                  "KEY",
                  "KEY_VALUE",
                  "NONE",
                  "RANK",
                  "REVERSE_INDEX",
                  "REVERSE_RANK",
                  "VALUE",
                  "EXISTS"
                ]
              },
              "inverted": {
                "type": "boolean"
              },
              "keyBegin": {
                "type": "object"
              },
              "keyEnd": {
                "type": "object"
              }
            }
          }
        ]
      },
      "MapGetByKeyRelativeIndexRange": {
        "type": "object",
        "description": "TODO",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/MapOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/MapOperation"
          },
          {
            "type": "object",
            "properties": {
              "index": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              },
              "value": {
                "type": "object",
                "writeOnly": true
              },
              "mapReturnType": {
                "type": "string",
                "writeOnly": true,
                "enum": [
                  "COUNT",
                  "INDEX",
                  "KEY",
                  "KEY_VALUE",
                  "NONE",
                  "RANK",
                  "REVERSE_INDEX",
                  "REVERSE_RANK",
                  "VALUE",
                  "EXISTS"
                ]
              },
              "inverted": {
                "type": "boolean"
              },
              "count": {
                "type": "integer",
                "format": "int32"
              }
            }
          }
        ]
      },
      "MapGetByRankOperation": {
        "type": "object",
        "description": "Return the map value with the specified rank.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/MapOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/MapOperation"
          },
          {
            "type": "object",
            "properties": {
              "rank": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              },
              "mapReturnType": {
                "type": "string",
                "writeOnly": true,
                "enum": [
                  "COUNT",
                  "INDEX",
                  "KEY",
                  "KEY_VALUE",
                  "NONE",
                  "RANK",
                  "REVERSE_INDEX",
                  "REVERSE_RANK",
                  "VALUE",
                  "EXISTS"
                ]
              },
              "inverted": {
                "type": "boolean"
              }
            }
          }
        ]
      },
      "MapGetByRankRangeOperation": {
        "type": "object",
        "description": "Return `count` values from the map beginning with the value with the specified `rank`. If `count` is omitted, all items with a `rank` greater than or equal to the specified `rank` will be returned.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/MapOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/MapOperation"
          },
          {
            "type": "object",
            "properties": {
              "mapReturnType": {
                "type": "string",
                "writeOnly": true,
                "enum": [
                  "COUNT",
                  "INDEX",
                  "KEY",
                  "KEY_VALUE",
                  "NONE",
                  "RANK",
                  "REVERSE_INDEX",
                  "REVERSE_RANK",
                  "VALUE",
                  "EXISTS"
                ]
              },
              "rank": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              },
              "count": {
                "type": "integer",
                "format": "int32"
              },
              "inverted": {
                "type": "boolean"
              }
            }
          }
        ]
      },
      "MapGetByValueListOperation": {
        "type": "object",
        "description": "Return all map items with a value contained in the provided list of values.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/MapOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/MapOperation"
          },
          {
            "type": "object",
            "properties": {
              "values": {
                "type": "array",
                "writeOnly": true,
                "items": {
                  "type": "object"
                }
              },
              "mapReturnType": {
                "type": "string",
                "writeOnly": true,
                "enum": [
                  "COUNT",
                  "INDEX",
                  "KEY",
                  "KEY_VALUE",
                  "NONE",
                  "RANK",
                  "REVERSE_INDEX",
                  "REVERSE_RANK",
                  "VALUE",
                  "EXISTS"
                ]
              },
              "inverted": {
                "type": "boolean"
              }
            }
          }
        ]
      },
      "MapGetByValueOperation": {
        "type": "object",
        "description": "Return all map values with the specified value.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/MapOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/MapOperation"
          },
          {
            "type": "object",
            "properties": {
              "value": {
                "type": "object",
                "writeOnly": true
              },
              "mapReturnType": {
                "type": "string",
                "writeOnly": true,
                "enum": [
                  "COUNT",
                  "INDEX",
                  "KEY",
                  "KEY_VALUE",
                  "NONE",
                  "RANK",
                  "REVERSE_INDEX",
                  "REVERSE_RANK",
                  "VALUE",
                  "EXISTS"
                ]
              },
              "inverted": {
                "type": "boolean"
              }
            }
          }
        ]
      },
      "MapGetByValueRangeOperation": {
        "type": "object",
        "description": "Return all map items with value in the range [`valueBegin`, `valueEnd`). If `valueBegin` is omitted, all map items with a value less than `valueEnd` will be returned. If `valueEnd` is omitted, all map items with a value greater than or equal to `valueBegin` will be returned.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/MapOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/MapOperation"
          },
          {
            "type": "object",
            "properties": {
              "mapReturnType": {
                "type": "string",
                "writeOnly": true,
                "enum": [
                  "COUNT",
                  "INDEX",
                  "KEY",
                  "KEY_VALUE",
                  "NONE",
                  "RANK",
                  "REVERSE_INDEX",
                  "REVERSE_RANK",
                  "VALUE",
                  "EXISTS"
                ]
              },
              "inverted": {
                "type": "boolean"
              },
              "valueBegin": {
                "type": "object"
              },
              "valueEnd": {
                "type": "object"
              }
            }
          }
        ]
      },
      "MapGetByValueRelativeRankRangeOperation": {
        "type": "object",
        "description": "TODO",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/MapOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/MapOperation"
          },
          {
            "type": "object",
            "properties": {
              "rank": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              },
              "value": {
                "type": "object",
                "writeOnly": true
              },
              "mapReturnType": {
                "type": "string",
                "writeOnly": true,
                "enum": [
                  "COUNT",
                  "INDEX",
                  "KEY",
                  "KEY_VALUE",
                  "NONE",
                  "RANK",
                  "REVERSE_INDEX",
                  "REVERSE_RANK",
                  "VALUE",
                  "EXISTS"
                ]
              },
              "inverted": {
                "type": "boolean"
              },
              "count": {
                "type": "integer",
                "format": "int32"
              }
            }
          }
        ]
      },
      "MapIncrementOperation": {
        "type": "object",
        "description": "Increment the map value with the specified key by the specified amount.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/MapOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/MapOperation"
          },
          {
            "type": "object",
            "properties": {
              "incr": {
                "type": "number",
                "writeOnly": true
              },
              "key": {
                "type": "object",
                "writeOnly": true
              },
              "mapPolicy": {
                "$ref": "#/components/schemas/MapPolicy"
              }
            }
          }
        ]
      },
      "MapIndexCTX": {
        "required": [
          "index",
          "type"
        ],
        "type": "object",
        "description": "Lookup map by index offset.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/CTX.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/CTX"
          },
          {
            "type": "object",
            "properties": {
              "type": {
                "type": "string",
                "description": "The type of context this object represents. It is always mapIndex",
                "enum": [
                  "mapIndex"
                ]
              },
              "index": {
                "type": "integer",
                "description": "If the index is negative, the resolved index starts backwards from end of list. If an index is out of bounds, a parameter error will be returned. Examples:\n\n* 0: First item.\n* 4: Fifth item.\n* -1: Last item.\n* -3: Third to last item.",
                "format": "int32"
              }
            }
          }
        ]
      },
      "MapKeyCTX": {
        "required": [
          "key",
          "type"
        ],
        "type": "object",
        "description": "Lookup map by key.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/CTX.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/CTX"
          },
          {
            "type": "object",
            "properties": {
              "type": {
                "type": "string",
                "description": "The type of context this object represents. It is always mapKey",
                "enum": [
                  "mapKey"
                ]
              },
              "key": {
                "type": "object",
                "description": "String, Integer, or ByteArraySpecifiedType",
                "example": "my-user-key"
              }
            }
          }
        ]
      },
      "MapKeyCreateCTX": {
        "required": [
          "key",
          "type"
        ],
        "type": "object",
        "description": "Lookup map by base map\u0027s key. If the map at key is not found, create it with the given sort order at that key.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/CTX.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/CTX"
          },
          {
            "type": "object",
            "properties": {
              "type": {
                "type": "string",
                "description": "The type of context this object represents. It is always mapKeyCreate",
                "enum": [
                  "mapKeyCreate"
                ]
              },
              "key": {
                "type": "object",
                "description": "String, Integer, or ByteArraySpecifiedType",
                "example": "my-user-key"
              },
              "order": {
                "type": "string",
                "description": "Map storage order.",
                "externalDocs": {
                  "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/MapOrder.html"
                },
                "enum": [
                  "UNORDERED",
                  "KEY_ORDERED",
                  "KEY_VALUE_ORDERED"
                ]
              }
            }
          }
        ]
      },
      "MapOperation": {
        "required": [
          "type"
        ],
        "type": "object",
        "description": "The base type for describing all cdt map operations. Should not be used directly.",
        "discriminator": {
          "propertyName": "type"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/Operation"
          },
          {
            "type": "object",
            "properties": {
              "binName": {
                "type": "string",
                "writeOnly": true
              },
              "ctx": {
                "type": "array",
                "items": {
                  "$ref": "#/components/schemas/CTX"
                }
              },
              "type": {
                "type": "string"
              }
            }
          }
        ],
        "oneOf": [
          {
            "$ref": "#/components/schemas/MapCreateOperation"
          },
          {
            "$ref": "#/components/schemas/MapClearOperation"
          },
          {
            "$ref": "#/components/schemas/MapGetByIndexOperation"
          },
          {
            "$ref": "#/components/schemas/MapGetByIndexRangeOperation"
          },
          {
            "$ref": "#/components/schemas/MapGetByKeyOperation"
          },
          {
            "$ref": "#/components/schemas/MapGetByKeyListOperation"
          },
          {
            "$ref": "#/components/schemas/MapGetByKeyRangeOperation"
          },
          {
            "$ref": "#/components/schemas/MapGetByRankOperation"
          },
          {
            "$ref": "#/components/schemas/MapGetByRankRangeOperation"
          },
          {
            "$ref": "#/components/schemas/MapGetByValueOperation"
          },
          {
            "$ref": "#/components/schemas/MapGetByValueRangeOperation"
          },
          {
            "$ref": "#/components/schemas/MapGetByValueListOperation"
          },
          {
            "$ref": "#/components/schemas/MapGetByValueRelativeRankRangeOperation"
          },
          {
            "$ref": "#/components/schemas/MapGetByKeyRelativeIndexRange"
          },
          {
            "$ref": "#/components/schemas/MapIncrementOperation"
          },
          {
            "$ref": "#/components/schemas/MapPutOperation"
          },
          {
            "$ref": "#/components/schemas/MapPutItemsOperation"
          },
          {
            "$ref": "#/components/schemas/MapRemoveByIndexOperation"
          },
          {
            "$ref": "#/components/schemas/MapRemoveByIndexRangeOperation"
          },
          {
            "$ref": "#/components/schemas/MapRemoveByKeyOperation"
          },
          {
            "$ref": "#/components/schemas/MapRemoveByKeyRangeOperation"
          },
          {
            "$ref": "#/components/schemas/MapRemoveByRankOperation"
          },
          {
            "$ref": "#/components/schemas/MapRemoveByRankRangeOperation"
          },
          {
            "$ref": "#/components/schemas/MapRemoveByValueOperation"
          },
          {
            "$ref": "#/components/schemas/MapRemoveByValueRangeOperation"
          },
          {
            "$ref": "#/components/schemas/MapRemoveByValueListOperation"
          },
          {
            "$ref": "#/components/schemas/MapRemoveByValueRelativeRankRange"
          },
          {
            "$ref": "#/components/schemas/MapSetPolicyOperation"
          },
          {
            "$ref": "#/components/schemas/MapSizeOperation"
          }
        ]
      },
      "MapPolicy": {
        "type": "object",
        "properties": {
          "order": {
            "type": "string",
            "enum": [
              "UNORDERED",
              "KEY_ORDERED",
              "KEY_VALUE_ORDERED"
            ]
          },
          "writeFlags": {
            "type": "array",
            "items": {
              "type": "string",
              "enum": [
                "DEFAULT",
                "CREATE_ONLY",
                "UPDATE_ONLY",
                "NO_FAIL",
                "PARTIAL"
              ]
            }
          }
        }
      },
      "MapPutItemsOperation": {
        "type": "object",
        "description": "Store multiple values into the map with the specified keys.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/MapOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/MapOperation"
          },
          {
            "type": "object",
            "properties": {
              "map": {
                "type": "object",
                "additionalProperties": {
                  "type": "object"
                },
                "writeOnly": true
              },
              "mapPolicy": {
                "$ref": "#/components/schemas/MapPolicy"
              }
            }
          }
        ]
      },
      "MapPutOperation": {
        "type": "object",
        "description": "Store the specified value into the map in the specified bin with the specified key. Equivalent to `Map[key] \u003d value`.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/MapOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/MapOperation"
          },
          {
            "type": "object",
            "properties": {
              "key": {
                "type": "object",
                "writeOnly": true
              },
              "value": {
                "type": "object",
                "writeOnly": true
              },
              "mapPolicy": {
                "$ref": "#/components/schemas/MapPolicy"
              }
            }
          }
        ]
      },
      "MapRankCTX": {
        "required": [
          "rank",
          "type"
        ],
        "type": "object",
        "description": "Lookup map by rank.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/CTX.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/CTX"
          },
          {
            "type": "object",
            "properties": {
              "type": {
                "type": "string",
                "description": "The type of context this object represents. It is always mapRank",
                "enum": [
                  "mapRank"
                ]
              },
              "rank": {
                "type": "integer",
                "description": "* 0 \u003d smallest value\n* N \u003d Nth smallest value\n* -1 \u003d largest value",
                "format": "int32"
              }
            }
          }
        ]
      },
      "MapRemoveByIndexOperation": {
        "type": "object",
        "description": "Remove and return the map item at the specified index.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/MapOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/MapOperation"
          },
          {
            "type": "object",
            "properties": {
              "index": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              },
              "mapReturnType": {
                "type": "string",
                "writeOnly": true,
                "enum": [
                  "COUNT",
                  "INDEX",
                  "KEY",
                  "KEY_VALUE",
                  "NONE",
                  "RANK",
                  "REVERSE_INDEX",
                  "REVERSE_RANK",
                  "VALUE",
                  "EXISTS"
                ]
              },
              "inverted": {
                "type": "boolean"
              }
            }
          }
        ]
      },
      "MapRemoveByIndexRangeOperation": {
        "type": "object",
        "description": "Remove all map items with indexes in the range `[index, index + count)` . If `count` is omitted, all items beginning with the item at the specified index will be removed.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/MapOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/MapOperation"
          },
          {
            "type": "object",
            "properties": {
              "index": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              },
              "mapReturnType": {
                "type": "string",
                "writeOnly": true,
                "enum": [
                  "COUNT",
                  "INDEX",
                  "KEY",
                  "KEY_VALUE",
                  "NONE",
                  "RANK",
                  "REVERSE_INDEX",
                  "REVERSE_RANK",
                  "VALUE",
                  "EXISTS"
                ]
              },
              "inverted": {
                "type": "boolean"
              },
              "count": {
                "type": "integer",
                "format": "int32"
              }
            }
          }
        ]
      },
      "MapRemoveByKeyOperation": {
        "type": "object",
        "description": "Remove and return the map item with the specified key from the map.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/MapOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/MapOperation"
          },
          {
            "type": "object",
            "properties": {
              "key": {
                "type": "object",
                "writeOnly": true
              },
              "mapReturnType": {
                "type": "string",
                "writeOnly": true,
                "enum": [
                  "COUNT",
                  "INDEX",
                  "KEY",
                  "KEY_VALUE",
                  "NONE",
                  "RANK",
                  "REVERSE_INDEX",
                  "REVERSE_RANK",
                  "VALUE",
                  "EXISTS"
                ]
              },
              "inverted": {
                "type": "boolean"
              }
            }
          }
        ]
      },
      "MapRemoveByKeyRangeOperation": {
        "type": "object",
        "description": "Remove and return map values with keys in the specified range. If `keyBegin` is omitted, all map values with key values less than `keyEnd` will be removed and returned. If `keyEnd` is omitted, all map values with a key greater than or equal to `keyBegin` will be removed and returned.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/MapOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/MapOperation"
          },
          {
            "type": "object",
            "properties": {
              "mapReturnType": {
                "type": "string",
                "writeOnly": true,
                "enum": [
                  "COUNT",
                  "INDEX",
                  "KEY",
                  "KEY_VALUE",
                  "NONE",
                  "RANK",
                  "REVERSE_INDEX",
                  "REVERSE_RANK",
                  "VALUE",
                  "EXISTS"
                ]
              },
              "inverted": {
                "type": "boolean"
              },
              "keyBegin": {
                "type": "object"
              },
              "keyEnd": {
                "type": "object"
              }
            }
          }
        ]
      },
      "MapRemoveByRankOperation": {
        "type": "object",
        "description": "Remove and return the map value with the specified rank.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/MapOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/MapOperation"
          },
          {
            "type": "object",
            "properties": {
              "rank": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              },
              "mapReturnType": {
                "type": "string",
                "writeOnly": true,
                "enum": [
                  "COUNT",
                  "INDEX",
                  "KEY",
                  "KEY_VALUE",
                  "NONE",
                  "RANK",
                  "REVERSE_INDEX",
                  "REVERSE_RANK",
                  "VALUE",
                  "EXISTS"
                ]
              },
              "inverted": {
                "type": "boolean"
              }
            }
          }
        ]
      },
      "MapRemoveByRankRangeOperation": {
        "type": "object",
        "description": "Remove and return `count` values from the map beginning with the value with the specified rank. If `count` is omitted, all items beginning with the specified `rank` will be removed and returned.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/MapOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/MapOperation"
          },
          {
            "type": "object",
            "properties": {
              "rank": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              },
              "mapReturnType": {
                "type": "string",
                "writeOnly": true,
                "enum": [
                  "COUNT",
                  "INDEX",
                  "KEY",
                  "KEY_VALUE",
                  "NONE",
                  "RANK",
                  "REVERSE_INDEX",
                  "REVERSE_RANK",
                  "VALUE",
                  "EXISTS"
                ]
              },
              "inverted": {
                "type": "boolean"
              },
              "count": {
                "type": "integer",
                "format": "int32"
              }
            }
          }
        ]
      },
      "MapRemoveByValueListOperation": {
        "type": "object",
        "description": "Remove and return all map items with a value contained in the provided list of values. Requires Aerospike Server `3.16.0.1` or later.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/MapOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/MapOperation"
          },
          {
            "type": "object",
            "properties": {
              "values": {
                "type": "array",
                "writeOnly": true,
                "items": {
                  "type": "object"
                }
              },
              "mapReturnType": {
                "type": "string",
                "writeOnly": true,
                "enum": [
                  "COUNT",
                  "INDEX",
                  "KEY",
                  "KEY_VALUE",
                  "NONE",
                  "RANK",
                  "REVERSE_INDEX",
                  "REVERSE_RANK",
                  "VALUE",
                  "EXISTS"
                ]
              },
              "inverted": {
                "type": "boolean"
              }
            }
          }
        ]
      },
      "MapRemoveByValueOperation": {
        "type": "object",
        "description": "Remove and return all map items with a value equal to the specified value.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/MapOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/MapOperation"
          },
          {
            "type": "object",
            "properties": {
              "value": {
                "type": "object",
                "writeOnly": true
              },
              "mapReturnType": {
                "type": "string",
                "writeOnly": true,
                "enum": [
                  "COUNT",
                  "INDEX",
                  "KEY",
                  "KEY_VALUE",
                  "NONE",
                  "RANK",
                  "REVERSE_INDEX",
                  "REVERSE_RANK",
                  "VALUE",
                  "EXISTS"
                ]
              },
              "inverted": {
                "type": "boolean"
              }
            }
          }
        ]
      },
      "MapRemoveByValueRangeOperation": {
        "type": "object",
        "description": "Remove and return all map items with value in the range `[valueBegin, valueEnd)`. If `valueBegin` is omitted, all map items with a value less than `valueEnd` will be removed and returned. If `valueEnd` is omitted, all map items with a value greater than or equal to `valueBegin` will be removed and returned.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/MapOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/MapOperation"
          },
          {
            "type": "object",
            "properties": {
              "mapReturnType": {
                "type": "string",
                "writeOnly": true,
                "enum": [
                  "COUNT",
                  "INDEX",
                  "KEY",
                  "KEY_VALUE",
                  "NONE",
                  "RANK",
                  "REVERSE_INDEX",
                  "REVERSE_RANK",
                  "VALUE",
                  "EXISTS"
                ]
              },
              "inverted": {
                "type": "boolean"
              },
              "valueBegin": {
                "type": "object"
              },
              "valueEnd": {
                "type": "object"
              }
            }
          }
        ]
      },
      "MapRemoveByValueRelativeRankRange": {
        "type": "object",
        "description": "TODO",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/MapOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/MapOperation"
          },
          {
            "type": "object",
            "properties": {
              "rank": {
                "type": "integer",
                "format": "int32",
                "writeOnly": true
              },
              "value": {
                "type": "object",
                "writeOnly": true
              },
              "mapReturnType": {
                "type": "string",
                "writeOnly": true,
                "enum": [
                  "COUNT",
                  "INDEX",
                  "KEY",
                  "KEY_VALUE",
                  "NONE",
                  "RANK",
                  "REVERSE_INDEX",
                  "REVERSE_RANK",
                  "VALUE",
                  "EXISTS"
                ]
              },
              "inverted": {
                "type": "boolean"
              },
              "count": {
                "type": "integer",
                "format": "int32"
              }
            }
          }
        ]
      },
      "MapSetPolicyOperation": {
        "type": "object",
        "description": "Set the policy for the map in the specified bin.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/MapOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/MapOperation"
          },
          {
            "type": "object",
            "properties": {
              "mapPolicy": {
                "$ref": "#/components/schemas/MapPolicy"
              }
            }
          }
        ]
      },
      "MapSizeOperation": {
        "type": "object",
        "description": "Return the size of the map in the specified bin.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/MapOperation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/MapOperation"
          }
        ]
      },
      "MapValueCTX": {
        "required": [
          "type",
          "value"
        ],
        "type": "object",
        "description": "Lookup map by value.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/CTX.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/CTX"
          },
          {
            "type": "object",
            "properties": {
              "type": {
                "type": "string",
                "description": "The type of context this object represents. It is always mapValue",
                "enum": [
                  "mapValue"
                ]
              },
              "value": {
                "type": "object"
              }
            }
          }
        ]
      },
      "OperateRequestBody": {
        "type": "object",
        "properties": {
          "opsList": {
            "type": "array",
            "items": {
              "oneOf": [
                {
                  "$ref": "#/components/schemas/AddOperation"
                },
                {
                  "$ref": "#/components/schemas/AppendOperation"
                },
                {
                  "$ref": "#/components/schemas/BitOperation"
                },
                {
                  "$ref": "#/components/schemas/DeleteOperation"
                },
                {
                  "$ref": "#/components/schemas/GetHeaderOperation"
                },
                {
                  "$ref": "#/components/schemas/GetOperation"
                },
                {
                  "$ref": "#/components/schemas/HLLOperation"
                },
                {
                  "$ref": "#/components/schemas/ListOperation"
                },
                {
                  "$ref": "#/components/schemas/MapOperation"
                },
                {
                  "$ref": "#/components/schemas/PrependOperation"
                },
                {
                  "$ref": "#/components/schemas/PutOperation"
                },
                {
                  "$ref": "#/components/schemas/ReadOperation"
                },
                {
                  "$ref": "#/components/schemas/TouchOperation"
                }
              ]
            }
          }
        },
        "description": "An array of operation objects specifying the operations to perform on the record",
        "example": {
          "opsList": [
            {
              "type": "ADD",
              "binName": "intBin",
              "incr": 2
            },
            {
              "type": "GET",
              "binName": "intBin"
            }
          ]
        }
      },
      "Operation": {
        "required": [
          "type"
        ],
        "type": "object",
        "properties": {
          "type": {
            "type": "string"
          }
        },
        "description": "The base type for describing all operations. Should not be used directly.",
        "discriminator": {
          "propertyName": "type"
        }
      },
      "PrependOperation": {
        "type": "object",
        "description": "Prepend a `value` to the item in the specified `binName`",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/Operation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/Operation"
          },
          {
            "type": "object",
            "properties": {
              "binName": {
                "type": "string",
                "writeOnly": true
              },
              "value": {
                "type": "string",
                "writeOnly": true
              }
            }
          }
        ]
      },
      "PutOperation": {
        "type": "object",
        "description": "Store a `value` in the specified `binName`.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/Operation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/Operation"
          },
          {
            "type": "object",
            "properties": {
              "binName": {
                "type": "string",
                "writeOnly": true
              },
              "value": {
                "type": "object",
                "writeOnly": true
              }
            }
          }
        ]
      },
      "ReadOperation": {
        "type": "object",
        "description": "Return the value of a specified `binName`.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/Operation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/Operation"
          },
          {
            "type": "object",
            "properties": {
              "binName": {
                "type": "string",
                "writeOnly": true
              }
            }
          }
        ]
      },
      "TouchOperation": {
        "type": "object",
        "description": "Reset a record’s TTL and increment its generation.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/Operation.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/Operation"
          }
        ]
      },
      "OperateResponseRecordBody": {
        "type": "object",
        "properties": {
          "record": {
            "$ref": "#/components/schemas/RestClientRecord"
          }
        }
      },
      "RestClientRecord": {
        "type": "object",
        "properties": {
          "generation": {
            "type": "integer",
            "description": "The generation of the record.",
            "format": "int32",
            "example": 2
          },
          "ttl": {
            "type": "integer",
            "description": "The time to live for the record, in seconds from now.",
            "format": "int32",
            "example": 1000
          },
          "bins": {
            "type": "object",
            "additionalProperties": {
              "type": "object",
              "description": "A mapping from binName to binValue",
              "example": {
                "bin1": "val1",
                "pi": "3.14"
              }
            },
            "description": "A mapping from binName to binValue",
            "example": {
              "bin1": "val1",
              "pi": "3.14"
            }
          }
        },
        "description": "Record associated with the key. Null if the record was not found."
      },
      "ExecuteRequestBody": {
        "type": "object",
        "properties": {
          "opsList": {
            "type": "array",
            "items": {
              "oneOf": [
                {
                  "$ref": "#/components/schemas/AddOperation"
                },
                {
                  "$ref": "#/components/schemas/AppendOperation"
                },
                {
                  "$ref": "#/components/schemas/BitOperation"
                },
                {
                  "$ref": "#/components/schemas/DeleteOperation"
                },
                {
                  "$ref": "#/components/schemas/GetHeaderOperation"
                },
                {
                  "$ref": "#/components/schemas/GetOperation"
                },
                {
                  "$ref": "#/components/schemas/HLLOperation"
                },
                {
                  "$ref": "#/components/schemas/ListOperation"
                },
                {
                  "$ref": "#/components/schemas/MapOperation"
                },
                {
                  "$ref": "#/components/schemas/PrependOperation"
                },
                {
                  "$ref": "#/components/schemas/PutOperation"
                },
                {
                  "$ref": "#/components/schemas/ReadOperation"
                },
                {
                  "$ref": "#/components/schemas/TouchOperation"
                }
              ]
            }
          }
        },
        "description": "An array of operation objects specifying the operations to perform on the record."
      },
      "RestClientExecuteTask": {
        "type": "object",
        "properties": {
          "taskId": {
            "type": "integer",
            "description": "The task ID value.",
            "format": "int64"
          },
          "scan": {
            "type": "boolean",
            "description": "The scan indicator."
          }
        }
      },
      "LngLat": {
        "type": "object",
        "properties": {
          "longitude": {
            "type": "number",
            "format": "double"
          },
          "latitude": {
            "type": "number",
            "format": "double"
          }
        },
        "description": "A 2 element array describing a position of the form [longitude, latitude]",
        "example": [
          37.421331,
          -122.09882
        ]
      },
      "LngLatRad": {
        "type": "object",
        "properties": {
          "latLng": {
            "$ref": "#/components/schemas/LngLat"
          },
          "radius": {
            "type": "number",
            "format": "double"
          }
        },
        "description": "A 2 element array describing a circle of the form [[longitude, latitude], radius].",
        "example": [
          [
            37.421331,
            -122.09882
          ],
          3.14159
        ]
      },
      "QueryContainsLongFilter": {
        "required": [
          "binName",
          "value"
        ],
        "type": "object",
        "description": "Filter for CDTs that contain a long value. Only allowed on bins which has a secondary index defined.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/query/Filter.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/QueryFilter"
          },
          {
            "type": "object",
            "properties": {
              "value": {
                "type": "integer",
                "format": "int64"
              },
              "collectionType": {
                "type": "string",
                "enum": [
                  "DEFAULT",
                  "LIST",
                  "MAPKEYS",
                  "MAPVALUES"
                ]
              }
            }
          }
        ]
      },
      "QueryContainsStringFilter": {
        "required": [
          "binName",
          "value"
        ],
        "type": "object",
        "description": "Filter for CDTs that contain a string value. Only allowed on bins which has a secondary index defined.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/query/Filter.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/QueryFilter"
          },
          {
            "type": "object",
            "properties": {
              "value": {
                "type": "string"
              },
              "collectionType": {
                "type": "string",
                "enum": [
                  "DEFAULT",
                  "LIST",
                  "MAPKEYS",
                  "MAPVALUES"
                ]
              }
            }
          }
        ]
      },
      "QueryEqualLongFilter": {
        "required": [
          "binName",
          "value"
        ],
        "type": "object",
        "description": "Filter for values that equal the provided value.  Only allowed on bins which has a secondary index defined.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/query/Filter.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/QueryFilter"
          },
          {
            "type": "object",
            "properties": {
              "value": {
                "type": "integer",
                "format": "int64"
              }
            }
          }
        ]
      },
      "QueryEqualsStringFilter": {
        "required": [
          "binName",
          "value"
        ],
        "type": "object",
        "description": "Filter for values that equal the provided value.  Only allowed on bins which has a secondary index defined.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/query/Filter.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/QueryFilter"
          },
          {
            "type": "object",
            "properties": {
              "value": {
                "type": "string"
              }
            }
          }
        ]
      },
      "QueryFilter": {
        "required": [
          "binName",
          "type"
        ],
        "type": "object",
        "properties": {
          "binName": {
            "type": "string",
            "description": "The bin for which a secondary-index is defined."
          },
          "ctx": {
            "type": "array",
            "description": "An optional context for elements within a CDT which a secondary-index is defined.",
            "externalDocs": {
              "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/cdt/CTX.html"
            },
            "items": {
              "oneOf": [
                {
                  "$ref": "#/components/schemas/ListIndexCTX"
                },
                {
                  "$ref": "#/components/schemas/ListIndexCreateCTX"
                },
                {
                  "$ref": "#/components/schemas/ListRankCTX"
                },
                {
                  "$ref": "#/components/schemas/ListValueCTX"
                },
                {
                  "$ref": "#/components/schemas/MapIndexCTX"
                },
                {
                  "$ref": "#/components/schemas/MapKeyCTX"
                },
                {
                  "$ref": "#/components/schemas/MapKeyCreateCTX"
                },
                {
                  "$ref": "#/components/schemas/MapRankCTX"
                },
                {
                  "$ref": "#/components/schemas/MapValueCTX"
                }
              ]
            }
          },
          "type": {
            "type": "string"
          }
        },
        "description": "QueryFilter base type. Only allowed on bin which has a secondary index defined.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/query/Filter.html"
        },
        "discriminator": {
          "propertyName": "type"
        },
        "oneOf": [
          {
            "$ref": "#/components/schemas/QueryEqualsStringFilter"
          },
          {
            "$ref": "#/components/schemas/QueryEqualLongFilter"
          },
          {
            "$ref": "#/components/schemas/QueryRangeFilter"
          },
          {
            "$ref": "#/components/schemas/QueryContainsStringFilter"
          },
          {
            "$ref": "#/components/schemas/QueryContainsLongFilter"
          },
          {
            "$ref": "#/components/schemas/QueryGeoWithinPolygonFilter"
          },
          {
            "$ref": "#/components/schemas/QueryGeoWithinRadiusFilter"
          },
          {
            "$ref": "#/components/schemas/QueryGeoContainsPointFilter"
          }
        ]
      },
      "QueryGeoContainsPointFilter": {
        "required": [
          "binName",
          "point"
        ],
        "type": "object",
        "description": "Geospatial filter for regions that contain a point.  Only allowed on bins which has a secondary index defined.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/query/Filter.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/QueryFilter"
          },
          {
            "type": "object",
            "properties": {
              "point": {
                "$ref": "#/components/schemas/LngLat"
              },
              "collectionType": {
                "type": "string",
                "enum": [
                  "DEFAULT",
                  "LIST",
                  "MAPKEYS",
                  "MAPVALUES"
                ]
              }
            }
          }
        ]
      },
      "QueryGeoWithinPolygonFilter": {
        "required": [
          "binName",
          "polygon"
        ],
        "type": "object",
        "description": "Geospatial filter for points contained inside and AeroCircle object.  Only allowed on bin which has a secondary index defined.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/query/Filter.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/QueryFilter"
          },
          {
            "type": "object",
            "properties": {
              "polygon": {
                "type": "array",
                "description": "Array of longitude and latitude describing a region.",
                "items": {
                  "$ref": "#/components/schemas/LngLat"
                }
              },
              "collectionType": {
                "type": "string",
                "enum": [
                  "DEFAULT",
                  "LIST",
                  "MAPKEYS",
                  "MAPVALUES"
                ]
              }
            }
          }
        ]
      },
      "QueryGeoWithinRadiusFilter": {
        "required": [
          "binName",
          "circle"
        ],
        "type": "object",
        "description": "Geospatial filter for points contained inside and AeroCircle object.  Only allowed on bin which has a secondary index defined.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/query/Filter.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/QueryFilter"
          },
          {
            "type": "object",
            "properties": {
              "circle": {
                "$ref": "#/components/schemas/LngLatRad"
              }
            }
          }
        ]
      },
      "QueryRangeFilter": {
        "required": [
          "begin",
          "binName",
          "end"
        ],
        "type": "object",
        "description": "Filter values numeric values within a range. Only allowed on bin which has a secondary index defined.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/query/Filter.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/QueryFilter"
          },
          {
            "type": "object",
            "properties": {
              "begin": {
                "type": "integer",
                "description": "Filter begin value inclusive.",
                "format": "int64"
              },
              "end": {
                "type": "integer",
                "description": "Filter end value inclusive.",
                "format": "int64"
              },
              "collectionType": {
                "type": "string",
                "enum": [
                  "DEFAULT",
                  "LIST",
                  "MAPKEYS",
                  "MAPVALUES"
                ]
              }
            }
          }
        ]
      },
      "QueryRequestBody": {
        "type": "object",
        "properties": {
          "filter": {
            "oneOf": [
              {
                "$ref": "#/components/schemas/QueryContainsLongFilter"
              },
              {
                "$ref": "#/components/schemas/QueryContainsStringFilter"
              },
              {
                "$ref": "#/components/schemas/QueryEqualLongFilter"
              },
              {
                "$ref": "#/components/schemas/QueryEqualsStringFilter"
              },
              {
                "$ref": "#/components/schemas/QueryGeoContainsPointFilter"
              },
              {
                "$ref": "#/components/schemas/QueryGeoWithinPolygonFilter"
              },
              {
                "$ref": "#/components/schemas/QueryGeoWithinRadiusFilter"
              },
              {
                "$ref": "#/components/schemas/QueryRangeFilter"
              }
            ]
          },
          "from": {
            "type": "string",
            "description": "Pagination token returned from last query request used to retrieve the next page. Use \"getToken\" to retrieve this token."
          }
        },
        "description": "Body of Query request."
      },
      "Pagination": {
        "type": "object",
        "properties": {
          "nextToken": {
            "type": "string",
            "description": "The next page token."
          },
          "totalRecords": {
            "type": "integer",
            "description": "The total number of records in page.",
            "format": "int64"
          }
        },
        "description": "Pagination details."
      },
      "QueryResponseBody": {
        "required": [
          "records"
        ],
        "type": "object",
        "properties": {
          "records": {
            "type": "array",
            "description": "Records returned from query.",
            "items": {
              "$ref": "#/components/schemas/RestClientKeyRecord"
            }
          },
          "pagination": {
            "$ref": "#/components/schemas/Pagination"
          }
        },
        "description": "Body of query response."
      },
      "RestClientKeyRecord": {
        "required": [
          "userKey"
        ],
        "type": "object",
        "properties": {
          "userKey": {
            "type": "object",
            "description": "The user key, it may be a string, integer, or URL safe Base64 encoded bytes.",
            "example": "userKey"
          },
          "generation": {
            "type": "integer",
            "description": "The generation of the record.",
            "format": "int32",
            "example": 2
          },
          "ttl": {
            "type": "integer",
            "description": "The time to live for the record, in seconds from now.",
            "format": "int32",
            "example": 1000
          },
          "bins": {
            "type": "object",
            "additionalProperties": {
              "type": "object",
              "description": "A mapping from binName to binValue",
              "example": {
                "bin1": "val1",
                "pi": "3.14"
              }
            },
            "description": "A mapping from binName to binValue",
            "example": {
              "bin1": "val1",
              "pi": "3.14"
            }
          }
        },
        "description": "Records returned from query."
      },
      "RestClientOperation": {
        "required": [
          "opValues",
          "operation"
        ],
        "type": "object",
        "properties": {
          "operation": {
            "type": "string",
            "description": "Aerospike operation to perform on the record",
            "example": "LIST_APPEND_ITEMS",
            "enum": [
              "ADD",
              "APPEND",
              "GET",
              "PREPEND",
              "READ",
              "GET_HEADER",
              "TOUCH",
              "PUT",
              "DELETE",
              "LIST_APPEND",
              "LIST_APPEND_ITEMS",
              "LIST_CLEAR",
              "LIST_GET",
              "LIST_GET_BY_INDEX",
              "LIST_GET_BY_INDEX_RANGE",
              "LIST_GET_BY_RANK",
              "LIST_GET_BY_RANK_RANGE",
              "LIST_GET_BY_VALUE_REL_RANK_RANGE",
              "LIST_GET_BY_VALUE",
              "LIST_GET_BY_VALUE_RANGE",
              "LIST_GET_BY_VALUE_LIST",
              "LIST_GET_RANGE",
              "LIST_INCREMENT",
              "LIST_INSERT",
              "LIST_INSERT_ITEMS",
              "LIST_POP",
              "LIST_POP_RANGE",
              "LIST_REMOVE",
              "LIST_REMOVE_BY_INDEX",
              "LIST_REMOVE_BY_INDEX_RANGE",
              "LIST_REMOVE_BY_RANK",
              "LIST_REMOVE_BY_RANK_RANGE",
              "LIST_REMOVE_BY_VALUE_REL_RANK_RANGE",
              "LIST_REMOVE_BY_VALUE",
              "LIST_REMOVE_BY_VALUE_RANGE",
              "LIST_REMOVE_BY_VALUE_LIST",
              "LIST_REMOVE_RANGE",
              "LIST_SET",
              "LIST_SET_ORDER",
              "LIST_SIZE",
              "LIST_SORT",
              "LIST_TRIM",
              "LIST_CREATE",
              "MAP_CLEAR",
              "MAP_DECREMENT",
              "MAP_GET_BY_INDEX",
              "MAP_GET_BY_INDEX_RANGE",
              "MAP_GET_BY_KEY",
              "MAP_GET_BY_KEY_LIST",
              "MAP_GET_BY_KEY_RANGE",
              "MAP_GET_BY_RANK",
              "MAP_GET_BY_RANK_RANGE",
              "MAP_GET_BY_VALUE",
              "MAP_GET_BY_VALUE_RANGE",
              "MAP_GET_BY_VALUE_LIST",
              "MAP_GET_BY_KEY_REL_INDEX_RANGE",
              "MAP_GET_BY_VALUE_REL_RANK_RANGE",
              "MAP_INCREMENT",
              "MAP_PUT",
              "MAP_PUT_ITEMS",
              "MAP_REMOVE_BY_INDEX",
              "MAP_REMOVE_BY_INDEX_RANGE",
              "MAP_REMOVE_BY_KEY",
              "MAP_REMOVE_BY_KEY_RANGE",
              "MAP_REMOVE_BY_RANK",
              "MAP_REMOVE_BY_RANK_RANGE",
              "MAP_REMOVE_BY_KEY_REL_INDEX_RANGE",
              "MAP_REMOVE_BY_VALUE_REL_RANK_RANGE",
              "MAP_REMOVE_BY_VALUE",
              "MAP_REMOVE_BY_VALUE_RANGE",
              "MAP_REMOVE_BY_VALUE_LIST",
              "MAP_SET_MAP_POLICY",
              "MAP_SIZE",
              "MAP_CREATE",
              "BIT_RESIZE",
              "BIT_INSERT",
              "BIT_REMOVE",
              "BIT_SET",
              "BIT_OR",
              "BIT_XOR",
              "BIT_AND",
              "BIT_NOT",
              "BIT_LSHIFT",
              "BIT_RSHIFT",
              "BIT_ADD",
              "BIT_SUBTRACT",
              "BIT_SET_INT",
              "BIT_GET",
              "BIT_COUNT",
              "BIT_LSCAN",
              "BIT_RSCAN",
              "BIT_GET_INT",
              "HLL_INIT",
              "HLL_ADD",
              "HLL_SET_UNION",
              "HLL_SET_COUNT",
              "HLL_FOLD",
              "HLL_COUNT",
              "HLL_UNION",
              "HLL_UNION_COUNT",
              "HLL_INTERSECT_COUNT",
              "HLL_SIMILARITY",
              "HLL_DESCRIBE"
            ]
          },
          "opValues": {
            "type": "object",
            "additionalProperties": {
              "type": "object",
              "example": {
                "bin": "listbin",
                "values": [
                  1,
                  2,
                  3
                ]
              }
            },
            "example": {
              "bin": "listbin",
              "values": [
                1,
                2,
                3
              ]
            }
          }
        },
        "description": "Deprecated in favor of more descriptive models.  The documentation for the old models can be found in the external documentation.",
        "externalDocs": {
          "url": "@Schema(description \u003d \"Deprecated in favor of more descriptive models.  The documentation for the old models can be found in the external documentation.\", externalDocs \u003d @ExternalDocumentation(url\u003d \"\"))\n"
        }
      },
      "RestClientIndex": {
        "required": [
          "bin",
          "name",
          "namespace",
          "type"
        ],
        "type": "object",
        "properties": {
          "type": {
            "type": "string",
            "enum": [
              "NUMERIC",
              "STRING",
              "GEO2DSPHERE"
            ]
          },
          "name": {
            "type": "string",
            "description": "The name of the index. This must be unique per set",
            "example": "ageIndex"
          },
          "namespace": {
            "type": "string",
            "example": "testNS"
          },
          "bin": {
            "type": "string",
            "description": "The bin which is indexed",
            "example": "ageBin"
          },
          "set": {
            "type": "string",
            "example": "testSet"
          },
          "ctx": {
            "type": "array",
            "items": {
              "oneOf": [
                {
                  "$ref": "#/components/schemas/ListIndexCTX"
                },
                {
                  "$ref": "#/components/schemas/ListIndexCreateCTX"
                },
                {
                  "$ref": "#/components/schemas/ListRankCTX"
                },
                {
                  "$ref": "#/components/schemas/ListValueCTX"
                },
                {
                  "$ref": "#/components/schemas/MapIndexCTX"
                },
                {
                  "$ref": "#/components/schemas/MapKeyCTX"
                },
                {
                  "$ref": "#/components/schemas/MapKeyCreateCTX"
                },
                {
                  "$ref": "#/components/schemas/MapRankCTX"
                },
                {
                  "$ref": "#/components/schemas/MapValueCTX"
                }
              ]
            }
          },
          "collection_type": {
            "type": "string",
            "enum": [
              "DEFAULT",
              "LIST",
              "MAPKEYS",
              "MAPVALUES"
            ]
          }
        }
      },
      "BatchDelete": {
        "required": [
          "key",
          "type"
        ],
        "type": "object",
        "description": "An object that describes a batch delete operation to be used in a batch request.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/BatchDelete.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/BatchRecord"
          },
          {
            "type": "object",
            "properties": {
              "type": {
                "type": "string",
                "description": "The type of batch request. It is always DELETE",
                "enum": [
                  "DELETE"
                ]
              },
              "policy": {
                "$ref": "#/components/schemas/BatchDeletePolicy"
              }
            }
          }
        ]
      },
      "BatchDeletePolicy": {
        "type": "object",
        "properties": {
          "filterExp": {
            "type": "string",
            "description": "Optional expression filter. If filterExp exists and evaluates to false, the specific batch key request is not performed and RecordClientBatchRecordResponse.ResultCode is set to FILTERED_OUT. If exists, this filter overrides the batch parent policy filterExp for the specific key in batch commands that allow a different policy per key. Otherwise, this filter is ignored."
          },
          "commitLevel": {
            "type": "string",
            "description": "Desired consistency guarantee when committing a transaction on the server. The default (COMMIT_ALL) indicates that the server should wait for master and all replica commits to be successful before returning success to the client.",
            "enum": [
              "COMMIT_ALL",
              "COMMIT_MASTER"
            ]
          },
          "generationPolicy": {
            "type": "string",
            "description": "Qualify how to handle record deletes based on record generation. The default (NONE) indicates that the generation is not used to restrict deletes.",
            "enum": [
              "NONE",
              "EXPECT_GEN_EQUAL",
              "EXPECT_GEN_GT"
            ]
          },
          "generation": {
            "type": "integer",
            "description": "Expected generation. Generation is the number of times a record has been modified (including creation) on the server.\nThis field is only relevant when generationPolicy is not NONE.",
            "format": "int32"
          },
          "durableDelete": {
            "type": "boolean",
            "description": "If the transaction results in a record deletion, leave a tombstone for the record. This prevents deleted records from reappearing after node failures. Valid for Aerospike Server Enterprise Edition only."
          },
          "sendKey": {
            "type": "boolean",
            "description": "Send user defined key in addition to hash digest. If true, the key will be stored with the record on the server."
          }
        },
        "description": "An object that describes a policy for a delete operation used in a batch request.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/policy/BatchDeletePolicy.html"
        }
      },
      "BatchRead": {
        "required": [
          "key",
          "type"
        ],
        "type": "object",
        "description": "An object that describes a batch read operation to be used in a batch request.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/BatchRead.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/BatchRecord"
          },
          {
            "type": "object",
            "properties": {
              "type": {
                "type": "string",
                "description": "The type of batch request. It is always READ",
                "enum": [
                  "READ"
                ]
              },
              "readAllBins": {
                "type": "boolean",
                "description": "Whether all bins should be returned with this record."
              },
              "opsList": {
                "type": "array",
                "description": "List of operation. Useful for reading from nested CDTs.",
                "items": {
                  "$ref": "#/components/schemas/Operation"
                }
              },
              "binNames": {
                "type": "array",
                "description": "List of bins to limit the record response to.",
                "example": [
                  "bin1"
                ],
                "items": {
                  "type": "string",
                  "description": "List of bins to limit the record response to.",
                  "example": "[\"bin1\"]"
                }
              },
              "policy": {
                "$ref": "#/components/schemas/BatchReadPolicy"
              }
            }
          }
        ]
      },
      "BatchReadPolicy": {
        "type": "object",
        "properties": {
          "filterExp": {
            "type": "string",
            "description": "Optional expression filter. If filterExp exists and evaluates to false, the specific batch key request is not performed and RecordClientBatchRecordResponse.ResultCode is set to FILTERED_OUT. If exists, this filter overrides the batch parent policy filterExp for the specific key in batch commands that allow a different policy per key. Otherwise, this filter is ignored."
          },
          "readModeAP": {
            "type": "string",
            "description": "Read policy for AP (availability) namespaces. How duplicates should be consulted in a read operation. Only makes a difference during migrations and only applicable in AP mode.",
            "enum": [
              "ONE",
              "ALL"
            ]
          },
          "readModeSC": {
            "type": "string",
            "description": "Read policy for SC (strong consistency) namespaces. Determines SC read consistency options.",
            "enum": [
              "SESSION",
              "LINEARIZE",
              "ALLOW_REPLICA",
              "ALLOW_UNAVAILABLE"
            ]
          }
        },
        "description": "An object that describes a policy for a read operation used in a batch request.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/policy/BatchReadPolicy.html"
        }
      },
      "BatchRecord": {
        "required": [
          "key",
          "type"
        ],
        "type": "object",
        "properties": {
          "key": {
            "$ref": "#/components/schemas/RestClientKey"
          },
          "type": {
            "type": "string"
          }
        },
        "description": "The batch operation base type.",
        "discriminator": {
          "propertyName": "type"
        },
        "oneOf": [
          {
            "$ref": "#/components/schemas/BatchRead"
          },
          {
            "$ref": "#/components/schemas/BatchWrite"
          },
          {
            "$ref": "#/components/schemas/BatchDelete"
          },
          {
            "$ref": "#/components/schemas/BatchUDF"
          }
        ]
      },
      "BatchUDF": {
        "required": [
          "functionName",
          "key",
          "packageName",
          "type"
        ],
        "type": "object",
        "description": "An object that describes a batch udf operation to be used in a batch request.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/BatchUDF.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/BatchRecord"
          },
          {
            "type": "object",
            "properties": {
              "type": {
                "type": "string",
                "description": "List of bins to limit the record response to.",
                "enum": [
                  "UDF"
                ]
              },
              "packageName": {
                "type": "string",
                "description": "Package or lua module name."
              },
              "functionName": {
                "type": "string",
                "description": "Package or lua module name."
              },
              "functionArgs": {
                "type": "array",
                "description": "Optional arguments to lua function.",
                "items": {
                  "type": "object",
                  "description": "Optional arguments to lua function."
                }
              },
              "policy": {
                "$ref": "#/components/schemas/BatchUDFPolicy"
              }
            }
          }
        ]
      },
      "BatchUDFPolicy": {
        "type": "object",
        "properties": {
          "filterExp": {
            "type": "string",
            "description": "Optional expression filter. If filterExp exists and evaluates to false, the specific batch key request is not performed and RecordClientBatchRecordResponse.ResultCode is set to FILTERED_OUT. If exists, this filter overrides the batch parent policy filterExp for the specific key in batch commands that allow a different policy per key. Otherwise, this filter is ignored."
          },
          "commitLevel": {
            "type": "string",
            "description": "Desired consistency guarantee when committing a transaction on the server. The default (COMMIT_ALL) indicates that the server should wait for master and all replica commits to be successful before returning success to the client.",
            "enum": [
              "COMMIT_ALL",
              "COMMIT_MASTER"
            ]
          },
          "expiration": {
            "type": "integer",
            "description": "Record expiration. Also known as ttl (time to live). Seconds record will live before being removed by the server.\nExpiration values:\n   1. -2: Do not change ttl when record is updated.\n   2. -1: Never expire.\n   3. 0: Default to namespace configuration variable \"default-ttl\" on the server.\n   4. greater than 0: Actual ttl in seconds.",
            "format": "int32"
          },
          "durableDelete": {
            "type": "boolean",
            "description": "If the transaction results in a record deletion, leave a tombstone for the record. This prevents deleted records from reappearing after node failures. Valid for Aerospike Server Enterprise Edition only."
          },
          "sendKey": {
            "type": "boolean",
            "description": "Send user defined key in addition to hash digest. If true, the key will be stored with the record on the server."
          }
        },
        "description": "An object that describes a policy for a udf operation used in a batch request.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/policy/BatchUDFPolicy.html"
        }
      },
      "BatchWrite": {
        "required": [
          "key",
          "type"
        ],
        "type": "object",
        "description": "An object that describes a batch write operation to be used in a batch request.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/BatchWrite.html"
        },
        "allOf": [
          {
            "$ref": "#/components/schemas/BatchRecord"
          },
          {
            "type": "object",
            "properties": {
              "type": {
                "type": "string",
                "description": "List of bins to limit the record response to.",
                "enum": [
                  "WRITE"
                ]
              },
              "opsList": {
                "type": "array",
                "description": "List of operation.",
                "items": {
                  "$ref": "#/components/schemas/Operation"
                }
              },
              "policy": {
                "$ref": "#/components/schemas/BatchWritePolicy"
              }
            }
          }
        ]
      },
      "BatchWritePolicy": {
        "type": "object",
        "properties": {
          "filterExp": {
            "type": "string",
            "description": "Optional expression filter. If filterExp exists and evaluates to false, the specific batch key request is not performed and RecordClientBatchRecordResponse.ResultCode is set to FILTERED_OUT. If exists, this filter overrides the batch parent policy filterExp for the specific key in batch commands that allow a different policy per key. Otherwise, this filter is ignored."
          },
          "recordExistsAction": {
            "type": "string",
            "description": "Qualify how to handle writes where the record already exists.",
            "enum": [
              "UPDATE",
              "UPDATE_ONLY",
              "REPLACE",
              "REPLACE_ONLY",
              "CREATE_ONLY"
            ]
          },
          "commitLevel": {
            "type": "string",
            "description": "Desired consistency guarantee when committing a transaction on the server. The default (COMMIT_ALL) indicates that the server should wait for master and all replica commits to be successful before returning success to the client.",
            "enum": [
              "COMMIT_ALL",
              "COMMIT_MASTER"
            ]
          },
          "generationPolicy": {
            "type": "string",
            "description": "Qualify how to handle record deletes based on record generation. The default (NONE) indicates that the generation is not used to restrict deletes.",
            "enum": [
              "NONE",
              "EXPECT_GEN_EQUAL",
              "EXPECT_GEN_GT"
            ]
          },
          "generation": {
            "type": "integer",
            "description": "Expected generation. Generation is the number of times a record has been modified (including creation) on the server.\nThis field is only relevant when generationPolicy is not NONE.",
            "format": "int32"
          },
          "expiration": {
            "type": "integer",
            "description": "Record expiration. Also known as ttl (time to live). Seconds record will live before being removed by the server.\nExpiration values:\n   1. -2: Do not change ttl when record is updated.\n   2. -1: Never expire.\n   3. 0: Default to namespace configuration variable \"default-ttl\" on the server.\n   4. greater than 0: Actual ttl in seconds.",
            "format": "int32"
          },
          "durableDelete": {
            "type": "boolean",
            "description": "If the transaction results in a record deletion, leave a tombstone for the record. This prevents deleted records from reappearing after node failures. Valid for Aerospike Server Enterprise Edition only."
          },
          "sendKey": {
            "type": "boolean",
            "description": "Send user defined key in addition to hash digest. If true, the key will be stored with the record on the server."
          }
        },
        "description": "An object that describes a policy for a write operation used in a batch request.",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/policy/BatchWritePolicy.html"
        }
      },
      "RestClientKey": {
        "required": [
          "namespace",
          "userKey"
        ],
        "type": "object",
        "properties": {
          "namespace": {
            "type": "string",
            "example": "test"
          },
          "userKey": {
            "type": "object",
            "description": "The user key, it may be a string, integer, or URL safe Base64 encoded bytes.",
            "example": "userKey"
          },
          "setName": {
            "type": "string",
            "example": "testSet"
          },
          "keytype": {
            "type": "string",
            "description": "Enum describing the type of the userKey. This field is omitted in MessagePack responses.",
            "example": "STRING",
            "enum": [
              "STRING",
              "INTEGER",
              "BYTES",
              "DIGEST"
            ]
          }
        },
        "description": "Key to retrieve a record."
      },
      "BatchRecordResponse": {
        "type": "object",
        "properties": {
          "resultCode": {
            "type": "integer",
            "description": "Result code for this returned record.",
            "format": "int32"
          },
          "resultCodeString": {
            "type": "string",
            "description": "Message associated with resultCode."
          },
          "record": {
            "$ref": "#/components/schemas/RestClientRecord"
          },
          "key": {
            "$ref": "#/components/schemas/RestClientKey"
          },
          "inDoubt": {
            "type": "boolean",
            "description": "Is it possible that the write transaction may have completed even though an error occurred for this record."
          }
        },
        "description": "Object returned in from a single batch operation",
        "externalDocs": {
          "url": "https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/com/aerospike/client/BatchRecord.html"
        }
      },
      "RestClientUserModel": {
        "required": [
          "password",
          "roles",
          "username"
        ],
        "type": "object",
        "properties": {
          "username": {
            "type": "string"
          },
          "password": {
            "type": "string"
          },
          "roles": {
            "type": "array",
            "items": {
              "type": "string"
            }
          }
        }
      },
      "RestClientPrivilege": {
        "required": [
          "code"
        ],
        "type": "object",
        "properties": {
          "code": {
            "type": "string",
            "enum": [
              "user-admin",
              "sys-admin",
              "data-admin",
              "udf-admin",
              "sindex-admin",
              "read",
              "read-write",
              "read-write-udf",
              "write",
              "truncate"
            ]
          },
          "namespace": {
            "type": "string",
            "description": "Namespace Scope",
            "example": "test"
          },
          "set": {
            "type": "string",
            "description": "setName Scope",
            "example": "testSet"
          }
        },
        "description": "List of assigned privileges."
      },
      "RestClientRole": {
        "required": [
          "name",
          "privileges"
        ],
        "type": "object",
        "properties": {
          "name": {
            "type": "string",
            "description": "Role name.",
            "example": "customRole"
          },
          "privileges": {
            "type": "array",
            "description": "List of assigned privileges.",
            "items": {
              "$ref": "#/components/schemas/RestClientPrivilege"
            }
          },
          "whitelist": {
            "type": "array",
            "description": "List of allowable IP addresses.",
            "items": {
              "type": "string",
              "description": "List of allowable IP addresses."
            }
          },
          "readQuota": {
            "type": "integer",
            "description": "Maximum reads per second limit.",
            "format": "int32"
          },
          "writeQuota": {
            "type": "integer",
            "description": "Maximum writes per second limit.",
            "format": "int32"
          }
        }
      },
      "RestClientRoleQuota": {
        "type": "object",
        "properties": {
          "readQuota": {
            "type": "integer",
            "description": "Maximum reads per second limit.",
            "format": "int32"
          },
          "writeQuota": {
            "type": "integer",
            "description": "Maximum writes per second limit.",
            "format": "int32"
          }
        }
      },
      "RestClientExecuteTaskStatus": {
        "type": "object",
        "properties": {
          "task": {
            "$ref": "#/components/schemas/RestClientExecuteTask"
          },
          "status": {
            "type": "string",
            "description": "The ExecuteTask status."
          }
        }
      },
      "RestClientScanResponse": {
        "type": "object",
        "properties": {
          "records": {
            "type": "array",
            "description": "List of records for current page.",
            "items": {
              "$ref": "#/components/schemas/RestClientKeyRecord"
            }
          },
          "pagination": {
            "$ref": "#/components/schemas/Pagination"
          }
        }
      },
      "User": {
        "type": "object",
        "properties": {
          "name": {
            "type": "string"
          },
          "roles": {
            "type": "array",
            "items": {
              "type": "string"
            }
          },
          "readInfo": {
            "type": "array",
            "items": {
              "type": "integer",
              "format": "int32"
            }
          },
          "writeInfo": {
            "type": "array",
            "items": {
              "type": "integer",
              "format": "int32"
            }
          },
          "connsInUse": {
            "type": "integer",
            "format": "int32"
          }
        }
      }
    }
  }
}