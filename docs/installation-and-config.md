# Installation and Configuration

## Installation

### Requirements

* The Rest Client requires Java 8.
* The Rest Client requires an Aerospike Server to be installed and reachable. See [Configuration](#configuration) for details on specifying the location of this server.

### Run from Executable Jar

* Build
```
./gradlew build
```
* Execute
```
java -jar build/libs/aerospike-client-rest-<VERSION>.jar
```
The fully executable jar contains an extra script at the front of the file, which allows you to just symlink your Spring Boot jar to init.d or use a systemd script.  
More information at the following links:
* [Installation as an init.d service](https://docs.spring.io/spring-boot/docs/2.2.6.RELEASE/reference/htmlsingle/#deployment-service)
* [Installation as a systemd service](https://docs.spring.io/spring-boot/docs/2.2.6.RELEASE/reference/htmlsingle/#deployment-systemd-service)

### Run with Docker
* Build the docker image
```
docker build -t aerospike-client-rest .
```

### Verifying installation

**Note:** The following steps assume REST Client's base path is `http://localhost:8080/as-rest-client` if this is not the case, the provided URLs will need to be modified accordingly.

To test that the rest client is up and running, and connected to the Aerospike database you can run:

    curl http://localhost:8080/as-rest-client/v1/cluster

This will return basic information about the cluster.

Interactive API documentation may be found at <http://localhost:8080/as-rest-client/swagger-ui.html> . This will allow you to
test out various commands in your browser.

The Swagger specification, in `JSON` format, can be found at <http://localhost:8080/as-rest-client/v2/api-docs> .

## Configuration

By default the REST Client looks for an Aerospike Server available at `localhost:3000` . The following environment variables allow specification of a different host/port.

* `aerospike_restclient_hostname` This is the IP address or Hostname of a single node in the cluster. It defaults to `localhost`. If TLS is being utilized, `aerospike_restclient_hostlist` should be used instead of this variable.
* `aerospike_restclient_port` The port to communicate with the Aerospike cluster over. Defaults to `3000`
* `aerospike_restclient_hostlist` A comma separated list of cluster hostnames, (optional TLS names) and ports. If this is specified, it overrides the previous two environment variables. The format is described below:

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

The REST Client also allows authentication to an Aerospike Enterprise edition server with security enabled. The following environment variables are used to find authentication information. **The Aerospike REST Client supports a single user only.**
**If more than user is necessary, contact support@aerospike.com**

* `aerospike_restclient_clientpolicy_user` This is the name of a user registered with the Aerospike database. This variable is only needed when the Aerospike cluster is running with security enabled.
* `aerospike_restclient_clientpolicy_password` This is the password for the previously specified user. This variable is only needed when the Aerospike cluster is running with security enabled.

### TLS Configuration

Beginning with version `1.1.0` the Aerospike REST Client supports TLS communication between the client and the Aerospike Server. (This feature requires an Enterprise Edition Aerospike Server). If utilizing TLS, the `aerospike_restclient_hostlist` variable should be set to include appropriate TLS Names for each of the Aerospike Nodes. For example: `localhost:cluster-tls-name:4333` The following environment variables allow configuration of this connection:

* `aerospike_restclient_ssl_enabled` boolean, set to to `true` to enable a TLS connection with the Aerospike Server. If no other SSL environment variables are provided, the REST client will attempt to establish a secure connection utilizing the default Java SSL trust and keystore settings. Default: `false`
* `aerospike_restclient_ssl_keystorepath` The path to a Java KeyStore to be used to interact with the Aerospike Server. If omitted the default Java KeyStore location and password will be used.
* `aerospike_restclient_ssl_keystorepassword` The password to the keystore. If a keystore path is specified, this must be specified as well.
* `aerospike_restclient_ssl_keypassword` The password for the key to be used when communicating with Aerospike. If omitted, and `aerospike_restclient_ssl_keystorepassword` is provided,  the value of `aerospike_restclient_ssl_keystorepassword` will be used as the key password.
* `aerospike_restclient_ssl_truststorepath` The path to a Java TrustStore to be used to interact with the Aerospike Server. If omitted the default Java TrustStore location and password will be used.
* `aerospike_restclient_ssl_truststorepassword` The password for the truststore. May be omitted if the TrustStore is not password protected.
* `aerospike_restclient_ssl_forloginonly` Boolean indicating that SSL should only be used for the initial login connection to Aerospike. Default: `false`
* `aerospike_restclient_ssl_allowedciphers` An optional comma separated list of ciphers that are permitted to be used in communication with Aerospike. Available cipher names can be obtained by `SSLSocket.getSupportedCipherSuites()`.
* `aerospike_restclient_ssl_allowedprotocols` An optional comma separated list of protocols that are permitted to be used in communication with Aerospike. Available values can be aquired using `SSLSocket.getSupportedProtocols()`. By Default only `TLSv1.2` is allowed.

## Further Reading

* For information about the data formats available to use with the REST Client see [Data Formats](./data-formats.md)