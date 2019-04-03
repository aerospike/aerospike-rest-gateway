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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aerospike.client.admin.Privilege;
import com.aerospike.client.admin.Role;
import com.aerospike.client.admin.User;
import com.aerospike.restclient.domain.RestClientPrivilege;
import com.aerospike.restclient.domain.RestClientRole;
import com.aerospike.restclient.domain.RestClientUserModel;
import com.aerospike.restclient.handlers.AdminHandler;

@Service
class AerospikeAdminServiceV1 implements AerospikeAdminService{

	private AdminHandler handler;

	@Autowired
	public AerospikeAdminServiceV1(AdminHandler handler) {
		this.handler = handler;
	}
	/* UserController Methods */

	@Override
	public User[] getUsers() {
		List<User> userList = handler.getUsers(null);
		User[] usrAry = userList.toArray(new User[0]);
		return usrAry;
	}

	@Override
	public User getUser(String username) {
		User user = handler.getUser(null, username);
		return user;
	}

	@Override
	public void dropUser(String userName) {
		handler.deleteUser(null, userName);
	}

	@Override
	public void createUser(RestClientUserModel userInfo) {
		String username = userInfo.getUsername();
		String password = userInfo.getPassword();
		String[] rolesAry = userInfo.getRoles();
		handler.createUser(null, username, password, Arrays.asList(rolesAry));
	}

	@Override
	public void changePassword(String user, String password) {
		handler.changePassword(null, user, password);
	}

	@Override
	public void grantRoles(String username, List<String>roles) {
		handler.grantRoles(null, username, roles);
	}

	@Override
	public void revokeRoles(String username, List<String>roles) {
		handler.revokeRoles(null, username, roles);
	}

	/* RoleController Methods */

	@Override
	public int getRoleCount() {
		return handler.getRoles(null).size();
	}

	@Override
	public List<RestClientRole> getRoles() {
		List<Role> roleList = handler.getRoles(null);
		return roleList.stream().map(elt -> new RestClientRole(elt)).collect(Collectors.toList());
	}

	@Override
	public void createRole(RestClientRole rcRole) {
		Role asRole = rcRole.toRole();
		handler.createRole(null, asRole.name, asRole.privileges);
	}

	@Override
	public RestClientRole getRole(String roleName) {
		Role role = handler.getRole(null, roleName);
		return new RestClientRole(role);
	}

	@Override
	public void dropRole(String roleName) {
		handler.deleteRole(null, roleName);
	}

	@Override
	public void grantPrivileges(String roleName, List<RestClientPrivilege> rcPrivileges) {
		List<Privilege>privileges = rcPrivileges.stream().map(elt -> elt.toPrivilege()).collect(Collectors.toList());
		handler.grantPrivileges(null, roleName, privileges);
	}

	@Override
	public void revokePrivileges(String roleName, List<RestClientPrivilege> rcPrivileges) {
		List<Privilege>privileges = rcPrivileges.stream().map(elt -> elt.toPrivilege()).collect(Collectors.toList());

		handler.revokePrivileges(null, roleName, privileges);
	}

}