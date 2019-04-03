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
package com.aerospike.restclient.service;

import java.util.List;

import com.aerospike.client.admin.User;
import com.aerospike.restclient.domain.RestClientPrivilege;
import com.aerospike.restclient.domain.RestClientRole;
import com.aerospike.restclient.domain.RestClientUserModel;

public interface AerospikeAdminService {

	/* Users Methods*/

	public User[] getUsers();

	public void createUser(RestClientUserModel userInfo);

	public User getUser(String username);

	public void dropUser(String userName);

	public void changePassword(String username, String password);

	public void grantRoles(String username, List<String>roles);

	public void revokeRoles(String username, List<String>roles);

	/* Roles Methods */
	public int getRoleCount();

	public List<RestClientRole> getRoles();

	public void createRole(RestClientRole rcRole);

	public RestClientRole getRole(String roleName);

	public void dropRole(String roleName);

	public void grantPrivileges(String roleName, List<RestClientPrivilege> rcPrivileges);

	public void revokePrivileges(String roleName, List<RestClientPrivilege> rcPrivileges);

}