# Aerospike REST Gateway

[![Build](https://github.com/aerospike/aerospike-rest-gateway/actions/workflows/build.yml/badge.svg)](https://github.com/aerospike/aerospike-rest-gateway/actions/workflows/build.yml)

The Aerospike REST gateway provides a server which translates Restful API requests into messages to an Aerospike
Cluster.

It can be used as a bridge between applications written in languages without an existing Aerospike Client library, or as
a pluggable component in a pre-existing architecture.

## API Documentation

Swagger UI API documentation for this project can be found
here [Aerospike REST Gateway API Documentation](https://docs.aerospike.com/apidocs/rest).

## Getting Started blog posts

1. [Aerospike REST Gateway Introduction](https://medium.com/aerospike-developer-blog/aerospike-rest-client-cb7e5967f423?source=friends_link&sk=0d6d69703e8a77da13ec0c6c012d1c29)
4. [Authentication and Authorization using Aerospike REST Gateway](https://medium.com/aerospike-developer-blog/authentication-and-authorization-using-aerospike-rest-client-ae0837301775?source=friends_link&sk=4be1513a1158a8ecb0b3c0e163ba1c4b)

## Prerequisites

* Java 17 or later for REST Gateway 2.0 and newer. Java 8 or later for REST Gateway 1.11 and earlier.
* Aerospike Server version 4.9+

## Installation

For instructions on installing the Rest Gateway see [Installation and Configuration](./docs/installation-and-config.md)
.

## Build

* Executable Jar

```sh
make build
```

This will place the file in the `build/libs` directory.

* Docker Image

```sh
docker build -t aerospike-rest-gateway .
```

## Running

See [Installation and Configuration](./docs/installation-and-config.md) for installation, configuration and running
manual.

## Authentication

Use [Basic access authentication](https://en.wikipedia.org/wiki/Basic_access_authentication) to enable multi-user
tenancy in the Aerospike REST gateway.  
Please note that only Aerospike Enterprise Edition supports
the [security features](https://aerospike.com/docs/guide/security/index.html).

For example, having the default `admin:admin` credentials, send the `Authorization: Basic YWRtaW46YWRtaW4=` header with
the request to make an authenticated query.

## Formats

The Aerospike REST gateway allows communication utilizing `JSON` and `MessagePack` formats. For more information about
how to specify the format, and recommended usages of each, see [Data Formats](./docs/data-formats.md).

## Expressions

The Aerospike REST gateway 1.7.0 and newer supports filter expressions added in server 5.6. The now deprecated predicate
expressions are supported in REST gateway 1.11.0 and older.

**Note:** The DSL used to define predicate expressions can also be used to define filter expressions.

See [Expressions](./docs/expressions.md) for more information.

## Generate Servers/Clients

Swagger tools such as [Swagger Codegen](https://swagger.io/tools/swagger-codegen/)
and [Swagger Editor](https://editor.swagger.io/)
allow you to automatically generate a server/client that integrates with Aerospike REST Gateway in a variety of
different
languages.

### Generate Servers/Clients using the Swagger Editor

1. Go to [Swagger Editor](https://editor.swagger.io/).
2. Import the Aerospike REST
   Client [openapi.json](https://aerospike.github.io/aerospike-rest-gateway/openapi.json) file.
3. Click on Generate Server/Generate Client and choose the desired language/framework.
4. A .zip file that contains all the necessary files for a server/client will be downloaded.

* Note: when generating a server the implementations/logic of the APIs are missing since the server generator only
  generates stub methods (to reduce boilerplate server code).

## License

Licensed under the [Apache 2.0 License](./LICENSE).
