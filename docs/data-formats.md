# REST Gateway Data Formats

API Requests which involve sending data can use the `JSON`, or `MessagePack` formats. By default JSON will be assumed.
To use `MessagePack`, set the `Content-Type` header to ``"application/msgpack"``. Similarly
Responses may be sent in `JSON` or `MessagePack`, `JSON` is the default. To receive `MessagePack` formatted data set
the `Accept` header to ``"application/msgpack"``.

## JSON Use Cases

For many uses `JSON` is a simpler and completely valid option. It provides simplicity of use, and familiarity. If basic
Key Value operations are being used, and neither Maps with non string keys, Bytes nor GeoJSON are required, then `JSON`
will work completely with the Aerospike data model.

**Note**: GeoJSON and ByteArrays can not be nested in CDTs.

In 2.0.4, a GeoJSON map with the keys "type" and "coordinates" will automatically be understood as a GeoJSON type.

```javascript
{
    "type": "Point",
    "coordinates": [1.123, 4.156]
}
```

In version prior to 2.0.4, a GeoJSON object can be provided by sending a base64 encoded GeoJSON string.
Base64 encoding the following string `{"type": "Point", "coordinates": [1.123, 4.156]}` results
in `eyJ0eXBlIjogIlBvaW50IiwgImNvb3JkaW5hdGVzIjogWzEuMTIzLCA0LjE1Nl19Cg==`.
To write the GeoJSON object use

```javascript
{
    "type": "GEO_JSON",
    "value": "eyJ0eXBlIjogIlBvaW50IiwgImNvb3JkaW5hdGVzIjogWzEuMTIzLCA0LjE1Nl19Cg=="
}
```

Similarly, starting in 1.0.0 a ByteArray can be provided using a map with
the keys "type"
and "value" where the type is "byteArray" and the value is a base64 encoded string.

```javascript
{
    "type": "BYTE_ARRAY",
    "value": "YWVyb3NwaWtlCg=="
}
```

## Message Pack Use Cases

Message pack is provided as an option because JSON cannot fully represent certain Aerospike data types. Specifically:

* Aerospike can store arrays of bytes.
* Aerospike maps may have keys which are not strings. e.g ``{1:2, 3.14: 159}``.
* Aerospike stores a GeoJSON type. Which is returned as a `MessagePack` extension type.

If you are not handling Maps with non string keys, and not using bytes nor GeoJSON, then JSON as an interchange format
will work for the Rest Gateway.

## MessagePack Format

The `MessagePack` sent and received by the REST Gateway is almost completely standard. The one specific detail is that
we
represent a `GeoJSON` object using
the [MessagePack Extension format type](https://github.com/msgpack/msgpack/blob/master/spec.md#extension-types).
The extension type value is `23` and the payload is the string representation of the `GeoJSON`. This is done to
differentiate a normal string from GeoJSON.
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
    MessageBufferPacker packer=new MessagePack.PackerConfig().newBufferPacker();
    String geoString="{\"coordinates\": [-122.0, 37.5], \"type\": \"Point\"}";
    packer.packMapHeader(1);
    packer.packString("geo_bin");
    packer.packExtensionTypeHeader((byte)23,geoString.length());
    packer.addPayload(geoString.getBytes("UTF-8"));
```

Bytes are a standard Message Pack type. Here is an example of creating a Bin Map to be used with the API

```python
    # Python 2.7
    test_bytes = bytearray([1,2,3])
    mp_bytes_bins = msgpack.packb({u'my_bytes': test_bytes}, use_bin_type=True)
```

```java
    byte[]testBytes={1,2,3};
    MessageBufferPacker packer=new MessagePack.PackerConfig().newBufferPacker();
    packer.packMapHeader(1);
    packer.packString("my_bytes");
    packer.packBinaryHeader(3);
    packer.writePayload(testBytes);
    byte[]payload=packer.toByteArray();
```
