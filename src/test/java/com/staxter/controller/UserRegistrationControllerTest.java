package com.staxter.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.staxter.service.ErrorResponse;
import com.staxter.service.UserRegistrationService;
import com.staxter.userrepository.User;
import com.staxter.userrepository.UserAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserRegistrationController.class)
public class UserRegistrationControllerTest {

    @MockBean
    private UserRegistrationService userRegistrationService;

    @Autowired
    private MockMvc mockMvc;

    private User requestUser;
    private User expectedUser;

    @BeforeEach
    public void init() {
        requestUser = new User();
        requestUser.setFirstName("FirstName");
        requestUser.setLastName("LastName");
        requestUser.setUserName("UserName");
        requestUser.setPlainTextPassword("PlainTextPassword");

        expectedUser = new User("id", "FirstName", "LastName", "UserName");
    }

    @Test
    public void whenUserDoesNotExist_thenCreateUser() throws Exception {
        when(userRegistrationService.createUser(Mockito.any())).thenReturn(expectedUser);

        String inputJson = mapToJson(requestUser);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/userservice/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(HttpStatus.OK.value(), status, "Response status should be 200");

        String content = mvcResult.getResponse().getContentAsString();
        User responseUser = mapFromJson(content, User.class);

        assertEquals(expectedUser.getUserName(), responseUser.getUserName(), "User name should be the same");
    }

    @Test
    public void whenUserExists_thenReturnErrorMessage() throws Exception {
        when(userRegistrationService.createUser(Mockito.any()))
                .thenThrow(new UserAlreadyExistsException("A user with the given username already exists"));

        String inputJson = mapToJson(requestUser);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/userservice/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(HttpStatus.CONFLICT.value(), status, "Response status should be 409");

        String content = mvcResult.getResponse().getContentAsString();
        ErrorResponse errorResponse = mapFromJson(content, ErrorResponse.class);

        assertEquals("USER_ALREADY_EXISTS", errorResponse.getCode());
    }

    @Test
    public void whenInvalidInput_thenReturnErrorMessage() throws Exception {
        when(userRegistrationService.createUser(Mockito.any())).thenReturn(expectedUser);

        requestUser.setPlainTextPassword("");
        String inputJson = mapToJson(requestUser);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/userservice/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(HttpStatus.BAD_REQUEST.value(), status, "Response status should be 400");

        String content = mvcResult.getResponse().getContentAsString();
        ErrorResponse errorResponse = mapFromJson(content, ErrorResponse.class);

        assertEquals("INVALID_INPUT_FIELD", errorResponse.getCode());
    }

    private String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    private <T> T mapFromJson(String json, Class<T> clazz) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, clazz);
    }
}