# Aerospike Rest Gateway package

This package contains the following files

* `openapi.json` The swagger specification for the Rest API.
* `aerospike-rest-gateway##<VERSION>.jar` A `.jar` file to be deployed.
* `aerospike-rest-gateway##<VERSION>-plain.jar` A plain `.jar` file.

## Installing and starting the Rest Gateway

Follow the [installation and configuration directions](./installation-and-config.md) to install and start the rest
client.

## First steps

**Note** The following directions assume that the Rest Gateway is listening
on `http://localhost:8080/`,
if this is not the case, change the URLS accordingly.

To get a quick introduction to the API and usage of the Rest Gateway we recommend visiting the Interactive Documentation
which will be located at <http://localhost:8080/swagger-ui/index.html> . This Interface shows all the
API
endpoints, and allows you to try them out from a browser.

## Further information

For directions on the interchange formats supported by the Rest Gateway, see [Data Formats](./data-formats.md) .
