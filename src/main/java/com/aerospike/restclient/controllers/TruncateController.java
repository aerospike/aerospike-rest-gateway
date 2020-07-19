/*
 * Copyright 2020 Aerospike, Inc.
 *
 * Portions may be licensed to Aerospike, Inc. under one or more contributor
 * license agreements WHICH ARE COMPATIBLE WITH THE APACHE LICENSE, VERSION 2.0.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.aerospike.restclient.controllers;

import com.aerospike.restclient.domain.RestClientError;
import com.aerospike.restclient.domain.auth.AuthDetails;
import com.aerospike.restclient.service.AerospikeTruncateService;
import com.aerospike.restclient.util.HeaderHandler;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Api(tags = "Truncate Operations", description = "Remove multiple records from the server.")
@RestController
@RequestMapping("/v1/truncate")
public class TruncateController {

    public static final String DATE_QUERY_PARAM_NOTES = "All records last updated before this date/time will be truncated. If not specified, all records will be truncated.\n "
            + "This is a string representation of a date time utilizing the ISO-8601 extended offset date-time format. example: 2019-12-03T10:15:30+01:00";
    public static final String DATE_QUERY_PARAM_EXAMPLE = "2019-12-03T10:15:30+01:00";

    @Autowired
    private AerospikeTruncateService truncateService;

    @RequestMapping(method = RequestMethod.DELETE, value = "/{namespace}", produces = {"application/json", "application/msgpack"})
    @ApiOperation(value = "Truncate records in a specified namespace.", nickname = "truncateNamespace")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    @ApiResponses(value = {
            @ApiResponse(code = 403, response = RestClientError.class, message = "Not authorized to access the resource",
                    examples = @Example(value = {@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
            @ApiResponse(code = 400, response = RestClientError.class, message = "Invalid parameters or request",
                    examples = @Example(value = {@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' ")}))
    })
    public void truncateNamespace(
            @ApiParam(value = "The namespace whose records will be truncated.", required = true) @PathVariable(value = "namespace") String namespace,
            @ApiParam(value = DATE_QUERY_PARAM_NOTES, required = false, example = DATE_QUERY_PARAM_EXAMPLE) @RequestParam(value = "date", required = false) String dateString,
            @RequestHeader(value = "Authorization", required = false) String basicAuth) {

        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);

        truncateService.truncate(authDetails, namespace, null, dateString);
    }


    @RequestMapping(method = RequestMethod.DELETE, value = "/{namespace}/{set}", produces = {"application/json", "application/msgpack"})
    @ApiOperation(value = "Truncate records in a specified namespace and set.", nickname = "truncateSet")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    @ApiResponses(value = {
            @ApiResponse(code = 403, response = RestClientError.class, message = "Not authorized to access the resource",
                    examples = @Example(value = {@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' ")})),
            @ApiResponse(code = 400, response = RestClientError.class, message = "Invalid parameters or request",
                    examples = @Example(value = {@ExampleProperty(mediaType = "Example json", value = "{'inDoubt': false, 'message': 'A message' ")}))
    })
    public void truncateSet(
            @ApiParam(value = "The namespace whose records will be truncated", required = true) @PathVariable(value = "namespace") String namespace,
            @ApiParam(value = "The set, in the specified namespace, whose records will be truncated", required = true) @PathVariable(value = "set") String set,
            @ApiParam(value = DATE_QUERY_PARAM_NOTES, required = false, example = DATE_QUERY_PARAM_EXAMPLE) @RequestParam(value = "date", required = false) String dateString,
            @RequestHeader(value = "Authorization", required = false) String basicAuth) {

        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);

        truncateService.truncate(authDetails, namespace, set, dateString);
    }

}
