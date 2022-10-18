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

import com.aerospike.client.admin.Privilege;
import com.aerospike.client.admin.Role;
import com.aerospike.client.admin.User;
import com.aerospike.restclient.domain.RestClientPrivilege;
import com.aerospike.restclient.domain.RestClientRole;
import com.aerospike.restclient.domain.RestClientRoleQuota;
import com.aerospike.restclient.domain.RestClientUserModel;
import com.aerospike.restclient.domain.auth.AuthDetails;
import com.aerospike.restclient.handlers.AdminHandler;
import com.aerospike.restclient.util.AerospikeClientPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
class AerospikeAdminServiceV1 implements AerospikeAdminService {

    @Autowired
    private AerospikeClientPool clientPool;

    /* UserController Methods */
    @Override
    public User[] getUsers(AuthDetails authDetails) {
        List<User> userList = AdminHandler.create(clientPool.getClient(authDetails)).getUsers(null);
        return userList.toArray(new User[0]);
    }

    @Override
    public User getUser(AuthDetails authDetails, String username) {
        return AdminHandler.create(clientPool.getClient(authDetails)).getUser(null, username);
    }

    @Override
    public void dropUser(AuthDetails authDetails, String userName) {
        AdminHandler.create(clientPool.getClient(authDetails)).deleteUser(null, userName);
    }

    @Override
    public void createUser(AuthDetails authDetails, RestClientUserModel userInfo) {
        String username = userInfo.getUsername();
        String password = userInfo.getPassword();
        String[] rolesAry = userInfo.getRoles();
        AdminHandler.create(clientPool.getClient(authDetails))
                .createUser(null, username, password, Arrays.asList(rolesAry));
    }

    @Override
    public void changePassword(AuthDetails authDetails, String user, String password) {
        AdminHandler.create(clientPool.getClient(authDetails)).changePassword(null, user, password);
    }

    @Override
    public void grantRoles(AuthDetails authDetails, String username, List<String> roles) {
        AdminHandler.create(clientPool.getClient(authDetails)).grantRoles(null, username, roles);
    }

    @Override
    public void revokeRoles(AuthDetails authDetails, String username, List<String> roles) {
        AdminHandler.create(clientPool.getClient(authDetails)).revokeRoles(null, username, roles);
    }

    /* RoleController Methods */
    @Override
    public int getRoleCount(AuthDetails authDetails) {
        return AdminHandler.create(clientPool.getClient(authDetails)).getRoles(null).size();
    }

    @Override
    public List<RestClientRole> getRoles(AuthDetails authDetails) {
        List<Role> roleList = AdminHandler.create(clientPool.getClient(authDetails)).getRoles(null);
        return roleList.stream().map(RestClientRole::new).toList();
    }

    @Override
    public void createRole(AuthDetails authDetails, RestClientRole rcRole) {
        Role asRole = rcRole.toRole();
        AdminHandler.create(clientPool.getClient(authDetails))
                .createRole(null, asRole.name, asRole.privileges, asRole.whitelist, asRole.readQuota,
                        asRole.writeQuota);
    }

    @Override
    public void setRoleQuotas(AuthDetails authDetails, String roleName, RestClientRoleQuota roleQuota) {
        AdminHandler.create(clientPool.getClient(authDetails))
                .setRoleQuotas(null, roleName, roleQuota.getReadQuota(), roleQuota.getWriteQuota());
    }

    @Override
    public RestClientRole getRole(AuthDetails authDetails, String roleName) {
        Role role = AdminHandler.create(clientPool.getClient(authDetails)).getRole(null, roleName);
        return new RestClientRole(role);
    }

    @Override
    public void dropRole(AuthDetails authDetails, String roleName) {
        AdminHandler.create(clientPool.getClient(authDetails)).deleteRole(null, roleName);
    }

    @Override
    public void grantPrivileges(AuthDetails authDetails, String roleName, List<RestClientPrivilege> rcPrivileges) {
        List<Privilege> privileges = rcPrivileges.stream().map(RestClientPrivilege::toPrivilege).toList();
        AdminHandler.create(clientPool.getClient(authDetails)).grantPrivileges(null, roleName, privileges);
    }

    @Override
    public void revokePrivileges(AuthDetails authDetails, String roleName, List<RestClientPrivilege> rcPrivileges) {
        List<Privilege> privileges = rcPrivileges.stream().map(RestClientPrivilege::toPrivilege).toList();

        AdminHandler.create(clientPool.getClient(authDetails)).revokePrivileges(null, roleName, privileges);
    }
}
