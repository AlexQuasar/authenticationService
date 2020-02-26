package com.alexquasar.authenticationService.web.input;

import com.alexquasar.authenticationService.dto.mailInteraction.DataMail;
import com.alexquasar.authenticationService.entity.Mail;
import com.alexquasar.authenticationService.entity.User;
import com.alexquasar.authenticationService.repository.MailRepository;
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

import java.util.logging.Logger;

import static org.junit.Assert.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthenticationControllerTest {

    MockMvc mockMvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    @Autowired
    MailRepository mailRepository;

    @Autowired
    ObjectMapper mapper;

    Logger log = Logger.getLogger(AuthenticationControllerTest.class.getName());
    String authentication = "/authentication";

    final String testMail = "mail_1@google.com";
    final String tokenHundredYears = "eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJtYWlsXzFAZ29vZ2xlLmNvbSIsImV4cCI6NDczNzg5MTQwNH0.3eIKjn783JMyoAROR5yjXFLJuWM3fRfVrjVTIr73REalQ1ZSfbzdjyPBN5dnBxE1qeT-vc0FJEWEap4Fvx0VMA";

    @Before
    public void setUp() throws Exception {
        ConfigurableMockMvcBuilder builder =
                MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
                        .apply(documentationConfiguration(this.restDocumentation));
        this.mockMvc = builder.build();
    }

    @Test
    @Transactional
    public void registration() throws Exception {
        String registration = this.authentication + "/registration";

        DataMail dataMail = new DataMail("mail_test@gmail.com", "12345", "user_test");

        int expectedMailSize = this.mailRepository.findAll().size() + 1;

        this.mockMvc.perform(post(registration)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(dataMail)))
        .andExpect(status().isOk());

        assertEquals(expectedMailSize, this.mailRepository.findAll().size());
        assertNotNull(this.mailRepository.findByLogin(dataMail.getLogin()));
    }

    @Test
    @Transactional
    public void authorization() throws Exception {
        String authorization = this.authentication + "/authorization";

        Mail mail = this.mailRepository.findByLogin(this.testMail);
        assertNotNull(mail);

        MvcResult result = this.mockMvc.perform(get(authorization)
                .param("login", mail.getLogin())
                .param("password", mail.getPassword()))
        .andExpect(status().isOk())
        .andReturn();

        String content = result.getResponse().getContentAsString();
        assertNotEquals("", content);
    }

    @Test
    @Transactional
    public void getUser() throws Exception {
        String getUser = this.authentication + "/getUser";

        getUser += "/{token}";

        MvcResult result = this.mockMvc.perform(get(getUser, tokenHundredYears)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

        String content = result.getResponse().getContentAsString();
        User user = this.mapper.readValue(content, User.class);

        assertEquals(1, user.getId());
    }

    @Test
    @Transactional
    public void allStepsTest() throws Exception {
        String registration = this.authentication + "/registration";
        String authorization = this.authentication + "/authorization";
        String getUser = this.authentication + "/getUser";

        DataMail dataMail = new DataMail("mail_test@gmail.com", "12345", "user_test");

        this.mockMvc.perform(post(registration)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(dataMail)))
        .andExpect(status().isOk());

        assertNotNull(this.mailRepository.findByLogin(dataMail.getLogin()));

        Mail mail = this.mailRepository.findByLogin(dataMail.getLogin());
        assertNotNull(mail);

        MvcResult resultAuthorization = this.mockMvc.perform(get(authorization)
                .param("login", mail.getLogin())
                .param("password", mail.getPassword()))
        .andExpect(status().isOk())
        .andReturn();

        String token = resultAuthorization.getResponse().getContentAsString();
        getUser += "/{token}";

        MvcResult result = this.mockMvc.perform(get(getUser, token)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

        String content = result.getResponse().getContentAsString();
        User user = this.mapper.readValue(content, User.class);

        assertEquals(mail.getUser().getId(), user.getId());
    }
}