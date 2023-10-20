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

import com.aerospike.client.admin.Privilege;
import com.aerospike.client.admin.PrivilegeCode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public class RestClientPrivilege {

    @NotNull
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    PrivilegeCode code;

    @Schema(description = "Namespace Scope", example = "testNS")
    String namespace;

    @Schema(description = "setName Scope", example = "testSet")
    String set;

    public PrivilegeCode getCode() {
        return this.code;
    }

    public String getNamespace() {
        return this.namespace;
    }

    public String getSet() {
        return this.set;
    }

    public void setCode(PrivilegeCode code) {
        this.code = code;
    }

    public void setNamespace(String ns) {
        this.namespace = ns;
    }

    public void setSet(String set) {
        this.set = set;
    }

    public Privilege toPrivilege() {
        Privilege privilege = new Privilege();
        privilege.code = code;
        privilege.namespace = namespace;
        privilege.setName = set;
        return privilege;
    }

    public RestClientPrivilege() {
    }

    public RestClientPrivilege(Privilege priv) {
        code = priv.code;
        namespace = priv.namespace;
        set = priv.setName;
    }
}