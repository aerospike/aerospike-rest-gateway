# Aerospike REST Client Demo Application

## Overview

This demo application is a React app using react-redux components generated from the REST Client's swagger specification.

The application models an example site which allows users to create personalized portfolios, and monitor the portfolios' performance. The application also displays information about the API requests to the REST Client utilized in the application.

**Note** The provided API call details only include information about request and response data; to see more information about headers and timing we suggest utilizing your browser's developer tools.

## Model

The input data for this application is a series of daily entries about stock symbols. Those entries include the
date, stock symbol, opening, and closing price. To see the actual input schema: see the included `all_stocks_5yr.csv`.

For each daily entry we create a record structured as follows:

    symbol: string # The stock symbol
    date: string # The date of the entry
    open: float # The opening price for the day
    close: float # The closing price for the day

The key of this record is formed by concatenating the stock symbol
with the string representation of the date. For example for the `GOOGL` entry for `2013-02-08` the resulting key is `GOOGL-2013-02-08`.

In addition to these daily entries there is a record for each individual stock. This record is structured as followed

    name: string # The symbol for this stock
    dates: [string, ...] # A list of string representations of the daily entries existing for this symbol

There is also a record including a list of stock symbols:

    symbols: [string, ...] # A list of all stock symbols.

The Application also includes the concept of a portfolio, which is a collection of stocks. Each portfolio has a record structured as:

    id: string # Unique identifier for the portfolio
    name: string # Name of the portfolio, in this app it is always the same as id
    stocks: list[string, ...] # A list of stock symbols contained in this portfolio

There is another record containing the most recently created portfolios:

    portfolios: [string, ...] A list of recently created portfolios

## Usage

**Note** These instructions assume that the demo is running on a Tomcat server with a base url of `http://localhost:8080`. If this is inaccurate, the provided URLs will need to be updated.

### Prerequisites

* Ensure that your Aerospike database has a namespace named test
* Follow the steps for installing and starting the [REST Client](./installation-and-config.md)
* Make sure that the rest client is available at `http://localhost:8080/as-rest-client`.

### Installation

* Place the `stocks` folder which came with this package into your Tomcat Installation's `webapps` folder.
* Restart Tomcat

### Using the demo

To try out the demo application go to <http://localhost:8080/stocks>

You should be prompted to upload some stock data. Follow the directions on the uppload page to acquire this data file. The application will transform this data into a series of records which will be stored into Aerospike. See [Data Model](#Model) section for information on the actual structuring of this data.

In the demo application, you may always return to the homepage by clicking on the trending upward icon in the top left corner of the page.
