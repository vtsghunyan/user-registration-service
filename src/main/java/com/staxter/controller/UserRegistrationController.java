package com.staxter.controller;

import com.staxter.service.ErrorResponse;
import com.staxter.service.UserRegistrationService;
import com.staxter.userrepository.InvalidInputException;
import com.staxter.userrepository.User;
import com.staxter.userrepository.UserAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/userservice/register")
public class UserRegistrationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRegistrationController.class);

    private final UserRegistrationService userRegistrationService;

    @Autowired
    public UserRegistrationController(UserRegistrationService userRegistrationService) {
        this.userRegistrationService = userRegistrationService;
    }

    @PostMapping
    public ResponseEntity create(@RequestBody User user) {
        User createdUser = null;
        try {
            validate(user);
            createdUser = userRegistrationService.createUser(user);
        } catch (InvalidInputException e) {
            LOGGER.warn("Input user has invalid input field");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("INVALID_INPUT_FIELD", e.getMessage()));
        } catch (UserAlreadyExistsException e) {
            LOGGER.warn(String.format("A user with username = '%s' already exists", user.getUserName()));
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("USER_ALREADY_EXISTS", e.getMessage()));
        }
        return ResponseEntity.ok(createdUser);
    }

    private void validate(User user) throws InvalidInputException {
        StringBuilder sb = new StringBuilder();
        boolean invalidInput = false;
        if(user.getFirstName() == null || user.getFirstName().isEmpty()) {
            sb.append("firstName is a required field and should not be null or empty");
            sb.append(System.getProperty("line.separator"));
            invalidInput = true;
        }

        if(user.getLastName() == null || user.getLastName().isEmpty()) {
            sb.append("lastName is a required field and should not be null or empty");
            sb.append(System.getProperty("line.separator"));
            invalidInput = true;
        }

        if(user.getUserName() == null || user.getUserName().isEmpty()) {
            sb.append("userName is a required field and should not be null or empty");
            sb.append(System.getProperty("line.separator"));
            invalidInput = true;
        }

        if(user.getPlainTextPassword() == null || user.getPlainTextPassword().isEmpty()) {
            sb.append("password is a required field and should not be null or empty");
            sb.append(System.getProperty("line.separator"));
            invalidInput = true;
        }

        if(invalidInput) {
            throw new InvalidInputException(sb.toString().trim());
        }
    }
}
