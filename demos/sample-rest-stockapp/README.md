# Aerospike Rest Client Sample Stock Webapp

## Overview

This application utilizes the Aerospike Rest Client to provide an example stock portfolio application. It provides users the opportunity to create portfolios and view historical performance data for the stocks contained in those portfolios.
By default it will run on `localhost:3333`. When visiting that URL for the first time, you should be redirected to `localhost:3333/upload` and prompted to upload some data. The source of that data is mentioned on that page and further on in this document.
After that, you can always return to the main page by clicking the trending icon in the top left corner of the page.

## Setup

This application relies on having data in a specific format preloaded into the Aerospike database. For this demonstration we used the data found in the ``all_stocks_5yrs.csv`` hosted by [Kaggle](https://www.kaggle.com/camnugent/sandp500#all_stocks_5yr.csv).

## Running the app in development mode

To start the application in development mode: run `yarn startdev` . This will load the development server and serve content at `localhost:3333` . The `.env` file may be edited to control various aspects of the demo application. Make sure to remove the `homepage` from `package.json` before running this command.

## Building the app for local usage

To build the App from this directory run: `yarn builddemo` this will package the app into a directory named `build/` .
To serve the app from this directory you may run: `node servelocal.js` .This will start the app up at localhost:3333 . Note that the `servelocal.js` script requires `expressjs` to be installed.

By default the app assumes that it is running at `http://localhost:8080/stocks`. To change this to `http://localhost:8080` for testing purposes, remove the entry for `"homepage"` from `package.json`
By default the app looks for the Aerospike Rest Client to be reachable at `http://localhost:8080`  To build the app to look for the Client in a different location. Edit the line `"builddemo": "REACT_APP_API_BASE=http://localhost:8080 react-scripts-ts build",` in `package.json` to set the environment variable to the URI where the Aerospike Rest Client will be located.

## Building the demo for Tomcat

To build and package the demo for use with Tomcat, there are a few considerations: Where the app will be running relative to the server root. By default this is `http://localhost:8080/stocks` another consideration is where the Rest Client will be available, by default we assume this is `http://localhost:8080/as-rest-client` . To change either of these values,
edit the entry for the `buildtomcat` script in `package.json`. To build the app for this purpose, run `yarn buildtomcat`.

The contents of the resulting `build/` directory should then be copied into a folder named `stocks` in the `webapps` folder of your Tomcat installation. e.g. `cp -r ./build /path/to/tomcat/webapps/stocks`