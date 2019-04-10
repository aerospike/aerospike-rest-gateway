# aerospike-client-rest

The Aerospike Rest client provides a server which translates Restful API requests into messages to an Aerospike Cluster.

It can be used as a bridge between applications written in languages without an existing Aerospike Client library, or as a pluggable component in a pre-existing architecture.

## Installation

For instructions on installing the Rest Client from a `.war` file see [Installation and Configuration](./docs/installation-and-config.md) .

## Building

To build a `.war` file for the REST client, run

```sh
./gradlew bootWar
```

This will place the file in the `build/libs` directory.

## Running Locally

To run the REST Client locally during development:

```sh
./gradlew bootRun
```

See [Installation and Configuration](./docs/installation-and-config.md) for configuration directions.

## Data Formats and API

### Formats

The Aerospike Rest client allows communication utilizing `JSON` and `MessagePack` formats. For more information about how to specify the format, and recommended usages of each, see [Data Formats](./docs/data-formats.md)

### Interactive UI and Swagger Specification

After installing and starting the Rest client, you can try out the API using an interactive frontend powered by Swagger UI. The interactive documentation is located at: `http://<API_ENDPOINT>:8080/swagger-ui.html`

The Swagger `.JSON` specification of the API is available at: `http://<API_ENDPOINT>:8080/v2/api-docs` .

So if the Rest Client is running on localhost these URLs would be `http://localhost:8080/swagger-ui.html` and `http://localhost:8080/v2/api-docs`.