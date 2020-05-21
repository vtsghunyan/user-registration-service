package com.staxter.service;

import com.staxter.userrepository.User;
import com.staxter.userrepository.UserAlreadyExistsException;

public interface UserRegistrationService {
    User createUser(User user) throws UserAlreadyExistsException;
}
