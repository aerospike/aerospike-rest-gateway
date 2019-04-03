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

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import com.aerospike.client.admin.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;

@JsonInclude(Include.NON_NULL)
public class RestClientRole {

	@NotNull
	@ApiModelProperty(example="customRole")
	String name;

	@NotNull
	List<RestClientPrivilege> privileges;

	public String getName() {
		return this.name;
	}
	public List<RestClientPrivilege> getPrivileges() {
		return this.privileges;
	}

	public void setName(String name) {
		this.name = name;
	}
	public void setPrivileges(List<RestClientPrivilege> privileges) {
		this.privileges = privileges;
	}

	/*
	 * Convert to Java Client role
	 */
	public Role toRole() {
		Role role = new Role();
		role.name = name;
		role.privileges = this.privileges.stream().map(elt -> elt.toPrivilege()).collect(Collectors.toList());

		return role;
	}

	public RestClientRole() {
	}

	/*
	 * Constructor from Java Client Role
	 */
	public RestClientRole (Role role) {
		name = role.name;
		privileges = role.privileges.stream().map(elt -> new RestClientPrivilege(elt)).collect(Collectors.toList());
	}
}