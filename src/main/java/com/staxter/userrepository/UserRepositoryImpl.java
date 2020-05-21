package com.staxter.userrepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRepositoryImpl.class);

    private static final AtomicInteger ID = new AtomicInteger();
    private final Map<String, User> users;

    public UserRepositoryImpl() {
        users = new HashMap<>();
    }

    @Override
    public User createUser(User user) throws UserAlreadyExistsException {
        if(users.containsKey(user.getUserName())) {
            throw new UserAlreadyExistsException("A user with the given username already exists");
        }

        String id = String.valueOf(ID.incrementAndGet());
        user.setId(id);
        user.setHashedPassword(hash(user.getPlainTextPassword()));
        users.put(user.getUserName(), user);

        return new User(user.getId(), user.getFirstName(), user.getLastName(), user.getUserName());
    }

    private String hash(String password) {
        String hashedPassword = null;
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        try {

            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt);
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            hashedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            LOGGER.warn("Can not hash password");
        }

        return hashedPassword;
    }
}
