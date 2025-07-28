package com.AgroLink.User.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String user) {
        super("User with User Name '" + user + "' was not found.");
    }
}

