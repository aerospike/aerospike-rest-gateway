# Installation and Configuration

## Installation

### Requirements

* The Rest Client requires Java 8.
* The Rest Client requires an Aerospike Server to be installed and reachable. See [Configuration](#configuration) for details on specifying the location of this server.

### Running on Tomcat

* If not already installed, download and install [Tomcat](https://tomcat.apache.org) . We recommend the Core distribution of Tomcat 9, found under the Binary Distributions section.

This will create a root installation folder which looks something like

    ./bin/
    ./conf/
    ./logs/
    ./webapps/

* Place the REST Client `.war` file in your tomcat installation's `webapps` folder.
* For more detailed server configurations, refer to the Documentation for the version of Tomcat which you are using. For Tomcat 9 these are located at: <https://tomcat.apache.org/tomcat-9.0-doc/introduction.html>
* Start tomcat. One way to do this is by running `bin/catalina.sh run` or `bin/catalina.sh start` from the root folder of your Tomcat installation.

### Verifying installation

**Note:** The following steps assume Tomcat's root is at `http://localhost:8080`  and the REST Client's base path is `http://localhost:8080/as-rest-client` if this is not the case, the provided URLs will need to be modified accordingly.

To test that the rest client is up and running, and connected to the Aerospike database you can run:

    curl http://localhost:8080/as-rest-client/v1/cluster

This will return basic information about the cluster.

Interactive API documentation may be found at <http://localhost:8080/as-rest-client/swagger-ui.html> . This will allow you to
test out various commands in your browser.

The Swagger specification, in `JSON` format, can be found at <http://localhost:8080/as-rest-client/v2/api-docs> .

## Configuration

By default the REST Client looks for an Aerospike Server available at `localhost:3000` . The following environment variables allow specification of a different host/port.

* `aerospike_restclient_hostname` This is the IP address or Hostname of a single node in the cluster. It defaults to `localhost`
* `aerospike_restclient_port` The port to communicate with the Aerospike cluster over. Defaults to `3000`
* `aerospike_restclient_hostlist` A comma separated list of cluster hostnames and ports. If this is specified, it overrides the previous two environment variables. The format is described below:

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

## Further Reading

* For information about the data formats available to use with the REST Client see [Data Formats](./data-formats.md)