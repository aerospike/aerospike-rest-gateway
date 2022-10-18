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
import com.aerospike.client.Info;
import com.aerospike.client.cluster.Node;
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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(Parameterized.class)
@SpringBootTest
public class InfoTests {

    @ClassRule
    public static final SpringClassRule springClassRule = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    public static TypeReference<Map<String, String>> infoResponseType = new TypeReference<Map<String, String>>() {
    };

    private final InfoPerformer performer;

    @Autowired
    private AerospikeClient client;

    @Autowired
    WebApplicationContext wac;

    private MockMvc mockMVC = null;

    private final String endpoint = "/v1/info";

    private Node testNode;

    @Parameters
    public static Object[] getParams() {
        return new Object[]{
                new JSONInfoPerformer(), new MsgPackInfoPerformer()
        };
    }

    @Before
    public void setup() {
        mockMVC = MockMvcBuilders.webAppContextSetup(wac).build();
        testNode = client.getNodes()[0];
    }

    public InfoTests(InfoPerformer performer) {
        this.performer = performer;
    }

    @Test
    public void testSingleInfoCommand() throws Exception {
        String command = "edition";
        List<String> commands = Arrays.asList(command);
        Map<String, String> responses = performer.performInfoAndReturn(endpoint, commands, mockMVC);

        Assert.assertTrue(responses.containsKey(command));
        String restClientResponse = responses.get(command);

        String clientResponse = Info.request(null, testNode, command);

        Assert.assertEquals(restClientResponse, clientResponse);

    }

    @Test
    public void testMultipleInfoCommands() throws Exception {
        String command1 = "edition";
        String command2 = "build";
        List<String> commands = Arrays.asList(command1, command2);
        Map<String, String> responses = performer.performInfoAndReturn(endpoint, commands, mockMVC);

        Assert.assertTrue(responses.containsKey(command1));
        Assert.assertTrue(responses.containsKey(command2));

        Map<String, String> clientResponses = Info.request(null, testNode, command1, command2);

        Assert.assertTrue(ASTestUtils.compareStringMap(responses, clientResponses));

    }

    @Test
    public void testNonExistentInfoCommand() throws Exception {
        String realCommand = "edition";
        // If this becomes a real command, the test will fail.
        String fakeCommand = "asdfasdfasdf";
        List<String> commands = Arrays.asList(realCommand, fakeCommand);
        Map<String, String> responses = performer.performInfoAndReturn(endpoint, commands, mockMVC);

        Assert.assertTrue(responses.containsKey(realCommand));
        Assert.assertFalse(responses.containsKey(fakeCommand));

        Map<String, String> clientResponses = Info.request(null, testNode, realCommand, fakeCommand);

        Assert.assertTrue(ASTestUtils.compareStringMap(responses, clientResponses));

    }

    @Test
    public void testSendCommandToSingleNode() throws Exception {
        String nameCommand = "name";
        String nodeName = testNode.getName();
        // If this becomes a real command, the test will fail.
        List<String> commands = Arrays.asList(nameCommand);
        String singleNodeEndpoint = endpoint + "/" + nodeName;
        Map<String, String> responses = performer.performInfoAndReturn(singleNodeEndpoint, commands, mockMVC);
        String rcNodeName = responses.get(nameCommand);

        String clientNodeName = Info.request(null, testNode, nameCommand);

        Assert.assertEquals(rcNodeName, clientNodeName);

    }

}

interface InfoPerformer {
    Map<String, String> performInfoAndReturn(String endpoint, List<String> commands, MockMvc mockMVC) throws Exception;
}

class JSONInfoPerformer implements InfoPerformer {
    private final ObjectMapper mapper;

    public JSONInfoPerformer() {
        mapper = new ObjectMapper();
    }

    @Override
    public Map<String, String> performInfoAndReturn(String endpoint, List<String> commands,
                                                    MockMvc mockMVC) throws Exception {
        String jsonCommands = mapper.writeValueAsString(commands);
        String results = mockMVC.perform(post(endpoint).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonCommands)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        return mapper.readValue(results, InfoTests.infoResponseType);
    }
}

class MsgPackInfoPerformer implements InfoPerformer {
    private final ObjectMapper mapper;
    private final MediaType mediaType = new MediaType("application", "msgpack");

    public MsgPackInfoPerformer() {
        mapper = new ObjectMapper(new MessagePackFactory());

    }

    @Override
    public Map<String, String> performInfoAndReturn(String endpoint, List<String> commands,
                                                    MockMvc mockMVC) throws Exception {
        byte[] jsonCommands = mapper.writeValueAsBytes(commands);
        byte[] results = mockMVC.perform(post(endpoint).accept(mediaType).contentType(mediaType).content(jsonCommands))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsByteArray();
        return mapper.readValue(results, InfoTests.infoResponseType);
    }
}