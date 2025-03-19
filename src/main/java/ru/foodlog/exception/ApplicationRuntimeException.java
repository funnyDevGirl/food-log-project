package ru.foodlog.exception;

public class ApplicationRuntimeException extends RuntimeException {

    public ApplicationRuntimeException(String message) {
        super(message);
    }
}
