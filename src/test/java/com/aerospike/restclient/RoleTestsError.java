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
package com.aerospike.restclient;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.AerospikeException;
import com.aerospike.client.admin.Privilege;
import com.aerospike.client.admin.PrivilegeCode;
import com.aerospike.client.admin.Role;
import com.aerospike.restclient.domain.RestClientPrivilege;
import com.aerospike.restclient.domain.RestClientRole;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RoleTestsError {

    String endpoint = "/v1/admin/role";
    Privilege readTestDemo;
    Privilege readWriteTestProd;
    String TestRoleName = "TestRole";
    List<String> createdRoles;
    List<Privilege> createdPrivileges;
    Role createdRole;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMVC;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private AerospikeClient client;

    @BeforeClass
    public static void okToRun() {
        Assume.assumeTrue(ASTestUtils.runningWithAuth());
    }

    @Before
    public void setup() throws InterruptedException {
        mockMVC = MockMvcBuilders.webAppContextSetup(wac).build();
        readTestDemo = new Privilege();
        readTestDemo.code = PrivilegeCode.READ;
        readTestDemo.namespace = "test";
        readTestDemo.setName = "demoo";

        readWriteTestProd = new Privilege();
        readWriteTestProd.code = PrivilegeCode.READ_WRITE;
        readWriteTestProd.namespace = "test";
        readWriteTestProd.setName = "prod";

        createdPrivileges = Arrays.asList(readTestDemo, readWriteTestProd);

        createdRole = new Role();
        createdRole.name = TestRoleName;
        createdRole.privileges = createdPrivileges;
        createdRoles = new ArrayList<String>();

        try {
            client.createRole(null, TestRoleName, createdPrivileges);
            createdRoles.add(TestRoleName);
        } catch (AerospikeException e) {
        } // If this fails, it probably means the role already exists, so it's fine.
        // Give some time for the creation to propagate to all nodes
        Thread.sleep(1000);
    }

    @After
    public void tearDown() {
        for (String role : createdRoles) {
            try {
                client.dropRole(null, role);
            } catch (AerospikeException e) {
            }
        }
        try {
            //Replication of the user dropping is asynchronous, so wait for it to happen
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
    }

    @Test
    public void getRoleThatDoesNotExist() throws Exception {
        mockMVC.perform(get(endpoint + "/" + "NotARealRole")).andExpect(status().isNotFound());
    }

    @Test
    public void deleteRoleThatDoesNotExist() throws Exception {
        mockMVC.perform(delete(endpoint + "/" + "NotARealRole")).andExpect(status().isNotFound());
    }

    @Test
    public void createRoleThatAlreadyExists() throws Exception {

        String createRoleContent = objectMapper.writeValueAsString(new RestClientRole(createdRole));

        mockMVC.perform(post(endpoint).contentType(MediaType.APPLICATION_JSON).content(createRoleContent))
                .andExpect(status().isConflict());
    }

    @Test
    public void grantPrivilegeToNonExistentRole() throws Exception {
        Privilege crPrivilege = new Privilege();
        crPrivilege.code = PrivilegeCode.SYS_ADMIN;
        RestClientPrivilege[] privAry = new RestClientPrivilege[1];
        privAry[0] = new RestClientPrivilege(crPrivilege);

        String privsContent = objectMapper.writeValueAsString(privAry);
        mockMVC.perform(post(endpoint + "/" + "NotARealRole" + "/privilege").contentType(MediaType.APPLICATION_JSON)
                .content(privsContent)).andExpect(status().isNotFound());
    }

    @Test
    public void revokePrivilegeFromNonExistentRole() throws Exception {
        RestClientPrivilege[] privAry = new RestClientPrivilege[1];
        privAry[0] = new RestClientPrivilege(readWriteTestProd);

        String privsContent = objectMapper.writeValueAsString(privAry);
        mockMVC.perform(
                patch(endpoint + "/" + "NotARealRole" + "/privilege/delete").contentType(MediaType.APPLICATION_JSON)
                        .content(privsContent)).andExpect(status().isNotFound());
    }

}