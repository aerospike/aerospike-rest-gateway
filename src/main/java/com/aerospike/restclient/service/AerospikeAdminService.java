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
package com.aerospike.restclient.service;

import com.aerospike.client.admin.User;
import com.aerospike.restclient.domain.RestClientPrivilege;
import com.aerospike.restclient.domain.RestClientRole;
import com.aerospike.restclient.domain.RestClientRoleQuota;
import com.aerospike.restclient.domain.RestClientUserModel;
import com.aerospike.restclient.domain.auth.AuthDetails;

import java.util.List;

public interface AerospikeAdminService {

    /* Users Methods*/
    User[] getUsers(AuthDetails authDetails);

    void createUser(AuthDetails authDetails, RestClientUserModel userInfo);

    User getUser(AuthDetails authDetails, String username);

    void dropUser(AuthDetails authDetails, String userName);

    void changePassword(AuthDetails authDetails, String username, String password);

    void grantRoles(AuthDetails authDetails, String username, List<String> roles);

    void revokeRoles(AuthDetails authDetails, String username, List<String> roles);

    /* Roles Methods */
    int getRoleCount(AuthDetails authDetails);

    List<RestClientRole> getRoles(AuthDetails authDetails);

    void createRole(AuthDetails authDetails, RestClientRole rcRole);

    void setRoleQuotas(AuthDetails authDetails, String roleName, RestClientRoleQuota roleQuota);

    RestClientRole getRole(AuthDetails authDetails, String roleName);

    void dropRole(AuthDetails authDetails, String roleName);

    void grantPrivileges(AuthDetails authDetails, String roleName, List<RestClientPrivilege> rcPrivileges);

    void revokePrivileges(AuthDetails authDetails, String roleName, List<RestClientPrivilege> rcPrivileges);
}
