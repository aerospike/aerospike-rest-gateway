# Installation and Configuration

## Installation

### Requirements

* The Rest Client requires Java 8.
* The Rest Client requires an Aerospike Server to be installed and reachable. See [Configuration](#configuration) for details on specifying the location of this server.

### Build and development

* Build 
```
./gradlew build
```
* Run Locally during development:

```sh
./gradlew bootRun
```

### Run from Jar file
```
java -jar build/libs/aerospike-client-rest-<VERSION>.jar
```
The fully executable jar contains an extra script at the front of the file, which allows you to just symlink your Spring Boot jar to init.d or use a systemd script.  
More information at the following links:
* [Installation as an init.d service](https://docs.spring.io/spring-boot/docs/2.2.6.RELEASE/reference/htmlsingle/#deployment-service)
* [Installation as a systemd service](https://docs.spring.io/spring-boot/docs/2.2.6.RELEASE/reference/htmlsingle/#deployment-systemd-service)

### Run using Docker
* Build the docker image
```
docker build -t aerospike-client-rest .
```

* Run the REST Client using docker
```sh
docker run -itd --rm -p 8080:8080 --name AS_Rest1 -e aerospike_restclient_hostname=172.17.0.3 aerospike-client-rest
```

### Run on Kubernetes
* Use the official [Helm chart](https://github.com/aerospike/aerospike-client-rest-kubernetes) to deploy the Aerospike REST Client to Kubernetes.

### Verifying installation

**Note:** The following steps assume REST Client's base path is `http://localhost:8080/` if this is not the case, the provided URLs will need to be modified accordingly.

To test that the rest client is up and running, and connected to the Aerospike database you can run:

    curl http://localhost:8080/v1/cluster

This will return basic information about the cluster.

Interactive API documentation may be found at <http://localhost:8080/swagger-ui.html> . This will allow you to
test out various commands in your browser.

The Swagger specification, in `JSON` format, can be found at <http://localhost:8080/v2/api-docs> .

## Configuration

* `server.port` Change the port the REST Client is listening on (default: 8080)
* `aerospike.restclient.hostname` The IP address or Hostname of a seed node in the cluster (default: `localhost`)
**Note:** If TLS is being utilized, `aerospike.restclient.hostlist` should be used instead of this variable.
* `aerospike.restclient.port` The port to communicate with the Aerospike cluster over. (default: `3000`)
* `aerospike.restclient.hostlist` A comma separated list of cluster hostnames, (optional TLS names) and ports. If this is specified, it overrides the previous two environment variables. The format is described below:

``` None
    The string format is : hostname1[:tlsname1][:port1],...
    * Hostname may also be an IP address in the following formats.
    *
    * IPv4: xxx.xxx.xxx.xxx
    * IPv6: [xxxx:xxxx:xxxx:xxxx:xxxx:xxxx:xxxx:xxxx]
    * IPv6: [xxxx::xxxx]
    *
    * IPv6 addresses must be enclosed by brackets.
    * tlsname and port are optional.
    */
```
Example:
```
java -jar as-rest-client-*.jar --aerospike.restclient.hostname=172.17.0.3 --server.port=9876
```
### Authentication

The REST Client also allows authentication to an Aerospike Enterprise edition server with security enabled. The following environment variables are used to find authentication information.

* `aerospike.restclient.clientpolicy.user` This is the name of a user registered with the Aerospike database. This variable is only needed when the Aerospike cluster is running with security enabled.
* `aerospike.restclient.clientpolicy.password` This is the password for the previously specified user. This variable is only needed when the Aerospike cluster is running with security enabled.

Multi-user authentication configuration variables:
* `aerospike.restclient.requireAuthentication` Set this boolean flag to true to require the Basic Authentication on each request.
* `aerospike.restclient.pool.size` Represents the max size of the clients LRU cache (default value: 16)

### TLS Configuration

Beginning with version `1.1.0` the Aerospike REST Client supports TLS communication between the client and the Aerospike Server. (This feature requires an Enterprise Edition Aerospike Server). If utilizing TLS, the `aerospike.restclient.hostlist` variable should be set to include appropriate TLS Names for each of the Aerospike Nodes. For example: `localhost:cluster-tls-name:4333` The following environment variables allow configuration of this connection:

* `aerospike.restclient.ssl.enabled` boolean, set to `true` to enable a TLS connection with the Aerospike Server. If no other SSL environment variables are provided, the REST client will attempt to establish a secure connection utilizing the default Java SSL trust and keystore settings. Default: `false`
* `aerospike.restclient.ssl.keystorepath` The path to a Java KeyStore to be used to interact with the Aerospike Server. If omitted the default Java KeyStore location and password will be used.
* `aerospike.restclient.ssl.keystorepassword` The password to the keystore. If a keystore path is specified, this must be specified as well.
* `aerospike.restclient.ssl.keypassword` The password for the key to be used when communicating with Aerospike. If omitted, and `aerospike.restclient.ssl.keystorepassword` is provided,  the value of `aerospike.restclient.ssl.keystorepassword` will be used as the key password.
* `aerospike.restclient.ssl.truststorepath` The path to a Java TrustStore to be used to interact with the Aerospike Server. If omitted the default Java TrustStore location and password will be used.
* `aerospike.restclient.ssl.truststorepassword` The password for the truststore. May be omitted if the TrustStore is not password protected.
* `aerospike.restclient.ssl.forloginonly` Boolean indicating that SSL should only be used for the initial login connection to Aerospike. Default: `false`
* `aerospike.restclient.ssl.allowedciphers` An optional comma separated list of ciphers that are permitted to be used in communication with Aerospike. Available cipher names can be obtained by `SSLSocket.getSupportedCipherSuites()`.
* `aerospike.restclient.ssl.allowedprotocols` An optional comma separated list of protocols that are permitted to be used in communication with Aerospike. Available values can be aquired using `SSLSocket.getSupportedProtocols()`. By Default only `TLSv1.2` is allowed.

## Further Reading

* For information about the data formats available to use with the REST Client see [Data Formats](./data-formats.md).
