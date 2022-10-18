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
import com.aerospike.client.admin.Role;
import com.aerospike.client.admin.User;
import com.aerospike.client.policy.ClientPolicy;
import com.aerospike.client.policy.TlsPolicy;
import com.aerospike.restclient.domain.RestClientUserModel;
import com.aerospike.restclient.util.TLSPolicyBuilder;
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

import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(Parameterized.class)
@SpringBootTest
public class UserTestsCorrect {

    @ClassRule
    public static final SpringClassRule springClassRule = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    private final RestUserHandler handler;

    @Parameters
    public static Object[] getParams() {
        return new Object[]{
                new JSONRestUserHandler(), new MsgPackRestUserHandler()
        };
    }

    public UserTestsCorrect(RestUserHandler handler) {
        this.handler = handler;
    }

    List<String> createdUsers;
    List<String> createdRoles = Arrays.asList(Role.SysAdmin, Role.DataAdmin, Role.Read);
    public static TypeReference<List<Map<String, Object>>> userListType = new TypeReference<List<Map<String, Object>>>() {
    };
    public static TypeReference<Map<String, Object>> userType = new TypeReference<Map<String, Object>>() {
    };
    String userName = "JunitUser";

    private MockMvc mockMVC;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private AerospikeClient client;

    @Autowired
    private TLSPolicyBuilder policyBuilder;

    private static TlsPolicy tlsPolicy;

    private final String endpoint = "/v1/admin/user";

    @BeforeClass
    public static void okToRun() {
        Assume.assumeTrue(ASTestUtils.runningWithAuth());
    }

    @Before
    public void setup() throws InterruptedException {
        mockMVC = MockMvcBuilders.webAppContextSetup(wac).build();
        createdUsers = new ArrayList<>();
        try {
            client.createUser(null, userName, "password", createdRoles);
        } catch (AerospikeException e) {
            // IF the user already exists that's OK.
            if (e.getResultCode() != ResultCode.USER_ALREADY_EXISTS) {
                throw e;
            }
        }
        if (tlsPolicy == null) {
            tlsPolicy = policyBuilder.build();
        }

        createdUsers.add(userName);
        // Give some time for the creation to propagate to all nodes
        Thread.sleep(2000);
    }

    @After
    public void tearDown() {
        for (String user : createdUsers) {
            try {
                client.dropUser(null, user);
            } catch (AerospikeException ignore) {
            }
        }
        try {
            //Replication of the user dropping is asynchronous, so wait for it to happen
            Thread.sleep(2000);
        } catch (InterruptedException ignore) {
        }
    }

    @Test
    public void getUsers() throws Exception {
        /* Get all users and verify that the one we just created is included*/

        List<Map<String, Object>> UserList = handler.getUsers(mockMVC, endpoint);
        Assert.assertTrue(usersListContainsUser(UserList, userName));
    }

    @Test
    public void createUser() throws Exception {
        String newUser = "newUser";
        String newPass = "topSecret";
        String[] roles = {Role.ReadWrite, Role.ReadWriteUdf};
        List<String> rolesList = Arrays.asList(roles);

        RestClientUserModel userModel = new RestClientUserModel();
        userModel.setUsername(newUser);
        userModel.setPassword(newPass);
        userModel.setRoles(roles);
        createdUsers.add(newUser);
        /* Get information on the user we just created*/
        handler.createUser(mockMVC, endpoint, userModel);

        Thread.sleep(1000);
        ClientPolicy policy = ASTestUtils.getClientPolicy(tlsPolicy, newUser, newPass);
        policy.user = newUser;
        policy.password = newPass;

        AerospikeClient newClient = new AerospikeClient(policy, ASTestUtils.getHost());
        Assert.assertTrue(newClient.isConnected());
        newClient.close();

        Assert.assertTrue(userHasRoles(newUser, rolesList));
    }

    @Test
    public void getUser() throws Exception {
        Map<String, Object> userObj = handler.getUser(mockMVC, endpoint + "/" + userName);

        Assert.assertEquals(userName, userObj.get("name"));
        Assert.assertTrue(userObjectHasRole(userObj, createdRoles.get(0)));
    }

    @Test
    public void deleteUser() throws Exception {
        /* Delete a user we just created and verify that it no longer exists*/
        mockMVC.perform(delete(endpoint + "/" + userName).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());

        int resultCode = ResultCode.OK;
        // Wait for the delete to process
        Thread.sleep(1000);
        try {
            client.queryUser(null, userName);
        } catch (AerospikeException e) {
            resultCode = e.getResultCode();
        }
        Assert.assertEquals(resultCode, ResultCode.INVALID_USER);
    }

    @Test
    public void patchUser() throws Exception {
        String newPassword = "SuperSecret";

        handler.patchUser(mockMVC, endpoint + "/" + userName, newPassword);

        Thread.sleep(1500);

        ClientPolicy policy = ASTestUtils.getClientPolicy(tlsPolicy, userName, newPassword);
        policy.user = userName;
        // Object mapper writes these wrapped with quotes
        policy.password = newPassword;

        AerospikeClient newClient = new AerospikeClient(policy, ASTestUtils.getHost());
        Assert.assertTrue(newClient.isConnected());
        newClient.close();
    }

    @Test
    public void grantRoles() throws Exception {
        String[] roles = {Role.ReadWrite, Role.ReadWriteUdf};
        List<String> rolesList = Arrays.asList(roles);

        handler.grantRoles(mockMVC, endpoint + "/" + userName + "/role", rolesList);

        Thread.sleep(1000);

        Assert.assertTrue(userHasRoles(userName, rolesList));
        // Verify that we are adding roles and not replacing
        Assert.assertTrue(userHasRoles(userName, createdRoles));
    }

    @Test
    public void deleteRoles() throws Exception {
        String[] deleteRoles = {Role.SysAdmin, Role.Read};
        List<String> rolesList = Arrays.asList(deleteRoles);

        handler.revokeRoles(mockMVC, endpoint + "/" + userName + "/role/delete", rolesList);

        Thread.sleep(1000);
        Assert.assertTrue(userDoesNotHaveRoles(userName, rolesList));
    }

    private boolean usersListContainsUser(List<Map<String, Object>> users, String searchUser) {
        for (Map<String, Object> testUser : users) {
            if (testUser.get("name").equals(searchUser)) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private boolean userObjectHasRole(Map<String, Object> user, String role) {
        List<String> roles = (List<String>) user.get("roles");
        for (String testRole : roles) {
            if (testRole.equals(role)) {
                return true;
            }
        }
        return false;
    }

    private boolean userHasRoles(String username, List<String> roles) {
        User user = client.queryUser(null, username);
        Set<String> searchRoles = new HashSet<>(roles);
        Set<String> actualRoles = new HashSet<>(user.roles);

        return actualRoles.containsAll(searchRoles);
    }

    private boolean userDoesNotHaveRoles(String username, List<String> roles) {
        User user = client.queryUser(null, username);
        Set<String> searchRoles = new HashSet<>(roles);
        Set<String> actualRoles = new HashSet<>(user.roles);
        searchRoles.retainAll(actualRoles);
        return searchRoles.size() == 0;
    }

}

interface RestUserHandler {
    List<Map<String, Object>> getUsers(MockMvc mockMVC, String endpoint) throws Exception;

    Map<String, Object> getUser(MockMvc mockMVC, String endpoint) throws Exception;

    void createUser(MockMvc mockMVC, String endpoint, RestClientUserModel user) throws Exception;

    // Change password
    void patchUser(MockMvc mockMVC, String endpoint, String newPassword) throws Exception;

    void grantRoles(MockMvc mockMVC, String endpoint, List<String> roles) throws Exception;

    void revokeRoles(MockMvc mockMVC, String endpoint, List<String> roles) throws Exception;
}

class JSONRestUserHandler implements RestUserHandler {
    ObjectMapper mapper;
    private final MediaType mediaType = MediaType.APPLICATION_JSON;

    public JSONRestUserHandler() {
        mapper = new ObjectMapper();
    }

    @Override
    public List<Map<String, Object>> getUsers(MockMvc mockMVC, String endpoint) throws Exception {
        String resJson = mockMVC.perform(get(endpoint).accept(mediaType))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return mapper.readValue(resJson, UserTestsCorrect.userListType);
    }

    @Override
    public Map<String, Object> getUser(MockMvc mockMVC, String endpoint) throws Exception {
        String resJson = mockMVC.perform(get(endpoint).accept(mediaType))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        return mapper.readValue(resJson, UserTestsCorrect.userType);
    }

    @Override
    public void createUser(MockMvc mockMVC, String endpoint, RestClientUserModel user) throws Exception {
        String userJson = mapper.writeValueAsString(user);
        mockMVC.perform(post(endpoint).contentType(mediaType).content(userJson)).andExpect(status().isAccepted());
    }

    @Override
    public void patchUser(MockMvc mockMVC, String endpoint, String newPassword) throws Exception {
        String passJson = mapper.writeValueAsString(newPassword);
        mockMVC.perform(patch(endpoint).contentType(mediaType).content(passJson)).andExpect(status().isAccepted());
    }

    @Override
    public void grantRoles(MockMvc mockMVC, String endpoint, List<String> roles) throws Exception {
        String rolesJson = mapper.writeValueAsString(roles);
        mockMVC.perform(post(endpoint).contentType(mediaType).content(rolesJson)).andExpect(status().isAccepted());
    }

    @Override
    public void revokeRoles(MockMvc mockMVC, String endpoint, List<String> roles) throws Exception {
        String rolesJson = mapper.writeValueAsString(roles);
        mockMVC.perform(patch(endpoint).contentType(mediaType).content(rolesJson)).andExpect(status().isAccepted());
    }

}

class MsgPackRestUserHandler implements RestUserHandler {
    ObjectMapper mapper;
    private final MediaType mediaType = new MediaType("application", "msgpack");

    public MsgPackRestUserHandler() {
        mapper = new ObjectMapper(new MessagePackFactory());
    }

    @Override
    public List<Map<String, Object>> getUsers(MockMvc mockMVC, String endpoint) throws Exception {
        byte[] resBytes = mockMVC.perform(get(endpoint).accept(mediaType))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsByteArray();

        return mapper.readValue(resBytes, UserTestsCorrect.userListType);
    }

    @Override
    public Map<String, Object> getUser(MockMvc mockMVC, String endpoint) throws Exception {
        byte[] userBytes = mockMVC.perform(get(endpoint).accept(mediaType))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsByteArray();
        return mapper.readValue(userBytes, UserTestsCorrect.userType);
    }

    @Override
    public void createUser(MockMvc mockMVC, String endpoint, RestClientUserModel user) throws Exception {
        byte[] userPayload = mapper.writeValueAsBytes(user);
        mockMVC.perform(post(endpoint).contentType(mediaType).content(userPayload)).andExpect(status().isAccepted());
    }

    @Override
    public void patchUser(MockMvc mockMVC, String endpoint, String newPassword) throws Exception {
        byte[] passwordPayload = mapper.writeValueAsBytes(newPassword);
        mockMVC.perform(patch(endpoint).contentType(mediaType).content(passwordPayload))
                .andExpect(status().isAccepted());
    }

    @Override
    public void grantRoles(MockMvc mockMVC, String endpoint, List<String> roles) throws Exception {
        byte[] rolesPayload = mapper.writeValueAsBytes(roles);
        mockMVC.perform(post(endpoint).contentType(mediaType).content(rolesPayload)).andExpect(status().isAccepted());
    }

    @Override
    public void revokeRoles(MockMvc mockMVC, String endpoint, List<String> roles) throws Exception {
        byte[] rolesPayload = mapper.writeValueAsBytes(roles);
        mockMVC.perform(patch(endpoint).contentType(mediaType).content(rolesPayload)).andExpect(status().isAccepted());
    }

}
