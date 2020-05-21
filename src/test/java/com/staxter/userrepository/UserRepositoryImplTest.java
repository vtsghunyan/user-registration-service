package com.staxter.userrepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class UserRepositoryImplTest {

    private UserRepository userRepository;

    private User requestUser;

    @BeforeEach
    public void init() {
        userRepository = new UserRepositoryImpl();

        requestUser = new User();
        requestUser.setFirstName("FirstName");
        requestUser.setLastName("LastName");
        requestUser.setUserName("UserName");
        requestUser.setPlainTextPassword("PlainTextPassword");
    }

    @Test
    public void whenUserDoesNotExist_thenCreateUser() {
        User responseUser = userRepository.createUser(requestUser);

        assertNotNull(responseUser, "Response should not be the null");
        assertTrue(responseUser.getId() != null && !responseUser.getId().isEmpty(), "User ID should be present");
        assertEquals(responseUser.getUserName(), responseUser.getUserName(), "User name should be the same");
    }

    @Test
    public void whenUserExists_thenThrowException() {
        userRepository.createUser(requestUser);

        assertThrows(UserAlreadyExistsException.class,
                () -> userRepository.createUser(requestUser),"Exception should be thrown");
    }
}