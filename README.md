# Aerospike REST Client

[![Build](https://github.com/aerospike/aerospike-client-rest/actions/workflows/build.yml/badge.svg)](https://github.com/aerospike/aerospike-client-rest/actions/workflows/build.yml)

The Aerospike REST client provides a server which translates Restful API requests into messages to an Aerospike Cluster.

It can be used as a bridge between applications written in languages without an existing Aerospike Client library, or as a pluggable component in a pre-existing architecture.

## API Documentation

Swagger UI API documentation for this project can be found here [Aerospike REST Client API Documentation](https://aerospike.github.io/aerospike-rest-gateway/).

## Getting Started blog posts

1. [Aerospike REST Client Introduction](https://medium.com/aerospike-developer-blog/aerospike-rest-client-cb7e5967f423?source=friends_link&sk=0d6d69703e8a77da13ec0c6c012d1c29)
2. [Dealing with Predicate Expression Filters in Aerospike REST Client (Part 1)](https://medium.com/aerospike-developer-blog/dealing-with-predicate-expression-filters-in-aerospike-rest-client-part-1-a43e43ac8c7d?source=friends_link&sk=bc0ed64110578ff6f4804753ca6369da)
3. [Dealing with Predicate Expression Filters in Aerospike REST Client (Part 2)](https://medium.com/aerospike-developer-blog/dealing-with-predicate-expression-filters-in-aerospike-rest-client-part-2-b9d9358c8a4e?source=friends_link&sk=35c37b035d12789aae6272704ef95829)
4. [Authentication and Authorization using Aerospike REST client](https://medium.com/aerospike-developer-blog/authentication-and-authorization-using-aerospike-rest-client-ae0837301775?source=friends_link&sk=4be1513a1158a8ecb0b3c0e163ba1c4b)

## Prerequisites

* Java 8 or later
* Aerospike Server version 4.9+

## Installation

For instructions on installing the Rest Client see [Installation and Configuration](./docs/installation-and-config.md) .

## Build

* Executable Jar

```sh
./gradlew build
```

This will place the file in the `build/libs` directory.

* Docker Image

```sh
docker build -t aerospike-client-rest .
```

## Running 

See [Installation and Configuration](./docs/installation-and-config.md) for installation, configuration and running manual.

## Authentication
Use [Basic access authentication](https://en.wikipedia.org/wiki/Basic_access_authentication) to enable multi-user tenancy in the Aerospike REST client.  
Please note that only Aerospike Enterprise Edition supports the [security features](https://aerospike.com/docs/guide/security/index.html).

For example, having the default `admin:admin` credentials, send the `Authorization: Basic YWRtaW46YWRtaW4=` header with the request to make an authenticated query.

## Formats

The Aerospike REST client allows communication utilizing `JSON` and `MessagePack` formats. For more information about how to specify the format, and recommended usages of each, see [Data Formats](./docs/data-formats.md).

## Generate Servers/Clients
Swagger tools such as [Swagger Codegen](https://swagger.io/tools/swagger-codegen/) and [Swagger Editor](https://editor.swagger.io/)
allow you to automatically generate a server/client that integrates with Aerospike REST Client in a variety of different languages.

### Generate Servers/Clients using the Swagger Editor
1. Go to [Swagger Editor](https://editor.swagger.io/).
2. Import the Aerospike REST Client [openapi.json](https://github.com/aerospike/aerospike-client-rest/blob/master/docs/openapi.json) file.
3. Click on Generate Server/Generate Client and choose the desired language/framework.
4. A .zip file that contains all the necessary files for a server/client will be downloaded.

* Note: when generating a server the implementations/logic of the APIs are missing since the server generator only
    generates stub methods (to reduce boilerplate server code).

## License
Licensed under the [Apache 2.0 License](./LICENSE).
