package com.alexquasar.authenticationService.web.input;

import com.alexquasar.authenticationService.entity.User;
import com.alexquasar.authenticationService.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.ConfigurableMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.Assert.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {

    MockMvc mockMvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    @Autowired
    UserRepository userRepository;

    @Autowired
    ObjectMapper mapper;

    Logger log = Logger.getLogger(UserControllerTest.class.getName());
    String userController = "/userController";

    User user;
    User newUser;

    @Before
    public void setUp() throws Exception {
        ConfigurableMockMvcBuilder builder =
                MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
                        .apply(documentationConfiguration(this.restDocumentation));
        this.mockMvc = builder.build();

        user = new User();
        user.setId(6);
        user.setName("user1");

        newUser = new User();
        newUser.setId(999);
        newUser.setName("UserName");
    }

    @Test
    @Transactional
    public void addUserTest() throws Exception {
        String addUser = userController + "/addUser";

        int expectedVisitsSize = userRepository.findAll().size() + 1;

        mockMvc.perform(post(addUser)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(newUser)))
        .andExpect(status().isOk());

        assertEquals(expectedVisitsSize, userRepository.findAll().size());
    }

    @Test
    @Transactional
    public void addUsersTest() throws Exception {
        String addUsers = userController + "/addUsers";

        List<User> users = Collections.singletonList(newUser);
        int expectedVisitsSize = userRepository.findAll().size() + users.size();

        mockMvc.perform(post(addUsers)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(users)))
        .andExpect(status().isOk())
        .andDo(document(addUsers));

        assertEquals(expectedVisitsSize, userRepository.findAll().size());
    }

    @Test
    public void findByIdTest() throws Exception {
        String findById = userController + "/findById";

        String stringId = Integer.toString(user.getId());

        MvcResult result = mockMvc.perform(get(findById)
                .accept(MediaType.APPLICATION_JSON)
                .param("id", stringId))
        .andExpect(status().isOk())
        .andReturn();

        String content = result.getResponse().getContentAsString();
        User returnUser = mapper.readValue(content, User.class);

        assertTrue(equalsFields(user, returnUser));
    }

    @Test
    public void findByNameTest() throws Exception {
        String findByName = userController + "/findByName";

        MvcResult result = mockMvc.perform(get(findByName)
                .accept(MediaType.APPLICATION_JSON)
                .param("name", user.getName()))
        .andExpect(status().isOk())
        .andReturn();

        String content = result.getResponse().getContentAsString();
        User returnUser = mapper.readValue(content, User.class);

        assertTrue(equalsFields(user, returnUser));
    }

    @Test
    public void findAllTest() throws Exception {
        String findAll = userController + "/findAll";

        MvcResult result = mockMvc.perform(get(findAll)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

        String content = result.getResponse().getContentAsString();
        List<User> users = mapper.readValue(content, new TypeReference<List<User>>() {});

        assertNotNull(users);
        assertNotEquals(0, users);
        assertEquals(1, users.stream().filter(i -> i.getId() == user.getId()).count());
    }

    private Boolean equalsFields(User firstUser, User secondUser) {
        return firstUser.getId() == secondUser.getId()
                && firstUser.getName().equals(secondUser.getName());
    }
}