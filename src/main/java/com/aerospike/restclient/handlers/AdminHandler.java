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
package com.aerospike.restclient.handlers;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.admin.Privilege;
import com.aerospike.client.admin.Role;
import com.aerospike.client.admin.User;
import com.aerospike.client.policy.AdminPolicy;

import java.util.List;

public class AdminHandler {

    private final AerospikeClient client;

    public AdminHandler(AerospikeClient client) {
        this.client = client;
    }

    /* User methods */
    public List<User> getUsers(AdminPolicy policy) {
        return client.queryUsers(policy);
    }

    public User getUser(AdminPolicy policy, String user) {
        return client.queryUser(policy, user);
    }

    public void deleteUser(AdminPolicy policy, String user) {
        client.dropUser(policy, user);
    }

    public void createUser(AdminPolicy policy, String user, String password, List<String> roles) {
        client.createUser(policy, user, password, roles);
    }

    public void changePassword(AdminPolicy policy, String user, String newPassword) {
        client.changePassword(policy, user, newPassword);
    }

    public void grantRoles(AdminPolicy policy, String user, List<String> roles) {
        client.grantRoles(policy, user, roles);
    }

    public void revokeRoles(AdminPolicy policy, String user, List<String> roles) {
        client.revokeRoles(policy, user, roles);
    }

    /* Role Methods */
    public List<Role> getRoles(AdminPolicy policy) {
        return client.queryRoles(policy);
    }

    public Role getRole(AdminPolicy policy, String roleName) {
        return client.queryRole(policy, roleName);
    }

    public void createRole(AdminPolicy policy, String roleName, List<Privilege> privileges, List<String> whitelist,
                           int readQuota, int writeQuota) {
        client.createRole(policy, roleName, privileges, whitelist, readQuota, writeQuota);
    }

    public void setRoleQuotas(AdminPolicy policy, String roleName, int readQuota, int writeQuota) {
        client.setQuotas(policy, roleName, readQuota, writeQuota);
    }

    public void deleteRole(AdminPolicy policy, String roleName) {
        client.dropRole(policy, roleName);
    }

    public void grantPrivileges(AdminPolicy policy, String roleName, List<Privilege> privileges) {
        client.grantPrivileges(policy, roleName, privileges);
    }

    public void revokePrivileges(AdminPolicy policy, String roleName, List<Privilege> privileges) {
        client.revokePrivileges(policy, roleName, privileges);
    }

    public static AdminHandler create(AerospikeClient client) {
        return new AdminHandler(client);
    }
}
