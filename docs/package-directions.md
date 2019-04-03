# Aerospike Rest Client demo package

This package contains the following files

* `swagger.json` The swagger specification for the Rest API.
* `api-doc.html` Generated HTML documentation for the API.
* `as-rest-client##<VERSION>.war` A `.war` file to be deployed in Tomcat, or another server accepting `.war` files.
* `stocks/` A directory containing our demo application.

## Installing and starting the Rest Client

Follow the [installation and configuration directions](./installation-and-config.md) to install and start the rest client.

## First steps

**Note** The following directions assume that the Rest Client is listening on `http://localhost:8080/as-rest-client`, if this is not the case, change the URLS accordingly.

To get a quick introduction to the API and usage of the Rest Client we recommend visiting the Interactive Documentation which will be located at <http://localhost:8080/as-rest-client/swagger-ui.html> . This Interface shows all of the API endpoints, and allows you to try them out from a browser.

The [Demo App Directions](./demo-app.md) provide information on how to start up, and interact with a simple application built around the Rest Client.

## Further information

For directions on the interchange formats supported by the Rest Client, see [Data Formats](./data-formats.md) .
