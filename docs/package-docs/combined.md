# Aerospike REST Client package

This package contains the following files

* `swagger.json` The swagger specification for the REST API.
* `api-doc.html` Generated HTML documentation for the API.
* `as-rest-client##<VERSION>.war` A `.war` file to be deployed in Tomcat, or another server accepting `.war` files. Directions for installation are provided further down in this document.

## Installation and Configuration

### Requirements

* The REST Client requires Java 8.
* The REST Client requires an Aerospike Server to be installed and reachable. See [Configuration](#configuration) for details on specifying the location of this server.

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

### Configuration

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

The REST Client also allows authentication to an Aerospike Enterprise edition server with security enabled. The following environment variables are used to find authentication information. **The Aerospike REST Client supports a single user only.** **If more than user is necessary, contact support@aerospike.com**

* `aerospike_restclient_clientpolicy_user` This is the name of a user registered with the Aerospike database. This variable is only needed when the Aerospike cluster is running with security enabled.
* `aerospike_restclient_clientpolicy_password` This is the password for the previously specified user. This variable is only needed when the Aerospike cluster is running with security enabled.

## REST Client Data Formats

API Requests which involve sending data can use the `JSON`, or `MessagePack` formats. By default JSON will be assumed. To use `MessagePack`, set the `Content-Type` header to ``"application/msgpack"``. Similarly
Responses may be sent in `JSON` or `MessagePack`, `JSON` is the default. To receive `MessagePack` formatted data set the `Accept` header to ``"application/msgpack"``.

### JSON Use Cases

For many uses `JSON` is a simpler and completely valid option. It provides simplicity of use, and familiarity. If basic Key Value operations are being used, and neither Maps with non string keys, Bytes nor GeoJSON are required, then `JSON` will work completely with the Aerospike data model.

### Message Pack Use Cases

Message pack is provided as an option because JSON cannot fully represent certain Aerospike data types. Specifically:

* Aerospike can store arrays of bytes.
* Aerospike maps may have keys which are not strings. e.g ``{1:2, 3.14: 159}``.
* Aerospike stores a GeoJSON type. Which is returned as a `MessagePack` extension type.

If you are not handling Maps with non string keys, and not using bytes nor GeoJSON, then JSON as an interchange format will work for the Rest Client.

### MessagePack Format

The `MessagePack` sent and received by the REST client is almost completely standard. The one specific detail is that we represent a `GeoJSON` object using the [MessagePack Extension format type](https://github.com/msgpack/msgpack/blob/master/spec.md#extension-types).
The extension type value is `23` and the payload is the string representation of the `GeoJSON`. This is done to differentiate a normal string from GeoJSON.
For example to write a bin map usable by the API with a GeoJSON value utilizing Python.

```python
# Python 2.7
import msgpack
packed_geojson = msgpack.ExtType(23, "{\"coordinates\": [-122.0, 37.5], \"type\": \"Point\"}")
packed_bins = {u'geo_bin': packed_geojson}
mp_bins = msgpack.packb(packed_bins)
```

Or with Java

```java
MessageBufferPacker packer = new MessagePack.PackerConfig().newBufferPacker();
String geoString = "{\"coordinates\": [-122.0, 37.5], \"type\": \"Point\"}";
packer.packMapHeader(1);
packer.packString("geo_bin");
packer.packExtensionTypeHeader((byte) 23, geoString.length());
packer.addPayload(geoString.getBytes("UTF-8"));
```

Bytes are a standard Message Pack type. Here is an example of creating a Bin Map to be used with the API

```python
# Python 2.7
test_bytes = bytearray([1,2,3])
mp_bytes_bins = msgpack.packb({u'my_bytes': test_bytes}, use_bin_type=True)
```

```java
byte[] testBytes = {1, 2, 3};
MessageBufferPacker packer = new MessagePack.PackerConfig().newBufferPacker();

packer.packMapHeader(1);

packer.packString("my_bytes");

packer.packBinaryHeader(3);
packer.writePayload(testBytes);

byte[] payload = packer.toByteArray();
```
