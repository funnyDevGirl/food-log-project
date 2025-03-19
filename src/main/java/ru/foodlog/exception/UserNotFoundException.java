package ru.foodlog.exception;

public class UserNotFoundException extends ApplicationRuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
