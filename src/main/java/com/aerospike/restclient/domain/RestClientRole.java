/*
 * Copyright 2019 Aerospike, Inc.
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
package com.aerospike.restclient.domain;

import com.aerospike.client.admin.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@JsonInclude(Include.NON_NULL)
@ApiModel(value = "RestClientRole")
public class RestClientRole {

    @NotNull
    @ApiModelProperty(required = true, value = "Role name.", example = "customRole")
    private String name;

    @NotNull
    @ApiModelProperty(required = true, value = "List of assigned privileges.")
    private List<RestClientPrivilege> privileges;

    @ApiModelProperty(value = "List of allowable IP addresses.")
    private List<String> whitelist;

    @ApiModelProperty(value = "Maximum reads per second limit.")
    private int readQuota;

    @ApiModelProperty(value = "Maximum writes per second limit.")
    private int writeQuota;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<RestClientPrivilege> getPrivileges() {
        return this.privileges;
    }

    public void setPrivileges(List<RestClientPrivilege> privileges) {
        this.privileges = privileges;
    }

    public List<String> getWhitelist() {
        return this.whitelist;
    }

    public void setWhitelist(List<String> whitelist) {
        this.whitelist = whitelist;
    }

    public int getReadQuota() {
        return this.readQuota;
    }

    public void setReadQuota(int readQuota) {
        this.readQuota = readQuota;
    }

    public int getWriteQuota() {
        return this.writeQuota;
    }

    public void setWriteQuota(int writeQuota) {
        this.writeQuota = writeQuota;
    }

    /*
     * Convert to Java Client role
     */
    public Role toRole() {
        Role role = new Role();
        role.name = name;
        role.privileges = this.privileges.stream()
                .map(RestClientPrivilege::toPrivilege).collect(Collectors.toList());
        role.whitelist = whitelist;
        role.readQuota = readQuota;
        role.writeQuota = writeQuota;

        return role;
    }

    public RestClientRole() {
    }

    /*
     * Constructor from Java Client Role
     */
    public RestClientRole(Role role) {
        name = role.name;
        privileges = role.privileges.stream()
                .map(RestClientPrivilege::new).collect(Collectors.toList());
        whitelist = role.whitelist;
        readQuota = role.readQuota;
        writeQuota = role.writeQuota;
    }
}
