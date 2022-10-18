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
import com.aerospike.client.ResultCode;
import com.aerospike.client.admin.Privilege;
import com.aerospike.client.admin.PrivilegeCode;
import com.aerospike.client.admin.Role;
import com.aerospike.restclient.domain.RestClientPrivilege;
import com.aerospike.restclient.domain.RestClientRole;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.msgpack.jackson.dataformat.MessagePackFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(Parameterized.class)
@SpringBootTest
public class RoleTestsCorrect {

    @ClassRule
    public static final SpringClassRule springClassRule = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    private final RestRoleHandler handler;

    @Parameters
    public static Object[] getParams() {
        return new Object[]{
                new JSONRestRoleHandler(), new MsgPackRestRoleHandler()
        };
    }

    public RoleTestsCorrect(RestRoleHandler handler) {
        this.handler = handler;
    }

    String endpoint = "/v1/admin/role";
    Privilege readTestDemo;
    Privilege readWriteTestProd;
    String TestRoleName = "TestRole";
    List<String> createdRoles;
    List<Privilege> createdPrivileges;
    Role createdRole;

    public static TypeReference<RestClientRole> roleType = new TypeReference<RestClientRole>() {
    };
    public static TypeReference<List<RestClientRole>> listRoleType = new TypeReference<List<RestClientRole>>() {
    };

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
        createdRoles = new ArrayList<>();

        try {
            client.createRole(null, TestRoleName, createdPrivileges);
            createdRoles.add(TestRoleName);
        } catch (AerospikeException ignore) {
        } // If this fails, it probably means the role already exists, so it's fine.
        // Give some time for the creation to propagate to all nodes
        Thread.sleep(1000);
    }

    @After
    public void tearDown() {
        for (String role : createdRoles) {
            try {
                client.dropRole(null, role);
            } catch (AerospikeException ignore) {
            }
        }
        try {
            //Replication of the user dropping is asynchronous, so wait for it to happen
            Thread.sleep(1000);
        } catch (InterruptedException ignore) {
        }
    }

    @Test
    public void getRoles() throws Exception {
        List<RestClientRole> roleList = handler.getRoles(mockMVC,
                endpoint);//objectMapper.readValue(resJson, listRoleType);
        Assert.assertTrue(rcRolesListContainsRole(roleList, createdRole));
    }

    @Test
    public void getRole() throws Exception {
        RestClientRole rcRole = handler.getRole(mockMVC,
                endpoint + "/" + TestRoleName);//objectMapper.readValue(resJson, roleType);
        Assert.assertTrue(roleEquals(rcRole.toRole(), createdRole));
    }

    @Test
    public void deleteRole() throws Exception {
        /* Make sure the role we just created matches what we get back */
        boolean exists = true;
        mockMVC.perform(delete(endpoint + "/" + TestRoleName)).andExpect(status().isAccepted());
        Thread.sleep(1000);

        try {
            client.queryRole(null, TestRoleName);
        } catch (AerospikeException e) {
            if (e.getResultCode() == ResultCode.INVALID_ROLE) {
                exists = false;
            }
        }
        Assert.assertFalse(exists);
    }

    @Test
    public void createRole() throws Exception {
        Role testRole = new Role();
        testRole.name = "crTestRole";
        Privilege crPrivilege = new Privilege();
        crPrivilege.code = PrivilegeCode.READ_WRITE_UDF;
        crPrivilege.namespace = "test";
        crPrivilege.setName = "rwu";

        testRole.privileges = Collections.singletonList(crPrivilege);
        createdRoles.add(testRole.name);

        handler.createRole(mockMVC, endpoint, new RestClientRole(testRole));

        // Role creation isn't synchronous across the entire cluster.
        Thread.sleep(1000);

        Role fetchedRole = client.queryRole(null, testRole.name);
        Assert.assertTrue(roleEquals(fetchedRole, testRole));
    }

    @Test
    public void grantPrivilege() throws Exception {
        Privilege crPrivilege = new Privilege();
        crPrivilege.code = PrivilegeCode.SYS_ADMIN;
        RestClientPrivilege[] privAry = new RestClientPrivilege[1];
        privAry[0] = new RestClientPrivilege(crPrivilege);

        handler.grantPrivileges(mockMVC, endpoint + "/" + TestRoleName + "/privilege", privAry);

        Thread.sleep(1000);

        Role fetchedRole = client.queryRole(null, TestRoleName);
        Assert.assertTrue(RoleHasPrivilege(fetchedRole, crPrivilege));
    }

    @Test
    public void revokePrivilege() throws Exception {
        RestClientPrivilege[] privAry = new RestClientPrivilege[1];
        privAry[0] = new RestClientPrivilege(readWriteTestProd);

        handler.revokePrivileges(mockMVC, endpoint + "/" + TestRoleName + "/privilege/delete", privAry);

        Thread.sleep(1000);
        Role fetchedRole = client.queryRole(null, TestRoleName);
        Assert.assertFalse(RoleHasPrivilege(fetchedRole, readWriteTestProd));
    }

    /*
     * Check if two roles are the same.
     * They are the same iff they have the same name, the same number of privileges,
     * and every privilege in role1 is also in role2
     */
    private boolean roleEquals(Role role1, Role role2) {
        if (!role1.name.equals(role2.name)) {
            return false;
        }
        if (role1.privileges.size() != role2.privileges.size()) {
            return false;
        }
        for (Privilege privilege : role1.privileges) {
            if (!RoleHasPrivilege(role2, privilege)) {
                return false;
            }
        }
        return true;
    }

    private boolean PrivilegeEquals(Privilege priv1, Privilege priv2) {

        if (priv1.namespace == null) {
            if (priv2.namespace != null) {
                return false;
            }
        } else if (!priv1.namespace.equals(priv2.namespace)) {
            return false;
        }

        if (priv1.setName == null) {
            if (priv2.setName != null) {
                return false;
            }
        } else if (!priv1.setName.equals(priv2.setName)) {
            return false;
        }

        return priv1.code == priv2.code;
    }

    private boolean rcRolesListContainsRole(List<RestClientRole> rcRoles, Role role) {
        for (RestClientRole rcRole : rcRoles) {
            if (roleEquals(rcRole.toRole(), role)) {
                return true;
            }
        }
        return false;
    }

    private boolean RoleHasPrivilege(Role role, Privilege privilege) {
        for (Privilege testPriv : role.privileges) {
            if (PrivilegeEquals(testPriv, privilege)) {
                return true;
            }
        }
        return false;
    }

}

interface RestRoleHandler {
    List<RestClientRole> getRoles(MockMvc mockMVC, String endpoint) throws Exception;
    RestClientRole getRole(MockMvc mockMVC, String endpoint) throws Exception;
    void createRole(MockMvc mockMVC, String endpoint, RestClientRole role) throws Exception;
    void grantPrivileges(MockMvc mockMVC, String endpoint, RestClientPrivilege[] privileges) throws Exception;
    void revokePrivileges(MockMvc mockMVC, String endpoint, RestClientPrivilege[] privileges) throws Exception;
}

class JSONRestRoleHandler implements RestRoleHandler {
    ObjectMapper mapper;
    private final MediaType mediaType = MediaType.APPLICATION_JSON;

    public JSONRestRoleHandler() {
        mapper = new ObjectMapper();
    }

    @Override
    public List<RestClientRole> getRoles(MockMvc mockMVC, String endpoint) throws Exception {
        String resJson = mockMVC.perform(get(endpoint).accept(mediaType))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return mapper.readValue(resJson, RoleTestsCorrect.listRoleType);
    }

    @Override
    public RestClientRole getRole(MockMvc mockMVC, String endpoint) throws Exception {
        String resJson = mockMVC.perform(get(endpoint).accept(mediaType))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return mapper.readValue(resJson, RoleTestsCorrect.roleType);
    }

    @Override
    public void createRole(MockMvc mockMVC, String endpoint, RestClientRole role) throws Exception {
        String createRoleContent = mapper.writeValueAsString(role);

        mockMVC.perform(post(endpoint).contentType(mediaType).content(createRoleContent))
                .andExpect(status().isAccepted());
    }

    @Override
    public void grantPrivileges(MockMvc mockMVC, String endpoint, RestClientPrivilege[] privileges) throws Exception {
        String privsContent = mapper.writeValueAsString(privileges);
        mockMVC.perform(post(endpoint).contentType(mediaType).content(privsContent)).andExpect(status().isAccepted());
    }

    @Override
    public void revokePrivileges(MockMvc mockMVC, String endpoint, RestClientPrivilege[] privileges) throws Exception {
        String privsContent = mapper.writeValueAsString(privileges);
        mockMVC.perform(patch(endpoint).contentType(mediaType).content(privsContent)).andExpect(status().isAccepted());
    }

}

class MsgPackRestRoleHandler implements RestRoleHandler {

    ObjectMapper mapper;
    private final MediaType mediaType = new MediaType("application", "msgpack");

    public MsgPackRestRoleHandler() {
        mapper = new ObjectMapper(new MessagePackFactory());
    }

    @Override
    public List<RestClientRole> getRoles(MockMvc mockMVC, String endpoint) throws Exception {
        byte[] resBytes = mockMVC.perform(get(endpoint).accept(mediaType))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsByteArray();
        return mapper.readValue(resBytes, RoleTestsCorrect.listRoleType);
    }

    @Override
    public RestClientRole getRole(MockMvc mockMVC, String endpoint) throws Exception {
        byte[] resBytes = mockMVC.perform(get(endpoint).accept(mediaType))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsByteArray();

        return mapper.readValue(resBytes, RoleTestsCorrect.roleType);
    }

    @Override
    public void createRole(MockMvc mockMVC, String endpoint, RestClientRole role) throws Exception {
        byte[] createRoleContent = mapper.writeValueAsBytes(role);

        mockMVC.perform(post(endpoint).contentType(mediaType).content(createRoleContent))
                .andExpect(status().isAccepted());
    }

    @Override
    public void grantPrivileges(MockMvc mockMVC, String endpoint, RestClientPrivilege[] privileges) throws Exception {
        byte[] privsContent = mapper.writeValueAsBytes(privileges);
        mockMVC.perform(post(endpoint).contentType(mediaType).content(privsContent)).andExpect(status().isAccepted());
    }

    @Override
    public void revokePrivileges(MockMvc mockMVC, String endpoint, RestClientPrivilege[] privileges) throws Exception {
        byte[] privsContent = mapper.writeValueAsBytes(privileges);
        mockMVC.perform(patch(endpoint).contentType(mediaType).content(privsContent)).andExpect(status().isAccepted());
    }

}