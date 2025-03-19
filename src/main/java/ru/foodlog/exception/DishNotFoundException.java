package ru.foodlog.exception;

public class DishNotFoundException extends ApplicationRuntimeException {

    public DishNotFoundException(String message) {
        super(message);
    }
}
