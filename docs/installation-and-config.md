# Installation and Configuration

## Installation

### Requirements

* The Rest Gateway requires Java 8.
* The Rest Gateway requires an Aerospike Server to be installed and reachable. See [Configuration](#configuration) for
  details on specifying the location of this server.

### Build and development

* Build

```
make build
```

* Run Locally during development:

```sh
make run
```

### Run using a Jar file

```
java -jar build/libs/aerospike-rest-gateway-<VERSION>.jar
```

The fully executable jar contains an extra script at the front of the file, which allows you to just symlink your Spring
Boot jar to init.d or use a systemd script.  
More information at the following links:

* [Installation as an init.d service](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#deployment-service)
* [Installation as a systemd service](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#deployment-systemd-service)

#### Complete example of running the REST gateway using a jar

```
java -server -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=8082 -Dcom.sun.management.jmxremote.rmi.port=8082 -Dcom.sun.management.jmxremote.local.only=false -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -XX:+UseG1GC -Xms2048m -Xmx2048m -jar ./aerospike-rest-gateway-X.X.X.jar --aerospike.restclient.hostname=localhost --aerospike.restclient.clientpolicy.user=*** --aerospike.restclient.clientpolicy.password=*** --logging.file.name=/var/log/restclient/asproxy.log
```

For more JVM command-line options, see
the [documentation](https://docs.oracle.com/javase/8/docs/technotes/tools/unix/java.html).

### Run using Docker

* Build the docker image

```
docker build -t aerospike-rest-gateway .
```

* Run the REST Gateway using docker

```sh
docker run -itd --rm -p 8080:8080 --name as-rest -e aerospike_restclient_hostname=172.17.0.3 aerospike-rest-gateway
```

### Run on Kubernetes

* Use the official [Helm chart](https://github.com/aerospike/aerospike-client-rest-kubernetes) to deploy the Aerospike
  REST Gateway to Kubernetes.

### Verifying installation

**Note:** The following steps assume REST Gateway's base path is `http://localhost:8080/` if this is not the case, the
provided URLs will need to be modified accordingly.

To test that the REST Gateway is up and running, and connected to the Aerospike database you can run:

    curl http://localhost:8080/v1/cluster

This will return basic information about the cluster.

Interactive API documentation may be found at <http://localhost:8080/swagger-ui.html> . This will allow you to
test out various commands in your browser.

The Swagger specification, in `JSON` format, can be found at <http://localhost:8080/v3/api-docs> .

## Configuration

Read more about Spring
Boot [Externalized Configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-external-config)
.

* `server.port` Change the port the REST Gateway is listening on (default: 8080)
* `aerospike.restclient.hostname` The IP address or Hostname of a seed node in the cluster (default: `localhost`)
  **Note:** If TLS is being utilized, `aerospike.restclient.hostlist` should be used instead of this variable.
* `aerospike.restclient.port` The port to communicate with the Aerospike cluster over. (default: `3000`)
* `aerospike.restclient.hostlist` A comma separated list of cluster hostnames, (optional TLS names) and ports. If this
  is specified, it overrides the previous two environment variables. The format is described below:

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
java -jar as-rest-gateway-*.jar --aerospike.restclient.hostname=172.17.0.3 --server.port=9876
```

### Authentication

The REST Gateway also allows authentication to an Aerospike Enterprise edition server with security enabled. The
following environment variables are used to find authentication information.

* `aerospike.restclient.clientpolicy.user` This is the name of a user registered with the Aerospike database. This
  variable is only needed when the Aerospike cluster is running with security enabled.
* `aerospike.restclient.clientpolicy.password` This is the password for the previously specified user. This variable is
  only needed when the Aerospike cluster is running with security enabled.
* `aerospike.restclient.clientpolicy.authMode` This is the authentication mode. Use it when user/password is defined.
  Supported modes are INTERNAL, EXTERNAL and EXTERNAL_INSECURE. Default is INTERNAL.
* `aerospike.restclient.clientpolicy.clusterName` This is the expected cluster name. Default is empty and does not
  validate target cluster name.

To utilize the multi-tenancy capability within the REST Gateway, send Aerospike login credentials using
the [Basic access authentication](https://en.wikipedia.org/wiki/Basic_access_authentication).  
Set custom multi-user authentication configuration variables if needed:

* `aerospike.restclient.requireAuthentication` Set this boolean flag to true to require the Basic Authentication on each
  request.
* `aerospike.restclient.pool.size` Represents the max size of the authenticated clients LRU cache (default value: 16).
  Please note that an oversized client cache will consume a lot of resources and affect the performance.

### TLS Configuration

Beginning with version `1.1.0` the Aerospike REST Gateway supports TLS communication between the client and the
Aerospike
Server. (This feature requires an Enterprise Edition Aerospike Server).
If utilizing TLS, the `aerospike.restclient.hostlist` variable should be set to include appropriate TLS Names for each
of the Aerospike Nodes. For example: `localhost:cluster-tls-name:4333` The following environment variables allow
configuration of this connection:

* `aerospike.restclient.ssl.enabled` boolean, set to `true` to enable a TLS connection with the Aerospike Server. If no
  other SSL environment variables are provided, the REST gateway will attempt to establish a secure connection utilizing
  the default Java SSL trust and keystore settings. Default: `false`
* `aerospike.restclient.ssl.keystorepath` The path to a Java KeyStore to be used to interact with the Aerospike Server.
  If omitted the default Java KeyStore location and password will be used.
* `aerospike.restclient.ssl.keystorepassword` The password to the keystore. If a keystore path is specified, this must
  be specified as well.
* `aerospike.restclient.ssl.keypassword` The password for the key to be used when communicating with Aerospike. If
  omitted, and `aerospike.restclient.ssl.keystorepassword` is provided, the value
  of `aerospike.restclient.ssl.keystorepassword` will be used as the key password.
* `aerospike.restclient.ssl.truststorepath` The path to a Java TrustStore to be used to interact with the Aerospike
  Server. If omitted the default Java TrustStore location and password will be used.
* `aerospike.restclient.ssl.truststorepassword` The password for the truststore. May be omitted if the TrustStore is not
  password protected.
* `aerospike.restclient.ssl.forloginonly` Boolean indicating that SSL should only be used for the initial login
  connection to Aerospike. Default: `false`
* `aerospike.restclient.ssl.allowedciphers` An optional comma separated list of ciphers that are permitted to be used in
  communication with Aerospike. Available cipher names can be obtained by `SSLSocket.getSupportedCipherSuites()`.
* `aerospike.restclient.ssl.allowedprotocols` An optional comma separated list of protocols that are permitted to be
  used in communication with Aerospike. Available values can be aquired using `SSLSocket.getSupportedProtocols()`. By
  Default only `TLSv1.2` is allowed.

### Aerospike Client Policy Configuration

The REST Gateway allows modification of the internal Aerospike client policies using the following environment
variables.
Please refer to
the [ClientPolicy](https://docs.aerospike.com/apidocs/java/com/aerospike/client/policy/ClientPolicy.html) page on the
Aerospike Java client API for more details about each policy settings.

* `aerospike.restclient.clientpolicy.connPoolsPerNode`
* `aerospike.restclient.clientpolicy.minConnsPerNode`
* `aerospike.restclient.clientpolicy.maxConnsPerNode`
* `aerospike.restclient.clientpolicy.maxSocketIdle`
* `aerospike.restclient.clientpolicy.tendInterval`
* `aerospike.restclient.clientpolicy.timeout`
* `aerospike.restclient.clientpolicy.failIfNotConnected`
* `aerospike.restclient.clientpolicy.sharedThreadPool`
* `aerospike.restclient.clientpolicy.useServicesAlternate`
* `aerospike.restclient.clientpolicy.requestProleReplicas`

## Further Reading

* For information about the data formats available to use with the REST Gateway see [Data Formats](./data-formats.md).
