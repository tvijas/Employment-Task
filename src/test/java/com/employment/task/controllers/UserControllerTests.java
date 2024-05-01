package com.employment.task.controllers;

import com.employment.task.TaskApplication;
import com.employment.task.models.User;
import com.employment.task.properties.UserConstraints;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TaskApplication.class})
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
    @Order(1)
    void createUser_ValidUser_ReturnsCreated() throws Exception {
        User user = new User("test@example.com", "John", "Doe", "2000-01-01", "Address", "1234567890");
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
                        .characterEncoding("utf-8"))
                .andExpect(status().isCreated());
    }
    @Test
    @Order(2)
    void createUser_InvalidUser_ThrowsBadRequest_EmptyFields() throws Exception {
        User user = new User("", "      ", "", LocalDate.of(2010, 1, 1), "Address", "1234567890");
        MvcResult result = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String responseBody = result.getResponse().getContentAsString();
        Assertions.assertEquals(3, responseBody.split("\n").length);
    }
    @Test
    @Order(3)
    void createUser_InvalidUser_ThrowsBadRequest_NotAdultUser() throws Exception {
        User user = new User("test@example.com", "John", "Doe", LocalDate.of(2010, 1, 1), "Address", "1234567890");
        MvcResult result = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String expected = "User must be at least " + userConstraints.getMinAge() + " years old";
        Assertions.assertEquals(expected, result.getResponse().getContentAsString());
    }

    @Test
    @Order(4)
    void createUser_InvalidUser_ThrowsBadRequest_NotValidEmail() throws Exception {
        User user = new User("testexample.com", "John", "Doe", LocalDate.of(1999, 1, 1), "Address", "1234567890");
        MvcResult result = mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andReturn();
        Assertions.assertEquals("Invalid email format", result.getResponse().getContentAsString());
    }

    @Test
    @Order(5)
    void updateUserField_ValidField_ReturnsOk() throws Exception {
        Map<String, Object> fields = new HashMap<>();
        fields.put("email", "test@ewwxample.com");
        fields.put("first_name", "ban");
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fields)))
                .andExpect(status().isOk());
    }

    @Test
    @Order(6)
    void updateUserField_InvalidField_ThrowsBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"invalid_field\": \"test@example.com\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(7)
    void updateUser_ValidUser_ReturnsOk() throws Exception {
        User user = new User("testsss@example.com", "John", "Doe", LocalDate.of(1990, 1, 1), "Address1", "4567890");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());
    }

    @Test
    @Order(8)
    void updateUser_InvalidUser_ThrowsBadRequest() throws Exception {
        User user = new User("", "John", "Doe", LocalDate.of(2010, 1, 1), "Address", "1234567890");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }


    @Test
    @Order(9)
    void searchUsersByBirthDateRange_ValidDates_ReturnsOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/search")
                        .param("from", "2024-01-01")
                        .param("to", "2024-12-31"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(10)
    void searchUsersByBirthDateRange_InvalidDates_ThrowsBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/search")
                        .param("from", "2025-01-01")
                        .param("to", "2024-12-31"))
                .andExpect(status().isBadRequest());
    }
    @Test
    @Order(11)
    void deleteUser_ValidId_ReturnsOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUser_InvalidId_ThrowsNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/999"))
                .andExpect(status().isNotFound());
    }
}

