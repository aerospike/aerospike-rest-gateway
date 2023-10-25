/*
 * Copyright 2022 Aerospike, Inc.
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
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@JsonInclude(Include.NON_NULL)
public class RestClientRole {

    @NotNull
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "Role name.", example = "customRole")
    private String name;

    @NotNull
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "List of assigned privileges.")
    private List<RestClientPrivilege> privileges;

    @Schema(description = "List of allowable IP addresses.")
    private List<String> whitelist;

    @Schema(description = "Maximum reads per second limit.")
    private int readQuota;

    @Schema(description = "Maximum writes per second limit.")
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
        role.privileges = this.privileges.stream().map(RestClientPrivilege::toPrivilege).toList();
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
        privileges = role.privileges.stream().map(RestClientPrivilege::new).toList();
        whitelist = role.whitelist;
        readQuota = role.readQuota;
        writeQuota = role.writeQuota;
    }
}
