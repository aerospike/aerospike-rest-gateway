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

import com.aerospike.client.admin.User;
import com.aerospike.restclient.domain.RestClientError;
import com.aerospike.restclient.domain.RestClientPrivilege;
import com.aerospike.restclient.domain.RestClientRole;
import com.aerospike.restclient.domain.RestClientRoleQuota;
import com.aerospike.restclient.domain.RestClientUserModel;
import com.aerospike.restclient.domain.auth.AuthDetails;
import com.aerospike.restclient.service.AerospikeAdminService;
import com.aerospike.restclient.util.ResponseExamples;
import com.aerospike.restclient.util.HeaderHandler;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "Admin Operations", description = "Manage users and privileges.")
@RestController
@RequestMapping("/v1/admin")
public class AdminController {

    @Autowired
    private AerospikeAdminService adminService;

    @Operation(summary = "Return a list of information about users.", operationId = "getUsers")
    @ApiResponse(
            responseCode = "403",
            description = "Not authorized to read user information.",
            content = @Content(
                    schema = @Schema(implementation = RestClientError.class),
                    examples = @ExampleObject(name = ResponseExamples.DEFAULT_NAME, value = ResponseExamples.DEFAULT_VALUE)))
    @RequestMapping(method = RequestMethod.GET, value = "/user", produces = {"application/json", "application/msgpack"})
    public User[] getUsers(@RequestHeader(value = "Authorization", required = false) String basicAuth) {

        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);
        return adminService.getUsers(authDetails);
    }

    @Operation(summary = "Return information about a specific user.", operationId = "getUser")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "403",
                    description = "Not authorized to read user information.",
                    content = @Content(
                            schema = @Schema(implementation = RestClientError.class),
                            examples = @ExampleObject(name = ResponseExamples.DEFAULT_NAME, value = ResponseExamples.DEFAULT_VALUE))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Specified user not found.",
                    content = @Content(
                            schema = @Schema(implementation = RestClientError.class),
                            examples = @ExampleObject(name = ResponseExamples.DEFAULT_NAME, value = ResponseExamples.DEFAULT_VALUE)))
    })
    @RequestMapping(method = RequestMethod.GET, value = "/user/{user}", produces = {"application/json", "application/msgpack"})
    public User getUser(@Parameter(required = true) @PathVariable(value = "user") String user,
                        @RequestHeader(value = "Authorization", required = false) String basicAuth) {

        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);
        return adminService.getUser(authDetails, user);
    }

    @Operation(summary = "Remove a user.", operationId = "dropUser")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "403",
                    description = "Not authorized to delete users.",
                    content = @Content(
                            schema = @Schema(implementation = RestClientError.class),
                            examples = @ExampleObject(name = ResponseExamples.DEFAULT_NAME, value = ResponseExamples.DEFAULT_VALUE))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Specified user not found.",
                    content = @Content(
                            schema = @Schema(implementation = RestClientError.class),
                            examples = @ExampleObject(name = ResponseExamples.DEFAULT_NAME, value = ResponseExamples.DEFAULT_VALUE)))
    })
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    @RequestMapping(method = RequestMethod.DELETE, value = "/user/{user}", produces = {"application/json", "application/msgpack"})
    public void dropUser(@Parameter(required = true) @PathVariable(value = "user") String user,
                         @RequestHeader(value = "Authorization", required = false) String basicAuth) {

        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);
        adminService.dropUser(authDetails, user);
    }

    @Operation(summary = "Change the password of the specified user.", operationId = "changePassword")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "403",
                    description = "Not authorized to modify users.",
                    content = @Content(
                            schema = @Schema(implementation = RestClientError.class),
                            examples = @ExampleObject(name = ResponseExamples.DEFAULT_NAME, value = ResponseExamples.DEFAULT_VALUE))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Specified user not found.",
                    content = @Content(
                            schema = @Schema(implementation = RestClientError.class),
                            examples = @ExampleObject(name = ResponseExamples.DEFAULT_NAME, value = ResponseExamples.DEFAULT_VALUE)))
    })
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    @RequestMapping(method = RequestMethod.PATCH, value = "/user/{user}", consumes = {"application/json", "application/msgpack"}, produces = {"application/json", "application/msgpack"})
    public void changePassword(@Parameter(required = true) @PathVariable(value = "user") String user,
                               @Parameter(required = true) @RequestBody String password,
                               @RequestHeader(value = "Authorization", required = false) String basicAuth) {

        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);
        adminService.changePassword(authDetails, user, password);
    }

    @Operation(summary = "Create a new user.", operationId = "createUser")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid user creation parameters.",
                    content = @Content(
                            schema = @Schema(implementation = RestClientError.class),
                            examples = @ExampleObject(name = ResponseExamples.DEFAULT_NAME, value = ResponseExamples.DEFAULT_VALUE))),
            @ApiResponse(
                    responseCode = "403",
                    description = "Not authorized to create users.",
                    content = @Content(
                            schema = @Schema(implementation = RestClientError.class),
                            examples = @ExampleObject(name = ResponseExamples.DEFAULT_NAME, value = ResponseExamples.DEFAULT_VALUE))),
            @ApiResponse(
                    responseCode = "409",
                    description = "User already exists.",
                    content = @Content(
                            schema = @Schema(implementation = RestClientError.class),
                            examples = @ExampleObject(name = ResponseExamples.DEFAULT_NAME, value = ResponseExamples.DEFAULT_VALUE)))
    })
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    @RequestMapping(method = RequestMethod.POST, value = "/user",
            consumes = {"application/json", "application/msgpack"}, produces = {"application/json", "application/msgpack"})
    public void createUser(@Parameter(required = true) @Valid @RequestBody RestClientUserModel userInfo,
                           @RequestHeader(value = "Authorization", required = false) String basicAuth) {

        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);
        adminService.createUser(authDetails, userInfo);
    }

    @Operation(summary = "Grant a set of roles to the specified user.", operationId = "grantRoles")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid role parameters.",
                    content = @Content(
                            schema = @Schema(implementation = RestClientError.class),
                            examples = @ExampleObject(name = ResponseExamples.DEFAULT_NAME, value = ResponseExamples.DEFAULT_VALUE))),
            @ApiResponse(
                    responseCode = "403",
                    description = "Not authorized to modify users.",
                    content = @Content(
                            schema = @Schema(implementation = RestClientError.class),
                            examples = @ExampleObject(name = ResponseExamples.DEFAULT_NAME, value = ResponseExamples.DEFAULT_VALUE))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Specified user not found.",
                    content = @Content(
                            schema = @Schema(implementation = RestClientError.class),
                            examples = @ExampleObject(name = ResponseExamples.DEFAULT_NAME, value = ResponseExamples.DEFAULT_VALUE)))
    })
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    @RequestMapping(method = RequestMethod.POST, value = "/user/{user}/role",
            consumes = {"application/json", "application/msgpack"}, produces = {"application/json", "application/msgpack"})
    public void grantRoles(@Parameter(required = true) @PathVariable(value = "user") String user,
                           @Parameter(required = true) @RequestBody List<String> roles,
                           @RequestHeader(value = "Authorization", required = false) String basicAuth) {

        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);
        adminService.grantRoles(authDetails, user, roles);
    }

    @Operation(summary = "Revoke a set of roles from the specified user.", operationId = "revokeRoles")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid role parameters.",
                    content = @Content(
                            schema = @Schema(implementation = RestClientError.class),
                            examples = @ExampleObject(name = ResponseExamples.DEFAULT_NAME, value = ResponseExamples.DEFAULT_VALUE))),
            @ApiResponse(
                    responseCode = "403",
                    description = "Not authorized to modify users.",
                    content = @Content(
                            schema = @Schema(implementation = RestClientError.class),
                            examples = @ExampleObject(name = ResponseExamples.DEFAULT_NAME, value = ResponseExamples.DEFAULT_VALUE))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Specified user not found.",
                    content = @Content(
                            schema = @Schema(implementation = RestClientError.class),
                            examples = @ExampleObject(name = ResponseExamples.DEFAULT_NAME, value = ResponseExamples.DEFAULT_VALUE)))
    })
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    @RequestMapping(method = RequestMethod.PATCH, value = "/user/{user}/role/delete",
            consumes = {"application/json", "application/msgpack"}, produces = {"application/json", "application/msgpack"})
    public void revokeRoles(
            @PathVariable(value = "user") @Parameter(description = "The user from which to revoke roles", required = true) String user,
            @RequestBody @Parameter(description = "A list of names to revoke from the user", required = true) List<String> roles,
            @RequestHeader(value = "Authorization", required = false) String basicAuth) {

        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);
        adminService.revokeRoles(authDetails, user, roles);
    }

    @Operation(summary = "Return a list of all roles registered with the Aerospike cluster.", operationId = "getRoles")
    @ApiResponse(
            responseCode = "403",
            description = "Not authorized to read role information.",
            content = @Content(
                    schema = @Schema(implementation = RestClientError.class),
                    examples = @ExampleObject(name = ResponseExamples.DEFAULT_NAME, value = ResponseExamples.DEFAULT_VALUE)))
    @RequestMapping(method = RequestMethod.GET, value = "/role", produces = {"application/json", "application/msgpack"})
    public List<RestClientRole> getRoles(@RequestHeader(value = "Authorization", required = false) String basicAuth) {

        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);
        return adminService.getRoles(authDetails);
    }

    @Operation(summary = "Create a role on the Aerospike cluster.", operationId = "createRole")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid role creation parameters.",
                    content = @Content(
                            schema = @Schema(implementation = RestClientError.class),
                            examples = @ExampleObject(name = ResponseExamples.DEFAULT_NAME, value = ResponseExamples.DEFAULT_VALUE))),
            @ApiResponse(
                    responseCode = "403",
                    description = "Not authorized to create roles.",
                    content = @Content(
                            schema = @Schema(implementation = RestClientError.class),
                            examples = @ExampleObject(name = ResponseExamples.DEFAULT_NAME, value = ResponseExamples.DEFAULT_VALUE))),
            @ApiResponse(
                    responseCode = "409",
                    description = "Role already exists.",
                    content = @Content(
                            schema = @Schema(implementation = RestClientError.class),
                            examples = @ExampleObject(name = ResponseExamples.DEFAULT_NAME, value = ResponseExamples.DEFAULT_VALUE)))
    })
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    @RequestMapping(method = RequestMethod.POST, value = "/role",
            consumes = {"application/json", "application/msgpack"}, produces = {"application/json", "application/msgpack"})
    public void createRole(@Parameter(required = true) @RequestBody RestClientRole rcRole,
                           @RequestHeader(value = "Authorization", required = false) String basicAuth) {

        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);
        adminService.createRole(authDetails, rcRole);
    }

    @Operation(summary = "Get information about a specific role.", operationId = "getRole")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "403",
                    description = "Not authorized to read role information.",
                    content = @Content(
                            schema = @Schema(implementation = RestClientError.class),
                            examples = @ExampleObject(name = ResponseExamples.DEFAULT_NAME, value = ResponseExamples.DEFAULT_VALUE))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Specified role not found.",
                    content = @Content(
                            schema = @Schema(implementation = RestClientError.class),
                            examples = @ExampleObject(name = ResponseExamples.DEFAULT_NAME, value = ResponseExamples.DEFAULT_VALUE)))
    })
    @RequestMapping(method = RequestMethod.GET, value = "/role/{name}", produces = {"application/json", "application/msgpack"})
    public RestClientRole getRole(
            @Parameter(description = "The name of the role whose information should be retrieved.", required = true) @PathVariable(value = "name") String role,
            @RequestHeader(value = "Authorization", required = false) String basicAuth) {

        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);
        return adminService.getRole(authDetails, role);
    }

    @Operation(summary = "Set maximum reads/writes per second limits for a role.", operationId = "setRoleQuotas")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid role creation parameters.",
                    content = @Content(
                            schema = @Schema(implementation = RestClientError.class),
                            examples = @ExampleObject(name = ResponseExamples.DEFAULT_NAME, value = ResponseExamples.DEFAULT_VALUE))),
            @ApiResponse(
                    responseCode = "403",
                    description = "Not authorized to create roles.",
                    content = @Content(
                            schema = @Schema(implementation = RestClientError.class),
                            examples = @ExampleObject(name = ResponseExamples.DEFAULT_NAME, value = ResponseExamples.DEFAULT_VALUE))),
            @ApiResponse(
                    responseCode = "409",
                    description = "Role already exists.",
                    content = @Content(
                            schema = @Schema(implementation = RestClientError.class),
                            examples = @ExampleObject(name = ResponseExamples.DEFAULT_NAME, value = ResponseExamples.DEFAULT_VALUE)))
    })
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    @RequestMapping(method = RequestMethod.POST, value = "/role/{name}/quota",
            consumes = {"application/json", "application/msgpack"}, produces = {"application/json", "application/msgpack"})
    public void setRoleQuotas(
            @Parameter(description = "The name of the role to which quotas will be set.", required = true) @PathVariable(value = "name") String name,
            @Parameter(required = true) @RequestBody RestClientRoleQuota roleQuota,
            @RequestHeader(value = "Authorization", required = false) String basicAuth) {

        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);
        adminService.setRoleQuotas(authDetails, name, roleQuota);
    }

    @Operation(summary = "Remove a specific role from the Aerospike cluster.", operationId = "dropRole")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "403",
                    description = "Not authorized to remove roles.",
                    content = @Content(
                            schema = @Schema(implementation = RestClientError.class),
                            examples = @ExampleObject(name = ResponseExamples.DEFAULT_NAME, value = ResponseExamples.DEFAULT_VALUE))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Specified role not found.",
                    content = @Content(
                            schema = @Schema(implementation = RestClientError.class),
                            examples = @ExampleObject(name = ResponseExamples.DEFAULT_NAME, value = ResponseExamples.DEFAULT_VALUE)))
    })
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    @RequestMapping(method = RequestMethod.DELETE, value = "/role/{name}", produces = {"application/json", "application/msgpack"})
    public void dropRole(
            @Parameter(description = "The name of the role to remove.", required = true) @PathVariable(value = "name") String role,
            @RequestHeader(value = "Authorization", required = false) String basicAuth) {

        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);
        adminService.dropRole(authDetails, role);
    }

    @Operation(summary = "Grant a list of privileges to a specific role.", operationId = "grantPrivileges")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid privilege parameters.",
                    content = @Content(
                            schema = @Schema(implementation = RestClientError.class),
                            examples = @ExampleObject(name = ResponseExamples.DEFAULT_NAME, value = ResponseExamples.DEFAULT_VALUE))),
            @ApiResponse(
                    responseCode = "403",
                    description = "Not authorized to modify roles.",
                    content = @Content(
                            schema = @Schema(implementation = RestClientError.class),
                            examples = @ExampleObject(name = ResponseExamples.DEFAULT_NAME, value = ResponseExamples.DEFAULT_VALUE))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Specified role not found.",
                    content = @Content(
                            schema = @Schema(implementation = RestClientError.class),
                            examples = @ExampleObject(name = ResponseExamples.DEFAULT_NAME, value = ResponseExamples.DEFAULT_VALUE)))
    })
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    @RequestMapping(method = RequestMethod.POST, value = "/role/{name}/privilege",
            consumes = {"application/json", "application/msgpack"}, produces = {"application/json", "application/msgpack"})
    public void grantPrivileges(
            @Parameter(description = "The name of the role to which privileges will be granted.", required = true) @PathVariable(value = "name") String name,
            @Parameter(required = true) @RequestBody List<RestClientPrivilege> privileges,
            @RequestHeader(value = "Authorization", required = false) String basicAuth) {

        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);
        adminService.grantPrivileges(authDetails, name, privileges);
    }

    @Operation(summary = "Remove a list of privileges from a specific role.", operationId = "revokePrivileges")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid privilege parameters.",
                    content = @Content(
                            schema = @Schema(implementation = RestClientError.class),
                            examples = @ExampleObject(name = ResponseExamples.DEFAULT_NAME, value = ResponseExamples.DEFAULT_VALUE))),
            @ApiResponse(
                    responseCode = "403",
                    description = "Not authorized to modify roles.",
                    content = @Content(
                            schema = @Schema(implementation = RestClientError.class),
                            examples = @ExampleObject(name = ResponseExamples.DEFAULT_NAME, value = ResponseExamples.DEFAULT_VALUE))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Specified role not found.",
                    content = @Content(
                            schema = @Schema(implementation = RestClientError.class),
                            examples = @ExampleObject(name = ResponseExamples.DEFAULT_NAME, value = ResponseExamples.DEFAULT_VALUE)))
    })
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    @RequestMapping(method = RequestMethod.PATCH, value = "/role/{name}/privilege/delete",
            consumes = {"application/json", "application/msgpack"}, produces = {"application/json", "application/msgpack"})
    public void revokePrivileges(
            @Parameter(description = "The name of the role from which privileges will be removed.", required = true) @PathVariable(value = "name") String name,
            @Parameter(required = true) @RequestBody List<RestClientPrivilege> privileges,
            @RequestHeader(value = "Authorization", required = false) String basicAuth) {

        AuthDetails authDetails = HeaderHandler.extractAuthDetails(basicAuth);
        adminService.revokePrivileges(authDetails, name, privileges);
    }
}
