package com.employment.task.controllers;

import com.employment.task.TaskApplication;
import com.employment.task.models.User;
import com.employment.task.properties.UserConstraints;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TaskApplication.class},
        properties = "spring.config.location=classpath:test.properties")
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ContextConfiguration(initializers = {PreparingTable.class})
class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Autowired
    private UserConstraints userConstraints;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @Test
    void createUser_ValidUser_ReturnsCreated() throws Exception {
        User user = new User("test@example.com", "John", "Doe", "2000-01-01", "Address", "1234567890");
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
                        .characterEncoding("utf-8"))
                .andExpect(status().isCreated());
    }

    @Test
    void createUser_InvalidUser_ThrowsBadRequest_EmptyFields() throws Exception {
        User user = new User("", "      ", "", "", "", "");
        MvcResult result = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String responseBody = result.getResponse().getContentAsString();
        Assertions.assertEquals(5, responseBody.split("\n").length);
    }

    @Test
    void createUser_InvalidUser_ThrowsBadRequest_NotAdultUser() throws Exception {
        User user = new User("test@example.com", "John", "Doe", "2010-01-01", "Address", "1234567890");
        MvcResult result = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String expected = "User must be at least " + userConstraints.getMinAge() + " years old";
        Assertions.assertEquals(expected, result.getResponse().getContentAsString());
    }

    @Test
    void createUser_InvalidUser_ThrowsBadRequest_NotValidEmail() throws Exception {
        User user = new User("testexample.com", "John", "Doe", "1999-01-01", "Address", "1234567890");
        MvcResult result = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andReturn();
        Assertions.assertEquals("Invalid email format", result.getResponse().getContentAsString());
    }

    @Test
    void createUser_InvalidUser_ThrowsBadRequest_EmailIsAlreadyTaken() throws Exception {
        User user = new User("user5@example.com", "John", "Doe", "1999-01-01", "Address", "1234567890");
        MvcResult result = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andReturn();
        Assertions.assertEquals("Email is already in use", result.getResponse().getContentAsString());
    }

    @Test
    void createUser_InvalidUser_ThrowsBadRequest_IncorrectLocalDatePattern() throws Exception {
        User user = new User("test1@example.com", "John", "Doe", "199a-aa-a1", "Address", "1234567890");
        MvcResult result = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andReturn();
        Assertions.assertEquals("birth_date must be \"yyyy-MM-dd\"", result.getResponse().getContentAsString());
    }

    @Test
    void updateUserField_ValidField_ReturnsOk() throws Exception {
        Map<String, Object> fields = new HashMap<>();
        fields.put("email", "test@ewwxample.com");
        fields.put("first_name", "ban");
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/users/7")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fields)))
                .andExpect(status().isOk());
    }

    @Test
    void updateUserField_InvalidField_ThrowsBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"invalid_field\": \"test@example.com\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUser_ValidUser_ReturnsOk() throws Exception {
        User user = new User("testsss@example.com", "John", "Doe", "1990-01-01", "Address1", "4567890");
        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/7")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());
    }

    @Test
    void searchUsers_ByBirthDateRange_ValidDates_ReturnsOk() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/users/search")
                        .param("from", "1993-01-01")
                        .param("to", "1996-12-31"))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(result.getResponse().getContentAsString());
        JsonNode body = objectMapper.readTree(result.getResponse().getContentAsString());
        Assertions.assertEquals(2, body.size());

    }

    @Test
    void searchUsers_ByBirthDateRange_InvalidDates_ThrowsBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/search")
                        .param("from", "2025-01-01")
                        .param("to", "2024-12-31"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteUser_ValidId_ReturnsOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUser_InvalidId_ThrowsNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/999"))
                .andExpect(status().isBadRequest());
    }
}

