package com.staxter.userrepository;

public interface UserRepository {
    User createUser( User user ) throws UserAlreadyExistsException;
}
