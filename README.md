# Aerospike REST Client

[![Build](https://github.com/aerospike/aerospike-client-rest/actions/workflows/build.yml/badge.svg)](https://github.com/aerospike/aerospike-client-rest/actions/workflows/build.yml)

The Aerospike REST client provides a server which translates Restful API requests into messages to an Aerospike Cluster.

It can be used as a bridge between applications written in languages without an existing Aerospike Client library, or as a pluggable component in a pre-existing architecture.

## Getting Started blog posts

1. [Aerospike REST Client Introduction](https://medium.com/aerospike-developer-blog/aerospike-rest-client-cb7e5967f423)
2. [Dealing with Predicate Expression Filters in Aerospike REST Client (Part 1)](https://medium.com/aerospike-developer-blog/dealing-with-predicate-expression-filters-in-aerospike-rest-client-part-1-a43e43ac8c7d)
3. [Dealing with Predicate Expression Filters in Aerospike REST Client (Part 2)](https://medium.com/aerospike-developer-blog/dealing-with-predicate-expression-filters-in-aerospike-rest-client-part-2-b9d9358c8a4e)
4. [Authentication and Authorization using Aerospike REST client](https://medium.com/aerospike-developer-blog/authentication-and-authorization-using-aerospike-rest-client-ae0837301775)

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

## Data Formats and API

### Formats

The Aerospike REST client allows communication utilizing `JSON` and `MessagePack` formats. For more information about how to specify the format, and recommended usages of each, see [Data Formats](./docs/data-formats.md).

### Interactive UI and Swagger Specification

After installing and starting the REST client, you can try out the API using an interactive frontend powered by Swagger UI. The interactive documentation is located at: `http://<API_ENDPOINT>:8080/swagger-ui.html`.

The Swagger `.JSON` specification of the API is available at: `http://<API_ENDPOINT>:8080/v2/api-docs` .

So if the REST Client is running on localhost these URLs would be `http://localhost:8080/swagger-ui.html` and `http://localhost:8080/v2/api-docs`.

## License
Licensed under the [Apache 2.0 License](./LICENSE).
