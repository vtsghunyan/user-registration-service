package com.staxter.service;

import com.staxter.userrepository.User;
import com.staxter.userrepository.UserAlreadyExistsException;
import com.staxter.userrepository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class UserRegistrationServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserRegistrationServiceImpl userRegistrationService;

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
    void whenUserDoesNotExist_thenCreateUser() {
        when(userRepository.createUser(Mockito.any())).thenReturn(expectedUser);
        User responseUser = userRegistrationService.createUser(requestUser);

        assertNotNull(responseUser, "Response should not be the null");
        assertTrue(responseUser.getId() != null && !responseUser.getId().isEmpty(), "User ID should be present");
        assertEquals(expectedUser.getUserName(), responseUser.getUserName(), "User name should be the same");
    }

    @Test
    public void whenUserExists_thenThrowException() {
        when(userRepository.createUser(Mockito.any()))
                .thenThrow(new UserAlreadyExistsException("A user with the given username already exists"));

         assertThrows(UserAlreadyExistsException.class,
                () -> userRegistrationService.createUser(requestUser),"Exception should be thrown");
    }
}